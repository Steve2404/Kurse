# Projet 5 — Corrigé étape par étape

> **Quand le lire :** ouvre **seulement la section de l'étape** que tu viens de terminer. Le code de référence est dans ce dossier, les tests de référence dans [`EmailTest.java`](EmailTest.java).
>
> Les valeurs ci-dessous ont été obtenues en direct avec **JDK 17** et **JUnit 5.11.4**.

---

## Étape 1 — Les trois surprises

La sortie de `Data` (vérifiée) :

```
1. de contact@boulangerie.fr a [ada@example.org], sujet Promo (URGENT) : -20 % sur les tartes
2. de contact@boulangerie.fr a [bob@example.org], sujet null : null
3. de contact@boulangerie.fr a [chef@boulangerie.fr, stagiaire@example.org], sujet Planning : Lundi 6 h
```

**Question — les surprises :**
1. **Le paramètre muet :** `true` veut dire « urgent », mais rien ne le dit à l'appel ; il faut ouvrir la classe. Et si l'on inverse le sujet et le corps (deux `String`), le compilateur ne voit rien.
2. **L'objet à moitié construit :** le constructeur à 2 paramètres crée un e-mail **sans sujet ni corps** (`null`), et rien ne l'empêche de partir. Le jour où un code fait `email.subject().length()`, c'est une `NullPointerException` loin de la cause.
3. **L'objet modifié dans le dos :** `setTo` garde **la même** liste que l'appelant ; quand l'appelant ajoute le stagiaire à **sa** liste, le mémo change aussi. Un e-mail déjà « prêt » part à quelqu'un qui n'était pas prévu.

**Question — combien de constructeurs :** 4 champs facultatifs, chacun présent ou absent : 2 × 2 × 2 × 2 = **16** combinaisons. Et c'est même **impossible** à écrire : `(from, to, subject)`, `(from, to, body)` et `(from, to, cc)` ont tous les trois les types `(String, String, String)` ; Java refuse deux constructeurs avec les mêmes types de paramètres. Le builder règle les deux problèmes : chaque valeur a un nom, et l'on ne donne que celles qu'on veut.

---

## Étape 2 — La fabrique statique

Le code : [`EmailAddress.java`](EmailAddress.java). Les tests : `addressesAreNormalizedAndShared` et `invalidAddresses`.

**Expérience — le constructeur privé** (vérifié) :

```
error: invalid canonical constructor in record EmailAddress
  (attempting to assign stronger access privileges; was public)
```

Le constructeur canonique d'un record doit être **au moins aussi visible** que le record : on ne peut pas le cacher.

**Question — `new EmailAddress(…)` :** il **contourne** la normalisation et le cache. Ici, ce n'est pas grave : la validation est dans le constructeur compact, donc `new EmailAddress("Ada@example.org")` est **refusé** (majuscule), et une adresse déjà propre crée seulement un objet en double, égal (`equals`) à celui du cache. On ne perd que le partage. Pour **interdire** vraiment `new`, il faudrait une classe ordinaire avec un constructeur privé, au lieu d'un record.

**Question — le cache sans fin :** si l'application voit des **millions** d'adresses différentes (un service d'envoi en masse qui tourne des mois), le cache grossit sans jamais rendre la mémoire : une **fuite de mémoire**. Un cache sert quand peu de valeurs reviennent souvent (les adresses des clients d'une boulangerie). Sinon, il faut le **borner** (le cache LRU du chapitre 17) ou s'en passer.

---

## Étape 3 — L'e-mail immuable et son builder

Le code : [`Email.java`](Email.java) (avec `Email.Builder`), [`Priority.java`](Priority.java), [`Attachment.java`](Attachment.java). Les tests : `buildValidatesEverything`, `attachmentsUpToTheLimitIncluded`, `builtEmailIsIndependentFromTheBuilder`.

**Question — `IllegalStateException` :** l'erreur ne vient pas d'**un** argument faux passé à `build()` (il n'en a pas), mais de l'**état** du builder au moment de l'appel : « tu me demandes de construire alors qu'il manque l'expéditeur ». C'est exactement le sens d'`IllegalStateException` (« l'objet n'est pas dans un état qui permet cet appel »). `attach("vide.txt", 0)`, lui, reçoit un argument faux : `IllegalArgumentException`.

**Question — empêcher la création sans `build()` :** le constructeur `Email(Builder b)` est **privé** : seul du code écrit **dans** la classe `Email` (donc le builder imbriqué) peut l'appeler. Et la classe est `final` : personne ne peut en hériter pour ajouter un constructeur. `Check` vérifie qu'il n'y a pas de `public Email(`.

---

## Étape 4 — Le rendu, `toBuilder()` et les fabriques

Les tests : `fullEmailRendersEveryLine`, `optionalLinesAreHiddenAndDefaultsApply`, `toBuilderMakesAVariantAndLeavesTheOriginal`, `staticFactories`. Le rendu de `orderReady("ada@example.org", "CMD-7")` (vérifié) :

```
De : contact@boulangerie.fr
A : ada@example.org
Sujet : Commande CMD-7 prete
Priorite : HIGH

Votre commande vous attend au comptoir.
```

**Question — `render()` et la responsabilité unique :** un peu, oui : c'est une **présentation** (le texte brut) rangée dans l'objet de données. Tant qu'il n'y a **qu'un** format, la garder ici est raisonnable : une méthode de 12 lignes ne justifie pas une classe de plus (ne pas sur-concevoir). Le jour où arrive un **deuxième** format (HTML, ou le format brut d'un vrai serveur de mail), on sort la présentation dans des classes à part (`TextRenderer`, `HtmlRenderer`) qui lisent l'`Email` par ses accesseurs : l'e-mail redevient une simple donnée.

---

## Étape 5 — Les mutants

Les 16 mutants sont tués par les 14 tests de référence. Les deux mutants de copie défensive (6 et 7) ne sont tués que par `builtEmailIsIndependentFromTheBuilder` : sans un test qui **modifie le builder après `build()`**, une copie oubliée ne se voit jamais, puisque tout marche tant que personne ne réutilise le builder.
