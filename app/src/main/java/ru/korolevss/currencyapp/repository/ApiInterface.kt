package ru.korolevss.currencyapp.repository

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import ru.korolevss.currencyapp.dto.ValCurs

interface ApiInterface {

    @GET("daily.xml")
    suspend fun getValCurs(): Call<ValCurs>
}