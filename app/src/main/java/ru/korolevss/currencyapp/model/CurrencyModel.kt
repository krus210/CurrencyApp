package ru.korolevss.currencyapp.model

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import retrofit2.await
import ru.korolevss.currencyapp.Resource
import ru.korolevss.currencyapp.repository.NetworkService
import java.lang.Exception

class CurrencyModel: ViewModel() {
    private var currencies = MutableLiveData<Resource<List<CurrencyItem>>>()

    init {
        try {
            viewModelScope.launch(Dispatchers.Main) {
                currencies = NetworkService.getValutes()
            }
        } catch (e: Exception) {
            Log.d("GET_CURRENCIES", e.toString())
        }
    }

    fun getCurrencies() = currencies
}