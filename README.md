# 🎬| Screenmatch

Essa é uma aplicação desenvolvida para facilitar a consulta de dados de séries.

Aplicação feita nos cursos **_"Java: trabalhando com lambdas, streams e Spring Framework"_**, **_"Java: Persistência de dados e consultas com Spring Data JPA"_** e **_Java: criando sua primeira API e conectando ao front"_** da _Alura_.

Front-end criado por [Monica Hilman](https://cursos.alura.com.br/user/monicahillman) disponível em: https://github.com/jacqueline-oliveira/3356-java-web-front

## 🚀 Tecnologias:

Esse projeto foi desenvolvido usando a [OMDb API](https://www.omdbapi.com) e as seguintes tecnologias: _Java 17_, _Spring Boot_, _Jackson Databidn_, _Maven_, _Visual Studio Code_, _Extensão Live Server_.

## 💻 Pré-requisitos:

- IDE Visual Studio Code
- Java 17
- MySQL
- Extensão _Live Server_

## 📃 Funcionalidades (versão para command line):

Com esta aplicação é possível:

- Pesquisar séries e adicioná-las ao banco de dados da aplicação;
- Pesquisar todas as séries já salvas no banco de dados da aplicação;
- Pesquisar séries salvas usando um trecho do título;
- Pesquisar séries salvas a partir do nome de um ator;
- Pesquisar séries salvas a partir do nome de um ator e de uma avaliação mínima;
- Verificar as cinco séries salvas com melhor avaliação;
- Pesquisar séries salvas a partir do gênero;
- Pesquisar séries salvas pelo número máximo de temporadas e mínimo de avaliação;
- Pesquisar episódios de séries salvas através de um trecho do título;
- Pesquisar uma série salva por seu título;
- Verificar os cinco episódios mais bem avaliados de uma série salva;
- Pesquisar episódios de uma série salva lançados a partir de certo ano.

## 📃 Funcionalidades (versão web):

Na versão web é possível visualizar:

- Últimas cinco séries que tiveram lançamentos recentes;
- Séries populares;
- Todas as séries;
- Todas as temporadas de uma série e o título de seus episódios;
- Os cinco episódios mais bem avaliados de uma série.

## 🛠️ Como configurar a aplicação:

1. Clone o repositório ou faça o download do arquivo zip.
2. Descompacte o arquivo zip.
3. Abra o projeto usando o VS Code.
4. Renomeie o arquivo `.envExample` para `.env` e configure as variáveis de acordo com as informações do seu banco de dados.

## 💻 Como usar (versão para command line):

1. Execute a aplicação através da classe `ScreenmatchApplicationCommandLine`.
2. Interaja com a aplicação atarvés do console.

## 💻 Como usar (versão web):

1. Execute o back-end da aplicação através da classe `ScreenmatchApplication`.
2. Execute o front-end da aplicação clicando com o botão direito no arquivo `index.html` e escolhendo "Open with Live Server".
3. Interaja com a aplicação através do navegador na guia que será aberta após o ínicio da execução com o Live Server.
