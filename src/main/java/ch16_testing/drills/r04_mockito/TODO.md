# Drill de rappel 4 — L'API de Mockito

> Première fois ? Lis d'abord le mode d'emploi [`ch16_testing/PARCOURS.md`](../../PARCOURS.md). À faire après le projet p06.

**Chrono cible :** 15 min, puis 8 min.

**Règles :**
- Tout se fait de mémoire, **imports compris** (`org.mockito.*`, `org.mockito.junit.jupiter.MockitoExtension`, et les `import static` de `Mockito` et `ArgumentMatchers`).
- Crée la classe de test **`Recall04Test`** dans le paquet `ch16_testing.drills.r04_mockito`, avec `@ExtendWith(MockitoExtension.class)`.
- Déclare **dans** `Recall04Test` trois interfaces imbriquées :
  - `Thermometer` : `int celsius()` ;
  - `Display` : `void show(String text)` et `void clear()` ;
  - `Converter` : `String convert(String input)`.
- Un champ `@Mock Display display;`. Ailleurs, crée les simulacres avec `mock(…)`.
- Un défi = une méthode `@Test` nommée `d01`, `d02`… Tous les tests doivent **passer**.

**Les notions de ce drill ont été apprises dans :** projet 6 (étapes 2 à 5), projet 8 (`lenient`). `thenAnswer`, `spy`, `doReturn`, `doThrow`, `atLeastOnce`, `getAllValues` et `argThat` sont **nouveaux** : la carte mémoire les explique.

<details><summary><b>Comment faire ce drill, concrètement</b> (à lire la 1re fois)</summary>

1. **Note l'heure** de départ.
2. **Crée la classe** : clic droit sur le dossier `r04_mockito` → **New** → **Java Class** → `Recall04Test`.
3. **Écris une méthode par défi.** Lance la classe : tout doit être vert. (La ligne `Sharing is only supported…` est normale : c'est Mockito.)
4. **Bloqué plus de 3 minutes ?** `// D04 : ✗`, et défi suivant.
5. **Lance `Check.java`.**
6. **Ensuite seulement**, la carte mémoire, puis les expériences.
7. **Note** date, temps et ✗ dans [`drills/README.md`](../README.md).

</details>

## Défis

- ☐ **D01.** Un simulacre de `Thermometer` qui répond 21 à `celsius()` ; vérifie la réponse.
  → `d01 : 1 executions, 1 reussies`
- ☐ **D02.** Sans rien préparer : `convert("x")` d'un simulacre de `Converter` rend `null`, et `celsius()` d'un simulacre de `Thermometer` rend 0 (dans un `assertAll`).
  → `d02 : 1 executions, 1 reussies`
- ☐ **D03.** `celsius()` lance `new IllegalStateException("panne")` ; vérifie le type et le message.
  → `d03 : 1 executions, 1 reussies`
- ☐ **D04.** `celsius()` répond 1, puis 2, puis 3 (un seul `thenReturn` à trois arguments). Quatre appels de suite donnent `List.of(1, 2, 3, 3)`.
  → `d04 : 1 executions, 1 reussies`
- ☐ **D05.** Appelle `display.show("a")` deux fois. Vérifie : exactement 2 fois `show("a")` ; jamais `clear()` ; au moins une fois `show` avec n'importe quel texte.
  → `d05 : 1 executions, 1 reussies`
- ☐ **D06.** `display.show("bonjour")` puis `display.show("au revoir")`. Un `ArgumentCaptor<String>` attrape les deux textes : `getValue()` rend le **dernier**, `getAllValues()` les deux, dans l'ordre.
  → `d06 : 1 executions, 1 reussies`
- ☐ **D07.** `display.clear()` puis `display.show("x")` ; vérifie l'**ordre** avec `InOrder`.
  → `d07 : 1 executions, 1 reussies`
- ☐ **D08.** `convert(anyString())` répond **le texte reçu, en majuscules** (`thenAnswer`, avec `invocation.getArgument(0, String.class)`). `convert("abc")` vaut `"ABC"`.
  → `d08 : 1 executions, 1 reussies`
- ☐ **D09.** Un **espion** d'une vraie `ArrayList<String>` : ajoute `"a"`, vérifie l'appel `add("a")` et la taille réelle (1). Puis, avec `doReturn(100).when(…)`, fais répondre 100 à `size()`, et vérifie-le.
  → `d09 : 1 executions, 1 reussies`
- ☐ **D10.** `display.clear()` (une méthode `void`) lance `new IllegalStateException("ecran casse")` : utilise `doThrow(…).when(display).clear()`. Vérifie le message.
  → `d10 : 1 executions, 1 reussies`
- ☐ **D11.** Un simulacre de `Display` jamais utilisé : `verifyNoInteractions`. Puis `display.show("seul appel")`, vérifié, suivi de `verifyNoMoreInteractions(display)`.
  → `d11 : 1 executions, 1 reussies`
- ☐ **D12.** Un simulacre de `Converter` : `convert(eq("a"))` répond `"A"`, et `convert(argThat(s -> s.startsWith("x")))` répond `"X"`. Vérifie `convert("a")` et `convert("xyz")`.
  → `d12 : 1 executions, 1 reussies`

## Expériences (après le drill)

1. Sur un espion d'une `ArrayList` **vide**, écris `when(liste.get(0)).thenReturn("x");`. Que se passe-t-il ? Et avec `doReturn("x").when(liste).get(0);` ?
2. Dans D10, écris `when(display.clear()).thenThrow(…)` au lieu de `doThrow`. Recopie l'erreur.

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

**Créer :**
- `@ExtendWith(MockitoExtension.class)` + `@Mock Type champ;` (+ `@InjectMocks` pour l'objet testé), ou `Type x = mock(Type.class);` ;
- `spy(objetReel)` : un **espion** d'un vrai objet ; ses méthodes font le vrai travail, sauf celles qu'on prépare.

**Préparer :**

| Écriture | Pour |
|---|---|
| `when(m.f(args)).thenReturn(v)` | rendre `v` |
| `.thenReturn(a, b, c)` | `a`, puis `b`, puis `c`, puis `c`… |
| `.thenThrow(new E(…))` | lancer une exception |
| `.thenAnswer(inv -> …)` | calculer la réponse ; `inv.getArgument(0, String.class)` lit un argument |
| `doReturn(v).when(m).f(args)` | sur un **espion** (n'appelle pas la vraie méthode) |
| `doThrow(new E()).when(m).f()` | sur une méthode **`void`** |
| `lenient().when(…)` | une réponse qui peut ne pas servir (Mockito strict) |

**Valeurs par défaut** d'un simulacre : `null`, 0, `false`, collections et `Optional` vides.

**Vérifier :**
- `verify(m).f(args)` = exactement 1 fois ; `verify(m, times(n))`, `never()`, `atLeastOnce()`, `atMost(n)` ;
- `verifyNoInteractions(m1, m2)` : jamais touchés ; `verifyNoMoreInteractions(m)` : rien d'autre que ce qui a été vérifié ;
- `InOrder o = inOrder(m1, m2); o.verify(m1).f(); o.verify(m2).g();`.

**Attraper :** `ArgumentCaptor<T> c = ArgumentCaptor.forClass(T.class); verify(m).f(c.capture());` puis `c.getValue()` (le dernier) ou `c.getAllValues()`.

**Matchers :** `any()`, `anyString()`, `anyInt()`, `anyLong()`, `eq(v)`, `argThat(x -> …)`. **Tous** les arguments en matchers, ou **aucun**.

**Les expériences** (vérifiées avec Mockito 5.14.2) :
1. `when(liste.get(0))` appelle la **vraie** méthode `get(0)` de l'espion, sur une liste vide : `IndexOutOfBoundsException: Index 0 out of bounds for length 0`, avant même que Mockito puisse préparer quoi que ce soit. Avec `doReturn("x").when(liste).get(0)`, la vraie méthode n'est pas appelée : `liste.get(0)` rend ensuite `"x"`.
2. Ça ne compile pas : `error: 'void' type not allowed here`. `when(…)` a besoin d'une **valeur** ; une méthode `void` n'en rend pas. D'où `doThrow(…).when(display).clear()`.

</details>
