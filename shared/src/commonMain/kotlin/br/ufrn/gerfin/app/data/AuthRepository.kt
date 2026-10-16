package br.ufrn.gerfin.app.data

import br.ufrn.gerfin.app.exception.CadastroException
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email

suspend fun login(emailDigitado: String, senhaDigitada: String) {
    supabase.auth.signInWith(Email) {
        email = emailDigitado
        password = senhaDigitada
    }
}

suspend fun register(emailDigitado: String, senhaDigitada01: String, senhaDigitada02: String) {

    if(senhaDigitada01 != senhaDigitada02){
        throw CadastroException("As senhas devem ser iguais!")
    }

    supabase.auth.signUpWith(Email) {
        email = emailDigitado
        password = senhaDigitada01
    }
}

