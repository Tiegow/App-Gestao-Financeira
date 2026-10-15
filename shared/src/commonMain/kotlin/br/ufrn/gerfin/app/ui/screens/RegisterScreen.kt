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
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack

@Composable
fun RegisterScreen(
    onRegister: () -> Unit,
    onVoltar: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf( value="" ) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Registrar-se",
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

            OutlinedTextField(
                value = senha,
                onValueChange = { senhaDigitada ->
                    senha = senhaDigitada
                },
                label = { Text("Digite novamente sua senha") }
            )

            Button(
                onClick = {
                    onRegister()
                }
            ) { Text("Registrar-se") }
        }

        IconButton(
            onClick = { onVoltar() },
            modifier = Modifier.align(Alignment.TopStart)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Voltar"
            )
        }
    }
}
