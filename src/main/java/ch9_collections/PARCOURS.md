# Chapitre 9 (Collections and Generics) — Mode d'emploi

Lis ce fichier **en entier une fois** avant de commencer. Le fonctionnement est le même qu'aux chapitres 1 à 8 et 10 :
- des **projets** à construire de A à Z, pour **comprendre** ;
- des **drills** chronométrés, répétés à intervalles espacés, pour **retenir**.

---

## 1. Ce que tu vas faire

| | Projets (`projects/`) | Drills de rappel (`drills/`) |
|---|---|---|
| Combien | 7 | 10 |
| But | résoudre de vrais problèmes algorithmiques **en choisissant la bonne collection**, et écrire tes propres génériques | retrouver vite et sans aide |
| Durée | 2 à 4 h chacun | 8 à 12 min chacun |
| Combien de fois | une fois ; p04 et p07 refaits 2 à 3 semaines plus tard | 6 fois chacun (J0, J+1, J+3, J+7, J+14, J+30) |
| Aide autorisée | Javadoc, papier, réflexion | **aucune** pendant le drill |

---

## 2. La règle du crescendo : chapitres 1 à 9

**Tu as droit à :**
- les **chapitres 1 à 8** : le langage, les classes, les interfaces, les records, les types scellés, les lambdas, les références de méthode, `java.util.function` ;
- tout le **chapitre 9** :
  - **`Collection`** et ses méthodes communes (`removeIf`, `retainAll`, `forEach`…) ;
  - **`List`** (`ArrayList`, `LinkedList`), `ListIterator`, `subList` ;
  - **`Set`** (`HashSet`, `LinkedHashSet`, `TreeSet`) et **`NavigableSet`** ;
  - **`Queue`**, **`Deque`** (`ArrayDeque`, `LinkedList`), **`PriorityQueue`** ;
  - **`Map`** (`HashMap`, `LinkedHashMap`, `TreeMap`), **`NavigableMap`**, `merge` et `compute…`, `Map.Entry` ;
  - les fabriques immuables (`List.of`, `copyOf`…), `Arrays.asList`, la classe `Collections` ;
  - **`Comparable`**, **`Comparator`** et ses combinateurs ;
  - **les génériques** : classes, interfaces, records et méthodes génériques, bornes, jokers `?`, `extends`, `super`, effacement de type.

**Ce qui reste exclu, et ce qu'on fait à la place :**

| Notion | Chapitre | À la place, ici |
|---|---|---|
| `stream()`, `Stream`, `Collectors`, `IntStream` | 10 | des boucles, `merge`, `computeIfAbsent`, `removeIf`, `replaceAll` |
| `Optional` | 10 | `null` (`poll`, `get`, `floorKey` rendent `null`) |
| `String.lines()`, `chars()` | 10/11 | `split`, `charAt` |
| `try/catch`, `throw` | 11 | les cas d'erreur sont dans les **expériences**, à tester à part |
| `Locale`, `NumberFormat`, `DateTimeFormatter` | 11 | concaténation ; centimes en `long` |
| `parallel`, threads | 13 | — |

`Check` refuse ces notions. Le message est `[FAIL] API : interdit ici`.

**Le formatage :** pas de `%f`. Les montants sont en centimes (`long`) et affichés par une petite méthode `money`.

---

## 3. Par où commencer (aujourd'hui)

1. Lis ce fichier jusqu'au bout.
2. Ouvre `projects/p01_inventory/TODO.md` en aperçu Markdown.
3. Suis la section 5.

**L'ordre complet :**

```
p01 → r01 r02 r06
p02 → r03 r05
p03 → r04
p04 → r08 r09
p05 → r07
p06 → (révision r03 r04)
p07 → r10 (test final)
```

Les **répétitions** des drills déjà faits passent toujours **avant** le travail du jour (voir `drills/README.md`).

---

## 4. La disposition des dossiers

```
ch9_collections/
├── PARCOURS.md              ← ce fichier
├── projects/
│   ├── README.md            ← la liste des 7 projets, à cocher
│   └── p01_inventory/
│       ├── TODO.md          ← L'ÉNONCÉ
│       ├── Data.java        ← les données : tu les lis, tu ne les modifies pas
│       ├── Check.java       ← le correcteur : tu le LANCES
│       ├── solution/        ← la correction : à la fin seulement
│       └── (tes types)      ← Item.java, Inventory.java : c'est TOI qui les crées
└── drills/
    ├── README.md            ← règles des drills + tableau de suivi
    └── r01_collection/
        ├── TODO.md          ← les défis + la carte mémoire repliée
        ├── Check.java
        ├── solution/
        └── (ton Recall01.java)
```

**Spécificités du chapitre 9 :**
- **Déclare avec l'interface, instancie avec la classe :** `List<Item> items = new ArrayList<>();`. `Check` cherche souvent cette forme exacte.
- **Choisis l'ordre :** `TreeMap` et `TreeSet` donnent un affichage **déterministe**. Un `HashMap` ou un `HashSet` n'en a pas : ne l'affiche jamais directement (la sortie attendue ne le fait pas).
- **Méfie-toi des vues :** `subList`, `keySet`, `values`, `headMap`, `descendingSet`… modifier la vue modifie l'original.
- **Les méthodes « qui rendent `null` » et celles « qui lèvent une exception »** (`poll`/`remove`, `peek`/`element`, `get`/`getFirst`). Dans les projets, on utilise celles qui rendent `null`. Les autres se testent dans les expériences.
- **Pour les génériques, lis la signature à voix haute :** « une liste de *quelque chose qui est* un `Number` » (`? extends`), « une liste qui *accepte* des `Integer` » (`? super`).
- **Complexités :** pour chaque algorithme, écris en commentaire sa complexité et la raison du choix de la collection (O(1) `ArrayDeque`, O(log n) `TreeMap` et `PriorityQueue`, O(1) en moyenne `HashMap`).

---

## 5. Comment faire un projet

### 5.1 Lire le `TODO.md`

| Partie | Ce que tu en fais |
|---|---|
| **En-tête** | les notions visées, les algorithmes, les types à créer, la règle du crescendo |
| **Tableau de bord** (étapes ☐) | les types et leurs membres, les **lignes exactes**, les **appels exacts** du `main`, les questions et les expériences |
| **Checklist** | ce que `Check` cherchera dans ton code |
| **Sortie attendue complète** | le contrat exact, au caractère près |

### 5.2 Travailler, étape par étape

1. **Sur papier**, pour chaque algorithme : la collection choisie, pourquoi, et ce qu'elle contient à chaque étape, sur un petit exemple.
2. **Crée la classe du `main` tout de suite**, pour pouvoir lancer `Check`.
3. **Fais une étape à la fois.** Lance `Check`, corrige, puis coche ☐ → ☑.
4. **Fais les expériences** et **réponds aux questions par écrit**, en commentaire.

### 5.3 Lire la réponse de `Check`

| Ligne | Signification | Que faire |
|---|---|---|
| `[ERREUR] classe introuvable` | mauvais nom de classe ou de paquet | vérifie le `package` et le nom du fichier |
| `[ERREUR] ton programme a lance …` | ton `main` a planté | `ConcurrentModificationException` : retrait dans un for-each ; `UnsupportedOperationException` : une liste immuable modifiée ; `NullPointerException` : un `get` sur une clé absente |
| `[FAIL] sortie : 3/8 … (ligne 4)` | la 4e ligne diffère | compare `attendu` et `obtenu` ; souvent un ordre de tri ou un `HashMap` à la place d'un `TreeMap` |
| `[FAIL] API : encore a placer …` | des éléments visés manquent | la checklist dit où ils servent |
| `[FAIL] API : interdit ici …` | un stream, `Optional`, `try/catch`… | remplace-le (voir la section 2) |
| `*** PROJET REUSSI ***` | tout est juste | passe à la section 5.5 |

L'argument `solution` vérifie la solution, pour voir à quoi ressemble un projet réussi.

### 5.4 Quand tu bloques

| Palier | Temps | Ce que tu fais |
|---|---|---|
| 1 | jusqu'à 20 min | affiche la collection après chaque opération, et déroule l'algorithme à la main sur 3 éléments |
| 2 | 20 min de plus | relis la **carte mémoire** du drill du même thème, ou demande-moi un **indice** |
| 3 | en dernier recours | lis **uniquement** la partie concernée de `solution/`, ferme, réécris de mémoire, note `// AIDE : solution consultée` |

**Jamais :**
- copier depuis `solution/` ;
- modifier `Check.java` ou `Data.java` ;
- taper en dur un résultat que Java doit calculer.

### 5.5 Quand c'est réussi

1. Compare avec `solution/` : le choix des collections et les signatures génériques. Lis les commentaires.
2. Coche le projet dans `projects/README.md`, puis fais ses drills.

---

## 6. Comment faire un drill

1. Note l'heure. Le chrono cible est en haut du `TODO.md`.
2. Crée `RecallNN.java` dans le dossier du drill.
3. Fais les défis. La ligne attendue est sous chaque défi.
4. **Rien d'autre que ta mémoire.** Plus de 3 minutes bloqué : ✗, et défi suivant.
5. Lance `Check`.
6. **Après seulement :** ouvre la carte mémoire, relis tes ✗, puis fais les expériences.
7. Note date, temps et ✗ dans `drills/README.md`.
8. **Avant chaque répétition, supprime ton `RecallNN.java`.**

---

## 7. Comment savoir que le chapitre 9 est acquis

- [ ] Les 7 projets affichent `PROJET REUSSI`, et toutes les questions ont une réponse écrite.
- [ ] Les 10 drills ont passé la répétition R3 (J+7, sans carte).
- [ ] r10 passe en moins de 10 minutes, sans carte.
- [ ] Tu sais, sans hésiter :
  - choisir entre `ArrayList`, `LinkedList`, `ArrayDeque`, `PriorityQueue`, `HashMap`, `TreeMap`… et justifier par la complexité ;
  - donner l'ordre et la règle des `null` pour chaque `Set` et chaque `Map` ;
  - dire quelle méthode de `Queue`/`Deque` lève une exception et laquelle rend `null` ;
  - prédire `remove(int)` contre `remove(Object)` ;
  - prédire `merge` et `compute` quand la fonction rend `null` ;
  - dire ce qui est modifiable dans `Arrays.asList`, `List.of`, `copyOf` et `unmodifiableList` ;
  - écrire un `Comparator` composé, et expliquer le piège du `TreeSet` (`compare == 0`) ;
  - dire si une affectation avec `? extends` ou `? super` compile, et ce qu'on peut y lire et y ajouter ;
  - lister ce que l'effacement de type interdit ;
  - expliquer le contrat `equals`/`hashCode` d'un `HashSet`, et ce que provoque un type brut (pollution, `ClassCastException` différée).
- [ ] Tu sais écrire sans aide : un parcours en largeur, Dijkstra avec une `PriorityQueue`, un tri topologique, un top-k avec un tas, un cache LRU (`LinkedHashMap`), une classe générique bornée.
- [ ] p04 et p07 ont été refaits **depuis un dossier vide**, 2 à 3 semaines plus tard.
