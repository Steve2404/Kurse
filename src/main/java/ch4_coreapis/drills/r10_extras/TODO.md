# Drill de rappel 10 — Les méthodes moins fréquentes (`String`, `StringBuilder`, `Math`, `char`)

> Première fois ? Lis d'abord le mode d'emploi [`ch4_coreapis/PARCOURS.md`](../../PARCOURS.md).

**Chrono cible :** 15 min, puis 8 min.

**Règles :**
- Tout se fait de mémoire.
- Crée la classe **`Recall10`** dans le paquet `ch4_coreapis.drills.r10_extras`.
- Prédis chaque valeur avant de lancer : les limites (`""`, `-0.0`, débordement) sont les pièges.

**Les notions de ce drill ont été apprises dans :** projets 1, 2, 4 et 8 (les méthodes moins fréquentes y apparaissent au fil des étapes). Si un défi te semble totalement inconnu, ce drill arrive trop tôt : refais d'abord la leçon de ce projet.

<details><summary><b>Comment faire ce drill, concrètement</b> (à lire la 1re fois)</summary>

1. **Note l'heure** de départ (montre ou téléphone).
2. **Crée la classe** : clic droit sur le dossier `r10_extras` → **New** → **Java Class** → `Recall10`. S'il faut d'autres classes, place-les comme le disent les **Règles** ci-dessus ; si elles ne précisent rien, écris-les dans le même fichier, sous `Recall10`, sans `public`.
3. **Écris `main`**, puis **une ligne par défi**. La ligne qui suit la flèche `→` est **exactement** ce que ton programme doit afficher : `System.out.println("D01 : " + …);`, où les `…` sont des valeurs que **Java** calcule, jamais recopiées.
4. **Lance `Recall10`** avec la flèche verte, pour voir tes lignes.
5. **Bloqué plus de 3 minutes sur un défi ?** Écris un commentaire `// D03 : ✗` à sa place, et passe au suivant.
6. **Lance `Check.java`** (flèche verte). Une ligne `[FAIL]` te montre `attendu` et `obtenu`.
7. **Ensuite seulement**, ouvre la **carte mémoire** tout en bas (en aperçu Markdown, clique sur le triangle « Ouvrir la carte »), relis tes ✗, et fais les **expériences** (écris la ligne, compile, lis le message, efface la ligne).
8. **Note** la date, ton temps et tes ✗ dans le tableau de [`drills/README.md`](../README.md).

</details>

## Défis

- ☐ **D01.** Cinq comparaisons :
  - `"apple".compareToIgnoreCase("APPLE")` ;
  - `"a".compareToIgnoreCase("B")` ;
  - `"Zoo".compareTo("apple")` ;
  - `"Zoo".compareToIgnoreCase("apple")` ;
  - `"ab".compareTo("abc")`.
  → `D01 : 0 -1 -7 25 -1`
- ☐ **D02.** Les méthodes à **expression régulière**, sur `"a1b22c333"` et `"a.b.c"` :
  - `replaceAll("[0-9]+", "#")` ;
  - `replaceFirst("[0-9]", "_")` ;
  - `"2026-10-02"` correspond-il à `\d{4}-\d{2}-\d{2}` (`matches`) ?
  - le nombre de morceaux de `"a.b.c"` découpé par `"\\."` ;
  - puis découpé par `"."`.
  → `D02 : a#b#c# a_b22c333 true 3 0`
- ☐ **D03.** Sur `sb = new StringBuilder("java")` :
  - `setCharAt(0, 'J')`, puis affiche `sb` ;
  - la capacité d'un builder vide, d'un `("abc")` et d'un `(5)` ;
  - `sb.length()` et `sb.lastIndexOf("a")` ;
  - `compareTo` entre les builders `"a"` et `"b"`.
  → `D03 : Java 16 19 5 4 3 -1`
- ☐ **D04.** Les conversions :
  - `String.valueOf` de `3.0`, de `true`, de `'x'` et d'un `char[]` `{'o', 'k'}` ;
  - `Integer.toString(42)` ;
  - `Integer.parseInt("-17")` ;
  - `Integer.valueOf("08")`.
  → `D04 : 3.0 true x ok 42 -17 8`
- ☐ **D05.** Les cas limites :
  - `"hello".indexOf("")` ;
  - `"hello".lastIndexOf("l", 2)` ;
  - `"abc".contains("")` ;
  - `"Mississippi".replace("ss", "s")` ;
  - `"a-b-c".lastIndexOf('-')` ;
  - `"x".repeat(0).isEmpty()`.
  → `D05 : 0 2 true Misisippi 3 true`
- ☐ **D06.** `Math`, la suite :
  - `signum(-4.2)` ;
  - `cbrt(27)` ;
  - `hypot(3, 4)` ;
  - `Math.PI` arrondi à 4 décimales avec `round` ;
  - `Math.E > 2.7` ;
  - `log10(1000)`.
  → `D06 : -1.0 3.0 5.0 3.1416 true 3.0`
- ☐ **D07.** Débordements et précision :
  - `Integer.MAX_VALUE + 1` ;
  - le même calcul après un cast en `long` ;
  - `0.1 + 0.2` ;
  - `min(-0.0, 0.0)` et `max(-0.0, 0.0)` ;
  - `(int) 3.99` et `(int) -3.99`.
  → `D07 : -2147483648 2147483648 0.30000000000000004 -0.0 0.0 3 -3`
- ☐ **D08.** L'arithmétique des `char` :
  - `(char) ('a' + 1)` ;
  - `'z' - 'a'` ;
  - `(int) '0'` ;
  - `Character.isDigit('7')` et `Character.isLetter('_')` ;
  - `Character.toUpperCase('q')` ;
  - `'7' - '0'`.
  → `D08 : b 25 48 true false Q 7`

## Expériences (hors sortie attendue)

1. `"a+b".split("+")` : que se passe-t-il ? Pourquoi faut-il `"\\+"` ?
2. `"a.b".replace(".", "!")` contre `"a.b".replaceAll(".", "!")` : prédis les deux.
3. `char c = 'a' + 1;` compile, mais `char d = c + 1;` non. Pourquoi ? (Constante de compilation.)
4. Ajoute 20 caractères à un `new StringBuilder()` et affiche `capacity()` : la capacité a augmenté toute seule.
5. `Integer.parseInt("12a")` : quelle exception ?

## Sortie attendue complète

```
D01 : 0 -1 -7 25 -1
D02 : a#b#c# a_b22c333 true 3 0
D03 : Java 16 19 5 4 3 -1
D04 : 3.0 true x ok 42 -17 8
D05 : 0 2 true Misisippi 3 true
D06 : -1.0 3.0 5.0 3.1416 true 3.0
D07 : -2147483648 2147483648 0.30000000000000004 -0.0 0.0 3 -3
D08 : b 25 48 true false Q 7
```

## Carte mémoire (à lire **après** le drill)

<details><summary>Ouvrir la carte</summary>

**Le texte ou la regex :**
- `replace` prend un **texte** et remplace toutes les occurrences ;
- `replaceAll`, `replaceFirst`, `matches` et `split` prennent une **expression régulière** : `.` y veut dire « n'importe quel caractère », d'où `"\\."` ;
- `split` retire les morceaux vides **de la fin** : avec `"."`, tous les morceaux sont vides, d'où 0.

**Les comparaisons :**
- `compareTo` rend la différence des codes au premier écart (`'Z' - 'a'` vaut −7), ou la différence des longueurs ;
- `compareToIgnoreCase` compare les lettres sans tenir compte de la casse.

**Le texte vide :**
- `indexOf("")` vaut 0 ; `contains("")` et `startsWith("")` valent `true` ;
- `repeat(0)` donne `""`.

**`StringBuilder` :**
- `setCharAt` remplace un caractère (`String` n'a pas cette méthode) ;
- la capacité par défaut est 16, ou 16 + la longueur du texte de départ ; elle grandit toute seule ;
- `StringBuilder` est `Comparable` depuis Java 11.

**Les conversions :**
- `String.valueOf(x)` pour tout type ;
- `Integer.parseInt` rend un `int`, `Integer.valueOf` un `Integer` ; `"08"` est lu en base 10.

**Les nombres :**
- un `int` déborde **silencieusement** : `MAX_VALUE + 1` donne `MIN_VALUE` ;
- un cast en `int` **tronque** vers 0 ;
- les `double` sont des approximations binaires (`0.1 + 0.2`) ;
- `-0.0` est plus petit que `0.0` pour `min` et `max`.

**Les `char` :**
- un `char` est un nombre : `c - 'a'` donne une position, `c - '0'` la valeur d'un chiffre ;
- `(char) (…)` revient à une lettre.

</details>
