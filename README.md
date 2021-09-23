# Desafio Shipp Mobile
Simulação de fechamento de pedido em estabelecimento utilizando a *Places API* da *Google*.

Link do desafio: https://bitbucket.org/zaittgroup/desafio-shipp-mobile/src/master/

## TO DO
* Ajustar os [bugs conhecidos](#bugs-conhecidos)
* Separar o acesso à *Places API* da *viewModel* da tela inicial. Talvez injetando-o na *viewModel* de alguma forma com o uso do *Dagger*, assim como fizemos com o consumo da API disponibilizada pelo desafio.

## Recursos utilizados na Aplicação
### Arquiteturas
* MVVM (Model View View-Model)

### Bibliotecas principais
* Dagger (Hilt)
  * Para injeções de dependências no geral.
* Retrofit2
  * Para consumir API.
* Places (Google)
  * Para consultar estabelecimentos próximos.

### Componentes
* Kotlin Coroutines
  * Utilizando Channel para notificar eventos;
  * Utilizando Flow para notificar o fragment sobre mudanças na viewModel (com o auxílio do LiveData).
* Room
  * Para armazenar dados em cache (no nosso caso, estamos armazenando os Cartões cadastrados)
* ViewModel
  * Para separar o tratamento de dados do ciclo de vida do fragment.
* Lifecycle
  * Para se atentar ao ciclo de vida dos fragments.
* LiveData
  * Para observar as notificações da viewModel.
* Navigation Component
  * Para controlar a navegação e as transições entre as telas.
  * Aplicar animação nas transições.

## Bugs conhecidos
* Após selecionar um estabelecimento que possui foto, ao deixar o app em background, o app quebra.
  * Motivo:
    * Estamos enviando a imagem do estabelecimento selecionado de um fragment a outro como um *Bitmap* através do *Bundle*. Quando o app vai para o *background*, ele tenta armazenar esse *Bundle* no *SavedStateHandle*, porém os dados são muito grandes para serem armazenados, devido ao *Bitmap*, e acabamos tendo uma *RuntimeException*.
	* Fizemos isso pois a *Places API* nos retorna um *Bitmap* para ser usado como a imagem do estabelecimento, porém não é uma boa prática enviar esse *Bitmap* entre os *fragments*. A *viewModel* até consegue lidar com o tamanho dos dados, mas quando for necessário jogar o app para o *background*, o Android não será capaz de armazená-lo.
  * Solução:
    * O certo seria utilizar a versão web-service do *Places* da Google. Esse webservice nos retornaria uma URL que conteria a imagem desejada. Esse seria carregada e mantida em cache facilmente utilizando a biblioteca *Glide* da *bumptech*
