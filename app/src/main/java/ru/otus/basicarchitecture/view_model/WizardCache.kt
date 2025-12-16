package ru.otus.basicarchitecture.view_model

import android.util.Log
import dagger.hilt.android.scopes.ActivityScoped
import jakarta.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.otus.basicarchitecture.model.Address
import ru.otus.basicarchitecture.model.UserData


@ActivityScoped
interface WizardCache : AutoCloseable {

    val userData: UserData

    class Impl @Inject constructor(
        nameFragmentModel: NameFragmentModel,
        addressFragmentModel: AddressFragmentModel,
    ) : WizardCache {

        private var name: String = ""
        private var surname: String = ""
        private var birthDate: Long = 0
        private var address: Address = Address()
        private var tags: List<String> = emptyList()

        private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

        override val userData: UserData
            get() = UserData(
                name,
                surname,
                birthDate,
                address,
                tags
            )

        init {
            with(nameFragmentModel) {
                nameFlow.onEach {
                    if (it.isValid) {
                        this@Impl.name = it.fValue
                        Log.i("CACHE", "Name: $name")
                    }
                }.launchIn(scope)

                surnameFlow.onEach { surname ->
                    if (surname.isValid) {
                        this@Impl.surname = surname.fValue
                        Log.i("CACHE", "Surname: $surname")
                    }
                }.launchIn(scope)

                birthDateFlow.onEach { birthDate ->
                    if (birthDate.isValid) {
                        this@Impl.birthDate = birthDate.fValue
                        Log.i("CACHE", "BirthDate: $birthDate")
                    }
                }.launchIn(scope)
            }

            addressFragmentModel.addressFlow.onEach {
                address = it
                Log.i("CACHE", "Address: $address")

            }.launchIn(scope)

        }

        override fun close() {
            scope.cancel()
        }
    }
}
