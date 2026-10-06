# Requisitos Completos do Projeto

## 1. Escopo e convenções

Este documento descreve o comportamento observado no protótipo Android Sentinela e separa o que já existe do que está planejado ou depende de decisão da equipe. A análise considera o código e as configurações presentes no repositório em 06/10/2026. O produto analisado é exclusivamente mobile.

Os requisitos funcionais (RF) descrevem capacidades do sistema. Os requisitos não funcionais (RNF) registram metas de qualidade ou restrições observáveis. Cada requisito traz uma história de usuário e critérios de aceitação em BDD. Quando a meta ainda não está definida pelo projeto, ela está marcada como **A VALIDAR COM A EQUIPE**.

Os status têm o seguinte sentido:

- **[Implementado]**: comportamento presente no código no escopo descrito.
- **[Parcial]**: existe uma parte do comportamento, mas há limitações ou lacunas relevantes.
- **[Não implementado/planejado]**: o código indica intenção futura, mas o comportamento ainda não existe.

## 2. Requisitos Funcionais

### R01 — Consultar alertas

**Status:** [Parcial]  
**Referência:** `app/src/main/java/com/sentinela/app/ui/screens/TelaAlertas.kt`, `app/src/main/java/com/sentinela/app/ui/components/ListaAlertas.kt`, `app/src/main/java/com/sentinela/app/ui/components/AlertaCard.kt`, `app/src/main/java/com/sentinela/app/model/Alerta.kt`

Frase: "O sistema deve apresentar os alertas disponíveis para consulta, com seus dados e nível de severidade."

#### HU01 — Consultar alertas disponíveis

Como pessoa usuária da comunidade,  
eu quero consultar os alertas disponíveis e suas informações principais,  
para que eu possa entender os riscos informados para cada local.

**CA01 — Apresentar os alertas disponíveis**  
DADO QUE existam alertas disponíveis,  
QUANDO a pessoa usuária acessar a área de alertas,  
ENTÃO o sistema deve apresentar cada alerta disponível.

**CA02 — Apresentar informações de cada alerta**  
DADO QUE um alerta esteja disponível,  
QUANDO ele for apresentado,  
ENTÃO devem ser exibidos tipo, local, severidade e horário,  
E a descrição deve ser exibida quando não estiver vazia.

**CA03 — Ordenar novos alertas**  
DADO QUE um alerta seja registrado ou simulado,  
QUANDO a lista for atualizada,  
ENTÃO esse alerta deve aparecer antes dos alertas que já estavam na lista.

**CA04 — Informar lista vazia**  
DADO QUE não existam alertas disponíveis,  
QUANDO a lista for apresentada,  
ENTÃO o sistema deve informar que não há alertas no momento.

**CA05 — Exibir conteúdo inicial do protótipo**  
DADO QUE a aplicação seja iniciada no estado inicial do protótipo,  
QUANDO a área de alertas for apresentada,  
ENTÃO devem ser apresentados os três alertas de exemplo definidos no projeto.

### R02 — Registrar alerta comunitário

**Status:** [Implementado]  
**Referência:** `app/src/main/java/com/sentinela/app/ui/components/FormularioAlerta.kt`, `app/src/main/java/com/sentinela/app/ui/screens/TelaAlertas.kt`, `app/src/main/java/com/sentinela/app/model/Alerta.kt`

Frase: "O sistema deve permitir que a pessoa usuária registre um alerta informando tipo, local, descrição opcional e severidade."

#### HU02 — Registrar alerta com dados válidos

Como pessoa usuária da comunidade,  
eu quero registrar uma ocorrência com suas informações principais,  
para que ela fique visível junto aos demais alertas durante o uso da aplicação.

**CA01 — Registrar tipo e local preenchidos**  
DADO QUE o tipo e o local contenham algum caractere diferente de espaço em branco,  
QUANDO a pessoa usuária confirmar o cadastro,  
ENTÃO o alerta deve ser incluído na lista.

**CA02 — Remover espaços externos**  
DADO QUE o tipo, o local ou a descrição contenham espaços no início ou no fim,  
QUANDO o alerta for registrado,  
ENTÃO esses espaços externos não devem fazer parte dos valores registrados.

**CA03 — Permitir descrição vazia**  
DADO QUE o tipo e o local sejam válidos e a descrição esteja vazia,  
QUANDO a pessoa usuária confirmar o cadastro,  
ENTÃO o alerta deve ser registrado sem descrição.

**CA04 — Selecionar severidade**  
DADO QUE a pessoa usuária esteja preenchendo um alerta,  
QUANDO selecionar uma das severidades baixa, média ou alta,  
ENTÃO o alerta registrado deve apresentar a severidade selecionada.

**CA05 — Usar severidade inicial**  
DADO QUE a pessoa usuária não altere a severidade inicialmente selecionada,  
QUANDO confirmar o cadastro,  
ENTÃO o alerta deve ser registrado com severidade baixa.

**CA06 — Incluir e confirmar o cadastro**  
DADO QUE os dados obrigatórios sejam válidos,  
QUANDO o alerta for registrado,  
ENTÃO ele deve aparecer antes dos alertas existentes,  
E o sistema deve apresentar uma mensagem de confirmação,  
E a mensagem deve poder ser fechada pela pessoa usuária.

**CA07 — Cancelar o cadastro**  
DADO QUE o formulário esteja aberto,  
QUANDO a pessoa usuária cancelar o cadastro,  
ENTÃO o formulário deve ser fechado,  
E nenhum alerta deve ser incluído na lista.

#### HU03 — Corrigir dados obrigatórios ausentes

Como pessoa usuária da comunidade,  
eu quero saber quando faltam informações obrigatórias,  
para que eu possa corrigir o cadastro antes de registrar a ocorrência.

**CA01 — Rejeitar tipo ou local vazio**  
DADO QUE o tipo ou o local esteja vazio ou contenha apenas espaços em branco,  
QUANDO a pessoa usuária tentar registrar o alerta,  
ENTÃO o sistema não deve incluir o alerta na lista,  
E deve informar: “Preencha ao menos o tipo e o local da ocorrência.”

**CA02 — Limpar erro ao corrigir campos obrigatórios**  
DADO QUE a mensagem de validação esteja sendo apresentada,  
QUANDO a pessoa usuária alterar o tipo ou o local,  
ENTÃO a mensagem de validação deve ser removida.

### R03 — Simular inclusão de alerta oficial

**Status:** [Implementado] — somente simulação local; não representa recebimento real de uma fonte oficial.  
**Referência:** `app/src/main/java/com/sentinela/app/ui/screens/TelaAlertas.kt`

Frase: "O sistema deve permitir simular a inclusão de um alerta identificado como oficial no protótipo."

#### HU04 — Simular alerta oficial

Como pessoa que demonstra ou avalia o protótipo,  
eu quero simular a inclusão de um alerta oficial,  
para que eu possa observar como um alerta desse tipo aparece na lista.

**CA01 — Incluir alerta de exemplo**  
DADO QUE a tela principal esteja aberta,  
QUANDO a pessoa usuária acionar a simulação de alerta oficial,  
ENTÃO o sistema deve incluir na lista o alerta de exemplo definido no projeto,  
E o alerta deve indicar severidade alta e abrangência de toda a cidade.

**CA02 — Confirmar a simulação**  
DADO QUE a simulação tenha incluído o alerta,  
QUANDO a lista for atualizada,  
ENTÃO o alerta deve aparecer antes dos alertas existentes,  
E o sistema deve informar que um novo alerta foi recebido.

### R04 — Receber alertas de fonte oficial

**Status:** [Não implementado/planejado]  
**Referência:** `app/src/main/java/com/sentinela/app/model/Alerta.kt` (comentário sobre futura integração com API pública); `app/src/main/java/com/sentinela/app/ui/screens/TelaAlertas.kt` (dados atualmente simulados)

Frase: "O sistema deve obter alertas de uma fonte pública oficial e apresentá-los para consulta."

#### HU05 — Consultar alertas oficiais recebidos

Como pessoa usuária da comunidade,  
eu quero consultar alertas publicados por uma fonte oficial,  
para que eu possa conhecer avisos de defesa civil sem depender de uma simulação local.

**CA01 — Apresentar alerta oficial recebido**  
DADO QUE uma fonte oficial disponibilize um alerta com tipo, local, descrição, severidade e horário,  
QUANDO o sistema receber esse alerta,  
ENTÃO ele deve ficar disponível na lista com esses dados.

**CA02 — Tratar indisponibilidade da fonte**  
DADO QUE a fonte oficial esteja indisponível ou não forneça um alerta,  
QUANDO o sistema tentar obter alertas,  
ENTÃO o comportamento de atualização, aviso à pessoa usuária e tentativa posterior deve seguir uma política definida pela equipe.

**A VALIDAR COM A EQUIPE:** origem oficial, frequência de atualização, tratamento de duplicidade, política para falhas e necessidade de armazenamento local.

## 3. Requisitos Não Funcionais

### R05 — Compatibilidade com a plataforma Android

**Tipo:** RNF  
**Status:** [Implementado] para os limites declarados na configuração; a execução em dispositivos não foi aferida neste documento.  
**Referência:** `app/build.gradle.kts`, `app/src/main/AndroidManifest.xml`

Frase: "O aplicativo deve ser instalável e executável em dispositivos Android a partir da versão mínima declarada pelo projeto."

#### HU06 — Usar o aplicativo em Android compatível

Como pessoa usuária de um dispositivo Android compatível,  
eu quero instalar e abrir o Sentinela,  
para que eu possa acessar os alertas no meu celular.

**CA01 — Respeitar a versão mínima declarada**  
DADO QUE o dispositivo execute Android API 26 ou superior,  
QUANDO a pessoa usuária instalar e abrir o aplicativo,  
ENTÃO a versão mínima configurada pelo projeto deve permitir a instalação.

**CA02 — Disponibilizar o aplicativo como app mobile**  
DADO QUE a instalação tenha sido concluída,  
QUANDO a pessoa usuária iniciar o aplicativo,  
ENTÃO a Activity principal deve abrir a área de alertas.

**Meta configurada:** `minSdk 26`, `targetSdk 36`, `compileSdk 36` e compatibilidade de fonte Java 11.

### R06 — Manter consistência visual e suportar temas do sistema

**Tipo:** RNF  
**Status:** [Implementado]  
**Referência:** `app/src/main/java/com/sentinela/app/ui/theme/Theme.kt`, `app/src/main/java/com/sentinela/app/ui/theme/Color.kt`, `app/src/main/java/com/sentinela/app/ui/theme/Type.kt`, `app/build.gradle.kts`

Frase: "A interface deve manter uma apresentação consistente com Material 3 e acompanhar o tema claro ou escuro do sistema."

#### HU07 — Usar a interface no tema do dispositivo

Como pessoa usuária,  
eu quero que a interface acompanhe as configurações visuais do meu dispositivo,  
para que eu possa consultar os alertas com uma apresentação coerente com meu ambiente de uso.

**CA01 — Aplicar tema claro ou escuro**  
DADO QUE o dispositivo esteja configurado para tema claro ou escuro,  
QUANDO o aplicativo for apresentado,  
ENTÃO a interface deve usar o esquema correspondente.

**CA02 — Aplicar cores dinâmicas quando disponíveis**  
DADO QUE o dispositivo execute Android 12 ou superior e permita cores dinâmicas,  
QUANDO o aplicativo for apresentado,  
ENTÃO o tema deve poder usar o esquema de cores dinâmicas do sistema.

**CA03 — Usar componentes Material 3**  
DADO QUE a pessoa usuária utilize as funções disponíveis,  
QUANDO a interface apresentar navegação, campos, cartões ou ações,  
ENTÃO esses elementos devem manter o padrão visual consistente adotado pelo aplicativo.

**Situação atual:** dependência Material 3 e tema claro/escuro estão presentes; o tema dinâmico está habilitado a partir do Android 12. As cores de severidade são fixas e independem do tema.

### R07 — Permitir uso sem conectividade nos fluxos locais

**Tipo:** RNF  
**Status:** [Parcial] — cadastro e consulta atuais usam estado local, mas não há persistência entre recriações da Activity ou reinicializações.  
**Referência:** `app/src/main/java/com/sentinela/app/ui/screens/TelaAlertas.kt`, `app/src/main/java/com/sentinela/app/model/Alerta.kt`, `app/src/main/AndroidManifest.xml`

Frase: "Os fluxos locais implementados devem poder ser utilizados sem conexão com a internet durante a sessão de uso."

#### HU08 — Consultar e registrar alertas sem conexão

Como pessoa usuária sem acesso à internet,  
eu quero consultar os alertas locais e registrar uma ocorrência,  
para que eu possa usar as funções já disponíveis mesmo sem conectividade.

**CA01 — Usar funções locais offline**  
DADO QUE o dispositivo não tenha conexão com a internet,  
QUANDO a pessoa usuária consultar a lista ou registrar um alerta local,  
ENTÃO essas funções devem continuar disponíveis durante a sessão.

**CA02 — Não prometer persistência dos dados**  
DADO QUE a aplicação seja encerrada ou sua Activity seja recriada,  
QUANDO a aplicação voltar a ser aberta,  
ENTÃO este requisito não garante a recuperação dos alertas registrados anteriormente.

**Situação atual:** o Manifest não declara permissão de internet e os alertas são mantidos em memória. A integração oficial planejada dependerá de conectividade e exigirá definir o comportamento offline.

### R08 — Adaptar a interface ao espaço disponível

**Tipo:** RNF  
**Status:** [Parcial] — a interface usa layouts Compose e lista rolável, mas não há critérios ou validação para dimensões, orientação, escalonamento de fonte e acessibilidade visual.  
**Referência:** `app/src/main/java/com/sentinela/app/ui/screens/TelaAlertas.kt`, `app/src/main/java/com/sentinela/app/ui/components/ListaAlertas.kt`, `app/src/main/java/com/sentinela/app/ui/components/FormularioAlerta.kt`

Frase: "A interface deve permitir consultar a lista e completar o cadastro nos tamanhos de tela Android suportados pelo projeto, sem ocultar conteúdo ou impedir as ações principais."

#### HU09 — Usar os fluxos principais em diferentes telas

Como pessoa usuária de um celular Android,  
eu quero consultar alertas e completar o cadastro independentemente do tamanho da tela,  
para que as funções principais permaneçam acessíveis no meu dispositivo.

**CA01 — Consultar lista rolável**  
DADO QUE existam mais alertas do que cabem na área visível,  
QUANDO a pessoa usuária percorrer a lista,  
ENTÃO os alertas devem continuar acessíveis por rolagem.

**CA02 — Preservar acesso aos campos e ações do cadastro**  
DADO QUE o formulário esteja aberto em um dispositivo Android suportado,  
QUANDO a pessoa usuária preencher os campos e acessar as ações de registrar ou cancelar,  
ENTÃO os campos e ações necessários devem permanecer acessíveis.

**A VALIDAR COM A EQUIPE:** dimensões/orientações mínimas a suportar e comportamento esperado com fontes ampliadas.

### R09 — Facilitar manutenção sem regressões de comportamento

**Tipo:** RNF  
**Status:** [Parcial] — a tela centraliza estado e há componentes de apresentação desacoplados por parâmetros e callbacks; não foram encontrados testes que demonstrem proteção contra regressões.  
**Referência:** `app/src/main/java/com/sentinela/app/ui/screens/TelaAlertas.kt`, `app/src/main/java/com/sentinela/app/ui/components/FormularioAlerta.kt`, `app/src/main/java/com/sentinela/app/ui/components/ListaAlertas.kt`, `app/src/main/java/com/sentinela/app/ui/components/AlertaCard.kt`

Frase: "A evolução de uma funcionalidade deve preservar os comportamentos não relacionados já definidos para o aplicativo."

#### HU10 — Evoluir o produto preservando os fluxos existentes

Como pessoa da equipe de desenvolvimento,  
eu quero alterar uma funcionalidade sem modificar inadvertidamente os fluxos não relacionados,  
para que novas versões preservem o comportamento esperado pelas pessoas usuárias.

**CA01 — Preservar consulta ao alterar cadastro**  
DADO QUE uma alteração seja feita no fluxo de cadastro,  
QUANDO o comportamento de consulta for verificado,  
ENTÃO a lista deve continuar apresentando os alertas e as informações previstas em R01.

**CA02 — Preservar cadastro ao alterar apresentação da lista**  
DADO QUE uma alteração seja feita na apresentação da lista,  
QUANDO o fluxo de cadastro for verificado,  
ENTÃO validações, inclusão e confirmação previstas em R02 devem continuar funcionando.

**Situação atual:** a organização favorece separação entre tela e componentes, mas a existência de testes automatizados para esses comportamentos não foi observada.

### R10 — Disponibilizar textos compreensíveis e preparados para localização

**Tipo:** RNF  
**Status:** [Parcial]  
**Referência:** `app/src/main/res/values/strings.xml`, `app/src/main/java/com/sentinela/app/ui/components/FormularioAlerta.kt`, `app/src/main/java/com/sentinela/app/ui/components/ListaAlertas.kt`, `app/src/main/java/com/sentinela/app/ui/screens/TelaAlertas.kt`

Frase: "Os textos apresentados às pessoas usuárias devem estar centralizados de forma a permitir revisão e localização consistente."

#### HU11 — Compreender os textos da interface

Como pessoa usuária do Sentinela,  
eu quero compreender os rótulos, mensagens e instruções apresentados,  
para que eu consiga consultar e registrar alertas sem ambiguidade.

**CA01 — Apresentar textos no idioma definido para a versão**  
DADO QUE a pessoa usuária utilize uma versão disponibilizada em um idioma,  
QUANDO interagir com qualquer fluxo do aplicativo,  
ENTÃO os rótulos e mensagens desse fluxo devem estar disponíveis nesse idioma.

**CA02 — Manter consistência textual**  
DADO QUE um mesmo conceito apareça em mais de um fluxo,  
QUANDO seus textos forem apresentados,  
ENTÃO eles devem usar a mesma terminologia definida pelo projeto.

**Situação atual:** os textos estão majoritariamente hardcoded em português; apenas `app_name` está em `strings.xml`.  
**A VALIDAR COM A EQUIPE:** idiomas que serão suportados e glossário de termos de Defesa Civil.

### R11 — Definir política de backup e transferência de dados

**Tipo:** RNF  
**Status:** [Parcial] — referências e arquivos de regras existem, mas não expressam uma política específica de inclusão ou exclusão.  
**Referência:** `app/src/main/AndroidManifest.xml`, `app/src/main/res/xml/backup_rules.xml`, `app/src/main/res/xml/data_extraction_rules.xml`

Frase: "O comportamento de backup e transferência de dados do aplicativo deve estar explicitamente definido conforme a política de privacidade do projeto."

#### HU12 — Ter clareza sobre proteção dos dados

Como pessoa responsável pelo produto,  
eu quero definir quais dados podem ser copiados ou transferidos pelo sistema operacional,  
para que o comportamento de backup seja compatível com a política de privacidade do projeto.

**CA01 — Aplicar política aprovada**  
DADO QUE a equipe tenha aprovado uma política de backup e transferência,  
QUANDO o Android executar backup ou transferência de dados,  
ENTÃO somente os dados autorizados por essa política devem ser incluídos.

**A VALIDAR COM A EQUIPE:** se o backup deve ser permitido, quais dados podem ser transferidos e qual comportamento se aplica aos dados locais que venham a ser adicionados.

### R12 — Manter configuração de build identificável

**Tipo:** RNF  
**Status:** [Implementado] quanto à declaração de ferramentas e dependências; a reprodutibilidade do build deve ser confirmada pela execução em ambiente limpo.  
**Referência:** `build.gradle.kts`, `app/build.gradle.kts`, `gradle/libs.versions.toml`, `gradle/wrapper/gradle-wrapper.properties`, `settings.gradle.kts`

Frase: "A equipe deve conseguir identificar as versões de ferramentas e dependências necessárias para construir o aplicativo."

#### HU13 — Construir uma versão do aplicativo

Como pessoa da equipe de desenvolvimento,  
eu quero identificar e utilizar a configuração de build do projeto,  
para que eu possa construir e avaliar uma versão do aplicativo.

**CA01 — Identificar versões declaradas**  
DADO QUE uma pessoa desenvolvedora consulte a configuração do projeto,  
QUANDO verificar ferramentas e dependências principais,  
ENTÃO deve encontrar suas versões declaradas no wrapper Gradle e no catálogo de versões.

**CA02 — Identificar os módulos e repositórios do build**  
DADO QUE uma pessoa desenvolvedora consulte a configuração do projeto,  
QUANDO verificar a estrutura de build,  
ENTÃO deve conseguir identificar o módulo Android do aplicativo e os repositórios configurados.

**Situação atual:** o projeto usa Gradle 9.2.1, Android Gradle Plugin 9.0.1, Kotlin 2.0.21 e Compose BOM 2024.09.00, declarados nos arquivos de configuração.

## 4. Lacunas e inconsistências

- **Integração oficial:** há comentário sobre futura API pública, mas não há cliente de rede, permissão de internet, sincronização ou tratamento de falhas. A definição da fonte, atualização, duplicidade e indisponibilidade ainda depende da equipe.
- **Simulação versus recebimento:** a ação chamada de simulação cria um alerta fixo localmente. A mensagem de sucesso diz “recebido”, mas não ocorre recebimento externo.
- **Persistência e ciclo de vida:** os alertas usam estado em memória. A lista e o próximo identificador não usam estado persistido; assim, não há garantia de recuperar inclusões após recriação da Activity ou reinicialização.
- **Backup:** o Manifest permite backup e referencia regras XML, mas os arquivos contêm conteúdo de exemplo/comentários e não definem política específica. O estado atual em memória não representa dados duráveis a proteger.
- **Lista vazia:** existe mensagem para lista vazia no componente, mas a tela principal inicia com três exemplos e não oferece remoção de alertas.
- **Horário:** o horário de novos alertas é o literal “agora”; não existe data/hora real ou fuso explicitamente tratado.
- **Perfis e permissões:** não há autenticação nem diferenciação entre comunidade e agentes da Defesa Civil. O papel de agente oficial é apenas sugerido pela simulação.
- **Campos:** não há limite de caracteres definido nem regra de conteúdo para tipo, local e descrição além da obrigatoriedade de tipo/local.
- **Idioma:** os textos estão hardcoded em português, sem política de idiomas ou externalização ampla.
- **Testes:** dependências de teste estão declaradas no build, mas não foram localizados arquivos de teste unitário ou instrumentado.
- **Acessibilidade:** ícones de ação possuem descrições de conteúdo; não foram localizados critérios ou testes para navegação assistiva, contraste, fontes ampliadas ou outras necessidades.
- **Interface adaptável:** não há matriz de dispositivos, tamanhos de tela, orientação ou tamanho de fonte a validar.
- **Regras de alerta:** não há comportamento implementado para edição, exclusão, reconhecimento, filtros, ordenação configurável ou notificação do sistema. Não foram tratados como requisitos existentes.
- **Critérios de API e backup:** os comportamentos de falha da fonte oficial e a política de backup não podem ser fechados até a equipe decidir as políticas correspondentes.

## 5. Tabela de rastreabilidade

| Requisito | Tipo (RF/RNF) | História | Nº de critérios | Status | Arquivo de referência |
|---|---|---|---:|---|---|
| R01 | RF | HU01 | 5 | [Parcial] | `TelaAlertas.kt`, `ListaAlertas.kt`, `AlertaCard.kt`, `Alerta.kt` |
| R02 | RF | HU02 | 7 | [Implementado] | `FormularioAlerta.kt`, `TelaAlertas.kt`, `Alerta.kt` |
| R02 | RF | HU03 | 2 | [Implementado] | `FormularioAlerta.kt` |
| R03 | RF | HU04 | 2 | [Implementado] (simulação) | `TelaAlertas.kt` |
| R04 | RF | HU05 | 2 | [Não implementado/planejado] | `Alerta.kt`, `TelaAlertas.kt` |
| R05 | RNF | HU06 | 2 | [Implementado] (configuração) | `app/build.gradle.kts`, `AndroidManifest.xml` |
| R06 | RNF | HU07 | 3 | [Implementado] | `Theme.kt`, `Color.kt`, `Type.kt`, `app/build.gradle.kts` |
| R07 | RNF | HU08 | 2 | [Parcial] | `TelaAlertas.kt`, `Alerta.kt`, `AndroidManifest.xml` |
| R08 | RNF | HU09 | 2 | [Parcial] | `TelaAlertas.kt`, `ListaAlertas.kt`, `FormularioAlerta.kt` |
| R09 | RNF | HU10 | 2 | [Parcial] | `TelaAlertas.kt`, `FormularioAlerta.kt`, `ListaAlertas.kt`, `AlertaCard.kt` |
| R10 | RNF | HU11 | 2 | [Parcial] | `strings.xml`, `FormularioAlerta.kt`, `ListaAlertas.kt`, `TelaAlertas.kt` |
| R11 | RNF | HU12 | 1 | [Parcial] | `AndroidManifest.xml`, `backup_rules.xml`, `data_extraction_rules.xml` |
| R12 | RNF | HU13 | 2 | [Implementado] (declarações) | `build.gradle.kts`, `app/build.gradle.kts`, `libs.versions.toml`, `gradle-wrapper.properties`, `settings.gradle.kts` |