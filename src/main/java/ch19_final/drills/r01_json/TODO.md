# Drill de rappel 1 — Un mini JSON, de mémoire

> Première fois ? Lis d'abord le mode d'emploi [`ch19_final/PARCOURS.md`](../../PARCOURS.md). À faire après le projet p01.

**Chrono cible :** 35 min, puis 20 min.

**Règles :**
- Tout se fait de mémoire, **imports compris**, dans le paquet `ch19_final.drills.r01_json`.
- Cette fois, pas de types `Json…` : les valeurs sont des objets Java ordinaires. `null`, `Boolean`, `Long` (entiers seulement), `String`, `List<Object>` et `Map<String, Object>` (dans l'ordre du texte).
- Le texte tient sur **une** ligne ; seul l'espace `' '` sépare les éléments.
- Crée `public final class MiniJson`, avec `public static Object parse(String text)` et `public static String write(Object value)`, et `public class MiniJsonException extends RuntimeException`, construite avec `(int column, String reason)` : message `"colonne 4 : ',' ou ']' attendu"`, colonnes comptées à partir de 1.
- Ni `Pattern`, ni `matches(`, ni `split(`, ni `replace…`. Aucune méthode de plus de 18 lignes.
- Tu n'écris pas de tests : les tests de référence vérifient ton code.

**Les notions de ce drill ont été apprises dans :** projet 1 (étapes 2 à 6).

<details><summary><b>Comment faire ce drill, concrètement</b> (à lire la 1re fois)</summary>

1. Note l'heure. Crée les deux classes, et avance défi par défi : chaque défi ajoute une règle de la grammaire.
2. Bloqué plus de 3 minutes sur un défi : écris `// D04 : ✗` et passe au suivant.
3. Lance `Check.java` (flèche verte). Chaque ligne `dNN : 1 executions, 1 reussies` est un défi réussi ; un échec affiche le test et le message.
4. **Après seulement**, ouvre la carte mémoire en bas, pour tes ✗.
5. Note ton temps dans [`drills/README.md`](../README.md). Avant la prochaine répétition, supprime tes deux fichiers.

</details>

## Défis

- ☐ **D01.** `parse` : les mots `true`, `false`, `null` et les entiers (un `-` facultatif, au moins un chiffre, pas de `0` en tête sauf `0` seul), rendus en `Long` ; des espaces sont permis avant et après.
  → `d01 : 1 executions, 1 reussies`
- ☐ **D02.** Les chaînes, avec les échappements `\"`, `\\`, `\/`, `\n`, `\t` et `é` (4 chiffres hexadécimaux, majuscules ou minuscules).
  → `d02 : 1 executions, 1 reussies`
- ☐ **D03.** Les tableaux, imbriqués, avec des espaces partout, en `List<Object>` (qui peut contenir `null`).
  → `d03 : 1 executions, 1 reussies`
- ☐ **D04.** Les objets, en `Map<String, Object>` qui garde l'**ordre** des clés (une valeur peut être `null`).
  → `d04 : 1 executions, 1 reussies`
- ☐ **D05.** Les erreurs, colonne et raison exactes : `fin du texte inattendue` (à la fin du texte), `valeur attendue`, `nombre invalide` (au **début** du nombre, y compris un nombre trop grand pour un `long`), `',' ou ']' attendu`, `',' ou '}' attendu`, `cle attendue`, `':' attendu`, `chaine non terminee` (à la fin du texte), `echappement invalide` (sur l'**antislash**), `texte en trop`.
  → `d05 : 1 executions, 1 reussies`
- ☐ **D06.** `write` : la version compacte (aucun espace), les clés d'une `Map` dans son ordre ; `Long` et `Integer` écrits tels quels ; dans les chaînes, `"` et `\` précédés d'un antislash, `\n` et `\t` en forme courte, les autres caractères de code < `0x20` en `\u` + 4 chiffres hexadécimaux en minuscules ; tout autre type → `IllegalArgumentException("type non JSON : Double")` (le nom simple de la classe).
  → `d06 : 1 executions, 1 reussies`

## Sortie attendue complète

```
d01 : 1 executions, 1 reussies
d02 : 1 executions, 1 reussies
d03 : 1 executions, 1 reussies
d04 : 1 executions, 1 reussies
d05 : 1 executions, 1 reussies
d06 : 1 executions, 1 reussies
```

<details><summary>Ouvrir la carte</summary>

- **Le squelette** : un constructeur privé (le texte), un champ `pos`, et `parse` qui fait `value()`, saute les espaces, puis refuse le **texte en trop**.
- **`value()`** regarde un caractère : `[` → tableau, `{` → objet, `"` → chaîne, `-` ou chiffre → nombre, sinon un mot (`text.startsWith(mot, pos)`).
- **`peek()`** saute les espaces, lance `fin du texte inattendue` si le texte est fini, et rend le caractère courant : il évite dix tests.
- **La boucle d'un conteneur** : après l'ouvrant, si `peek()` voit le fermant → vide ; sinon `do { élément } while (next(fermant, message))`, où `next` rend vrai sur `,`, faux sur le fermant, et lance l'erreur sinon.
- **Les erreurs** : `new MiniJsonException(pos + 1, raison)` ; le nombre retient son `start` pour pointer son début ; l'échappement pointe l'antislash (`pos - 1` quand `pos` est déjà après la lettre).
- **`write`** : une chaîne de `instanceof` (`null` d'abord, puis `Boolean`, `Long`, `Integer`, `String`, `List<?>`, `Map<?, ?>`), un `StringBuilder` passé partout, et `String.format("\\u%04x", (int) c)`.

</details>
