# Projet 8 — Indices, étape par étape

> **Comment s'en servir :** n'ouvre un indice qu'après **20 minutes** bloqué sur l'étape (palier 2 du `PARCOURS.md`). Ouvre l'**indice 1** d'abord ; l'**indice 2** seulement s'il ne suffit pas. Pour vérifier tes **réponses aux questions**, c'est [`solution/CORRIGE.md`](solution/CORRIGE.md), une fois l'étape terminée.

---

## Étape 1 — L'amende, en tableau

<details><summary>Indice 1</summary>

Il y a trois règles, donc trois sortes de limites : le retard nul ou négatif, les 3 jours offerts du premium, et le plafond de 1000. Pour chacune : juste sur la limite, et juste à côté.

</details>

<details><summary>Indice 2</summary>

Le plafond est atteint à 50 jours (50 × 20 = 1000) pour un adhérent normal, et à 53 jours pour un premium (53 − 3 = 50). Teste 50 et 51, puis 53 et 54.

</details>

---

## Étape 2 — Le décor

<details><summary>Indice 1</summary>

Le service est créé à la main dans le `@BeforeEach` : `service = new LibraryService(catalog, loans, members, mailer, CLOCK);`.

</details>

<details><summary>Indice 2</summary>

`lenient().when(members.find("M1")).thenReturn(Optional.of(ana));` : importe `lenient` depuis `org.mockito.Mockito`.

</details>

---

## Étape 3 — Emprunter

<details><summary>Indice 1</summary>

Pour une liste de N prêts identiques : `Collections.nCopies(n, unPret)` (chapitre 9). Une petite méthode `static Loan loan(String isbn, String member, LocalDate due)` dans le test évite de répéter les constructeurs.

</details>

<details><summary>Indice 2</summary>

Pour l'ordre des refus : un adhérent qui a **à la fois** un retard et 3 prêts. Le message doit être celui du retard.

</details>

---

## Étape 4 — Rendre

<details><summary>Indice 1</summary>

`verify(loans).close(lePret, TODAY)` vérifie à la fois le prêt clos et la date. Pour « aucun courriel » : `verifyNoInteractions(mailer)`.

</details>

<details><summary>Indice 2</summary>

Pour le « bon » prêt : prépare deux prêts en cours pour le même adhérent, et rends le **second**. Seul un filtre sur l'ISBN choisit le bon.

</details>

---

## Étape 5 — Les mutants

<details><summary>Indice 1</summary>

Relis les règles une par une. Pour chaque mot important (« avant », « au plus », « positive », « dans cet ordre », « le bon »), demande-toi : « lequel de mes tests casserait si ce mot changeait ? ».

</details>

<details><summary>Indice 2 : ce que change chaque mutant</summary>

1. Le 1er jour de retard est gratuit.
2. Les premiums ont 2 jours offerts au lieu de 3.
3. Plus de plafond.
4. Le plafond est à 1020.
5. Un prêt qui expire aujourd'hui compte comme un retard.
6. La limite est de 4 prêts (5 → 6 pour un premium).
7. Les premiums sont limités à 3 prêts.
8. On peut prêter un exemplaire de plus qu'il n'en existe.
9. Les premiums empruntent pour 21 jours.
10. Le prêt n'est pas enregistré.
11. Un livre inconnu n'est pas refusé.
12. Le prêt est clos à sa date prévue au lieu d'aujourd'hui.
13. Un courriel est envoyé même pour une amende de 0.
14. Le service clôt le premier prêt de l'adhérent, quel que soit le livre.
15. Le retard est calculé à l'envers (toujours négatif pour un retard).
16. La limite est vérifiée avant le retard.

</details>
