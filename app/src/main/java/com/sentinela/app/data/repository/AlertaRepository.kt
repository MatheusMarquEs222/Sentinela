package com.sentinela.app.data.repository

import com.sentinela.app.model.Alerta
import kotlinx.coroutines.flow.Flow

/**
 * Ponto único de acesso aos alertas para os ViewModels. Esconde de onde os
 * dados vêm (hoje o Room; depois também a API de alertas oficiais).
 */
interface AlertaRepository {
    fun observarAlertas(): Flow<List<Alerta>>
    fun observarAlerta(id: Long): Flow<Alerta?>
    suspend fun registrar(alerta: Alerta): Long
    suspend fun atualizar(alerta: Alerta)
    suspend fun excluir(alerta: Alerta)
    suspend fun limparHistorico()

    /** Grava [alertas] apenas se o banco ainda estiver vazio (primeira execução). */
    suspend fun popularSeVazio(alertas: List<Alerta>)
}
