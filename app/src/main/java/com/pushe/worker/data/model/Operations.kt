package com.pushe.worker.data.model

import com.google.gson.annotations.SerializedName

data class Operation (
//  Этап производства
    @SerializedName("Номер")
    val number: String? = null,
    @SerializedName("Наименование")
    val name: String? = null,
//  Производственная операция
    @SerializedName("Вид работ")
    val type: String? = null,
    @SerializedName("Количество")
    val amount: Float? = null,
    @SerializedName("Ед. изм.")
    val unit: String? = null,
    @SerializedName("Выполнено")
    val date: String? = null,
//  Выработка сотрудников
    @SerializedName("Расценка")
    val tarrif: Float? = null,
    @SerializedName("Сумма")
    val sum: Float? = null
)

data class Operations<T>(
//    @field:Json(name = "results")
    @SerializedName("Операции")
    val results: List<T>
)


