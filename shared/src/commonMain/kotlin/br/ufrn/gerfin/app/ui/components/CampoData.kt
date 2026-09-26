package br.ufrn.gerfin.app.ui.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Preview
@Composable
@Suppress("FunctionNaming", "ktlint:standard:function-naming")
fun CampoDataPreview() {
    CampoData(dataSelecionada = "01/10/2026", onDataChange = {}, isErro = false)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Suppress("FunctionNaming", "ktlint:standard:function-naming")
fun CampoData(
    dataSelecionada: String,
    onDataChange: (String) -> Unit,
    isErro: Boolean,
    modifier: Modifier = Modifier,
) {
    var mostrarModal by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    OutlinedTextField(
        value = dataSelecionada,
        onValueChange = { },
        readOnly = true,
        label = { Text("Data") },
        modifier = modifier.fillMaxWidth(),
        isError = isErro,
        supportingText = { if (isErro) Text("Selecione uma data") },
        trailingIcon = {
            IconButton(onClick = { mostrarModal = true }) {
                Icon(Icons.Default.DateRange, contentDescription = "Selecionar data")
            }
        },
        interactionSource =
            remember { MutableInteractionSource() }.also { interactionSource ->
                LaunchedEffect(interactionSource) {
                    interactionSource.interactions.collect {
                        if (it is PressInteraction.Release) {
                            mostrarModal = true
                        }
                    }
                }
            },
    )

    if (mostrarModal) {
        DatePickerDialog(
            onDismissRequest = { mostrarModal = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            val dataStr =
                                br.ufrn.gerfin.app.utils
                                    .formatMillisToDateString(millis)
                            onDataChange(dataStr)
                        }
                        mostrarModal = false
                    },
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarModal = false }) {
                    Text("Cancelar")
                }
            },
        ) {
            DatePicker(state = datePickerState)
        }
    }
}
