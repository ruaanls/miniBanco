# MiniBanco API

## Sobre o Projeto
O **MiniBanco** é uma API REST desenvolvida em **Java / Spring Boot** que simula operações bancárias de um internet banking, como cadastro de usuários, autenticação, consulta de saldo, transferências via **PIX** e extrato paginado de transações.  
O backend foi construído utilizando **arquitetura hexagonal (Ports & Adapters)** e princípios de **Clean Architecture**, priorizando manutenibilidade, testabilidade e baixo acoplamento entre camadas.

O projeto conta com **testes unitários abrangentes** utilizando **JUnit 5** e **Mockito**, garantindo a qualidade e confiabilidade do código através de cobertura de testes nas camadas de serviço e repositório.

Em produção, a API se conecta a um **Oracle Autonomous Database** hospedado na **Oracle Cloud**, garantindo alta disponibilidade, segurança e observabilidade do banco de dados. A solução também foi projetada para ser compatível com bancos gerenciados em outras clouds, como **Azure SQL Database**, permitindo flexibilidade de infraestrutura.  
Além disso, o projeto utiliza um **Rest Client** para integrar com uma API externa durante o fluxo de transações, enriquecendo as operações financeiras com validações adicionais.

A autenticação é feita via **JWT**, protegendo todas as rotas exceto registro e login.

**🚀 Deploy em nuvem:**  
- **Render**: exposição pública dos endpoints HTTP para consumo da API.  
- **Oracle Kubernetes Engine (OKE)**: manifests Kubernetes versionados na pasta `k8s/` permitem o deploy da aplicação em um cluster gerenciado, com pods rodando em **nodes privados**.

---

## Endpoint Base em Produção
Quando publicada na Render, a aplicação fica disponível em:

**API:** [https://minibanco-1.onrender.com](https://minibanco-1.onrender.com)

**Documentação (Swagger UI):** [https://minibanco-1.onrender.com/swagger-ui/index.html](https://minibanco-1.onrender.com/swagger-ui/index.html)

**Ambiente local:** para desenvolvimento, a API roda em `http://localhost:8080`.

---

## 📚 Documentação da API (Swagger/OpenAPI)

A API possui documentação interativa completa através do **Swagger UI**, facilitando o teste e compreensão de todos os endpoints disponíveis.

### Acessar a Documentação

**Produção (Render):** [Swagger UI](https://minibanco-1.onrender.com/swagger-ui/index.html)

**Local (Desenvolvimento):**
```
http://localhost:8080/swagger-ui.html
```

### Recursos da Documentação

- **Interface interativa**: teste todos os endpoints diretamente pelo navegador
- **Autenticação JWT integrada**: botão "Authorize" para configurar o token de acesso
- **Exemplos de requisição e resposta**: modelos completos para cada endpoint
- **Códigos de status HTTP**: documentação de todos os possíveis retornos
- **Validações e regras de negócio**: descrições detalhadas de cada operação

### Como usar a autenticação no Swagger

1. Acesse o endpoint `/auth/login` no Swagger
2. Execute a requisição com suas credenciais
3. Copie o token retornado na resposta
4. Clique no botão **"Authorize"** (🔒) no topo da página
5. Cole o token no formato: `Bearer <seu-token>`
6. Agora você pode testar os endpoints protegidos

**Nota:** Os endpoints `/auth/login` e `/auth/registro` não requerem autenticação.

---

## Arquitetura, Infraestrutura e Deploy

- **Arquitetura de software**:
  - **Hexagonal / Ports & Adapters** para isolar domínio, aplicação e infraestrutura.
  - Camadas bem definidas (`core`, `application`, `adapters` e `infra`), facilitando testes unitários e de integração.
- **Persistência de dados**:
  - **Produção**: conexão com **Oracle Autonomous Database** na Oracle Cloud.
  - **Alternativa / estudo**: suporte a **SQL Server** (por exemplo, **Azure SQL Database**) utilizando o script SQL descrito abaixo.
- **Kubernetes (Oracle OKE)**:
  - Pasta `k8s/` contém manifests de **Deployment**, **HPA** e **Namespace**, preparados para deploy em um cluster **Oracle Kubernetes Engine (OKE)**.
  - A comunicação com o banco ocorre a partir de **nodes privados**, aumentando a segurança de rede.
- **Topologia de acesso**:
  - Acesso administrativo ao cluster e banco feito via **bastion host** utilizando **chave privada**, evitando exposição pública direta.
  - A API também pode ser publicada em provedores PaaS como **Render**, facilitando o consumo por clientes web ou mobile.
- **Qualidade e Testes**:
  - **Testes unitários** com **JUnit 5** e **Mockito** para garantir a qualidade do código
  - Cobertura de testes nas camadas de serviço (`TransactionServiceImpl`, `UserServiceImpl`) e repositório
  - Uso de mocks para isolar dependências e testar comportamentos específicos
  - Testes de integração com banco de dados em memória (H2) para validação de persistência

---

## 🧪 Testes

O projeto possui uma suíte completa de **testes unitários** utilizando **JUnit 5** e **Mockito**, seguindo as melhores práticas de TDD (Test-Driven Development) e garantindo alta cobertura de código.

### Tecnologias de Teste

- **JUnit 5**: Framework de testes para Java
- **Mockito**: Framework para criação de mocks e stubs, isolando dependências
- **Spring Boot Test**: Suporte para testes de integração com contexto Spring
- **H2 Database**: Banco de dados em memória para testes de integração

### Estrutura de Testes

Os testes estão organizados nas seguintes camadas:

- **Testes de Serviço** (`application/service/`):
  - `TransactionServiceImplTest`: Testes unitários para lógica de transações PIX
  - Validação de regras de negócio, autorizações e integrações externas
  
- **Testes de Repositório** (`adapters/outbound/JPA/repositories/`):
  - `JpaUserRepositoryTest`: Testes de persistência de usuários
  - `JpaTransactionRepositoryTest`: Testes de persistência de transações

### Executar os Testes

Para executar todos os testes do projeto:

```bash
./gradlew test
```

Ou através da IDE, executando a classe de teste desejada.

---

## Como Rodar o Projeto

### 1. Configuração do Banco de Dados
Em produção o projeto foi pensado para utilizar **Oracle Autonomous Database**, mas para fins de estudo/local você pode usar uma instância de **SQL Server** (local ou em nuvem, como o **Azure SQL Database**) e executar o seguinte script para criar as tabelas necessárias:

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

---

## Autenticação
- **Registro** e **Login**: não exigem autenticação.
- **Demais endpoints**: exigem o header `Authorization: Bearer <token>` com um JWT válido.

---

## 🔐 Regras de Autorização e Segurança

O sistema implementa um **sistema robusto de autorização baseado em JWT**, garantindo que cada usuário só possa acessar e manipular dados da sua própria conta. Todas as operações protegidas validam a identidade do usuário através do token JWT fornecido.

### Como Funciona a Autorização

1. **Autenticação via JWT**: Ao fazer login, o usuário recebe um token JWT que contém o CPF do usuário autenticado.
2. **Validação do Token**: Em cada requisição protegida, o sistema:
   - Valida a assinatura e expiração do token JWT
   - Extrai o CPF do usuário do token
   - Compara com os dados da requisição para garantir autorização

### Regras Específicas por Endpoint

#### 💸 Transferências PIX (`POST /banco/pix`)

**Validação de Autorização:**
- ✅ O usuário autenticado (dono do token JWT) **deve ser o remetente** (`cpfEnvio`) da transferência
- ❌ Um usuário **não pode iniciar uma transação como destinatário** - apenas como remetente
- ❌ Um usuário **não pode fazer transferências de contas de outros usuários**

**Exemplo:**
- Se o token JWT pertence ao CPF `123.456.789-00`, esse usuário só pode fazer transferências **saindo** da conta `123.456.789-00`
- Tentar fazer uma transferência com `cpfEnvio` diferente do CPF do token resultará em erro **403 FORBIDDEN**

#### 📋 Extrato de Transações (`GET /banco/extrato/{cpf}`)

**Validação de Autorização:**
- ✅ O usuário autenticado (dono do token JWT) **só pode consultar o extrato da própria conta**
- ❌ Um usuário **não pode visualizar o extrato de outros usuários**

**Exemplo:**
- Se o token JWT pertence ao CPF `123.456.789-00`, esse usuário só pode consultar o extrato usando `/banco/extrato/123.456.789-00`
- Tentar consultar o extrato de outro CPF resultará em erro **403 FORBIDDEN**

#### 👤 Operações de Usuário

**Endpoints afetados:**
- `GET /users/saldo/{cpf}` - Consultar saldo
- `PUT /users/{cpf}` - Atualizar dados
- `DELETE /users/{cpf}` - Excluir conta

**Validação de Autorização:**
- ✅ O usuário autenticado (dono do token JWT) **só pode realizar operações na própria conta**
- ❌ Um usuário **não pode consultar, atualizar ou excluir contas de outros usuários**

**Exemplo:**
- Se o token JWT pertence ao CPF `123.456.789-00`, esse usuário só pode:
  - Consultar saldo: `/users/saldo/123.456.789-00`
  - Atualizar: `/users/123.456.789-00`
  - Excluir: `/users/123.456.789-00`
- Tentar operar em outro CPF resultará em erro **403 FORBIDDEN**

### Respostas de Erro de Autorização

Quando uma operação viola as regras de autorização, a API retorna:

```json
{
  "status": "FORBIDDEN",
  "message": "Você não pode realizar ações em contas que não sejam a sua, por favor realize ações em contas que tenham o mesmo cpf do seu login"
}
```

**HTTP Status:** `403 FORBIDDEN`



---

## Regras de Negócio
- Todos os CPFs devem estar no formato `XXX.XXX.XXX-XX`.
- O campo `tipoConta` aceita apenas os valores `COMUM` ou `LOJISTA`.
- O saldo inicial deve ser maior ou igual a zero.
- O token JWT deve ser enviado no header para todas as operações protegidas.
- **Lojistas só podem receber transações, não podem enviar valores para outros usuários.**

---

## Autoria
Projeto realizado por **Ruan Lima Silva**.

🔗 [LinkedIn](https://www.linkedin.com/in/ruanls/)
