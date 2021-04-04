# Weather

# Тестовое задание
# Общее описание
Приложение отображает текущую погоду.
Погода отображается для текущего местоположения устройства.

Отображаемые данные:
- температура воздуха, в градусах Цельсия
- скорость и направление ветра
- картинка, соответствующая текущим погодным условиям

Для получения этих данных использовать API от OpenWeatherMap.org
см. http://openweathermap.org/current -> By geographic coordinates

Данные должны запрашиваться в случае, если
* пользователь явно запросил обновление данных
* с момента последней успешной загрузки данных прошло более 10 минут либо она ещё не выполнялась 10 минут отсчитываем с момента последнего поднятия активити из фона - если она стала активной и не уходила в фон, данные перезапрашивать, для простоты реализации, не надо. Проверяем необходимость актуализации только при переходе в видимое состояние (*onResume*)

Загрузка данных в случае, когда приложение не загружено, не требуется 
(то есть фоновая загрузка, отображение в области нотификаций - не нужна).

# По реализации
## Хранение данных
Загруженные данные должны храниться в локальной БД. По выбору - *SQLite, Room, Realm*. 
У нас в приложении сейчас используется Realm, поэтому он будет предпочтительнее. 
## Загрузка данных
Желательно использование *Retrofit*.
Для загрузки изображений желательно использовать *Picasso* или *Glide*.
На текущий момент для нас предпочтительнее *Glide*
## Получение данных о местоположении
Для получения данных о местоположении должны использоваться штатные средства.
Должна выполняться стандартная проверка на наличие и актуальность установленной версии *Google Play Services*, на текущие настройки устройства, управляющие доступом к данным о местоположении.
## UI
Ожидается использование стандартного Toolbar с кнопкой "Обновить".
Стили текста, общий внешний вид ожидается в рамках material design.
## Разное
Минимальная версия ОС - SDK 19 (Андроид 4.4)
Язык - Котлин. 
Рекомендуемая среда разработки - Android Studio актуальной версии.
Желательно использование Dagger 2.
Предпочтительно использование RxJava, корутины в наш проект пока не завезли. 
## На что мы смотрим
Общая структура приложения - разбиение по модулям/пакетам, их взаимодействие. 

Архитектурный стиль приложения - *clean architecture, MVP, MVVM*.

Поведение приложения в нештатных сценариях:
* нет сервисов google play
* ошибка при определении координат текущего местоположения
* ошибка сетевого запроса.
