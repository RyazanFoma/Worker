package com.pushe.worker.data.model

import com.google.gson.annotations.SerializedName

data class Total(
    @SerializedName("Наименование")
    val name: String? = null,
    @SerializedName("Сумма")
    val value: Float? = null
)

//data class Totals<T>(
//    @SerializedName("Итоги")
//    val results: List<T>
//)
