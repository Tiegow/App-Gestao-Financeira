package br.ufrn.gerfin.app.validation

import br.ufrn.gerfin.app.exception.CadastroException

fun validarRegistro(
    email: String,
    senha01: String,
    senha02: String,
) {
    val regex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$".toRegex()
    val emailValido = regex.matches(email)

    if (email.isBlank()) {
        throw CadastroException("Você deve informar o e-mail!")
    }
    if (senha01.isBlank()) {
        throw CadastroException("Você deve informar uma senha!")
    }
    if (senha02.isBlank()) {
        throw CadastroException("Você deve informe a confirmação da senha!")
    }

    if (!emailValido) {
        throw CadastroException("Informe um email válido!")
    }
    if (senha01.length < 6 || senha02.length < 6) {
        throw CadastroException("As senhas devem ter mais de 6 caracteres!")
    }
    if (senha01 != senha02) {
        throw CadastroException("As senhas devem ser iguais!")
    }
}
