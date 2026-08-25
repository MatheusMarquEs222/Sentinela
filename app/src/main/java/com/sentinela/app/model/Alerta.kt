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
 * Nesta primeira entrega os alertas vivem apenas em memória (estado local da tela).
 * Quando a equipe integrar a API pública de alertas, esta classe passa a ser
 * populada a partir da resposta remota em vez de dados mockados.
 */
data class Alerta(
    val id: Int,
    val tipo: String,
    val local: String,
    val descricao: String,
    val severidade: Severidade,
    val horario: String
)
