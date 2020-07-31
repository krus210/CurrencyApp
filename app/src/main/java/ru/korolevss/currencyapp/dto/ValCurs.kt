package ru.korolevss.currencyapp.dto

import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

@Root(strict = false, name = "ValCurs")
data class ValCurs @JvmOverloads constructor(
    @field:Attribute(name = "Date") var date: String? = null,
    @field:Attribute(name = "name") var name: String? = null,
    @field:ElementList(
        name = "Valute",
        type = Valute::class,
        inline = true
    ) var valutes: List<Valute>? = null
)