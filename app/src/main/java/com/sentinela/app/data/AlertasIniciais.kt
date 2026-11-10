package com.sentinela.app.data

import com.sentinela.app.model.Alerta
import com.sentinela.app.model.Severidade
import java.time.LocalDate
import java.time.ZoneId

private fun hojeAs(hora: Int, minuto: Int): Long =
    LocalDate.now().atTime(hora, minuto).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

/** Alertas de exemplo gravados no banco na primeira execução do app. */
fun alertasIniciais(): List<Alerta> = listOf(
    Alerta(
        tipo = "Risco de alagamento",
        local = "Bairro Vila Nova",
        descricao = "Chuva forte prevista para as próximas horas; ruas baixas podem alagar.",
        severidade = Severidade.ALTA,
        criadoEm = hojeAs(8, 12)
    ),
    Alerta(
        tipo = "Vendaval",
        local = "Região Central",
        descricao = "Rajadas de vento acima de 60 km/h detectadas pela estação local.",
        severidade = Severidade.MEDIA,
        criadoEm = hojeAs(7, 40)
    ),
    Alerta(
        tipo = "Qualidade do ar",
        local = "Zona Industrial",
        descricao = "Índice de poluição levemente acima da média para o período.",
        severidade = Severidade.BAIXA,
        criadoEm = hojeAs(6, 55)
    )
)
