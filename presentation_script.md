```markdown
Roteiro para vídeo (~5 minutos)

1) Introdução (30s)
- Apresentação rápida: seu nome, nome do projeto (ChefAI), objetivo do sistema.

2) Demonstração (2m)
- Mostrar terminal: executar jar, inserir nome e ingredientes, escolher suggestor.
- Mostrar que o sistema retorna 3 receitas com tempo <= 30 minutos, ingredientes (marcados os que você tem) e passos.

3) Código & POO (2m)
- Mostrar diagrama de classes (CLASS_DIAGRAM.puml).
- Explicar como o código aplica os pilares:
  - Encapsulamento: classes modelagem (Ingredient, Recipe, User) com campos privados e getters/setters.
  - Abstração/Herança: SuggestorBase é abstrato; SuggestorRapido e SuggestorSaudavel são implementações.
  - Polimorfismo: SuggestorFactory retorna SuggestorBase e o fluxo usa a mesma interface.
  - Composição: Recipe contém RecipeIngredient.
- Explicar LLMClient e a decisão de solicitar saída JSON para parsing confiável.

4) Dificuldades e Aprendizados (1m)
- Exemplos: lidar com respostas inconsistentes do LLM, necessidade de prompt bem estruturado, tratamento seguro de chave (config.properties), modelagem de domínio.

Checklist para entrega (mostrar na tela rapidamente)
- Código funcionando via jar
- README com instruções
- Diagrama de classes
- Vídeo ~5min cobrindo demo / código / aprendizados