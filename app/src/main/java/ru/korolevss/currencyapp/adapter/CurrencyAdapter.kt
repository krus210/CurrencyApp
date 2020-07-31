package ru.korolevss.currencyapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.korolevss.currencyapp.R
import ru.korolevss.currencyapp.model.CurrencyItem

class CurrencyAdapter(var list: List<CurrencyItem>):
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var btnClickListener: OnBtnClickListener? = null

    interface OnBtnClickListener {
        fun onBtnClicked(position: Int)
    }

    fun updatePosts(newData: List<CurrencyItem>) {
        this.list = newData
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_card, parent, false)
        return CurrencyViewHolder(this, view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currency = list[position]
        with(holder as CurrencyViewHolder) {
            bind(currency)
        }
    }

}