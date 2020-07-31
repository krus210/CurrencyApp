package ru.korolevss.currencyapp.adapter

import androidx.recyclerview.widget.DiffUtil
import ru.korolevss.currencyapp.model.CurrencyItem

class CurrencyDiffUtilCallback (
    private val oldList: List<CurrencyItem>,
    private val newList: List<CurrencyItem>
) : DiffUtil.Callback() {

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldModel = oldList[oldItemPosition]
        val newModel = newList[newItemPosition]
        return oldModel.code == newModel.code
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldModel = oldList[oldItemPosition]
        val newModel = newList[newItemPosition]
        return oldModel.value == newModel.value
    }

}