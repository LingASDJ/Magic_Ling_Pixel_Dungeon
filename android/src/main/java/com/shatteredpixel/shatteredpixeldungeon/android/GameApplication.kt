package com.shatteredpixel.shatteredpixeldungeon.android;

import android.app.Application
import android.content.Context
import cat.ereza.customactivityoncrash.config.CaocConfig

class GameApplication : Application() {

//    companion object {
//        val appId = "61853bf9e0f9bb492b4f7eba"
//        val channel = "Umeng"
//    }
    override fun onCreate() {
        super.onCreate()
//        UMConfigure.preInit(this, appId, channel)
        CaocConfig.Builder.create()
            .backgroundMode(CaocConfig.BACKGROUND_MODE_SILENT) //default: CaocConfig.BACKGROUND_MODE_SHOW_CUSTOM
            .enabled(true) //default: true
            .backgroundMode(CaocConfig.BACKGROUND_MODE_CRASH) //default: CaocConfig.BACKGROUND_MODE_SHOW_CUSTOM
            .enabled(true) //default: true
            .backgroundMode(CaocConfig.BACKGROUND_MODE_SHOW_CUSTOM) //default: CaocConfig.BACKGROUND_MODE_SHOW_CUSTOM
            .enabled(true) //default: true
            .showErrorDetails(true) //default: true
            .showRestartButton(true) //default: true
            .logErrorOnRestart(true) //default: true
            .trackActivities(false) //default: false
            .minTimeBetweenCrashesMs(2000) //default: 3000
            //.errorDrawable(R.drawable.customactivityoncrash_error_image) //default: bug image
            //
            .apply()
    }


}