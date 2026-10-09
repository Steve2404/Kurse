# Projet 4 — Le moteur de recherche et son cache (le hachage)

> Première fois ? Lis d'abord le mode d'emploi [`ch17_algorithms/PARCOURS.md`](../../PARCOURS.md).
>
> **Bloqué sur une étape ?** [`INDICES.md`](INDICES.md) donne deux indices repliés par étape, sans code. **Étape finie ?** [`solution/CORRIGE.md`](solution/CORRIGE.md) donne, étape par étape, le code de l'étape, les **réponses aux questions** et le résultat exact des **expériences**. N'ouvre que la section de l'étape que tu viens de faire.

**Notions visées (chapitre 17) :**
- **le réflexe du hachage** : « ai-je déjà vu ceci ? » en O(1), au lieu de tout relire ;
- **compter et regrouper** avec une `Map` : `merge`, `computeIfAbsent`, une **signature** comme clé ;
- **comment marche une `HashMap`** : des seaux, `hashCode`, `equals`, les **collisions**, le **facteur de charge** et l'agrandissement ;
- **le cache LRU** : une `HashMap` **et** une liste doublement chaînée, pour tout faire en O(1) ;
- **un mauvais `hashCode`** ruine tout.

**Ce que TU crées :** dans `ch17_algorithms.projects.p04_hashing` :
- **`Hashing`**, **`SimpleHashMap<K, V>`** et **`LruCache<K, V>`** (signatures imposées) ;
- tes tests : **`HashingTest`**, **`SimpleHashMapTest`** et **`LruCacheTest`** (un fichier par classe, c'est l'usage).

**Règle du crescendo :** tout Java 17, JUnit et Mockito. `HashMap` et `HashSet` sont permis dans `Hashing` et dans le cache, mais la table et le cache s'écrivent **à la main** : pas de `LinkedHashMap`, de `Hashtable` ni de `removeEldestEntry`. Pas de `System.out` ni de `Thread.sleep` dans tes tests.

Chaque étape commence par une **📖 leçon**, avec un exemple sur un autre sujet : le vestiaire d'un théâtre.

> **🧰 Tes outils pour ce projet**
>
> - **Lancer tous tes tests à la fois :** clic droit sur le dossier `p04_hashing` → **Run 'Tests in p04_hashing'**.
> - **Mesurer**, pour les expériences : une petite classe `Mesure` avec un `main` et `System.nanoTime()`.

---

## Tableau de bord

### ☐ Étape 1 — L'idée du hachage : « l'ai-je déjà vu ? »

**📖 La leçon : le vestiaire numéroté.** Au théâtre, si les manteaux étaient en tas, retrouver le tien voudrait dire fouiller tout le tas : O(n). Le vestiaire te donne un **numéro** ; ton manteau est sur le crochet de ce numéro : un seul geste, **O(1)**. Une `HashMap` fait la même chose : elle calcule un numéro à partir de la clé (son `hashCode`), et range la valeur sur le crochet correspondant.

Le réflexe : dès qu'un algorithme demande « est-ce que j'ai déjà vu telle valeur ? » à chaque tour d'une boucle, une `HashMap` ou un `HashSet` transforme O(n²) en **O(n)**.

**👉 À toi :** `public final class Hashing` (constructeur `private`) avec **`public static int[] twoSum(int[] a, int target)`** : dans un tableau **non trié**, les indices `{i, j}` (`i < j`) de deux cases dont la somme vaut `target`, ou un tableau vide. Pour chaque case `j`, on cherche si le **complément** `target - a[j]` a déjà été vu, et à quel indice. On rend la **première paire complétée** (la plus petite `j`) ; pour des doublons, on garde le **premier** indice vu (`putIfAbsent`).

**Tes tests :** une paire au début, au milieu, deux cases égales, une paire dont le premier élément est en tête, un doublon où il faut garder le premier indice (`{5, 5, 1}`, cible 6), aucune paire, une seule case.

**🧪 Expérience :** dans `Mesure`, écris la version naïve (deux boucles imbriquées) et chronomètre-la contre la tienne sur `{0, 2, 4, …}` (aucune paire de somme 1), pour 10 000, 20 000 et 40 000 cases.

### ☐ Étape 2 — Compter et regrouper

**📖 La leçon : une signature comme clé.** Pour regrouper des objets « qui se ressemblent », on leur calcule une **signature** commune, et on s'en sert comme clé. Au vestiaire : tous les manteaux d'une même famille sous le nom de famille. Deux outils de `Map` (chapitre 9) font le travail en une ligne :

```java
compte.merge(mot, 1, Integer::sum);                                  // ajoute 1 au compte (ou le crée à 1)
groupes.computeIfAbsent(signature, k -> new ArrayList<>()).add(mot); // crée la liste au besoin, puis ajoute
```

**👉 À toi :**
- **`public static Map<String, List<String>> groupAnagrams(List<String> words)`** : des anagrammes ont les mêmes lettres ; leur signature est le mot aux **lettres triées** (`"niche"` → `"cehin"`). Chaque groupe garde l'ordre d'arrivée des mots ;
- **`public static Character firstUnique(String s)`** : le premier caractère qui n'apparaît **qu'une fois**, ou `null`. Deux passages : compter, puis relire la chaîne dans l'ordre ;
- **`public static List<String> topWords(String text, int k)`** : les `k` mots les plus fréquents (au plus). Un mot = une suite de lettres `a` à `z` après passage en minuscules avec `toLowerCase(Locale.ROOT)` (coupe avec `split("[^a-z]+")`, et ignore les morceaux vides). À fréquence égale, l'**ordre alphabétique**.

**Tes tests :** six mots dont deux groupes d'anagrammes et un mot seul ; une liste vide ; `"leetcode"`, `"loveleetcode"`, `"aabbc"`, `"abcabc"` (aucun), `"z"`, la chaîne vide ; une phrase avec ponctuation et majuscules, avec `k = 3` et `k = 5` ; et deux mots à égalité (`"ours chat"`) pour vérifier l'ordre alphabétique.

**❓ Question :** pourquoi, dans ton test de `topWords`, faut-il **deux mots à égalité** pour vérifier l'ordre alphabétique ? Que se passerait-il si l'on se fiait à l'ordre de la `HashMap` ?

### ☐ Étape 3 — La plus longue suite consécutive

**👉 À toi :** **`public static int longestConsecutive(int[] a)`** : la longueur de la plus longue suite d'entiers **consécutifs** présents dans le tableau, dans n'importe quel ordre (`{100, 4, 200, 1, 3, 2}` → 4, pour 1, 2, 3, 4). En **O(n)** : mets toutes les valeurs dans un `HashSet`, puis ne compte une suite **qu'à partir de son début** (une valeur `v` dont `v - 1` est absent).

**Tes tests :** les deux exemples classiques, une seule valeur, des doublons, des négatifs, un tableau vide.

**❓ Question :** pourquoi ne compter qu'à partir du **début** d'une suite ? Que deviendrait la complexité sans cette condition, pour le tableau `{n, n−1, …, 2, 1}` ?

### ☐ Étape 4 — Ta propre table de hachage

**📖 La leçon : les seaux.** À l'intérieur, une `HashMap` est un **tableau de seaux**. Pour une clé :
1. `key.hashCode()` donne un `int` (qui peut être **négatif**) ;
2. `Math.floorMod(hash, nombreDeSeaux)` le transforme en numéro de seau, entre 0 et `nombreDeSeaux - 1` (`%` rendrait un négatif pour un hash négatif) ;
3. le seau contient une **petite liste chaînée** des entrées tombées là : deux clés **différentes** peuvent avoir le même numéro (une **collision** : `"Aa"` et `"BB"` ont le même `hashCode`, 2112) ;
4. dans le seau, on compare les clés avec **`equals`** (jamais `==`).

Si les seaux se remplissent trop, chercher redevient lent. Quand le nombre d'entrées dépasse **les trois quarts** du nombre de seaux (le **facteur de charge**, 0,75), on **double** le tableau, et chaque entrée est rangée dans son **nouveau** seau (son numéro dépend du nombre de seaux).

**👉 À toi :** **`public final class SimpleHashMap<K, V>`** :
- 8 seaux au départ ; une classe interne `Node` (clé, valeur, suivant) ;
- **`public V put(K key, V value)`** : remplace la valeur si la clé existe (et rend l'ancienne), sinon ajoute en tête du seau (et rend `null`) ; puis, si `size` dépasse `nombreDeSeaux * 3 / 4`, agrandit ;
- **`public V get(Object key)`**, **`public V remove(Object key)`** (rend la valeur retirée, ou `null`) ;
- **`public int size()`** et **`public int capacity()`** (le nombre de seaux) ;
- une clé `null` : `Objects.requireNonNull(key, "cle absente")`.

**Tes tests :** ajouter, remplacer, retirer ; remplacer avec une **autre** chaîne égale (`new String("cle")` deux fois : la taille reste 1) ; une collision `"Aa"` / `"BB"` ; trois clés du même seau (`"AaAa"`, `"BBBB"`, `"AaBB"`), dont on retire celle du **milieu** de la chaîne ; des clés `Integer` négatives (et `Integer.MIN_VALUE`) ; l'agrandissement (8 seaux avec 6 entrées, 16 dès la 7e) ; la clé `null` ; un million d'entrées en moins de 4 secondes.

**❓ Question :** pourquoi faut-il **ranger à nouveau** chaque entrée lors de l'agrandissement, au lieu de recopier les seaux tels quels ?

### ☐ Étape 5 — Le cache LRU

**📖 La leçon : deux structures qui s'entraident.** Un vestiaire trop petit doit parfois rendre un manteau pour en accepter un nouveau : il rend celui qui est là **depuis le plus longtemps sans avoir servi** (*Least Recently Used*). Il faut donc :
- **trouver** un manteau en O(1) : une `HashMap` de la clé vers le maillon ;
- **savoir lequel** a servi le moins récemment, et **déplacer** un manteau qui vient de servir, en O(1) : une **liste doublement chaînée**, du plus ancien au plus récent. Chaque maillon connaît son précédent **et** son suivant : on le décroche en O(1).

Deux maillons **sentinelles** vides, au début et à la fin, évitent tous les cas particuliers (liste vide, premier, dernier).

**👉 À toi :** **`public final class LruCache<K, V>`** :
- **`public LruCache(int capacity)`** : une capacité `< 1` lance `IllegalArgumentException("capacite invalide : " + capacity)` ;
- **`public V get(K key)`** : la valeur, ou `null` ; un `get` réussi rend l'entrée **la plus récente** ;
- **`public void put(K key, V value)`** : une clé existante est mise à jour **et** devient la plus récente ; une nouvelle clé, si le cache est plein, chasse d'abord la **plus ancienne** ;
- **`public int size()`** et **`public List<K> keysFromOldest()`** (les clés, de la plus ancienne à la plus récente).

**Tes tests :** l'éviction de la plus ancienne ; un `get` qui sauve une entrée ; un `put` sur une clé existante ; un `get` raté qui ne change pas l'ordre ; la capacité 0 ; un million d'opérations en moins de 3 secondes.

### ☐ Étape 6 — La vitesse, et les mutants

**👉 À toi :** lance `Check`, et tue les **13** mutants.

### Expériences (hors sortie attendue)

1. **Un mauvais `hashCode`**, dans `Mesure` : écris `record Mauvais(int v) { @Override public int hashCode() { return 42; } }`, puis chronomètre l'ajout de 5 000, 10 000 et 20 000 clés `Mauvais` dans ta `SimpleHashMap`, et la même chose avec des clés `Integer`. Que se passe-t-il, et pourquoi ?
2. Dans ta table, remplace `Math.floorMod(…)` par `key.hashCode() % length`. Quel test casse, avec quelle exception ?

---

## Checklist (vérifiée par `Check`)

- **Ton code :** les trois classes et toutes leurs signatures exactes, `Math.floorMod(`, `.hashCode()`, `.equals(` ; jamais `LinkedHashMap`, `java.util.Hashtable`, `removeEldestEntry`.
- **Tes tests :** au moins **25** tests, `@Test`, `@ParameterizedTest`, `assertEquals(`, `assertTimeoutPreemptively(` ; ni `System.out` ni `Thread.sleep`.
- **Les tests de référence** passent sur ton code, **vitesse comprise**.
- **Les 13 mutants** sont tués.

---

## Ce que `Check` affiche quand tout est juste

```
=== Verification des tests de ch17_algorithms.projects.p04_hashing ===
[PASS] tes tests sur TON code : 30 tests, 30 reussis
[PASS] tes tests sur le code de REFERENCE : 30 tests, 30 reussis
[PASS] les tests de REFERENCE sur TON code : 30 tests, 30 reussis
   mutant 1 : tue (par negativeHashCodesWork)
   …
[PASS] mutants : 13/13 tues
--- API de ton code ---
[PASS] API : tous les elements vises sont utilises
--- API de tes tests ---
[PASS] API : tous les elements vises sont utilises

*** PROJET REUSSI : tes tests passent, attrapent tous les mutants, et ton code est juste. ***
```
