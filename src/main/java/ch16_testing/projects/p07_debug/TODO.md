# Projet 7 — L'inventaire bogué (le débogueur, puis un test par bug)

> Première fois ? Lis d'abord le mode d'emploi [`ch16_testing/PARCOURS.md`](../../PARCOURS.md).
>
> **Bloqué sur une étape ?** [`INDICES.md`](INDICES.md) donne deux indices repliés par étape, sans code. **Étape finie ?** [`solution/CORRIGE.md`](solution/CORRIGE.md) donne, étape par étape, le code de l'étape, les **réponses aux questions** et le résultat exact des **expériences**. N'ouvre que la section de l'étape que tu viens de faire.

**Notions visées (chapitre 16) :**
- **le débogueur d'IntelliJ** :
  - le **point d'arrêt** et le lancement en mode *Debug* ;
  - avancer pas à pas : *Step Over*, *Step Into*, *Step Out*, *Resume*, *Run to Cursor* ;
  - lire les **variables**, évaluer une expression (*Evaluate Expression*), surveiller une valeur (*Watches*) ;
  - le **point d'arrêt conditionnel** et le **point d'arrêt sur exception** ;
- **la méthode** : reproduire, isoler, comprendre, corriger, **verrouiller par un test** ;
- **six bugs classiques** de Java : `==` sur des `String`, une limite `>` au lieu de `>=`, un débordement d'`int`, une division qui tronque, `==` sur des `Integer`, une boucle qui commence à 1 ;
- **un bug en cache un autre** : corriger dans l'ordre, relancer à chaque fois.

**Ce qui est FOURNI :** `Data.java` contient un programme d'inventaire écrit par quelqu'un d'autre (la classe `LegacyInventory`), avec **six bugs**. Tu ne modifies pas ce fichier.

**Ce que TU crées :** dans `ch16_testing.projects.p07_debug` :
- **`Inventory`** : une **copie** de `LegacyInventory` que tu corriges, avec les signatures imposées ci-dessous ;
- **`InventoryTest`** : un test de non-régression **par bug**, nommé `@DisplayName("BUG 1 : …")` à `"BUG 6 : …"`.

**Règle du crescendo :** chapitres 1 à 15, plus JUnit. Pas de `System.out` ni de `Thread.sleep` dans tes tests.

Chaque étape commence par une **📖 leçon**. Ici, les exemples sont dans le vrai programme : c'est lui qu'on débogue.

> **🧰 Tes outils pour ce projet : le débogueur** (raccourcis du clavier par défaut d'IntelliJ sous Windows)
>
> | Geste | Comment |
> |---|---|
> | poser / enlever un point d'arrêt | clic dans la marge, à gauche du numéro de ligne (un rond rouge), ou **Ctrl+F8** |
> | lancer en mode Debug | clic sur la flèche verte à côté de `main` → **Debug 'Data.main()'** ; ensuite **Maj+F9** relance |
> | passer à la ligne suivante | **F8** (*Step Over*) |
> | entrer dans la méthode appelée | **F7** (*Step Into*) |
> | finir la méthode en cours | **Maj+F8** (*Step Out*) |
> | continuer jusqu'au prochain point d'arrêt | **F9** (*Resume*) |
> | aller jusqu'au curseur | **Alt+F9** (*Run to Cursor*) |
> | calculer une expression | **Alt+F8** (*Evaluate Expression*), tape l'expression, **Entrée** |
> | surveiller une expression | panneau *Variables* → clic droit → **Add to Watches** |
> | condition sur un point d'arrêt | clic droit sur le rond rouge → champ **Condition** |
> | arrêter sur une exception | **Ctrl+Maj+F8** → **+** → *Java Exception Breakpoints* → le nom de l'exception |
> | arrêter le programme | **Ctrl+F2** (le carré rouge) |
>
> Quand le programme est arrêté, la ligne **surlignée en bleu** n'est **pas encore** exécutée. La fenêtre **Debug**, en bas, montre la pile d'appels (*Frames*) à gauche et les variables à droite.

---

## Tableau de bord

### ☐ Étape 1 — Reproduire

**📖 La leçon : d'abord voir le bug.** Avant de chercher, on **reproduit** : on lance, on compare avec ce qui est attendu, on note **chaque** différence. Un bug qu'on ne sait pas reproduire ne se corrige pas.

**👉 À toi :** lance `Data` (flèche verte, en mode normal). Compare sa sortie avec la **sortie juste**, ligne par ligne :

```
PEN en stock : 200
prix moyen : 17016
expedier 200 PEN : true
PEN en stock : 0
valeur du stock : 5001313175
INK et CUP au meme niveau : true
stock bas (< 100) : [GLUE, PAD, PEN]
```

Puis crée ta classe `public final class Inventory` : **copie** tout le contenu de `LegacyInventory` (avec sa classe `Item`), et rends **publiques** ses méthodes : `receive`, `quantity`, `ship`, `stockValueCents`, `averagePriceCents`, `sameQuantity`, `lowStock`. Pour l'instant, ne corrige rien.

**❓ Question :** quelles lignes diffèrent ? Une ligne est **juste** dans la sortie bugguée : laquelle ? Peut-on en conclure que le code qui la produit est juste ?

### ☐ Étape 2 — Le premier point d'arrêt (BUG 1)

**📖 La leçon : arrêter le temps.** Un point d'arrêt fige le programme **juste avant** une ligne. Tu vois alors **toutes** les variables, et tu avances ligne par ligne. On ne devine plus : on **regarde**.

**👉 À toi :**
1. Pose un point d'arrêt sur la ligne `if (item.sku == sku) {` de `LegacyInventory.receive`.
2. Lance `Data` en mode **Debug**. Le programme s'arrête à la 2e livraison (la 1re n'entre pas dans la boucle : la liste est vide).
3. Appuie sur **F9** jusqu'à la livraison `PEN;80;150` (regarde `line` dans le panneau *Variables*). Avance avec **F8** jusqu'à ce que `item` soit le premier article, `PEN`.
4. Ouvre **Alt+F8** et évalue : `item.sku`, `sku`, `item.sku == sku`, puis `item.sku.equals(sku)`.
5. Corrige le bug **dans ton `Inventory`**.

**🧪 Le test de non-régression :** dans `InventoryTest`, un test `@DisplayName("BUG 1 : …")` qui reçoit **deux** livraisons du même code, et vérifie que c'est bien **un seul** article : par exemple, le prix moyen ou l'expédition de toute la quantité en une fois.

**❓ Questions :**
- Que valent `item.sku == sku` et `item.sku.equals(sku)` ? Pourquoi sont-ils différents, alors que les deux affichent `PEN` ?
- Avec ce bug, `quantity("PEN")` rendait pourtant 200 : pourquoi ?
- Le bug apparaîtrait-il si les deux livraisons étaient **exactement la même ligne**, `"PEN;10;100"` deux fois ? Évalue `"PEN;10;100".split(";")[0] == "PEN;10;100".split(";")[0]` pour le savoir.

### ☐ Étape 3 — Le point d'arrêt conditionnel (BUG 2)

**📖 La leçon : s'arrêter seulement au bon moment.** Une boucle qui tourne 10 000 fois ne se débogue pas en appuyant 10 000 fois sur F9. Un **point d'arrêt conditionnel** ne s'arrête que si sa condition est vraie.

**👉 À toi :**
1. Lance d'abord **ton** `Inventory` corrigé du BUG 1 : écris un test temporaire (ou relis la sortie de ton test « scénario » plus bas) avec les livraisons de `Data.DELIVERIES`, puis `ship("PEN", 200)`.
2. Dans **ton** `ship`, pose un point d'arrêt sur `if (item.qty > qty) {`, avec la condition `item.sku.equals("PEN")`.
3. Lance ton test en mode Debug (clic droit sur la flèche verte du test → **Debug**). Regarde `item.qty` et `qty`.
4. Corrige.

**🧪 Le test :** `@DisplayName("BUG 2 : …")` : expédier **exactement** tout le stock réussit et laisse 0 ; expédier ensuite 1 échoue ; expédier un code inconnu échoue.

**❓ Question :** avec le BUG 1 encore présent, `ship("PEN", 200)` échouait **aussi**, mais pour une autre raison : laquelle ?

### ☐ Étape 4 — Évaluer une expression (BUG 3)

**👉 À toi :**
1. Pose un point d'arrêt dans `stockValueCents`, sur `total += …`, avec la condition `item.sku.equals("BOX")`.
2. En mode Debug, évalue (**Alt+F8**) : `item.qty * item.priceCents`, puis `(long) item.qty * item.priceCents`.
3. Corrige.

**🧪 Le test :** `@DisplayName("BUG 3 : …")` : un seul article `BOX;50000;99999`, et sa valeur exacte (calcule-la sur papier).

**❓ Question :** `total` est un `long`. Pourquoi le résultat déborde-t-il quand même ? Où faut-il placer la conversion en `long` ?

### ☐ Étape 5 — Surveiller des variables (BUG 4)

**👉 À toi :**
1. Pose un point d'arrêt sur la ligne `return` de **ton** `averagePriceCents`.
2. Lance le scénario de `Data` sur ton `Inventory` en mode Debug. Clic droit sur `sum` → **Add to Watches**, puis sur `n`. Évalue `sum / n` et `(double) sum / n`.
3. Corrige : le prix moyen est arrondi au centime **le plus proche** (projet 1 : on ajoute la moitié du diviseur).

**🧪 Le test :** `@DisplayName("BUG 4 : …")` : deux articles à 100 et 101 centimes ; la moyenne vaut 100,5, donc 101.

**❓ Question :** avant ta correction du BUG 1, `LegacyInventory` donnait 14 606 ; après, ton `Inventory` donne 17 015 ; la bonne réponse est 17 016. Explique chacun des deux écarts.

### ☐ Étape 6 — Le cache des `Integer` (BUG 5)

**📖 La leçon : `==` sur des objets.** Pour les objets, `==` compare les **références** (chapitre 2), pas les valeurs. Les enveloppes (`Integer`, `Long`…) ont un piège en plus : Java garde **en cache** les petits nombres, de −128 à 127. `Integer.valueOf(100)` rend toujours **le même** objet ; `Integer.valueOf(1000)` en fabrique un **nouveau** à chaque fois.

**👉 À toi :**
1. Point d'arrêt dans `LegacyInventory.sameQuantity`. En mode Debug, évalue : `find(a).qty`, `find(b).qty`, `find(a).qty == find(b).qty`, `find(a).qty.equals(find(b).qty)`.
2. Évalue aussi `(Integer) 127 == (Integer) 127` et `(Integer) 128 == (Integer) 128`.
3. Corrige, dans ton `Inventory` : compare les **quantités** sous forme d'`int` (`quantity(a) == quantity(b)`), ce qui règle aussi le cas d'un code inconnu.

**🧪 Le test :** `@DisplayName("BUG 5 : …")` : deux articles à 1000, et un troisième à 40.

**❓ Question :** pourquoi un test avec deux articles à 100 aurait-il laissé passer ce bug ?

### ☐ Étape 7 — Un bug en cache un autre (BUG 6)

**👉 À toi :**
1. Relance le scénario de `Data` sur **ton** `Inventory`, maintenant corrigé des bugs 1 à 5. La dernière ligne, `stock bas`, était **juste** avec le programme d'origine : l'est-elle encore ?
2. Point d'arrêt sur la ligne `for` de ton `lowStock`. Avance avec **F8** et regarde la valeur de `i` au premier tour.
3. Corrige.

**🧪 Le test :** `@DisplayName("BUG 6 : …")` : le **premier** article reçu est sous le seuil.

**❓ Question :** pourquoi la ligne `stock bas` était-elle juste avec `LegacyInventory`, malgré ce bug ?

### ☐ Étape 8 — Fermer les portes : valider les entrées

**📖 La leçon : le point d'arrêt sur exception.** Quand une exception part de loin, au fond d'une bibliothèque, un point d'arrêt sur **exception** arrête le programme **à l'endroit exact où elle est lancée**, avec toutes les variables de ce moment-là.

**👉 À toi :**
- **`receive`** refuse une ligne qui n'a pas **exactement 3** morceaux, dont les nombres ne se lisent pas, avec une quantité `< 1` ou un prix `< 0` : `IllegalArgumentException("livraison invalide : " + line)`, **sans** toucher au stock. Attrape la `NumberFormatException` de `Integer.parseInt` (chapitre 11) pour la transformer.
- **`ship`** refuse une quantité `< 1` : `IllegalArgumentException("quantite invalide : " + qty)`.
- **Tes tests :** un test paramétré sur `"PEN;0;100"`, `"PEN;5;-1"`, `"PEN;cinq;100"`, `"PEN;5"`, `"PEN;5;100;x"` (le message, et le stock toujours vide) ; `ship` avec 0 ; la moyenne d'un inventaire vide vaut 0 ; et un test « scénario » qui rejoue `Data.DELIVERIES` et vérifie **les sept valeurs** de la sortie juste avec `assertAll`.

**🧪 Expérience :** pose un point d'arrêt sur exception pour `NumberFormatException`. Lance en mode Debug un test qui appelle `receive("PEN;cinq;100")`. Où le débogueur s'arrête-t-il ? Remonte la pile (*Frames*) jusqu'à ta méthode `receive`.

### ☐ Étape 9 — Les mutants : les six bugs reviennent

**👉 À toi :** lance `Check`. Les mutants **1 à 6 sont les six bugs** de `Data`, remis un par un dans le code corrigé ; les mutants 7 à 10 attaquent ta validation. Tes tests de non-régression doivent tous les tuer.

---

## Checklist (vérifiée par `Check`)

- **Ton code :** `final class Inventory` et les sept signatures publiques (`void receive(String line)`, `int quantity(String sku)`, `boolean ship(String sku, int qty)`, `long stockValueCents()`, `long averagePriceCents()`, `boolean sameQuantity(String a, String b)`, `List<String> lowStock(int threshold)`), un cast `(long)`, `catch (NumberFormatException`.
- **Tes tests :** au moins **14** tests, `Data.DELIVERIES`, six `@DisplayName("BUG 1` à `@DisplayName("BUG 6`, `@ParameterizedTest`, `assertAll(` ; ni `System.out` ni `Thread.sleep`.
- **Les 10 mutants** sont tués.

---

## Ce que `Check` affiche quand tout est juste

```
=== Verification des tests de ch16_testing.projects.p07_debug ===
[PASS] tes tests sur TON code : 14 tests, 14 reussis
[PASS] tes tests sur le code de REFERENCE : 14 tests, 14 reussis
[PASS] les tests de REFERENCE sur TON code : 14 tests, 14 reussis
   mutant 1 : tue (par …)
   …
   mutant 10 : tue (par averageOfNothingIsZero)
[PASS] mutants : 10/10 tues
--- API de ton code ---
[PASS] API : tous les elements vises sont utilises
--- API de tes tests ---
[PASS] API : tous les elements vises sont utilises

*** PROJET REUSSI : tes tests passent, attrapent tous les mutants, et ton code est juste. ***
```
