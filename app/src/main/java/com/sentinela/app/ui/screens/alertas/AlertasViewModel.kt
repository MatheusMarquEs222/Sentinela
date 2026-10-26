package com.sentinela.app.ui.screens.alertas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.sentinela.app.SentinelaApplication
import com.sentinela.app.data.repository.AlertaRepository
import com.sentinela.app.model.Alerta
import com.sentinela.app.model.Severidade
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Concentra o estado e as regras da tela de alertas. A tela apenas observa os
 * [StateFlow]s expostos e repassa os eventos do usuário para as funções públicas.
 *
 * @param relogio fonte do horário atual; substituível nos testes.
 */
class AlertasViewModel(
    private val repository: AlertaRepository,
    private val relogio: () -> Long = System::currentTimeMillis
) : ViewModel() {

    val uiState: StateFlow<AlertasUiState> = repository.observarAlertas()
        .map<List<Alerta>, AlertasUiState> { AlertasUiState.Sucesso(it) }
        .catch { emit(AlertasUiState.Erro("Não foi possível carregar os alertas.")) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = AlertasUiState.Carregando
        )

    private val _formulario = MutableStateFlow(FormularioUiState())
    val formulario: StateFlow<FormularioUiState> = _formulario.asStateFlow()

    private val _mensagem = MutableStateFlow<String?>(null)
    val mensagem: StateFlow<String?> = _mensagem.asStateFlow()

    fun abrirFormulario() {
        _formulario.value = FormularioUiState(aberto = true)
    }

    fun fecharFormulario() {
        _formulario.value = FormularioUiState(aberto = false)
    }

    fun limparErroFormulario() {
        _formulario.update { it.copy(erro = null) }
    }

    fun registrarAlerta(dados: DadosFormulario) {
        if (dados.tipo.isBlank() || dados.local.isBlank()) {
            _formulario.update { it.copy(erro = MENSAGEM_CAMPOS_OBRIGATORIOS) }
            return
        }
        val alerta = Alerta(
            tipo = dados.tipo.trim(),
            local = dados.local.trim(),
            descricao = dados.descricao.trim(),
            severidade = dados.severidade,
            criadoEm = relogio()
        )
        viewModelScope.launch {
            repository.registrar(alerta)
            fecharFormulario()
            _mensagem.value = "Alerta \"${alerta.tipo}\" registrado com sucesso!"
        }
    }

    /** Simula a chegada de um alerta oficial até a integração com a API (R04). */
    fun simularAlertaOficial() {
        val alerta = Alerta(
            tipo = "Alerta oficial de defesa civil",
            local = "Toda a cidade",
            descricao = "Risco de deslizamento em áreas de encosta. Evite deslocamentos desnecessários.",
            severidade = Severidade.ALTA,
            criadoEm = relogio(),
            oficial = true
        )
        viewModelScope.launch {
            repository.registrar(alerta)
            _mensagem.value = "Novo alerta recebido: ${alerta.tipo}"
        }
    }

    fun dispensarMensagem() {
        _mensagem.value = null
    }

    companion object {
        const val MENSAGEM_CAMPOS_OBRIGATORIOS = "Preencha ao menos o tipo e o local da ocorrência."

        val Factory = viewModelFactory {
            initializer {
                val aplicacao = this[APPLICATION_KEY] as SentinelaApplication
                AlertasViewModel(aplicacao.container.alertaRepository)
            }
        }
    }
}
