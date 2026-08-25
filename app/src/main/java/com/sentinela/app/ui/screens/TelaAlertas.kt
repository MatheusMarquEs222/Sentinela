package com.sentinela.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sentinela.app.model.Alerta
import com.sentinela.app.model.Severidade
import com.sentinela.app.ui.components.FormularioAlerta
import com.sentinela.app.ui.components.ListaAlertas

private fun alertasIniciais(): List<Alerta> = listOf(
    Alerta(
        id = 1,
        tipo = "Risco de alagamento",
        local = "Bairro Vila Nova",
        descricao = "Chuva forte prevista para as próximas horas; ruas baixas podem alagar.",
        severidade = Severidade.ALTA,
        horario = "08:12"
    ),
    Alerta(
        id = 2,
        tipo = "Vendaval",
        local = "Região Central",
        descricao = "Rajadas de vento acima de 60 km/h detectadas pela estação local.",
        severidade = Severidade.MEDIA,
        horario = "07:40"
    ),
    Alerta(
        id = 3,
        tipo = "Qualidade do ar",
        local = "Zona Industrial",
        descricao = "Índice de poluição levemente acima da média para o período.",
        severidade = Severidade.BAIXA,
        horario = "06:55"
    )
)

/**
 * Tela principal do Sentinela — "stateful": é aqui que mora o estado real
 * (a lista de alertas, a visibilidade do formulário e a mensagem de
 * confirmação). Os componentes que ela usa ([ListaAlertas], [FormularioAlerta])
 * são todos stateless e recebem tudo via parâmetros/callbacks, seguindo o
 * padrão de state hoisting.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaAlertas(modifier: Modifier = Modifier) {
    var mostrarFormulario by rememberSaveable { mutableStateOf(false) }
    var mensagemConfirmacao by rememberSaveable { mutableStateOf<String?>(null) }
    val alertas = remember { alertasIniciais().toMutableStateList() }
    var proximoId by remember { mutableStateOf(alertas.size + 1) }

    fun registrarAlerta(novoAlerta: Alerta) {
        alertas.add(0, novoAlerta.copy(id = proximoId))
        proximoId++
        mostrarFormulario = false
        mensagemConfirmacao = "Alerta \"${novoAlerta.tipo}\" registrado com sucesso!"
    }

    fun simularAlertaOficial() {
        val alertaOficial = Alerta(
            id = proximoId,
            tipo = "Alerta oficial de defesa civil",
            local = "Toda a cidade",
            descricao = "Risco de deslizamento em áreas de encosta. Evite deslocamentos desnecessários.",
            severidade = Severidade.ALTA,
            horario = "agora"
        )
        proximoId++
        alertas.add(0, alertaOficial)
        mensagemConfirmacao = "Novo alerta recebido: ${alertaOficial.tipo}"
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text("Sentinela") },
                actions = {
                    IconButton(onClick = { simularAlertaOficial() }) {
                        Icon(
                            imageVector = Icons.Filled.Notifications,
                            contentDescription = "Simular alerta oficial"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { mostrarFormulario = !mostrarFormulario }) {
                Icon(
                    imageVector = if (mostrarFormulario) Icons.Filled.Close else Icons.Filled.Add,
                    contentDescription = if (mostrarFormulario) "Fechar formulário" else "Registrar novo alerta"
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            mensagemConfirmacao?.let { mensagem ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, end = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = mensagem,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(onClick = { mensagemConfirmacao = null }) {
                            Icon(imageVector = Icons.Filled.Close, contentDescription = "Fechar mensagem")
                        }
                    }
                }
            }

            if (mostrarFormulario) {
                FormularioAlerta(
                    onSubmit = ::registrarAlerta,
                    onCancelar = { mostrarFormulario = false },
                    modifier = Modifier.padding(vertical = 12.dp)
                )
                HorizontalDivider(modifier = Modifier.padding(bottom = 8.dp))
            }

            ListaAlertas(
                alertas = alertas,
                modifier = Modifier.weight(1f)
            )
        }
    }
}
