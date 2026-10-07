# Projet 1 — Corrigé étape par étape

> **Quand le lire :** ouvre **seulement la section de l'étape** que tu viens de terminer, pour comparer ton code et tes réponses. Le programme complet est dans ce dossier : `Shape`, `Circle`, `Rectangle`, `Square`, `Triangle`, `Polygon` et `ShapesApp`.
>
> Les messages ci-dessous ont été obtenus en direct avec **JDK 17** (`javac` 17.0.18), sur des classes de test réduites.

---

## Étape 1 — La hiérarchie

**Le code :** [`Shape.java`](Shape.java), [`Circle.java`](Circle.java), [`Rectangle.java`](Rectangle.java), [`Square.java`](Square.java) et [`Triangle.java`](Triangle.java).

**Les idées clés :**
- **`abstract`** : `Shape` décrit ce que toute forme **sait faire** (`area`, `perimeter`), sans savoir **comment**. Une classe concrète **doit** redéfinir toutes les méthodes abstraites héritées.
- **`describe()` est `final`** : le **format** est imposé à toutes les formes. Les sous-classes ne personnalisent que le **point d'extension** `extra()`. C'est le patron « méthode modèle » (*template method*).
- **Le constructeur `protected`** : on ne crée jamais une `Shape` directement. Seules les sous-classes l'appellent, via `super(name)`.
- **`Square` n'a pas de `area()`** : il hérite de celui de `Rectangle` (largeur × hauteur), et ça marche puisque ses deux côtés sont égaux.
- **`super.extra()`** dans `Square` appelle la version du **parent** (` 3.0x3.0`), puis ajoute ` (cote 3.0)`.

**Expériences :**

| Expérience | Erreur de `javac` |
|---|---|
| `new Shape("x")` | `Shape is abstract; cannot be instantiated` |
| redéfinir `describe()` dans `Circle` | `describe() in Circle cannot override describe() in Shape` (la ligne suivante précise : `overridden method is final`) |
| `this(1)` en 2e ligne de `Circle()` | `call to this must be first statement in constructor` |
| retirer `super("cercle")` | `constructor Shape in class Shape cannot be applied to given types;` `required: String` `found: no arguments` |

**La dernière, expliquée :** sans appel explicite, `javac` insère `super();` (sans argument) au début de chaque constructeur. Or `Shape` n'a **pas** de constructeur sans argument : dès qu'une classe déclare un constructeur, Java ne fournit plus celui par défaut.

---

## Étape 2 — Trier des objets par leur méthode

**Le code :** la deuxième partie du `main` de [`ShapesApp.java`](ShapesApp.java).

**Le polymorphisme à l'œuvre :** le tri appelle `shapes[j].area()` sans savoir de quelle forme il s'agit. La JVM exécute à chaque fois le `area()` de l'**objet réel** : π r² pour un cercle, Héron pour un triangle. Ajouter une nouvelle forme ne demande **aucune** modification du tri.

**L'ordre obtenu :**

| Forme | Aire |
|---|---|
| cercle(2) | 12.57 |
| rectangle | 12.0 |
| carré | 9.0 |
| triangle | 6.0 |
| cercle(1) | 3.14 |
| triangle invalide | 0 |

Le plus grand périmètre est celui du rectangle (14), devant le cercle(2) (12.57).

---

## Étape 3 — Le type de la référence ou de l'objet

**Le code :**

```java
Rectangle r = new Square(5);
System.out.println("Rectangle r = new Square(5) : " + r + " | carre ? " + (r instanceof Square) + " | " + r.getClass().getSimpleName());
```

**Question — pourquoi `extra()` est-il celui de `Square` ?** Il y a deux types en jeu :
- le **type de la référence** (`Rectangle`) décide de ce que `javac` **autorise** : seules les méthodes connues de `Rectangle` peuvent être appelées sur `r` ;
- le **type de l'objet** (`Square`) décide de **quelle version** s'exécute. Pour une méthode d'instance redéfinie, c'est toujours celle de l'objet réel, au moment de l'exécution (**liaison dynamique**).

`describe()` (dans `Shape`) appelle `extra()`, et l'objet est un `Square`, donc c'est `Square.extra()`. `instanceof` et `getClass()` regardent eux aussi l'**objet**, pas la variable.

---

## Étape 4 — L'enveloppe convexe et le point dans le polygone

**Le code :** [`Polygon.java`](Polygon.java) et la fin du `main`.

**La copie défensive dans le constructeur :** `points[i].clone()` copie chaque point. Sans ça, l'appelant pourrait modifier `Data.POINTS` après coup et déformer un `Polygon` censé être fixe (`private final`). `final` sur un tableau empêche seulement de le **réaffecter**, pas de modifier ses cases.

**`super("polygone(" + points.length + ")")`** : l'argument de `super(…)` peut être une expression, à condition de ne pas utiliser `this` (l'objet n'est pas encore construit).

**L'algorithme d'Andrew :**
- Trié par x, on parcourt les points de gauche à droite. Le produit vectoriel `cross(o, a, b)` est > 0 si o → a → b tourne **à gauche**.
- Pour la chaîne inférieure, on veut toujours tourner à gauche : un virage à droite ou un alignement (`<= 0`) signifie que le dernier sommet est **à l'intérieur**, et on le retire.
- On fait de même à l'envers pour la chaîne supérieure.

Complexité : O(n log n) pour le tri, plus O(n) pour la construction (ici O(n²), car le tri est par insertion).

**Le lancer de rayon :** depuis le point, une demi-droite vers la droite. Elle traverse les bords un nombre **impair** de fois si le point est **dedans**. La condition `(p[1] > y) != (q[1] > y)` sélectionne les arêtes qui croisent l'horizontale y. Le calcul suivant donne l'abscisse du croisement.

**`formes creees : 8` :** les 6 formes du tableau, le `Square(5)` de l'étape 3, puis le polygone. Le compteur `static` est incrémenté **dans le constructeur de `Shape`**, par lequel passe **toute** création, quelle que soit la sous-classe.
