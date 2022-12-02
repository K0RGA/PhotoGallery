# PhotoGallery
Приложения для вывода картинок с сайта [Flickr.com](https://www.flickr.com/explore)

## О приложении
Приложение основанно на приложении описанном в книге "Android Программирование для профессионалов 4-е издание". Функционал реализован с использованием API сайта.

![](https://github.com/K0RGA/files/blob/main/PhotoGalleryExampleOfWork.gif)

### Что умеет приложение:
* Выводить недавно загруженнеы фотографии
* Находить картинки с помощью поисковой строки
* Уведомлять о появлении фотографий, которые пользователь ещё не видел
* Открывать страницу картинки с использованием WebView

### Список используемых технологий
* Весь код был написан  на **Kotlin**
* **MVVM**-архитектура
* **Kotlin coroutines** для Retrofit
* **Picasso** для загрузки изображений
* **Retrofit** для построение REST API
