package com.pushe.worker.logup.data

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("Код")
    val id: String? = null,
    @SerializedName("Наименование")
    val name: String? = null,
    @SerializedName("Пароль")
    val password: String? = null,
)
