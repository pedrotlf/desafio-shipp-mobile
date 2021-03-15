package br.com.pedrotlf.desafioshippmobile.order

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import br.com.pedrotlf.desafioshippmobile.EstablishmentOrder
import br.com.pedrotlf.desafioshippmobile.R
import br.com.pedrotlf.desafioshippmobile.api.API
import br.com.pedrotlf.desafioshippmobile.api.CheckoutResponse
import br.com.pedrotlf.desafioshippmobile.api.OrderRequest
import br.com.pedrotlf.desafioshippmobile.api.ResumeResponse
import br.com.pedrotlf.desafioshippmobile.utils.Prefs
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.NumberFormat

class OrderViewModel(application: Application) : AndroidViewModel(application) {
    private val prefs = Prefs(application)

    private val api = API.create(application.getString(R.string.order_base_url))

    fun validateResume(establishmentOrder: EstablishmentOrder, callback: (ResumeResponse?)->Unit){
        val request = OrderRequest(
            establishmentOrder.latLng?.latitude,
            establishmentOrder.latLng?.longitude,
            prefs.currentLocation?.latitude,
            prefs.currentLocation?.longitude,
            establishmentOrder.price
        )
        api.postResume(request).enqueue(object : Callback<ResumeResponse>{
            override fun onFailure(call: Call<ResumeResponse>, t: Throwable) {
                callback(null)
            }

            override fun onResponse(call: Call<ResumeResponse>, response: Response<ResumeResponse>) {
               callback(response.body())
            }

        })
    }

    fun checkout(establishmentOrder: EstablishmentOrder, callback: (CheckoutResponse?) -> Unit){
        val request = OrderRequest(
            establishmentOrder.latLng?.latitude,
            establishmentOrder.latLng?.longitude,
            prefs.currentLocation?.latitude,
            prefs.currentLocation?.longitude,
            establishmentOrder.price,
            "1111111111111111",
            "789",
            "01/18"
        )
        api.checkout(request).enqueue(object : Callback<CheckoutResponse>{
            override fun onFailure(call: Call<CheckoutResponse>, t: Throwable) {
                callback(null)
            }

            override fun onResponse(call: Call<CheckoutResponse>, response: Response<CheckoutResponse>) {
                callback(response.body())
            }

        })
    }
}