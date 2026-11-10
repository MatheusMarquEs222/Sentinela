package com.sentinela.app.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sentinela.app.model.Severidade
import com.sentinela.app.ui.screens.alertas.DadosFormulario
import com.sentinela.app.ui.theme.SentinelaTheme

/**
 * Formulário para registrar um novo alerta (exibido dentro de um bottom sheet
 * pela tela principal).
 *
 * Os campos de texto são estado de edição local — via [mutableStateOf]/[remember] —
 * que só "sobe" no envio, como [DadosFormulario] em [onSubmit]. A validação fica
 * no ViewModel, que devolve a mensagem em [erro]; [onCampoAlterado] avisa quando
 * o usuário volta a editar o tipo ou o local, para a mensagem ser removida.
 */
@Composable
fun FormularioAlerta(
    erro: String?,
    onSubmit: (DadosFormulario) -> Unit,
    onCampoAlterado: () -> Unit,
    onCancelar: () -> Unit,
    modifier: Modifier = Modifier
) {
    var titulo by remember { mutableStateOf("") }
    var local by remember { mutableStateOf("") }
    var descricao by remember { mutableStateOf("") }
    var severidade by remember { mutableStateOf(Severidade.BAIXA) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp)
            .padding(bottom = 20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Column {
            Text(
                text = "Registrar novo alerta",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Campos com * são obrigatórios.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        erro?.let { MensagemErro(it) }

        OutlinedTextField(
            value = titulo,
            onValueChange = {
                titulo = it
                onCampoAlterado()
            },
            label = { Text("Tipo de ocorrência *") },
            placeholder = { Text("Ex.: alagamento, vendaval") },
            isError = erro != null && titulo.isBlank(),
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = local,
            onValueChange = {
                local = it
                onCampoAlterado()
            },
            label = { Text("Local / bairro *") },
            placeholder = { Text("Ex.: Bairro Vila Nova") },
            isError = erro != null && local.isBlank(),
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = descricao,
            onValueChange = { descricao = it },
            label = { Text("Descrição (opcional)") },
            placeholder = { Text("O que está acontecendo?") },
            minLines = 3,
            maxLines = 5,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        )

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = "Nível de severidade",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Severidade.entries.forEach { opcao ->
                    BotaoSeveridade(
                        severidade = opcao,
                        selecionado = severidade == opcao,
                        onClick = { severidade = opcao },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedButton(
                onClick = onCancelar,
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
            ) {
                Text("Cancelar")
            }
            Button(
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp),
                onClick = {
                    onSubmit(DadosFormulario(titulo, local, descricao, severidade))
                }
            ) {
                Text("Registrar")
            }
        }
    }
}

@Composable
private fun MensagemErro(texto: String) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.errorContainer,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                Icons.Filled.Warning,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onErrorContainer,
                modifier = Modifier.size(18.dp)
            )
            Text(
                text = texto,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onErrorContainer
            )
        }
    }
}

/** Botão de escolha de severidade: a opção selecionada usa as cores do nível e um check. */
@Composable
private fun BotaoSeveridade(
    severidade: Severidade,
    selecionado: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val estilo = estiloDaSeveridade(severidade)
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.height(44.dp),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(
            width = 1.5.dp,
            color = if (selecionado) estilo.cor else MaterialTheme.colorScheme.outline
        ),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = if (selecionado) estilo.fundo else MaterialTheme.colorScheme.surface,
            contentColor = if (selecionado) estilo.cor else MaterialTheme.colorScheme.onSurface
        ),
        contentPadding = ButtonDefaults.TextButtonContentPadding
    ) {
        if (selecionado) {
            Icon(Icons.Filled.Check, contentDescription = null, modifier = Modifier.size(16.dp))
        }
        Text(
            text = severidade.rotulo,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = if (selecionado) 4.dp else 0.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun FormularioAlertaPreview() {
    SentinelaTheme(dynamicColor = false) {
        FormularioAlerta(erro = null, onSubmit = {}, onCampoAlterado = {}, onCancelar = {})
    }
}
