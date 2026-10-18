package br.ufrn.gerfin.app.validation

import br.ufrn.gerfin.app.exception.CadastroException

fun  validarLogin(
    email: String,
    senha: String
) {
    if (email.isBlank()) {
        throw CadastroException("Você deve informar o e-mail!")
    }
    if (senha.isBlank()) {
        throw CadastroException("Você deve informar uma senha!")
    }
}
