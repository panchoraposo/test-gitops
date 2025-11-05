# credit-approval-mcp

MCP Server para el servicio de aprobación de créditos de NeuralBank.

Este proyecto expone las funcionalidades del servicio de aprobación de créditos como herramientas MCP (Model Context Protocol), permitiendo que sistemas de IA interactúen con el sistema de aprobación de créditos.

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:

```shell script
./mvnw quarkus:dev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at <http://localhost:8082/q/dev/>.

## Packaging and running the application

The application can be packaged using:

```shell script
./mvnw package
```

It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.

The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.

## Creating a native executable

You can create a native executable using:

```shell script
./mvnw package -Dnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using:

```shell script
./mvnw package -Dnative -Dquarkus.native.container-build=true
```

## Related Guides

- MCP Server - HTTP/SSE ([guide](https://docs.quarkiverse.io/quarkus-mcp-server/dev/index.html)): This extension enables developers to implement the MCP server features easily.

## Provided Code

### REST

Easily start your REST Web Services

[Related guide section...](https://quarkus.io/guides/getting-started-reactive#reactive-jax-rs-resources)

