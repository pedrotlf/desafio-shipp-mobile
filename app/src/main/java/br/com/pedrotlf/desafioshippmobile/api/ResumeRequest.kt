package br.com.pedrotlf.desafioshippmobile.api

import com.fasterxml.jackson.annotation.JsonProperty

data class ResumeRequest(
    @JsonProperty("store_latitude") val storeLatidude: Double?,
    @JsonProperty("store_longitude") val storeLongitude: Double?,
    @JsonProperty("user_latitude") val userLatitude: Double?,
    @JsonProperty("user_longitude") val userLongitude: Double?,
    val value: Double?
)