# usuario
Microsserviço de gerenciamento de usuários do sistema Agendador de Tarefas. Cuida de autenticação via JWT, CRUD de usuários e gerenciamento de endereços e telefones, com consulta de CEP integrada via ViaCEP.

## Stack
- Java 17
- Spring Boot
- Spring Security + JWT (java-jwt)
- Spring Data JPA
- PostgreSQL
- OpenFeign (integração com ViaCEP)
- Springdoc OpenAPI (Swagger)
- Gradle
- SonarQube

## API
Rotas marcadas com 🔒 exigem o header `Authorization` com um JWT válido.

| Método | Rota | Descrição | Auth |
|--------|------|-----------|------|
| POST | /usuario | Criar um novo usuário | - |
| POST | /usuario/login | Autenticar e obter token JWT | - |
| GET | /usuario?email= | Buscar usuário por e-mail | 🔒 |
| PUT | /usuario | Atualizar dados do usuário | 🔒 |
| DELETE | /usuario/{email} | Remover usuário por e-mail | 🔒 |
| POST | /usuario/endereco | Cadastrar endereço | 🔒 |
| PUT | /usuario/endereco?id= | Atualizar endereço | 🔒 |
| POST | /usuario/telefone | Cadastrar telefone | 🔒 |
| PUT | /usuario/telefone?id= | Atualizar telefone | 🔒 |
| GET | /usuario/endereco/{cep} | Consultar endereço a partir de um CEP (ViaCEP) | - |

## Regras de negocio
- Um usuário pode ter múltiplos endereços e telefones associados.
- A consulta de CEP é delegada à API pública do ViaCEP via Feign Client.
- Toda operação de escrita sobre usuário, endereço ou telefone exige token JWT válido.

## Executando localmente
Requer PostgreSQL em `localhost:5432`.
```bash
./gradlew bootRun
```
Serviço sobe na porta **8080**.

## Documentação
Com o serviço no ar, o Swagger fica disponível em `/swagger-ui.html`.

## Executando com Docker
Este serviço faz parte de um ambiente multi-container. Consulte o repositório principal da organização [agendador-tarefas](https://github.com/Lucasmanoel1) para o `docker-compose.yml` completo.

> Este serviço não deve ser acessado diretamente pelo frontend — toda comunicação passa pelo [bff-agendador](https://github.com/Lucasmanoel1/bff-agendador-tarefas).
