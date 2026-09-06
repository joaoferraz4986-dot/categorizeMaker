# 🛠️ categorizeMaker

> Sistema inteligente de inventário, rastreabilidade e gestão operacional desenvolvido para o **Lab Maker da ETEC**.

O **categorizeMaker** centraliza o controle de ferramentas, insumos e equipamentos do laboratório, além de permitir o acompanhamento detalhado dos projetos desenvolvidos no espaço Maker.

---

## 🚀 Funcionalidades Principais

- 📦 **Gestão de Inventário e Peças**: Cadastro, edição, remoção e listagem de materiais, ferramentas e componentes eletrônicos.
- 🔄 **Controle de Status**: Monitoramento dinâmico do estado dos itens (*Disponível/Livre*, *Em Uso*, *Quebrado*, *Emprestado*).
- 📂 **Acompanhamento de Projetos**: Associação de itens e insumos aos projetos desenvolvidos no laboratório.
- 🔍 **Busca Dinâmica via API**: Pesquisa rápida e filtragem por categorias e nomes.
- 📄 **Exportação de Relatórios em PDF**: Geração de relatórios PDF estilizados do inventário utilizando a biblioteca iText.
- 🛡️ **Autenticação e Segurança**: Sistema de Login e Registro com autenticação JWT, filtro de segurança, controle de sessão/timeout e limitação de taxa (*Rate Limiting*).
- 🌙 **Interface Moderna**: Layout responsivo com suporte a modo escuro (*Dark Mode*), navegação por abas e modais interativos.
- 🏗️ **Arquitetura Sólida**: Organização em camadas (*Controllers*, *Services*, *Repositories*, *DTOs* e *Mappers* com MapStruct).

---

## 🛠️ Stack Tecnológica

### Back-end
- **Linguagem**: Java 17+
- **Framework**: Spring Boot 3
- **Segurança**: Spring Security & JWT (Auth0)
- **Mapeamento & DTOs**: MapStruct
- **Persistência**: Spring Data JPA / Hibernate
- **Banco de Dados**: H2 (Desenvolvimento) / PostgreSQL ou MySQL (Produção)
- **Relatórios**: iText PDF

### Front-end
- **Linguagens**: HTML5, CSS3, JavaScript ES6+ (Modular)
- **Template Engine**: Thymeleaf / Static Resources
- **Utilitários**: Fetch API, manipulação de Imagens Base64, visões em Grid e Tabela

---

## 📋 Pré-requisitos

Antes de iniciar, certifique-se de ter instalado em sua máquina:
- [JDK 17](https://www.oracle.com/java/technologies/downloads/#java17) ou superior
- [Apache Maven](https://maven.apache.org/) (opcional, pois o projeto inclui o Maven Wrapper)
- Git

---

## 🔧 Como Executar o Projeto Localmente

### 1. Clonar o repositório
```bash
git clone [https://github.com/joaoferraz4986-dot/categorizeMaker.git](https://github.com/joaoferraz4986-dot/categorizeMaker.git)
cd categorizeMaker
```

### 2. Compilar e executar a aplicação

* **No Linux/macOS:**
  ```bash
  ./mvnw spring-boot:run
  ```

* **No Windows (CMD/PowerShell):**
  ```powershell
  .\mvnw.cmd spring-boot:run
  ```

### 3. Acessar no navegador
- **Aplicação Principal:** `http://localhost:8080/`
- **Tela de Registro/Autenticação:** `http://localhost:8080/authentication/registro`
- **Dashboard do Laboratório:** `http://localhost:8080/lab`

---

## 📐 Estrutura do Projeto

```text
src/main/java/com/makernav/categorize/
├── controller/       # Endpoints REST e controllers da interface web
├── dto/              # Objetos de Transferência de Dados (Request/Response)
│   └── mapper/       # Mapeadores MapStruct (ItemMapper, UsuarioMapper)
├── model/            # Entidades JPA (Item, Projeto, Usuario)
├── service/          # Regras de negócio (ItemService, ProjetoService, JWTService, ItemPdfService)
└── infra/            # Configurações de segurança, filtros JWT, Rate Limiter e repositórios
```

---

## 👥 Equipe e Orientação

- **Product Owner / Orientador:** wip
- **Desenvolvedores:** wip