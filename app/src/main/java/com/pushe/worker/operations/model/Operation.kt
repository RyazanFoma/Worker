package com.pushe.worker.operations.model

import com.google.gson.annotations.SerializedName

data class Operation (
    @SerializedName("Код")
    val id: String? = null,
    @SerializedName("Наименование")
    val name: String? = null,
    @SerializedName("Длительность")
    val duration: Float? = null,
    @SerializedName("Тариф")
    val rate: Float? = null
)