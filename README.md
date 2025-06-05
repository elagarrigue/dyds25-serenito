DYDS - Proyecto

Flujo General de la Aplicación
Inicio de la App
- Se configura un HttpClient con la clave de API y headers necesarios.
- Se instancia MoviesRemoteDataSourceImpl, MoviesRepositoryImpl y los ViewModel mediante Dependency Injection.
- Se lanza la ventana principal con App() y se inicializa la navegación.

Pantalla Principal (HomeScreen)
- HomeViewModel carga automáticamente las películas populares al inicializarse.
- Si no hay datos en caché, se hace una petición HTTP a TMDb.
- La respuesta (RemoteMovie) se transforma a una entidad de dominio (Movie y QualifiedMovie) para ser utilizada en la UI.
- Se actualiza el estado y se renderiza un MovieGrid.

Pantalla de Detalle (DetailScreen)
- Al seleccionar una película, se navega a detail/{movieId}.
- DetailViewModel solicita los detalles de la película mediante un caso de uso, que accede al repositorio.
- Se muestra el detalle con imagen, título, puntuación y sinopsis.

Principios Aplicados
SOLID
- S (Single Responsibility): Cada clase cumple una única responsabilidad (ej.: ViewModel se encargaba tanto de la pantalla de inicio como la de detalle, por eso fue separada en HomeViewModel y DetailViewModel).
- O (Open/Closed): Las clases están abiertas a extensión pero cerradas a modificación (por ejemplo, se puede cambiar la fuente de datos sin tocar la UI).
- L (Liskov Substitution): Las abstracciones (MoviesRepository) pueden ser reemplazadas por sus implementaciones sin romper el sistema.
- I (Interface Segregation): Las interfaces están bien separadas por responsabilidad (MoviesDataSource, UseCase).
- D (Dependency Inversion): Los ViewModels dependen de abstracciones (casos de uso), no de clases concretas.

Clean Architecture
- Data Layer: Se comunica con la API externa (TMDb) y transforma el modelo RemoteMovie.
- Domain Layer: Contiene la lógica de negocio, casos de uso y entidades puras (Movie, QualifiedMovie).
- Presentation Layer: Administra el estado de la UI con ViewModel y Compose, separada del resto de la lógica.

Clean Code
- Código legible, modular y mantenible.
- Separación clara entre capas.
- Nombres descriptivos y concisos.
- Tratamiento de errores y uso de State para UI reactiva.

Cache Inteligente
- Los datos obtenidos de la API se almacenan en memoria (MoviesCache).
- Si la API falla, el sistema puede mostrar datos cacheados en lugar de mostrar un error.