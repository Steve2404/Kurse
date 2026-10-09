# Drill de rappel 1 — Les assertions de JUnit

> Première fois ? Lis d'abord le mode d'emploi [`ch16_testing/PARCOURS.md`](../../PARCOURS.md). À faire après les projets p01 et p02.

**Chrono cible :** 12 min, puis 6 min.

**Règles :**
- Tout se fait de mémoire, **imports compris** (`org.junit.jupiter.api.Test` et les `import static org.junit.jupiter.api.Assertions.…`).
- Crée la classe de test **`Recall01Test`** dans le paquet `ch16_testing.drills.r01_assertions`.
- Un défi = **une méthode `@Test`** nommée exactement `d01`, `d02`… Chaque test doit **passer** (vert).

**Les notions de ce drill ont été apprises dans :** projet 1 (étapes 2 à 5) et projet 2 (étape 2) ; `assertSame` et le cache des `Integer` au projet 7 (étape 6). Si un défi te semble totalement inconnu, ce drill arrive trop tôt : refais d'abord la leçon de ce projet.

<details><summary><b>Comment faire ce drill, concrètement</b> (à lire la 1re fois)</summary>

1. **Note l'heure** de départ (montre ou téléphone).
2. **Crée la classe** : clic droit sur le dossier `r01_assertions` → **New** → **Java Class** → `Recall01Test`.
3. **Écris une méthode `@Test` par défi**, nommée `d01`, `d02`… Le test fait **exactement** la vérification décrite, avec l'assertion demandée.
4. **Lance `Recall01Test`** (flèche verte à côté de la classe) : tout doit être vert.
5. **Bloqué plus de 3 minutes sur un défi ?** Écris un commentaire `// D03 : ✗` à sa place, et passe au suivant.
6. **Lance `Check.java`** (flèche verte). Il lance tes tests et affiche une ligne par méthode : `d01 : 1 executions, 1 reussies`. Une ligne `[FAIL]` te montre `attendu` et `obtenu`.
7. **Ensuite seulement**, ouvre la **carte mémoire** tout en bas (en aperçu Markdown, clique sur le triangle « Ouvrir la carte »), relis tes ✗, et fais les **expériences**.
8. **Note** la date, ton temps et tes ✗ dans le tableau de [`drills/README.md`](../README.md).

</details>

## Défis

- ☐ **D01.** `assertEquals` : `Math.max(3, 7)` vaut 7.
  → `d01 : 1 executions, 1 reussies`
- ☐ **D02.** `assertNotEquals` : `"java"` et `"JAVA"` sont différents.
  → `d02 : 1 executions, 1 reussies`
- ☐ **D03.** `assertTrue` : `"kayak"` retourné avec un `StringBuilder` est égal à `"kayak"`. Puis `assertFalse` : `"java".isBlank()`.
  → `d03 : 1 executions, 1 reussies`
- ☐ **D04.** Avec `Map.of("a", 1)` : `assertNull` sur la valeur de `"b"`, `assertNotNull` sur celle de `"a"`.
  → `d04 : 1 executions, 1 reussies`
- ☐ **D05.** `assertSame` : `Integer.valueOf(127)` et `Integer.valueOf(127)`. `assertNotSame` : `Integer.valueOf(128)` et `Integer.valueOf(128)`.
  → `d05 : 1 executions, 1 reussies`
- ☐ **D06.** Trie le tableau `{5, 3, 9, 1}` avec `Arrays.sort`, puis `assertArrayEquals` avec le tableau trié attendu.
  → `d06 : 1 executions, 1 reussies`
- ☐ **D07.** `assertIterableEquals` : `List.of(1, 2, 3)` et une `ArrayList` qui contient 1, 2, 3.
  → `d07 : 1 executions, 1 reussies`
- ☐ **D08.** `assertThrows` : `Integer.parseInt("x")` lance une `NumberFormatException` ; vérifie son message exact avec `assertEquals`.
  → `d08 : 1 executions, 1 reussies`
- ☐ **D09.** `assertDoesNotThrow` : `Integer.parseInt("42")` ; **range** sa valeur de retour dans un `int`, puis vérifie qu'elle vaut 42.
  → `d09 : 1 executions, 1 reussies`
- ☐ **D10.** `assertAll` avec trois vérifications sur `"Bonjour"` : sa longueur, qu'il commence par `"Bon"`, et son caractère d'indice 3.
  → `d10 : 1 executions, 1 reussies`
- ☐ **D11.** `assertEquals` à trois arguments : `0.1 + 0.2` vaut `0.3`, à `1e-9` près.
  → `d11 : 1 executions, 1 reussies`
- ☐ **D12.** `assertTimeout` d'une seconde autour de : la somme de 1 à 100 (`IntStream.rangeClosed`) vaut 5050.
  → `d12 : 1 executions, 1 reussies`

## Expériences (après le drill)

1. Dans D05, remplace 127 par 128 dans `assertSame`. Recopie le message d'échec.
2. Dans D06, remplace `assertArrayEquals` par `assertEquals`. Vert ou rouge ? Pourquoi ?
3. Dans D07, remplace `assertIterableEquals` par `assertEquals`. Vert ou rouge ? Pourquoi ?

## Sortie attendue complète

```
d01 : 1 executions, 1 reussies
d02 : 1 executions, 1 reussies
d03 : 1 executions, 1 reussies
d04 : 1 executions, 1 reussies
d05 : 1 executions, 1 reussies
d06 : 1 executions, 1 reussies
d07 : 1 executions, 1 reussies
d08 : 1 executions, 1 reussies
d09 : 1 executions, 1 reussies
d10 : 1 executions, 1 reussies
d11 : 1 executions, 1 reussies
d12 : 1 executions, 1 reussies
```

<details><summary>Ouvrir la carte</summary>

| Assertion | Vérifie | Piège |
|---|---|---|
| `assertEquals(attendu, obtenu)` | `equals` | l'ordre : **attendu d'abord** |
| `assertEquals(a, b, delta)` | deux `double` à `delta` près | sans delta, `0.1 + 0.2` ≠ `0.3` |
| `assertNotEquals(a, b)` | `!equals` | |
| `assertTrue(c)` / `assertFalse(c)` | un `boolean` | message pauvre : préférer `assertEquals` si possible |
| `assertNull(x)` / `assertNotNull(x)` | `null` ou pas | |
| `assertSame(a, b)` / `assertNotSame(a, b)` | `==` (le **même objet**) | cache des `Integer` : −128 à 127 |
| `assertArrayEquals(a, b)` | le contenu de deux tableaux | `assertEquals` sur des tableaux compare les **références** |
| `assertIterableEquals(a, b)` | les éléments, dans l'ordre | le type de collection ne compte pas |
| `assertThrows(T.class, () -> …)` | une exception de type `T` (ou sous-type) | **rend** l'exception : vérifier son message |
| `assertDoesNotThrow(() -> …)` | aucune exception | **rend** la valeur de la lambda |
| `assertAll(() -> …, () -> …)` | tout, et signale **tous** les échecs | |
| `assertTimeout(Duration, () -> …)` | fini à temps | attend la fin ; `assertTimeoutPreemptively` coupe |

- **Les imports :** `import static org.junit.jupiter.api.Assertions.*;` (ou un par un).
- **Un dernier argument** `String` (ou `Supplier<String>`) s'ajoute au message d'échec : `assertEquals(7, x, "le maximum")`.

**Les expériences** (vérifiées avec JUnit 5.11.4) :
1. `assertSame` avec 128 échoue : `expected: java.lang.Integer@7bc1a03d<128> but was: java.lang.Integer@70b0b186<128>`. Même valeur (`<128>`), mais deux objets différents (les numéros après `@` changent à chaque lancement).
2. `assertEquals` sur deux tableaux est **rouge** : `expected: [I@14a2f921<[1, 3, 5, 9]> but was: [I@3c87521<[1, 3, 5, 9]>`. Un tableau n'a pas d'`equals` qui compare le contenu : `assertEquals` compare donc les références.
3. `assertEquals` sur `List.of(1, 2, 3)` et l'`ArrayList` est **vert** : le `equals` des listes compare les éléments, quel que soit le type de liste.

</details>
