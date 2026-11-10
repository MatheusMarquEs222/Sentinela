package com.sentinela.app.model

/**
 * Nível de severidade de um alerta climático/de defesa civil.
 * Cada valor tem um rótulo em português para exibição direta na UI.
 */
enum class Severidade(val rotulo: String) {
    BAIXA("Baixa"),
    MEDIA("Média"),
    ALTA("Alta")
}

/**
 * Representa um alerta climático ou de defesa civil comunitária.
 *
 * É o modelo usado pelas camadas de UI e ViewModel; a camada de dados converte
 * de/para [com.sentinela.app.data.local.AlertaEntity] ao ler e gravar no Room.
 *
 * @property id identificador gerado pelo banco (0 enquanto o alerta não foi salvo).
 * @property criadoEm momento do registro, em milissegundos desde a época (epoch).
 */
data class Alerta(
    val id: Long = 0,
    val tipo: String,
    val local: String,
    val descricao: String,
    val severidade: Severidade,
    val criadoEm: Long,
    val oficial: Boolean = false
)
