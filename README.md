# Pós-Tech Arquitetura e Desenvolvimento Java
- Fase 4: Nesta terceira fase, o objetivo era criar um serviço de parquímetro escalável e confiável.

## Índice

- [Pré-requisitos](#pré-requisitos)
- [Instalação](#instalação)
- [Uso](#uso)
- [Relatório técnico](#relatório-técnico)

## Pré-requisitos
Para rodar o projeto na sua máquina é necessário:
- Java 17
- Gradlew
- Docker

## Instalação
Siga as etapas abaixo para configurar e executar o projeto em seu ambiente local:
1. Clone o repositório
   ```sh
   git clone https://github.com/mayaravlima/tech-challenge-4
   ```  
2. Navegue até o diretório do projeto:
   ```sh
   cd tech-challenge-4
   ```
3. Rode o comando para utilizar o Docker Compose e subir o banco de dados:
   ```sh
    docker-compose up --force-recreate -d --build
    ```
4. Rode o comando para utilizar o Docker Compose e subir o banco de dados:
   ```sh
    ./gradlew flywayMigrate
    ```
5. Acesse os endpoints
   ```sh
   localhost:8080/
   ```
## Uso
Foi utilizado o Swagger para documentar a API. Para acessar a documentação basta acessar o endpoint:

```sh
    http://localhost:8080/swagger-ui/index.html#/
```
Considerações sobre alguns endpoints:

### Category Controller


## Relatório Técnico
### Estrutura do projeto:
Escolhi implementar o Domain-Driven Design (DDD) no projeto para criar uma representação mais precisa e alinhada com o domínio do trabalho. Essa abordagem me permitiu organizar e estruturar o código de maneira mais compreensível, refletindo de forma eficaz as complexidades específicas do no contexto do projeto. O DDD ajuda a traduzir os conceitos do mundo real para o código, facilitando a manutenção e a evolução do sistema ao longo do tempo. O projeto está dividida da seguinte maneira:
- Camada de Domínio (domain):
  - Entidades: Representam objetos identificáveis com uma identidade única e têm um ciclo de vida.
  - Objetos de Valor: São objetos imutáveis que têm seu valor determinado inteiramente pelos seus atributos.
  - Agregados: Grupos de entidades e objetos de valor que são tratados como uma unidade coesa. Uma entidade dentro do agregado é considerada a raiz do agregado.  
- Camada de Aplicação (application):
    - Serviços de Aplicação (Application Services): Contêm lógica de aplicação e orquestram a interação entre entidades e objetos de valor.
    - Interfaces de Aplicação (Application Interfaces): Definem as interfaces através das quais os clientes externos interagem com a aplicação.
- Camada de Infraestrutura (infrastructure):
    - Repositórios (Repositories): Abstraem o acesso e a persistência de dados, permitindo que as entidades sejam recuperadas e armazenadas.
    - Serviços de Infraestrutura (Infrastructure Services): Fornecem funcionalidades relacionadas à infraestrutura, como logging, comunicação com serviços externos, etc.

Além disso, estamos utilizando: 
- Flyway: O Flyway foi utilizado para a criação de migração de banco de dados e assim simplificar o gerenciamento e a evolução de esquemas de banco de dados.
- JaCoCo: foi utilizado no projeto pela necessidade de avaliar e garantir a qualidade do código-fonte por meio da geração de relatórios de cobertura de testes. O JaCoCo oferece insights valiosos sobre quais partes do código estão sendo exercitadas pelos testes, permitindo uma abordagem mais eficaz na melhoria da robustez e confiabilidade do software.
- Command Pattern: Optei pelo padrão Command no projeto devido à necessidade de desacoplamento entre módulos, permitindo que solicitações sejam tratadas de forma flexível e extensível. Essa abordagem facilita a adição de novos comandos sem alterar o código existente, promovendo uma arquitetura modular e fácil de dar suporte a operações de desfazer quando necessário. A utilização do padrão Command também melhora a legibilidade e manutenção do código, promovendo uma abstração eficaz das operações executadas.
- Notification Pattern: Optei por utilizar esse padrão no projeto para facilitar a comunicação entre componentes de forma assíncrona e desacoplada. Esse padrão permite que os objetos notifiquem eventos sem precisar conhecer detalhes sobre os objetos que ouvirão as notificações, promovendo uma arquitetura mais flexível e escalável. Além disso, o Notification Pattern é útil para lidar com eventos distribuídos, proporcionando uma maneira eficaz de gerenciar a troca de informações entre diferentes partes do sistema de forma eficiente.
- Uma interface foi introduzida nos controllers, visando evitar a poluição excessiva destes com mapeamentos de requisições e configurações de documentação.

### Decisões de arquitetura
- A criação de um vídeo requer a obrigatória inclusão do id de uma categoria.
- Para a geração de métricas, implementamos a adição da coluna "click_count" na tabela de vídeos, calculando a média de cliques em relação ao total de vídeos registrados no banco de dados.
- No processo de recomendação de vídeos para um usuário, selecionamos um vídeo da sua lista de favoritos e identificamos a categoria correspondente. Em seguida, são retornados até cinco vídeos pertencentes a essa mesma categoria.
