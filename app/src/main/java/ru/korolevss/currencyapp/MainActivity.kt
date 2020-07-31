package ru.korolevss.currencyapp

import android.icu.util.Currency
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.*
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.blongho.country_data.World
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_fail.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.korolevss.currencyapp.adapter.CurrencyAdapter
import ru.korolevss.currencyapp.adapter.CurrencyDiffUtilCallback
import ru.korolevss.currencyapp.dto.ValCurs
import ru.korolevss.currencyapp.model.CurrencyItem
import ru.korolevss.currencyapp.model.CurrencyModel
import ru.korolevss.currencyapp.repository.NetworkService


class MainActivity : AppCompatActivity(), CurrencyAdapter.OnBtnClickListener {

    val viewModel by lazy { ViewModelProviders.of(this).get(CurrencyModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        with(container) {
            layoutManager = LinearLayoutManager(context)
            adapter = CurrencyAdapter(emptyList()).apply {
                btnClickListener = this@MainActivity
            }
        }

        viewModel.getCurrencies().observe(this, Observer {
            it?.let {
                when (it.status) {
                    Resource.Status.SUCCESS -> {
                        updateList(it.data!!)
                        determinateBar.isVisible = false
                    }
                    Resource.Status.ERROR -> {
                        showError(it.message!!)
                        determinateBar.isVisible = false
                    }
                    Resource.Status.LOADING -> {
                        determinateBar.isVisible = true

                    }
                }
            }
        })

        swipeContainer.setOnRefreshListener {
            viewModel.getCurrenciesFromServer()
            swipeContainer.isRefreshing = false
        }
    }

    override fun onBtnClicked(position: Int) {
        viewModel.changeMainCurrency(position)
    }

    private fun updateList(currencies: List<CurrencyItem>) {
        with(container.adapter as CurrencyAdapter) {
            val currencyDiffUtilCallback = CurrencyDiffUtilCallback(list, currencies)
            val postDiffResult = DiffUtil.calculateDiff(currencyDiffUtilCallback)
            updatePosts(currencies)
            postDiffResult.dispatchUpdatesTo(this)
        }
    }

    private fun showError(message: Int) {
        val dialog = AlertDialog.Builder(this)
            .setView(R.layout.item_fail)
            .show()
        with(dialog) {
            textViewFailed.text = getString(message)
            buttonOk.setOnClickListener {
                dialog.dismiss()
            }
        }
    }

}

