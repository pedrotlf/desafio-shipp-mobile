package br.com.pedrotlf.desafioshippmobile.api

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit

interface OrderApi {

    @Headers("Accept: application/json")
    @POST("order/resume")
    suspend fun validateResume(@Body requestBody: OrderRequest): ResumeResponse

    @Headers("Accept: application/json")
    @POST("order")
    suspend fun checkout(@Body requestBody: OrderRequest): CheckoutResponse

    companion object {
        const val baseUrl = "https://k12xubei40.execute-api.sa-east-1.amazonaws.com/challenge/v1/"
    }
}