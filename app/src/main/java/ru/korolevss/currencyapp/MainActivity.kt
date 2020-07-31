package ru.korolevss.currencyapp

import android.icu.util.Currency
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.korolevss.currencyapp.dto.ValCurs
import ru.korolevss.currencyapp.model.CurrencyItem
import ru.korolevss.currencyapp.model.CurrencyModel
import ru.korolevss.currencyapp.repository.NetworkService


class MainActivity : AppCompatActivity() {

//    val viewModel by lazy { ViewModelProviders.of(this).get(CurrencyModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        viewModel.getCurrencies().observe(this, Observer {
//            it?.let {
//                when (it.status) {
//                    Resource.Status.SUCCESS -> Log.d("OBSERVE", it.data.toString())
//                    Resource.Status.ERROR -> Log.d("OBSERVE", "${it.message}")
//                    Resource.Status.LOADING -> Log.d("OBSERVE", "loading")
//                }
//            }
//        })

        lifecycleScope.launch{
            NetworkService.api
                .getValCurs()
                .enqueue(object : Callback<ValCurs> {
                    override fun onResponse(call: Call<ValCurs>, response: Response<ValCurs>) {
                        Log.d("OBSERVE", "${response.body()}")
                    }
                    override fun onFailure(call: Call<ValCurs>, t: Throwable) {
                        Log.d("OBSERVE", "$t")
                    }
                })
        }

    }
}

