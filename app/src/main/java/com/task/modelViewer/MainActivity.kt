package com.task.modelViewer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.task.modelViewer.screens.ModelViewerScreen
import com.task.modelViewer.ui.theme.ModelViewerTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * Single Activity - entire app runs here.
 * No fragments, no navigation needed.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ModelViewerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.Black
                ) {
                    ModelViewerScreen()
                }
            }
        }
    }
}
