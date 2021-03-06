package br.com.pedrotlf.desafioshippmobile.utils

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.Manifest.permission.ACCESS_WIFI_STATE
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat

open class BaseActivity: AppCompatActivity() {
    private val REQUEST_CODE_LOCATION_PERMISSION = 6283
    private val LOCATION_DENIED = 6363
    val TAG: String
        get() = this.javaClass.simpleName

    fun requestLocationPermission(callback: (()->Unit)? = {}){
        if (callback != null) {
            onLocationPermissionCallback = callback
        }
        ActivityCompat.requestPermissions( this, arrayOf(
            ACCESS_FINE_LOCATION,
            ACCESS_WIFI_STATE
        ), REQUEST_CODE_LOCATION_PERMISSION)
    }

    fun checkLocationPermission(): Boolean{
        return (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, ACCESS_WIFI_STATE) == PackageManager.PERMISSION_GRANTED)
    }

    private var onLocationPermissionCallback: ()->Unit = {}
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode == REQUEST_CODE_LOCATION_PERMISSION){
            if(grantResults.any{it == PackageManager.PERMISSION_DENIED}) {
                setResult(LOCATION_DENIED)
                finish()
            } else if (grantResults.any{it == PackageManager.PERMISSION_GRANTED}){
                onLocationPermissionCallback()
            }
        }
    }

    override fun onActivityReenter(resultCode: Int, data: Intent?) {
        super.onActivityReenter(resultCode, data)
        if(resultCode == LOCATION_DENIED)
            finish()
    }
}