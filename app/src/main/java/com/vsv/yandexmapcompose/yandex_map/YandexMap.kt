package com.vsv.yandexmapcompose.yandex_map

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.vsv.yandexmapcompose.R
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraListener
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.CameraUpdateReason
import com.yandex.mapkit.map.Map
import com.yandex.mapkit.map.Map.CameraCallback
import com.yandex.mapkit.map.MapObject
import com.yandex.mapkit.map.MapObjectTapListener
import com.yandex.mapkit.map.PlacemarkMapObject
import com.yandex.mapkit.mapview.MapView
import com.yandex.runtime.image.ImageProvider
import java.util.UUID

@Composable
fun YandexMap(
    points: List<YandexPoint>,
    onPointClick: (YandexPoint) -> Unit,
    initialPosition: CameraPosition = CameraPosition(
        Point(
            points.firstOrNull { it.isSelected }?.latitude
                ?: points.first().latitude,
            points.firstOrNull { it.isSelected }?.longitude
                ?: points.first().longitude
        ), 10f, 0f, 0f
    ),
    onMapTouched: (Boolean) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val pointsHolder = remember { YandexPointsHolder(context) }
    val mapView = remember { MapView(context) }
    val p by remember { mutableStateOf(mapView.mapWindow.map.mapObjects.addCollection()) }
    var currentZoom by remember { mutableFloatStateOf(10f) }

    val cameraCallback = object : CameraCallback {
        override fun onMoveFinished(p0: Boolean) {}
    }

    val mapObjectTapListener = object : MapObjectTapListener {
        override fun onMapObjectTap(p0: MapObject, p1: Point): Boolean {
            val data = (p0 as PlacemarkMapObject).userData as YandexPoint
            pointsHolder.selectMarker(
                data.id,
                data.selectedIcon,
                data.unselectedIcon
            )
            onPointClick(data)
            return true
        }
    }

    LaunchedEffect(points) {
        p.clear()
        pointsHolder.clear()
        points.forEach { point ->
            val marker = p.addPlacemark().apply {
                geometry = Point(
                    point.latitude,
                    point.longitude
                )
                userData = point
                setIcon(
                    ImageProvider.fromResource(
                        context,
                        if (point.isSelected) point.selectedIcon else point.unselectedIcon
                    ),
                )
                zIndex = if (point.isSelected) 2f else 0f
                addTapListener(mapObjectTapListener)
            }
            pointsHolder.putMarker(point.id, marker)
            if (point.isSelected) pointsHolder.setSelectedMarker(point.id)
        }
        pointsHolder.getSelectedMarker()?.let { marker ->
            mapView.mapWindow.map.move(
                CameraPosition(marker.geometry, currentZoom, 0f, 0f),
                Animation(Animation.Type.LINEAR, 0.5f),
                cameraCallback
            )
        }
    }

    YandexMapImpl(
        mapView = mapView,
        initialPosition = initialPosition,
        onMapMove = { c, b ->
            currentZoom = c.zoom
            onMapTouched(b)
        },
        modifier = modifier
    )
}

@Composable
private fun YandexMapImpl(
    mapView: MapView,
    initialPosition: CameraPosition,
    onMapMove: (CameraPosition, Boolean) -> Unit,
    modifier: Modifier
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraListener = object : CameraListener {
        override fun onCameraPositionChanged(
            p0: Map,
            p1: CameraPosition,
            p2: CameraUpdateReason,
            p3: Boolean
        ) {
            onMapMove(p1, p3)
        }
    }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_START -> {
                    MapKitFactory.getInstance().onStart()
                    mapView.onStart()
                }
                Lifecycle.Event.ON_STOP -> {
                    MapKitFactory.getInstance().onStop()
                    mapView.onStop()
                }
                else -> Unit
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
    AndroidView(
        factory = {
            mapView.apply {
                mapWindow.map.isRotateGesturesEnabled = false
                mapWindow.map.apply {
                    move(initialPosition)
                    addCameraListener(cameraListener)
                }
            }
        },
        modifier = modifier
    )
}

data class YandexPoint(
    val latitude: Double,
    val longitude: Double,
    val isSelected: Boolean,
    val id: String = UUID.randomUUID().toString(),
    @DrawableRes val selectedIcon: Int = R.drawable.ic_map_point_red,
    @DrawableRes val unselectedIcon: Int = R.drawable.ic_map_point_black
)
