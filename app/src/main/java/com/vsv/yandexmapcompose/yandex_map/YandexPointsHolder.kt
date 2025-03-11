package com.vsv.yandexmapcompose.yandex_map

import com.yandex.mapkit.map.PlacemarkMapObject

class YandexPointsHolder() {
    private val mapObjects = mutableMapOf<String, PlacemarkMapObject>()
    private var selectedMarker: String? = null

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
}