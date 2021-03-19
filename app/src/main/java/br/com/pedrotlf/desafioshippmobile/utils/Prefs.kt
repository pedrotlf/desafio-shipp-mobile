package br.com.pedrotlf.desafioshippmobile.utils

import android.content.Context
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.RectangularBounds
import org.jetbrains.anko.AnkoLogger

class Prefs(context: Context): AnkoLogger {
//    private val PREFS_FILENAME = "br.com.pedrotlf.desafioshippmobile"
//
//    private val KEY_CURRENT_LOCATION = "current_location"
//    private val prefs = context.getSharedPreferences(PREFS_FILENAME, Context.MODE_PRIVATE)
//
//    var currentLocation: MyLatLng?
//        get() {
//            val json = prefs.getString(KEY_CURRENT_LOCATION, "")
//            return if (json?.isNotEmpty() == true)
//                ObjectMapper().registerKotlinModule().readValue(json, MyLatLng::class.java)
//            else null
//        }
//        set(value) {
//            if (value != null)
//                prefs.edit().putString(KEY_CURRENT_LOCATION, ObjectMapper().registerKotlinModule().writeValueAsString(value)).apply()
//            else
//                prefs.edit().remove(KEY_CURRENT_LOCATION).apply()
//        }
}