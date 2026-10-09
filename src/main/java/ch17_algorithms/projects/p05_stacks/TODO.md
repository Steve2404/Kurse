# Projet 5 — La calculatrice de poche (piles et files)

> Première fois ? Lis d'abord le mode d'emploi [`ch17_algorithms/PARCOURS.md`](../../PARCOURS.md).
>
> **Bloqué sur une étape ?** [`INDICES.md`](INDICES.md) donne deux indices repliés par étape, sans code. **Étape finie ?** [`solution/CORRIGE.md`](solution/CORRIGE.md) donne, étape par étape, le code de l'étape, les **réponses aux questions** et le résultat exact des **expériences**. N'ouvre que la section de l'étape que tu viens de faire.

**Notions visées (chapitre 17) :**
- **la pile** (dernier entré, premier sorti) et **la file** (premier entré, premier sorti) ; `Deque` et `ArrayDeque` plutôt que la vieille `Stack` ;
- **les parenthèses équilibrées** ;
- **la notation polonaise inverse**, et **l'algorithme de la gare de triage** de Dijkstra (priorités, associativité, parenthèses) ;
- **la pile monotone** : « le prochain plus grand », le plus grand rectangle d'un histogramme ;
- **le coût amorti** : une file faite de deux piles, une pile qui connaît son minimum.

**Ce que TU crées :** dans `ch17_algorithms.projects.p05_stacks` :
- **`Calculator`**, **`Monotonic`**, **`MinStack`** et **`QueueFromStacks<T>`** (signatures imposées) ;
- tes tests : **`CalculatorTest`**, **`MonotonicTest`** et **`StructuresTest`** (pour `MinStack` et `QueueFromStacks`).

**Règle du crescendo :** tout Java 17, JUnit et Mockito. Les piles se font avec `Deque` et `ArrayDeque` : la vieille classe `java.util.Stack` est interdite (et bien sûr un moteur de script qui calculerait à ta place). Pas de `System.out` ni de `Thread.sleep` dans tes tests.

Chaque étape commence par une **📖 leçon**, avec un exemple sur un autre sujet : une pile d'assiettes, et une gare de triage.

> **🧰 Tes outils pour ce projet**
>
> - **Une pile en Java :** `Deque<Integer> pile = new ArrayDeque<>();` puis `pile.push(x)` (poser dessus), `pile.pop()` (retirer le dessus), `pile.peek()` (regarder le dessus sans le retirer), `pile.isEmpty()`.
> - **Lancer tous tes tests :** clic droit sur le dossier `p05_stacks` → **Run 'Tests in p05_stacks'**.

---

## Tableau de bord

### ☐ Étape 1 — La pile, et les parenthèses

**📖 La leçon : la pile d'assiettes.** On pose une assiette **sur** la pile, on reprend **celle du dessus** : la dernière posée est la première reprise (*LIFO*, *last in, first out*). En Java, la pile moderne est une `Deque` (« file à deux bouts ») : `ArrayDeque`. La vieille classe `Stack` existe encore, mais elle est lente (synchronisée, chapitre 13) et déroutante (voir l'expérience).

Une pile est faite pour les choses **imbriquées** : chaque parenthèse fermante doit fermer **la dernière ouvrante encore ouverte**, celle du dessus de la pile.

**👉 À toi :** `public final class Calculator` (constructeur `private`) avec **`public static boolean balanced(String s)`** : vrai si les parenthèses `()`, `[]` et `{}` de `s` sont bien équilibrées et bien imbriquées. Les autres caractères sont ignorés.

**Tes tests :** équilibrées (vide, `()`, `([]{})`, avec du texte autour, imbriquées) ; déséquilibrées (`(`, `)`, `(]`, `([)]`, `((`, `())(`, `}{`).

**🧪 Expérience :** pose 1, 2, 3 sur une `Stack` et sur une `ArrayDeque` (avec `push`), puis affiche les deux piles et leur `peek()`. Qu'est-ce qui surprend ?

**❓ Question :** pourquoi faut-il vérifier que la pile est **vide** à la fin ?

### ☐ Étape 2 — La notation polonaise inverse

**📖 La leçon : l'opérateur après les nombres.** En notation polonaise inverse (*RPN*), on écrit l'opérateur **après** ses deux opérandes : `3 4 +` vaut 7, et `2 3 4 * +` vaut 2 + (3 × 4) = 14. Plus besoin de parenthèses ni de priorités ! Une pile suffit : un nombre se pose sur la pile ; un opérateur reprend les **deux** nombres du dessus, calcule, et pose le résultat.

**Piège :** le premier nombre repris est l'opérande de **droite**. `8 2 -` vaut 8 − 2 = 6, pas 2 − 8.

**👉 À toi :** **`public static long evalRpn(String expr)`** : les jetons sont séparés par des espaces ; des entiers positifs et les opérateurs `+ - * /` (division entière). Une expression invalide (un opérateur sans deux nombres, un jeton inconnu, ou pas **exactement un** nombre à la fin) lance `IllegalArgumentException("expression invalide : " + expr)`. Une division par zéro laisse passer l'`ArithmeticException` de Java.

**Tes tests :** une addition, une soustraction (l'ordre !), une division (et une division entière : `7 2 /`), une expression longue, un nombre seul ; cinq expressions invalides (les messages) ; la division par zéro.

### ☐ Étape 3 — La gare de triage

**📖 La leçon : l'aiguillage de Dijkstra.** Pour transformer `3 + 4 * 2` en `3 4 2 * +`, Edsger Dijkstra a imaginé une gare de triage : les **nombres** (les wagons) passent **tout droit** vers la sortie ; les **opérateurs** attendent sur une **voie de garage** (une pile).
- Quand un opérateur arrive, les opérateurs qui attendent et qui ont une priorité **supérieure ou égale** partent d'abord vers la sortie (`*` et `/` ont la priorité 2, `+` et `-` la priorité 1). « Égale » compte : `8 - 3 - 2` se lit de gauche à droite, (8 − 3) − 2 = 3.
- Une parenthèse ouvrante va sur la voie de garage ; une fermante fait partir tous les opérateurs jusqu'à l'ouvrante, qu'on jette.
- À la fin, tous les opérateurs qui attendent partent.

**👉 À toi :**
- **`public static List<String> toRpn(String infix)`** : les jetons en notation polonaise inverse. Les nombres peuvent avoir **plusieurs chiffres**, les espaces sont facultatifs (`"12+34"`), pas de moins devant un nombre. Une parenthèse sans partenaire : `IllegalArgumentException("parentheses desequilibrees")` ; un autre caractère : `IllegalArgumentException("caractere inconnu : " + c)` ;
- **`public static long evaluate(String infix)`** : `toRpn`, puis `evalRpn` des jetons joints par des espaces.

**Tes tests :** la priorité (`3 + 4 * 2`), les parenthèses, l'associativité à gauche (`8 - 3 - 2`, `100 / 10 / 5`), les nombres à plusieurs chiffres collés aux opérateurs ; des évaluations ; les trois refus ; une somme de 200 001 termes et 50 000 parenthèses imbriquées en moins de 3 secondes.

**❓ Question :** que donnerait `8 - 3 - 2` si l'on ne faisait partir que les opérateurs de priorité **strictement** supérieure ?

### ☐ Étape 4 — La pile monotone

**📖 La leçon : ceux qui attendent un plus grand.** Pour chaque jour, combien de jours faut-il attendre une température **plus chaude** ? Comparer chaque jour à tous les suivants : O(n²). Astuce : une pile des jours **qui attendent encore** leur jour plus chaud. Quand un nouveau jour arrive, tous les jours de la pile **plus froids** que lui ont trouvé leur réponse : on les dépile. La pile reste donc **triée** (monotone). Chaque jour entre une fois et sort au plus une fois : **O(n)** au total, malgré la boucle dans la boucle.

**👉 À toi :** `public final class Monotonic` (constructeur `private`) :
- **`public static int[] daysUntilWarmer(int[] temps)`** : pour chaque jour, le nombre de jours jusqu'au prochain jour **strictement** plus chaud, ou 0 ;
- **`public static long largestRectangle(int[] heights)`** : l'aire du plus grand rectangle sous un histogramme (barres de largeur 1). Même idée : une pile d'indices de barres de hauteurs croissantes ; quand une barre plus **basse** (ou égale) arrive, chaque barre plus haute dépilée ne peut plus s'étendre à droite : son rectangle a pour largeur la distance entre la barre qui arrive et la barre restée sous elle dans la pile. Une barre fictive de hauteur 0 à la fin vide la pile. Calcule l'aire en `long`.

**Tes tests :** l'exemple classique des températures (`{73, 74, 75, 71, 69, 72, 76, 73}`), trois jours égaux, une montée, un tableau vide ; sept histogrammes (dont `{2, 1, 5, 6, 2, 3}` → 10, des barres égales, une montée, un tableau vide) ; 100 000 barres de hauteur 100 000 ; un million de valeurs montantes et descendantes en moins de 3 secondes.

**❓ Question :** pourquoi la boucle `while` dans la boucle `for` ne fait-elle pas un O(n²) ?

### ☐ Étape 5 — Le coût amorti : deux structures astucieuses

**📖 La leçon : payer en avance, rarement.** Une file (premier entré, premier sorti) avec **deux piles** : on entre dans la pile `in`, on sort de la pile `out`. Quand `out` est vide, on y **verse** toute la pile `in` : l'ordre s'inverse, ce qui remet les premiers arrivés sur le dessus. Un versement coûte cher, mais chaque élément n'est versé **qu'une fois** dans sa vie : en moyenne, chaque opération coûte O(1). On dit **O(1) amorti**.

**👉 À toi :**
- **`public final class MinStack`** : `push(int v)`, `int pop()`, `int peek()`, `int min()` et `int size()`, **tous en O(1)**. Une 2e pile retient le minimum **à chaque hauteur** de la pile. Sur une pile vide, `pop`, `peek` et `min` lancent `NoSuchElementException("pile vide")` ;
- **`public final class QueueFromStacks<T>`** : `offer(T)`, `T poll()` et `T peek()` (`null` si la file est vide), `int size()`. Ne verse que si la pile de sortie est **vide**.

**Tes tests :** une `MinStack` où le minimum apparaît deux fois (le minimum doit survivre au retrait de l'un des deux) ; la pile vide ; une file où l'on mélange ajouts et retraits ; sa taille ; un million d'opérations en moins de 3 secondes.

**❓ Question :** pour `MinStack`, pourquoi ne pas retenir **un seul** minimum dans un champ ?

### ☐ Étape 6 — La vitesse, et les mutants

**👉 À toi :** lance `Check`, et tue les **13** mutants.

### Expériences (hors sortie attendue)

1. Dans ton `largestRectangle`, retire le cast `(long)` du calcul de l'aire. Que donne le test des 100 000 barres de hauteur 100 000 ? Calcule `100_000 * 100_000` dans `Mesure`.
2. Dans ta `QueueFromStacks`, verse **à chaque** `poll` (sans tester que la sortie est vide). Quel test casse ?

---

## Checklist (vérifiée par `Check`)

- **Ton code :** les quatre classes et leurs signatures exactes, `Deque<`, `ArrayDeque` ; jamais `java.util.Stack`, `new Stack`, `ScriptEngine`.
- **Tes tests :** au moins **40** tests (chaque cas compte), `@Test`, `@ParameterizedTest`, `assertEquals(`, `assertThrows(`, `assertTimeoutPreemptively(` ; ni `System.out` ni `Thread.sleep`.
- **Les tests de référence** passent sur ton code, **vitesse comprise**.
- **Les 13 mutants** sont tués.

---

## Ce que `Check` affiche quand tout est juste

```
=== Verification des tests de ch17_algorithms.projects.p05_stacks ===
[PASS] tes tests sur TON code : 49 tests, 49 reussis
[PASS] tes tests sur le code de REFERENCE : 49 tests, 49 reussis
[PASS] les tests de REFERENCE sur TON code : 49 tests, 49 reussis
   mutant 1 : tue (par unbalancedStrings ["(" desequilibre])
   …
[PASS] mutants : 13/13 tues
--- API de ton code ---
[PASS] API : tous les elements vises sont utilises
--- API de tes tests ---
[PASS] API : tous les elements vises sont utilises

*** PROJET REUSSI : tes tests passent, attrapent tous les mutants, et ton code est juste. ***
```
