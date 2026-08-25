package com.sentinela.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.sentinela.app.model.Alerta
import com.sentinela.app.model.Severidade
import com.sentinela.app.ui.theme.SeveridadeAlta
import com.sentinela.app.ui.theme.SeveridadeBaixa
import com.sentinela.app.ui.theme.SeveridadeMedia

private fun corDaSeveridade(severidade: Severidade): Color = when (severidade) {
    Severidade.BAIXA -> SeveridadeBaixa
    Severidade.MEDIA -> SeveridadeMedia
    Severidade.ALTA -> SeveridadeAlta
}

/**
 * Cartão que exibe um único alerta. Componente stateless: recebe os dados
 * prontos e apenas os renderiza, sem guardar nenhum estado próprio.
 */
@Composable
fun AlertaCard(alerta: Alerta, modifier: Modifier = Modifier) {
    Card(modifier = modifier.fillMaxWidth()) {
        Row(modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min)) {
            Box(
                modifier = Modifier
                    .width(6.dp)
                    .fillMaxHeight()
                    .background(corDaSeveridade(alerta.severidade))
            )
            Column(modifier = Modifier.padding(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = alerta.tipo, style = MaterialTheme.typography.titleSmall)
                    Text(
                        text = alerta.severidade.rotulo,
                        style = MaterialTheme.typography.labelSmall,
                        color = corDaSeveridade(alerta.severidade)
                    )
                }
                Text(text = alerta.local, style = MaterialTheme.typography.bodySmall)
                if (alerta.descricao.isNotBlank()) {
                    Text(text = alerta.descricao, style = MaterialTheme.typography.bodyMedium)
                }
                Text(text = alerta.horario, style = MaterialTheme.typography.labelSmall)
            }
        }
    }
}
