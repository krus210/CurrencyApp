package ru.korolevss.currencyapp.adapter

import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_card.view.*
import ru.korolevss.currencyapp.model.CurrencyItem
import java.text.DecimalFormat

class CurrencyViewHolder(private val adapter: CurrencyAdapter, private val view: View) :
    RecyclerView.ViewHolder(view) {

    init {
        view.setOnClickListener {
            if (adapterPosition != RecyclerView.NO_POSITION) {
                adapter.btnClickListener?.onBtnClicked(adapterPosition)
            }
        }
    }

    fun bind(currency: CurrencyItem) {
        with(view) {
            imageFlag.setImageResource(currency.flagResource)
            textCode.text = currency.code
            textName.text = currency.name
            val df = DecimalFormat()
            df.maximumFractionDigits = 4
            df.minimumFractionDigits = 1
            df.isGroupingUsed = false
            val currencyValue = df.format(currency.value)
            val content = SpannableString(currencyValue)
            content.setSpan(UnderlineSpan(), 0, content.length, 0)
            textCurrency.text = content
        }
    }

}
