# Proposta do Produto — GestFin

## Visão do Produto

Para pessoas que possuem dificuldades em organizar suas finanças pessoais, em especial usuários endividados ou com pouca familiaridade com ferramentas digitais,
Que necessitam de um controle simples e previsível sobre suas contas diárias, compromissos futuros e parcelamentos,
O GestFin é um aplicativo de gestão financeira pessoal
Que automatiza o registro de lançamentos recorrentes e parcelados, emite alertas de vencimento e consolida relatórios mensais e anuais de forma clara, simples e direta,
Diferente de planilhas manuais ou aplicativos que utilizam excesso de telas e apresentam baixo nível de automação,
Nosso produto combina simplicidade, automação prática de parcelas/recorrência e previsibilidade, oferecendo alertas diretos de vencimento que permitem manter as finanças organizadas com o mínimo de esforço.

## Definição do MVP

### Hipótese de Valor

Acreditamos que pessoas com dificuldades na organização financeira ou pouca familiaridade com ferramentas digitais vão registrar e acompanhar suas movimentações diárias, parcelas e contas agendadas no aplicativo de forma contínua porque a automação de lançamentos futuros (recorrências/parcelamentos) e os alertas de vencimento reduzem significativamente o esforço manual e a complexidade de manter o controle financeiro atualizado. Diante disso, o usuário pode compreender melhor a sua própria situação, o que permite tomar decisões mais conscientes (Junior & Andrade, 2018).

### Requisitos Funcionais Identificados

| ID | Funcionalidade | Descrição |
|----|-----------------|-----------|
| RF01 | Cadastro de receitas | Registro de receitas com valor, data, categoria, forma de recebimento e observações. |
| RF02 | Cadastro de despesas | Registro de despesas com valor, data, categoria, forma de pagamento e observações. |
| RF03 | Relatório mensal | Consolidado do mês selecionado: total de receitas, total de despesas pagas, total de despesas em aberto e saldo do período. |
| RF04 | Relatório anual | Visão consolidada dos 12 meses, com total de entradas, total de saídas e saldo acumulado no ano. |
| RF05 | Agendamento de lançamentos futuros | Receita ou despesa com data futura, que aparece automaticamente no mês correspondente quando chega a data (ex: "conta de luz de dezembro" cadastrada em outubro). |
| RF06 | Notificação de vencimento | Alerta ao usuário quando um lançamento agendado está próximo do vencimento ou venceu. |
| RF07 | Lançamentos recorrentes com confirmação | O sistema sugere o lançamento do mês seguinte com base na recorrência configurada; o lançamento só é efetivado após confirmação do usuário. |
| RF08 | Lançamentos parcelados | Ao cadastrar uma receita/despesa parcelada, o sistema gera automaticamente todas as parcelas nos meses correspondentes, permitindo visualizar a previsão total desde o início. |

### Escopo do MVP

| No MVP (Escopo Declarado) | Fora do MVP (Futuras Iterações) |
|---|---|
| Cadastro de Receitas e Despesas: registro simples com valor, data, categoria, forma de pagamento/recebimento e observações (RF01, RF02) | Importação automática de extratos bancários via Open Finance ou leitura de PDF/OFX |
| Automação de Parcelamentos: geração automática das parcelas mensais ao cadastrar uma compra parcelada (RF08) | Simulação ou renegociação de juros para parcelamentos e dívidas |
| Agendamento e Recorrência: lançamentos com data futura que aparecem no mês correspondente (RF05) e sugestão de lançamentos recorrentes com confirmação manual (RF07) | Pagamento automático de contas via integração bancária ou leitura de código de barras (Pix/Boleto) |
| Notificação de Vencimento: alertas no aplicativo sobre contas e lançamentos próximos do vencimento ou vencidos (RF06) | Lembretes por e-mail, WhatsApp ou SMS |
| Relatórios e Consolidação: visão mensal condensada (receitas, despesas pagas/abertas e saldo) (RF03) e relatório anual simples (RF04) | Gráficos interativos avançados, estatísticas personalizadas e projeção patrimonial de longo prazo |
| Autenticação Básica: cadastro (nome, e-mail, senha) e login com armazenamento seguro | Autenticação via redes sociais (Google/Apple ID) ou biometria |
| Gestão de Categorias: categorias padrão pré-definidas para receitas e despesas | Módulo de educação financeira, calculadoras de investimento e metas/reserva de emergência |

## Backlog Inicial

Backlog disponível no GitHub Projects do repositório: [https://github.com/users/Tiegow/projects/1]

## Plataforma-alvo

A plataforma-alvo escolhida para o GestFin é o **Android**, desenvolvido em Kotlin Multiplatform (KMP) — o que também permite gerar um app iOS nativo a partir da mesma base de código como evolução futura.

O Android foi priorizado como alvo principal por dois motivos ligados diretamente ao produto e ao público definido:

1. **Perfil do público-alvo.** O GestFin é voltado a pessoas com dificuldades de organização financeira e baixa familiaridade com ferramentas digitais — um público com forte presença no mercado Android, que lidera com 296,9 milhões de smartphones ativados e 86,2% de participação de mercado, contra 12,9% do iOS (Leite et al., 2017). Como o app depende de uso cotidiano (registrar lançamentos, receber alertas), maximizar o alcance nesse segmento é prioridade.
2. **Viabilidade de desenvolvimento e distribuição no prazo da disciplina.** O iOS foi considerado e descartado como alvo principal porque exige macOS com Xcode para build, além do Apple Developer Program (custo anual) para distribuição via TestFlight. O Android pode ser desenvolvido com Android Studio e emulador, e distribuído gratuitamente via APK/release do GitHub — compatível com o cronograma de quatro sprints.

Referências: LECHETA, R. R. *Google Android*. 4. ed. São Paulo: Novatec, 2015. LEITE, A. C.; REIS, H. M. Comparativo entre sistemas operacionais móveis – Android x iOS. *Simpósio de Tecnologia da FATEC*, Taquaritinga, 2017.

## Backend

Para o backend do GestFin foi escolhido o **Supabase** (Opção A). A decisão está relacionada à necessidade de persistir e organizar dados financeiros estruturados e relacionados entre si (usuários, receitas, despesas, categorias, lançamentos futuros, recorrências e parcelas), além de oferecer suporte à autenticação e à comunicação entre app e backend.

O Supabase oferece um banco PostgreSQL relacional, adequado às consolidações exigidas pelos relatórios mensais e anuais (RF03, RF04), além de API REST gerada automaticamente, autenticação, realtime, storage e edge functions.

Foi considerado também o **Firebase** (Opção B), por disponibilizar autenticação, Firestore, storage, notificações e crash reporting integrados. Foi descartado porque o modelo de consultas do Firestore é mais restrito para dados relacionais, enquanto o GestFin precisa de consolidações (somas, agrupamentos por período/categoria) mais naturais em um banco relacional como o PostgreSQL do Supabase.

Referência: SANCHES, 2025.

## Equipe

| Nome | Matrícula | Papel |
|------|-----------|-------|
| Joab Urbano de Araujo | 20260072887 | FullStack |
| Marcos Martins Nóbrega | 20240078177 | `[PREENCHER]` |
| Tiego Rafael Belo da Rocha | 20230034535 | FullStack |
| Vladimir Vieira do Nascimento | 20240078210 | `[PREENCHER]` |

## Coorte de Apresentação e Integração

- Coorte: 
- Integração com outra disciplina: Esse projeto não será utilizzado em outra diciplina
