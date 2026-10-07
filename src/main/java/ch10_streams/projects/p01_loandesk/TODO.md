# Projet 1 — Le guichet de prêt d'une bibliothèque

> Première fois ? Lis d'abord le mode d'emploi [`ch10_streams/PARCOURS.md`](../../PARCOURS.md) : comment lire cette fiche, lancer `Check`, quoi faire en cas de blocage.
>
> **Bloqué sur une étape ?** [`INDICES.md`](INDICES.md) donne deux indices repliés par étape, sans code. **Étape finie ?** [`solution/CORRIGE.md`](solution/CORRIGE.md) donne, étape par étape, le code de l'étape, les **réponses aux questions** et le résultat exact des **expériences**. N'ouvre que la section de l'étape que tu viens de faire.

**API visée :** `Optional`, `OptionalInt`, `OptionalDouble`  
**Ce qui est donné :** `Data.java` (les données brutes) et `Check.java` (le correcteur). C'est tout.  
**Ce que TU crées :** tous les fichiers `.java` du programme, dans ce paquet `ch10_streams.projects.p01_loandesk`. Les records, les classes, l'interface, les méthodes et le `main`. Tu choisis les noms, sauf un : la classe qui contient `main` s'appelle **`LoanDesk`**, parce que c'est elle que `Check` lance.

**Pour vérifier :** lance `Check.java`. Il exécute ton `main`, compare ta sortie à la sortie attendue (en bas de ce fichier) et te montre la première ligne fausse. Il lit aussi tes sources : il liste les méthodes d'`Optional` que tu n'as pas encore utilisées et refuse tout appel à `Optional.get()`.  
Ne regarde `solution/` qu'à la fin.

---

## Le problème

Le guichet charge les livres et les membres, puis exécute les commandes de `Data.COMMANDS` dans l'ordre. Chaque commande affiche une ou plusieurs lignes. Une commande impossible affiche `REFUS : <raison>` et n'arrête pas les suivantes.

Les données :
- `Data.BOOKS` : `isbn;titre;auteur;exemplaires disponibles`
- `Data.MEMBERS` : `id;nom;email`. L'email peut manquer.
- `Data.COMMANDS` : `EMPRUNT`, `RETOUR`, `CONTACT`, `INFO`, `BILAN`, plus une commande inconnue.

Règle du projet : **aucun `null` ne circule.** Toute recherche qui peut échouer rend un `Optional`.

---

## Tableau de bord

Coche au fur et à mesure. Chaque étape contient sa règle, les lignes qu'elle doit produire et ses contraintes : tout est au même endroit.

### ☐ Étape 1 — Concevoir le modèle (sans écrire de logique)

Sur papier d'abord : quels types ? record, classe, interface ? quels champs ? dans quels fichiers ?

- Un **livre** vient d'une ligne de `Data.BOOKS`. Son nombre d'exemplaires change à chaque emprunt et à chaque retour. Si tu fais du livre un record (immuable), où ranges-tu ce nombre ?
- Un **membre** a un email **facultatif**. Deux pièges dans les vraies données :
  - `"M2;Hugo;".split(";")` ne rend que **2** morceaux, car `split` jette les champs vides de la fin ;
  - `"M4;Tom;  "` : un email fait d'espaces n'est pas un email.
- Le reste du programme lit l'email comme un `Optional<String>`. Règle de conception : `Optional` sert de **type de retour**, pas de champ ni de paramètre. Tu ne stockes donc pas d'`Optional` : tu le fabriques dans une méthode, en une seule chaîne et **sans `if`**.
- Il te faudra aussi un **emprunt** (qui a pris quoi) et un **retour** (qui, quel livre, combien de jours de retard, quelle pénalité), car le BILAN en a besoin.
- **Les montants sont en centimes**, dans un `int` : 50 centimes par jour, plafond 1000. Pour l'affichage `1.50`, écris une petite méthode avec `/` et `%` (chapitre 2) et `String.format("%d.%02d", …)` (chapitre 4).
  - **Question :** pourquoi ne pas utiliser un `double` (0.50, 10.00) ? Pense à l'arrondi et à l'affichage.

### ☐ Étape 2 — Une interface de recherche de livres

- Crée une **interface** avec deux recherches abstraites. Chacune rend un `Optional` :
  - par isbn (exact) ;
  - par titre (sans tenir compte des majuscules).
- Ajoute une méthode **`default`** qui cherche par isbn puis, **seulement si rien n'est trouvé**, par titre.
  - Une seule expression, sans `if`.
  - Le résultat doit **rester un `Optional`**, donc ni `orElse` ni `orElseGet`.
  - Quelle méthode d'`Optional` (Java 9) fait exactement ça ? Pourquoi prend-elle un `Supplier` et pas une valeur ?
- Ton guichet implémente cette interface.

### ☐ Étape 3 — L'état du guichet et ses recherches

- **Ce que le guichet garde :**
  - les membres par id ;
  - les livres par isbn, **dans l'ordre de `Data`**, car le BILAN en dépend. Quelle `Map` garde l'ordre d'insertion ?
  - les exemplaires disponibles ;
  - les emprunts en cours ;
  - une **file d'attente par livre** (premier arrivé, premier servi) ;
  - l'historique des retours.
- **Les recherches à écrire :**
  - « membre par id » rend un `Optional`. Attention, `Map.get` rend `null` quand la clé manque ;
  - « l'emprunt de tel membre pour tel livre » rend un `Optional`.
- **Le chargement :** un constructeur ou une fabrique qui lit `Data.BOOKS` et `Data.MEMBERS`.

### ☐ Étape 4 — `EMPRUNT <membre> <isbn>`

Les refus, dans cet ordre :
```
REFUS : membre inconnu M9
REFUS : livre inconnu B7
REFUS : Lea a deja Dune
```
- **Un refus est une réponse normale, pas une erreur.** Ta méthode rend la ligne à afficher : soit le résultat, soit `REFUS : …`.
  - **Contrainte :** les deux premiers refus s'écrivent **sans `if`**, en imbriquant tes recherches : `recherche membre` → `map(…)` → `recherche livre` → `map(…)` → `orElse("REFUS : …")`.
  - **Conception (chapitre 8) :** EMPRUNT et RETOUR ont tous les deux besoin d'un membre **et** d'un livre. Écris **une** méthode « avec membre et livre » qui reçoit le traitement à faire sous forme de `BiFunction<…>`, et qui gère les deux refus une seule fois.
- Pour « a déjà », tu ne **lis** pas l'emprunt trouvé, tu testes seulement s'il existe. C'est le seul endroit du projet où `isPresent()` est le bon outil.

Plus d'exemplaire : le membre entre dans la file du livre.
```
ATTENTE : Ines en position 1 pour Le Petit Prince
```
Sinon :
```
OK : Lea emprunte Dune (reste 2)
```

### ☐ Étape 5 — La pénalité de retard

- Jusqu'à 3 jours de retard : rien.
- Au-delà : 0,50 par jour **au-delà des 3**, plafonné à 10,00.
- La méthode rend un `Optional<Integer>` (centimes), **vide** quand il n'y a pas de pénalité. Ici tu construis l'`Optional` toi-même, avec `Optional.empty()` et `Optional.of(...)`.
- Calcule à la main **avant** de coder : 0 → vide, 6 → 150, 20 → 850, 40 → 1000.

> Pourquoi un `Optional` vide plutôt que `0` ?

### ☐ Étape 6 — `RETOUR <membre> <isbn> <jours de retard>`

S'il n'y a pas d'emprunt :
```
REFUS : Lea n'a pas Dune
```
Sinon :
```
RETOUR : Hugo rend Le Petit Prince, penalite 1.50
RETOUR : Lea rend Dune, sans penalite
```
- La fin de la ligne s'écrit en **une expression** sur l'`Optional` de l'étape 5.
- Le montant s'affiche avec ta méthode de l'étape 1 (centimes → `1.50`).
- **Le stock d'un livre :** chaque livre chargé a forcément un stock. S'il manquait, ce serait un **bug** du programme, pas un refus. Lis-le avec `orElseThrow(Supplier)` en fournissant une `IllegalStateException` avec un message clair. Ce cas ne se produit jamais avec ces données : c'est un garde-fou.

Ensuite, si quelqu'un attend ce livre, le **premier de la file** l'emprunte aussitôt (même ligne `OK : …` qu'à l'étape 4), puis on le prévient :
```
AVIS ines@biblio.org : Le Petit Prince vous attend
AVIS par courrier a Tom : Le Petit Prince vous attend
```
- **Une seule chaîne `Optional`, qui finit par `ifPresent`, pour trois actions :** la file peut ne pas exister, `poll` rend `null` si elle est vide, et ta recherche de membre rend **déjà** un `Optional`.
  - Pourquoi `map` ne convient pas pour l'étape « retrouver le membre » ? Quel type obtiendrais-tu ?
- **Le texte de l'avis :** `orElseGet`, pas `orElse`.
  - Qu'est-ce qu'`orElse` calculerait pour rien quand l'email existe ?

### ☐ Étape 7 — `CONTACT <membre>`

```
CONTACT Lea : lea@mail.fr
CONTACT Hugo : par courrier
REFUS : membre inconnu M7
```
- Ici, pas d'exception. Tu traites le cas présent **et** le cas absent en **un seul appel** sur l'`Optional` du membre.

### ☐ Étape 8 — `INFO <isbn ou titre>`

```
INFO B2 Fondation (Asimov) : 2 disponible(s), 0 en attente
REFUS : aucun livre pour Silmarillion
```
- Utilise la méthode `default` de l'étape 2.
- Attention, un titre peut contenir des espaces : `INFO Le Petit Prince`.
- Un livre introuvable donne la ligne `REFUS`, toujours sans `if`.
- **Le nombre en attente :** la file d'un livre que personne n'a jamais attendu peut ne pas exister du tout. Écris-le avec une chaîne `Optional`, sans `if` ni `getOrDefault`.

### ☐ Étape 9 — `BILAN` (4 lignes)

**Ligne 1 :**
```
BILAN : 3 penalite(s), total 20.00
```
- Seuls les retours **avec** pénalité comptent.

**Ligne 2 :**
```
BILAN : retard max 40 jour(s) (Tom), moyen 16.5 jour(s)
```
- Elle porte sur **tous** les retours.
- **Le max en jours :** il vient d'un `IntStream`, donc d'un `OptionalInt`, lu avec `getAsInt()`.
- **Le nom :** `OptionalInt` n'a ni `map` ni `filter`, il ne sait rien de Tom. Il te faut un deuxième max, sur les retours eux-mêmes.
- **La moyenne :** un `OptionalDouble`.
- **Sans aucun retour :** la ligne devient `BILAN : aucun retour`. Teste avec `isEmpty()`, puis lis la valeur avec `orElseThrow()` **sans argument** (le `get()` moderne).

**Ligne 3 :**
```
BILAN : joignables par email [lea@mail.fr, ines@biblio.org]
```
- Tu dois passer de `Stream<Optional<String>>` à `Stream<String>`, sans `filter(isPresent)` ni `get`. Utilise une référence de méthode d'`Optional` (Java 9) dans `flatMap`.

**Ligne 4 :**
```
BILAN : 0 emprunt(s) en cours
```

### ☐ Étape 10 — Le `main` de `LoanDesk`

- Il charge les données, puis exécute chaque commande de `Data.COMMANDS`.
- Toute autre commande produit `REFUS : commande inconnue RENOUVELER`.
- Chaque commande affiche sa réponse : un refus n'arrête pas les suivantes. Une `switch` (chapitre 3) choisit le traitement.
- Ensuite, lance `Check.java`.

---

## Checklist API (vérifiée par `Check`)

| Méthode | Étape où elle a sa place | ☐ |
|---|---|---|
| `Optional.ofNullable` | 1, 3 | ☐ |
| `Optional.of` / `Optional.empty` | 5 | ☐ |
| `map` / `filter` | 1, 6 | ☐ |
| `flatMap` | 6 | ☐ |
| `or` | 2 | ☐ |
| `orElse` | 6, 7 | ☐ |
| `orElseGet` | 6 | ☐ |
| `orElseThrow(Supplier)` | 6 (garde-fou du stock) | ☐ |
| `orElseThrow()` | 9 | ☐ |
| `ifPresent` | 6 | ☐ |
| `ifPresentOrElse` | 7 | ☐ |
| `isPresent` | 4 | ☐ |
| `isEmpty` | 9 | ☐ |
| `Optional::stream` | 9 | ☐ |
| `OptionalInt` + `getAsInt` | 9 | ☐ |
| `OptionalDouble` | 9 | ☐ |
| ~~`Optional.get()`~~ | **interdit** | — |

---

## Sortie attendue complète

C'est le contrat exact que vérifie `Check` :

```
OK : Lea emprunte Dune (reste 2)
REFUS : Lea a deja Dune
REFUS : membre inconnu M9
REFUS : livre inconnu B7
OK : Hugo emprunte Le Petit Prince (reste 0)
ATTENTE : Ines en position 1 pour Le Petit Prince
ATTENTE : Tom en position 2 pour Le Petit Prince
RETOUR : Hugo rend Le Petit Prince, penalite 1.50
OK : Ines emprunte Le Petit Prince (reste 0)
AVIS ines@biblio.org : Le Petit Prince vous attend
RETOUR : Lea rend Dune, sans penalite
REFUS : Lea n'a pas Dune
CONTACT Hugo : par courrier
CONTACT Tom : par courrier
CONTACT Lea : lea@mail.fr
REFUS : membre inconnu M7
INFO B2 Fondation (Asimov) : 2 disponible(s), 0 en attente
INFO B4 Le Petit Prince (Saint-Exupery) : 0 disponible(s), 1 en attente
REFUS : aucun livre pour Silmarillion
RETOUR : Ines rend Le Petit Prince, penalite 8.50
OK : Tom emprunte Le Petit Prince (reste 0)
AVIS par courrier a Tom : Le Petit Prince vous attend
RETOUR : Tom rend Le Petit Prince, penalite 10.00
REFUS : commande inconnue RENOUVELER
BILAN : 3 penalite(s), total 20.00
BILAN : retard max 40 jour(s) (Tom), moyen 16.5 jour(s)
BILAN : joignables par email [lea@mail.fr, ines@biblio.org]
BILAN : 0 emprunt(s) en cours
```
