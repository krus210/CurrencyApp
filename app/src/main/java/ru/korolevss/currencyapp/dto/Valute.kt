package ru.korolevss.currencyapp.dto

import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(strict = false, name = "Valute")
data class Valute(
    @field:Attribute(name = "ID") var id: String? = null,
    @field:Element(name = "NumCode") var numCode: String? = null,
    @field:Element(name = "CharCode") var charCode: String? = null,
    @field:Element(name = "Nominal") var nominal: Int? = null,
    @field:Element(name = "Name") var name: String? = null,
    @field:Element(name = "Value") var value: String? = null
)