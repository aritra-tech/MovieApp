package com.aritradas.movieapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.aritradas.movieapp.di.appModule
import com.aritradas.movieapp.ui.navigation.Navigation
import com.aritradas.movieapp.ui.theme.MovieAppTheme
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext
import org.koin.core.context.startKoin

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        if (GlobalContext.getOrNull() == null) {
            startKoin {
                androidContext(this@MainActivity)
                modules(appModule)
            }
        }
        
        enableEdgeToEdge()
        setContent {
            MovieAppTheme {
                Navigation()
            }
        }
    }
}