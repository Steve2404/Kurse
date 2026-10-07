# Drill de rappel 7 — Les objets immuables

> Première fois ? Lis d'abord le mode d'emploi [`ch6_classdesign/PARCOURS.md`](../../PARCOURS.md).

**Chrono cible :** 12 min, puis 6 min.

**Règles :**
- Tout se fait de mémoire.
- Fichier `Recall07.java`, paquet `ch6_classdesign.drills.r07_immutable`. Les classes :

| Classe | Contenu |
|---|---|
| `Temperature` (`final`) | `private final double celsius` ; un constructeur `private` ; `static ofCelsius(double)` ; `plus(double)` rend un **nouvel** objet ; `fahrenheit()` = `c * 9 / 5 + 32` ; `equals`, `hashCode` (`Double.hashCode`), `toString` = `20.0C` |
| `Series` (`final`) | `private final int[] values` ; un constructeur `private` ; `static of(int[])` **copie** ; `get(i)` ; `int[] values()` rend une **copie** ; `with(index, value)` rend une nouvelle `Series` ; `average()` ; `toString` avec `Arrays.toString` |

**Les notions de ce drill ont été apprises dans :** projet 4 (étapes 1 à 3). Si un défi te semble totalement inconnu, ce drill arrive trop tôt : refais d'abord la leçon de ce projet.

<details><summary><b>Comment faire ce drill, concrètement</b> (à lire la 1re fois)</summary>

1. **Note l'heure** de départ (montre ou téléphone).
2. **Crée la classe** : clic droit sur le dossier `r07_immutable` → **New** → **Java Class** → `Recall07`. S'il faut d'autres classes, place-les comme le disent les **Règles** ci-dessus ; si elles ne précisent rien, écris-les dans le même fichier, sous `Recall07`, sans `public`.
3. **Écris `main`**, puis **une ligne par défi**. La ligne qui suit la flèche `→` est **exactement** ce que ton programme doit afficher : `System.out.println("D01 : " + …);`, où les `…` sont des valeurs que **Java** calcule, jamais recopiées.
4. **Lance `Recall07`** avec la flèche verte, pour voir tes lignes.
5. **Bloqué plus de 3 minutes sur un défi ?** Écris un commentaire `// D03 : ✗` à sa place, et passe au suivant.
6. **Lance `Check.java`** (flèche verte). Une ligne `[FAIL]` te montre `attendu` et `obtenu`.
7. **Ensuite seulement**, ouvre la **carte mémoire** tout en bas (en aperçu Markdown, clique sur le triangle « Ouvrir la carte »), relis tes ✗, et fais les **expériences** (écris la ligne, compile, lis le message, efface la ligne).
8. **Note** la date, ton temps et tes ✗ dans le tableau de [`drills/README.md`](../README.md).

</details>

## Défis

- ☐ **D01.** `t = Temperature.ofCelsius(20)`, puis `warmer = t.plus(5)`. Affiche t, `warmer` et `t.fahrenheit()`.
  → `D01 : 20.0C 25.0C 68.0`
- ☐ **D02.** Dans cet ordre :
  1. `raw = {3, 1, 4}` ;
  2. `s = Series.of(raw)` ;
  3. `raw[0] = 99` ;
  4. `leaked = s.values()` ;
  5. `leaked[1] = 77`.
  
  Affiche `s`, `s.get(0)` et `s.get(1)`.
  → `D02 : [3, 1, 4] 3 1`
- ☐ **D03.** Compare t avec `Temperature.ofCelsius(20)` : `equals`, `==`, puis l'égalité des `hashCode`.
  → `D03 : true false true`
- ☐ **D04.** `changed = s.with(2, 9)`. Affiche s, `changed` et `changed.average()`.
  → `D04 : [3, 1, 4] [3, 1, 9] 4.333333333333333`
- ☐ **D05.** `final StringBuilder notImmutable = new StringBuilder("final");`, puis `append(" mais modifiable")`. Affiche-le, puis `"abc".toUpperCase().equals("ABC")`.
  → `D05 : final mais modifiable true`

## Expériences (hors sortie attendue)

1. Retire `final` de `class Series`, puis écris une sous-classe qui ajoute un setter : l'immuabilité tient-elle encore ?
2. Retire le `clone()` de `of(...)` : que devient D02 ? Et celui de `values()` ?
3. Pourquoi D05 montre-t-il que `final` sur une variable ne rend **pas** l'objet immuable ?

## Sortie attendue complète

```
D01 : 20.0C 25.0C 68.0
D02 : [3, 1, 4] 3 1
D03 : true false true
D04 : [3, 1, 4] [3, 1, 9] 4.333333333333333
D05 : final mais modifiable true
```

## Carte mémoire (à lire **après** le drill)

<details><summary>Ouvrir la carte</summary>

**Les 5 règles d'une classe immuable :**
1. la classe est `final` (ou ses constructeurs sont privés) : pas de sous-classe mutable ;
2. les champs sont `private final` ;
3. il n'y a aucun setter, aucune méthode qui modifie l'état ;
4. **les copies défensives** des objets mutables (tableaux, `StringBuilder`, `Date`…) se font à l'entrée **et** à la sortie ;
5. les « modifications » rendent un **nouvel** objet (comme `String`, `LocalDate`, `BigDecimal`).

**`final` sur une variable** interdit de la réaffecter, mais ne fige pas l'objet qu'elle désigne.

**`equals` et `hashCode`** sont toujours redéfinis ensemble, et sur les mêmes champs.

</details>
