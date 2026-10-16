package br.ufrn.gerfin.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun LoginScreen(
    onEntrar: (String, String) -> Unit,
    onRegister: () -> Unit,
    mensagemErro: String?,
) {
    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf( value="" ) }

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Login",
            style = MaterialTheme.typography.headlineLarge
        )

        OutlinedTextField(
            value = email,
            onValueChange = { digitado ->
                email = digitado
            },
            label = { Text("Digite seu E-mail") }
        )

        OutlinedTextField(
            value = senha,
            onValueChange = { senhaDigitada ->
                senha = senhaDigitada
            },
            label = { Text("Digite sua senha") }
        )

        Button(
            onClick = {
                onEntrar(email, senha)
            }
        ) { Text("Entrar") }

        TextButton(
            onClick = {
                onRegister()
            }
        ) { Text("Não tem conta? Registre-se") }

        mensagemErro?.let { mensagem ->
            Text(
                text = mensagem,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}
