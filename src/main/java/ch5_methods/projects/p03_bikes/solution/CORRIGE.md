# Projet 3 — Corrigé étape par étape

> **Quand le lire :** ouvre **seulement la section de l'étape** que tu viens de terminer, pour comparer ton code et tes réponses. Le programme complet est dans [`Network.java`](Network.java), [`Station.java`](Station.java) et [`BikeApp.java`](BikeApp.java).
>
> Les messages et les exceptions ci-dessous ont été obtenus en direct avec **JDK 17** (`javac` / `java` 17.0.18).

---

## Étape 1 — Tracer l'ordre de chargement

**Le code :** toute la classe [`Network.java`](Network.java), et le bloc `static { System.out.println("[charge] BikeApp"); }` de `BikeApp`.

**L'ordre, expliqué :**
1. Pour exécuter `main`, la JVM doit d'abord **initialiser** `BikeApp`. Son bloc static s'exécute donc **avant** `main`, d'où `[charge] BikeApp` en premier.
2. `Network` n'est initialisée qu'à son **premier usage** : l'appel `km(…)` dans `main`, d'où `main commence` avant ses traces.
3. Dans `Network`, les initialiseurs static (champs et blocs) s'exécutent **dans l'ordre du fichier** : `NAMES` (avec la trace de `load()`), puis le bloc 1, puis `DIST`, puis le bloc 2.

**Expérience — le bloc 1 avant la déclaration de `NAMES` :**

```
error: illegal forward reference
```

Un initialiseur static ne peut pas **lire** par son nom simple un champ static déclaré **plus bas**. C'est la même règle que pour les champs d'instance au chapitre 1.

**Expérience — supprimer l'affectation de `SIZE` :**

```
error: variable SIZE not initialized in the default constructor
```

Un champ `final` **doit** être affecté exactement une fois. Pour un `static final` sans valeur, cette affectation doit se faire dans un bloc static. Le libellé de `javac` parle du « constructeur par défaut », mais c'est bien l'absence d'affectation qui est en cause.

**Floyd-Warshall en une phrase :** `DIST[i][j]` contient d'abord les routes directes. Pour chaque station k, on se demande si le trajet i → k → j est plus court que le trajet connu. Après le dernier k, `DIST` contient toutes les plus courtes distances. Exemple : Gare → Parc n'a pas de route directe, mais Gare → Centre (2) + Centre → Parc (3) = **5 km**.

**`import static java.lang.Math.min;`** permet d'écrire `min(a, b)` sans préfixe.

---

## Étape 2 — Des objets avec un registre partagé

**Le code :** toute la classe [`Station.java`](Station.java), sauf `nearest` et `describe`.

```java
private static final Station[] ALL = new Station[10];
private static int created;
final int id;
…
{
    id = created++;
    System.out.println("[objet] station #" + id);
}
```

**Question — pourquoi `id` peut-il être `final` sans valeur sur sa ligne ?** Un champ `final` d'instance doit être affecté **exactement une fois**, avant la fin de la construction de l'objet. Un **bloc d'initialisation d'instance** convient, tout comme un constructeur. Une **seconde** affectation serait refusée. Vérifié avec un bloc **et** un constructeur qui affectent tous deux `id` :

```
error: variable id might already have been assigned
```

**Question — une méthode `static` peut-elle lire `name` directement ?** **Non.** Une méthode static n'a pas d'objet courant (pas de `this`) : de quelle station lirait-elle le nom ?

```
error: non-static variable name cannot be referenced from a static context
```

Elle doit passer par une référence (`s.name`), comme le fait `snapshot` avec `ALL[i].name`. L'inverse marche : une méthode d'instance lit librement les membres static, comme `describe` lit `created`.

---

## Étape 3 — Les trajets

**Le code :** la méthode `nearest` de `Station` et la méthode `ride` de `BikeApp`.

**Le premier trajet, Parc → Gare :**
- Le Parc a 0 vélo. La station la plus proche qui a un vélo est le Centre, à 3 km. On marche **3 km**, et le Centre passe de 1 à 0 vélo.
- La Gare a 6 vélos sur 8, donc elle n'est pas pleine : on dépose là.
- À vélo : Centre → Gare, **2 km**.

**`import static …Network.km;`** permet d'écrire `km(a, b)` sans préfixe dans `BikeApp`.

**Pourquoi `ride` est `private` :** c'est un détail de `BikeApp`. Aucune autre classe n'a à l'appeler. Les compteurs `walked` et `ridden` sont aussi `private static` : ils appartiennent à l'application, pas à un objet.

---

## Étape 4 — Le camion et le `static` via `null`

**Le code :** la méthode `rebalance` de `BikeApp`, la méthode `describe` de `Station`, et les dernières lignes du `main`.

**Le premier mouvement du camion :** à l'arrivée, `Port=6/6` est en surplus (6 > 3) et `Parc=1/5` en manque (1 < 2). Port → Parc fait 4 km, la plus courte paire possible. On déplace min(6 − 3, 2 − 1) = **1** vélo.

**Le `static` via `null` :** `nothing.count()` rend 5, sans exception. Pour un membre `static`, Java ne regarde que le **type déclaré** de la variable (`Station`) : l'appel devient `Station.count()`, et la valeur de `nothing` n'est jamais lue. Vérifié : `javac` sans option **ne dit rien**. IntelliJ et `javac -Xlint:static` signalent :

```
warning: [static] static method should be qualified by type name, S, instead of by an expression
```

**Question — et `nothing.describe()`, une méthode d'instance ?** Une **`NullPointerException`** à l'exécution. Une méthode d'instance a besoin de l'objet, et il n'y en a pas :

```
java.lang.NullPointerException: Cannot invoke "S.describe()" because "<local1>" is null
```

(vérifié sur une classe `S` de test.) Ça compile, car le type déclaré a bien une méthode `describe()`.
