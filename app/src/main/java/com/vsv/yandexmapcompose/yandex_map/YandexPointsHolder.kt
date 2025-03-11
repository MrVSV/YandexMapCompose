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

    fun putMarker(address: String, marker: PlacemarkMapObject) {
        mapObjects[address] = marker
    }

    fun setSelectedMarker(address: String) {
        selectedMarker = address
    }

    fun getSelectedMarker(): PlacemarkMapObject? = mapObjects[selectedMarker]

    fun clear() {
        mapObjects.clear()
        selectedMarker = null
    }

    fun selectMarker(
        address: String,
        @DrawableRes selectedIcon: Int,
        @DrawableRes unselectedIcon: Int
    ) {
        mapObjects[selectedMarker]?.apply {
            setIcon(unselectedIcon.iconToImageProvider())
            zIndex = 0f
        }
        mapObjects[address]?.apply {
            setIcon(selectedIcon.iconToImageProvider())
            zIndex = 2f
        }
        selectedMarker = address
    }
}