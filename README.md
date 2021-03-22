# Desafio Shipp Mobile
Simulação de fechamento de pedido em estabelecimento utilizando a *Places API* da *Google*.

Como a ideia é que o app nunca vá para a loja, irei manter na *master* o que acredito ser "estável" :)

### Arquiteturas
* MVVM (Model View View-Model)

###Bibliotecas principais
* Dagger (Hilt)
  * Para injeções de dependências no geral.
* Retrofit2
  * Para consumir API.

### Componentes
* Kotlin Coroutines
  * Utilizando Channel para notificar eventos;
  * Utilizando Flow para notificar o fragment sobre mudanças na viewModel (com o auxílio do LiveData).
* Lifecycle
  * Para se atentar ao ciclo de vida dos fragments.
* LiveData
  * Para observar as notificações da viewModel.
* ViewModel
  * Para separar o tratamento de dados do ciclo de vida do fragment.
* Navigation Component
  * Para controlar a navegação e as transições entre as telas.
  * Aplicar animação nas transições.
