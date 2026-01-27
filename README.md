# MiniBanco API

## Sobre o Projeto
O **MiniBanco** é uma API REST desenvolvida em Java para simular operações bancárias básicas, como cadastro de usuários, login, consulta de saldo, transferências via PIX e extrato de transações. O projeto foi desenvolvido utilizando **arquitetura hexagonal** (Ports & Adapters), seguindo também os princípios da **arquitetura limpa** para garantir alta manutenibilidade, testabilidade e separação de responsabilidades.

A API realiza conexão com um banco de dados **SQL Server** hospedado em nuvem na **Azure**, garantindo persistência e segurança dos dados. Além disso, o projeto faz uso de um **Rest Client** para realizar chamadas a uma API externa durante o processo de transação, agregando informações externas ao fluxo financeiro.

A autenticação é feita via JWT, protegendo todas as rotas exceto registro e login.

**🚀 A aplicação está disponível em produção na nuvem através da plataforma Render, permitindo acesso público aos endpoints da API.**

---

## Endpoint Base
A aplicação estará disponível em:

```
https://minibanco.onrender.com
```

**Nota:** Para desenvolvimento local, a aplicação estará disponível em `http://localhost:8080`.

---

## Como Rodar o Projeto

### 1. Configuração do Banco de Dados (SQL Server)
Para rodar o projeto, você deve criar sua própria instância do SQL Server (local ou em nuvem, como o Azure SQL Database) e executar o seguinte script para criar as tabelas necessárias:

```sql
CREATE TABLE users (
    id VARCHAR(36) PRIMARY KEY,
    nome_completo VARCHAR(255) NOT NULL,
    cpf VARCHAR(14) NOT NULL UNIQUE,
    tipo VARCHAR(50) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    saldo DECIMAL(15,2) DEFAULT 0.00
);

CREATE TABLE transactions (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    valor DECIMAL(15,2) NOT NULL,
    id_envio VARCHAR(36) NOT NULL,
    id_recebedor VARCHAR(36) NOT NULL,
    timestamp_transacao DATETIME DEFAULT GETDATE(),
    CONSTRAINT fk_transactions_envio 
        FOREIGN KEY (id_envio) REFERENCES users(id),
    CONSTRAINT fk_transactions_recebedor 
        FOREIGN KEY (id_recebedor) REFERENCES users(id)
);

EXEC sp_rename 'transactions.timestamp_transacao', 'data_hora_transacao', 'COLUMN';
```

Após criar o banco e as tabelas, configure as variáveis de ambiente no seu sistema (ou no arquivo `.env` se estiver usando) para apontar para o seu banco:


**Exemplo para Azure SQL Database:**
```
DB_USERNAME=admin_usuario
DB_PASSWORD=MinhaSenha123!
DB_URL=jdbc:sqlserver://sqlserver-meuservidor.database.windows.net:1433;database=meuBanco;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;
```

### 2. Rodando pela IDE (IntelliJ, Eclipse, VSCode, etc)
1. Importe o projeto para sua IDE de preferência.
2. Navegue até o arquivo `MiniBancoApplication.java` (geralmente em `src/main/java/br/com/fiap/minibanco/MiniBancoApplication.java`).
3. Clique com o botão direito no arquivo e selecione **Run** (ou "Executar").
4. A aplicação será iniciada e estará disponível em `http://localhost:8080`.

### 3. Rodando pela Linha de Comando (Maven)
1. Abra o terminal na raiz do projeto.
2. Execute o comando:
   ```sh
   mvn spring-boot:run
   ```
3. Aguarde o carregamento. A aplicação estará disponível em `http://localhost:8080`.

---

## Autenticação
- **Registro** e **Login**: não exigem autenticação.
- **Demais endpoints**: exigem o header `Authorization: Bearer <token>` com um JWT válido.

---

## Endpoints

### 1. Registrar Usuário
- **Rota:** `POST /auth/registro`
- **Headers:** Nenhum
- **Body:**
```json
{
  "nome": "João Silva",
  "cpf": "123.456.789-00",
  "tipoConta": "COMUM",
  "email": "joao@email.com",
  "senha": "1234",
  "saldo": 100.00
}
```
- **Resposta de Sucesso:**
  - `200 OK` (sem corpo)
- **Possíveis Erros:**
  - Usuário já cadastrado:
    - **HTTP 400 BAD_REQUEST**
    ```json
    {
      "status": "BAD_REQUEST",
      "message": "usuário já cadastrado, por favor tente novamente com um outro cpf"
    }
    ```
  - Email já cadastrado:
    - **HTTP 400 BAD_REQUEST**
    ```json
    {
      "status": "BAD_REQUEST",
      "message": "O email: email@email.com está em uso, por favor tente outro email ou realize o login"
    }
    ```
  - Erro de validação:
    - **HTTP 400 BAD_REQUEST**
    ```json
    {
      "status": "BAD_REQUEST",
      "message": "Erro de validação",
      "errors": [
        "O campo NOME é obrigatório",
        "CPF deve estar no formato XXX.XXX.XXX-XX"
      ]
    }
    ```

### 2. Login
- **Rota:** `POST /auth/login`
- **Headers:** Nenhum
- **Body:**
```json
{
  "cpf": "123.456.789-00",
  "senha": "1234"
}
```
- **Resposta de Sucesso:**
```json
{
  "token": "jwt_token"
}
```
- **Possíveis Erros:**
  - Usuário não encontrado:
    - **HTTP 404 NOT_FOUND**
    ```json
    {
      "status": "NOT_FOUND",
      "message": "O cpf: 123.456.789-00 não existe, por favor informe um cpf válido! "
    }
    ```
  - Erro de validação:
    - **HTTP 400 BAD_REQUEST**
    ```json
    {
      "status": "BAD_REQUEST",
      "message": "Erro de validação",
      "errors": [
        "O campo SENHA é obrigatório"
      ]
    }
    ```

### 3. Consultar Saldo
- **Rota:** `GET /users/saldo/{cpf}`
- **Headers:** `Authorization: Bearer <token>`
- **Resposta de Sucesso:**
```json
{
  "nome": "João Silva",
  "cpf": "123.456.789-00",
  "tipoConta": "COMUM",
  "saldo": 100.00
}
```
- **Possíveis Erros:**
  - Usuário não encontrado:
    - **HTTP 404 NOT_FOUND**
    ```json
    {
      "status": "NOT_FOUND",
      "message": "O cpf: 123.456.789-00 não existe, por favor informe um cpf válido! "
    }
    ```
  - Token inválido ou ausente: (mensagem padrão do Spring Security ou customizada)

### 4. Atualizar Usuário
- **Rota:** `PUT /users/{cpf}`
- **Headers:** `Authorization: Bearer <token>`
- **Body:** (igual ao registro)
- **Resposta de Sucesso:**
```json
{
  "nome": "João Silva",
  "cpf": "123.456.789-00",
  "tipoConta": "COMUM",
  "saldo": 150.00
}
```
- **Possíveis Erros:**
  - Usuário não encontrado:
    - **HTTP 404 NOT_FOUND**
    ```json
    {
      "status": "NOT_FOUND",
      "message": "O cpf: 123.456.789-00 não existe, por favor informe um cpf válido! "
    }
    ```
  - Email já cadastrado:
    - **HTTP 400 BAD_REQUEST**
    ```json
    {
      "status": "BAD_REQUEST",
      "message": "O email: email@email.com está em uso, por favor tente outro email ou realize o login"
    }
    ```
  - Erro de validação: (igual ao registro)
  - Ação não permitida:
    - **HTTP 403 FORBIDDEN**
    ```json
    {
      "status": "FORBIDDEN",
      "message": "Você não pode realizar ações em contas que não sejam a sua, por favor realize ações de contas que tenham o mesmo cpf do seu login"
    }
    ```

### 5. Excluir Usuário
- **Rota:** `DELETE /users/{cpf}`
- **Headers:** `Authorization: Bearer <token>`
- **Resposta de Sucesso:**
  - `200 OK` (sem corpo)
- **Possíveis Erros:**
  - Usuário não encontrado:
    - **HTTP 404 NOT_FOUND**
    ```json
    {
      "status": "NOT_FOUND",
      "message": "O cpf: 123.456.789-00 não existe, por favor informe um cpf válido! "
    }
    ```
  - Ação não permitida:
    - **HTTP 403 FORBIDDEN**
    ```json
    {
      "status": "FORBIDDEN",
      "message": "Você não pode realizar ações em contas que não sejam a sua, por favor realize ações de contas que tenham o mesmo cpf do seu login"
    }
    ```

### 6. Transferência PIX
- **Rota:** `POST /banco/pix`
- **Headers:** `Authorization: Bearer <token>`
- **Body:**
```json
{
  "valor": 50.00,
  "cpfEnvio": "123.456.789-00",
  "cpfRecebimento": "987.654.321-00"
}
```
- **Resposta de Sucesso:**
```json
{
  "id": 1,
  "valor": 50.00,
  "dataHora": "2024-06-01T12:00:00Z",
  "usuarioEnvio": {
    "nomeCompleto": "João Silva",
    "cpf": "123.456.789-00",
    "tipo": "COMUM"
  },
  "usuarioRecebimento": {
    "nomeCompleto": "Maria Souza",
    "cpf": "987.654.321-00",
    "tipo": "LOJISTA"
  }
}
```
- **Possíveis Erros:**
  - Saldo insuficiente:
    - **HTTP 403 FORBIDDEN**
    ```json
    {
      "status": "FORBIDDEN",
      "message": "Sua transferência foi NEGADA, por saldo insuficiente. Por favor verifique seu saldo atual disponível e tente novamente"
    }
    ```
  - Transferência para o mesmo CPF:
    - **HTTP 400 BAD_REQUEST**
    ```json
    {
      "status": "BAD_REQUEST",
      "message": "Não é possível fazer uma transferência para o mesmo cpf, por favor tente novamente com um outro cpf"
    }
    ```
  - Transação não permitida:
    - **HTTP 403 FORBIDDEN**
    ```json
    {
      "status": "FORBIDDEN",
      "message": "Sua transação foi negada por motivos de segurança, tente novamente em alguns minutos"
    }
    ```
  - Transação não encontrada:
    - **HTTP 404 NOT_FOUND**
    ```json
    {
      "status": "NOT_FOUND",
      "message": "Não existe nenhuma transação com o id: 1 em nosso sistema, por favor tente novamente com um outro id"
    }
    ```

### 7. Extrato
- **Rota:** `GET /banco/extrato/{cpf}/{page}`
  - O parâmetro `{page}` é um PathVariable que indica o número da página desejada (começando em 0). Para acessar outras páginas do extrato, basta alterar esse valor na URL.
- **Headers:** `Authorization: Bearer <token>`
- **Resposta de Sucesso:**
```json
{
  "content": [
    {
      "id": 1,
      "valor": 50.00,
      "dataHora": "2024-06-01T12:00:00Z",
      "usuarioEnvio": {
        "nomeCompleto": "João Silva",
        "cpf": "123.456.789-00",
        "tipo": "COMUM"
      },
      "usuarioRecebimento": {
        "nomeCompleto": "Maria Souza",
        "cpf": "987.654.321-00",
        "tipo": "LOJISTA"
      }
    }
  ],
  "totalPages": 1,
  "totalElements": 1,
  "number": 0
}
```
- **Possíveis Erros:**
  - Sem transações para o CPF informado:
    - **HTTP 404 NOT_FOUND**
    ```json
    {
      "status": "NOT_FOUND",
      "message": "Não foram encontradas nenhuma transferência no CPF informado."
    }
    ```
  - Usuário não encontrado: (igual ao consultar saldo)
---

## Exceção OracleInputException (Erro Genérico)

A exceção `OracleInputException` pode ocorrer em qualquer endpoint da API, indicando um erro interno inesperado ou de inconsistência de dados. Sempre que ela ocorrer, a resposta será:

- **HTTP 500 INTERNAL_SERVER_ERROR**
```json
{
  "status": "INTERNAL_SERVER_ERROR",
  "message": "Erro de inconsistência de dados, tente novamente em alguns minutos"
}
```

---

## Regras de Negócio
- Todos os CPFs devem estar no formato `XXX.XXX.XXX-XX`.
- O campo `tipoConta` aceita apenas os valores `COMUM` ou `LOJISTA`.
- O saldo inicial deve ser maior ou igual a zero.
- O token JWT deve ser enviado no header para todas as operações protegidas.
- **Lojistas só podem receber transações, não podem enviar valores para outros usuários.**

---

## Autoria
Projeto realizado por Ruan Lima Silva.
