package ru.otus.basicarchitecture.view_model

import dagger.hilt.android.scopes.ActivityScoped
import jakarta.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
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
        private var country: String = ""
        private var city: String = ""
        private var address: String = ""
        private var tags: List<String> = emptyList()

        private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

        override val userData: UserData
            get() = UserData(
                name,
                surname,
                birthDate,
                country,
                city,
                address,
                tags
            )

        init {
            nameFragmentModel.name
                .onEach { name ->
                    if (name.isValid) this.name = name.fValue
                }
                .launchIn(scope)

            nameFragmentModel.surname
                .onEach { surname ->
                    if (surname.isValid) this.surname = surname.fValue
                }
                .launchIn(scope)

            nameFragmentModel.birthDate
                .onEach { birthDate ->
                    if (birthDate.isValid) this.birthDate = birthDate.fValue
                }
                .launchIn(scope)

        }

        override fun close() {
            scope.cancel()
        }
    }
}
