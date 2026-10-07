# Projet 6 — Corrigé étape par étape

> **Quand le lire :** ouvre **seulement la section de l'étape** que tu viens de terminer, pour comparer ton code et tes réponses. Le programme complet est dans [`Hotel.java`](Hotel.java).

---

## Étape 1 — Lire les données

**Le code de l'étape :**

```java
for (int i = 0; i < Data.ROOMS.length; i++) {
    String[] p = Data.ROOMS[i].split(";");
    roomNumbers[i] = p[0];
    roomTypes[i] = p[1];
    roomCents[i] = Integer.parseInt(p[2].replace(".", ""));
}
for (int i = 0; i < n; i++) {
    String[] p = Data.BOOKINGS[i].split(";");
    codes[i] = p[0];
    guests[i] = normalize(p[1]);
    rooms[i] = roomIndex(p[2]);
    arrivals[i] = LocalDate.parse(p[3]);
    departures[i] = LocalDate.parse(p[4]);
}
```

**Les tableaux parallèles :** sans classe « Réservation » (chapitres 5 à 7), chaque champ a son tableau, et l'indice `i` fait le lien. C'est fragile : un tri sur un seul tableau casserait tout. Les records du chapitre 7 régleront ça.

**Question — centimes sans `double` :** `"120.50".replace(".", "")` donne `"12050"`, puis `parseInt` donne **12050**. Aucune virgule flottante, donc aucune erreur d'arrondi. Cela suppose **exactement 2 décimales** dans les données : `"80.5"` donnerait 805 au lieu de 8050.

**`rooms[i]` est un indice**, pas le numéro `101` : `roomIndex` cherche le numéro dans `roomNumbers` avec `equals`. On peut alors écrire `roomCents[rooms[i]]` directement.

---

## Étape 2 — Les factures

**Le code de l'étape :**

```java
static String money(long cents) {
    return cents / 100 + "." + (cents % 100 < 10 ? "0" : "") + cents % 100;
}

static String normalize(String raw) {
    StringBuilder sb = new StringBuilder();
    for (String part : raw.strip().split(" +")) {
        if (sb.length() > 0) {
            sb.append(' ');
        }
        sb.append(part.substring(0, 1).toUpperCase()).append(part.substring(1).toLowerCase());
    }
    return sb.toString();
}

static long price(int booking) {
    int base = roomCents[rooms[booking]];
    long total = 0;
    for (LocalDate night = arrivals[booking]; night.isBefore(departures[booking]); night = night.plusDays(1)) {
        DayOfWeek day = night.getDayOfWeek();
        boolean weekend = day == DayOfWeek.FRIDAY || day == DayOfWeek.SATURDAY;
        total += weekend ? Math.round(base * (100 + Data.WEEKEND_PERCENT) / 100.0) : base;
    }
    return total;
}

String reference = guests[i].substring(0, 3).toUpperCase() + "-" + arrivals[i].getDayOfYear() + "-" + roomNumbers[rooms[i]];
String.format("%-3s %-13s %-6s %s -> %s %2d nuit(s) %9s  ref %s", …)
```

**B1 à la main** (chambre 101, 80.00 €, du vendredi 18 au lundi 21) :

| Nuit | Jour | Prix |
|---|---|---|
| 18 | vendredi | 8000 × 1.20 = 9600 |
| 19 | samedi | 9600 |
| 20 | dimanche | 8000 |

Le 21 est le jour du départ, ce n'est pas une nuit. Total : 27200, soit **272.00**. La référence est `LEA-352-101` : le 18 décembre est le 352e jour de 2026.

**Question — l'affichage naïf de 8 centimes :** `cents / 100 + "." + cents % 100` donne **`0.8`**, lu comme 80 centimes. Le zéro de tête du nombre de centimes est perdu. D'où le ternaire `(cents % 100 < 10 ? "0" : "")`. C'est le même piège qu'au chapitre 1, corrigé autrement.

**Le `StringBuilder` dans `normalize` :** une boucle de concaténations de `String` crée un nouvel objet à chaque tour. Un `StringBuilder` modifie le même objet.

---

## Étape 3 — Les conflits : l'algorithme

**Le code de l'étape :**

```java
static boolean overlap(int i, int j) {
    return rooms[i] == rooms[j] && arrivals[i].isBefore(departures[j]) && arrivals[j].isBefore(departures[i]);
}

for (int i = 0; i < n; i++) {
    for (int j = i + 1; j < n; j++) {
        if (overlap(i, j)) {
            LocalDate from = arrivals[i].isAfter(arrivals[j]) ? arrivals[i] : arrivals[j];
            LocalDate to = departures[i].isBefore(departures[j]) ? departures[i] : departures[j];
            … ChronoUnit.DAYS.between(from, to) …
        }
    }
}
```

**Question — pourquoi B1 et B3 ne sont pas en conflit ?** B1 = `[18, 21)`, B3 = `[21, 23)`. Test : `21 < 21` est **faux**. La date de départ est **exclue** : Léa libère la chambre le 21 au matin, et Inès l'occupe le soir. Avec `<=`, on signalerait un faux conflit à chaque changement de client.

**Pourquoi `j = i + 1` :** chaque paire n'est testée qu'**une fois** (B2 et B4, jamais B4 et B2), et jamais une réservation avec elle-même. Le nombre de tests passe de n² à n(n − 1)/2.

**B2 et B4 :**
- B2 = `[20, 24)`, B4 = `[23, 26)`.
- Début commun : max(20, 23) = 23. Fin commune : min(24, 26) = 24.
- **1 nuit**, celle du 23.

---

## Étape 4 — Le planning en tableau 2D

**Le code de l'étape :**

```java
int[][] planning = new int[roomNumbers.length][Data.PLANNING_DAYS];
for (int i = 0; i < n; i++) {
    for (int d = 0; d < Data.PLANNING_DAYS; d++) {
        LocalDate night = start.plusDays(d);
        if (!night.isBefore(arrivals[i]) && night.isBefore(departures[i])) {
            planning[rooms[i]][d]++;
        }
    }
}
// en-tête : String.format("%3d", jour) ; ligne : count == 0 ? '.' : count == 1 ? '#' : '!'
```

**Question — `arrivée ≤ n < départ` avec `isBefore` seulement :**
- `arrivée ≤ n` est la négation de `n < arrivée`, donc `!n.isBefore(arrivée)`.
- `n < départ` s'écrit `n.isBefore(départ)`.

**Le `!` de la chambre 101 le 22 :** B3 `[21, 23)` et B6 `[22, 24)` contiennent tous deux la nuit du 22, d'où un compte de 2, donc `!`.

**Pourquoi un `int[][]` et pas un `boolean[][]` ?** Un booléen dit « occupé ou non ». Pour voir une **surréservation**, il faut **compter**.

---

## Étape 5 — Les clients triés

**Le code de l'étape :**

```java
String[] sortedGuests = Arrays.copyOf(guests, n);
Arrays.sort(sortedGuests);
System.out.println("CLIENTS : " + String.join(", ", sortedGuests));
```

**Trier une copie :** `guests` est un tableau **parallèle**. Le trier sur place décalerait les noms par rapport aux codes, aux chambres et aux dates. C'est le danger des tableaux parallèles signalé à l'étape 1.

**`String.join(", ", tableau)`** colle les éléments avec le séparateur **entre** eux, jamais en fin.
