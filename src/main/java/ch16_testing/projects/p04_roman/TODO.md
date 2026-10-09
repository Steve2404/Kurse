# Projet 4 — Les chiffres romains, test d'abord (TDD)

> Première fois ? Lis d'abord le mode d'emploi [`ch16_testing/PARCOURS.md`](../../PARCOURS.md).
>
> **Bloqué sur une étape ?** [`INDICES.md`](INDICES.md) donne deux indices repliés par étape, sans code. **Étape finie ?** [`solution/CORRIGE.md`](solution/CORRIGE.md) donne, étape par étape, le code de l'étape, les **réponses aux questions** et le résultat exact des **expériences**. N'ouvre que la section de l'étape que tu viens de faire.

**Notions visées (chapitre 16) :**
- **le TDD** (*Test-Driven Development*, développement piloté par les tests) :
  - 🔴 **rouge** : écrire **un** petit test qui échoue ;
  - 🟢 **vert** : écrire le code **le plus simple** qui le fait passer ;
  - 🔵 **refactoring** : nettoyer le code (et les tests) **sans** changer ce qu'il fait, les tests restant verts ;
- **faire émerger** un algorithme cas par cas, au lieu de tout concevoir d'avance ;
- **refuser les entrées mal formées** : une seule écriture correcte par nombre ;
- **le test aller-retour** (une propriété sur **toutes** les entrées) et `assertTimeout` ;
- **les mutants équivalents** : un « bug » qu'aucun test ne peut voir, parce qu'il ne change rien.

Côté algorithmes : l'algorithme **glouton** (le plus grand symbole qui rentre), la lecture avec soustraction (IV = 5 − 1).

**Ce que TU crées :** dans `ch16_testing.projects.p04_roman` :
- **`RomanNumerals`** (ses méthodes sont imposées) ;
- **`RomanNumeralsTest`**, écrit **avant** le code, cycle par cycle.

**Règle du crescendo :** chapitres 1 à 15, plus JUnit. Pas de `double` ni de `float` dans le code ; pas de `System.out` ni de `Thread.sleep` dans tes tests.

**La règle du jeu, sur l'honneur :** dans ce projet, tu n'écris **jamais** une ligne de `RomanNumerals` sans un test rouge qui la réclame. `Check` ne peut pas le vérifier : il ne voit que le résultat. Mais c'est la seule façon d'apprendre le TDD. Le corrigé montre chaque cycle.

Chaque étape commence par une **📖 leçon**, avec un exemple sur un autre sujet : les années bissextiles.

> **🧰 Tes outils pour ce projet**
>
> - **Le rythme :** un test → **Ctrl+Maj+F10** (rouge) → le code → **Maj+F10** (relance : vert) → nettoyer → **Maj+F10** (toujours vert).
> - **Créer une méthode qui n'existe pas encore :** dans le test, écris l'appel `RomanNumerals.toRoman(1)` ; le nom est rouge ; **Alt+Entrée** → *Create method 'toRoman'*. IntelliJ écrit la signature pour toi.
> - **Un « rouge » qui ne compile pas compte :** au cycle 1, le test ne compile même pas (la classe n'existe pas). C'est un rouge.

---

## Tableau de bord

### ☐ Étape 1 — Le premier cycle, puis le deuxième

**📖 La leçon : le cycle rouge, vert, refactoring.** Pour coder `estBissextile(int annee)` en TDD :

```
🔴 test : estBissextile(2023) est faux        → ne compile pas
🟢 code : return false;                        → vert (oui, c'est de la triche, c'est VOULU)
🔴 test : estBissextile(2024) est vrai         → rouge
🟢 code : return annee % 4 == 0;               → vert
🔴 test : estBissextile(1900) est faux         → rouge
🟢 code : return annee % 4 == 0 && annee % 100 != 0;
🔴 test : estBissextile(2000) est vrai         → rouge
🟢 code : return annee % 4 == 0 && (annee % 100 != 0 || annee % 400 == 0);
🔵 refactoring : ranger les 4 tests en un seul test paramétré ; tout reste vert
```

Trois règles :
1. **Jamais de code sans un test rouge** qui le réclame.
2. **Le code le plus simple** qui passe, même s'il paraît bête (`return false;`). C'est le **test suivant** qui force la vraie solution.
3. **Le refactoring** se fait **au vert**, sans ajouter de comportement.

Pourquoi ? Chaque ligne de code est **prouvée nécessaire** par un test, donc testée. Et tu avances par toutes petites marches : quand ça casse, tu sais que c'est la dernière ligne.

**👉 À toi :** `public final class RomanNumerals`, avec **`public static String toRoman(int n)`**.
- **Cycle 1 :** 🔴 le test `toRoman(1)` vaut `"I"` ; 🟢 le code le plus simple.
- **Cycle 2 :** 🔴 `toRoman(2)` vaut `"II"` ; 🟢 le code le plus simple **qui garde le cycle 1 vert**.
- **Cycle 3 :** 🔴 `toRoman(3)` vaut `"III"` ; 🟢 ; 🔵 une boucle remplace les `if`, si tu en avais.

**❓ Question :** après le cycle 1, ton code est `return "I";`. Pourquoi ne pas écrire tout de suite l'algorithme complet ?

### ☐ Étape 2 — Le 5 et le 4 : la table apparaît

**📖 La leçon : laisser émerger la structure.** Quand le même genre de `if` revient pour la troisième fois, c'est le moment d'un **refactoring** : remplacer les cas par une **donnée** (un tableau) et une boucle générique. Pour les bissextiles, on n'en arrive pas là ; pour les chiffres romains, si.

**👉 À toi :**
- **Cycle 4 :** 🔴 `toRoman(5)` vaut `"V"` ; 🟢.
- **Cycle 5 :** 🔴 `toRoman(4)` vaut `"IV"` ; 🟢.
- **Cycle 6 :** 🔴 `toRoman(9)` vaut `"IX"`, puis `toRoman(10)` vaut `"X"`, puis `toRoman(14)` vaut `"XIV"` (un cycle chacun).
- 🔵 **Refactoring :** deux tableaux parallèles, valeurs et symboles, **du plus grand au plus petit**, et l'algorithme glouton : pour chaque valeur, **tant qu'**elle rentre dans ce qui reste, ajoute son symbole et retire la valeur. Mets `4` et `9` **dans** la table (avec `"IV"` et `"IX"`) : plus aucun cas particulier. Utilise un `StringBuilder` (chapitre 4).
- 🔵 **Refactoring des tests :** range tes cas dans **un** test `@CsvSource` (projet 3).

**❓ Question :** pourquoi mettre `"IV"` et `"IX"` dans la table plutôt que de traiter la soustraction à part ?

### ☐ Étape 3 — Jusqu'à 3999, et pas plus loin

**👉 À toi :**
- **Cycles suivants :** 🔴 un cas à la fois : 40 (`XL`), 90 (`XC`), 400 (`CD`), 900 (`CM`), 1994 (`MCMXCIV`), 2024 (`MMXXIV`), 3999 (`MMMCMXCIX`). Pour chacun, complète **seulement** la table. Le code ne change plus.
- **Les limites :** 🔴 0, −1 et 4000 lancent `IllegalArgumentException("hors limites : " + n)` (un `@ValueSource(ints = …)`) ; 🟢.

**❓ Question :** à partir de quel cycle le **code** (pas la table) n'a-t-il plus changé ? Qu'est-ce que cela dit de l'algorithme ?

### ☐ Étape 4 — Dans l'autre sens : `fromRoman`

**📖 La leçon : un symbole qui se soustrait.** En lisant de gauche à droite, un symbole **plus petit que son voisin de droite** se **soustrait** : dans `XIV`, le `I` est devant `V`, donc il compte −1. Total : 10 − 1 + 5 = 14.

**👉 À toi :** **`public static int fromRoman(String s)`**, toujours en TDD :
- 🔴 `fromRoman("I")` vaut 1 ; 🟢 ;
- 🔴 `fromRoman("III")` vaut 3 ; 🟢 ;
- 🔴 `fromRoman("IV")` vaut 4 ; 🟢 : pour chaque caractère, compare sa valeur à celle du **suivant** (0 s'il n'y en a pas) ;
- 🔵 ajoute la vérification dans ton test `@CsvSource` : chaque ligne vérifie **les deux sens**.
- Un `Objects.requireNonNull(s, "chiffre absent")` en premier (🔴 d'abord : un test avec `null`).

### ☐ Étape 5 — Refuser ce qui est mal écrit

**📖 La leçon : une seule écriture correcte.** `"IIII"` donne 4 si l'on additionne, mais s'écrit `"IV"`. `"IC"` donne 99, mais s'écrit `"XCIX"`. Pour refuser toutes ces écritures, inutile d'écrire une règle par piège : il n'existe qu'**une** écriture correcte de chaque nombre, et **`toRoman` sait la fabriquer**. Après le calcul, on vérifie que `toRoman(total)` redonne **exactement** la chaîne reçue.

**👉 À toi :**
- 🔴 un test `@ValueSource(strings = …)` : `"IIII"`, `"IC"`, `"VV"`, `"IIV"`, `"MMMM"`, `""`, `"iv"`, `"XZ"`, `"MCMC"` lancent tous `IllegalArgumentException("chiffre romain invalide : " + s)` ;
- 🟢 :
  - un caractère inconnu (`Z`, `i`…) lance cette exception **tout de suite** ;
  - après le calcul : un total hors de 1 à 3999, ou `toRoman(total)` différent de `s`, la lance aussi.

**❓ Questions :**
- Que vaudrait le total de `""` sans contrôle ? Et celui de `"MMMM"` ? Pourquoi faut-il contrôler les limites **avant** d'appeler `toRoman(total)` ?
- Quelle est la valeur de `"MCMC"` si l'on additionne ? Pourquoi est-ce mal écrit ?

### ☐ Étape 6 — Le filet de sécurité : l'aller-retour

**📖 La leçon : tester toutes les entrées.** Quand il n'y a « que » 3999 entrées possibles, on peut **toutes** les essayer. La propriété : pour tout `n` de 1 à 3999, `fromRoman(toRoman(n))` redonne `n`. `assertTimeout(Duration.ofSeconds(1), () -> …)` vérifie en plus que le tout prend **moins d'une seconde**.

```java
@Test
void toutesLesAnneesDuSiecle() {
    assertTimeout(Duration.ofSeconds(1), () ->
            IntStream.rangeClosed(1901, 2000).forEach(a -> assertEquals(a % 4 == 0, Calendrier.estBissextile(a))));
}
```

**👉 À toi :** le test aller-retour, avec `IntStream.rangeClosed(1, 3999)` et `assertTimeout`.

**🧪 Expérience :** remplace temporairement `Duration.ofSeconds(1)` par `Duration.ofMillis(1)`. Recopie le message.

**❓ Question :** ce test aller-retour suffirait-il **seul** ? Imagine un `toRoman` qui rendrait `"A"`, `"B"`, `"C"`… et un `fromRoman` qui ferait l'inverse.

### ☐ Étape 7 — Les mutants, et le mutant qu'on ne peut pas tuer

**📖 La leçon : le mutant équivalent.** Un mutant change le code. Parfois, le changement ne change **aucun résultat** : aucun test au monde ne peut le voir. C'est un **mutant équivalent**. Il ne révèle pas un trou dans tes tests, mais un code **en trop** (ou une vérification faite deux fois).

**👉 À toi :** lance `Check` et tue les **11** mutants.

**🧪 Expérience :** dans **ton** `fromRoman`, passe chaque caractère en majuscule avant de le lire (`Character.toUpperCase(s.charAt(i))`). Lance tes tests **et** `Check`. Est-ce que `"iv"` est maintenant accepté ? Pourquoi ? Remets le code.

### Expériences (hors sortie attendue)

1. Enlève `"CM"` et `900` de ta table. Lance tes tests : lesquels échouent, et que rend `toRoman(900)` ?
2. Remplace le `while` de l'algorithme glouton par un `if`. Quel est le premier cas qui casse ?

---

## Checklist (vérifiée par `Check`)

- **Ton code :** `final class RomanNumerals`, `static String toRoman(int n)`, `static int fromRoman(String s)`, `StringBuilder`, `Objects.requireNonNull(` ; ni `double` ni `float`.
- **Tes tests :** au moins **25** tests, `@ParameterizedTest`, `@CsvSource(`, `@ValueSource(ints`, `@ValueSource(strings`, `@Test`, `assertTimeout(`, `Duration.ofSeconds(`, `IntStream.rangeClosed(1, 3999)`, `NullPointerException.class` ; ni `System.out` ni `Thread.sleep`.
- **Les 11 mutants** sont tués.

---

## Ce que `Check` affiche quand tout est juste

```
=== Verification des tests de ch16_testing.projects.p04_roman ===
[PASS] tes tests sur TON code : 29 tests, 29 reussis
[PASS] tes tests sur le code de REFERENCE : 29 tests, 29 reussis
[PASS] les tests de REFERENCE sur TON code : 29 tests, 29 reussis
   mutant 1 : tue (par convertsBothWays [900 = CM])
   …
   mutant 11 : tue (par malformedNumeralsAreRejected ["MCMC" est refuse])
[PASS] mutants : 11/11 tues
--- API de ton code ---
[PASS] API : tous les elements vises sont utilises
--- API de tes tests ---
[PASS] API : tous les elements vises sont utilises

*** PROJET REUSSI : tes tests passent, attrapent tous les mutants, et ton code est juste. ***
```
