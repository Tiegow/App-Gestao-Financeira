# Entrega da Sprint 1 — GestFin

Este documento registra o estado observado no código e as evidências ainda necessárias para a entrega. Não substitui a rubrica da disciplina.

## Incremento

- Telas de login, registro, dashboard mensal e cadastro de receitas/despesas.
- Navegação Compose com rotas tipadas e eventos recebidos pelas telas por callbacks.
- Componentes reutilizáveis de campos, resumo financeiro e linha de transação.
- Temas Material 3 claro e escuro, com Surface na raiz.
- Validação do cadastro de transações e mensagens de erro na autenticação.
- Integração de login e registro com Supabase. Os lançamentos do dashboard continuam em memória.

## Evidências da entrega

| Item | Registro |
|---|---|
| Vídeo da Sprint 1 | [Apresentação no YouTube](https://www.youtube.com/watch?v=aF9qTKWK3j4), link fornecido pelo integrante |
| Demonstração de duas larguras | Conteúdo do vídeo ainda não conferido |
| Teste com TalkBack | Não realizado, conforme informado pelo integrante |
| CI na branch principal | Conferir no GitHub após integrar os PRs |
| PRs, revisões e cartões | Conferir no quadro e no histórico do GitHub |

## Limitações identificadas

- Não há deep link declarado nem rota com argumento; portanto não há comando funcional de deep link a documentar.
- Não foi identificada adaptação por `currentWindowAdaptiveInfo().windowSizeClass`.
- Faltam previews específicos de todas as telas, incluindo estado vazio.
- Não foram identificadas marcações de títulos com `heading()`.
- O botão de adicionar lançamento usa o texto “+”, sem um nome de ação acessível explícito.
- O grupo não realizou teste com TalkBack. As descrições existentes nos ícones não comprovam um teste com leitor de tela.

Essas lacunas pertencem à rubrica da Sprint 1. Registrar aqui não significa que foram atendidas ou que seu adiamento foi autorizado.

## Continuidade do projeto

Conectar leitura e escrita de lançamentos ao Supabase, associar os registros ao usuário autenticado e evoluir a organização do estado com ViewModel. Persistência, rede e testes de interface são conteúdo previsto para a Sprint 2 nos requisitos fornecidos.

## Fechamento

- O link do vídeo está no README; verificar que ele abre para outra pessoa.
- Conferir o roteiro: telas, Voltar, deep link, duas larguras, leitor de tela, CI e próximos passos; todos os integrantes devem falar.
- Registrar somente testes e ajustes de acessibilidade efetivamente realizados.
- Vincular o PR ao cartão correspondente e solicitar revisão de outro integrante.
- Integrar na branch principal e conferir o workflow completo antes do prazo.
