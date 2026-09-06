# 🛠️ categorizeMaker

> Sistema web full-stack para gestão operacional, rastreabilidade de insumos e controle de projetos práticos do **Laboratório Maker**.

O **categorizeMaker** centraliza o controle de peças, ferramentas e componentes do espaço maker, substituindo controles manuais e planilhas por uma plataforma ágil com autenticação segura, controle de estados de materiais e geração automatizada de relatórios em PDF.

---

## 🚀 Tecnologias

[![Java](https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://www.java.com/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.3.5-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)
[![MySQL](https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white)](https://www.mysql.com/)
[![Flyway](https://img.shields.io/badge/Flyway-CC0200?style=for-the-badge&logo=flyway&logoColor=white)](https://flywaydb.org/)
[![JavaScript](https://img.shields.io/badge/JavaScript-ES_Modules-F7DF1E?style=for-the-badge&logo=javascript&logoColor=black)](https://developer.mozilla.org/docs/Web/JavaScript)
[![OpenPDF](https://img.shields.io/badge/PDF_Reports-OpenPDF-blue?style=for-the-badge)](https://github.com/LibrePDF/OpenPDF)

* **Back-end:** Java 21, Spring Boot 3 (Web, Security, Data JPA, Validation), Auth0 Java-JWT, MapStruct, Lombok, OpenPDF, Springdoc OpenAPI.
* **Banco de Dados:** MySQL com versionamento automatizado via Flyway Migrations.
* **Front-end:** Interface estática servida pelo Spring Boot, JavaScript Modular (ES Modules) com Service Layer centralizada e Fetch API nativa.

---

## ⚙️ Pré-requisitos

* [JDK 21](https://www.oracle.com/java/technologies/downloads/#java21) configurado no `JAVA_HOME`.
* [Git](https://git-scm.com/) instalado.
* [XAMPP](https://www.apachefriends.org/) (ou instância local do MySQL na porta `3306`).
* Maven (opcional — o projeto inclui o executável `./mvnw`).

---

## 🔧 Passo a Passo para Execução Local

### 1. Clonar o projeto
```bash
git clone [https://github.com/joaoferraz4986-dot/categorizeMaker.git](https://github.com/joaoferraz4986-dot/categorizeMaker.git)
cd categorizeMaker
```

### 2. Inicializar o Banco de Dados
1. Abra o **XAMPP Control Panel** e inicialize o serviço do **MySQL**.
2. Acesse o **phpMyAdmin** (`http://localhost/phpmyadmin`) ou o terminal do MySQL e crie o schema:
```sql
CREATE DATABASE categorize_db;
```
> O Flyway executará as migrations automaticamente no primeiro boot da aplicação, criando as tabelas (`item`, `usuario`, `projeto` e `projeto_item`).

### 3. Configurar Credenciais
As configurações estão em `src/main/resources/application.yaml`. Por padrão, a aplicação conecta com o usuário `root` e senha em branco. Caso seu ambiente use credenciais diferentes, defina as variáveis de ambiente antes de rodar:
```bash
# Opcional (apenas se seu MySQL exigir senha):
export DB_USERNAME=seu_usuario
export DB_PASSWORD=sua_senha
```

### 4. Compilar e Iniciar a Aplicação

* **Windows (PowerShell):**
```powershell
.\mvnw.cmd clean package
.\mvnw.cmd spring-boot:run
```

* **Windows (CMD):**
```cmd
mvnw.cmd clean package
mvnw.cmd spring-boot:run
```

* **Linux / macOS:**
```bash
chmod +x mvnw
./mvnw clean package
./mvnw spring-boot:run
```

### 5. Acesso no Navegador
A aplicação subirá na porta padrão `8080`:
* **Página Inicial:** `http://localhost:8080/`
* **Login:** `http://localhost:8080/login.html`
* **Cadastro:** `http://localhost:8080/cadastro.html`
* **Inventário do Laboratório:** `http://localhost:8080/lab.html`

> **Como testar o fluxo básico:**
> 1. Acesse a tela de **Cadastro** e registre um novo usuário (por padrão, recebe a permissão `PROFESSOR`).
> 2. Faça **Login** para emitir o token JWT (armazenado automaticamente no `localStorage`).
> 3. Acesse o **Inventário (`/lab.html`)** para cadastrar, editar, buscar e remover insumos.

---

## 🏛️ Arquitetura do Sistema

```text
src/
├── main/
│   ├── java/com/makernav/categorize/
│   │   ├── controller/      # Endpoints REST e redirecionamento de páginas
│   │   ├── dto/             # Request/Response records e mappers MapStruct
│   │   ├── infra/           # Filtro JWT, Exception Handlers e Repositories
│   │   ├── model/           # Entidades JPA (Item, Usuario, Projeto) e Enums
│   │   └── service/         # Regras de negócio, JWT e geração de PDFs
│   └── resources/
│       ├── application.yaml
│       ├── db/migration/    # Scripts SQL versionados do Flyway (V1 a V4)
│       └── static/          # Interface Web (HTML5, CSS3, JS Modular)
│           ├── js/
│           │   ├── services/  # api.js, itemService.js, authService.js
│           │   └── ...        # Scripts controladores de interface
└── test/                    # Testes de integração do contexto Spring
```

---

## 📡 Visão Geral dos Endpoints REST

| Recurso | Método | Rota | Descrição |
| :--- | :--- | :--- | :--- |
| **Auth** | `POST` | `/authentication/registro/` | Cadastro com senha criptografada em BCrypt |
| **Auth** | `POST` | `/authentication/login/` | Autenticação e emissão de token JWT |
| **Itens** | `GET` | `/api/items` | Lista todos os itens do laboratório |
| **Itens** | `GET` | `/api/items/{id}` | Busca detalhes de um item específico |
| **Itens** | `POST` | `/api/items` | Cadastra novo insumo com imagem Base64 |
| **Itens** | `PUT` | `/api/items/{id}` | Atualiza dados cadastrais de uma peça |
| **Itens** | `DELETE` | `/api/items/{id}` | Remove um item do inventário |
| **Itens** | `GET` | `/api/items/search?nome=` | Busca dinâmica de itens por prefixo |
| **Itens** | `GET` | `/api/items/export/pdf` | Exportação de relatório PDF customizado |
| **Projetos** | `GET` | `/projeto` | Listagem dos projetos registrados |
| **Projetos** | `POST` | `/projeto` | Registro de novos projetos |
| **Projetos** | `DELETE` | `/projeto/{id}` | Remoção de projetos |

---

## 📌 Status do Desenvolvimento (Roadmap)

- [x] Autenticação stateless via Spring Security com tokens JWT (Auth0).
- [x] Controle de acesso e limitação de tentativas de login (*Rate Limiting* via Caffeine).
- [x] CRUD completo de itens com persistência de imagens em Base64.
- [x] Exportação de relatórios tabulares em PDF via OpenPDF.
- [x] Camada de *Service Layer* no front-end para isolar chamadas `fetch`.
- [ ] Conclusão dos endpoints de atualização (`PUT`) e filtros por categoria para Projetos.
- [ ] Mapeamento JPA e regras de negócio para a tabela associativa `projeto_item`.
- [ ] Conexão da interface de métricas (`dashboard.html`) a rotas agregadoras de dados.
- [ ] Padronização visual da interface (UI) e criação de padrões base para novas telas.


---

## 📄 Licença

Distribuído sob a licença **MIT**. Consulte `LICENSE` para mais informações.