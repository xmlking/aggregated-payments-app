# Setup

## Prerequisites

```shell
brew install curlie
brew install ktlint
brew install redpanda-data/tap/redpanda
```

```shell
sdk rm java
sdk i java 21.0.10-tem
java --version

sdk rm gradle
sdk i gradle
gradle --version

sdk i springboot
sdk i quarkus

# optional
sdk i java 21.0.10-graal
# Do you want java 21.0.10-graal to be set as default? (Y/n): n
```

[Oracle GraalVM Container Images](https://blogs.oracle.com/java/post/new-oracle-graalvm-container-images)

## IntelliJ Plugins

1. [Spotless](https://plugins.jetbrains.com/plugin/18321-spotless-gradle)
2. [Ktlint](https://plugins.jetbrains.com/plugin/15057-ktlint-unofficial-/)
3. [Kotest](https://plugins.jetbrains.com/plugin/14080-kotest)
4. [GraphQL](https://plugins.jetbrains.com/plugin/8097-graphql)
5. [Env files support](https://plugins.jetbrains.com/plugin/9525--env-files-support)
6. [lombok](https://plugins.jetbrains.com/plugin/6317-lombok)