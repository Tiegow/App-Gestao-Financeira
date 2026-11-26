package br.ufrn.gerfin.app.data

import br.ufrn.gerfin.app.domain.repository.AuthRepository
import br.ufrn.gerfin.app.domain.validation.validarLogin
import br.ufrn.gerfin.app.domain.validation.validarRegistro
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email

class SupabaseAuthRepository(
    private val client: SupabaseClient = supabase,
) : AuthRepository {
    override suspend fun login(
        email: String,
        senha: String,
    ) {
        validarLogin(email, senha)

        client.auth.signInWith(Email) {
            this.email = email
            this.password = senha
        }
    }

    override suspend fun registrar(
        email: String,
        senha01: String,
        senha02: String,
    ) {
        validarRegistro(email, senha01, senha02)

        client.auth.signUpWith(Email) {
            this.email = email
            this.password = senha01
        }
    }

    override fun usuarioAtualId(): String? = client.auth.currentUserOrNull()?.id
}
