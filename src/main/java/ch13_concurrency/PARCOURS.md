# Chapitre 13 (Concurrency) — Mode d'emploi

Lis ce fichier **en entier une fois** avant de commencer. Le fonctionnement est le même qu'aux chapitres 1 à 11 :
- des **projets** à construire de A à Z, pour **comprendre** ;
- des **drills** chronométrés, répétés à intervalles espacés, pour **retenir**.

---

## 1. Ce que tu vas faire

| | Projets (`projects/`) | Drills de rappel (`drills/`) |
|---|---|---|
| Combien | 7 | 6 |
| But | des programmes **concurrents corrects** : découper un travail, le répartir, le combiner, sans course ni interblocage | retrouver vite et sans aide l'API et ses pièges |
| Durée | 2 à 4 h chacun | 10 à 15 min chacun |
| Combien de fois | une fois ; p03 et p07 refaits 2 à 3 semaines plus tard | 6 fois chacun (J0, J+1, J+3, J+7, J+14, J+30) |
| Aide autorisée | Javadoc, papier, réflexion | **aucune** pendant le drill |

---

## 2. La règle du crescendo : chapitres 1 à 13

**Tu as droit à :**
- les **chapitres 1 à 12** : tout le langage, les collections, les génériques, les streams, `Optional`, les exceptions, la localisation ;
- tout le **chapitre 13** :
  - **les threads** : `Thread`, `Runnable`, les états, `interrupt`, les daemons ;
  - **les executors** : `ExecutorService`, `Callable`, `Future`, `invokeAll`/`invokeAny`, `ScheduledExecutorService` ;
  - **la synchronisation** : `synchronized`, `volatile`, les classes atomiques, `Lock`/`ReentrantLock`, `ReadWriteLock`, `CyclicBarrier` ;
  - **les collections concurrentes** et les files bloquantes ;
  - **les streams parallèles** et les collecteurs concurrents ;
  - **les problèmes** : course, interblocage, famine, livelock ;
  - `CountDownLatch`, comme outil de coordination dans les projets.

**Ce qui reste exclu :** les fichiers (chapitre 14), JDBC (chapitre 15), `System.exit`, `printStackTrace` et `Thread.stop()`. `Check` les refuse avec le message `[FAIL] API : interdit ici`.

---

## 3. La règle d'or de ce chapitre : une sortie DÉTERMINISTE

`Check` compare ta sortie, ligne par ligne. Or l'ordre d'exécution des threads change à chaque lancement. Tes programmes doivent donc **n'afficher que des résultats qui ne dépendent pas de cet ordre** :
- **c'est `main` qui affiche**, après avoir attendu les threads (`join`, `Future.get`, `awaitTermination`). Un thread de travail n'écrit jamais dans la console ;
- **chaque thread écrit dans sa propre case**, ou dans une structure concurrente, ou dans un atomique ;
- **les résultats sont combinés dans un ordre fixe** (fusion des morceaux dans l'ordre), ou **triés** avant l'affichage ;
- **un état « intermédiaire »** (`TIMED_WAITING`, `BLOCKED`…) s'**observe** : on attend en boucle que le thread l'atteigne, on ne le devine pas ;
- **les résultats « au moins »** (nombre de battements d'une tâche périodique) s'affichent comme une **condition** (`>= 5`), pas comme une valeur exacte.

**Et toujours :**
- `shutdown()` chaque executor, dans un `finally`. Sinon le programme, et `Check`, ne se terminent pas ;
- relâche chaque `Lock` dans un `finally`.

Lance ton programme plusieurs fois : la sortie doit être identique à chaque fois.

---

## 4. Par où commencer (aujourd'hui)

1. Lis ce fichier jusqu'au bout.
2. Ouvre `projects/p01_downloader/TODO.md` en aperçu Markdown.
3. Suis la section 6.

**L'ordre complet :**

```
p01 → r01
p02 → r02
p03 → r03
p04 → r04
p05 → (révision r03)
p06 → r05
p07 → r06 (test final)
```

---

## 5. La disposition des dossiers

```
ch13_concurrency/
├── PARCOURS.md              ← ce fichier
├── projects/
│   ├── README.md            ← la liste des 7 projets, à cocher
│   └── p01_downloader/
│       ├── TODO.md          ← L'ÉNONCÉ
│       ├── Data.java        ← les données : tu les lis, tu ne les modifies pas
│       ├── Check.java       ← le correcteur : tu le LANCES
│       ├── solution/        ← la correction : à la fin seulement
│       └── (tes types)      ← ChunkStats.java, Downloader.java... : c'est TOI qui les crées
└── drills/
    ├── README.md            ← règles des drills + tableau de suivi
    └── r01_threads/ …
```

---

## 6. Comment faire un projet

### 6.1 Lire le `TODO.md`

| Partie | Ce que tu en fais |
|---|---|
| **En-tête** | les notions visées, l'algorithme, les types à créer |
| **Tableau de bord** (étapes ☐) | les types et leurs membres, les **lignes exactes**, les **appels exacts**, l'ordre des opérations (`start`, `join`, `shutdown`…), les questions et les expériences |
| **Checklist** | ce que `Check` cherchera dans ton code |
| **Sortie attendue complète** | le contrat exact |

### 6.2 Travailler, étape par étape

1. **Sur papier :** qui partage quoi ? Pour chaque donnée partagée, choisis sa protection : verrou, atomique, structure concurrente, ou « une case par thread ».
2. **Écris d'abord la version séquentielle** du calcul. La plupart des projets comparent le résultat parallèle au séquentiel.
3. **Fais une étape à la fois.** Lance `Check`, corrige, puis coche ☐ → ☑. **Lance-le plusieurs fois.**
4. **Fais les expériences.** Casser volontairement la synchronisation et voir les résultats changer, c'est **le** meilleur moyen de comprendre.

### 6.3 Lire la réponse de `Check`

| Ligne | Signification | Que faire |
|---|---|---|
| `[ERREUR] classe introuvable` | mauvais nom de classe ou de paquet | vérifie le `package` et le nom du fichier |
| `[ERREUR] ton programme a lance …` | une exception est sortie du `main` | souvent `ExecutionException`, `InterruptedException` non traitée |
| `[FAIL] sortie : … (ligne 4)` | la ligne diffère | un affichage depuis un thread, un ordre non trié, un `join` oublié |
| `Check` ne se termine pas | un executor n'est pas arrêté, ou un interblocage | `shutdown()` dans un `finally` ; l'ordre des verrous |
| `[FAIL] API : …` | un élément manque, ou est interdit | la checklist |
| `*** PROJET REUSSI ***` | tout est juste | **relance-le 2 ou 3 fois**, puis compare avec la solution |

### 6.4 Quand tu bloques

| Palier | Temps | Ce que tu fais |
|---|---|---|
| 1 | jusqu'à 20 min | remplace le pool par un seul thread : si le résultat devient juste, c'est un problème de partage |
| 2 | 20 min de plus | relis la carte mémoire du drill du même thème, ou demande-moi un **indice** |
| 3 | en dernier recours | lis **uniquement** la partie concernée de `solution/`, ferme, réécris |

---

## 7. Comment faire un drill

1. Note l'heure. Crée `RecallNN.java`, de mémoire.
2. Lance `Check` (deux fois, pour vérifier la stabilité).
3. **Après seulement :** la carte mémoire, puis les expériences.
4. Note date, temps et ✗ dans `drills/README.md`. Avant chaque répétition, supprime ton `RecallNN.java`.

---

## 8. Comment savoir que le chapitre 13 est acquis

- [ ] Les 7 projets affichent `PROJET REUSSI`, plusieurs fois de suite, et toutes les questions ont une réponse écrite.
- [ ] Les 6 drills ont passé la répétition R3 (J+7, sans carte).
- [ ] Tu sais, sans hésiter :
  - donner l'état d'un thread dans une situation donnée, et ce que fait `interrupt` ;
  - choisir entre `execute`, `submit`, `invokeAll` et `invokeAny`, et ce que lève `Future.get` ;
  - arrêter proprement un executor ;
  - choisir entre `synchronized`, atomique, `Lock` et structure concurrente ;
  - écrire un `tryLock` avec délai ;
  - expliquer course, interblocage, famine et livelock, et un remède à chacun ;
  - dire ce qui reste déterministe dans un stream parallèle, et ce qu'exige `reduce`.
- [ ] Tu sais écrire sans aide :
  - un calcul découpé et fusionné ;
  - un producteur / consommateur avec arrêt propre ;
  - des virements sans interblocage ;
  - une barrière entre des phases.
- [ ] p03 et p07 ont été refaits **depuis un dossier vide**, 2 à 3 semaines plus tard.
