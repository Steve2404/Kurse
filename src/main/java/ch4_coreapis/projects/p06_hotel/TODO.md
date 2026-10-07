# Projet 6 (CAPSTONE) — Les réservations d'un hôtel

> Première fois ? Lis d'abord le mode d'emploi [`ch4_coreapis/PARCOURS.md`](../../PARCOURS.md).
>
> **Bloqué sur une étape ?** [`INDICES.md`](INDICES.md) donne deux indices repliés par étape, sans code. **Étape finie ?** [`solution/CORRIGE.md`](solution/CORRIGE.md) donne, étape par étape, le code de l'étape, les **réponses aux questions** et le résultat exact des **expériences**. N'ouvre que la section de l'étape que tu viens de faire.

**Notions visées :** tout le chapitre 4 à la fois.
- l'analyse de lignes (`split`, `strip`, `substring`, la casse) ;
- `StringBuilder` ;
- `String.format` (sans `%f`) et `String.join` ;
- des **tableaux parallèles** et un **tableau 2D** ;
- `Arrays.sort` / `copyOf` ;
- `LocalDate.parse`, `ChronoUnit`, `DayOfWeek` ;
- `Math.round` ;
- un algorithme de **chevauchement d'intervalles**.

**Ce qui est donné :** `Data.java` (les chambres et les réservations) et `Check.java`.

**Ce que TU crées :** tout le programme, dans le paquet `ch4_coreapis.projects.p06_hotel`. La classe du `main` s'appelle **`Hotel`**.

**Règle du crescendo :** chapitres 1 à 4. Pas de collection, pas de `record` ni de classe « réservation » obligatoire : les tableaux parallèles suffisent. Au chapitre 5, tu pourras les remplacer par des objets.

**Indication :** peu d'indices. Recalcule les factures, les conflits et le planning **à la main**.

**Tes outils pour ce projet** (pas d'arguments) :

```
javac -d build/ch4-p06 -sourcepath src/main/java src/main/java/ch4_coreapis/projects/p06_hotel/Hotel.java
java "-Duser.language=fr" -cp build/ch4-p06 ch4_coreapis.projects.p06_hotel.Hotel
```

**Ce projet réutilise tout le chapitre :** `split` et `parseInt` (projet 1), `String.format` (projet 1, étape 6), les dates (projet 5), les tableaux 2D (projet 3), `StringBuilder` (projet 2), `Arrays.sort` et `String.join` (projet 4).

---

## Le problème

Un petit hôtel de 3 chambres veut :
1. **Les factures.** Une ligne par réservation, alignée en colonnes :
   - le client au **nom normalisé** (`"  lea MARTIN "` devient `Lea Martin`) ;
   - le type de chambre, les dates et le nombre de nuits ;
   - le prix ;
   - une référence `LEA-352-101`, faite des 3 premières lettres du prénom en majuscules, du jour de l'année de l'arrivée et du numéro de chambre.
2. **Le prix d'un séjour :** nuit par nuit. Les nuits du **vendredi et du samedi** sont majorées de `Data.WEEKEND_PERCENT` %. Le jour du départ n'est **pas** une nuit. Calcule en **centimes** (`"120.50"` devient 12050) pour éviter les erreurs d'arrondi des `double`.
3. **Le chiffre d'affaires.**
4. **Les conflits :** deux réservations **de la même chambre** qui se chevauchent, avec le nombre de nuits communes et la première d'entre elles.
5. **Le planning :** pour chaque chambre et chacune des 10 nuits à partir du 18/12 :
   - `.` si elle est libre ;
   - `#` si elle est réservée une fois ;
   - `!` si elle est **surréservée**.
6. **La liste des clients**, triée.

---

## Tableau de bord

### ☐ Étape 1 — Lire les données

**📖 La leçon : des tableaux parallèles.** Sans classe ni collection, on peut ranger des fiches dans **plusieurs tableaux de même taille** : la case `i` de chaque tableau décrit la **même** fiche.

```java
String[] noms = new String[3];
int[] ages = new int[3];
noms[0] = "Lea";  ages[0] = 8;     // la fiche n° 0
noms[1] = "Tom";  ages[1] = 10;    // la fiche n° 1
```

**Indice pour les centimes :** `"120.50"` contient un point. Coupe le texte en deux autour du point, puis convertis chaque morceau. Attention : dans une expression régulière, `.` veut dire « n'importe quel caractère ». Pour un vrai point, écris `"\\."`.

**👉 À toi :**

- **Les chambres et les réservations** : un `split(";")` par ligne. Range les champs dans des **tableaux parallèles** : l'indice i décrit la même réservation dans chaque tableau.
- **Les prix :** `"120.50"` se convertit en centimes **sans `double`**. Comment ?

### ☐ Étape 2 — Les factures

```
B1  Lea Martin    SIMPLE 2026-12-18 -> 2026-12-21  3 nuit(s)    272.00  ref LEA-352-101
CHIFFRE D'AFFAIRES : 2292.60
```

**📖 Rappel :** `String.format` avec des largeurs (projet 1, étape 6). Un nombre de centimes inférieur à 10 a besoin d'un zéro devant : `%02d` affiche un entier sur 2 chiffres, complété par des zéros.

**👉 À toi :**

- **Vérifie B1 à la main :** le 18 décembre 2026 est un vendredi. Calcule les trois nuits.
- **Les colonnes :** `String.format` avec des largeurs (`%-13s`, `%2d`, `%9s`).
- **Les centimes :** affiche-les avec **deux** chiffres après le point. Que donnerait un affichage naïf pour 8 centimes ?

### ☐ Étape 3 — Les conflits : l'algorithme

```
B2 et B4 : chambre 102, 1 nuit(s) en commun a partir du 2026-12-23
B3 et B6 : chambre 101, 1 nuit(s) en commun a partir du 2026-12-22
```

**📖 Conseil :** dessine chaque séjour comme un segment sur une frise de dates, arrivée incluse et départ **exclu**. Deux segments se chevauchent quand chacun **commence avant que l'autre finisse**.

**👉 À toi :**

- **La règle :** deux séjours `[a1, d1)` et `[a2, d2)` se chevauchent si `a1 < d2` **et** `a2 < d1`.
  - **Question :** pourquoi B1 (départ le 21) et B3 (arrivée le 21) ne sont-ils **pas** en conflit ?
- **Les nuits communes :** du plus tardif des deux débuts au plus précoce des deux départs.
- **Chaque paire est testée une seule fois :** une boucle `j` qui commence à `i + 1`.

### ☐ Étape 4 — Le planning en tableau 2D

```
      18 19 20 21 22 23 24 25 26 27
101    #  #  #  #  !  #  .  .  .  .
```

**📖 Rappel :** la grille de comptage est un `int[][]` (projet 3). Une ligne de texte se construit avec un `StringBuilder` et `append` (projet 2).

**👉 À toi :**

- Un `int[chambres][nuits]` qui compte les réservations de chaque nuit, puis un `StringBuilder` par ligne.
- **Question :** une nuit `n` appartient au séjour si `arrivée ≤ n < départ`. Comment l'écrire avec `isBefore` seulement ?

### ☐ Étape 5 — Les clients triés

```
CLIENTS : Adam Leroy, Hugo Durand, Ines Petit, Lea Martin, Tom Robert, Zoe Bernard
```

**📖 Rappel :** copier un tableau avant de le trier (projet 4, étape 1), puis `String.join(", ", copie)`.

**👉 À toi :**

- Trie une **copie** du tableau, puis assemble les noms avec `String.join`.

---

## Checklist (vérifiée par `Check`)

`split`, `strip`, `substring`, `LocalDate.parse`, `ChronoUnit.DAYS.between`, `isBefore`, `isAfter`, `getDayOfYear`, `new StringBuilder(`, `String.format`, `String.join`, `Arrays.sort`, `Arrays.copyOf`, `Math.round`, un `int[][]` pour le planning ☐

---

## Sortie attendue complète

```
=== FACTURES ===
B1  Lea Martin    SIMPLE 2026-12-18 -> 2026-12-21  3 nuit(s)    272.00  ref LEA-352-101
B2  Hugo Durand   DOUBLE 2026-12-20 -> 2026-12-24  4 nuit(s)    482.00  ref HUG-354-102
B3  Ines Petit    SIMPLE 2026-12-21 -> 2026-12-23  2 nuit(s)    160.00  ref INE-355-101
B4  Tom Robert    DOUBLE 2026-12-23 -> 2026-12-26  3 nuit(s)    385.60  ref TOM-357-102
B5  Zoe Bernard   SUITE  2026-12-24 -> 2026-12-27  3 nuit(s)    833.00  ref ZOE-358-201
B6  Adam Leroy    SIMPLE 2026-12-22 -> 2026-12-24  2 nuit(s)    160.00  ref ADA-356-101
CHIFFRE D'AFFAIRES : 2292.60
=== CONFLITS ===
B2 et B4 : chambre 102, 1 nuit(s) en commun a partir du 2026-12-23
B3 et B6 : chambre 101, 1 nuit(s) en commun a partir du 2026-12-22
2 conflit(s)
=== PLANNING (nombre de reservations par nuit) ===
      18 19 20 21 22 23 24 25 26 27
101    #  #  #  #  !  #  .  .  .  .
102    .  .  #  #  #  !  #  #  .  .
201    .  .  .  .  .  .  #  #  #  .
CLIENTS : Adam Leroy, Hugo Durand, Ines Petit, Lea Martin, Tom Robert, Zoe Bernard
```
