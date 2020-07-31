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
                nominal = 1.0,
                value = 1.0
            )
        val ratio =  oldCurrencies[position].nominal / oldCurrencies[position].value
        val newCurrencies = mutableListOf(mainCurrency)
        oldCurrencies.forEach {
            if ( it.code != mainCurrency.code) {
                newCurrencies.add(it.copy(
                    value = ratio * ( it.value / it.nominal )
                ))
            }
        }
        currencies.value = Resource.success(newCurrencies)
    }
}