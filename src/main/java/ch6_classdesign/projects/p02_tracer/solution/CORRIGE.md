# Projet 2 — Corrigé étape par étape

> **Quand le lire :** ouvre **seulement la section de l'étape** que tu viens de terminer, pour comparer ton code et tes réponses. Le programme complet est dans ce dossier : `Tracer`, `Vehicle`, `Car`, `ElectricCar` et `TracerApp`.
>
> Le journal et les messages ci-dessous ont été obtenus en direct avec **JDK 17** (`javac` / `java` 17.0.18), sur une copie de la solution.

---

## Étape 1 — Le journal et les classes

**Le code :** les cinq classes de la solution. Les points clés :

```java
public static int log(String what, int v) {   // Tracer
    log(what + " = " + v);
    return v;
}

public Car(String name) {                     // Car
    this(name, 5);
    Tracer.log("[ctor] Car(String)");
}
```

**`log(String, int)` rend v :** c'est ce qui permet `protected int wheels = Tracer.log("…", 4);`. L'initialisation du champ **se voit** dans le journal, au moment exact où elle a lieu.

**`this(name, 5)` dans `Car(String)` :** un constructeur qui commence par `this(…)` n'appelle **pas** `super(…)` lui-même. C'est l'autre constructeur (`Car(String, int)`) qui le fait. Un seul `super` par construction.

---

## Étape 2 — Ce qui charge une classe, ou pas

**Le code :**

```java
Tracer.log("main : KIND = " + ElectricCar.KIND + " (aucune classe chargee)");
Tracer.log("main : Car.count = " + Car.count + " (Vehicle charge, pas Car)");
```

**Question — pourquoi lire `KIND` ne déclenche rien ?** `KIND` est une **constante de compilation** : `static final`, de type `String`, initialisée par un littéral. `javac` remplace `ElectricCar.KIND` par `"EV"` **directement dans le code de `TracerApp`**. À l'exécution, `ElectricCar` n'est même pas consultée.

**Et si `KIND` n'était pas `final` ?** Alors lire `ElectricCar.KIND` est un vrai accès au champ, qui **initialise** `ElectricCar`. Initialiser une classe initialise d'abord ses parents. Vérifié : dès la ligne 02, on voit `[static] Vehicle.count`, `Vehicle bloc`, `Car bloc`, `ElectricCar.built` et `ElectricCar bloc`, avant le texte du `main`.

**Question — quelle classe `Car.count` charge-t-il ?** **`Vehicle` seulement.** `count` est **déclaré** dans `Vehicle`. Accéder à un champ static initialise la classe qui le **déclare**, pas celle par laquelle on l'écrit. D'où les lignes 03 et 04 (`Vehicle`), et pas de `[static] Car bloc` avant la ligne 07.

---

## Étape 3 — Construire, trois fois

**Le code :** la suite du `main` de [`TracerApp.java`](TracerApp.java).

**Le journal de `new ElectricCar("Zoe", 300)`, expliqué (vérifié) :**

| Ligne | Ce qui se passe |
|---|---|
| 07 à 09 | 1er `new ElectricCar` : la classe n'est pas encore initialisée. Les parents d'abord : `Vehicle` l'est déjà, donc `Car`, puis `ElectricCar` (ses static **dans l'ordre du fichier**) |
| 10, 11 | `ElectricCar(…)` appelle `super(name, 4)`, qui appelle `super(name)`, qui appelle `Object()`. Puis les **initialiseurs de `Vehicle`** : `wheels`, puis le bloc |
| 12, 13 | le **corps** du constructeur de `Vehicle` |
| 14, 15 | de retour dans `Car` : ses initialiseurs (le bloc), puis le corps de `Car(String,int)` |
| 16, 17 | de retour dans `ElectricCar` : ses initialiseurs (`battery = 50`), puis le corps (`battery = 300`) |

**Question — pourquoi `battery` vaut 0 à la ligne 13 ?** `label()` est **redéfini** dans `ElectricCar`. Même appelé depuis le constructeur de `Vehicle`, c'est donc la version de l'**objet réel** qui s'exécute (liaison dynamique). Mais à ce moment, seul `Vehicle` a été initialisé : les initialiseurs d'`ElectricCar` n'ont **pas encore** tourné. `battery` a donc encore sa **valeur par défaut**, 0. Ni 50 (son initialiseur, ligne 16) ni 300 (le constructeur, ligne 17).

**À retenir :** n'appelle jamais une méthode **redéfinissable** depuis un constructeur. Elle verrait un objet à moitié construit. C'est pour ça que `describe()` était `final` au projet 1.

**La 2e construction** ne réaffiche aucune ligne `[static]` : une classe n'est initialisée **qu'une fois**.

**Expérience — `label()` dans le constructeur de `Car` :** placé à la fin de `Car(String, int)`, vérifié :
- pour `Car("Clio")`, on voit `voiture Clio 5 places` : l'objet est un `Car`, et `seats` est déjà affecté ;
- pour les deux `ElectricCar`, on voit encore `electrique … batterie 0` : `label()` est toujours celui d'`ElectricCar`, et `battery` n'est toujours pas initialisé (le corps de `Car` passe **avant** les initialiseurs d'`ElectricCar`).

**Expérience — `id = ++count;` dans un seul chemin :**

```
error: variable id might not have been initialized
```

`id` est `final` **sans** valeur sur sa ligne. `javac` vérifie que **chaque** chemin de **chaque** constructeur l'affecte exactement une fois.
