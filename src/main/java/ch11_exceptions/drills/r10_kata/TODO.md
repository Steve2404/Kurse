# Drill de rappel 10 — Kata : `MessageFormat`, `Properties`, parse sans exception

> Première fois ? Lis d'abord le mode d'emploi [`ch11_exceptions/PARCOURS.md`](../../PARCOURS.md).

**Chrono cible :** 10 min, puis 5 min.

**Règles :**
- Tout se fait de mémoire, **imports compris**.
- Crée la classe **`Recall10`** dans le paquet `ch11_exceptions.drills.r10_kata`.
- **Première instruction du `main` :** `Locale.setDefault(Locale.US)`.
- Ajoute `static Optional<Integer> safeParse(String text)`, qui rend `Optional.of(Integer.parseInt(text.strip()))`, ou `Optional.empty()` en cas de `NumberFormatException`.

**Les notions de ce drill ont été apprises dans :** projets 1 à 7 : c'est le test final du chapitre. Si un défi te semble totalement inconnu, ce drill arrive trop tôt : refais d'abord la leçon de ce projet.

<details><summary><b>Comment faire ce drill, concrètement</b> (à lire la 1re fois)</summary>

1. **Note l'heure** de départ (montre ou téléphone).
2. **Crée la classe** : clic droit sur le dossier `r10_kata` → **New** → **Java Class** → `Recall10`. S'il faut d'autres classes, place-les comme le disent les **Règles** ci-dessus ; si elles ne précisent rien, écris-les dans le même fichier, sous `Recall10`, sans `public`.
3. **Écris `main`**, puis **une ligne par défi**. La ligne qui suit la flèche `→` est **exactement** ce que ton programme doit afficher : `System.out.println("D01 : " + …);`, où les `…` sont des valeurs que **Java** calcule, jamais recopiées.
4. **Lance `Recall10`** avec la flèche verte, pour voir tes lignes.
5. **Bloqué plus de 3 minutes sur un défi ?** Écris un commentaire `// D03 : ✗` à sa place, et passe au suivant.
6. **Lance `Check.java`** (flèche verte). Une ligne `[FAIL]` te montre `attendu` et `obtenu`.
7. **Ensuite seulement**, ouvre la **carte mémoire** tout en bas (en aperçu Markdown, clique sur le triangle « Ouvrir la carte »), relis tes ✗, et fais les **expériences** (écris la ligne, compile, lis le message, efface la ligne).
8. **Note** la date, ton temps et tes ✗ dans le tableau de [`drills/README.md`](../README.md).

</details>

## Défis

- ☐ **D01.** `MessageFormat.format("{1} a {0} ans, {1} !", 7, "Ana")`, puis `MessageFormat.format("{0}{0}", "ha")`.
  → `D01 : Ana a 7 ans, Ana ! | haha`
- ☐ **D02.** `"l''an {0}"` avec 2026 ; `"l'an {0}"` avec 2026 ; `"'{0}' = {0}"` avec `"x"`.
  → `D02 : l'an 2,026 | lan {0} | {0} = x`
- ☐ **D03.** Trois morceaux :
  - `MessageFormat.format("{0,number,#.#} | {0,number,integer} | {1,number,percent}", 3.14159, 0.25)` ;
  - `new MessageFormat("{0,number}", Locale.GERMANY).format(new Object[] {1234.5})` ;
  - `MessageFormat.format("{0}", 1234567)`.
  → `D03 : 3.1 | 3 | 25% | 1.234,5 | 1,234,567`
- ☐ **D04.** Deux objets `Properties` :
  - `defaults` : `color=bleu`, `size=M` ;
  - `props = new Properties(defaults)`, puis `props.setProperty("color", "rouge")`.
  
  Affiche :
  - `getProperty("color")`, `getProperty("size")` ;
  - `get("size")` ;
  - `getProperty("font", "Arial")` ;
  - `size()` ;
  - `stringPropertyNames()` triés.
  → `D04 : rouge M null Arial 1 [color, size]`
- ☐ **D05.** `props.put("count", 3)`. Affiche `getProperty("count")`, `get("count")`, puis `containsKey("count")`.
  → `D05 : null 3 true`
- ☐ **D06.** `safeParse(" 42 ")`, `safeParse("4x2")`, puis `safeParse("12").map(n -> n * 2).orElse(-1)`.
  → `D06 : Optional[42] Optional.empty 24`

## Expériences (hors sortie attendue)

1. Dans D02, pourquoi l'année s'affiche-t-elle `2,026` ? Comment l'éviter (`{0,number,#}`) ?
2. `MessageFormat.format("{0} {1}", "seul")` : que devient `{1}` ?
3. `props.setProperty("n", null)` : que se passe-t-il ?
4. Dans `safeParse`, pourquoi un `Optional` plutôt que de laisser l'exception remonter ?

## Sortie attendue complète

```
D01 : Ana a 7 ans, Ana ! | haha
D02 : l'an 2,026 | lan {0} | {0} = x
D03 : 3.1 | 3 | 25% | 1.234,5 | 1,234,567
D04 : rouge M null Arial 1 [color, size]
D05 : null 3 true
D06 : Optional[42] Optional.empty 24
```

## Carte mémoire (à lire **après** le drill)

<details><summary>Ouvrir la carte</summary>

**`MessageFormat`** :
- `{n}` désigne l'argument d'indice n ; un argument sans `{n}` est ignoré, et un `{n}` sans argument reste tel quel ;
- les types : `{0,number}`, `{0,number,integer}`, `{0,number,percent}`, `{0,number,#.##}` ;
- un nombre **sans type** est formaté avec le groupement de la locale ;
- **l'apostrophe :** `'` ouvre une zone littérale (les `{}` n'y comptent plus) ; `''` écrit une apostrophe. Dans les bundles français, écris donc toujours `l''article` ;
- `MessageFormat.format(motif, args…)` (locale par défaut), ou `new MessageFormat(motif, locale).format(new Object[] {…})`.

**`Properties`** (une `Hashtable<Object, Object>`) :
- `setProperty`, `getProperty(clé)`, `getProperty(clé, défaut)`, `stringPropertyNames()` ;
- `new Properties(defaults)` : seul `getProperty` consulte les valeurs par défaut (pas `get`, `size` ou `containsKey`) ;
- `getProperty` rend `null` pour une valeur qui n'est pas une `String`.

</details>
