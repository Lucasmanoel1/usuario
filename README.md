# 👤 MS-Usuario

Microserviço responsável pelo gerenciamento completo de usuários dentro da arquitetura do **Agendador de Tarefas**. Fornece autenticação via JWT, CRUD de usuários e gerenciamento de endereços e telefones, além de integração com a API do ViaCEP.

---

## 🏗️ Arquitetura do sistema

Este serviço faz parte de uma arquitetura de microserviços composta por 4 módulos:

```
Frontend
    └── BFF (bff-agendador) :8083
            ├── MS-Usuario      :8080  → PostgreSQL
            ├── MS-Agendador    :8081  → MongoDB
            └── MS-Notificacao  :8082  → SMTP (e-mail)
```

> **Importante:** Os endpoints deste serviço **não devem ser acessados diretamente**. Toda comunicação deve passar pelo BFF.

---

## 🚀 Tecnologias

| Tecnologia | Uso |
|---|---|
| Java 17 | Linguagem |
| Spring Boot 4.0.3 | Framework |
| Spring Security + JWT (JJWT 0.13) | Autenticação |
| Spring Data JPA | Persistência |
| PostgreSQL | Banco de dados |
| Spring Cloud OpenFeign | Integração com ViaCEP |
| Springdoc OpenAPI (Swagger) | Documentação |
| Lombok | Redução de boilerplate |
| SonarQube | Qualidade de código |
| Docker | Containerização |
| GitHub Actions | CI/CD |

---

## 📋 Endpoints

### Usuário
| Método | Rota | Descrição | Auth |
|---|---|---|---|
| `POST` | `/usuario` | Cria novo usuário | ❌ |
| `POST` | `/usuario/login` | Realiza login e retorna token JWT | ❌ |
| `GET` | `/usuario?email=` | Busca usuário por e-mail | ✅ |
| `PUT` | `/usuario` | Atualiza dados do usuário | ✅ |
| `DELETE` | `/usuario/{email}` | Remove usuário por e-mail | ✅ |

### Endereço
| Método | Rota | Descrição | Auth |
|---|---|---|---|
| `POST` | `/usuario/endereco` | Cadastra novo endereço | ✅ |
| `PUT` | `/usuario/endereco?id=` | Atualiza endereço por ID | ✅ |
| `GET` | `/usuario/endereco/{cep}` | Consulta endereço via ViaCEP | ❌ |

### Telefone
| Método | Rota | Descrição | Auth |
|---|---|---|---|
| `POST` | `/usuario/telefone` | Cadastra novo telefone | ✅ |
| `PUT` | `/usuario/telefone?id=` | Atualiza telefone por ID | ✅ |

> ✅ = Requer header `Authorization: Bearer {token}`

---

## ⚙️ Como executar

### Pré-requisitos
- Docker e Docker Compose instalados

### Com Docker Compose

```bash
docker-compose up --build
```

O serviço subirá em `http://localhost:8080`

### Variáveis de ambiente

| Variável | Descrição | Padrão |
|---|---|---|
| `SPRING_DATASOURCE_URL` | URL do PostgreSQL | `jdbc:postgresql://localhost:5432/db_usuario` |
| `SPRING_DATASOURCE_USERNAME` | Usuário do banco | `postgres` |
| `SPRING_DATASOURCE_PASSWORD` | Senha do banco | `1234` |

---

## 📖 Documentação (Swagger)

Após subir o serviço, acesse:

```
http://localhost:8080/swagger-ui.html
```

---

## 🗂️ Estrutura do projeto

```
src/main/java/com/lucasmanoel/usuario/
├── business/
│   ├── UsuarioService.java        # Regras de negócio
│   ├── ViaCepService.java         # Integração com ViaCEP
│   ├── converter/                 # Conversão entidade ↔ DTO
│   └── dto/                       # Data Transfer Objects
├── controller/
│   └── UsuarioController.java     # Endpoints REST
├── infrastructure/
│   ├── clients/                   # Feign Client (ViaCEP)
│   ├── entity/                    # Entidades JPA
│   ├── exceptions/                # Exceções customizadas
│   ├── repository/                # Repositórios JPA
│   └── security/                  # JWT + Spring Security
```

---

## 🔗 Repositórios relacionados

- [bff-agendador](https://github.com/Lucasmanoel1/bff-agendador-tarefas) — Gateway de entrada
- [agendador-tarefas](https://github.com/Lucasmanoel1/agendador-tarefas) — Gerenciamento de tarefas
- [notificacao](https://github.com/Lucasmanoel1/notificacao) — Envio de e-mails
