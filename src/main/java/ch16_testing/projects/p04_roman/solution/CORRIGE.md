# Projet 4 — Corrigé étape par étape

> **Quand le lire :** ouvre **seulement la section de l'étape** que tu viens de terminer. Le code final est dans [`RomanNumerals.java`](RomanNumerals.java), et les tests de référence dans [`RomanNumeralsTest.java`](RomanNumeralsTest.java). Ici, tu trouves aussi le code **à chaque cycle**, pour comparer ton chemin.
>
> Les résultats ci-dessous ont été obtenus en direct avec **JDK 17** (17.0.18) et **JUnit 5.11.4**, sur la solution et sur de petits programmes d'essai.

---

## Étape 1 — Le premier cycle, puis le deuxième

Les trois premiers cycles :

```java
// Cycle 1 — test : assertEquals("I", RomanNumerals.toRoman(1));
public static String toRoman(int n) {
    return "I";
}

// Cycle 2 — test : assertEquals("II", RomanNumerals.toRoman(2));
public static String toRoman(int n) {
    if (n == 2) {
        return "II";
    }
    return "I";
}

// Cycle 3 — test : assertEquals("III", RomanNumerals.toRoman(3));   puis refactoring : plus de if
public static String toRoman(int n) {
    return "I".repeat(n);
}
```

**Question — pourquoi pas tout de suite l'algorithme complet ?** Parce qu'aucun test ne le **réclame** encore : chaque ligne écrite sans test rouge est une ligne **non testée**. Et parce qu'on se trompe moins en faisant de toutes petites marches : si un test casse, c'est forcément la dernière ligne. Enfin, l'algorithme qui **émerge** est souvent plus simple que celui qu'on aurait imaginé d'avance (ici, la table de l'étape 2).

---

## Étape 2 — Le 5 et le 4

```java
// Cycles 4 (5 = V) et 5 (4 = IV) : deux if de plus, et ça devient laid…
// Cycle 6 (9, 10, 14) puis refactoring : la table et le glouton.
private static final int[] VALUES = {10, 9, 5, 4, 1};
private static final String[] SYMBOLS = {"X", "IX", "V", "IV", "I"};

public static String toRoman(int n) {
    StringBuilder sb = new StringBuilder();
    int rest = n;
    for (int i = 0; i < VALUES.length; i++) {
        while (rest >= VALUES[i]) {
            sb.append(SYMBOLS[i]);
            rest -= VALUES[i];
        }
    }
    return sb.toString();
}
```

Les tests, après leur propre refactoring : un seul `@CsvSource({"1, I", "2, II", "3, III", "4, IV", "5, V", "9, IX", "10, X", "14, XIV"})`.

**Question — pourquoi `IV` dans la table ?** Avec `IV` et `IX` dans la table, la soustraction devient un **symbole comme un autre** : le glouton les choisit tout seul quand il le faut (pour 4, la plus grande valeur qui rentre est justement 4). Le code n'a plus **aucun cas particulier** : moins de code, moins de bugs. Traiter la soustraction à part demanderait des `if` pour chaque paire (4, 9, 40, 90, 400, 900).

---

## Étape 3 — Jusqu'à 3999

Les cycles de 40 à 3999 ne changent que les tableaux. La table finale : `{1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1}` et `{"M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"}`. Puis le contrôle `hors limites`.

**Question — quand le code s'arrête-t-il de changer ?** Dès le refactoring de l'étape 2. Ensuite, chaque cycle n'ajoute qu'une **donnée**. Cela montre que l'algorithme (le glouton) était le bon : il est **général**, et tout le savoir propre aux chiffres romains est dans la table.

---

## Étape 4 — Dans l'autre sens

**Le code :** `fromRoman` et `value` dans `RomanNumerals.java` (sans la vérification finale, qui vient à l'étape 5). Pour `"XIV"` : X (10, suivant I = 1, on ajoute) + I (1, suivant V = 5, on retire) + V (5, pas de suivant, on ajoute) = 10 − 1 + 5 = **14**.

---

## Étape 5 — Refuser ce qui est mal écrit

**Le code :** le `default` du `switch` dans `value`, et le `if` final de `fromRoman`.

**Question — les totaux sans contrôle :** `""` donne **0** (aucun caractère, la boucle ne tourne pas) ; `"MMMM"` donne **4000**. Il faut contrôler les limites **avant** `toRoman(total)`, car `toRoman(0)` et `toRoman(4000)` **lancent** `hors limites`. L'utilisateur recevrait alors « hors limites : 0 » au lieu de « chiffre romain invalide : » : le mauvais message. Grâce à `||` qui s'arrête au premier `true`, `toRoman` n'est appelé qu'avec un total valide.

**Question — `"MCMC"` :** M (1000) + C (−100, devant M) + M (1000) + C (100) = **2000**. Mais 2000 s'écrit `MM` : `toRoman(2000)` rend `"MM"`, différent de `"MCMC"`, donc refusé. On n'écrit jamais 1000 + 900 + 100 quand 2000 a son écriture directe.

---

## Étape 6 — L'aller-retour

**Le code :** le test `everyNumberSurvivesTheRoundTrip`.

**L'expérience** (vérifiée sur un calcul plus long que les 3999 nombres) : `execution exceeded timeout of 1 ms by 41 ms`. Le nombre après « by » change à chaque lancement. Attention : `assertTimeout` **attend la fin** du calcul avant de conclure ; pour couper un calcul qui ne finit jamais, il existe `assertTimeoutPreemptively`.

**Question — le test aller-retour seul ?** **Non.** Un couple `toRoman`/`fromRoman` qui coderait 1 en `"A"`, 2 en `"B"`… et ferait exactement l'inverse passerait l'aller-retour sans rien savoir des chiffres romains. Une propriété vérifie que les deux méthodes sont **cohérentes entre elles**, pas qu'elles sont **justes**. Il faut aussi des **exemples connus** (le `@CsvSource`) : les deux genres de tests se complètent.

---

## Étape 7 — Les mutants

**L'expérience** (vérifiée) : avec `Character.toUpperCase` dans `fromRoman`, **tous** les tests restent verts, les tiens **et** ceux de référence. `"iv"` est **toujours refusé** : le calcul donne bien 4, mais `toRoman(4)` rend `"IV"`, différent de `"iv"`, et la vérification finale refuse. Ce changement est un **mutant équivalent** : il ne change aucun résultat. (Il figurait dans la première version de `Check`, et il survivait même aux tests de référence : il a été retiré.) La leçon : le passage en majuscules serait du code **inutile**, car la vérification finale fait déjà le travail.

---

## Expériences de fin de projet

1. Sans `CM` dans la table (vérifié), trois cas échouent : `900` donne **`DCD`** (D + CD), `1994` donne `MDCDXCIV`, `3999` donne `MMMDCDXCIX`. Le glouton n'est juste que si la table contient toutes les paires soustractives.
2. Avec `if` au lieu de `while` (vérifié), le premier cas du tableau qui casse est **2** : `toRoman(2)` rend `"I"` (chaque symbole au plus une fois). Plus surprenant : `"MCMC"` devient **accepté**, car `toRoman(2000)` rend maintenant `"MCMC"` (M, puis CM, puis C) ; la vérification finale compare avec un `toRoman` lui-même faux. Le test aller-retour casse aussi.
