# Blogs api

Uma api de blogs onde você pode fazer postagens, adicionar categorias a essas postagens,
fazer comentários, criar usuários e mais.

## Documentação da api
🚧 **Coming soon** 🚧

## Rodando a aplicação

Para rodar o projeto você precis ter na sua máquina o docker e o docker compose.
Com o docker instalado e as variáveis de ambiente configuradas basta executar:

```bash
docker compose up -d
```

Após isso o docker compose vai iniciar o container do mysql na porta `3306`
e o container da api na porta `8000`. Você pode modificar essas portas
no arquivo `docker-compose.yml`

## Stacks utilizadas

### Java 17

- Aprimorar meus conhecimentos com a linguagem.
- É uma linguagem consolidada no mercado.
- Fortemente tipada o que ajuda tanto para escrever como para debugar códigos.
- Eu gosto de java 🙂.

### SpringBoot / SpringSecurity / SpringData...

- O maior framework web do marcado quando falamos de java.
- Ecossistema simples e ao mesmo tempo completo.
- Comunidade grande.
- Aprimorar meus conhecimentos com spring.

### Mysql

- Simples e direto ao ponto mas poderia ser qualquer banco sql no lugar.

## Próximos passos

- [ ] Documentar utilizando swagger
- [ ] Isolar o core da aplicação para não depender do spring (Adapter)