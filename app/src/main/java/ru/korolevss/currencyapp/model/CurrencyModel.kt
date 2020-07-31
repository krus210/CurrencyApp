package ru.korolevss.currencyapp.model

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import ru.korolevss.currencyapp.Resource
import ru.korolevss.currencyapp.repository.NetworkService
import java.lang.Exception
import java.math.BigDecimal

class CurrencyModel : ViewModel() {
    private var currencies = MutableLiveData<Resource<List<CurrencyItem>>>()

    init {
        getCurrenciesFromServer()
    }

    fun getCurrenciesFromServer() {
        try {
            currencies = NetworkService.getValutes()
        } catch (e: Exception) {
            Log.d("GET_CURRENCIES", e.toString())
        }
    }

    fun getCurrencies() = currencies

    fun changeMainCurrency(position: Int) {
        val oldCurrencies = currencies.value!!.data!!
        val mainCurrency =
            oldCurrencies[position].copy(
                nominal = BigDecimal.valueOf(1.0),
                value = BigDecimal.valueOf(1.0)
            )
        val ratio = oldCurrencies[position].value.divide(oldCurrencies[position].nominal)
        val newCurrencies = mutableListOf(mainCurrency)
        oldCurrencies.forEach {
            if ( it.code != mainCurrency.code) {
                newCurrencies.add(it.copy(
                    value = it.value.div(it.nominal).div(ratio)
                ))
            }
        }
        currencies.value = Resource.success(newCurrencies)
    }
}