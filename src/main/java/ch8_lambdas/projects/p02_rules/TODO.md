# Projet 2 — Le moteur de règles de mots de passe

> Première fois ? Lis d'abord le mode d'emploi [`ch8_lambdas/PARCOURS.md`](../../PARCOURS.md).
>
> **Bloqué sur une étape ?** [`INDICES.md`](INDICES.md) donne deux indices repliés par étape, sans code. **Étape finie ?** [`solution/CORRIGE.md`](solution/CORRIGE.md) donne, étape par étape, le code de l'étape, les **réponses aux questions** et le résultat exact des **expériences**. N'ouvre que la section de l'étape que tu viens de faire.

**Notions visées (chapitre 8) :**
- **`Predicate`** et sa composition : `and`, `or`, `negate`, `Predicate.not`, `Predicate.isEqual` ;
- **`BiPredicate`**, que l'on fixe sur un argument pour obtenir un `Predicate` ;
- **compiler un texte en lambdas** : chaque opérateur devient `and`, `or` ou `negate` ;
- les valeurs extraites sont **capturées** ;
- une interface fonctionnelle à toi, **`CharTest`**, non générique ;
- les références de méthode **`Character::isDigit`** ;
- un **record qui contient des lambdas** (`Fix`) ;
- une interface **`Rule extends Predicate<String>`**, pour pouvoir créer un tableau `Rule[]` ;
- la référence **`compile(…)::test`** qui adapte un `Predicate` en `Rule`.

Côté algorithmes :
- un **analyseur par descente récursive** (priorités `!` > `&` > `|`, parenthèses) ;
- des **correctifs gloutons** appliqués jusqu'à satisfaire la règle.

**Ce qui est donné :** `Data.java` et `Check.java`.

**Ce que TU crées :** dans le paquet `ch8_lambdas.projects.p02_rules` :
- `RuleParser`, `Rule`, `Fix` ;
- **`RulesApp`** (le `main`).

**Règle du crescendo :** chapitres 1 à 8. Pas de collection ni de `Comparator`.

**Tes outils pour ce projet** (pas d'arguments, `Data.java` donné) :

```
javac -d build/ch8-p02 -sourcepath src/main/java src/main/java/ch8_lambdas/projects/p02_rules/RulesApp.java
java "-Duser.language=fr" -cp build/ch8-p02 ch8_lambdas.projects.p02_rules.RulesApp
```

---

## Tableau de bord

### ☐ Étape 1 — Compiler les règles

```
mot de passe   STRONG  LENIENT ADMIN   NOJAVA
motdepasse     -       -       -       oui
MotDePasse1    oui     oui     -       oui
...
```

**📖 La leçon : combiner des `Predicate`.** Un `Predicate` sait se combiner avec un autre, sans écrire de `if` :

```java
Predicate<String> court = s -> s.length() <= 4;
Predicate<String> commenceParK = s -> s.startsWith("k");
court.and(commenceParK).test("kiwi")     // true  : les deux
court.or(commenceParK).test("kaki")      // true  : au moins un
court.negate().test("kiwi")              // false : le contraire
```

**📖 La leçon : une lambda fabriquée par une méthode.** Une méthode peut **rendre** une lambda, qui retient les paramètres de la méthode :

```java
static Predicate<String> plusLongQue(int n) {
    return s -> s.length() > n;          // la lambda retient n
}
plusLongQue(3).test("pomme")             // true
```

**📖 Rappel :** l'analyseur avec une méthode par niveau de priorité (chapitre 6, projet 6, étape 2). Une référence de méthode convient à **n'importe quelle** interface fonctionnelle dont la méthode a les bons types (projet 1, étape 1).

**👉 À toi :**

- **`RuleParser`** :
  - un champ `String[] tokens` (le texte découpé sur `" +"`) et un `int pos` ;
  - `public static Predicate<String> compile(String text)`.
- **Les trois niveaux** :
  - `or()` = `and()`, puis tant qu'il y a `|` : `p = p.or(and())` ;
  - `and()` = `not()`, puis tant qu'il y a `&` : `p = p.and(not())` ;
  - `not()` = `!` suivi de `not().negate()`, ou `(` suivi de `or()` puis `)`, ou un atome.
- **`static Predicate<String> atom(String t)`**, une lambda par atome :
  - `len>=N` et `len<=N` capturent N ;
  - `digit`, `upper` et `lower` utilisent `has(s, Character::isDigit)` (etc.) ;
  - `space` → `s.contains(" ")` ;
  - `starts:X`, `ends:X` et `contains:X` capturent X ;
  - un atome inconnu → `s -> false`.
- **`@FunctionalInterface interface CharTest { boolean test(char c); }`** et `static boolean has(String s, CharTest test)`.
  - **Question :** pourquoi `Character::isDigit` convient-il à `CharTest` ?
- **`@FunctionalInterface public interface Rule extends Predicate<String> {}`**.
- **Dans `main`** :
  - pour chaque règle de `Data.RULES` (`NOM = texte`), garde le nom et `compiled[i] = RuleParser.compile(texte)::test` ;
  - l'en-tête est `mot de passe` sur 15 caractères (`%-15s`), puis chaque nom sur 8 (`%-8s`) ;
  - chaque ligne : le candidat sur 15, puis `oui` ou `-` sur 8 par règle ;
  - les espaces finaux sont retirés.
  - **Question :** pourquoi `new Predicate<String>[4]` ne compile-t-il pas ?

### ☐ Étape 2 — `BiPredicate` et utilitaires

```
forts et sans "alice" : MotDePasse1 Secret2026!! javaRocks7A
utilitaires : false true true false false
```

**📖 La leçon : les utilitaires de `Predicate`.**

```java
Predicate.not(court).test("banane")      // true : écrit autrement que court.negate()
Predicate.isEqual("kiwi").test("kiwi")   // true : un test d'égalité tout fait
BiPredicate<String, Integer> plusLong = (s, n) -> s.length() > n;
plusLong.test("abc", 2)                  // true
```

**👉 À toi :**

- `BiPredicate<String, String> containsUser = (pwd, user) -> …` (sans tenir compte de la casse).
- `Predicate<String> notUser = pwd -> !containsUser.test(pwd, user)`, avec `user = Data.USER`.
- La règle forte est `compile("len>=8 & upper & lower & digit & ! space").and(notUser)` ; liste les candidats qui la passent.
- **Les utilitaires :**
  - `Predicate.not(String::isEmpty)` testé sur `""` puis `"x"` ;
  - `Predicate.isEqual("secret")` testé sur `"secret"`, puis sa négation ;
  - `containsUser.negate().test("ALICE!", "alice")`.

### ☐ Étape 3 — Les correctifs

```
corrige "motdepasse" -> "Motdepasse7" (+chiffre, +majuscule) true
...
```

**📖 La leçon : un `record` qui contient des lambdas.** Les composants d'un `record` peuvent être de n'importe quel type, y compris `Predicate` ou `UnaryOperator`. On appelle ensuite la lambda à travers l'accesseur : `fix.problem().test(texte)`.

**👉 À toi :**

- **`record Fix(String name, Predicate<String> problem, UnaryOperator<String> repair)`**.
- **Les 5 correctifs, dans l'ordre :**

| Nom | Problème | Réparation |
|---|---|---|
| `sans espace` | contient une espace | retirer les espaces |
| `+chiffre` | aucun chiffre | ajouter `7` |
| `+majuscule` | aucune majuscule | mettre la 1re lettre en majuscule |
| `+minuscule` | aucune minuscule | ajouter `x` |
| `allonge` | moins de 8 caractères | compléter avec `#` jusqu'à 8 |

- **L'algorithme :** au plus 5 tours, tant que la règle forte échoue. À chaque tour, applique le **premier** correctif dont le problème est vrai, puis reteste.
- Affiche `corrige "avant" -> "après" (correctifs séparés par , ) true/false`, pour `{"motdepasse", "court1", "Mot De Passe 9", "ab", "OK"}`.
- **Expériences :**
  - dans `atom`, écris `n++` après la lambda `len>=` : quelle erreur ?
  - `Predicate<String> p = s -> s.length();` : quelle erreur ?

---

## Checklist (vérifiée par `Check`)

- `Data.RULES` et `Data.CANDIDATES` ;
- `Predicate<String>`, `.or(`, `.and(`, `.negate()`, `Predicate.not(`, `Predicate.isEqual(` ;
- `BiPredicate<String, String>` ;
- `interface Rule extends Predicate<String>` et `::test` ;
- `Character::isDigit`, `UnaryOperator<String> repair`, `@FunctionalInterface`.

---

## Sortie attendue complète

```
mot de passe   STRONG  LENIENT ADMIN   NOJAVA
motdepasse     -       -       -       oui
MotDePasse1    oui     oui     -       oui
court1         -       oui     -       oui
Mot De Passe 9 -       oui     -       oui
adminX         -       oui     oui     oui
Secret2026!!   oui     oui     oui     oui
javaRocks7A    oui     oui     -       -
Alice2026Pw    oui     oui     -       oui
forts et sans "alice" : MotDePasse1 Secret2026!! javaRocks7A
utilitaires : false true true false false
corrige "motdepasse" -> "Motdepasse7" (+chiffre, +majuscule) true
corrige "court1" -> "Court1##" (+majuscule, allonge) true
corrige "Mot De Passe 9" -> "MotDePasse9" (sans espace) true
corrige "ab" -> "Ab7#####" (+chiffre, +majuscule, allonge) true
corrige "OK" -> "OK7x####" (+chiffre, +minuscule, allonge) true
```
