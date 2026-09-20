# Java Design Practice

Laboratório pessoal de Java com foco em boas práticas de design orientado a objetos.  
Cada projeto parte de um código funcional mas mal estruturado e o evolui aplicando um conjunto específico de conceitos — com comentários explicando cada decisão.

---

## Projetos

| Projeto | Conceitos | Pasta |
|---------|-----------|-------|
| [SOLID Principles](#solid-principles) | SRP, OCP, LSP, ISP, DIP, Composição, Lei de Demeter | [`solid-principles/`](solid-principles/) |
| [OO Fundamentals](#oo-fundamentals) | Encapsulamento, Construtores, Interface, Composição, Herança, Polimorfismo | [`oo-fundamentals/`](oo-fundamentals/) |

---

## solid-principles

Refatoração de um sistema de pedidos de loja com múltiplas violações de design.  
O ponto de partida é um código que compila e funciona — mas acumula responsabilidades em um único método, usa `if/else` em cadeia para decidir comportamento, e força classes a implementar métodos que não suportam.

Cada princípio foi corrigido de forma isolada e documentada:

- **SRP** — `finalizarPedido` foi decomposto; persistência extraída para `PedidoRepositoryImpl`
- **OCP** — `IDesconto` substituiu o `if/else` de tipo de cliente; novos descontos não tocam no serviço
- **LSP** — interface `IEntrega` com `disponivelPara()` eliminou exceção inesperada em subtipo
- **ISP** — `IPagamento` dividida em três interfaces; cada classe implementa só o que cumpre
- **DIP** — `PedidoService` recebe dependências por injeção; depende apenas de interfaces
- **Composição** — `extends` removido onde não havia relação "é um tipo de"
- **Lei de Demeter** — navegação de três níveis substituída por método no objeto dono dos dados

→ [`solid-principles/README.md`](solid-principles/README.md)

---

## oo-fundamentals

Evolução progressiva de um sistema de loja de consoles em cinco etapas, cada uma introduzindo um conceito de OO sobre o resultado da anterior.

1. **Encapsulamento** — atributos públicos tornados privados; acesso controlado por getters
2. **Construtores** — objeto não pode mais ser criado em estado incompleto
3. **Interface e Composição** — `IConsole` define o contrato; `DadosConsole` evita duplicação sem herança
4. **Herança** — `PlaystationPortatil extends Playstation` especializa sem quebrar contrato (contraste com violação de LSP)
5. **Polimorfismo e OCP** — `Loja` opera sobre `IConsole` sem `if/else`; `Xbox` adicionado sem alterar uma linha da classe

→ [`oo-fundamentals/README.md`](oo-fundamentals/README.md)

---

## Como executar

Pré-requisito: JDK 17 ou superior. Cada projeto tem seus próprios comandos no README interno.

---

## Estrutura

```
java-design-practice/
├── README.md
├── solid-principles/
│   ├── README.md
│   └── src/projetoprincipiosdesign/   (22 arquivos .java)
└── oo-fundamentals/
    ├── README.md
    └── src/
        └── Atividade_POO_Solucao.java
```
