package com.vsv.yandexmapcompose.yandex_map

import android.content.Context
import androidx.annotation.DrawableRes
import com.yandex.mapkit.map.PlacemarkMapObject
import com.yandex.runtime.image.ImageProvider
import kotlin.collections.get

class YandexPointsHolder(
    private val context: Context
) {
    private val mapObjects = mutableMapOf<String, PlacemarkMapObject>()
    private var selectedMarker: String? = null

    private fun Int.iconToImageProvider(): ImageProvider {
        return ImageProvider.fromResource(context, this)
    }

    fun putMarker(id: String, marker: PlacemarkMapObject) {
        mapObjects[id] = marker
    }

    fun setSelectedMarker(id: String) {
        selectedMarker = id
    }

    fun getSelectedMarker(): PlacemarkMapObject? = mapObjects[selectedMarker]

    fun clear() {
        mapObjects.clear()
        selectedMarker = null
    }

    fun selectMarker(
        id: String,
        @DrawableRes selectedIcon: Int,
        @DrawableRes unselectedIcon: Int
    ) {
        mapObjects[selectedMarker]?.apply {
            setIcon(unselectedIcon.iconToImageProvider())
            zIndex = 0f
        }
        mapObjects[id]?.apply {
            setIcon(selectedIcon.iconToImageProvider())
            zIndex = 2f
        }
        selectedMarker = id
    }
}