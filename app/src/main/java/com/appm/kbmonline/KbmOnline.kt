package com.appm.kbmonline

import android.app.Application
import com.mapbox.mapboxsdk.Mapbox
import timber.log.Timber

class KbmOnline : Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())

        }

        Mapbox.getInstance(applicationContext, getString(R.string.token_mapbox))

    }


}