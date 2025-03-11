package com.vsv.yandexmapcompose

import android.app.Application
import com.yandex.mapkit.MapKitFactory

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        //set your API key
        MapKitFactory.setApiKey(BuildConfig.YANDEX_MAPKIT_API_KEY)
    }
}