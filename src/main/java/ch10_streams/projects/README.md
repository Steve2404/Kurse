# Chapitre 10 — Projets (tableau de bord général)

> Le parcours complet (projets + drills de rappel + plan de répétition) est décrit dans `../PARCOURS.md`.

Chaque projet est une **application à construire de A à Z**. Dans son dossier, tu ne trouves que trois choses :

| Fichier | Rôle |
|---|---|
| `TODO.md` | L'énoncé et le tableau de bord par étapes : règles, lignes attendues, contraintes et questions, au même endroit |
| `Data.java` | Les données brutes |
| `Check.java` | Le correcteur : il lance ton `main`, compare ta sortie ligne par ligne, puis vérifie que tu as pratiqué toute l'API visée |

**C'est toi qui crées tous les fichiers `.java` :** records, classes, interfaces, enums, exceptions, méthodes et le `main`. Un seul nom est imposé par projet : celui de la classe qui contient le `main`.

`solution/` contient une conception commentée parmi d'autres. Ne l'ouvre **qu'après** avoir réussi (ou être resté vraiment bloqué).

Lance `Check.java` avec l'argument `solution` pour voir à quoi ressemble un projet réussi.

---

## Parcours

| ☐ | Projet | Thème du chapitre | Classe `main` | Algorithme au cœur |
|---|---|---|---|---|
| ☐ | `p01_loandesk` — guichet de prêt | `Optional`, `OptionalInt`, `OptionalDouble` | `LoanDesk` | file d'attente, pénalités plafonnées |
| ☐ | `p02_busnetwork` — itinéraires de bus | sources, paresse, opérations intermédiaires, recherche | `BusNetwork` | horaires générés à la demande, trajet avec correspondance |
| ☐ | `p03_weather` — station météo | `IntStream`, `LongStream`, `DoubleStream` | `WeatherStation` | fenêtre glissante, médiane, histogramme, plus longues séries |
| ☐ | `p04_league` — championnat | `reduce` (3 formes), `collect`, `Collector.of` et ses 4 morceaux | `League` | classement avec départages, série fusionnable par morceaux |
| ☐ | `p05_music` — plateforme musicale | tous les `Collectors` | `MusicStats` | recommandation par similarité de Jaccard |
| ☐ | `p06_spliterator` — journal de caisse | `Spliterator`, `StreamSupport` | `CashJournal` | découpe d'un journal sans couper une transaction |
| ☐ | `p07_warehouse` — **capstone** entrepôt | tout le chapitre | `Warehouse` | allocation de stock par priorité, contrôle comptable |
| ☐ | `p08_telemetry` — supervision de serveurs | `LongStream`/`DoubleStream`, interfaces fonctionnelles primitives, pièges | `Telemetry` | trous de sonde, règles d'alerte, composition de fonctions |

Fais-les **dans l'ordre**. Chaque projet réutilise des réflexes du précédent : `Optional::stream` (p01) revient dans p02, p05 et p07, et les centimes en `long` de p06 reviennent dans p07.

## Méthode conseillée pour chaque projet

1. **Lis tout le `TODO.md`**, puis la sortie attendue. Calcule 2 ou 3 lignes **à la main**.
2. **Conçois sur papier** tes types et leurs responsabilités. Ne code qu'ensuite.
3. **Avance étape par étape.** Lance `Check` souvent : il te donne la première ligne fausse.
4. **Réponds par écrit** (en commentaire) à chaque question du `TODO.md`. Ce sont des questions d'examen OCP.
5. Quand `Check` affiche `PROJET REUSSI`, **compare** ta conception avec `solution/`.
