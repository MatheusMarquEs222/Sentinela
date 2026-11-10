package com.sentinela.app.ui.screens.alertas

import com.sentinela.app.data.repository.AlertaRepository
import com.sentinela.app.model.Alerta
import com.sentinela.app.model.Severidade
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

private class RepositorioFalso : AlertaRepository {
    val alertas = MutableStateFlow<List<Alerta>>(emptyList())
    private var proximoId = 1L

    override fun observarAlertas(): Flow<List<Alerta>> = alertas
    override fun observarAlerta(id: Long): Flow<Alerta?> = alertas.map { lista -> lista.find { it.id == id } }

    override suspend fun registrar(alerta: Alerta): Long {
        val id = proximoId++
        alertas.value = listOf(alerta.copy(id = id)) + alertas.value
        return id
    }

    override suspend fun atualizar(alerta: Alerta) {
        alertas.value = alertas.value.map { if (it.id == alerta.id) alerta else it }
    }

    override suspend fun excluir(alerta: Alerta) {
        alertas.value = alertas.value.filterNot { it.id == alerta.id }
    }

    override suspend fun limparHistorico() {
        alertas.value = emptyList()
    }

    override suspend fun popularSeVazio(alertas: List<Alerta>) {
        if (this.alertas.value.isEmpty()) alertas.forEach { registrar(it) }
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
class AlertasViewModelTest {

    private val repositorio = RepositorioFalso()
    private lateinit var viewModel: AlertasViewModel

    @Before
    fun configurar() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        viewModel = AlertasViewModel(repositorio, relogio = { 1_000L })
    }

    @After
    fun limpar() {
        Dispatchers.resetMain()
    }

    @Test
    fun `registrar com dados validos grava alerta sem espacos externos e fecha o formulario`() = runTest {
        viewModel.abrirFormulario()

        viewModel.registrarAlerta(DadosFormulario("  Alagamento ", " Centro ", "  ", Severidade.MEDIA))

        val salvo = repositorio.alertas.value.single()
        assertEquals("Alagamento", salvo.tipo)
        assertEquals("Centro", salvo.local)
        assertEquals("", salvo.descricao)
        assertEquals(Severidade.MEDIA, salvo.severidade)
        assertEquals(1_000L, salvo.criadoEm)
        assertFalse(viewModel.formulario.value.aberto)
        assertEquals("Alerta \"Alagamento\" registrado com sucesso!", viewModel.mensagem.value)
    }

    @Test
    fun `registrar sem tipo ou local nao grava e mostra erro de validacao`() {
        viewModel.abrirFormulario()

        viewModel.registrarAlerta(DadosFormulario("   ", "Centro", "", Severidade.BAIXA))

        assertTrue(repositorio.alertas.value.isEmpty())
        assertTrue(viewModel.formulario.value.aberto)
        assertEquals(AlertasViewModel.MENSAGEM_CAMPOS_OBRIGATORIOS, viewModel.formulario.value.erro)

        viewModel.limparErroFormulario()
        assertNull(viewModel.formulario.value.erro)
    }

    @Test
    fun `simular alerta oficial grava alerta oficial de severidade alta`() {
        viewModel.simularAlertaOficial()

        val salvo = repositorio.alertas.value.single()
        assertTrue(salvo.oficial)
        assertEquals(Severidade.ALTA, salvo.severidade)
        assertEquals("Novo alerta recebido: ${salvo.tipo}", viewModel.mensagem.value)
    }

    @Test
    fun `uiState reflete os alertas do repositorio`() = runTest {
        val coleta = backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect {}
        }
        assertEquals(AlertasUiState.Sucesso(emptyList()), viewModel.uiState.value)

        viewModel.simularAlertaOficial()

        val estado = viewModel.uiState.value as AlertasUiState.Sucesso
        assertEquals(1, estado.alertas.size)
        coleta.cancel()
    }
}
