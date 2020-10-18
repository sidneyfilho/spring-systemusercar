<pre align="center">
  _______     _______ _______ ______ __  __   _    _  _____ ______ _____     _____          _____  
 / ____\ \   / / ____|__   __|  ____|  \/  | | |  | |/ ____|  ____|  __ \   / ____|   /\   |  __ \ 
| (___  \ \_/ / (___    | |  | |__  | \  / | | |  | | (___ | |__  | |__) | | |       /  \  | |__) |
 \___ \  \   / \___ \   | |  |  __| | |\/| | | |  | |\___ \|  __| |  _  /  | |      / /\ \ |  _  / 
 ____) |  | |  ____) |  | |  | |____| |  | | | |__| |____) | |____| | \ \  | |____ / ____ \| | \ \ 
|_____/   |_| |_____/   |_|  |______|_|  |_|  \____/|_____/|______|_|  \_\  \_____/_/    \_\_|  \_\

</pre>

<p align="center">Desafio Pitang - API RESTful para Sistema de Usuários de Carros </p>

## :house: Começando

1. Clone este repo usando: `https://github.com/sidneyfilho/systemusercar.git`
2. Instale os pacotes usando o gerenciador `mvn clean install`
3. Entre na pasta Target e rode o comando `java -jar systemcaruser-0.0.1-SNAPSHOT.jar`
4. Use Swagger para simular as requisições acessando: http://localhost/swagger-ui.html
5. Verifique as informações dos dados usando o banco H2, acessando: http://localhost/h2-console

## 👨🏼‍💻 Estórias de Usuário

* #001 - Como usuário, quero fazer login no sistema, para que eu possa aproveitar o serviço do site;
* #002 - Enquanto com usuário, quero ter o controle para cadastrar, buscar, remover e atualizar os usuários no sistema, para que eu possa ter a gestão;
* #003 - Como usuário logado, quero receber informações do meu acesso, para que eu possa ver em detalhes o meu perfil;
* #004 - Enquanto com usuário autenticado, quero ter o controle para cadastrar, buscar, remover e atualizar os carros no sistema, para que eu me sinta mais no controle;

## :bulb: Solução

Seguindo o raciocínio de **Estórias de Usuário**, a solução foi criar uma API RESTful onde existem duas entidades principais (User e Car), ambas tem como objetivo englobar a regra de negocio passada pelo desafio, mais detalhes do que foi usado como tecnologia encontrasse mais abaixo no tópico **Recursos**.

## :tada: Recursos

* Utilizado **Java** ``v11.0.8``;
* Framework **Spring** ``v2.1.17``;
* Atribuido **JWT** como token e **Spring Security** para validar as requisições;
* Servidor **Tomcat** embutido na aplicação;
* Processo de build via **Maven**;
* Banco de dados em memória **H2**;
* Persistência com **JPA/Hibernate** para mapear as entidades e fazer a comunicação com uma base;
* Testes unitários usando **MockMvc**;
* **Swagger** para simular as requisições dos endpoints;
* **Exceptions Handle**, exceção customizadas;
* Redução de código com **Lombok**;
* Foi usado **BCryptPasswordEncoder** do Spring para criptografar as senhas;
* Obs: O **requisito extra** foi feito;
* Projeto Frontend: https://github.com/sidneyfilho/systemusercar_frontend
* Disponibilização da API rodando no host **Heroku**;
* https://desafio-pitang-java.herokuapp.com/swagger-ui.html

## :file_folder: Estrutura de arquivo
```
systemusercar/
 │
 ├── src/main/java/
 │   └── com.pitang.systemusercar
 │       ├── configuration
 │       │   └── SwaggerConfig.java
 │       │
 │       ├── controller
 │       │   ├── AuthController.java
 │       │   ├── CarController.java
 │       │   └── UserController.java
 │       │
 │       ├── exception
 │       │   └── CustomException.java
 │       │
 │       ├── model
 │       │   ├── Car.java
 │       │   ├── User.java
 │       │   └── dto
 │       │       ├── AuthDTO.java
 │       │       ├── CarDTO.java
 │       │       └── UserDTO.java
 │       │
 │       ├── repository
 │       │   ├── CarRepository.java
 │       │   └── UserRepository.java
 │       │
 │       ├── security
 │       │   ├── JwtFilterAuth.java
 │       │   └── WebSecurity.java
 │       │
 │       ├── service
 │       │   ├── AuthService.java
 │       │   ├── CarService.java
 │       │   └── UserService.java
 │       │
 │       └── SystemUserCarApplication.java
 │
 ├── test/java/
 │   └── com.pitang.systemusercar
 │       └── SystemUserCarApplicationTests.java
 │
 ├── src/main/resources/
 │   └── application.yml
 │
 ├── .gitignore
 ├── mvnw/mvnw.cmd
 ├── pom.xml
 ├── README.md
 └── system.properties.md

```

## :copyright: Autorização
Sistema feito exclusivamente para o desafio da empresa Pitang, desenvolvido por Sidney Filho.
