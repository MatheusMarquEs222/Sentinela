# Sentinela

Projeto integrador — app de alertas climáticos e defesa civil comunitária.

## Sobre o MVP

O Sentinela nasceu da proposta de ajudar comunidades a se manterem informadas
sobre riscos climáticos (enchentes, vendavais, deslizamentos, etc.) e avisos
oficiais de defesa civil, exibindo notificações e alertas de forma dinâmica
no dispositivo móvel.

**Status atual:** esta entrega implementa a primeira tela funcional do app —
o registro e a visualização de alertas — com dados mockados em memória. A
integração com uma API pública real de alertas climáticos/defesa civil é um
próximo passo do projeto.

## Tecnologias

- Kotlin
- Jetpack Compose (UI 100% declarativa) + Material Design 3
- Gradle (Kotlin DSL)
- Kotlin 2.0.21 · AGP 9.0.1 · Compose BOM 2024.09.00
- `minSdk` 26 · `targetSdk`/`compileSdk` 36

## Tela implementada

A tela **Sentinela** (`TelaAlertas`) permite:

- Visualizar a lista de alertas ativos, cada um com tipo, local, descrição,
  horário e um indicador de severidade (Baixa / Média / Alta).
- Registrar um novo alerta através de um formulário (tipo, local, descrição
  e severidade), com validação simples dos campos obrigatórios.
- Ver uma mensagem de confirmação assim que um alerta é registrado.
- Simular o recebimento de um alerta oficial (botão no topo da tela), que
  aparece imediatamente no início da lista — uma prévia de como o app vai se
  comportar quando a API de alertas estiver integrada.

### Arquitetura da tela

```
ui/screens/TelaAlertas.kt      -> componente STATEFUL (guarda o estado)
ui/components/
  ListaAlertas.kt              -> stateless
  AlertaCard.kt                 -> stateless
  FormularioAlerta.kt           -> stateless (state hoisting)
model/Alerta.kt                -> dados do domínio (Alerta, Severidade)
ui/theme/                       -> cores e tipografia (Material 3)
```

`TelaAlertas` guarda o estado real (lista de alertas, visibilidade do
formulário, mensagem de confirmação) usando `mutableStateOf`/`remember`/
`rememberSaveable`, e repassa dados e callbacks para os componentes
filhos — que não guardam estado de negócio próprio, apenas o exibem ou o
devolvem para cima quando o usuário interage (state hoisting).

## Como rodar

1. Abra a pasta `Mobile/Sentinela` no Android Studio e aguarde o Gradle sync.
2. Rode no emulador ou em um dispositivo físico (▶ Run).

Ou, via linha de comando:

```bash
./gradlew assembleDebug
```

## Equipe

- Matheus Marques
- _(completar com os demais integrantes)_
