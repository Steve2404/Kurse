# Chapitre 8 — Projets (tableau de bord)

> Le parcours complet (projets + drills + répétition, et la règle du crescendo) est décrit dans `../PARCOURS.md`.

Chaque projet est une **application à construire de A à Z**. Dans son dossier, tu ne trouves que :
- `TODO.md` : l'énoncé ;
- `Data.java` : les données ;
- `Check.java` : le correcteur ;
- `solution/` : à n'ouvrir qu'à la fin.

**Tous les types, c'est toi qui les crées.**

| ☐ | Projet | Notions | Classe `main` | Ce qui est dur |
|---|---|---|---|---|
| ☐ | `p01_pipeline` — pipeline de texte | toutes les syntaxes, interface fonctionnelle à toi, `Function` (`andThen`, `compose`, `identity`), `BiFunction`, `BinaryOperator` | `PipelineApp` | analyseur de pipelines, RLE, César, mémoïsation sans collection |
| ☐ | `p02_rules` — règles de mots de passe | `Predicate` (`and`, `or`, `negate`, `not`, `isEqual`), `BiPredicate`, lambdas dans un record | `RulesApp` | compiler une règle texte en `Predicate` (descente récursive), correctifs gloutons |
| ☐ | `p03_events` — simulation d'agence | `Runnable`, `Supplier` paresseux, `IntSupplier`, `BiConsumer.andThen`, `this::m` | `EventsApp` | simulation à événements discrets, tas binaire écrit à la main |
| ☐ | `p04_numeric` — laboratoire numérique | toutes les interfaces primitives, fonction qui rend une fonction | `NumericApp` | dichotomie, Newton, Simpson, section dorée, Monte-Carlo |
| ☐ | `p05_calculator` — calculatrice | les 4 références de méthode, constructeurs et constructeur de tableau | `Calculator` | gare de triage (priorités, associativité à droite, fonctions à 2 arguments) |
| ☐ | `p06_sorting` — trier avec des fonctions | interface à toi avec combinateurs, `ToIntFunction`, capture | `SortingApp` | tri fusion stable, insertion, top-k, dichotomie par clé |
| ☐ | `p07_orders` — **capstone** commandes | tout le chapitre, currying, primitives `long` | `Engine` | meilleure combinaison de promos (ordre de composition), livraison paresseuse |
