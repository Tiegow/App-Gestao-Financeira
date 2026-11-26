package br.ufrn.gerfin.app.fakes

import br.ufrn.gerfin.app.domain.repository.AuthRepository
import br.ufrn.gerfin.app.domain.validation.validarLogin
import br.ufrn.gerfin.app.domain.validation.validarRegistro

class FakeAuthRepository(
    private val usuarioId: String? = "usuario-1",
    private val excecaoRede: Exception? = null,
) : AuthRepository {
    override suspend fun login(
        email: String,
        senha: String,
    ) {
        validarLogin(email, senha)
        excecaoRede?.let { throw it }
    }

    override suspend fun registrar(
        email: String,
        senha01: String,
        senha02: String,
    ) {
        validarRegistro(email, senha01, senha02)
        excecaoRede?.let { throw it }
    }

    override fun usuarioAtualId(): String? = usuarioId
}
