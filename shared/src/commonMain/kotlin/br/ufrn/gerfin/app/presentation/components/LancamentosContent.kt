package br.ufrn.gerfin.app.presentation.components

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import br.ufrn.gerfin.app.presentation.state.LancamentosUiState

@Composable
@Suppress("FunctionNaming", "ktlint:standard:function-naming")
fun LancamentosContent(
    state: LancamentosUiState,
    onTentarNovamente: () -> Unit,
) {
    when (state) {
        LancamentosUiState.Carregando -> {
            CircularProgressIndicator()
        }

        LancamentosUiState.Vazio -> {
            Text("Nenhum lançamento encontrado")
        }

        is LancamentosUiState.Sucesso -> {
            LazyColumn {
                items(state.transacoes) { transacao ->
                    TransacaoItem(transacao)
                }
            }
        }

        is LancamentosUiState.Erro -> {
            Text(state.mensagem)

            Button(onClick = onTentarNovamente) {
                Text("Tentar novamente")
            }
        }
    }
}
