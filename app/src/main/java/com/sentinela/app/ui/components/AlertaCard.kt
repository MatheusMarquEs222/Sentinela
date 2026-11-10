package com.sentinela.app.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sentinela.app.model.Alerta
import com.sentinela.app.model.Severidade
import com.sentinela.app.ui.theme.SentinelaTheme
import com.sentinela.app.ui.util.horarioFormatado
import com.sentinela.app.ui.theme.SeveridadeAlta
import com.sentinela.app.ui.theme.SeveridadeAltaEscuro
import com.sentinela.app.ui.theme.SeveridadeAltaFundo
import com.sentinela.app.ui.theme.SeveridadeAltaFundoEscuro
import com.sentinela.app.ui.theme.SeveridadeBaixa
import com.sentinela.app.ui.theme.SeveridadeBaixaEscuro
import com.sentinela.app.ui.theme.SeveridadeBaixaFundo
import com.sentinela.app.ui.theme.SeveridadeBaixaFundoEscuro
import com.sentinela.app.ui.theme.SeveridadeMedia
import com.sentinela.app.ui.theme.SeveridadeMediaEscuro
import com.sentinela.app.ui.theme.SeveridadeMediaFundo
import com.sentinela.app.ui.theme.SeveridadeMediaFundoEscuro

/** Cor do texto/ícone, cor de fundo e ícone que representam cada severidade. */
data class EstiloSeveridade(val cor: Color, val fundo: Color, val icone: ImageVector)

/**
 * A severidade nunca é indicada só por cor: cada nível tem também um ícone
 * próprio e o rótulo escrito na pílula do cartão.
 */
@Composable
fun estiloDaSeveridade(severidade: Severidade): EstiloSeveridade {
    val escuro = isSystemInDarkTheme()
    return when (severidade) {
        Severidade.BAIXA -> EstiloSeveridade(
            cor = if (escuro) SeveridadeBaixaEscuro else SeveridadeBaixa,
            fundo = if (escuro) SeveridadeBaixaFundoEscuro else SeveridadeBaixaFundo,
            icone = Icons.Filled.Info
        )
        Severidade.MEDIA -> EstiloSeveridade(
            cor = if (escuro) SeveridadeMediaEscuro else SeveridadeMedia,
            fundo = if (escuro) SeveridadeMediaFundoEscuro else SeveridadeMediaFundo,
            icone = Icons.Filled.Notifications
        )
        Severidade.ALTA -> EstiloSeveridade(
            cor = if (escuro) SeveridadeAltaEscuro else SeveridadeAlta,
            fundo = if (escuro) SeveridadeAltaFundoEscuro else SeveridadeAltaFundo,
            icone = Icons.Filled.Warning
        )
    }
}

/**
 * Cartão que exibe um único alerta. Componente stateless: recebe os dados
 * prontos e apenas os renderiza, sem guardar nenhum estado próprio.
 * Alertas oficiais ganham uma faixa de cabeçalho e borda na cor da severidade.
 */
@Composable
fun AlertaCard(alerta: Alerta, modifier: Modifier = Modifier) {
    val estilo = estiloDaSeveridade(alerta.severidade)

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = if (alerta.oficial) {
            BorderStroke(2.dp, estilo.cor)
        } else {
            BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
        }
    ) {
        if (alerta.oficial) {
            CabecalhoOficial(cor = estilo.cor)
        }
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(estilo.fundo, RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(estilo.icone, contentDescription = null, tint = estilo.cor)
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = alerta.tipo,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    InfoComIcone(icone = Icons.Filled.LocationOn, texto = alerta.local)
                }
                PilulaSeveridade(severidade = alerta.severidade, estilo = estilo)
            }
            if (alerta.descricao.isNotBlank()) {
                Text(text = alerta.descricao, style = MaterialTheme.typography.bodyMedium)
            }
            InfoComIcone(icone = Icons.Filled.DateRange, texto = alerta.horarioFormatado())
        }
    }
}

@Composable
private fun CabecalhoOficial(cor: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(cor)
            .padding(horizontal = 14.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        val corTexto = MaterialTheme.colorScheme.surface
        Icon(
            Icons.Filled.CheckCircle,
            contentDescription = null,
            tint = corTexto,
            modifier = Modifier.size(16.dp)
        )
        Text(
            text = "OFICIAL · DEFESA CIVIL",
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = corTexto,
            modifier = Modifier.weight(1f)
        )
        // Enquanto não houver integração real (R04), o alerta oficial é simulado.
        Surface(shape = RoundedCornerShape(6.dp), color = corTexto) {
            Text(
                text = "Simulação",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = cor,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
            )
        }
    }
}

@Composable
private fun PilulaSeveridade(severidade: Severidade, estilo: EstiloSeveridade) {
    Surface(shape = RoundedCornerShape(50), color = estilo.fundo) {
        Text(
            text = severidade.rotulo,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = estilo.cor,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
        )
    }
}

@Composable
private fun InfoComIcone(icone: ImageVector, texto: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            icone,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(15.dp)
        )
        Text(
            text = texto,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AlertaCardPreview() {
    SentinelaTheme(dynamicColor = false) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            AlertaCard(
                Alerta(1, "Risco de alagamento", "Bairro Vila Nova",
                    "Chuva forte prevista para as próximas horas.", Severidade.ALTA, System.currentTimeMillis())
            )
            AlertaCard(
                Alerta(2, "Alerta oficial de defesa civil", "Toda a cidade",
                    "Risco de deslizamento em áreas de encosta.", Severidade.ALTA, System.currentTimeMillis(), oficial = true)
            )
        }
    }
}
