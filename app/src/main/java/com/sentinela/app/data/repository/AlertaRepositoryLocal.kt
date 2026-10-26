package com.sentinela.app.data.repository

import com.sentinela.app.data.local.AlertaDao
import com.sentinela.app.data.local.paraEntidade
import com.sentinela.app.data.local.paraModelo
import com.sentinela.app.model.Alerta
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

/** Implementação do [AlertaRepository] apoiada no banco Room. */
class AlertaRepositoryLocal(
    private val dao: AlertaDao,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : AlertaRepository {

    override fun observarAlertas(): Flow<List<Alerta>> =
        dao.observarTodos()
            .map { entidades -> entidades.map { it.paraModelo() } }
            .flowOn(dispatcher)

    override fun observarAlerta(id: Long): Flow<Alerta?> =
        dao.observarPorId(id)
            .map { it?.paraModelo() }
            .flowOn(dispatcher)

    override suspend fun registrar(alerta: Alerta): Long = withContext(dispatcher) {
        dao.inserir(alerta.copy(id = 0).paraEntidade())
    }

    override suspend fun atualizar(alerta: Alerta) = withContext(dispatcher) {
        dao.atualizar(alerta.paraEntidade())
    }

    override suspend fun excluir(alerta: Alerta) = withContext(dispatcher) {
        dao.excluir(alerta.paraEntidade())
    }

    override suspend fun limparHistorico() = withContext(dispatcher) {
        dao.excluirTodos()
    }

    override suspend fun popularSeVazio(alertas: List<Alerta>) = withContext(dispatcher) {
        if (dao.contar() == 0) {
            dao.inserirTodos(alertas.map { it.copy(id = 0).paraEntidade() })
        }
    }
}
