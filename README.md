## Обедающие философы на Akka

Моделирование [задачи об обедающих философах](https://ru.wikipedia.org/wiki/Задача_об_обедающих_философах) на Scala с использованием фреймворка Akka.

Краткая суть задачи: есть 5 философов и 5 вилок. Чтобы поесть, философу необходимо взять две вилки. Из-за этого условия, все философы не могут есть одновременно.

## Зависимости

* [Scala 2.13.4](https://scala-lang.org)
* [Akka 2.6.12](https://akka.io)

## Реализация

Главный метод `main` находится в объекте `Dining`.

Список классов и объектов:
* `Dining` - главный актор, порождающий вилки (`Fork`) и философов (`Philosopher`). Каждому философу знает, где для него лежит левая и правая вилка.
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
[2021-02-15 14:23:18,871] [INFO] [org.dedkot.Philosopher] [akka://Table/user/Darwin] - Darwin eats use Fork4 and Fork5
[2021-02-15 14:23:22,351] [INFO] [org.dedkot.Philosopher] [akka://Table/user/Babbage] - Babbage eats use Fork2 and Fork3
[2021-02-15 14:23:26,737] [INFO] [org.dedkot.Philosopher] [akka://Table/user/Darwin] - Darwin end eats and put Fork4 and Fork5
[2021-02-15 14:23:26,883] [INFO] [org.dedkot.Philosopher] [akka://Table/user/Einstein] - Einstein eats use Fork5 and Fork1
[2021-02-15 14:23:29,214] [INFO] [org.dedkot.Philosopher] [akka://Table/user/Babbage] - Babbage end eats and put Fork2 and Fork3
[2021-02-15 14:23:34,006] [INFO] [org.dedkot.Philosopher] [akka://Table/user/Cabral] - Cabral eats use Fork3 and Fork4
...
```