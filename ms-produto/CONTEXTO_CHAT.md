# Contexto do Projeto — api_udemy (Spring Boot)

> Cole este arquivo no início de uma conversa de chat para que o assistente entenda o projeto sem precisar ver o código.

---

## Visão Geral

API REST de estudos desenvolvida em Java 21 com Spring Boot 3.4.4. O domínio é um "mercadinho" — CRUD de produtos com suporte a consultas dinâmicas via JPA Specifications. O projeto acompanha um curso de Spring na Udemy e evolui junto com o aprendizado.

---

## Stack

| Item | Versão / Detalhe |
|---|---|
| Java | 21 |
| Spring Boot | 3.4.4 |
| Banco de dados | PostgreSQL (`jdbc:postgresql://localhost:5432/mercadinho`) |
| ORM | Spring Data JPA + Hibernate (`ddl-auto=update`) |
| Mapeamento objeto | ModelMapper 3.2.6 |
| Boilerplate | Lombok |
| Build | Maven |
| Porta | 8083 |

---

## Arquitetura em Camadas

```
view/controller    →   services   →   repository   →   banco
     ↕                    ↕
ProdutoRequest     ProdutoDTO         Produto (Entity)
ProdutoResponse    RequestDTO
```

A comunicação entre camadas usa **DTOs** (objetos de transferência) mapeados com **ModelMapper**, mantendo a entidade JPA isolada do lado externo.

---

## Estrutura de Pacotes

```
br.com.thiagolima.api_udemy
├── model
│   ├── Produto.java               — entidade JPA
│   ├── SearchSpecification.java   — filtro dinâmico (coluna + valor + operação)
│   ├── SpecificationInput.java    — wrapper de um único filtro (usado em endpoints individuais)
│   └── exception
│       └── ResourceNotFoundException.java
├── model/error
│   └── ErrorMessage.java          — payload de erro padronizado
├── handler
│   └── RestExceptionHandler.java  — @ControllerAdvice, trata ResourceNotFoundException → 404
├── repository
│   └── ProdutoRepository.java     — JpaRepository<Produto,Integer> + JpaSpecificationExecutor
├── services
│   └── ProdutoService.java        — lógica de negócio, constrói Specifications
├── shared
│   ├── ProdutoDTO.java            — DTO de trânsito entre controller e service
│   └── RequestDTO.java            — DTO para consultas com lista de filtros
└── view/controller
    ├── ProdutoController.java
    └── model
        ├── ProdutoRequest.java    — payload de entrada (sem id)
        └── ProdutoResponse.java   — payload de saída (sem observacao)
```

---

## Entidade Principal: `Produto`

```java
@Entity
public class Produto {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String nome;
    private Integer quantidade;
    private Double valor;
    private String observacao;
    private LocalDate dataValidade;
}
```

---

## Endpoints Ativos — `/api/produtos`

| Método | Path | Descrição | Body / Param |
|---|---|---|---|
| GET | `/api/produtos` | Lista todos os produtos | — |
| GET | `/api/produtos/{id}` | Busca produto por id | `@PathVariable id` |
| POST | `/api/produtos` | Cadastra produto | `ProdutoRequest` |
| PUT | `/api/produtos/{id}` | Atualiza produto | `@PathVariable id` + `ProdutoRequest` |
| DELETE | `/api/produtos/{id}` | Remove produto | `@PathVariable id` |
| GET | `/api/produtos/byMaiorQue` | Conta/deleta produtos onde coluna >= valor | `SpecificationInput` |
| GET | `/api/produtos/byList` | Busca com lista de filtros dinâmicos (AND/OR) | `RequestDTO` |

### Payload `ProdutoRequest`
```json
{
  "nome": "string",
  "quantidade": 0,
  "valor": 0.0,
  "observacao": "string",
  "dataValidade": "2025-12-31"
}
```

### Payload `RequestDTO` (endpoint `/byList`)
```json
{
  "specificationContextList": [
    { "columnName": "valor", "value": "10.0", "operation": "GREATER_THAN" },
    { "columnName": "nome", "value": "%pao%", "operation": "LIKE" }
  ],
  "overallOperation": "AND"
}
```
Operações suportadas: `EQUAL`, `GREATER_THAN`, `GREATER_THAN_EQUAL`, `LESS_THAN`, `LESS_THAN_EQUAL`, `LIKE`, `IN`.

---

## Tratamento de Erros

- `ResourceNotFoundException` (RuntimeException) → interceptada pelo `RestExceptionHandler` → HTTP 404
- `ErrorMessage` retornado: `{ "title": "Not Found", "status": 404, "detail": "mensagem" }`
- O endpoint `GET /api/produtos/{id}` ainda tem um `try/catch` genérico legado que retorna `204 NO_CONTENT` (isso é inconsistente com o handler — ponto de melhoria conhecido)

---

## Código Comentado (Estudos Anteriores)

No `ProdutoService` e `ProdutoController` há blocos comentados que foram exercícios de Specifications progressivos:

1. **Equal fixo** — specification hardcoded por nome exato
2. **Equal dinâmico** — recebe `SpecificationInput` com coluna + valor
3. **Between datas** — intervalo de datas com paginação e ordenação
4. **Like** — busca parcial por string

Esses blocos estão preservados como histórico de aprendizado e podem ser reativados.

---

## Pontos de Atenção / Possíveis Próximos Passos

- `ModelMapper` é instanciado com `new` dentro dos métodos do controller — candidato a virar um `@Bean`
- `GET /api/produtos/{id}` mistura `try/catch` com o `@ControllerAdvice` — pode ser simplificado
- `ProdutoRepository_old.java` existe como arquivo legado (sem uso ativo)
- Ainda não há validações de bean (`@NotNull`, `@Valid`) nos requests
- Ainda não há testes unitários/integração implementados
- `@XSlf4j` importado no service mas não utilizado (Lombok SLF4J annotation)
- `GenerationType.AUTO` no id pode gerar sequências inesperadas no PostgreSQL — `IDENTITY` é mais previsível
