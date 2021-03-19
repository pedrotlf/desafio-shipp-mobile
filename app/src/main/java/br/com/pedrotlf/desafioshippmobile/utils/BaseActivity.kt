package br.com.pedrotlf.desafioshippmobile.utils

import android.Manifest.permission.*
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import br.com.pedrotlf.desafioshippmobile.establishments.EstablishmentsViewModel
import br.com.pedrotlf.desafioshippmobile.order.OrderViewModel
import org.jetbrains.anko.support.v4.act

open class BaseActivity: AppCompatActivity() {
    private val REQUEST_CODE_LOCATION_PERMISSION = 6283
    private val LOCATION_DENIED = 6363
    val TAG: String
        get() = this.javaClass.simpleName

    lateinit var establishmentsViewModel: EstablishmentsViewModel
    lateinit var orderViewModel: OrderViewModel

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == LOCATION_DENIED)
            finish()
    }
}