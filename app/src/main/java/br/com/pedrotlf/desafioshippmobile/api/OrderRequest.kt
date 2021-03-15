package br.com.pedrotlf.desafioshippmobile.api

import com.fasterxml.jackson.annotation.JsonProperty

data class OrderRequest(
    @JsonProperty("store_latitude") val storeLatidude: Double?,
    @JsonProperty("store_longitude") val storeLongitude: Double?,
    @JsonProperty("user_latitude") val userLatitude: Double?,
    @JsonProperty("user_longitude") val userLongitude: Double?,
    val value: Double?,
    @JsonProperty("card_number") val cardNumber: String?,
    val cvv: String?,
    @JsonProperty("expiry_date") val expiryDate: String?
){
    constructor(storeLatidude: Double?, storeLongitude: Double?, userLatitude: Double?, userLongitude: Double?, value: Double?): this(storeLatidude, storeLongitude, userLatitude, userLongitude, value, null, null, null)
}