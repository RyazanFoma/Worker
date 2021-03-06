package com.pushe.worker.operations.data

import com.google.gson.annotations.SerializedName

data class Operation (
//  Этап производства
    @SerializedName("Дата")
    val date: String? = null,
    @SerializedName("Номер")
    val number: String? = null,
    @SerializedName("Наименование")
    val name: String? = null,
//  Производственная операция
    @SerializedName("ВидРабот")
    val type: String? = null,
    @SerializedName("Количество")
    val amount: Float? = null,
    @SerializedName("Выполнено")
    val performed: Float? = null,
    @SerializedName("ЕдИзм")
    val unit: String? = null,
//  Выработка сотрудников
    @SerializedName("Расценка")
    val tariff: Float? = null,
    @SerializedName("Сумма")
    val sum: Float? = null,
)


