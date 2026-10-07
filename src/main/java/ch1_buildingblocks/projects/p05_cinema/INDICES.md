# Projet 5 (capstone) — Indices, étape par étape

> **Comment s'en servir :** c'est le capstone : essaie **vraiment** sans aide d'abord, en relisant tes projets 1 à 4. N'ouvre un indice qu'après **20 minutes** bloqué. L'**indice 1** renvoie au projet où la notion a été vue ; l'**indice 2** est plus précis. Pour vérifier tes **réponses aux questions**, c'est [`solution/CORRIGE.md`](solution/CORRIGE.md), une fois l'étape terminée.

---

## Étape 1 — Le modèle (paquet `model`)

<details><summary>Indice 1</summary>

- Le journal d'initialisation reprend la technique du **projet 3** : un compteur `static` et une méthode « journaliser puis rendre » utilisée dans les initialiseurs de champs.
- Le calcul des montants reprend le **projet 1**.
- Les classes publiques réparties dans un paquet reprennent le **projet 4**.

</details>

<details><summary>Indice 2</summary>

L'ordre du texte dans la séance doit être : le champ salle (initialisé par la méthode de journal), puis le bloc `{ }` qui lit `this.capacite`, puis le champ capacité (initialisé par la méthode de journal), puis le constructeur. Le compteur et les méthodes de journal peuvent vivre dans la séance elle-même, en `static`.

</details>

---

## Étape 2 — Le lanceur (paquet `app`)

<details><summary>Indice 1</summary>

Un import explicite par classe : `import ch1_buildingblocks.projects.p05_cinema.model.NomDeClasse;`. Les conversions sont celles du **projet 1** (étape 3), plus une méthode d'`Integer` que tu n'as pas encore utilisée.

</details>

<details><summary>Indice 2</summary>

La méthode cherchée s'appelle `Integer.decode`. Elle prend un seul `String` et lit le préfixe pour choisir la base.

</details>

---

## Étape 3 — Le billet

<details><summary>Indice 1</summary>

Sous-total = 2 × 1150 + 3 × 850. La ligne « Code promo » réaffiche le **texte** de l'argument (`0x0F`) et le **nombre** décodé (`15`) : ce sont deux valeurs différentes.

</details>

<details><summary>Indice 2</summary>

- La remise en division entière : 72750 / 100 donne 727.
- Pour le pied : les `"""` fermants se placent **5 colonnes à gauche** du texte, seuls sur leur ligne. Le texte finit donc par un saut de ligne : `print`, pas `println`.
- L'en-tête, lui, colle ses `"""` au dernier `+`.

</details>

---

## Étape 4 — Revue finale

<details><summary>Indice 1</summary>

Compte un fichier par classe publique. Pour les commandes, reprends ton `commandes.sh` du **projet 4** (étapes 1 et 2), en changeant les chemins, le nom de classe et les arguments.

</details>

<details><summary>Indice 2</summary>

Pour le ramasse-miettes : quand `main` se termine, que deviennent ses variables locales ? Et pendant le `main`, y a-t-il des objets créés **sans** être rangés dans une variable ?

</details>
