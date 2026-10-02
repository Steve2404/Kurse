# Drill de rappel 3 — Écrire, chaîner, relancer

> Première fois ? Lis d'abord le mode d'emploi [`ch11_exceptions/PARCOURS.md`](../../PARCOURS.md).

**Chrono cible :** 12 min, puis 6 min.

**Règles :**
- Tout se fait de mémoire, **imports compris**.
- Crée la classe **`Recall03`** dans le paquet `ch11_exceptions.drills.r03_custom`.
- Dans le même fichier, crée deux classes package-private :
  - **`ConfigException extends Exception`**, avec les **quatre** constructeurs `()`, `(String message)`, `(Throwable cause)` et `(String message, Throwable cause)` ;
  - **`QuotaExceededException extends RuntimeException`** :
    - construite avec `(int used, int limit)`, message `quota <used>/<limit>` ;
    - `int excess()` rend `used - limit`.
- Dans `Recall03`, deux méthodes :
  - `static void load(String name) throws ConfigException` : lève une `IOException("disque plein")`, l'attrape, et lève `new ConfigException("chargement de " + name, e)` ;
  - `static void retry(String name) throws ConfigException` : appelle `load`, attrape `Exception e`, et fait `throw e;`.

## Défis

- ☐ **D01.** Affiche `getMessage()` de :
  - `new ConfigException()` ;
  - `new ConfigException("cle absente")` ;
  - `new ConfigException(new IllegalStateException("etat"))`.
  → `D01 : null | cle absente | java.lang.IllegalStateException: etat`
- ☐ **D02.** `load("app.yml")`. Attrape l'exception et affiche son message, puis ` <- `, puis le nom simple et le message de la cause.
  → `D02 : chargement de app.yml <- IOException: disque plein`
- ☐ **D03.** `outer = new RuntimeException("haut", new IllegalArgumentException("milieu", new ArithmeticException("bas")))`. Descends les `getCause()` jusqu'à la racine, en comptant la profondeur.
  → `D03 : bas a la profondeur 2`
- ☐ **D04.** `late = new IllegalStateException("tard")`, puis `late.initCause(new ArithmeticException("origine"))`. Affiche le message de la cause.
  → `D04 : origine`
- ☐ **D05.** `retry("db.yml")` ; affiche le message attrapé.
  → `D05 : chargement de db.yml`
- ☐ **D06.** `throw new QuotaExceededException(120, 100)` ; attrape-la. Affiche le message, `excess()`, puis `instanceof RuntimeException`.
  → `D06 : quota 120/100 ; depassement 20 ; true`

## Expériences (hors sortie attendue)

1. Dans `retry`, réaffecte `e = new Exception();` avant `throw e;`. Pourquoi faut-il alors `throws Exception` ?
2. Appelle `initCause` deux fois sur la même exception. Que se passe-t-il ?
3. Affiche `new ConfigException("x")` (son `toString()`) : pourquoi le nom de **paquet** y apparaît-il ?
4. Une classe qui étend `Throwable` directement : est-elle vérifiée ?
5. Dans une classe à part, affiche une même exception de trois façons : `System.out.println(e)`, `e.getMessage()` et `e.printStackTrace()`. Où sort la 3e ? Que contient-elle de plus ?
6. `catch (IllegalStateException | IllegalArgumentException e) { e = new IllegalStateException(); }` : quelle erreur ? Et dans un `catch` à un seul type ?

## Sortie attendue complète

```
D01 : null | cle absente | java.lang.IllegalStateException: etat
D02 : chargement de app.yml <- IOException: disque plein
D03 : bas a la profondeur 2
D04 : origine
D05 : chargement de db.yml
D06 : quota 120/100 ; depassement 20 ; true
```

## Carte mémoire (à lire **après** le drill)

<details><summary>Ouvrir la carte</summary>

- **Vérifiée** → `extends Exception` ; **non vérifiée** → `extends RuntimeException`.
- **Les 4 constructeurs habituels :** `()`, `(String)`, `(Throwable)`, `(String, Throwable)`. Ils ne sont **pas hérités** : il faut les écrire.
- **`super(cause)`** seul : `getMessage()` vaut `cause.toString()`.
- **`toString()`** = nom **complet** + `": "` + message (ou le nom seul, si le message est `null`).
- **Afficher une exception :** `println(e)` (= `toString()`), `getMessage()`, ou `printStackTrace()` (sur `System.err`, avec la pile d'appels et les `Caused by:`).
- **Le paramètre d'un multi-catch** est implicitement `final` (pas de réaffectation) ; celui d'un `catch` à un seul type peut être réaffecté (mais ne le fais pas).
- **Chaîner :** `new X(message, cause)` ou `initCause` (une seule fois). `getCause()` remonte la chaîne.
- **Relance précise (Java 7) :** `catch (Exception e) { throw e; }` relance seulement ce que le `try` peut lever. Il faut que `e` reste effectivement finale.

</details>
