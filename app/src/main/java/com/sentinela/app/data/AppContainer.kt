package com.sentinela.app.data

import android.content.Context
import com.sentinela.app.data.local.SentinelaDatabase
import com.sentinela.app.data.repository.AlertaRepository
import com.sentinela.app.data.repository.AlertaRepositoryLocal

/**
 * Injeção de dependências manual: cria uma única instância de cada objeto da
 * camada de dados. O [Context] fica restrito aqui e nunca chega aos ViewModels.
 */
class AppContainer(context: Context) {
    private val database = SentinelaDatabase.criar(context)

    val alertaRepository: AlertaRepository = AlertaRepositoryLocal(database.alertaDao())
}
