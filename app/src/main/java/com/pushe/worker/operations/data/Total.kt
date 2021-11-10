package com.pushe.worker.operations.data

import com.google.gson.annotations.SerializedName

data class Total(
    @SerializedName("Наименование")
    val name: String? = null,
    @SerializedName("Сумма")
    val value: Float? = null
)
