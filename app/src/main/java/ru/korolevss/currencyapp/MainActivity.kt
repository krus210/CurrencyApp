package ru.korolevss.currencyapp

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_fail.*
import ru.korolevss.currencyapp.adapter.CurrencyAdapter
import ru.korolevss.currencyapp.adapter.CurrencyDiffUtilCallback
import ru.korolevss.currencyapp.model.CurrencyItem
import ru.korolevss.currencyapp.model.CurrencyModel


class MainActivity : AppCompatActivity(), CurrencyAdapter.OnBtnClickListener {

    private val viewModel
            by lazy { ViewModelProviders.of(this).get(CurrencyModel::class.java) }

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
                        updateList(it.data)
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

    }

    override fun onBtnClicked(position: Int) {
        viewModel.changeMainCurrency(position)
    }

    private fun updateList(currencies: List<CurrencyItem>?) {
        with(container.adapter as CurrencyAdapter) {
            currencies?.let {
                val currencyDiffUtilCallback = CurrencyDiffUtilCallback(list, currencies)
                val postDiffResult = DiffUtil.calculateDiff(currencyDiffUtilCallback)
                updatePosts(currencies)
                postDiffResult.dispatchUpdatesTo(this)
            }
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

