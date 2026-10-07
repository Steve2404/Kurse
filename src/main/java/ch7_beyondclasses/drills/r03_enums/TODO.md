# Drill de rappel 3 — Les enums simples

> Première fois ? Lis d'abord le mode d'emploi [`ch7_beyondclasses/PARCOURS.md`](../../PARCOURS.md).

**Chrono cible :** 10 min, puis 5 min.

**Règles :**
- Tout se fait de mémoire.
- Fichier `Recall03.java`, paquet `ch7_beyondclasses.drills.r03_enums`. Sous `Recall03` : `enum Day { MON, TUE, WED, THU, FRI, SAT, SUN }`.
- Dans `Recall03`, deux méthodes `static` :
  - `boolean isWeekend(Day d)` : un `switch` **instruction**, avec `case SAT:` et `case SUN:` empilés qui rendent `true`, et `default` qui rend `false` ;
  - `int hours(Day d)` : un `switch` **expression**, avec `case SAT, SUN -> 0`, `case FRI -> 4` et `default -> 8`.

**Les notions de ce drill ont été apprises dans :** projet 2 (étape 1). Si un défi te semble totalement inconnu, ce drill arrive trop tôt : refais d'abord la leçon de ce projet.

<details><summary><b>Comment faire ce drill, concrètement</b> (à lire la 1re fois)</summary>

1. **Note l'heure** de départ (montre ou téléphone).
2. **Crée la classe** : clic droit sur le dossier `r03_enums` → **New** → **Java Class** → `Recall03`. S'il faut d'autres classes, place-les comme le disent les **Règles** ci-dessus ; si elles ne précisent rien, écris-les dans le même fichier, sous `Recall03`, sans `public`.
3. **Écris `main`**, puis **une ligne par défi**. La ligne qui suit la flèche `→` est **exactement** ce que ton programme doit afficher : `System.out.println("D01 : " + …);`, où les `…` sont des valeurs que **Java** calcule, jamais recopiées.
4. **Lance `Recall03`** avec la flèche verte, pour voir tes lignes.
5. **Bloqué plus de 3 minutes sur un défi ?** Écris un commentaire `// D03 : ✗` à sa place, et passe au suivant.
6. **Lance `Check.java`** (flèche verte). Une ligne `[FAIL]` te montre `attendu` et `obtenu`.
7. **Ensuite seulement**, ouvre la **carte mémoire** tout en bas (en aperçu Markdown, clique sur le triangle « Ouvrir la carte »), relis tes ✗, et fais les **expériences** (écris la ligne, compile, lis le message, efface la ligne).
8. **Note** la date, ton temps et tes ✗ dans le tableau de [`drills/README.md`](../README.md).

</details>

## Défis

- ☐ **D01.** Affiche :
  - `Day.values().length` et `Day.MON.ordinal()` ;
  - `Day.SUN.name()` ;
  - `Day.valueOf("FRI")` et `Day.WED`.
  → `D01 : 7 0 SUN FRI WED`
- ☐ **D02.** `Day d = Day.TUE;`. Affiche :
  - `MON.compareTo(FRI)` et `SUN.compareTo(MON)` ;
  - `d == Day.TUE` et `d.equals(Day.TUE)`.
  → `D02 : -4 6 true true`
- ☐ **D03.** Pour chaque jour : compte les jours du week-end avec `isWeekend`, et additionne les `hours`.
  → `D03 : 2 36`
- ☐ **D04.** Deux calculs :
  - le jour après `SUN`, avec `values()[(ordinal() + 1) % length]` ;
  - le jour après `FRI`.
  → `D04 : MON SAT`
- ☐ **D05.** La première lettre du nom de chaque jour, collées.
  → `D05 : MTWTFSS`

## Expériences (hors sortie attendue)

1. Écris `case Day.SAT:` dans le `switch` : que dit `javac` en Java 17 ?
2. `Day.valueOf("mon")` : que se passe-t-il à l'exécution ?
3. `new Day()` et `class Weekday extends Day` : quelles erreurs ?
4. `if (d == 1)` : quelle erreur ? Un enum n'est pas un `int`.
5. Dans `hours`, retire `default` : quelle erreur ? Et si tu listes les 7 jours sans `default` ?

## Sortie attendue complète

```
D01 : 7 0 SUN FRI WED
D02 : -4 6 true true
D03 : 2 36
D04 : MON SAT
D05 : MTWTFSS
```

## Carte mémoire (à lire **après** le drill)

<details><summary>Ouvrir la carte</summary>

**Les méthodes d'un enum :**
- `values()` : un nouveau tableau des constantes, dans l'ordre de déclaration ;
- `ordinal()` : la position, à partir de 0 ;
- `name()` : le nom exact (et `toString()` par défaut) ;
- `valueOf("NOM")` : sensible à la casse ; un nom inconnu lève une `IllegalArgumentException` ;
- `compareTo` : la différence des `ordinal`.

**La comparaison :** chaque constante est unique, donc `==` suffit.

**Dans un `switch` :**
- les `case` portent le nom **sans** préfixe (`case SAT:`) ;
- un `switch` **expression** qui couvre toutes les constantes n'a pas besoin de `default`.

**Un enum :**
- ne s'instancie pas et ne s'étend pas ;
- peut implémenter des interfaces ;
- hérite de `java.lang.Enum`.

</details>
