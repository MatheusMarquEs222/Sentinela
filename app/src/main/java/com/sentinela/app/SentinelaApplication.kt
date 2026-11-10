package com.sentinela.app

import android.app.Application
import com.sentinela.app.data.AppContainer
import com.sentinela.app.data.alertasIniciais
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class SentinelaApplication : Application() {

    /** Escopo que vive enquanto o processo do app existir. */
    private val escopoAplicacao = CoroutineScope(SupervisorJob())

    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
        escopoAplicacao.launch {
            container.alertaRepository.popularSeVazio(alertasIniciais())
        }
    }
}
