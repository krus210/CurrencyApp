@file:Suppress("DEPRECATION")

package ru.korolevss.currencyapp.repository

import androidx.lifecycle.MutableLiveData
import com.blongho.country_data.World
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import ru.korolevss.currencyapp.App
import ru.korolevss.currencyapp.BuildConfig
import ru.korolevss.currencyapp.R
import ru.korolevss.currencyapp.Resource
import ru.korolevss.currencyapp.dto.ValCurs
import ru.korolevss.currencyapp.model.CurrencyItem
import java.math.BigDecimal
import java.util.*

object NetworkService {

    private const val BASE_URL = "https://www.cbr-xml-daily.ru/"
    private val retrofit: Retrofit

    init {
        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(OkHttpClient())
            .addConverterFactory(SimpleXmlConverterFactory.create())
            .build()
    }

    val api = retrofit.create(ApiInterface::class.java)

    suspend fun getValutes(): MutableLiveData<Resource<List<CurrencyItem>>> {
        val mutableLiveData = MutableLiveData<Resource<List<CurrencyItem>>>()
        mutableLiveData.value = Resource.loading()
        api
            .getValCurs()
            .enqueue(object : Callback<ValCurs> {
                override fun onResponse(call: Call<ValCurs>, response: Response<ValCurs>) {
                    val valCures = response.body()
                    if (valCures?.valutes.isNullOrEmpty()) {
                        mutableLiveData.value = Resource.error(R.string.empty_list_of_currencies)
                    } else {
                        mutableLiveData.value = Resource.success(getCurrencies(valCures!!))
                    }
                }
                override fun onFailure(call: Call<ValCurs>, t: Throwable) {
                    mutableLiveData.value = Resource.error(R.string.fail_loading_currencies)
                }
            })
        return mutableLiveData
    }

    private fun getCurrencies(valCures: ValCurs): List<CurrencyItem> {
        World.init(App.context)
        val usdValute = valCures.valutes?.first { it.charCode == "USD" }!!
        val allCurrencies = World.getAllCurrencies()
        val usdCurrency = CurrencyItem(
            code = usdValute.charCode!!,
            name = allCurrencies.first { it.code == usdValute.charCode }.name,
            flagResource = World.getFlagOf(usdValute.numCode!!.toInt()),
            nominal = BigDecimal(1.0),
            value = BigDecimal(1.0)
        )
        val currencyRatio =
            BigDecimal(usdValute.nominal!!).div(BigDecimal(usdValute.value!!.toDouble()))
        val currencies = mutableListOf(usdCurrency)
        valCures.valutes!!.forEach {
            if (it.charCode != "USD") {
                currencies.add(
                    CurrencyItem(
                        code = it.charCode!!,
                        name = allCurrencies.first { it1 -> it1.code == it.charCode }.name,
                        flagResource = World.getFlagOf(it.numCode!!.toInt()),
                        nominal = BigDecimal(1.0),
                        value = BigDecimal(it.value!!.toDouble())
                            .div(BigDecimal(it.nominal!!.toDouble()))
                            .multiply(currencyRatio)
                    )
                )
            }
        }
        return currencies
    }

}