package com.vsv.yandexmapcompose

import androidx.lifecycle.ViewModel
import com.vsv.yandexmapcompose.yandex_map.YandexPoint
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MainViewModel: ViewModel() {

    private val _state = MutableStateFlow<MainState>(MainState())
    val state = _state.asStateFlow()

    fun onPointClick(point: YandexPoint) {
        val list = _state.value.points.toMutableList()
        list.forEachIndexed { index, p ->
            if (index == list.indexOf(point)) {
                list[index] = p.copy(isSelected = true)
            } else {
                list[index] = p.copy(isSelected = false)
            }
        }
        _state.update { it.copy(points = list) }
    }
}

data class MainState(
    val points: List<YandexPoint> = listOf(
        YandexPoint(latitude = 55.0, longitude = 37.0, isSelected = false),
        YandexPoint(latitude = 55.0, longitude = 37.1, isSelected = false),
        YandexPoint(latitude = 55.0, longitude = 37.2, isSelected = false),
        YandexPoint(latitude = 55.1, longitude = 37.0, isSelected = false),
        YandexPoint(latitude = 55.1, longitude = 37.1, isSelected = false),
        YandexPoint(latitude = 55.1, longitude = 37.2, isSelected = false),
        YandexPoint(latitude = 55.2, longitude = 37.0, isSelected = false),
        YandexPoint(latitude = 55.2, longitude = 37.1, isSelected = false),
        YandexPoint(latitude = 55.2, longitude = 37.2, isSelected = false),
    )
)

