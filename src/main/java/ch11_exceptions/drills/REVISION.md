# Chapitre 11 (Exceptions and Localization) — parcours, drills et plan de révision

Les **exercices** (`ch11_exceptions/exercises`, 01 → 20) t'apprennent les notions.
Les **drills** (`ch11_exceptions/drills/exercises`, 01 → 06) te les font répéter jusqu'à ce
qu'elles sortent toutes seules. Tous les drills utilisent les mêmes données : `drills/Ledger.java`
(5 entrées brutes, une date, le bundle `ch11_exceptions.messages`). Lis ce fichier une fois.

Les corrigés (`solutions/` et `drills/solutions/`) sont **commentés** : chaque méthode
explique pourquoi on l'écrit ainsi et quel piège elle évite. Lis-les **après** avoir réussi.

⚠️ La Locale par défaut dépend de la machine (ici `de_DE`) : les exercices et les drills
passent **toujours** une Locale explicite. Fais de même.

---

## Par quoi commencer : exercices ou drills ?

**Les deux, en alternant, thème par thème.** Jour 1 : les exercices du thème
(comprendre). Jour 2 : le drill du thème (mémoriser).

| Étape | Thème | Jour 1 — exercices | Jour 2 — drills |
|---|---|---|---|
| 1 | Exceptions : checked, catch, finally, cause | 01 → 02 → 03 → 04 → 05 → 06 | Drill01 |
| 2 | try-with-resources | 07 → 08 → 09 | Drill02 |
| 3 | Formater des nombres et des messages | 10 → 11 → 12 | Drill03 |
| 4 | Formater des dates | 13 → 14 | Drill04 |
| 5 | Locale et ResourceBundle | 15 → 16 → 17 → 18 → 19 | Drill05 |
| 6 | Synthèse | 20 (capstone : import de lot localisé) | Drill06 (kata mélangé) |

**Séance type (≈ 1 h) :** 1) les révisions dues (10 – 20 min), 2) la nouveauté,
3) 2 minutes de « carte vierge » : l'arbre Throwable / Error / Exception / RuntimeException,
l'ordre try → close → catch → finally, les lettres de `DateTimeFormatter`, l'ordre de recherche
des bundles.

**Conseil propre à ce chapitre :** devant une exception, pose deux questions — *checked ?*
(alors catch ou throws obligatoire) et *qui gagne ?* (finally qui rend, exception du bloc
contre exception de close). Devant un format : *quelle Locale ?* et *0 ou # ?*

| Drill | Contenu | TODO |
|---|---|---|
| 01 `ExceptionBasics` | try/catch dans une boucle, finally, multi-catch, cause, checked + throws, `getMessage`, les 5 exceptions du JDK, checked ou non | 10 |
| 02 `TryWithResources` | une et deux ressources, close → catch → finally, suppressed, close qui échoue seul, `try (r)` Java 9, ressource null, Scanner | 8 |
| 03 `NumberFormatting` | `DecimalFormat` (0, #, arrondi au pair), devise US et FR, pourcentage, Allemagne, compact, `parse`, `MessageFormat` et `''` | 12 |
| 04 `DateFormatting` | ISO, motifs, 12 h, mois en français, texte entre apostrophes, styles localisés, `parse`, `UnsupportedTemporalTypeException` | 10 |
| 05 `LocaleAndBundles` | constante, constructeur, Builder, `forLanguageTag`, `toLanguageTag`, `getDisplayCountry`, `getBundle`, parent, `keySet`, catégorie FORMAT, gabarit localisé | 11 |
| 06 `MixedKata` | 10 questions sur le registre, **sans indiquer la forme** | 10 |

---

## Comment faire un drill

1. Lance un chronomètre.
2. Remplis les TODO **sans regarder la « CARTE MÉMOIRE »** en bas du fichier.
3. Bloqué plus d'une minute ? Regarde la carte, **cache-la, puis réécris de mémoire**.
   Mets une croix à côté de ce TODO : c'est un point faible.
4. Lance `main()` jusqu'à 100 %.
5. Note ton temps, ton score au premier lancement et tes TODO « croix » dans le tableau.

## Pour ne plus oublier

- **Rappel actif** : refaire depuis une page blanche vaut dix relectures.
- **Répétition espacée** : J, J+1, J+3, J+7, J+14, J+30, puis tous les 2 mois.
- **Mélange** : le Drill06 chaque semaine pendant la révision de l'examen.
- **Lecture de code** : les exercices 02 et 09 comparent ta règle aux **verdicts réels de `javac`**
  (`exception IOException has already been caught`, `... is never thrown in body of corresponding
  try statement`, `Alternatives in a multi-catch statement cannot be related by subclassing`,
  `overridden method does not throw Exception`, `auto-closeable resource r may not be assigned`…) ;
  les exercices 04, 11, 14 et 19 la comparent à la **JVM** (try/catch/finally exécutés,
  `DecimalFormat`, `DateTimeFormatter`, le vrai `ResourceBundle.getBundle`).
  Relis leurs Javadoc avant l'examen, puis fais les questions de révision du livre.

## Remettre un fichier à zéro pour le refaire

```
git restore src/main/java/ch11_exceptions/drills/exercises/Drill01_ExceptionBasics.java
```

(tant que tes réponses ne sont pas commitées ; sinon `git restore --source=origin/main -- <chemin>`).
⚠️ Cela efface ta version : c'est voulu pour un drill.

## Tableau de suivi

Format : `date – temps – score au 1er lancement` (ex. `30/09 – 8 min – 9/10`).

| Drill | J | J+1 | J+3 | J+7 | J+14 | J+30 | TODO faibles |
|---|---|---|---|---|---|---|---|
| 01 Exceptions | | | | | | | |
| 02 try-with-resources | | | | | | | |
| 03 Nombres et messages | | | | | | | |
| 04 Dates | | | | | | | |
| 05 Locale et bundles | | | | | | | |
| 06 Kata mélangé | | | | | | | |
