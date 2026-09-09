package br.ufrn.gerfin.app.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.ufrn.gerfin.app.model.Transacao

@Composable
@Suppress("FunctionNaming", "ktlint:standard:function-naming")
fun TransacaoItem(transacao: Transacao) {
    val corValor = if (transacao.isReceita) Color(0xFF2E7D32) else Color(0xFFC62828)
    val sinal = if (transacao.isReceita) "+" else "-"

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors =
            CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
            ),
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = transacao.descricao,
                style = MaterialTheme.typography.bodyLarge,
            )
            Text(
                text = "$sinal R$ ${transacao.valor}",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = corValor,
            )
        }
    }
}
