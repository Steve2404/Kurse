# Projet 1 — Corrigé étape par étape

> **Quand le lire :** ouvre **seulement la section de l'étape** que tu viens de terminer, pour comparer ton code et tes réponses. Le programme complet est dans [`Parking.java`](Parking.java).
>
> Les messages d'erreur ci-dessous ont été obtenus en direct avec **JDK 17** (`javac` 17.0.18). `javac` affiche aussi la ligne fautive et un `^` sous l'endroit exact ; seule la 1re ligne du message est recopiée ici.

---

## Étape 1 — Les libellés et les tarifs

**Le code de l'étape :**

```java
static String label(int type) {
    return type == 1 ? "MOTO" : type == 2 ? "VOITURE" : "CAMION";
}

static int hourlyRate(int type) {
    return type == 1 ? 150 : type == 2 ? 250 : 400;
}
```

**Question — comment Java lit-il le ternaire imbriqué ?** Le ternaire est **associatif à droite**. Java lit :

```java
t == 1 ? "MOTO" : (t == 2 ? "VOITURE" : "CAMION")
```

Le 2e ternaire est la branche « sinon » du 1er. Les parenthèses ci-dessus rendent cette lecture explicite. Elles ne changent rien au résultat.

---

## Étape 2 — Le calcul d'un ticket

**Le code de l'étape :**

```java
static final int FREE_MINUTES = 15;
static final int DAILY_CAP = 2000;
static final int TRUCK_CAP = 3500;

int billable = minutes > FREE_MINUTES ? minutes - FREE_MINUTES : 0;
int hours = (billable + 59) / 60;
int fee = hours * hourlyRate(type);
fee += night ? fee / 2 : 0;
int cap = type == 3 ? TRUCK_CAP : DAILY_CAP;
fee = fee > cap ? cap : fee;
fee -= subscriber ? fee / 5 : 0;
boolean free = visit % 10 == 0;
int due = free ? 0 : fee;
```

**La formule `(n + 59) / 60`**, vérifiée :

| n | (n + 59) / 60 |
|---|---|
| 0 | 59 / 60 = **0** |
| 1 | 60 / 60 = **1** |
| 60 | 119 / 60 = **1** |
| 61 | 120 / 60 = **2** |

Ajouter 59 fait passer à l'heure suivante **dès** qu'une minute dépasse. La division entière tronque le reste.

**Question — pourquoi `fee / 5` et pas `fee * 0.2` ?**
- `fee / 5` reste en `int`, sans aucune conversion. C'est exact pour des centimes (avec troncature).
- `fee * 0.2` est un `double`. Le ranger dans un `int` demande une conversion avec perte.

**Expérience — `fee = fee * 0.8;` :**

```
error: incompatible types: possible lossy conversion from double to int
```

`int * double` est **promu** en `double`, et Java refuse de ranger un `double` dans un `int` sans cast. En revanche, `fee *= 0.8;` **compile** (et donne 400 pour 500). L'affectation composée contient un cast **caché** : `fee = (int) (fee * 0.8)`.

**Calcul du ticket #3 :** camion, 600 min, abonné.
- Minutes facturables : 600 − 15 = 585, soit (585 + 59) / 60 = **10 h**.
- Montant : 10 × 400 = 4000, plafonné à 3500.
- Remise abonné : 3500 − 700 = **2800**, affiché `28.00`.

---

## Étape 3 — Le texte du ticket

**Le code de l'étape :**

```java
String reason = hours == 0 ? "gratuit (moins de 15 min)" : free ? "offert (10e visite)" : euros(due);
return "#" + ++issued + " " + label(type) + " | " + minutes + " min | " + (night ? "nuit" : "jour")
        + (!subscriber ? "" : " | abonne") + " | " + hours + " h | " + reason;
```

**`++n` ou `n++` ?**
- `++issued` incrémente **puis** rend la nouvelle valeur : le 1er ticket affiche `#1`.
- `issued++` rend l'**ancienne** valeur (0), puis incrémente : le 1er ticket afficherait `#0`.

Vérifié : avec `n = 0`, `"#" + n++` affiche `#0` ; avec `m = 0`, `"#" + ++m` affiche `#1`. Dans les deux cas, la variable vaut 1 ensuite.

**Piège de priorité — `"…" + night ? "nuit" : "jour"` :**

```
error: incompatible types: String cannot be converted to boolean
```

Le `+` est **plus prioritaire** que `?:`. Java lit `("…" + night) ? "nuit" : "jour"`. La condition est alors un `String`, pas un `boolean`. Il faut écrire `"…" + (night ? "nuit" : "jour")`.

---

## Étape 4 — Le `main`

**Le code de l'étape :**

```java
System.out.println(ticket(Integer.parseInt(args[0]), Integer.parseInt(args[1]),
        Boolean.parseBoolean(args[2]), Boolean.parseBoolean(args[3]), Integer.parseInt(args[4])));
System.out.println(ticket(1, 10, false, false, 1));
System.out.println(ticket(3, 600, false, true, 4));
System.out.println(ticket(2, 61, true, true, 10));
System.out.println(ticket(1, 16, false, false, 7));
System.out.println(ticket(3, 1440, true, false, 20));
System.out.println("Tickets emis : " + issued + ", prochain numero : " + (issued + 1));
```

**Question — pourquoi `(issued + 1)` entre parenthèses ?** Sans elles, `"… : " + issued + 1` se lit de gauche à droite. On a d'abord `"… : 6"` (un `String`), puis `+ 1` **concatène** : on obtient `prochain numero : 61`. Les parenthèses font l'addition entière **avant** la concaténation.
