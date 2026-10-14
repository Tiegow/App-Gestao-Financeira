package br.ufrn.gerfin.app.utils

import kotlin.math.round

fun parseValor(valorStr: String): Double {
    val limpo = valorStr.replace(".", "").replace(",", ".")
    return limpo.toDoubleOrNull() ?: 0.0
}

fun formatarValor(valor: Double): String {
    val intPart = valor.toLong()
    val cents = round((valor - intPart) * 100).toInt().toString().padStart(2, '0')
    val intFormatado =
        intPart
            .toString()
            .reversed()
            .chunked(3)
            .joinToString(".")
            .reversed()
    return "$intFormatado,$cents"
}
