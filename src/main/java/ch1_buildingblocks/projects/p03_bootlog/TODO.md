# Projet 3 — Le journal de démarrage d'un serveur

> Première fois ? Lis d'abord le mode d'emploi [`ch1_buildingblocks/PARCOURS.md`](../../PARCOURS.md) : comment lire cette fiche, lancer `Check`, quoi faire en cas de blocage.

**Notions visées (chapitre 1) :**
- **l'ordre d'initialisation** d'un objet : champs et blocs d'initialisation dans l'ordre du texte, **puis** le constructeur ;
- les **valeurs par défaut** des champs, et le piège de la lecture anticipée ;
- la **portée** des variables : locale, de bloc, d'instance, de classe ;
- le **masquage** d'un champ par une variable locale ;
- `var` et `final` ;
- le **ramasse-miettes** (quand un objet devient-il éligible ?).

**Ce qui est donné :** `Check.java`.

**Ce que TU crées :** tout le programme, dans le paquet `ch1_buildingblocks.projects.p03_bootlog`. La classe du `main` s'appelle **`BootLog`**. Le serveur est une **autre** classe, dans le même fichier ou dans le sien.

**Règle du crescendo :** seulement le chapitre 1, plus `+ - * / %`. Pas de `if`, pas de boucle, pas de `this(...)` entre constructeurs ni de bloc `static { }` (chapitre 6). `Check` les refuse.

---

## Le problème

Quand un serveur démarre, l'équipe veut un **journal numéroté** de chaque étape de construction de l'objet, pour comprendre dans quel ordre Java initialise tout.

La sortie attendue **est** ce journal. Ton travail consiste à placer les champs, les blocs et le constructeur **de sorte que Java produise exactement cet ordre**.

Tu n'as **pas le droit de tricher** en écrivant toutes les lignes dans le constructeur. `Check` exige de vrais blocs d'initialisation et un champ initialisé par un appel de méthode. Et les valeurs `null` et `0` du journal doivent venir **de Java**, pas d'un texte écrit à la main.

---

## Tableau de bord

### ☐ Étape 1 — L'outil de journalisation

- La classe `BootLog` contient un **compteur d'étapes**.
  - **Contrainte :** ce compteur n'est **jamais initialisé explicitement**. Pourquoi vaut-il pourtant 0 au départ ?
- Une méthode « noter » augmente le compteur (`step = step + 1`, car `++` et `+=` sont au chapitre 2) et affiche `numéro. message`.
- Deux méthodes « journaliser puis rendre » (une pour un `int`, une pour un `String`) permettent d'**initialiser un champ en l'écrivant au journal** :
  ```
  1. Server : champ port = 8080
  ```

### ☐ Étape 2 — L'ordre d'initialisation : la classe `Server`

```
1. Server : champ port = 8080
2. Server : bloc A (port = 8080, name = null)
3. Server : champ name = alpha
4. Server : lecture anticipee de late = 0
5. Server : champ late = 42
6. Server : bloc B (early = 0, late = 42)
7. Server : constructeur debut (this.maxUsers = 0, parametre maxUsers = 50)
8. Server : constructeur fin (maxUsers = 50, serveur n 1)
```
**À la main d'abord.** Avant d'écrire une ligne, dessine l'ordre des champs, des deux blocs et du constructeur dans ta classe, de façon à reproduire ce journal.

**Les pièges à comprendre (et à expliquer en commentaire) :**
- **Ligne 2, `name = null`.** Le bloc A lit `name` **avant** sa déclaration.
  - **Expérience :** écris `name` tout seul dans le bloc A. Lis l'erreur de `javac`.
  - Comment le lire légalement ? Que vaut-il à ce moment-là, et pourquoi ?
- **Lignes 4 à 6, `early = 0`.** Le champ `early` est initialisé par un **appel de méthode** qui lit `late`, déclaré plus bas. Ici, pas d'erreur de compilation, mais la valeur lue n'est pas 42.
  - **Question :** pourquoi le compilateur refuse-t-il la lecture directe (ligne 2) mais accepte-t-il la lecture via une méthode ?
- **Ligne 7, `this.maxUsers = 0`.** Le paramètre du constructeur porte **le même nom** que le champ.
  - Au début du constructeur, que vaut le champ ? Et le paramètre ?
- **Le compteur de serveurs créés** est un champ **de classe** (`static`) de `BootLog`, incrémenté par le constructeur.

### ☐ Étape 3 — Deux objets, deux initialisations

```
--- 2e serveur ---
9. Server : champ port = 8080
...
16. Server : constructeur fin (maxUsers = 10, serveur n 2)
```
- Le `main` crée **deux** serveurs (50 puis 10 utilisateurs). Déclare-les avec **`var`**.
- **Question :** pourquoi tout le journal recommence-t-il pour le 2e objet, alors que le compteur d'étapes, lui, continue ?
- **Expériences :** pour chacune, lis l'erreur de `javac`, puis retire la ligne.
  1. `var x;`
  2. `var y = null;`
  3. `var a = 1, b = 2;`
  4. réaffecte un `var` déclaré avec un `Server` en lui donnant un `String`

### ☐ Étape 4 — La portée et le masquage

```
17. portee : port local = 9090, champ this.port = 8080
18. portee : dans le bloc, backup = 9091
19. portee : name local = local, champ this.name = alpha
```
- Une méthode du serveur déclare une variable **locale** `port` qui **masque** le champ. Affiche les deux.
- **Un bloc `{ }` à l'intérieur de la méthode** déclare une variable `backup`.
  - **Expérience :** utilise `backup` **après** l'accolade fermante. Que dit `javac` ?
- Même chose avec une variable locale `name`.

### ☐ Étape 5 — Le bilan et le ramasse-miettes

```
2 serveurs crees (attendu 2), 19 etapes journalisees
first et second designent le serveur de 10 utilisateurs
```
- Le nombre attendu est une variable locale **`final`**.
  - **Expérience :** essaie de la modifier. Lis l'erreur.
- **À la fin, fais `first = second;`.** Réponds en commentaire :
  - après cette ligne, combien d'objets `Server` sont **éligibles** au ramasse-miettes ? Lequel ?
  - et si on avait fait `first = null;` à la place ?
  - `System.gc()` garantit-il que l'objet sera détruit ?

---

## Checklist (vérifiée par `Check`)

| Élément | Étape | ☐ |
|---|---|---|
| `static int` (compteur jamais initialisé) | 1 | ☐ |
| au moins 2 blocs d'initialisation `{ … }` | 2 | ☐ |
| un champ initialisé par un appel de méthode (`int early = readLate();`) | 2 | ☐ |
| `this.name`, `this.maxUsers` | 2 | ☐ |
| `var` | 3 | ☐ |
| `this.port` (masquage) | 4 | ☐ |
| `final int` | 5 | ☐ |

---

## Sortie attendue complète

```
--- 1er serveur ---
1. Server : champ port = 8080
2. Server : bloc A (port = 8080, name = null)
3. Server : champ name = alpha
4. Server : lecture anticipee de late = 0
5. Server : champ late = 42
6. Server : bloc B (early = 0, late = 42)
7. Server : constructeur debut (this.maxUsers = 0, parametre maxUsers = 50)
8. Server : constructeur fin (maxUsers = 50, serveur n 1)
--- 2e serveur ---
9. Server : champ port = 8080
10. Server : bloc A (port = 8080, name = null)
11. Server : champ name = alpha
12. Server : lecture anticipee de late = 0
13. Server : champ late = 42
14. Server : bloc B (early = 0, late = 42)
15. Server : constructeur debut (this.maxUsers = 0, parametre maxUsers = 10)
16. Server : constructeur fin (maxUsers = 10, serveur n 2)
--- portee ---
17. portee : port local = 9090, champ this.port = 8080
18. portee : dans le bloc, backup = 9091
19. portee : name local = local, champ this.name = alpha
--- bilan ---
2 serveurs crees (attendu 2), 19 etapes journalisees
first et second designent le serveur de 10 utilisateurs
```
