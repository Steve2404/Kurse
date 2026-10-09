# 🏠 Palais mental — chapitre 4 : la cuisine, stations 7 à 12

> **Avant :** lis une fois [`PALAIS_MENTAL.md`](../../../../PALAIS_MENTAL.md). Les stations 1 à 6 de la cuisine gardent le chapitre 3.
> **Quand :** après le capstone du chapitre, puis avant chaque répétition des drills.
> **Comment :** lis la règle, ferme les yeux, joue la scène 10 secondes, redis la règle à voix haute.

---

### 7. 🧊 Le frigo — `String` est immuable

- **Image :** dans le frigo, des **mots gravés dans des blocs de glace**. Tu tapes dessus avec `toUpperCase()` : le bloc ne change **pas**, le frigo t'en **crache un nouveau**. Si tu ne l'attrapes pas (pas d'affectation), il fond par terre. Tu découpes un bloc avec `substring(1, 4)` : le couteau s'arrête **juste avant** la case 4. Tu cherches une lettre absente avec `indexOf` : le frigo répond **−1** en clignotant.
- **À retenir :**
  - une `String` ne change **jamais** : `s.toUpperCase();` seul ne fait **rien** ; il faut `s = s.toUpperCase();` ;
  - `substring(debut, fin)` : `fin` est **exclue** ; `indexOf` rend **−1** si rien n'est trouvé ; `charAt` hors limites → `StringIndexOutOfBoundsException` ;
  - `+` se lit **de gauche à droite** : `1 + 2 + "a"` donne `"3a"`, mais `"a" + 1 + 2` donne `"a12"` ;
  - `strip()` enlève les blancs Unicode, `trim()` seulement les caractères ≤ espace ; `isBlank()` contre `isEmpty()`.
- **Mon image :** …

### 8. ❄️ Le congélateur — le pool de chaînes

- **Image :** le congélateur est le **pool**. Chaque littéral `"pomme"` y est rangé **une seule fois** : deux variables qui disent `"pomme"` montrent **le même sac**, donc `==` est vrai. Un `new String("pomme")` fabrique un **sac neuf posé sur la table**, hors du congélateur : `==` devient faux. `intern()` remet le sac **dans** le congélateur et te rend celui qui y était.
- **À retenir :**
  - les littéraux et les **constantes calculées à la compilation** (`"a" + "b"`) sont dans le pool : `==` est vrai ;
  - `new String(…)` et une chaîne calculée **à l'exécution** sont des objets à part : `==` est faux ;
  - pour comparer le **contenu** : `equals` (ou `equalsIgnoreCase`).
- **Mon image :** …

### 9. 📦 Le micro-ondes — `StringBuilder`

- **Image :** le micro-ondes **transforme le plat sur place** (mutable). Chaque bouton (`append`, `insert`, `delete`, `reverse`, `replace`) renvoie **le même plat**, donc tu peux enchaîner les boutons. `delete(1, 3)` enlève les morceaux 1 et 2, **pas** le 3. Tu poses deux micro-ondes identiques côte à côte et tu demandes `equals` : il répond **faux**, car il ne regarde que si c'est **le même appareil**.
- **À retenir :**
  - `StringBuilder` est **mutable** : ses méthodes modifient l'objet et renvoient **`this`** (chaînage) ;
  - `delete(debut, fin)` et `replace(debut, fin, s)` : `fin` exclue ; `deleteCharAt(i)`, `insert(i, x)`, `reverse()` ;
  - `StringBuilder` ne redéfinit **pas** `equals` : comparer avec `sb1.toString().equals(sb2.toString())` (ou `compareTo`).
- **Mon image :** …

### 10. 🗄️ Le placard — les tableaux et `Arrays`

- **Image :** le placard a des **cases numérotées à partir de 0**. Une étiquette **`length`** est collée dessus, sans parenthèses (c'est un champ). Une case neuve est remplie de **zéros** (ou de `null`). Tu demandes `Arrays.binarySearch` dans un placard **trié** : si l'objet manque, il répond un **nombre négatif bizarre**, *moins la place où il irait, moins un*. `Arrays.mismatch` répond **−1** quand les deux placards sont identiques.
- **À retenir :**
  - `int[] a = new int[3];` est rempli de valeurs par défaut ; `a.length` sans parenthèses ; une case hors limites → `ArrayIndexOutOfBoundsException` ;
  - piège : `int[] a, b;` déclare **deux tableaux**, `int a[], b;` un tableau et **un `int`** ;
  - `Arrays.sort` ; `Arrays.binarySearch` (tableau **trié**) rend `-(point d'insertion) - 1` si absent ;
  - `Arrays.equals` compare le contenu (le `equals` d'un tableau compare les références) ; `Arrays.compare` ; `Arrays.mismatch` rend −1 si égaux.
- **Mon image :** …

### 11. 🍴 Le tiroir à couverts — `Math` et les enveloppes

- **Image :** dans le tiroir, une **cuillère `round`** arrondit les nombres et te rend un **`long`** quand tu lui donnes un `double` (un `int` pour un `float`). Les **fourchettes `ceil` et `floor`** piquent vers le haut ou le bas, mais rendent toujours un **`double`**. Une **louche `pow`** rend aussi un `double`. `Math.random()` tire un nombre entre **0 inclus et 1 exclu**. Au fond, deux tire-bouchons : `parseInt` sort un **`int` nu**, `valueOf` sort un **`Integer` emballé**.
- **À retenir :**
  - `Math.round(double)` → `long`, `Math.round(float)` → `int` ; `ceil`, `floor`, `pow`, `sqrt` → `double` ;
  - `Math.random()` dans `[0, 1[` ; `min`, `max`, `abs` gardent le type ;
  - `Integer.parseInt("12")` → `int` ; `Integer.valueOf("12")` → `Integer` ; une chaîne invalide → `NumberFormatException` ;
  - l'autoboxing convertit tout seul `int` ↔ `Integer` ; déballer `null` → `NullPointerException`.
- **Mon image :** …

### 12. 🍽️ La table — les dates et les heures

- **Image :** sur la table, un **calendrier en marbre** (immuable). Tu appelles `plusDays(1)` : un **nouveau** calendrier apparaît, l'ancien n'a pas bougé ; si tu ne le ramasses pas, il disparaît. Tu demandes le **32 janvier** : le calendrier **se fend** (`DateTimeException`). Un **sablier `Period`** compte en **années, mois, jours** pour les dates ; un **chronomètre `Duration`** compte en **heures, minutes, secondes**. Si tu enchaînes `Period.ofYears(1).ofDays(2)`, seul le **dernier** compte.
- **À retenir :**
  - `LocalDate`, `LocalTime`, `LocalDateTime`, `ZonedDateTime` sont **immuables** : `d.plusDays(1);` seul ne fait rien ;
  - une date impossible (`LocalDate.of(2024, 1, 32)`) → `DateTimeException` ; les mois commencent à **1** ;
  - `Period` (années, mois, jours) pour les dates ; `Duration` (heures, minutes, secondes) pour les heures ; ajouter un `Period` à un `LocalTime` → `UnsupportedTemporalTypeException` ;
  - les méthodes `of…` de `Period` sont **statiques** : enchaînées, seule la **dernière** compte ;
  - au passage à l'heure d'été, l'heure **saute** (2 h → 3 h).
- **Mon image :** …

---

## ⚡ La balade éclair

1. Station 7 : que vaut `"a" + 1 + 2` ? Et `1 + 2 + "a"` ?
2. Station 8 : pourquoi `new String("x") == "x"` est-il faux ?
3. Station 9 : que rend `sb.append("x")` ?
4. Station 10 : que déclare `int a[], b;` ?
5. Station 11 : quel type rend `Math.round(2.5)` ?
6. Station 12 : que donne `Period.ofYears(1).ofDays(2)` ?
