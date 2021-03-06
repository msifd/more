# Боевые действия

Конфиги записываются в `config/more/actions.json` в виде списка объектов.

Сразу начнем с примера одного из таких объектов:

```json
{
  "id": "hard_hit",
  "title": "Сильный удар",
  "tags": [
    "melee"
  ],
  "self": [
    "3d7-3",
    "score*:0.9",
    "ability:STR:1.25"
  ],
  "target": [
    "damage",
    "damage+:5"
  ]
}
```

Как можно заметить, действие разделено на целых шесть разных компонентов:
- `id` - уникалый идентификатор этого действия;
- `title` - название действия которое видит игрок. Если название начинается с точки, то оно рассматривается как ключ для строки из языковых файлов;
- `tags` - список тегов данного действия, они описывают какие-то особые свойства действия в целом;
- `self` - список из эффектов которые применяются относительно себя. Тут находятся эффекты из которых собирается счет действия.
- `target` - список из эффектов которые применяются относительно цели;

## Тэги

Науке известны следующие тэги:
- `melee, ranged, magical, intellectual` - Устанавливают тип действия. Защищаться можно только действием того же типа что и атака;
- `defencive` - Отмечает действие как оборонительное;
- `none, equip, reload` - Разные пассивные тэги для действий доступных по-умолчанию. Не трогай их.

## Эффекты

Наконец самое сладенькое. Хоть все эффекты это эффекты, но их можно разделить на три группы по степени усложнения:
- Простые эффекты
- Динамические эффекты
- Баффы

Эффекты также можно разделить еще на две группы по моменту их применения:
- Применяемые во время подсчета счета
- Применяемые по резульатам хода

Все эффекты применяются последовательно, так-что можно делать нехитрые математические штуки с уроном или счетом.

Но прежде чем мы начнем разбирательства определим термины:
- Сторона - просто название для одного из двух бойцов;
- Цель - боец, в зависимости от того, в каком из списков находится эффект (`target` или `self`);
- Другая сторона - другой боец который не Цель;

### Простые эффекты

Простые эффекты это просто. Чтобы добавить простой эффект нужно всего лишь написать его имя.
- `damage` - добавляет цели урон, который нанесла другая сторона. Если добавить эффект несколько раз, то он будет дублироваться.
- `3d7-3` - стандартный ролл для боя

На этом все.

### Динамические эффекты

Это эффекты в которые можно вводить разные значения. Название эффекта и его аргументы отделяются друг от друга двоеточиями.

Например:
`damage+:5` - добавит 5 к урону, получаемому целью.

Типы принимаемых значений:
- `INT` - целочисленное число, т.е. без запятой;
- `FLOAT` - число с плавающей запятой, т.е. с запятой;
- `STRING` - строка для непонятных значений;
- `EFFECT` - эффект. Применяется только в баффах о которых дальше.

Список эффектов счета:
- `ability:STRING:FLOAT` - добавляет значение абилки умноженное на модификатор. Первым аргументом принимают название абилки: `STR, END, REF, PER, SPR, WIL, INT, DET`;
- `score+:INT` - добавляет число к счету цели;
- `score*:FLOAT` - умножает на число весь текущий счет цели;
- `min_score:INT` - если счет цели меньше указанного числа, то его действие будет неудачным;
- `mod ability:STRING:INT` - добавляет стакающийся модификатор статы. Применяется всегда перед другими эффектами счета.

Список эффектов урона:
- `damage+:INT` - наносит цели блокируемый броней урон;
- `raw damage+:INT` - наносит неблокируемый урон;
- `damage*:FLOAT` - умножает на число весь текущий получаемый целью урон;
- `other damage+:INT` - наносит другой стороне блокируемый броней урон от имени цели;
- `other damage*:FLOAT` - умножает на число весь текущий получаемый другой стороной урон;
- `heal:INT` - лечит цель. Применяется до нанесения урона.

### Баффы

На самом деле все баффы это один единственный динамический эффект.

Пример баффа "наносит 1 урона в ход в течении 2 ходов": `buff:0:2:stack:damage+:1`.

Препарируем его:
```
buff - название эффекта баффа
  :0 - пауза перед активацией
  :2 - кол-во активаций
  :stack - способ обновления баффа
  :damage+:1 - эффект баффа
```

Баффы вызываются два раза, точно также как и другие эффекты - во время формирования счета и во время применения действия.
После каждого успешного действия они обновляются, уменьшая сначала счетчик паузы, а потом счетчик активаций. Когда они оба равны нулю, то бафф удаляется.

#### Слияние баффов
Слияние баффов - это процесс добавления баффа к имеющимся баффам. Если персонаж уже имеет бафф **такого же типа**, то может быть применена особая логика.

Способы слияния баффов:
- `replace` - заменить более сильным баффом. Если эффект нового баффа сильнее имеющегося, то он заменяет его эффект и счетчик шагов;
- `stack` - просто добавить новый бафф. Эффекты баффов будут накладываться друг на друга.
- `extend` - обновить счетчик шагов. Самый бесполезный на мой взгляд режим;

#### Вспомогательные эффекты
Применяются для тонкого контроля над активацией. Их нужно вставлять перед самим эффектом, к примеру бафф `buff:0:1:stack:role:offence:score+:-10` наложит штраф на очки только во время атаки, но не во время защиты.

Сами эффекты:
- `role:STRING:EFFECT` - эффект срабатывает только для определенного типа хода. Значения арумента: `offence` или `defence`;
- `tag:STRING:EFFECT` - эффект срабатывает если у активного действия есть указанный тег. Тэги указаны в начале.
