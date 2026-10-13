package com.sentinela.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sentinela.app.model.Alerta
import com.sentinela.app.model.Severidade
import com.sentinela.app.ui.components.FormularioAlerta
import com.sentinela.app.ui.components.ListaAlertas
import com.sentinela.app.ui.theme.SentinelaTheme

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
            horario = "agora",
            oficial = true
        )
        proximoId++
        alertas.add(0, alertaOficial)
        mensagemConfirmacao = "Novo alerta recebido: ${alertaOficial.tipo}"
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Sentinela", fontWeight = FontWeight.Bold)
                        Text(
                            text = "Alertas climáticos e de defesa civil",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                actions = {
                    FilledTonalButton(
                        onClick = { simularAlertaOficial() },
                        contentPadding = ButtonDefaults.ButtonWithIconContentPadding,
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Notifications,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(Modifier.width(6.dp))
                        Text("Simular oficial")
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { mostrarFormulario = true },
                icon = { Icon(Icons.Filled.Add, contentDescription = null) },
                text = { Text("Registrar alerta") }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            mensagemConfirmacao?.let { mensagem ->
                BannerConfirmacao(
                    mensagem = mensagem,
                    onFechar = { mensagemConfirmacao = null },
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            ListaAlertas(
                alertas = alertas,
                modifier = Modifier.weight(1f)
            )
        }

        if (mostrarFormulario) {
            ModalBottomSheet(
                onDismissRequest = { mostrarFormulario = false },
                sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
            ) {
                FormularioAlerta(
                    onSubmit = ::registrarAlerta,
                    onCancelar = { mostrarFormulario = false }
                )
            }
        }
    }
}

/** Mensagem de confirmação exibida acima da lista, fechada pelo botão "Fechar". */
@Composable
private fun BannerConfirmacao(mensagem: String, onFechar: () -> Unit, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
    ) {
        Row(
            modifier = Modifier.padding(start = 14.dp, end = 4.dp, top = 4.dp, bottom = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Icon(Icons.Filled.CheckCircle, contentDescription = null, modifier = Modifier.size(20.dp))
            Text(
                text = mensagem,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.weight(1f)
            )
            TextButton(onClick = onFechar) {
                Text("Fechar", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun TelaAlertasPreview() {
    SentinelaTheme(dynamicColor = false) {
        TelaAlertas(modifier = Modifier.fillMaxSize())
    }
}
