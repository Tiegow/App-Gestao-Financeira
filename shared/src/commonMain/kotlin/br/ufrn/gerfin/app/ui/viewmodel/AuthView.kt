package br.ufrn.gerfin.app.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class AuthView : ViewModel() {
    var mensagemErro by mutableStateOf<String?>(null)
        private set
}
