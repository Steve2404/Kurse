# Drill de rappel 2 — Le cycle de vie des tests

> Première fois ? Lis d'abord le mode d'emploi [`ch16_testing/PARCOURS.md`](../../PARCOURS.md). À faire après le projet p02.

**Chrono cible :** 12 min, puis 6 min.

**Règles :**
- Tout se fait de mémoire, **imports compris**.
- Crée la classe de test **`Recall02Test`** dans le paquet `ch16_testing.drills.r02_lifecycle`.
- Un défi = une méthode de test nommée exactement comme demandé. Tous les tests exécutés doivent **passer**.
- La classe a deux champs : `static int answer;` et `List<String> items;`.

**Les notions de ce drill ont été apprises dans :** projet 2 (étapes 1 et 3, et son expérience sur l'ordre du cycle de vie) ; `@Disabled` au projet 1 (expériences de fin). `@RepeatedTest`, `@Tag` et `@TestInstance` sont **nouveaux** : la carte mémoire les explique ; ce drill sert aussi à les découvrir.

<details><summary><b>Comment faire ce drill, concrètement</b> (à lire la 1re fois)</summary>

1. **Note l'heure** de départ.
2. **Crée la classe** : clic droit sur le dossier `r02_lifecycle` → **New** → **Java Class** → `Recall02Test`.
3. **Écris les méthodes** dans l'ordre des défis. Lance la classe (flèche verte) : tout ce qui s'exécute doit être vert.
4. **Bloqué plus de 3 minutes ?** `// D04 : ✗`, et défi suivant.
5. **Lance `Check.java`.** Il affiche une ligne par méthode **exécutée**, avec son nombre d'exécutions.
6. **Ensuite seulement**, la carte mémoire, puis les expériences.
7. **Note** date, temps et ✗ dans [`drills/README.md`](../README.md).

</details>

## Défis

- ☐ **D00.** Le décor :
  - une méthode `@BeforeAll` qui met `answer` à 41 ;
  - une méthode `@BeforeEach` qui crée une `ArrayList` neuve dans `items` et y ajoute `"outer"` ;
  - une méthode `@AfterEach` qui vide `items`.
- ☐ **D01.** `d01` ajoute `"a"` à `items`, et vérifie que `items` vaut `List.of("outer", "a")`.
  → `d01 : 1 executions, 1 reussies`
- ☐ **D02.** `d02` ajoute `"b"` puis `"c"`, et vérifie que `items` contient 3 éléments.
  → `d02 : 1 executions, 1 reussies`
- ☐ **D03.** `d03` vérifie que `answer + 1` vaut 42.
  → `d03 : 1 executions, 1 reussies`
- ☐ **D04.** `d04` est un **test répété 3 fois** ; il reçoit un paramètre `RepetitionInfo` et vérifie que le nombre total de répétitions vaut 3, et que la répétition courante est entre 1 et 3.
  → `d04 : 3 executions, 3 reussies`
- ☐ **D05.** `d05` est un test **désactivé** avec la raison `"pas encore"`, qui contient seulement `fail("ne doit jamais s'executer")`. Il n'apparaît **pas** dans le rapport.
- ☐ **D06.** Une classe **`@Nested`** `Inner`, avec son propre `@BeforeEach` qui ajoute `"inner"` à `items`, et un test `d06` qui vérifie que `items` vaut `List.of("outer", "inner")`.
  → `d06 : 1 executions, 1 reussies`
- ☐ **D07.** `d07`, avec le nom affiché `"un nom en francais"` et l'étiquette `"rapide"`, vérifie que `items` contient `"outer"`.
  → `d07 : 1 executions, 1 reussies`
- ☐ **D08.** Une classe `@Nested` `Shared` dont **une seule instance** sert à tous ses tests (`@TestInstance(TestInstance.Lifecycle.PER_CLASS)`). Un champ `int counter`, une méthode `@BeforeAll` **non `static`** qui le met à 0, et deux tests `d08a` et `d08b` qui font chacun `counter++` puis vérifient que `counter` est entre 1 et 2.
  → `d08a : 1 executions, 1 reussies`
  → `d08b : 1 executions, 1 reussies`

## Expériences (après le drill)

1. Retire `@Disabled` de `d05`, et relance `Check`. Que devient le rapport ?
2. Retire `static` de ta méthode `@BeforeAll` (et du champ `answer`). Recopie le message.
3. Retire `@TestInstance(…)` de `Shared`. Que se passe-t-il ?

## Sortie attendue complète

```
d01 : 1 executions, 1 reussies
d02 : 1 executions, 1 reussies
d03 : 1 executions, 1 reussies
d04 : 3 executions, 3 reussies
d06 : 1 executions, 1 reussies
d07 : 1 executions, 1 reussies
d08a : 1 executions, 1 reussies
d08b : 1 executions, 1 reussies
```

<details><summary>Ouvrir la carte</summary>

| Annotation | Quand | Remarque |
|---|---|---|
| `@BeforeAll` | une fois, avant tous les tests de la classe | `static`, sauf avec `@TestInstance(PER_CLASS)` |
| `@BeforeEach` | avant **chaque** test | celui de la classe extérieure passe avant celui d'un `@Nested` |
| `@AfterEach` | après **chaque** test, même s'il échoue | |
| `@AfterAll` | une fois, après tous les tests | `static`, sauf avec `PER_CLASS` |
| `@Test` | un test | |
| `@RepeatedTest(n)` | le test, `n` fois | paramètre facultatif `RepetitionInfo` : `getCurrentRepetition()`, `getTotalRepetitions()` |
| `@Disabled("raison")` | jamais | le test n'est pas exécuté (IntelliJ le marque ignoré) |
| `@Nested` | un groupe de tests | classe interne **non `static`** |
| `@DisplayName("…")` | — | le nom affiché ; ne change pas le nom de la méthode |
| `@Tag("…")` | — | une étiquette, pour lancer seulement certains tests (Maven, IntelliJ) |
| `@TestInstance(Lifecycle.PER_CLASS)` | — | **une seule** instance pour tous les tests de la classe : les champs sont partagés |

- **Par défaut**, JUnit crée **une instance par test** (`PER_METHOD`) : un champ ne se partage jamais entre deux tests.
- `fail("message")` fait échouer le test tout de suite.

**Les expériences** (vérifiées avec JUnit 5.11.4) :
1. Sans `@Disabled`, `d05` s'exécute et échoue : le rapport contient une ligne `d05 : 1 executions, 0 reussies`, et `Check` affiche `[FAIL] rapport`.
2. Sans `static`, toute la classe est en échec avant le 1er test : `@BeforeAll method '…once()' must be static unless the test class is annotated with @TestInstance(Lifecycle.PER_CLASS).`
3. Sans `PER_CLASS`, la méthode `@BeforeAll` non `static` de `Shared` provoque la même erreur, pour le groupe `Shared` : `d08a` et `d08b` ne s'exécutent plus.

</details>
