package ru.otus.basicarchitecture.view_model

import dagger.hilt.android.scopes.ActivityScoped
import jakarta.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import ru.otus.basicarchitecture.model.Interest
import ru.otus.basicarchitecture.model.UserData


@ActivityScoped
interface WizardCache : AutoCloseable {

    val userData: UserData

    var name: String
    var surname: String
    var birthDate: Long
    var address: String
    var tags: Set<Interest>

    class Impl @Inject constructor() : WizardCache {

        override var name: String = ""
        override var surname: String = ""
        override var birthDate: Long = 0

        override var address: String = ""

        override var tags: Set<Interest> = emptySet()

        private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

        override val userData: UserData
            get() = UserData(
                name,
                surname,
                birthDate,
                address,
                tags
            )

        override fun close() {
            scope.cancel()
        }
    }
}
