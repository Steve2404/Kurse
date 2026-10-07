# Projet 4 — Corrigé étape par étape

> **Quand le lire :** ouvre **seulement la section de l'étape** que tu viens de terminer, pour comparer ton code et tes réponses. Le programme complet est dans [`Sensors.java`](Sensors.java).
>
> Les messages d'erreur et les valeurs ci-dessous ont été obtenus en direct avec **JDK 17** (`javac` / `java` 17.0.18).

---

## Étape 1 — La trame et les contrôles qui se comptent

**Le code de l'étape :**

```java
class Frame {
    int temperature;
    int humidity;
    int battery;
    boolean signed;

    Frame(int temperature, int humidity, int battery, boolean signed) {
        this.temperature = temperature;
        this.humidity = humidity;
        this.battery = battery;
        this.signed = signed;
    }
}
```

Dans `Sensors` :

```java
static int frames;
static int controls;

static boolean temperatureOk(Frame f) {
    controls++;
    return f.temperature >= -20 && f.temperature <= 50;
}
// humidityOk : 0..100 ; batteryOk : > 10 ; signatureOk : f.signed — même modèle
```

**L'idée :** le `controls++` est un **effet de bord**. Il laisse une trace de chaque exécution, et c'est cette trace qui rend visible le court-circuit.

---

## Étape 2 — `&&` contre `&`

**Le code de l'étape :**

```java
controls = 0;
boolean lazy = temperatureOk(f) && humidityOk(f) && batteryOk(f) && signatureOk(f);
int lazyControls = controls;
controls = 0;
boolean eager = temperatureOk(f) & humidityOk(f) & batteryOk(f) & signatureOk(f);
int eagerControls = controls;
```

**La prédiction :**

| Trame | 1er contrôle qui échoue | `&&` | `&` |
|---|---|---|---|
| #1 (22, 55, 80, signée) | aucun | 4 | 4 |
| #2 (70 °C) | température | **1** | 4 |
| #3 (humidité 120) | humidité | **2** | 4 |
| #4 (−30 °C) | température | **1** | 4 |
| #5 (non signée) | signature, le dernier | 4 | 4 |

**Questions :**
- **Le verdict peut-il différer ?** **Non.** Sur des `boolean`, `&` et `&&` calculent la même valeur logique, d'où `meme verdict true` partout.
- **Les effets de bord ?** **Oui.** Avec `&&`, les contrôles après le premier `false` ne s'exécutent **pas** : leurs `controls++` n'ont pas lieu. Avec `&`, tout s'exécute.
- **Quand préférer `&` ?** Quand on **veut** que les deux côtés s'exécutent, par exemple pour journaliser ou compter tous les contrôles. C'est rare. Dans la majorité des cas, `&&` est préférable : plus rapide, et il protège, comme dans `s != null && s.length() > 0`.

---

## Étape 3 — `^` et `||`

**Le code de l'étape :**

```java
boolean oneClimateFault = !temperatureOk(f) ^ !humidityOk(f);
boolean alert = f.temperature > 45 || f.battery < 20;
return "trame #" + ++frames + " (" + … + ") : " + (lazy ? "valide" : "rejetee")
        + " | && " + lazyControls + " controle(s), & " + eagerControls + " | meme verdict " + (lazy == eager)
        + " | un seul defaut climat " + oneClimateFault + " | alerte " + (alert ? "oui" : "non");
```

**`^` :** « l'un ou l'autre, **mais pas les deux** ».
- Trame #2 : la température échoue, l'humidité non, donc `true`.
- Trame #3 : l'inverse, donc `true`.
- Trame #4 : **les deux** échouent, donc `false`, alors que `||` aurait donné `true`.

**`||` :** « au moins un ».
- #2 (70 > 45), #3 et #5 (batterie < 20) déclenchent l'alerte.
- `||` court-circuite aussi : si la température dépasse 45, la batterie n'est pas testée.

---

## Étape 4 — Incréments, affectations, références

**Les incréments, terme par terme :**

| Expression | Terme | Valeur rendue | `id` après |
|---|---|---|---|
| `a = id++ + ++id` | `id++` | 5 | 6 |
|  | `++id` | 7 | 7 |
|  | **a** | 5 + 7 = **12** | 7 |
| `b = id-- - --id` | `id--` | 7 | 6 |
|  | `--id` | 5 | 5 |
|  | **b** | 7 − 5 = **2** | **5** |

**Les affectations :**
- **`x = y = 4;`** L'affectation est **associative à droite** : `x = (y = 4)`. L'expression `y = 4` vaut **4** (la valeur affectée), qui est ensuite affectée à `x`.
- **`z = (x += 2) * x;`** Les opérandes s'évaluent de **gauche à droite**. `(x += 2)` fait passer `x` de 4 à 6 et vaut 6. Le `x` de droite est lu **après**, il vaut donc 6 aussi : 6 × 6 = **36**.
- **`(flag = true) ? … : …`** C'est une **affectation**, pas une comparaison. Elle vaut `true`, et c'est un `boolean`, donc c'est accepté comme condition. Avec un `int`, le même piège ne compile pas : `(k = 1) ? …` donne `error: incompatible types: int cannot be converted to boolean`. Avec des `boolean`, `javac` ne peut pas t'aider.

**Les références :**
- `f1 == f2` vaut `false` : deux `new` créent **deux objets**, même avec un contenu identique.
- `f1 == f3` vaut `true` : `f3 = f1` copie la **référence**, les deux variables désignent le même objet.
- `==` sur des objets compare l'**identité**, jamais le contenu.

**`instanceof` :**
- `number instanceof Integer` vaut `true`, et `number instanceof Number` vaut `true` aussi : `Integer` **hérite** de `Number`.
- `text instanceof Integer` vaut `false` : un `String` n'est pas un `Integer`.
- `nothing instanceof Object` vaut **`false`** : `null instanceof X` est **toujours** `false`, quel que soit X.

**Expérience — `"texte" instanceof Integer` écrit directement :**

```
error: incompatible types: String cannot be converted to Integer
```

`javac` connaît le type exact (`String`). Il sait qu'aucun objet ne peut être à la fois un `String` et un `Integer`, car ce sont deux classes sans lien d'héritage. Le test serait toujours faux, donc il est refusé. Via une variable `Object`, le test est possible, car un `Object` **pourrait** être un `Integer`.
