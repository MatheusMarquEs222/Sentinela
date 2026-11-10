package com.sentinela.app.ui.screens.alertas

import com.sentinela.app.model.Alerta
import com.sentinela.app.model.Severidade

/** Estado da lista de alertas exibida na tela principal. */
sealed interface AlertasUiState {
    data object Carregando : AlertasUiState
    data class Sucesso(val alertas: List<Alerta>) : AlertasUiState
    data class Erro(val mensagem: String) : AlertasUiState
}

/** Estado do formulário de registro (aberto ou não e a mensagem de validação). */
data class FormularioUiState(
    val aberto: Boolean = false,
    val erro: String? = null
)

/** Dados digitados no formulário, ainda sem validação. */
data class DadosFormulario(
    val tipo: String,
    val local: String,
    val descricao: String,
    val severidade: Severidade
)
