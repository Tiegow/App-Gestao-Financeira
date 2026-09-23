package br.ufrn.gerfin.app.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign

@Composable
@Suppress("FunctionNaming", "ktlint:standard:function-naming")
fun CampoMoeda(
    valor: TextFieldValue,
    onValorChange: (TextFieldValue) -> Unit,
    isErro: Boolean,
    modifier: Modifier = Modifier,
) {
    OutlinedTextField(
        value = valor,
        onValueChange = { input ->
            val digits = input.text.filter { it.isDigit() }
            if (digits.length <= 15) {
                val formatted = formatarMoeda(digits)
                onValorChange(
                    TextFieldValue(
                        text = formatted,
                        selection = TextRange(formatted.length),
                    ),
                )
            }
        },
        label = { Text("Valor") },
        prefix = { Text("R$ ") },
        modifier = modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.End),
        singleLine = true,
        isError = isErro,
        supportingText = {
            if (isErro) {
                Text("Insira um valor numérico maior que zero")
            }
        },
    )
}

private fun formatarMoeda(digits: String): String {
    val number = digits.toLongOrNull() ?: 0L
    val cents = (number % 100).toString().padStart(2, '0')
    val reais = (number / 100).toString()
    val reaisFormatado =
        reais
            .reversed()
            .chunked(3)
            .joinToString(".")
            .reversed()
    return "$reaisFormatado,$cents"
}
