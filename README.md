# desafio-shipp-mobile
Simulação de fechamento de pedido em estabelecimento utilizando a Places API da Google.

Como a ideia é que o app nunca vá para a loja, irei manter na master o que acredito ser "estável" :)

- Utilizando arquitetura MVVM (Model View View-Model)

- Utilizando componentes como: 

* Kotlin Flow
  * Notificando mudanças nos dados atraves do operador flatMapLatest
* LiveData
  * Para observar as mudanças
