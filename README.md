## Обедающие философы на Akka

Моделирование [задачи об обедающих философах](https://ru.wikipedia.org/wiki/Задача_об_обедающих_философах) на Scala с использованием фреймворка Akka.

Краткая суть задачи: есть 5 философов и 5 вилок. Чтобы поесть, философу необходимо взять две вилки. Из-за этого условия, все философы не могут есть одновременно.

## Зависимости

* [Scala 2.13.4](https://scala-lang.org)
* [Akka 2.6.12](https://akka.io)

## Реализация

Главный метод `main` находится в объекте `Dining`.

Список классов и объектов:
* `Dining` - главный актор, порождающий вилки (`Fork`) и философов (`Philosopher`). Каждый философ знает, где для него лежит левая и правая вилка.
* `Fork` - актор, реализующий вилку. Принимает команды `Take` и `Put`, отвечает `Taken` и `Busy`.
* `Philosopher` - актор, реализующий философа. Содержит ссылки на левую и правую вилки относительно него самого. Действия (думать и есть) симулируются через метод `Thread.sleep` на `3-10 секунд` (период каждый раз генерируется). Логика его действий такова, что сначала философ берёт левую вилку. Если она свободна, то собирается взять правую вилку, иначе начинает думать. Если правая вилка оказалась свободной, то он начинает есть, иначе освобождает левую вилку и начинает думать.

## Запуск

Требуется `sbt 1.4.7`.

В папке с проектом запустить команду
```
sbt run
```

Пример вывода
```
...
[2021-02-18 10:37:58,096] [INFO] [org.dedkot.Philosopher] [akka://Table/user/Abbagnano] - Abbagnano is eating using Fork1 and Fork2
[2021-02-18 10:38:00,859] [INFO] [org.dedkot.Philosopher] [akka://Table/user/Darwin] - Darwin is eating using Fork4 and Fork5
[2021-02-18 10:38:04,491] [INFO] [org.dedkot.Philosopher] [akka://Table/user/Abbagnano] - Abbagnano is ending eating and puting Fork1 and Fork2
[2021-02-18 10:38:09,615] [INFO] [org.dedkot.Philosopher] [akka://Table/user/Darwin] - Darwin is ending eating and puting Fork4 and Fork5
[2021-02-18 10:38:11,461] [INFO] [org.dedkot.Philosopher] [akka://Table/user/Babbage] - Babbage is eating using Fork2 and Fork3
[2021-02-18 10:38:12,913] [INFO] [org.dedkot.Philosopher] [akka://Table/user/Darwin] - Darwin is eating using Fork4 and Fork5
...
```