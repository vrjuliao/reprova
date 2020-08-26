# Reprova

Trabalho prático da disciplina de Reuso de Software, DCC/UFMG, 2020/1

## Instruções de construção/ambiente (Eclipse)

A forma mais simples de se importar e configurar o projeto é utilizando a IDE [Eclipse](https://www.eclipse.org/). Após instalar e abrir o Eclipse, basta importar o diretório no qual este projeto foi baixado.

## Instruções de construção/ambiente (UNIX)

O makefile do projeto contém instruções para construção do projeto utilizando [Maven](https://maven.apache.org/), ferramenta de gerenciamento de projetos Java. Para construir o projeto é necessário primeiro [instalar o Maven](https://maven.apache.org/install.html). No tutorial de instalação se menciona o uso da variável de ambiente `JAVA_HOME`; instruções para sua configuração em MacOS pode ser encontrada [aqui](https://mkyong.com/java/how-to-set-java_home-environment-variable-on-mac-os-x/).

Com o mvn instalado, basta rodar o comando `make build` (considerando que o programa `make` esteja instalado).

A execução do programa, entretanto, depende de um banco de dados MongoDB funcionando. Para este fim existe um arquivo `docker-compose.yml` no diretório raiz do projeto. Para executá-lo basta ter o Docker Desktop instalado em sua máquina e executar o comando `docker-compose up` no mesmo diretório no qual o arquivo `docker-compose.yml` está presente. Este comando irá levantar o container do MongoDB  e um container contendo o projeto.

Alternativamente é possível iniciar o programa separadamente mas com uma instância do MongoDB sendo executada no local especificado no código. Esta alternativa também é facilitada pelo uso de Docker, bastando levantar a imagem utilizada no compose via `docker run`:

`docker run -p27017:27017 mvertes/alpine-mongo`

Com isto, no Eclipse, se torna necessário finalmente configurar três variáveis de ambiente:

* `REPROVA_MONGO`, que contém a URL do servidor Mongo

* `PORT`, que representa a porta na qual o serviço do Reprova irá rodar

* `REPROVA_TOKEN`, que representa o token utilizado para autenticar as requisições que precisam deste recurso

 Apesar de se tratarem de variáveis de ambiente eu não consegui fazer com que o programa as lesse apenas exportando-as no terminal. A maneira pela qual consegui, graças a [esta resposta no Stack Overflow](https://stackoverflow.com/a/12810433/4357295), se baseia em configurar as variáveis de ambiente diretamente no Eclipse, o que pode ser feito segundo as imagens a seguir:

![Localidade Eclipse](https://github.com/ghapereira/reprova/blob/master/assets/location.png)
![Variáveis](https://github.com/ghapereira/reprova/blob/master/assets/envs.png)

Após configurar estas variáveis deve ser o suficiente clicar no botão de executar na própria janela de configuração das variáveis. Uma outra maneira de se executar o projeto é selecionar o arquivo principal (no caso, `br.engsoft.reprova.Reprova.java`) com o botão direito e clicar em "Run as".

## Troubleshooting

Alguns dos passos que fiz durante as tentativas de se rodar o projeto foram utilizar, no diretório raiz do projeto, os comandos `mvn compile` e `mvn package`, de acordo com [essa documentação](https://spring.io/guides/gs/maven/). Não foi o suficiente, mas talvez seja necessário no processo. Acredito que apenas as instruções acima devam ser suficientes; caso contrário, vale a pena seguir estas aqui.

## Instruções de execução

Ao ser iniciado, o servidor do Reprova escuta em `localhost:8888`. Se acessar este endpoint pelo navegador, ou fazer um `GET` via algum cliente REST (como o Postman), serão exibidas as questões públicas gravadas. Se acessar o endpoint passando como parâmetro o token (por exemplo, se seu token é `ABC`, o endpoint para a requisição seria `localhost:8888?token=ABC`) todas as questões serão listadas.

Para inserir uma questão é necessário fazer uma requisição `POST` para `localhost:8888/api/questions?token=ABC`, com um payload no mesmo formato do exemplo:

```JSON
{
    "theme": "Software Engineering",
    "description": "Sample private description",
    "statement": "A sample private software engineering question",
    "pvt": false
}
```

O atributo `pvt` pode ser omitido, caso em que o Reprova o interpreta como `true`. Caso seja especificado ele pode assumir os valores tanto `true` quanto `false`, indicando se a questão é privada ou não.

Uma coleção do Postman foi incluída no projeto para efeitos de teste.
