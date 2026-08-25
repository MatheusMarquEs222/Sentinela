package com.sentinela.app.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sentinela.app.model.Alerta

/**
 * Renderiza a lista de alertas ativos. Componente stateless: apenas recebe
 * a lista já pronta e a desenha, sem conhecer de onde ela veio.
 */
@Composable
fun ListaAlertas(alertas: List<Alerta>, modifier: Modifier = Modifier) {
    if (alertas.isEmpty()) {
        Box(modifier = modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            Text(
                text = "Nenhum alerta no momento.",
                style = MaterialTheme.typography.bodyMedium
            )
        }
        return
    }

    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        items(items = alertas, key = { it.id }) { alerta ->
            AlertaCard(alerta = alerta)
        }
    }
}
