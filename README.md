# Desafio Shipp Mobile
Simulação de fechamento de pedido em estabelecimento utilizando a *Places API* da *Google*.

Como a ideia é que o app nunca vá para a loja, irei manter na master o que acredito ser "estável" :)

### Arquiteturas
* MVVM (Model View View-Model)

### Componentes
* Kotlin Coroutines
  * Utilizando Channel para captar eventos do usuário;
  * Utilizando Flow para notificar mudanças nos dados, atraves do operador flatMapLatest, assim como os eventos do usuário.
* Lifecycle
  * Para se atentar ao ciclo de vida dos fragments.
* LiveData
  * Para observar as mudanças nos dados.
* Navigation Component
  * Para controlar as transições e animações entre as telas.
