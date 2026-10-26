package com.sentinela.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sentinela.app.model.Alerta
import com.sentinela.app.ui.theme.SentinelaTheme

/**
 * Renderiza a lista de alertas ativos. Componente stateless: apenas recebe
 * a lista já pronta e a desenha, sem conhecer de onde ela veio.
 */
@Composable
fun ListaAlertas(alertas: List<Alerta>, modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxWidth()) {
        CabecalhoSecao(total = alertas.size)

        if (alertas.isEmpty()) {
            EstadoVazio(modifier = Modifier.weight(1f))
            return@Column
        }

        // A LazyColumn mantém a posição pelo item que estava no topo; sem isso,
        // um alerta novo entraria acima da área visível e ficaria escondido.
        val estadoLista = rememberLazyListState()
        LaunchedEffect(alertas.first().id) {
            estadoLista.animateScrollToItem(0)
        }
        LazyColumn(
            state = estadoLista,
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            // Espaço extra no fim para o último cartão não ficar sob o botão flutuante.
            contentPadding = PaddingValues(top = 4.dp, bottom = 96.dp)
        ) {
            items(items = alertas, key = { it.id }) { alerta ->
                AlertaCard(alerta = alerta)
            }
        }
    }
}

@Composable
private fun CabecalhoSecao(total: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 18.dp, bottom = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "ALERTAS ATIVOS",
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Surface(shape = RoundedCornerShape(50), color = MaterialTheme.colorScheme.surfaceVariant) {
            Text(
                text = total.toString(),
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
            )
        }
        HorizontalDivider(modifier = Modifier.weight(1f))
    }
}

@Composable
private fun EstadoVazio(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 48.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(88.dp)
                    .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(28.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Filled.Notifications,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(40.dp)
                )
            }
            Text(
                text = "Nenhum alerta no momento.",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Quando houver uma ocorrência na sua região, ela aparece aqui. " +
                    "Viu algo acontecendo? Registre um alerta.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview(showBackground = true, heightDp = 500)
@Composable
private fun ListaVaziaPreview() {
    SentinelaTheme(dynamicColor = false) {
        ListaAlertas(alertas = emptyList(), modifier = Modifier.padding(horizontal = 16.dp))
    }
}
