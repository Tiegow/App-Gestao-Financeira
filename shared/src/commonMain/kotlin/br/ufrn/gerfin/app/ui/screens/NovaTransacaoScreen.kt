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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import br.ufrn.gerfin.app.ui.components.CampoData
import br.ufrn.gerfin.app.ui.components.CampoDropdown
import br.ufrn.gerfin.app.ui.components.CampoMoeda

@Composable
@Suppress("FunctionNaming", "ktlint:standard:function-naming", "LongMethod")
fun NovaTransacaoScreen(
    onSalvar: (Transacao) -> Unit,
    onCancelar: () -> Unit,
) {
    val scrollState = rememberScrollState()

    var isReceita by remember { mutableStateOf(false) }
    var valorTextField by remember { mutableStateOf(TextFieldValue(text = "0,00", selection = TextRange(4))) }
    var dataRaw by remember { mutableStateOf("") }
    var categoria by remember { mutableStateOf("") }
    var formaPagamento by remember { mutableStateOf("") }
    var observacoes by remember { mutableStateOf("") }

    var isPago by remember { mutableStateOf(true) }

    val categoriasDespesa = listOf("Alimentação", "Moradia", "Transporte", "Saúde", "Lazer", "Outros")
    val categoriasReceita = listOf("Salário", "Rendimentos", "Vendas", "Outros")
    val opcoesCategoria = if (isReceita) categoriasReceita else categoriasDespesa

    val formasPagamento = listOf("Dinheiro", "Pix", "Cartão de Crédito", "Cartão de Débito", "Boleto")

    // Validações
    val isValorValido =
        valorTextField.text
            .filter { it.isDigit() }
            .toLongOrNull()
            ?.let { it > 0L } ?: false
    val isDataValida = dataRaw.isNotBlank()
    val isCategoriaValida = categoria.isNotBlank()
    val isFormaValida = formaPagamento.isNotBlank()

    val isValido = isValorValido && isDataValida && isCategoriaValida && isFormaValida

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
                    .verticalScroll(scrollState)
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
                onTipoChange = { novoTipo ->
                    isReceita = novoTipo
                    categoria = "" // reseta a categoria ao mudar o tipo
                },
            )

            CampoMoeda(
                valor = valorTextField,
                onValorChange = { valorTextField = it },
                isErro = !isValorValido && valorTextField.text != "0,00",
            )

            CampoData(
                dataSelecionada = dataRaw,
                onDataChange = { dataRaw = it },
                isErro = !isDataValida && dataRaw.isNotEmpty(),
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
            ) {
                Text(
                    text = if (isReceita) "Valor já recebido?" else "Conta já paga?",
                    style = MaterialTheme.typography.bodyLarge,
                )
                androidx.compose.material3.Switch(
                    checked = isPago,
                    onCheckedChange = { isPago = it },
                )
            }

            CampoDropdown(
                label = "Categoria",
                opcoes = opcoesCategoria,
                selecionado = categoria,
                onSelecionadoChange = { categoria = it },
            )

            CampoDropdown(
                label = "Forma de Pagamento",
                opcoes = formasPagamento,
                selecionado = formaPagamento,
                onSelecionadoChange = { formaPagamento = it },
            )

            OutlinedTextField(
                value = observacoes,
                onValueChange = { observacoes = it },
                label = { Text("Observações") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 2,
            )

            Spacer(modifier = Modifier.weight(1f, fill = false))

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
                                valor = valorTextField.text,
                                isReceita = isReceita,
                                data = dataRaw,
                                categoria = categoria,
                                formaPagamento = formaPagamento,
                                observacoes = observacoes.trim(),
                                isPago = isPago,
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
