package ru.otus.basicarchitecture.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import jakarta.inject.Inject
import ru.otus.basicarchitecture.R
import ru.otus.basicarchitecture.view_model.WizardCache

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var wizardCache: WizardCache

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}