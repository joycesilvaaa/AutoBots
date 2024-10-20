## Requisitos

Antes de rodar o projeto, verifique se você tem os seguintes itens instalados:

- **Java Development Kit (JDK)** 17
- **Apache Maven**
- **IntelliJ IDEA** ou sua IDE de preferência
- **Lombok** (essencial para o funcionamento do projeto)
- **Insomnia**  ou outra ferramenta de sua preferência (para testar as rotas).

## Como Rodar o Projeto

### Usando IntelliJ IDEA

#### Passo 1: Instalar o Lombok

Para garantir que o Lombok funcione corretamente, siga estas etapas:

1. **Abra o IntelliJ IDEA.**
2. Vá para `File > Settings` (ou `IntelliJ IDEA > Preferences` no macOS).
3. Selecione **Plugins** no menu à esquerda.
4. Na barra de pesquisa, digite **Lombok**.
5. Instale o plugin e reinicie o IntelliJ IDEA.

#### Passo 2: Clonar o Repositório

Clone este repositório para sua máquina local. No terminal, execute:

```bash
git clone https://github.com/joycesilvaaa/AutoBots
````

#### Passo 3: Abrir o Projeto no IntelliJ

Para abrir o projeto no IntelliJ IDEA, siga os passos abaixo:

1. **Inicie o IntelliJ IDEA.**
   - Certifique-se de que você está na tela inicial do aplicativo.

2. **Clique em "Open" ou "Import Project".**
   - Se você estiver na tela inicial, encontrará essas opções. Ambas funcionam para abrir um projeto existente.

3. **Navegue até o diretório do repositório clonado.**
   - Use o explorador de arquivos para localizar a pasta onde você clonou o repositório **AutoBots**.

4. **Selecione a pasta `automanager`.**
   - Clique na pasta **automanager** para selecioná-la.

5. **Clique em "OK" para abrir o projeto.**
   - O IntelliJ irá carregar o projeto e suas dependências.
     
#### Passo 4: Configurar o Maven

O IntelliJ IDEA geralmente detecta o Maven automaticamente, mas caso você precise configurar manualmente, siga estas etapas:

1. **Abra as configurações do projeto:**
   - Vá para `File > Project Structure` ou pressione `Ctrl + Alt + Shift + S` (Windows/Linux) ou `Command + ;` (macOS).

2. **Verifique o SDK do projeto:**
   - No painel esquerdo, selecione **Project**.
   - Na seção **Project SDK**, certifique-se de que está selecionado o **Java 17**. Se não estiver, clique na lista suspensa e selecione a versão correta. Caso o JDK 17 não esteja disponível, você pode adicioná-lo clicando em **New...** e localizando o diretório de instalação do JDK 17 em seu sistema.

3. **Confirme as configurações:**
   - Após selecionar o SDK correto, clique em **Apply** e depois em **OK** para fechar a janela.

#### Passo 5: Executar o Projeto

Agora que você configurou o projeto, siga estas etapas para executá-lo:

1. **Localize a classe principal:**
   - No painel de navegação do IntelliJ, encontre a classe principal do seu projeto, que geralmente é chamada de **AutomanagerApplication.java**. Essa classe contém o método `main()` que inicia a aplicação.

2. **Execute a classe principal:**
   - Clique com o botão direito do mouse na classe **AutomanagerApplication**.
   - Selecione a opção **Run 'AutomanagerApplication.main()'**. Isso iniciará a aplicação.

3. **Verifique os logs:**
   - Após alguns instantes, você verá os logs da aplicação no terminal integrado do IntelliJ. Verifique se a aplicação foi iniciada corretamente e se não há erros.

### Testando as Rotas

Para testar as rotas da aplicação, você pode utilizar **Insomnia** ou **Postman**. Abaixo estão as instruções básicas para cada uma dessas ferramentas.

#### Usando Insomnia

1. **Baixe e instale o Insomnia** se ainda não o fez. Você pode encontrar a versão mais recente no [site oficial do Insomnia](https://insomnia.rest/download).

2. **Abra o Insomnia.**

3. **Crie uma nova requisição:**
   - Clique em **Create** para iniciar uma nova requisição.

4. **Configure a requisição:**
   - Escolha o tipo de requisição (GET, POST, etc.).
   - Insira a URL da API que deseja testar, por exemplo:  
     `http://localhost:8080/cliente/clientes`

5. **Para requisições POST:**
   - Clique na aba **Body**.
   - Selecione **JSON** como formato e insira os dados do cliente em formato JSON.

6. **Envie a requisição:**
   - Clique em **Send** para enviar a requisição e visualize a resposta no painel.

