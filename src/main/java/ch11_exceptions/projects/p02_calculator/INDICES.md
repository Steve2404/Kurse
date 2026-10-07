# Projet 2 — Indices, étape par étape

> **Comment s'en servir :** n'ouvre un indice qu'après **20 minutes** bloqué sur l'étape (palier 2 du `PARCOURS.md`). Ouvre l'**indice 1** d'abord ; l'**indice 2** seulement s'il ne suffit pas. Aucun indice ne donne le code : pour vérifier tes **réponses aux questions**, c'est [`solution/CORRIGE.md`](solution/CORRIGE.md), étape par étape, une fois l'étape terminée.

---

## Étape 1 — Les types

<details><summary>Indice 1</summary>

`SyntaxException` garde la position dans un champ `private final int`. Ses deux constructeurs appellent `super(message)` ou `super(message, cause)`, puis affectent la position.

</details>

<details><summary>Indice 2</summary>

Une interface fonctionnelle peut déclarer une exception vérifiée : `R apply(T value) throws SyntaxException;`. C'est justement ce que `Function` ne fait pas.

</details>

---

## Étape 2 — L'analyseur

<details><summary>Indice 1</summary>

`tokenize` : une boucle `while (i < s.length())`. Selon le caractère, on avance `i` sur une suite de chiffres ou de lettres, ou on prend un seul caractère. Le jeton garde sa position de **départ**.

</details>

<details><summary>Indice 2</summary>

- Une méthode par règle, comme au chapitre 6 (projet 6). La sentinelle `<fin>` évite tout débordement d'indice.
- Le nombre : `try { return Long.parseLong(t.text()); } catch (NumberFormatException e) { throw new SyntaxException("nombre trop grand", t.position(), e); }`.

</details>

---

## Étape 3 — Évaluer et afficher

<details><summary>Indice 1</summary>

`evaluate` : `try { return …; } catch (ArithmeticException | IllegalArgumentException e) { throw new EvaluationException(…, e); } finally { evaluations++; }`.

</details>

<details><summary>Indice 2</summary>

Dans `logged`, `throw e;` relance **la même** exception. Java analyse quelles exceptions vérifiées peuvent réellement arriver dans le `try`.

</details>

---

## Étape 4 — `Error`, `Optional` et lambdas

<details><summary>Indice 1</summary>

`tryEvaluate` : `try { return Optional.of(evaluate(text)); } catch (SyntaxException | EvaluationException e) { return Optional.empty(); }`.

</details>

<details><summary>Indice 2</summary>

`unchecked(f)` rend `value -> { try { return f.apply(value); } catch (SyntaxException e) { throw new RuntimeException("dans une lambda", e); } }`.

</details>
