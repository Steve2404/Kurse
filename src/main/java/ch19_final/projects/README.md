# Chapitre 19 — Projets (tableau de bord)

> Le parcours complet (projets + drills + répétition, et **comment `Check` vérifie**) est décrit dans `../PARCOURS.md`.

Chaque projet construit une **brique** de l'application de l'atelier de vélos ; le projet 8 les **assemble**. Dans chaque dossier : `TODO.md` (l'énoncé, avec une leçon par étape), `INDICES.md`, `Check.java`, et `solution/` (avec `CORRIGE.md`), à n'ouvrir qu'à la fin.

**Tout le code et tous les tests, c'est toi qui les écris.** Les projets 3 et 8 réutilisent ton code des projets précédents.

| ☐ | Projet | Notions | Ce qui est dur |
|---|---|---|---|
| ☐ | `p01_json` — le format d'échange | type `sealed`, descente récursive, erreurs « où et quoi », profondeur maximale, écrivain, test de propriété | les positions exactes des erreurs |
| ☐ | `p02_store` — le dépôt en base H2 | injection SQL, migrations versionnées, exécuter autour, transactions, verrou optimiste, `LIKE` échappé | le piège du DDL qui valide tout seul |
| ☐ | `p03_api` — l'API HTTP avec le JDK seul | HTTP et REST, codes de statut, routeur, un seul guichet d'erreurs, adaptateur, port 0, concurrence côté serveur | les 405 et les en-têtes |
| ☐ | `p04_jobs` — les rappels en arrière-plan | échecs passagers, recul exponentiel, interruption, pool borné, `CompletableFuture`, délai, idempotence, arrêt propre | tester les fils sans dormir |
| ☐ | `p05_resilience` — tenir quand tout va mal | horloge manuelle, seau à jetons, fuite de mémoire, disjoncteur, percentiles, repli | l'essai unique du demi-ouvert |
| ☐ | `p06_performance` — le rapport trop lent | mesurer, profileur JFR, maître étalon, un passage, micro-mesure, test de vitesse | ne rien changer au résultat |
| ☐ | `p07_review` — la revue de code | liste de contrôle, un test par défaut, `equals`/`hashCode`, argent, `==`, limites, temps, ressources, état partagé, vie privée | trouver les défauts que la démo cache |
| ☐ | `p08_atelier` — **capstone** l'atelier en ligne | assembler, architecture hexagonale, règle métier nouvelle, *check-then-act*, filtres, racine de composition, bout en bout, exploitation | tout faire tenir ensemble |
