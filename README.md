```markdown
# ChefAI - Sugestor de Receitas Inteligente

Resumo
-----
ChefAI sugere receitas rápidas (até 30 minutos) com base nos ingredientes informados pelo usuário. Exemplo de uso para trabalhos acadêmicos: demonstra aplicação prática dos pilares da Programação Orientada a Objetos, integração com LLMs e tratamento de configuração segura (chave não hard-coded).

Requisitos
---------
- Java 17+
- Maven
- Chave de API de um serviço LLM (ex.: OpenAI) — a URL e a chave são armazenadas em config.properties (não colocar chave no código)

Setup
-----
1. Copie o arquivo de exemplo:
   cp src/main/resources/config.properties.example config.properties
2. Edite `config.properties` e coloque:
   - api.key = SUA_CHAVE_AQUI
   - api.url = https://api.openai.com/v1/chat/completions (ou outro endpoint compatível)
   - api.provider = openai
3. Build:
   mvn package
4. Executar:
   java -jar target/cheffai-1.0-SNAPSHOT.jar

Arquitetura e POO
-----------------
- Encapsulamento: classes com atributos privados e getters/setters.
- Abstração/Herança: `SuggestorBase` é abstrato; `SuggestorRapido` e `SuggestorSaudavel` estendem.
- Polimorfismo: código usa referências a `SuggestorBase` e escolhe implementação via `SuggestorFactory`.
- Composição: `Recipe` contém `List<Ingredient>`.

Diagrama de classes
-------------------
Arquivo: CLASS_DIAGRAM.puml (PlantUML)

API / Prompt
------------
O `LLMClient` constrói um prompt (via `PromptBuilder`) pedindo 3 receitas em JSON com esquema conhecido:
[
  {
    "name": "...",
    "ingredients": [{"name":"", "amount":"", "hasIngredient": true|false}],
    "steps": ["..."],
    "time_minutes": 20
  },
  ...
]

Testes e desenvolvimento offline
-------------------------------
Se você não tiver chave, é possível adaptar `LLMClient` para retornar respostas mock (veja comentários no código).

Apresentação (vídeo)
--------------------
Roteiro sugerido incluso no arquivo `presentation_script.md`.

Observações de segurança
------------------------
Não comite `config.properties` com chave. Use variáveis de ambiente/segredos no CI quando necessário.

```