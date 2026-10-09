# Drill de rappel 5 — Kata : la calculatrice de chaînes (TDD chronométré)

> Première fois ? Lis d'abord le mode d'emploi [`ch16_testing/PARCOURS.md`](../../PARCOURS.md). À faire après le projet p04.

**Chrono cible :** 25 min, puis 15 min.

**C'est un kata :** un petit exercice que l'on **refait** souvent, comme une gamme au piano. Le but n'est pas de trouver la solution (tu la connaîtras vite), mais de rendre le **geste du TDD** automatique : un test rouge, le code le plus simple, un nettoyage, et on recommence.

**Règles :**
- Tout se fait de mémoire, **imports compris**.
- Dans le paquet `ch16_testing.drills.r05_kata_calculator` : **`StringCalculator`** (avec `public static int add(String numbers)`) et **`StringCalculatorTest`**.
- **Sur l'honneur :** pas une ligne de `StringCalculator` sans un test rouge qui la réclame. Prends les règles **dans l'ordre**, une à la fois.

**Les notions de ce drill ont été apprises dans :** projet 4 (le TDD, en entier), projet 3 (les tests paramétrés), projet 1 (les exceptions). `Pattern.quote` est **nouveau** : la carte mémoire l'explique.

<details><summary><b>Comment faire ce drill, concrètement</b> (à lire la 1re fois)</summary>

1. **Note l'heure** de départ.
2. **Crée** `StringCalculatorTest` d'abord (clic droit sur le dossier `r05_kata_calculator` → **New** → **Java Class**). Écris le test de la règle 1 : il ne compile pas, c'est ton premier rouge.
3. **Alt+Entrée** sur `StringCalculator` → *Create class*, puis sur `add` → *Create method*. Le code le plus simple. Vert.
4. **Règle suivante**, et ainsi de suite. Relance tous les tests à chaque fois (**Maj+F10**).
5. **Bloqué plus de 5 minutes sur une règle ?** Note `// règle 6 : ✗` et passe à la suivante.
6. **Lance `Check.java`** : comme dans les projets, il lance tes tests sur ton code, sur le code de référence, et sur 9 mutants.
7. **Ensuite seulement**, la carte mémoire.
8. **Note** date, temps et ✗ dans [`drills/README.md`](../README.md).

</details>

## Les règles, dans l'ordre

1. `add("")` vaut **0**.
2. Un nombre seul : `add("1")` vaut 1.
3. Deux nombres séparés par une virgule : `add("1,2")` vaut 3.
4. **Autant** de nombres qu'on veut : `add("1,2,3,4")` vaut 10.
5. Le **retour à la ligne** est aussi un séparateur : `add("1\n2,3")` vaut 6.
6. Un séparateur **personnalisé** : si la chaîne commence par `//`, le caractère suivant est le séparateur, puis vient un retour à la ligne, puis les nombres : `add("//;\n1;2")` vaut 3. Le retour à la ligne reste un séparateur : `add("//;\n1;2\n3")` vaut 6.
7. Le séparateur personnalisé peut être un caractère **spécial** des expressions régulières : `add("//.\n1.2")` vaut 3, `add("//|\n3|4")` vaut 7.
8. Un nombre **négatif** lance `IllegalArgumentException("negatifs interdits : " + liste)`, où la liste contient **tous** les négatifs, séparés par `", "` : `add("1,-1,2,-3")` → `negatifs interdits : -1, -3`.
9. Les nombres **plus grands que 1000** sont ignorés : `add("2,1001")` vaut 2, mais `add("2,1000")` vaut 1002.
10. `add(null)` lance `NullPointerException("chaine absente")`.

**Dans tes tests :** au moins **10** tests (un `@ParameterizedTest` compte chacun de ses cas), au moins un `@ParameterizedTest`, et des `assertThrows` qui vérifient le **message**.

## Ce que `Check` affiche quand tout est juste

```
=== Verification des tests de ch16_testing.drills.r05_kata_calculator ===
[PASS] tes tests sur TON code : 12 tests, 12 reussis
[PASS] tes tests sur le code de REFERENCE : 12 tests, 12 reussis
[PASS] les tests de REFERENCE sur TON code : 12 tests, 12 reussis
   mutant 1 : tue (par …)
   …
[PASS] mutants : 9/9 tues
--- API de ton code ---
[PASS] API : tous les elements vises sont utilises
--- API de tes tests ---
[PASS] API : tous les elements vises sont utilises

*** PROJET REUSSI : tes tests passent, attrapent tous les mutants, et ton code est juste. ***
```

(Le nombre de tests est le tien.)

<details><summary>Ouvrir la carte</summary>

**Le rythme du TDD :** 🔴 un test (le plus petit pas possible) → 🟢 le code le plus simple → 🔵 nettoyer (code **et** tests), tout restant vert.

**Les pièges du kata :**
- `split` prend une **expression régulière** : `"[,\n]"` coupe sur la virgule **ou** le retour à la ligne ;
- `"."`, `"|"`, `"*"`, `"+"`, `"?"`… sont **spéciaux** dans une expression régulière ; `Pattern.quote(s)` (import `java.util.regex.Pattern`) rend `s` littéral : `Pattern.quote(".") + "|\n"` coupe sur un point **ou** un retour à la ligne ;
- `"//;\n1;2"` : le séparateur est le caractère d'indice 2 (`substring(2, 3)`), les nombres commencent à l'indice 4 (`substring(4)`) ;
- dans un `@CsvSource`, un `\n` écrit dans la chaîne Java devient un **vrai** retour à la ligne, et il **coupe** la ligne CSV : `"1\n2,3|6"` ne donne qu'une valeur, `1` (vérifié : `No ParameterResolver registered for parameter [int arg1]`). Teste les retours à la ligne dans des `@Test` ordinaires. Pour les virgules dans les valeurs, choisis un autre séparateur de colonnes : `delimiter = '|'` ;
- les négatifs se **collectent** dans une liste pendant la boucle, et l'exception part **après** : `Collectors.joining(", ")` (chapitre 10).

**Les mutants** : la règle 1 qui rend −1 ; le retour à la ligne oublié ; `Pattern.quote` oublié ; le retour à la ligne oublié avec un séparateur personnalisé ; 1000 ignoré ; −1 accepté ; un seul négatif listé ; `","` au lieu de `", "` ; une somme qui garde seulement le dernier nombre.

</details>
