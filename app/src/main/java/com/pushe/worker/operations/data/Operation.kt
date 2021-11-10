package com.pushe.worker.operations.data

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
    val tariff: Float? = null,
    @SerializedName("Сумма")
    val sum: Float? = null
)


