# Projet 1 — La tarification d'un parking

> Première fois ? Lis d'abord le mode d'emploi [`ch2_operators/PARCOURS.md`](../../PARCOURS.md) : comment lire cette fiche, lancer `Check`, quoi faire en cas de blocage.

**Notions visées (chapitre 2) :**
- l'**opérateur ternaire** `? :`, simple et **imbriqué** ;
- les opérateurs de comparaison ;
- `!` ;
- la **division entière** et le **modulo** `%` ;
- les affectations composées `+=` / `-=` ;
- `++` (préfixe ou suffixe : lequel faut-il ?).

**Ce qui est donné :** `Check.java`. Les données arrivent par les arguments.

**Ce que TU crées :** tout le programme, dans le paquet `ch2_operators.projects.p01_parking`. La classe du `main` s'appelle **`Parking`**.

**Règle du crescendo :** chapitres 1 et 2 seulement. **Pas de `if`, pas de `switch`, pas de boucle** : ce sont des notions du chapitre 3. Chaque choix s'écrit avec un **ternaire**. Pas de méthode de `String`. `Check` refuse tout cela.

---

## Le problème

Un parking facture ses clients selon le véhicule, la durée, la nuit, l'abonnement et la fidélité. Le guichet calcule le ticket du client présent (arguments `2 135 true false 3`), puis 5 tickets de démonstration.

**Les règles :**
1. **Type de véhicule :**
   - 1 = MOTO, 1.50 €/h ;
   - 2 = VOITURE, 2.50 €/h ;
   - 3 = CAMION, 4.00 €/h.
2. **Les 15 premières minutes** sont gratuites. Seules les minutes **au-delà** sont facturées.
3. **Arrondi :** toute heure commencée est due. Par exemple, 1 minute facturable = 1 h.
4. **Nuit :** +50 % du montant.
5. **Plafond journalier :** 20.00 €, ou 35.00 € pour un camion.
6. **Abonné :** −20 % (après le plafond).
7. **Fidélité :** toute visite dont le numéro est un **multiple de 10** est offerte.

Tout se calcule en **centimes** (`int`), comme au chapitre 1.

---

## Tableau de bord

### ☐ Étape 1 — Les libellés et les tarifs

- Écris une méthode qui donne le **libellé** du véhicule, et une qui donne son **tarif horaire**.
- **Contrainte :** chacune tient en **une** expression : un ternaire **imbriqué** `a ? x : b ? y : z`.
- **Question :** comment Java lit-il `t == 1 ? "MOTO" : t == 2 ? "VOITURE" : "CAMION"` ? Où mettrais-tu des parenthèses pour le rendre explicite ?

### ☐ Étape 2 — Le calcul d'un ticket

**Exemple :**
```
#1 VOITURE | 135 min | nuit | 2 h | 7.50
```
Pour 135 min, il y a 120 minutes facturables, soit 2 h. 2 × 250 = 500, plus 50 % de nuit = 750. Le plafond ne joue pas, et ce n'est pas un abonné. Total : 7.50.

- **Les minutes facturables :** jamais négatives. C'est un ternaire.
- **L'heure commencée :** en **division entière**, sans `Math`. La formule classique est `(n + 59) / 60`. Vérifie-la sur 0, 1, 60 et 61.
- **La nuit :** ajoute la moitié du montant, ou rien, en **une** affectation composée avec un ternaire à droite.
- **Le plafond :** un ternaire, dont la limite dépend aussi du véhicule.
- **L'abonné :** retire 20 % avec `-=`.
  - **Question :** pourquoi `fee / 5` et pas `fee * 0.2` ? Que dirait `javac` si tu écrivais `fee = fee * 0.8;` ?
- **La fidélité :** utilise `%`.

### ☐ Étape 3 — Le texte du ticket

```
#2 MOTO | 10 min | jour | 0 h | gratuit (moins de 15 min)
#3 CAMION | 600 min | jour | abonne | 10 h | 28.00
#4 VOITURE | 61 min | nuit | abonne | 1 h | offert (10e visite)
```
- **Le numéro de ticket** est un compteur `static`, incrémenté **dans** l'expression du texte. Le 1er ticket porte le numéro 1.
  - `++n` ou `n++` : lequel ? Essaie les deux.
- **Les parties optionnelles** sont des ternaires : `nuit` ou `jour`, `| abonne` ou rien.
  - **Contrainte :** utilise l'opérateur `!` pour celle de l'abonné.
- **Le résultat, dans cet ordre de priorité :**
  1. `gratuit (moins de 15 min)` si 0 h ;
  2. sinon `offert (10e visite)` ;
  3. sinon le montant.
- **Piège de priorité :** `"…" + night ? "nuit" : "jour"` ne compile pas. Pourquoi ? Où faut-il les parenthèses ?

### ☐ Étape 4 — Le `main`

```
Tickets emis : 6, prochain numero : 7
```
- Le ticket du client présent vient des 5 arguments. Les 5 tickets de démonstration sont des appels écrits en dur (voir la sortie attendue).
- **Question :** dans `"… : " + (issued + 1)`, pourquoi les parenthèses ? Que donnerait le texte sans elles ?

---

## Checklist (vérifiée par `Check`)

| Élément | Étape | ☐ |
|---|---|---|
| au moins 2 ternaires imbriqués | 1, 3 | ☐ |
| `(n + 59) / 60` | 2 | ☐ |
| `+=`, `-=` | 2 | ☐ |
| `% 10 == 0` | 2 | ☐ |
| `++` | 3 | ☐ |
| `!` | 3 | ☐ |
| `Boolean.parseBoolean` | 4 | ☐ |

---

## Sortie attendue complète

```
#1 VOITURE | 135 min | nuit | 2 h | 7.50
#2 MOTO | 10 min | jour | 0 h | gratuit (moins de 15 min)
#3 CAMION | 600 min | jour | abonne | 10 h | 28.00
#4 VOITURE | 61 min | nuit | abonne | 1 h | offert (10e visite)
#5 MOTO | 16 min | jour | 1 h | 1.50
#6 CAMION | 1440 min | nuit | 24 h | offert (10e visite)
Tickets emis : 6, prochain numero : 7
```
