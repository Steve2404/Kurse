# Projet 7 (capstone) — Indices, étape par étape

> **Comment s'en servir :** c'est le capstone : essaie **vraiment** sans aide d'abord, en relisant les projets 1 à 6. N'ouvre un indice qu'après **20 minutes** bloqué. Pour vérifier tes **réponses aux questions**, c'est [`solution/CORRIGE.md`](solution/CORRIGE.md), une fois l'étape terminée.

---

## Étape 1 — La hiérarchie et le catalogue sans doublons

<details><summary>Indice 1</summary>

Trois niveaux : `Media`, puis `Book`, `Movie` et `AudioMedia`, puis `Album` et `Podcast` sous `AudioMedia`. Chaque `matches` redéfini commence par `super.matches(query) ||`, et chaque `toString` par `super.toString() +`.

</details>

<details><summary>Indice 2</summary>

- `equals` : `o instanceof Media m && m.kind().equals(kind()) && m.title.equals(title) && m.year == year`.
- Dans le catalogue, une boucle sur les médias déjà gardés teste `catalog[i].equals(m)`. Le doublon a quand même été **créé** : il a donc consommé un id.

</details>

---

## Étape 2 — Trier et chercher

<details><summary>Indice 1</summary>

Tri par insertion sur une copie (`System.arraycopy`). Condition de décalage : année plus petite, **ou** même année et titre plus grand (`compareTo(…) > 0`).

</details>

<details><summary>Indice 2</summary>

Pour chaque requête, une boucle sur le catalogue appelle `catalog[i].matches(q)`. C'est la liaison dynamique qui choisit la version de `Book`, `Movie` ou `AudioMedia`.

</details>

---

## Étape 3 — ISBN et minutes par type

<details><summary>Indice 1</summary>

Poids : `i % 2 == 0 ? 1 : 3` pour les 12 premiers chiffres, et `(digits.charAt(i) - '0')` donne la valeur d'un chiffre. Valide si la longueur vaut 13 **et** si le 13e chiffre égale `expectedCheck()`.

</details>

<details><summary>Indice 2</summary>

Minutes par type : un `int[4]` et un `String[] kinds = {"livre", "film", "album", "podcast"}`. Pour chaque média, trouve l'indice k où `kinds[k].equals(m.kind())` et additionne `m.minutes()`.

</details>

---

## Étape 4 — Recommandations et playlists

<details><summary>Indice 1</summary>

Jaccard : compte les tags communs (double boucle avec `equals`). L'union vaut `taille1 + taille2 - communs`. Pour les recommandations, un tableau `others` sans le média aimé, trié par similarité décroissante puis par titre.

</details>

<details><summary>Indice 2</summary>

- Sac à dos : `reachable[0] = true`. Pour chaque piste t, `for (int s = limit; s >= tracks[t]; s--)`, et si `!reachable[s] && reachable[s - tracks[t]]`, marque `s`, mémorise `lastTrack[s] = t` et `previous[s] = s - tracks[t]`.
- Le meilleur total : le plus grand `s` atteignable. Puis remonte avec `previous`.

</details>
