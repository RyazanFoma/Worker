package com.pushe.worker.operations.model

import com.google.gson.annotations.SerializedName

data class Operations<T>(
//    @field:Json(name = "results")
    @SerializedName("Операции")
    val results: List<T>
)


