package ru.otus.basicarchitecture.view_model

import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import ru.otus.basicarchitecture.use_case.AddressSuggestUseCase
import ru.otus.basicarchitecture.view.LoadAddressesViewState

/**
 * Тесты для AddressFragmentModel
 *
 * Проверяют:
 * - Обновление адреса
 * - Очистку вариантов
 * - Выбор адреса
 * - Загрузку вариантов адресов (успешный и ошибочный сценарии)
 *
 * Особенности:
 * - Использует mockk для мокирования зависимостей
 * - Проверяет промежуточные состояния (LoadingProgress)
 * - Использует задержку для перехвата промежуточных состояний
 */

@OptIn(ExperimentalCoroutinesApi::class)
class AddressFragmentModelTest {

    private val testDispatcher = StandardTestDispatcher()

    @MockK
    private lateinit var addressSuggestUseCase: AddressSuggestUseCase
    private lateinit var wizardCache: WizardCache.Impl
    private lateinit var viewModel: AddressFragmentModel

    @Before
    fun setUp() {
        // Устанавливаем тестовый диспетчер как основной
        Dispatchers.setMain(testDispatcher)

        // Инициализируем моки, помеченные аннотацией @MockK
        MockKAnnotations.init(this)

        // Создаем зависимости
        wizardCache = WizardCache.Impl()

        // Создаем ViewModel с мокированными зависимостями
        viewModel = AddressFragmentModel(addressSuggestUseCase, wizardCache)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun updateAddress_emits_new_address_when_it_is_not_blank_and_different_from_current() =
        runTest {
            val newAddress = "New Address"

            val result = viewModel.updateAddress(newAddress)
            advanceUntilIdle()

            assertTrue(result)
            assertEquals(newAddress, viewModel.addressFlow.first())
            assertEquals(newAddress, wizardCache.address)
        }

    @Test
    fun updateAddress_returns_false_when_address_is_blank() = runTest {
        val blankAddress = ""

        val result = viewModel.updateAddress(blankAddress)

        assertFalse(result)
        assertEquals("", viewModel.addressFlow.first())
    }

    @Test
    fun updateAddress_returns_false_when_address_is_the_same_as_current() = runTest {
        val sameAddress = "Same Address"
        viewModel.updateAddress(sameAddress) // Устанавливаем текущий адрес

        val result = viewModel.updateAddress(sameAddress)

        assertFalse(result)
    }

    @Test
    fun clearVariants_emits_LoadAddresses_state() = runTest {
        val initialState = viewModel.state.first()
        assertTrue(initialState is LoadAddressesViewState.LoadAddresses)

        viewModel.clearVariants()

        val newState = viewModel.state.first()
        assertTrue(newState is LoadAddressesViewState.LoadAddresses)
        assertNull((newState as LoadAddressesViewState.LoadAddresses).error)
    }

    @Test
    fun addressSelected_emits_AddressSelected_state() = runTest {
        val initialState = viewModel.state.first()
        assertTrue(initialState is LoadAddressesViewState.LoadAddresses)

        viewModel.addressSelected()

        val newState = viewModel.state.first()
        assertTrue(newState is LoadAddressesViewState.AddressSelected)
    }

    @Test
    fun loadAddressVariants_emits_LoadingProgress_then_Content_on_success() = runTest {

        // Дано
        val rawAddress = "Test Address"
        val expectedVariants = listOf("Variant 1", "Variant 2")

        // Мокируем ответ use case с задержкой для перехвата промежуточного состояния
        coEvery { addressSuggestUseCase.findAddress(rawAddress) } coAnswers {
            kotlinx.coroutines.delay(100)
            expectedVariants
        }

        val states = mutableListOf<LoadAddressesViewState>()

        // Подписываемся на изменения состояния
        val job = viewModel.state.onEach { state ->
            states.add(state)
        }.launchIn(this)


        viewModel.loadAddressVariants(rawAddress)

        // Ждем завершения корутины
        advanceUntilIdle()

        // Отменяем подписку
        job.cancel()


        // Проверяем что состояния пришли в правильном порядке
        // Может быть 2 или 3 состояния из-за начального состояния
        assertTrue("Ожидалось 2 или 3 состояния, но было ${states.size}", states.size in 2..3)

        // Находим индекс LoadingProgress состояния
        val loadingProgressIndex =
            states.indexOfFirst { it is LoadAddressesViewState.LoadingProgress }
        val contentIndex = states.indexOfFirst { it is LoadAddressesViewState.Content }

        // Проверяем что LoadingProgress было перед Content
        assertTrue("LoadingProgress должно быть перед Content", loadingProgressIndex < contentIndex)

        // Проверяем что последнее состояние - Content
        assertTrue(
            "Последнее состояние должно быть Content, но было ${states.last().javaClass.simpleName}",
            states.last() is LoadAddressesViewState.Content
        )

        // Проверяем финальное состояние
        coVerify(exactly = 1) { addressSuggestUseCase.findAddress(rawAddress) }
        confirmVerified(addressSuggestUseCase)

        val contentState = states.last() as LoadAddressesViewState.Content
        assertEquals("Ожидаемые варианты не совпадают", expectedVariants, contentState.addresses)
    }

    @Test
    fun loadAddressVariants_emits_LoadingProgress_then_Error_on_failure() = runTest {

        // Дано
        val rawAddress = "Test Address"
        val testException = Exception("Network error")

        // Мокируем ошибку в use case с задержкой для перехвата промежуточного состояния
        coEvery { addressSuggestUseCase.findAddress(rawAddress) } coAnswers {
            kotlinx.coroutines.delay(100)
            throw testException
        }
        val states = mutableListOf<LoadAddressesViewState>()

        // Подписываемся на изменения состояния
        val job = viewModel.state.onEach { state ->
            states.add(state)
        }.launchIn(this)


        viewModel.loadAddressVariants(rawAddress)

        // Ждем завершения корутины
        advanceUntilIdle()

        // Отменяем подписку
        job.cancel()


        // Проверяем что состояния пришли в правильном порядке
        // Может быть 2 или 3 состояния из-за начального состояния
        assertTrue("Ожидалось 2 или 3 состояния, но было ${states.size}", states.size in 2..3)

        // Находим индекс LoadingProgress состояния
        val loadingProgressIndex =
            states.indexOfFirst { it is LoadAddressesViewState.LoadingProgress }
        val errorIndex =
            states.indexOfFirst { it is LoadAddressesViewState.LoadAddresses && it.error != null }

        // Проверяем что LoadingProgress было перед состоянием с ошибкой
        assertTrue(
            "LoadingProgress должно быть перед состоянием с ошибкой",
            loadingProgressIndex < errorIndex
        )

        // Проверяем что последнее состояние - LoadAddresses с ошибкой
        val lastState = states.last()
        assertTrue(
            "Последнее состояние должно быть LoadAddresses с ошибкой, но было ${lastState.javaClass.simpleName}",
            lastState is LoadAddressesViewState.LoadAddresses && lastState.error != null
        )

        // Проверяем финальное состояние
        coVerify(exactly = 1) { addressSuggestUseCase.findAddress(rawAddress) }
        confirmVerified(addressSuggestUseCase)

        val errorState = lastState as LoadAddressesViewState.LoadAddresses
        assertEquals(
            "Ожидаемая ошибка не совпадает",
            testException.message,
            errorState.error?.message
        )
    }
}