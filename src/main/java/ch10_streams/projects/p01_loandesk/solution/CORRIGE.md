# Projet 1 — Corrigé étape par étape

> **Quand le lire :** ouvre **seulement la section de l'étape** que tu viens de terminer, pour comparer ton code et tes réponses. Le programme complet est dans ce dossier : `Book`, `Member`, `Catalog` et `LoanDesk`. Les commentaires `TODO n` de la solution renvoient aux étapes.
>
> Les messages et les valeurs ci-dessous ont été obtenus en direct avec **JDK 17** (`javac` / `java` 17.0.18).

---

## Étape 1 — Concevoir le modèle

**Le code :** [`Book.java`](Book.java), [`Member.java`](Member.java), et les records `Loan` et `Return` de [`LoanDesk.java`](LoanDesk.java).

**Le stock hors du record :** un `Book` est immuable, et son stock change sans cesse. On garde donc le stock à part, dans une `Map` du guichet. Le livre reste une donnée **fixe**, partageable.

**Les deux pièges de l'email :**
- `"M2;Hugo;".split(";")` rend **2** morceaux (vérifié) : `split` jette les chaînes vides de la **fin**. Lire `p[2]` lèverait `ArrayIndexOutOfBoundsException`, d'où `p.length > 2 ? p[2] : null`.
- `"M4;Tom;  "` donne 3 morceaux, mais le 3e n'est fait que d'espaces. Le `filter(e -> !e.isBlank())` le rejette.

**`Optional` comme type de retour, pas comme champ :** `Optional` n'est pas sérialisable, et il ajoute un objet par champ. Il est conçu pour dire « ce **résultat** peut manquer ». On stocke la valeur brute (`null` possible), et c'est la méthode `email()` qui fabrique l'`Optional`.

**Question — pourquoi pas un `double` pour les montants ?** Les `double` sont binaires : 0.1 n'a pas de valeur exacte. Vérifié : 0.1 + 0.1 + 0.1 donne `0.30000000000000004`. Additionner des pénalités en `double` accumule ces erreurs, et l'affichage `1.5` au lieu de `1.50` demanderait un format. En centimes (`int`), tout est **exact**, et `String.format("%d.%02d", c / 100, c % 100)` affiche toujours 2 chiffres.

---

## Étape 2 — Une interface de recherche de livres

**Le code :** [`Catalog.java`](Catalog.java).

**`Optional.or(Supplier)`** (Java 9) : si l'`Optional` est vide, il appelle le `Supplier`, qui rend **un autre `Optional`**. Le résultat reste un `Optional`, alors qu'`orElse` ou `orElseGet` le « déballeraient ».

**Pourquoi un `Supplier` et pas une valeur ?** Pour la **paresse** : `byTitle` parcourt tout le catalogue. Avec un `Supplier`, il n'est exécuté **que si** `byIsbn` n'a rien trouvé. Une valeur serait calculée à l'avance, pour rien dans la plupart des cas.

---

## Étape 3 — L'état du guichet et ses recherches

**Le code :** les champs, le constructeur, `byIsbn`, `byTitle`, `findMember` et `findLoan` de `LoanDesk`.

**`LinkedHashMap`** garde l'**ordre d'insertion**, celui de `Data`. Une `HashMap` donnerait un ordre imprévisible pour la liste des emails du BILAN.

**`Optional.ofNullable(map.get(k))`** : c'est le pont entre l'ancienne API (qui rend `null`) et les `Optional`.

**`stream().filter(…).findFirst()`** rend directement un `Optional`.

---

## Étape 4 — `EMPRUNT <membre> <isbn>`

**Le code :** `withMemberAndBook`, `borrow` et `lend`.

**Les refus sans `if` :** les deux `map` imbriqués ne s'exécutent que si le membre, **puis** le livre, existent. Chaque `orElse` fournit le refus du niveau qui a échoué. Le **premier** absent donne le message.

**Une `BiFunction` pour le traitement :** EMPRUNT et RETOUR partagent la vérification « membre et livre ». Seule l'**action** change : `this::borrow`, ou la lambda du retour.

**`isPresent()` ici, et seulement ici :** on ne lit pas l'emprunt, on teste son **existence**. Ailleurs, `isPresent()` suivi de `get()` est un anti-motif : `map`, `orElse` et `ifPresent` le remplacent.

---

## Étape 5 — La pénalité de retard

**Le code :** la méthode `fee`.

**À la main :**
- 0 jour donne un `Optional` vide ;
- 6 jours donnent (6 − 3) × 50 = **150** ;
- 20 jours donnent 17 × 50 = **850** ;
- 40 jours donnent 37 × 50 = 1850, plafonné à **1000**.

**Question — pourquoi un `Optional` vide plutôt que `0` ?** « Pas de pénalité » et « une pénalité de 0,00 » sont **deux situations différentes**. L'affichage dit `sans penalite` dans le premier cas. Avec `0`, il faudrait un `if (fee == 0)`, et la règle « 0 veut dire absent » serait cachée dans le code. Avec `Optional`, l'absence est **explicite dans le type**.

---

## Étape 6 — `RETOUR <membre> <isbn> <jours de retard>`

**Le code :** les deux méthodes `giveBack` et `notice`.

**`orElseThrow(Supplier)` pour un invariant :** un livre chargé a **toujours** un stock. S'il manquait, ce serait un **bug**, pas un cas métier. On veut que le programme s'arrête net, avec un message clair, plutôt que de continuer avec une valeur inventée.

**La chaîne du suivant de file :** `Optional.ofNullable(file).map(Deque::pollFirst).flatMap(this::findMember).ifPresent(…)`.
- La file peut ne pas exister : `ofNullable`.
- `pollFirst` rend `null` si elle est vide, et `map` transforme ce `null` en `Optional` vide.
- `findMember` rend **déjà** un `Optional` : il faut `flatMap`.

**Question — pourquoi pas `map` pour retrouver le membre ?** `map(this::findMember)` donnerait un **`Optional<Optional<Member>>`** : un `Optional` dans un `Optional`. Vérifié sur un exemple réduit, l'affecter à un `Optional<Integer>` ne compile pas : `error: incompatible types: inference variable U has incompatible bounds`. `flatMap` « aplatit » les deux niveaux.

**Question — `orElse` contre `orElseGet` pour l'avis :** `orElse(x)` reçoit une **valeur déjà calculée**. L'argument est évalué **avant** l'appel, même si l'email existe. Vérifié avec un compteur : `e.orElse(mk.get())` sur un `Optional` **plein** appelle quand même `mk` (compteur à 1), alors que `e.orElseGet(mk)` ne l'appelle pas (compteur toujours à 1). Ici, on construirait le texte « par courrier » pour rien.

---

## Étape 7 — `CONTACT <membre>`

**Le code :** la méthode `contact`.

**`ifPresentOrElse(action, actionSiVide)`** (Java 9) traite les deux cas en un appel. On n'a pas besoin de valeur de retour : on affiche directement.

---

## Étape 8 — `INFO <isbn ou titre>`

**Le code :** la méthode `info`.

**`find(query)`** utilise la méthode `default` de l'interface, qui cherche par isbn, puis par titre. `INFO Le Petit Prince` passe par le titre : il faut tout le texte après `INFO `, d'où `substring`.

**Le nombre en attente :** `Optional.ofNullable(waitlists.get(isbn)).map(Deque::size).orElse(0)`. Pour Fondation, personne n'a jamais attendu, la file n'existe pas, et le résultat vaut 0.

---

## Étape 9 — `BILAN` (4 lignes)

**Le code :** la méthode `report`.

**Les `Optional` primitifs :**
- `IntStream.max()` rend un `OptionalInt`, lu avec `getAsInt()` ;
- `average()` rend un `OptionalDouble`.

Ces types n'ont **ni `map` ni `filter`** : ils transportent un nombre, pas un objet. Pour savoir **qui** a le plus grand retard, il faut un `max` sur les `Return` eux-mêmes, qui rend un `Optional<Return>`.

**`orElseThrow()` sans argument :** il fait la même chose que `get()`, avec un nom qui dit ce qui arrive si l'`Optional` est vide (`NoSuchElementException`). On l'appelle **après** avoir testé `isEmpty()`.

**`flatMap(Optional::stream)`** (Java 9) : chaque `Optional` devient un stream de **0 ou 1** élément. `flatMap` les met bout à bout, et les absents disparaissent naturellement, sans `filter(isPresent)` ni `get()`.

---

## Étape 10 — Le `main` de `LoanDesk`

**Le code :** `execute` et `main`.

**Un `switch` en flèche** choisit le traitement. Chaque commande affiche sa réponse, et un refus est une ligne comme une autre : il n'arrête rien. `RENOUVELER` tombe dans le `default`.
