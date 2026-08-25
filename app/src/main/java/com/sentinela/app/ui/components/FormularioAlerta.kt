package com.sentinela.app.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sentinela.app.model.Alerta
import com.sentinela.app.model.Severidade

/**
 * Formulário para registrar um novo alerta.
 *
 * O componente é "stateless" do ponto de vista de quem o usa: não recebe nem
 * expõe a lista de alertas, apenas devolve um [Alerta] pronto via [onSubmit]
 * quando o usuário confirma. Os campos de texto (título, local, descrição)
 * são estado de edição local — via [mutableStateOf]/[remember] — que só "sobe"
 * para o componente pai (a tela) no momento do envio, seguindo o padrão de
 * state hoisting.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormularioAlerta(
    onSubmit: (Alerta) -> Unit,
    onCancelar: () -> Unit,
    modifier: Modifier = Modifier
) {
    var titulo by remember { mutableStateOf("") }
    var local by remember { mutableStateOf("") }
    var descricao by remember { mutableStateOf("") }
    var severidade by remember { mutableStateOf(Severidade.BAIXA) }
    var mensagemDeErro by remember { mutableStateOf<String?>(null) }

    Card(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(text = "Registrar novo alerta", style = MaterialTheme.typography.titleMedium)

            OutlinedTextField(
                value = titulo,
                onValueChange = {
                    titulo = it
                    mensagemDeErro = null
                },
                label = { Text("Tipo de ocorrência") },
                placeholder = { Text("Ex: Alagamento, vendaval...") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = local,
                onValueChange = {
                    local = it
                    mensagemDeErro = null
                },
                label = { Text("Local / bairro") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = descricao,
                onValueChange = { descricao = it },
                label = { Text("Descrição") },
                minLines = 2,
                maxLines = 4,
                modifier = Modifier.fillMaxWidth()
            )

            Text(text = "Nível de severidade", style = MaterialTheme.typography.labelLarge)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Severidade.entries.forEach { opcao ->
                    FilterChip(
                        selected = severidade == opcao,
                        onClick = { severidade = opcao },
                        label = { Text(opcao.rotulo) }
                    )
                }
            }

            mensagemDeErro?.let { erro ->
                Text(
                    text = erro,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedButton(onClick = onCancelar, modifier = Modifier.weight(1f)) {
                    Text("Cancelar")
                }
                Button(
                    modifier = Modifier.weight(1f),
                    onClick = {
                        if (titulo.isBlank() || local.isBlank()) {
                            mensagemDeErro = "Preencha ao menos o tipo e o local da ocorrência."
                            return@Button
                        }
                        onSubmit(
                            Alerta(
                                id = 0, // o id definitivo é atribuído por quem recebe o alerta
                                tipo = titulo.trim(),
                                local = local.trim(),
                                descricao = descricao.trim(),
                                severidade = severidade,
                                horario = "agora"
                            )
                        )
                        titulo = ""
                        local = ""
                        descricao = ""
                        severidade = Severidade.BAIXA
                    }
                ) {
                    Text("Registrar")
                }
            }
        }
    }
}
