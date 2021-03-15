package br.com.pedrotlf.desafioshippmobile.api

import com.fasterxml.jackson.annotation.JsonProperty

data class ResumeResponse(
    @JsonProperty("product_value") val productValue: Double?,
    val distance: Double?,
    @JsonProperty("total_value") val totalValue: Double?,
    @JsonProperty("fee_value") val feeValue: Double?
)