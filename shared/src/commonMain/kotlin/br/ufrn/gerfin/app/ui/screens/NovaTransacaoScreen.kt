package br.ufrn.gerfin.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import br.ufrn.gerfin.app.model.Transacao
import br.ufrn.gerfin.app.ui.components.CampoMoeda

@Composable
@Suppress("FunctionNaming", "ktlint:standard:function-naming")
fun NovaTransacaoScreen(
    onSalvar: (Transacao) -> Unit,
    onCancelar: () -> Unit,
) {
    var descricao by remember { mutableStateOf("") }
    var isReceita by remember { mutableStateOf(false) }
    var valorTextField by remember {
        mutableStateOf(TextFieldValue(text = "0,00", selection = TextRange(4)))
    }

    val isValorValido =
        valorTextField.text
            .filter { it.isDigit() }
            .toLongOrNull()
            ?.let { it > 0L } ?: false
    val isValido = descricao.isNotBlank() && isValorValido

    Scaffold(
        modifier =
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .safeContentPadding(),
    ) { paddingValues ->
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(
                text = "Nova Transação",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
            )

            TipoTransacaoSelector(
                isReceita = isReceita,
                onTipoChange = { isReceita = it },
            )

            OutlinedTextField(
                value = descricao,
                onValueChange = { descricao = it },
                label = { Text("Descrição") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
            )

            CampoMoeda(
                valor = valorTextField,
                onValorChange = { valorTextField = it },
                isErro = !isValorValido && valorTextField.text != "0,00",
            )

            Spacer(modifier = Modifier.weight(1f))

            FormularioAcoes(
                isValido = isValido,
                onCancelar = onCancelar,
                onSalvar = {
                    if (isValido) {
                        onSalvar(
                            Transacao(
                                id =
                                    kotlin.random.Random
                                        .nextInt()
                                        .toString(),
                                descricao = descricao.trim(),
                                valor = valorTextField.text,
                                isReceita = isReceita,
                            ),
                        )
                    }
                },
            )
        }
    }
}

@Composable
@Suppress("FunctionNaming", "ktlint:standard:function-naming")
private fun TipoTransacaoSelector(
    isReceita: Boolean,
    onTipoChange: (Boolean) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        FilterChip(
            selected = !isReceita,
            onClick = { onTipoChange(false) },
            label = { Text("Despesa") },
        )
        FilterChip(
            selected = isReceita,
            onClick = { onTipoChange(true) },
            label = { Text("Receita") },
        )
    }
}

@Composable
@Suppress("FunctionNaming", "ktlint:standard:function-naming")
private fun FormularioAcoes(
    isValido: Boolean,
    onCancelar: () -> Unit,
    onSalvar: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        OutlinedButton(onClick = onCancelar, modifier = Modifier.weight(1f)) {
            Text("Cancelar")
        }
        Button(
            onClick = onSalvar,
            modifier = Modifier.weight(1f),
            enabled = isValido,
        ) {
            Text("Salvar")
        }
    }
}
