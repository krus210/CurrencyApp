@file:Suppress("DEPRECATION")

package ru.korolevss.currencyapp.repository

import android.util.Log
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
import java.lang.Exception
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

    fun getApi() = retrofit.create(ApiInterface::class.java)

    fun getValutes(): MutableLiveData<Resource<List<CurrencyItem>>> {
        val mutableLiveData = MutableLiveData<Resource<List<CurrencyItem>>>()
        mutableLiveData.value = Resource.loading()
        getApi()
            .getValCurs()
            .enqueue(object : Callback<ValCurs> {
                override fun onResponse(call: Call<ValCurs>, response: Response<ValCurs>) {
                    val valCures = response.body()
                    if (valCures?.valutes.isNullOrEmpty()) {
                        mutableLiveData.value = Resource.error(R.string.empty_list_of_currencies)
                    } else {
                        mutableLiveData.value = Resource.success(getCurrencies(valCures!!))
                        Log.d("Currency", "${mutableLiveData.value?.data}")
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
        val usdCurrency = CurrencyItem(
            code = usdValute.charCode!!,
            name = Currency.getInstance("USD").displayName,
            flagResource = World.getFlagOf(usdValute.numCode!!.toInt()),
            nominal = 1.0,
            value = 1.0
        )
        val currencyRatio =
            usdValute.value!!.replace(',', '.').toDouble() / usdValute.nominal!!.toDouble()
        val currencies = mutableListOf(usdCurrency)
        valCures.valutes!!.forEach {
            if (it.charCode != "USD") {
                val currencyItem = CurrencyItem(
                    code = it.charCode!!,
                    name = Currency.getInstance(it.charCode).displayName,
                    flagResource = World.getFlagOf(it.numCode!!.toInt()),
                    nominal = 1.0,
                    value = currencyRatio / (it.value!!.replace(',', '.')
                        .toDouble() / it.nominal!!.toDouble())
                )
                currencies.add(currencyItem)
            }
        }
        val indexOfEuro = currencies.indexOfFirst { it.code == "EUR" }
        val copyOfEuro = currencies[indexOfEuro]
            .copy(flagResource = R.drawable.ic_flag_of_europe)
        currencies[indexOfEuro] = copyOfEuro
        return currencies
    }

}