# Chapitre 11 — Projets (tableau de bord)

> Le parcours complet (projets + drills + répétition, et la règle du crescendo) est décrit dans `../PARCOURS.md`.

Chaque projet est une **application à construire de A à Z**. Dans son dossier, tu ne trouves que :
- `TODO.md` : l'énoncé ;
- `Data.java` : les données ;
- `Check.java` : le correcteur ;
- `solution/` : à n'ouvrir qu'à la fin.

**Tous les types, c'est toi qui les crées.** Pour p07, tu crées aussi les fichiers `.properties`.

| ☐ | Projet | Notions | Classe `main` | Ce qui est dur |
|---|---|---|---|---|
| ☐ | `p01_bank` — guichet de banque | hiérarchie vérifiée / non vérifiée, `throw` et `throws`, redéfinition, ordre des `catch`, multi-catch, `finally`, chaînage | `Teller` | virement atomique avec compensation, exception vérifiée dans une lambda |
| ☐ | `p02_calculator` — calculatrice robuste | traduire en gardant la cause, relance précise, `Math.…Exact`, `Error`, `Optional`, lambdas | `Calculator` | analyseur récursif qui situe chaque erreur, adaptateur `ThrowingFunction` |
| ☐ | `p03_resources` — ressources et services | try-with-resources, `AutoCloseable` et `Closeable`, supprimées, Java 9 | `Resources` | réessais (cause et supprimées), disjoncteur CLOSED / OPEN / HALF_OPEN |
| ☐ | `p04_vm` — machine à exceptions | hiérarchie abstraite, multi-catch, exception hors famille, règles de `finally` | `VmLab` | gestionnaires `TRY` et déroulage de pile, comme la JVM |
| ☐ | `p05_invoice` — facturation internationale | `NumberFormat` (toutes les fabriques), `DecimalFormat`, compact, `parse` et `ParseException` | `Billing` | TVA par taux, partage au plus fort reste, amortissement |
| ☐ | `p06_agenda` — agenda international | `DateTimeFormatter` (motifs, styles, ISO, échappement), erreurs de dates | `Agenda` | lecture à repli avec supprimées, N-ième jour du mois, jours ouvrés, fuseaux et heure d'été |
| ☐ | `p07_shop` — **capstone** boutique | `Locale`, `ResourceBundle`, `.properties`, `MessageFormat`, `Properties`, catégories | `Shop` | ordre de recherche des bundles, apostrophes, taux de traduction |
