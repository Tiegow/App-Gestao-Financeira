package br.ufrn.gerfin.app.data

import br.ufrn.gerfin.app.domain.error.LancamentoFailure
import kotlinx.io.IOException
import kotlinx.serialization.SerializationException
import kotlin.test.Test
import kotlin.test.assertIs
import kotlin.test.assertSame

class LancamentoFailureMapperTest {
    @Test
    fun `io exception vira falha de conexao`() {
        val original = IOException("offline")

        val failure = original.toLancamentoFailure()

        assertIs<LancamentoFailure.SemConexao>(failure)
        assertSame(original, failure.cause)
    }

    @Test
    fun `falha de serializacao vira resposta invalida`() {
        val original = SerializationException("json invalido")

        val failure = original.toLancamentoFailure()

        assertIs<LancamentoFailure.RespostaInvalida>(failure)
        assertSame(original, failure.cause)
    }

    @Test
    fun `erro desconhecido nao e tratado como falha de rede`() {
        val original = IllegalStateException("bug")

        val failure = original.toLancamentoFailure()

        assertSame(original, failure)
    }
}
