package br.ufrn.gerfin.app.domain.error

sealed class LancamentoFailure(
    message: String,
    cause: Throwable,
) : Exception(message, cause) {
    class SemConexao(
        cause: Throwable,
    ) : LancamentoFailure("Sem conexão com o servidor.", cause)

    class RespostaInvalida(
        cause: Throwable,
    ) : LancamentoFailure("O servidor retornou uma resposta inválida.", cause)
}
