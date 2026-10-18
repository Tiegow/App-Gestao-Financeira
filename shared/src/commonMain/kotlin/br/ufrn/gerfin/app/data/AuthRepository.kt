package br.ufrn.gerfin.app.data

import br.ufrn.gerfin.app.validation.validarLogin
import br.ufrn.gerfin.app.validation.validarRegistro
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email

suspend fun login(
    emailDigitado: String,
    senhaDigitada: String,
) {
    validarLogin(emailDigitado, senhaDigitada)

    supabase.auth.signInWith(Email) {
        email = emailDigitado
        password = senhaDigitada
    }
}

suspend fun register(
    emailDigitado: String,
    senhaDigitada01: String,
    senhaDigitada02: String,
) {
    validarRegistro(emailDigitado, senhaDigitada01, senhaDigitada02)

    supabase.auth.signUpWith(Email) {
        email = emailDigitado
        password = senhaDigitada01
    }
}
