[![MIT License][license-shield]][license-url]

<h1 align="center">Борцы Даня, Саша, Мухаммет</h1>

## О проекте

Добро пожаловать в библиотеку Kotlin Binary Tree! Эта библиотека разработана для предоставления разработчикам
эффективных и гибких реализаций трех различных типов бинарных деревьев: Binary Tree, AVL Tree и Red-Black Tree

## Как пользоватся

### Инициализация
```kotlin
val binaryTree = BinaryTree<Int, String>()
val redBlackTree = RBTree<Int, String>()
val avlTree = AVLTree<Int, String>()
```
### Базовые операции

#### Добавление
```kotlin
val binaryTree = BinaryTree<Int, String>()

binaryTree.add(1, "A")
binaryTree.add(2, "B")
binaryTree.add(3, "C")

println(binaryTree)
```

#### Поиск
```kotlin
val binaryTree = BinaryTree<Int, String>()

/* 
    добавляем ноды 
*/

// поиск существующего элемента
println(binaryTree.get(1))
println(binaryTree[3])
println(binaryTree.getOrDefault(2, "No such element"))

// поиск не существующего элемента
println(binaryTree.get(4))
println(binaryTree[5])
println(binaryTree.getOrDefault(8, "No such element"))
```
#### Вывод
```kotlin
A
C
B
null
null
No such element
```

#### Удаление
```kotlin
val binaryTree = BinaryTree<Int, String>()

/* 
    добавляем ноды 
*/

// удаление существующего элемента
binaryTree.delete(1)

// удаление не существующего элемента ничего не делает
binaryTree.delete(4)

println(binaryTree.toString())
```

#### Вывод
```text
[(2: B), (3: C)]
```

#### Присваивание
```kotlin
val binaryTree = BinaryTree<Int, String>()

/* 
    добавляем ноды 
*/

println(binaryTree.toString())

// присвоить новое значение существующему элементу
binaryTree.set(2, "D")
binaryTree[3] = "E"

println(binaryTree.toString())

// присвоить значение не существующему элементу
binaryTree.set(4, "Y")
binaryTree[5] = "X"

println(binaryTree.toString())
```

#### Вывод
```text
// изначальный вид
[(1: A), (2: B), (3: C)]

// после присваивания
[(1: A), (2: D), (3: E)]

// после присваивания значения не существующему элементу
[(1: A), (2: D), (3: E), (4: Y), (5: X)]
```

#### Минимум / Максимум
```kotlin
val binaryTree = BinaryTree<Int, String>()

binaryTree.add(1, "A")
binaryTree.add(2, "B")
binaryTree.add(3, "C")

println(binaryTree.min())
println(binaryTree.max())
```

#### Вывод
```text
(1: A)
(3: C)
```

#### Итерирование по дереву

<details>
  <summary>Итерирование по ключу</summary>

  ```kotlin
  val binaryTree = BinaryTree<Int, String>()

binaryTree.add(2, "B")
binaryTree.add(1, "A")
binaryTree.add(3, "C")

binaryTree.iterator().forEach {
    println(it.key)
}

  println(binaryTree.toString())
  ```
#### Вывод
  ```text
1
2
3
  ```
</details>

<details>
  <summary>BST итерирование</summary>

  ```kotlin
  val binaryTree = BinaryTree<Int, String>()

binaryTree.add(2, "B")
binaryTree.add(1, "A")
binaryTree.add(3, "C")

binaryTree.iterateBFS().forEach {
    println(it.key)
}

  println(binaryTree.toString())
  ```
#### Вывод
  ```text
2
1
3
  ```
</details>

<details>
  <summary>DFS итерирование</summary>

  ```kotlin
  val binaryTree = BinaryTree<Int, String>()

binaryTree.add(2, "B")
binaryTree.add(1, "A")
binaryTree.add(3, "C")

binaryTree.iterateDFS().forEach {
    println(it.key)
}

  println(binaryTree.toString())
  ```
#### Вывод
  ```text
2
1
3
  ```
</details>

#### Соединение двух деревьев
```kotlin
val binaryTree = BinaryTree<Int, String>()
val secondBinaryTree = BinaryTree<Int, String>()

binaryTree.add(2, "B")
binaryTree.add(1, "A")
binaryTree.add(3, "C")

secondBinaryTree.add(4, "D")
secondBinaryTree.add(5, "E")
secondBinaryTree.add(6, "F")


binaryTree.merge(secondBinaryTree)

println(binaryTree.toString())
```

#### Вывод
  ```text
[(1: A), (2: B), (3: C), (4: D), (5: E), (6: F)]
  ```

#### Клонирование дерева
```kotlin
val binaryTree = BinaryTree<Int, String>()

binaryTree.add(2, "B")
binaryTree.add(1, "A")
binaryTree.add(3, "C")

val cloneTree = binaryTree.clone()

println(cloneTree.toString())

```

#### Вывод
  ```text
[(1: A), (2: B), (3: C)]
  ```

### Виды выводов дерева
<details>
  <summary>Итерационный вывод</summary>

  ```kotlin
  val binaryTree = BinaryTree<Int, String>()

  binaryTree.add(1, "A")
  binaryTree.add(2, "B")
  binaryTree.add(3, "C")

  println(binaryTree.toString())
  ```
#### Вывод
  ```text
  [(1: A), (2: B), (3: C)]
  ```
</details>

<details>
  <summary>Вертикальный вывод рисунком</summary>

 ```kotlin
  val binaryTree = BinaryTree<Int, String>()

  binaryTree.add(1, "A")
  binaryTree.add(2, "B")
  binaryTree.add(3, "C")

  println(binaryTree.toString(mode = Tree.TreeStringMode.WIDTH))
  ```
#### Вывод
  ```text
  |       ┌── (3: C)
  |   ┌── (2: B)
  └── (1: A)
  ```
</details>

<details>
  <summary>Горизонтальный вывод рисунком</summary>
  
   ```kotlin
  val binaryTree = BinaryTree<Int, String>()

  binaryTree.add(1, "A")
  binaryTree.add(2, "B")
  binaryTree.add(3, "C")

  println(binaryTree.toString(mode = Tree.TreeStringMode.WIDTH))
  ```
#### Вывод
  ```text
────────┐
       (1: A)                 
          └─────┐         
               (2: B)       
                  └────┐    
                    (3: C)  
  ```
</details>
<details>
  <summary>Цветной вывод Red-Black Tree</summary>
- Добавляйте параметр к дереву

```kotlin
redBlackTree.setColored(true)
```

```kotlin
    val redBlackTree = RBTree<Int, String>()
    redBlackTree.setColored(true)

    redBlackTree.add(1, "A")
    redBlackTree.add(2, "B")
    redBlackTree.add(3, "C")

    println(redBlackTree.toString())
    println(redBlackTree.toString(mode = Tree.TreeStringMode.WIDTH))
    println(redBlackTree.toString(mode = Tree.TreeStringMode.HEIGHT))
```
</details>

## Создано с использованием

- Kotlin 1.9.22: Библиотека написана на Kotlin, используя его лаконичный синтаксис и мощные возможности.
- JDK 21: Построено с использованием Java Development Kit версии 21, обеспечивая совместимость с современными
  средами Java и используя последние усовершенствования Java.

## Главные особенности

- #### Три Варианта Бинарных Деревьев: Выберите из различных типов бинарных деревьев, чтобы удовлетворить свои конкретные требования:

- Binary Tree: Фундаментальная структура бинарного дерева, предоставляющая простые возможности вставки, удаления и поиска.
- AVL Tree: Самобалансирующееся бинарное дерево поиска, обеспечивающее оптимальную производительность для операций вставки, удаления и поиска.
- Red-Black Tree: Еще одно самобалансирующееся бинарное дерево поиска с логарифмической высотой, обеспечивающее эффективные операции для больших наборов данных.

- #### Универсальные Операции: Каждая реализация дерева поддерживает основные операции для управления структурами деревьев:
    - Поиск: Быстро находите узлы в дереве на основе ключевых значений.
    - Вставка: Добавляйте новые узлы, сохраняя целостность и баланс дерева.
    - Удаление: Удаляйте узлы из дерева, не нарушая его структурных свойств.
    - Вывод на консоль: Визуализируйте структуру дерева через печать в консоли, помогая в отладке и визуализации задач.

[//]: # (## Usage)

<!-- LICENSE -->

## Лицензия

Распространяется по лицензии MIT. См. файл `LICENSE.txt` для получения дополнительной информации.

## Авторы

- [AlexandrKudrya](https://github.com/AlexandrKudrya)
- [7gambit7](https://github.com/7gambit7)
- [VersusXX](https://github.com/VersusXX)

[license-shield]: https://img.shields.io/github/license/othneildrew/Best-README-Template.svg?style=for-the-badge:
[license-url]: https://github.com/spbu-coding-2023/trees-11/blob/main/LICENSE.txt