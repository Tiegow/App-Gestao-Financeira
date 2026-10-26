package br.ufrn.gerfin.app.utils

import kotlin.math.round

fun parseValor(valorStr: String): Double {
    val limpo = valorStr.replace(".", "").replace(",", ".")
    return limpo.toDoubleOrNull() ?: 0.0
}

fun formatarValor(valor: Double): String {
    val totalCentavos = round(valor * 100).toLong()
    val sinal = if (totalCentavos < 0) "-" else ""
    val absoluto = kotlin.math.abs(totalCentavos)

    val reais = (absoluto / 100)
        .toString()
        .reversed()
        .chunked(3)
        .joinToString(".")
        .reversed()

    val centavos = (absoluto % 100)
        .toString()
        .padStart(2, '0')

    return "$sinal$reais,$centavos"
}
