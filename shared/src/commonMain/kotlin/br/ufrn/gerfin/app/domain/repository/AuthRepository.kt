package br.ufrn.gerfin.app.domain.repository

interface AuthRepository {
    suspend fun login(
        email: String,
        senha: String,
    )

    suspend fun registrar(
        email: String,
        senha01: String,
        senha02: String,
    )

    fun usuarioAtualId(): String?
}
