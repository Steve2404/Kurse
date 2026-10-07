# Drill de rappel 9 — `Locale` et `ResourceBundle`

> Première fois ? Lis d'abord le mode d'emploi [`ch11_exceptions/PARCOURS.md`](../../PARCOURS.md).

**Chrono cible :** 12 min, puis 6 min.

**Règles :**
- Tout se fait de mémoire, **imports compris**, y compris les **fichiers**.
- Crée quatre bundles dans `src/main/resources/ch11_exceptions/drills/r09_bundles/` :

  | Fichier | Contenu |
  |---|---|
  | `labels.properties` | `hello=Hello racine`, `color=colour`, `size=taille racine`, `only=seulement ici` |
  | `labels_en.properties` | `hello=Hello en`, `color=color` |
  | `labels_en_CA.properties` | `hello=Hello Canada` |
  | `labels_fr.properties` | `hello=Bonjour` |

- Crée la classe **`Recall09`** dans le paquet `ch11_exceptions.drills.r09_bundles`.
  - Le nom de base est `ch11_exceptions.drills.r09_bundles.labels`.
  - Ajoute `static String show(ResourceBundle b)`, qui rend `[<getLocale()>] <hello> / <color> / <size>`.
- **Première instruction du `main` :** `Locale.setDefault(Locale.FRANCE)`.

**Les notions de ce drill ont été apprises dans :** projet 7 (étapes 1, 2 et 5). Si un défi te semble totalement inconnu, ce drill arrive trop tôt : refais d'abord la leçon de ce projet.

<details><summary><b>Comment faire ce drill, concrètement</b> (à lire la 1re fois)</summary>

1. **Note l'heure** de départ (montre ou téléphone).
2. **Crée la classe** : clic droit sur le dossier `r09_bundles` → **New** → **Java Class** → `Recall09`. S'il faut d'autres classes, place-les comme le disent les **Règles** ci-dessus ; si elles ne précisent rien, écris-les dans le même fichier, sous `Recall09`, sans `public`.
3. **Écris `main`**, puis **une ligne par défi**. La ligne qui suit la flèche `→` est **exactement** ce que ton programme doit afficher : `System.out.println("D01 : " + …);`, où les `…` sont des valeurs que **Java** calcule, jamais recopiées.
4. **Lance `Recall09`** avec la flèche verte, pour voir tes lignes.
5. **Bloqué plus de 3 minutes sur un défi ?** Écris un commentaire `// D03 : ✗` à sa place, et passe au suivant.
6. **Lance `Check.java`** (flèche verte). Une ligne `[FAIL]` te montre `attendu` et `obtenu`.
7. **Ensuite seulement**, ouvre la **carte mémoire** tout en bas (en aperçu Markdown, clique sur le triangle « Ouvrir la carte »), relis tes ✗, et fais les **expériences** (écris la ligne, compile, lis le message, efface la ligne).
8. **Note** la date, ton temps et tes ✗ dans le tableau de [`drills/README.md`](../README.md).

</details>

## Défis

- ☐ **D01.** `show` du bundle de `new Locale("en", "CA")`.
  → `D01 : [en_CA] Hello Canada / color / taille racine`
- ☐ **D02.** `show` du bundle de `Locale.US`.
  → `D02 : [en] Hello en / color / taille racine`
- ☐ **D03.** `show` du bundle de `Locale.GERMANY`.
  → `D03 : [fr] Bonjour / colour / taille racine`
- ☐ **D04.** `canada` = le bundle de `Locale.CANADA`. Affiche :
  - ses clés triées ;
  - `containsKey("only")` et `getString("only")` ;
  - ` | ` ;
  - `getString("nope")` dans un `try` : `catch (MissingResourceException e)` → nom simple et `getKey()`.
  → `D04 : [color, hello, only, size] true seulement ici | MissingResourceException nope`
- ☐ **D05.** Trois locales :
  - `a = new Locale("en", "CA")` ;
  - `b = new Locale.Builder().setRegion("CA").setLanguage("en").build()` ;
  - `c = Locale.forLanguageTag("en-CA")`.
  
  Affiche :
  - `a` ;
  - `c.toLanguageTag()` ;
  - l'égalité de `a`, `Locale.CANADA`, `b` et `c` ;
  - `new Locale("EN")` ;
  - `a.getDisplayCountry(Locale.FRENCH)` ;
  - `Locale.JAPAN.getDisplayLanguage(Locale.GERMAN)` ;
  - `Locale.getDefault()`.
  → `D05 : en_CA en-CA true en Canada Japanisch fr_FR`
- ☐ **D06.** `Locale.setDefault(Locale.Category.DISPLAY, Locale.GERMANY)`. Affiche `Locale.FRANCE.getDisplayName()`, ` | `, puis `Locale.getDefault()`, la catégorie `DISPLAY` et la catégorie `FORMAT`.
  → `D06 : Französisch (Frankreich) | fr_FR de_DE fr_FR`
- ☐ **D07.** `byDefault = ResourceBundle.getBundle(BASE)` (**sans** locale). Affiche `show(byDefault)`, ` | `, puis le nom simple de la classe de `byDefault.getObject("hello")`.
  → `D07 : [fr] Bonjour / colour / taille racine | String`

## Expériences (hors sortie attendue)

1. Dans D03, pourquoi `color` vaut-il `colour` (la racine), et non `color` (anglais) ?
2. Retire `Locale.setDefault(Locale.FRANCE)` sur une machine allemande : que devient D03 ?
3. Renomme `labels_fr.properties` en `labels_FR.properties` : que se passe-t-il ?
4. `new Locale.Builder().setLanguage("english")` : quelle exception ?

## Sortie attendue complète

```
D01 : [en_CA] Hello Canada / color / taille racine
D02 : [en] Hello en / color / taille racine
D03 : [fr] Bonjour / colour / taille racine
D04 : [color, hello, only, size] true seulement ici | MissingResourceException nope
D05 : en_CA en-CA true en Canada Japanisch fr_FR
D06 : Französisch (Frankreich) | fr_FR de_DE fr_FR
D07 : [fr] Bonjour / colour / taille racine | String
```

## Carte mémoire (à lire **après** le drill)

<details><summary>Ouvrir la carte</summary>

**Choisir le bundle** pour la locale demandée `fr_CA`, avec une locale par défaut `en_US` :
1. `labels_fr_CA` ;
2. `labels_fr` ;
3. `labels_en_US` ;
4. `labels_en` ;
5. `labels` (la racine) ;
6. sinon, `MissingResourceException`.

On s'arrête au **premier fichier trouvé**. La locale par défaut ne sert que si **rien** n'existe pour la locale demandée (hors racine).

**Chercher une clé :** dans le bundle trouvé, puis dans ses **parents** seulement (`fr_CA` → `fr` → racine), jamais dans la locale par défaut. Une clé absente partout lève `MissingResourceException`.

**`Locale`** :
- `new Locale("fr")`, `new Locale("fr", "CA")` (la casse est normalisée) ;
- les constantes `Locale.CANADA_FRENCH`, `Locale.FRANCE`… ;
- `new Locale.Builder().setLanguage().setRegion().build()` (valide les valeurs) ;
- `Locale.forLanguageTag("fr-CA")` ;
- `toString()` donne `fr_CA` ; `toLanguageTag()` donne `fr-CA`.

**Sans locale,** `getBundle(base)` utilise `Locale.getDefault()`. `getObject(clé)` rend un `Object` ; `getString` = `(String) getObject`.

**Les catégories :** `setDefault(Locale.Category.FORMAT, …)` règle les nombres et les dates ; `DISPLAY` règle les noms affichés. `setDefault(locale)` règle tout.

</details>
