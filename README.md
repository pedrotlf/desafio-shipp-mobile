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
* Ao deixar o app em background, nas telas após a inicial, o app quebra
  * Motivo:
    * Estamos carregando a imagem do estabelecimento selecionado entre os fragments como um *Bitmap* através do *SavedStateHandle*. Quando o app vai para o *background*, ele tenta armazenar esse *SavedStateHandle*, porém, os dados são muito grandes para serem armazenados, devido ao *Bitmap*, e acabamos tendo uma *RuntimeException*.
	* Fizemos isso pois a *Places API* nos retorna um *Bitmap* para ser usado como a imagem do estabelecimento, porém não é uma boa prática ficar carregando esse *Bitmap* entre os *fragments*. A *viewModel* até consegue lidar com o tamanho dos dados, mas quando for necessário jogar o app para o *background*, o Android não será capaz de armazená-lo.
  * Solução:
    * O certo seria armazenar as imagens em *cache*. Poderíamos usar o componente *Room* para armazenar a imagem em uma tabela registrando também sua *photoMetadata* (dado necessário para buscar a imagem na *Places API*), dessa forma poderíamos evitar buscas desnecessárias por imagens já armazenadas, o que poupa o usuário algum tempo. Como os dados estaríam armazenados em um banco de dados local, seríamos capazes de acessar esses dados de qualquer viewModel sem precisar do *SavedStateHandle*, logo não precisaríamos nos preocupar com o app indo para o background.
