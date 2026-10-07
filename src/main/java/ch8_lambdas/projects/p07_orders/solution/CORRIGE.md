# Projet 7 (capstone) — Corrigé étape par étape

> **Quand le lire :** ouvre **seulement la section de l'étape** que tu viens de terminer, pour comparer ton code et tes réponses. Le programme complet est dans ce dossier : `Purchase`, `Promo`, `Validation` et `Engine` (qui contient le `main`).
>
> Les sorties ci-dessous ont été obtenues en direct avec **JDK 17** (`java` 17.0.18), sur une copie de la solution.

---

## Étape 1 — Les données

**Le code :** [`Purchase.java`](Purchase.java), [`Promo.java`](Promo.java) et [`Validation.java`](Validation.java).

**Un record qui contient des fonctions :** une `Promo`, c'est un code, une **condition** (`LongPredicate`) et un **effet** (`LongUnaryOperator`). `parse` fabrique les deux lambdas à partir du texte : elles capturent `value` et `minimum`.

**`apply` teste l'éligibilité au moment de l'appel :** c'est important quand on enchaîne les codes. Après une première remise, le montant a baissé, et un code « dès 30.00 » peut ne plus être éligible.

**Les interfaces `Long…` :** les montants sont des `long` en centimes. `LongPredicate` et `LongUnaryOperator` évitent tout boxing.

---

## Étape 2 — Le moteur

**Le code :** [`Engine.java`](Engine.java), sauf le `main`.

**Une règle = une `Validation`** créée à partir d'un texte. Pour `max-qty`, la branche du `switch` est un bloc : il déclare une `ToIntFunction` auxiliaire, puis `yield` la validation, qui la capture.

**Le currying :** `PERCENT_OFF` est une `Function<Integer, LongUnaryOperator>`. On lui donne un pourcentage, et elle **rend une fonction**. `PERCENT_OFF.apply(15)` est « la remise de 15 % », réutilisable à volonté. C'est une fonction à deux arguments (pourcentage, montant) découpée en deux appels.

**Le trajet de P1 :**
- 2 × 15.90 + 5 × 2.50 = **44.30** ;
- gold −15 % donne **37.65** ;
- TEN (−10 %) donne 33.88 (≥ 30.00), puis MINUS5 (−5.00) donne **28.88** ;
- moins de 50.00, donc port payant : 2 × 400 g + 5 × 20 g = 900 g, soit 1 kilo commencé, donc 4.90 + 1.00 = **5.90**.

**La livraison paresseuse :** le `LongSupplier` n'est appelé **que** si le prix est < 50.00. Sinon, le poids n'est jamais calculé. C'est la même idée que le rapport du projet 3.

**`promos[i]::apply`** est une référence de méthode **sur un objet précis** (la promo n° i). Elle s'adapte à `LongUnaryOperator`, car `apply(long)` rend un `long`.

---

## Étape 3 — Le programme

**Le code :** le `main` d'`Engine`.

**Trois `Consumer` en un :** chaque notification passe par le journal, le séparateur, puis le compteur. Le moteur ne sait pas ce qu'on fait de ses messages.

**Question — pourquoi `-10% puis -5.00` ≠ `-5.00 puis -10%` ?** La composition de fonctions n'est **pas commutative** :
- 10000 − 10 % = 9000, puis − 500 = **8500** ;
- 10000 − 500 = 9500, puis − 10 % = **8550**.

Un pourcentage s'applique à un montant **déjà réduit** ou non. C'est pour ça que `bestPromos` essaie les paires **dans les deux ordres** (i puis j, et j puis i).

**Question — à quoi sert la condition « le premier code a agi » ?** Sans elle, on peut afficher une paire dont le premier code n'a **rien fait**. Vérifié sur P6 (26.70, sous le minimum de MINUS5) : sans la condition, l'étiquette devient `[MINUS5 puis TEN]` au lieu de `[TEN]`, pour le même prix de 24.03. MINUS5 n'était pas éligible, et le client croirait avoir utilisé deux codes. La condition `single < amount` n'essaie une paire que si le 1er code a vraiment fait baisser le prix. La condition `pair < single` exige que le 2e code apporte quelque chose de plus.
