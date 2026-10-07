# Projet 2 — Corrigé étape par étape

> **Quand le lire :** ouvre **seulement la section de l'étape** que tu viens de terminer, pour comparer ton code et tes réponses. Le programme complet est dans ce dossier : `RuleParser`, `Rule`, `Fix` et `RulesApp`.
>
> Les messages ci-dessous ont été obtenus en direct avec **JDK 17** (`javac` 17.0.18).

---

## Étape 1 — Compiler les règles

**Le code :** [`RuleParser.java`](RuleParser.java), [`Rule.java`](Rule.java) et le tableau du `main` de [`RulesApp.java`](RulesApp.java).

**Compiler une règle = assembler des prédicats :** `len>=8 & upper` devient `atom("len>=8").and(atom("upper"))`. Aucun mot de passe n'est testé à ce moment. On construit une **fonction**, qui testera plus tard.

**Les priorités :** `!` lie plus fort que `&`, qui lie plus fort que `|`. `starts:adm & len>=6 | ends:!!` se lit donc `(starts:adm & len>=6) | ends:!!`. C'est la même descente récursive qu'au chapitre 6 (projet 6).

**`and` et `or` court-circuitent**, comme `&&` et `||` : `p.and(q)` ne teste pas `q` si `p` est faux.

**Question — pourquoi `Character::isDigit` convient à `CharTest` ?** Une référence de méthode convient à **toute** interface fonctionnelle dont la méthode abstraite a une signature **compatible**. `CharTest.test(char)` rend un `boolean`, et `Character.isDigit(char)` prend un `char` et rend un `boolean`. Le nom de l'interface n'a aucune importance : seule la **forme** compte.

**`compile(…)::test` dans un `Rule[]` :** le `Predicate<String>` compilé n'est pas un `Rule`. Mais la référence de méthode `pred::test` (sur **cet objet-là**) a la bonne forme pour la méthode abstraite de `Rule`. Java crée donc un `Rule` qui délègue au prédicat.

**Question — pourquoi `new Predicate<String>[4]` ne compile pas ?**

```
error: generic array creation
```

À l'exécution, les types génériques sont **effacés** : un tableau ne pourrait pas vérifier qu'on n'y range que des `Predicate<String>`. Java interdit donc de créer un tableau d'un type générique paramétré. D'où l'interface `Rule`, **non générique**, qui permet `new Rule[4]`. Les collections du chapitre 9 évitent ce problème.

---

## Étape 2 — `BiPredicate` et utilitaires

**Le code :** le bloc `containsUser` et la ligne `utilitaires`.

**Fixer un argument :** `containsUser` prend **deux** arguments. `notUser = pwd -> !containsUser.test(pwd, user)` en fait un `Predicate` à **un** argument, en capturant `user`. C'est l'idée de l'**application partielle**.

**`Alice2026Pw` est exclu** de la liste « forts et sans alice », car il contient `alice` sans tenir compte de la casse.

**Les utilitaires :**
- `Predicate.not(String::isEmpty)` donne `false` pour `""` et `true` pour `"x"`. C'est une méthode `static` (Java 11+), pratique avec une référence de méthode.
- `Predicate.isEqual("secret")` est un prédicat qui teste `equals("secret")`. Sa `negate()` donne `false` sur `"secret"`.
- `containsUser.negate()` existe aussi sur `BiPredicate`. `"ALICE!"` contient `alice`, donc la négation donne `false`.

---

## Étape 3 — Les correctifs

**Le code :** le tableau `fixes` et la boucle de correction.

**Un record qui contient des lambdas :** `Fix` regroupe un **nom**, un **test** et une **réparation**. Le comportement devient une **donnée** : ajouter un correctif ne demande qu'une ligne dans le tableau.

**Le premier exemple, `motdepasse` :**
1. il n'a pas de chiffre, d'où `+chiffre` : `motdepasse7` ;
2. il n'a pas de majuscule, d'où `+majuscule` : `Motdepasse7`.

La règle forte passe : `true`.

**`OK`** : au fil des tours, on ajoute un chiffre, une minuscule, puis on complète jusqu'à 8 caractères. Au bout de 5 tours au plus, la boucle s'arrête : la règle passe, ou le nombre de tours est atteint.

**Expériences :**

| Expérience | Erreur de `javac` (vérifiée) |
|---|---|
| `n++` après la lambda `len>=` | `local variables referenced from a lambda expression must be final or effectively final`. Même **après** la lambda, toute modification rend `n` non effectively final |
| `Predicate<String> p = s -> s.length();` | `incompatible types: bad return type in lambda expression` `int cannot be converted to boolean` |
