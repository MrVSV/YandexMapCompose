package com.vsv.yandexmapcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vsv.yandexmapcompose.ui.theme.YandexMapComposeTheme
import com.vsv.yandexmapcompose.yandex_map.YandexMap
import com.yandex.mapkit.MapKitFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MapKitFactory.initialize(this)
        val viewModel = MainViewModel()
        enableEdgeToEdge()
        setContent {
            val state = viewModel.state.collectAsStateWithLifecycle().value
            YandexMapComposeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    YandexMap(
                        points = state.points,
                        onPointClick = { point ->
                            viewModel.onPointClick(point)
                        },
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    )

                }
            }
        }
    }
}
