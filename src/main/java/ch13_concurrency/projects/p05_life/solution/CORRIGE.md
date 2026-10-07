# Projet 5 — Corrigé étape par étape

> **Quand le lire :** ouvre **seulement la section de l'étape** que tu viens de terminer. Le programme complet est dans ce dossier.
>
> Les résultats ci-dessous ont été obtenus en direct avec **JDK 17** (17.0.18).

---

## Étape 1 — Les règles

**Le code :** `Life.java`. Les mêmes règles qu'au chapitre 4 (projet 3), mais sur une grille **torique** : les bords se rejoignent, grâce au modulo.

---

## Étape 2 — Les générations en parallèle

**Le principe :** chaque ouvrier calcule **sa bande** de lignes de la génération suivante, puis attend les autres à la barrière. Quand tous sont arrivés, l'action de barrière échange les grilles et note la population, puis tout le monde repart pour la génération suivante.

**Question — pourquoi un ouvrier ne peut-il pas lire `current` pendant l'échange ?** L'action de barrière s'exécute pendant que **tous** les ouvriers sont bloqués dans `await()`. Aucun ne repart avant la fin de l'action. Il n'y a donc personne pour lire `current` pendant l'échange : c'est la barrière elle-même qui sert de verrou.

**Question — un pool de 3 threads pour 4 parties ?** Le programme **se bloque**. Les 3 premiers ouvriers attendent à la barrière, et le 4e ne peut jamais démarrer : il attend qu'un thread du pool se libère, ce qui n'arrivera jamais. Vérifié avec un petit programme : au bout d'une seconde, `en attente 3, cassee false`, et rien ne bouge. Seul `shutdownNow()` (qui interrompt les threads) a permis de l'arrêter.

**La dernière ligne :** `parties 4, en attente 0, cassee false` : à la fin, personne n'attend, et la barrière n'a jamais été cassée.

---

## Étape 3 — La barrière cassée

**L'enchaînement :**
1. l'autre thread attend dans `await()` (`getNumberWaiting()` vaut 1) ;
2. `interrupt()` : son `await()` lance une `InterruptedException` (il note `interrompu`), et la barrière est **cassée** ;
3. `main` appelle `await()` à son tour : il reçoit tout de suite une `BrokenBarrierException`, sans attendre ;
4. `isBroken()` vaut `true` ; après `reset()`, la barrière est neuve, et `isBroken()` vaut `false`.
