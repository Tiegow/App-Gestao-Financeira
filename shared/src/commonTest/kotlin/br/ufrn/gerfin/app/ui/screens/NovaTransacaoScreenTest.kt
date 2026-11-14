package br.ufrn.gerfin.app.ui.screens

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.runComposeUiTest
import br.ufrn.gerfin.app.data.CategoriaDto
import br.ufrn.gerfin.app.model.TipoCategoria
import kotlin.test.Test
import kotlin.test.assertTrue

@OptIn(ExperimentalTestApi::class)
class NovaTransacaoScreenTest {
    private val categorias =
        listOf(
            CategoriaDto(
                id = 1,
                nome = "Alimentação",
                tipo = TipoCategoria.DESPESA,
            ),
        )

    @Test
    fun `formulario inicia invalido`() =
        runComposeUiTest {
            setContent {
                NovaTransacaoScreen(
                    categorias = categorias,
                    onSalvar = {},
                    onCancelar = {},
                )
            }

            onNodeWithText("Nova Transação").assertIsDisplayed()
            onNodeWithText("Salvar").assertIsNotEnabled()
        }

    @Test
    fun `formulario preenchido permite salvar`() =
        runComposeUiTest {
            var transacaoSalva = false

            setContent {
                NovaTransacaoScreen(
                    categorias = categorias,
                    onSalvar = { transacaoSalva = true },
                    onCancelar = {},
                    initialData = "10/11/2026",
                )
            }

            onNodeWithTag("campo-valor").performTextInput("1000")

            onNodeWithTag("campo-categoria").performClick()
            onNodeWithText("Alimentação").performClick()

            onNodeWithTag("campo-forma-de-pagamento").performClick()
            onNodeWithText("Pix").performClick()

            onNodeWithText("Salvar").assertIsEnabled()
            onNodeWithText("Salvar").performClick()

            assertTrue(transacaoSalva)
        }
}