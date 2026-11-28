package br.ufrn.gerfin.app.data

import br.ufrn.gerfin.app.domain.error.LancamentoFailure
import io.github.jan.supabase.postgrest.exception.PostgrestRestException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.ResponseException
import kotlinx.io.IOException
import kotlinx.serialization.SerializationException

internal fun Throwable.toLancamentoFailure(): Throwable =
    when (this) {
        is HttpRequestTimeoutException, is IOException ->
            LancamentoFailure.SemConexao(this)
        is PostgrestRestException, is ResponseException, is SerializationException ->
            LancamentoFailure.RespostaInvalida(this)
        else -> this
    }
