package com.sentinela.app.ui.screens.alertas

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sentinela.app.model.Alerta
import com.sentinela.app.model.Severidade
import com.sentinela.app.ui.components.FormularioAlerta
import com.sentinela.app.ui.components.ListaAlertas
import com.sentinela.app.ui.theme.SentinelaTheme

/**
 * Tela principal do Sentinela. Obtém o [AlertasViewModel] e repassa seu estado
 * para [ConteudoTelaAlertas]; não guarda estado nem contém regras de negócio.
 */
@Composable
fun TelaAlertas(
    modifier: Modifier = Modifier,
    viewModel: AlertasViewModel = viewModel(factory = AlertasViewModel.Factory)
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val formulario by viewModel.formulario.collectAsStateWithLifecycle()
    val mensagem by viewModel.mensagem.collectAsStateWithLifecycle()

    ConteudoTelaAlertas(
        uiState = uiState,
        formulario = formulario,
        mensagem = mensagem,
        onAbrirFormulario = viewModel::abrirFormulario,
        onFecharFormulario = viewModel::fecharFormulario,
        onRegistrar = viewModel::registrarAlerta,
        onCampoAlterado = viewModel::limparErroFormulario,
        onSimularOficial = viewModel::simularAlertaOficial,
        onDispensarMensagem = viewModel::dispensarMensagem,
        modifier = modifier
    )
}

/** Versão stateless da tela: tudo chega por parâmetro e sai por callback. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConteudoTelaAlertas(
    uiState: AlertasUiState,
    formulario: FormularioUiState,
    mensagem: String?,
    onAbrirFormulario: () -> Unit,
    onFecharFormulario: () -> Unit,
    onRegistrar: (DadosFormulario) -> Unit,
    onCampoAlterado: () -> Unit,
    onSimularOficial: () -> Unit,
    onDispensarMensagem: () -> Unit,
    modifier: Modifier = Modifier
) {
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
                        onClick = onSimularOficial,
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
                onClick = onAbrirFormulario,
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
            mensagem?.let { texto ->
                BannerConfirmacao(
                    mensagem = texto,
                    onFechar = onDispensarMensagem,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            when (uiState) {
                AlertasUiState.Carregando -> Box(
                    modifier = Modifier.weight(1f).fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }

                is AlertasUiState.Erro -> Box(
                    modifier = Modifier.weight(1f).fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = uiState.mensagem, color = MaterialTheme.colorScheme.error)
                }

                is AlertasUiState.Sucesso -> ListaAlertas(
                    alertas = uiState.alertas,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        if (formulario.aberto) {
            ModalBottomSheet(
                onDismissRequest = onFecharFormulario,
                sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
            ) {
                FormularioAlerta(
                    erro = formulario.erro,
                    onSubmit = onRegistrar,
                    onCampoAlterado = onCampoAlterado,
                    onCancelar = onFecharFormulario
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
        ConteudoTelaAlertas(
            uiState = AlertasUiState.Sucesso(
                listOf(
                    Alerta(
                        id = 1,
                        tipo = "Risco de alagamento",
                        local = "Bairro Vila Nova",
                        descricao = "Chuva forte prevista para as próximas horas.",
                        severidade = Severidade.ALTA,
                        criadoEm = System.currentTimeMillis()
                    )
                )
            ),
            formulario = FormularioUiState(),
            mensagem = null,
            onAbrirFormulario = {},
            onFecharFormulario = {},
            onRegistrar = {},
            onCampoAlterado = {},
            onSimularOficial = {},
            onDispensarMensagem = {},
            modifier = Modifier.fillMaxSize()
        )
    }
}
