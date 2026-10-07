# Drill de rappel 1 — Les threads

> Première fois ? Lis d'abord le mode d'emploi [`ch13_concurrency/PARCOURS.md`](../../PARCOURS.md).

**Chrono cible :** 12 min, puis 6 min.

**Règles :**
- Tout se fait de mémoire.
- Crée la classe **`Recall01`** dans le paquet `ch13_concurrency.drills.r01_threads`, avec un champ `static String result`.
- Dans le même fichier, crée la classe package-private **`Worker extends Thread`**. Son `run()` met dans `result` : `"travail fait par " + ("Thread-N" si getName() commence par "Thread-", sinon getName())`.
- Une case `String[] name = new String[1]` sert aux échanges entre threads.

**Les notions de ce drill ont été apprises dans :** projet 1 (étapes 2 à 4). Si un défi te semble totalement inconnu, ce drill arrive trop tôt : refais d'abord la leçon de ce projet.

<details><summary><b>Comment faire ce drill, concrètement</b> (à lire la 1re fois)</summary>

1. **Note l'heure** de départ (montre ou téléphone).
2. **Crée la classe** : clic droit sur le dossier `r01_threads` → **New** → **Java Class** → `Recall01`. S'il faut d'autres classes, place-les comme le disent les **Règles** ci-dessus ; si elles ne précisent rien, écris-les dans le même fichier, sous `Recall01`, sans `public`.
3. **Écris `main`**, puis **une ligne par défi**. La ligne qui suit la flèche `→` est **exactement** ce que ton programme doit afficher : `System.out.println("D01 : " + …);`, où les `…` sont des valeurs que **Java** calcule, jamais recopiées.
4. **Lance `Recall01`** avec la flèche verte, pour voir tes lignes.
5. **Bloqué plus de 3 minutes sur un défi ?** Écris un commentaire `// D03 : ✗` à sa place, et passe au suivant.
6. **Lance `Check.java`** (flèche verte). Une ligne `[FAIL]` te montre `attendu` et `obtenu`.
7. **Ensuite seulement**, ouvre la **carte mémoire** tout en bas (en aperçu Markdown, clique sur le triangle « Ouvrir la carte »), relis tes ✗, et fais les **expériences** (écris la ligne, compile, lis le message, efface la ligne).
8. **Note** la date, ton temps et tes ✗ dans le tableau de [`drills/README.md`](../README.md).

</details>

## Défis

- ☐ **D01.** `new Thread(() -> name[0] = Thread.currentThread().getName(), "t1")`, puis `start()` et `join()`.
  → `D01 : t1`
- ☐ **D02.** Un `Worker`, avec `start()` et `join()`. Affiche `result`, puis l'état du thread.
  → `D02 : travail fait par Thread-N TERMINATED`
- ☐ **D03.** Un `Runnable who` qui note le nom du thread courant. Appelle d'abord `who.run()`, puis passe-le à `new Thread(who, "t3")` avec `start()` et `join()`.
  → `D03 : main t3`
- ☐ **D04.** Un thread qui fait `Thread.sleep(60_000)`. S'il est interrompu, il note `"interrompu, drapeau " + isInterrupted()`.
  1. Note son état **avant** `start()` ;
  2. après `start()`, attends `TIMED_WAITING` (boucle avec `Thread.sleep(1)`) ;
  3. `join(20)`, puis `isAlive()` ;
  4. `interrupt()`, puis `join()`.
  
  Affiche les deux états, `isAlive` après `join(20)`, puis l'état et `isAlive` à la fin.
  → `D04 : NEW TIMED_WAITING true TERMINATED false`
- ☐ **D05.** Affiche le message noté par ce thread.
  → `D05 : interrompu, drapeau false`
- ☐ **D06.** `Thread.currentThread().interrupt()`, puis `Thread.interrupted()` deux fois.
  → `D06 : true false`
- ☐ **D07.** Un thread vide :
  1. `setDaemon(true)` et `setPriority(Thread.MAX_PRIORITY)`, puis `start()` et `join()` ;
  2. puis un 2e `start()`, à attraper.
  
  Affiche `isDaemon()`, `getPriority()`, `Thread.NORM_PRIORITY`, puis le nom simple de l'exception.
  → `D07 : true 10 5 IllegalThreadStateException`
- ☐ **D08.** `wait` et `notifyAll`, avec un objet `mailbox` et une case `String[] mail` :
  1. un thread `reader`, dans `synchronized (mailbox)`, fait `mailbox.wait()` tant que `mail[0] == null` ;
  2. `main` attend l'état `WAITING` et le note ;
  3. dans `synchronized (mailbox)`, `main` remplit `mail[0]` puis fait `notifyAll()` ;
  4. `join()` ;
  5. enfin `mailbox.notify()` **hors** de tout `synchronized`, à attraper.

  Affiche l'état noté, l'état final, puis le nom de l'exception.
  → `D08 : WAITING TERMINATED IllegalMonitorStateException`

## Expériences (hors sortie attendue)

1. Appelle `setDaemon(true)` **après** `start()` : que se passe-t-il ?
2. Dans D04, que vaut `getState()` juste après `start()` ? Est-ce toujours la même valeur ?
3. Pourquoi faut-il attraper `InterruptedException` autour de `Thread.sleep` (vérifiée ou non) ?
4. Un thread non daemon qui boucle sans fin : le programme se termine-t-il quand `main` finit ?

## Sortie attendue complète

```
D01 : t1
D02 : travail fait par Thread-N TERMINATED
D03 : main t3
D04 : NEW TIMED_WAITING true TERMINATED false
D05 : interrompu, drapeau false
D06 : true false
D07 : true 10 5 IllegalThreadStateException
D08 : WAITING TERMINATED IllegalMonitorStateException
```

## Carte mémoire (à lire **après** le drill)

<details><summary>Ouvrir la carte</summary>

| État | Quand |
|---|---|
| `NEW` | créé, pas encore `start()` |
| `RUNNABLE` | en cours d'exécution (ou prêt) |
| `BLOCKED` | attend un verrou `synchronized` |
| `WAITING` | `join()`, `wait()` sans délai (rendu par `notify()`/`notifyAll()`) |
| `TIMED_WAITING` | `sleep(n)`, `join(n)`, `wait(n)` |
| `TERMINATED` | `run()` est fini |

- **`start()`** crée un nouveau thread qui exécute `run()`. **`run()`** appelé directement s'exécute dans le thread courant.
- Un thread ne se démarre qu'**une** fois : sinon `IllegalThreadStateException`.
- **`interrupt()`** pose un drapeau. `sleep`, `join` et `wait` lèvent alors `InterruptedException` (vérifiée) et **effacent** le drapeau.
  - `isInterrupted()` lit le drapeau ;
  - `Thread.interrupted()` (statique) le lit **et l'efface**.
- **Les daemons** n'empêchent pas la JVM de s'arrêter ; `setDaemon` doit être appelé **avant** `start()`.
- **`wait()`/`notify()`/`notifyAll()`** (méthodes d'`Object`) : il faut **tenir le verrou** de l'objet (`synchronized`), sinon `IllegalMonitorStateException`. `wait()` **rend** le verrou pendant l'attente. On l'appelle toujours dans une boucle `while (condition)`.
- **Les priorités** vont de 1 à 10, avec 5 par défaut ; ce n'est qu'un indice pour l'ordonnanceur.

</details>
