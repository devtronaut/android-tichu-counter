package com.application.android_tichu_counter

import android.app.Application
import android.util.Log

/**
 * Entry-point class for the application.
 * Offers singleton instance of the application.
 *
 * @author Devtronaut
 */
class TichuApplication : Application() {
    lateinit var appComponent: ApplicationComponent

    companion object {
        private const val TAG = "Application"

        // Single application instance
        private lateinit var instance: TichuApplication

        /**
         * Get single instance of the application.
         *
         * @return instance of the application
         */
        // Required for PreferenceUtils to create the preferences for the application
        @Synchronized
        fun getInstance(): TichuApplication {
            return instance
        }
    }

    // Extend function to instantiate the single instance of the application
    override fun onCreate() {
        super.onCreate()
        instance = this
        this.appComponent = this.initDagger()

        Log.d(TAG, "Initialized application instance.")
    }

    private fun initDagger() = DaggerApplicationComponent.builder()
        .appModule(AppModule(this))
        .build()
}