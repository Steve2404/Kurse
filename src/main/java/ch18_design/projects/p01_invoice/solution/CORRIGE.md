# Projet 1 — Corrigé étape par étape

> **Quand le lire :** ouvre **seulement la section de l'étape** que tu viens de terminer. Le code de référence est dans ce dossier (un fichier par classe), les tests de référence dans [`GarageTest.java`](GarageTest.java) et [`PartsTest.java`](PartsTest.java).
>
> Les valeurs ci-dessous ont été obtenues en direct avec **JDK 17** et **JUnit 5.11.4** : en lançant `Data`, les tests de référence, `Check`, et de petites classes de mesure.

---

## Étape 1 — Lire le legacy et nommer ses odeurs

**Question — les odeurs :**

| Odeur | Dans `invoice` |
|---|---|
| méthode trop longue | une seule méthode de 56 lignes (54 lignes non vides dans son corps) |
| noms obscurs | `c`, `f`, `l`, `t`, `t2`, `m`, `r`, `r2`, `n`, `v`, `x`, `p`, `q`, `u`, `a`, `qt`, `mn` |
| code dupliqué | le format des montants, recopié 9 fois ; le refus `throw new IllegalArgumentException("ligne invalide : " + x)`, 5 fois |
| nombres magiques | `15`, `14`, `1800L`, `20000`, `10`, `240`, `5`, `20`, `50`, `100` |
| code de type | `p[0].equals("PART")`, `p[0].equals("LABOR")` |
| paramètre booléen | `boolean f` : on lit `invoice("Dupont", true, …)` sans savoir ce que veut dire `true` |
| responsabilités mêlées | la même boucle découpe le texte (`split`), valide, calcule **et** construit la sortie |

**Question — les copies :** le bout `< 10 ? "0" : ""` apparaît **10 fois** (compté avec Ctrl+F) : **9** pour les montants (une ligne de pièce, une ligne de main-d'œuvre, puis les 7 montants du bas), **1** pour les minutes du libellé `(1 h 05)`. Il complète par un zéro à gauche : sans lui, 5 centimes s'écriraient `0,5`. Pour une espace des milliers, il faudrait changer les **9** formats de montant, sans en oublier un.

**Question — les règles de prix :**
- **main-d'œuvre** : facturée au **quart d'heure commencé** (`(mn + 14) / 15`), 18,00 le quart d'heure ; les minutes **facturées** (arrondies) s'additionnent dans `m` ;
- **remise fidélité** : client fidèle **et** pièces `>= 200,00` (seuil **inclus**) : 10 % des **pièces seules**, arrondi au centime le plus proche ;
- **remise main-d'œuvre** : plus de 240 minutes facturées, `> 240` (seuil **exclu**) : 5 % de la main-d'œuvre, même arrondi ;
- **net** = pièces + main-d'œuvre − les deux remises ; **TVA** = 20 % du net, même arrondi ; **total** = net + TVA ;
- les lignes de remise ne s'affichent que si la remise s'applique.

**Question — les lignes bizarres :** `"PART;X;deux;100"` fait lancer `java.lang.NumberFormatException: For input string: "deux"` par `Integer.parseInt` (vérifié), une exception **brute**, pas le joli message `ligne invalide`. `"TAXI;X;1"` tombe dans le dernier `else` : `IllegalArgumentException("ligne invalide : TAXI;X;1")`.

---

## Étape 2 — Le filet de sécurité : la caractérisation

Le test de référence est [`GarageTest.java`](GarageTest.java) : `sameAsLegacy`, `sampleInvoice`, `goldenMaster` (300 répétitions), `thresholdsAndRounding`, `sameRefusalsAsLegacy` (9 cas), `unreadableNumberStillEscapesAsNumberFormatException`.

**Expérience — `1800L` devient `1700L`** (vérifié avec les tests de référence) : **217** tests de `GarageTest` échouent : les **215** répétitions du maître étalon qui contiennent au moins une ligne de main-d'œuvre, plus `sampleInvoice` et `thresholdsAndRounding`. Le message donne les lignes tirées et le client, par exemple `lignes : [LABOR;Travail 0;48, LABOR;Travail 1;55, LABOR;Travail 2;35, PART;Piece 3;4;6748], fidele : false ==> expected: <FACTURE - Client : Client 1…`. Grâce au message, l'échec se rejoue à la main.

**Expérience — ce que touche le hasard** (vérifié, même tirage que le test) : sur 300 factures, **74** ont la remise fidélité, **86** la remise main-d'œuvre, **5** une main-d'œuvre à **exactement** 240 minutes, **44** sont vides… et **0** ont des pièces à exactement 200,00.

**Question — la graine :** une graine fixe rend chaque facture **reproductible** : un échec d'aujourd'hui se rejoue demain à l'identique, dans le débogueur. Une graine **par répétition** rend chaque répétition **indépendante** : le message dit « répétition 27 », et la graine 27 refabrique exactement cette facture-là, sans rejouer les 26 d'avant.

**Question — les seuils à la main :** le hasard tombe **rarement**, voire **jamais**, pile sur un seuil : aucune des 300 factures n'a 200,00 de pièces exactement. Un `>=` changé en `>` passerait donc inaperçu (l'étape 8 le vérifie : c'est le mutant 5). Les limites se testent **toujours** à la main : exactement sur le seuil, et juste à côté.

**Question — la bizarrerie figée :** non, une `NumberFormatException` brute est un mauvais comportement (un message clair serait mieux). Mais **refactorer, c'est ne rien changer** : si l'on corrige en même temps, on ne sait plus si une différence vient d'une erreur de refactoring ou d'une correction voulue. On fige, on refactore, **puis**, dans une étape à part, avec son propre test, on corrige. Et peut-être qu'un programme appelant attrape justement cette exception-là.

---

## Étape 3 — Un objet valeur : `Money`

Le code : [`Money.java`](Money.java). Les tests : `moneyFormat`, `moneyPercentRoundsHalfUp`, `moneyArithmeticAndValidation` dans [`PartsTest.java`](PartsTest.java).

**Question — `minus` sous zéro :** un `Money` négatif est une valeur **impossible** dans ce métier. Le refuser **tout de suite** fait échouer le bug à l'endroit où il naît, au lieu d'imprimer une facture fausse trois classes plus loin. Le legacy n'a jamais besoin d'un montant négatif : il calcule une remise **positive** et écrit le signe à la main, `"Remise fidelite : -" + …`. Ton imprimeur fait pareil.

---

## Étape 4 — Remplacer le code de type par du polymorphisme

Le code : [`Category.java`](Category.java), [`InvoiceLine.java`](InvoiceLine.java), [`Part.java`](Part.java), [`Labor.java`](Labor.java), [`Fee.java`](Fee.java), [`LineParser.java`](LineParser.java). Les tests : `laborIsBilledByStartedQuarter`, `partAndFee`, `parser`.

**Question — le `switch` acceptable :** il faut bien, **une fois**, passer du texte au bon type : c'est le rôle d'une **fabrique**. Ce `switch` est **le seul** endroit qui connaît les codes `"PART"`, `"LABOR"`, `"FEE"`. Ajouter une sorte de ligne y ajoute un `case`, et rien d'autre dans le programme ne demande plus « quelle sorte de ligne es-tu ? ».

**Question — `default billedMinutes()` :** avec `instanceof`, le calcul devrait **connaître** chaque sorte de ligne, et un futur type « déplacement facturé à la minute » oublierait d'y être ajouté. Avec la méthode par défaut, chaque ligne **répond** elle-même : 0 pour une pièce ou un forfait, les minutes facturées pour la main-d'œuvre. Le calcul fait `mapToInt(InvoiceLine::billedMinutes).sum()` et ne change plus jamais.

**Question — le test en double :** à cause du **message**. Le legacy dit `ligne invalide : PART;X;0;100` ; `Part` dit `quantite invalide : 0`. Pour garder le comportement du legacy, `LineParser` refuse **avant** de construire la `Part`. La vérification de `Part` reste utile : elle protège les `Part` construites **sans** lecteur, dans le code ou dans les tests. C'est exactement le mutant 11.

---

## Étape 5 — Une responsabilité par classe

Le code : [`Customer.java`](Customer.java), [`InvoiceTotals.java`](InvoiceTotals.java), [`InvoiceCalculator.java`](InvoiceCalculator.java), [`InvoicePrinter.java`](InvoicePrinter.java).

**Le test du calcul, sur papier** (pièces 300,00, 300 minutes, fidèle, sans forfait) : pièces **30 000** ; main-d'œuvre : 300 minutes = 20 quarts d'heure = **36 000** ; forfaits **0** ; remise fidélité : 10 % de 30 000 = **3 000** ; remise main-d'œuvre : 300 > 240, donc 5 % de 36 000 = **1 800** ; net = 30 000 + 36 000 − 3 000 − 1 800 = **61 200** ; TVA = **12 240** ; total = **73 440**. Le test de référence `calculatorWithBothDiscounts` ajoute un forfait de 10,00 (net 62 200, TVA 12 440, total 74 640).

**Question — une raison de changer chacune :**
- **seulement `InvoicePrinter`** : écrire `Pièces` avec un accent, aligner les montants à droite, ajouter l'adresse du garage en en-tête ;
- **seulement `InvoiceCalculator`** : une remise fidélité à 15 %, une TVA à 21 %, un nouveau seuil ;
- **seulement `LineParser`** : des lignes séparées par des virgules au lieu de points-virgules, ou lues depuis du JSON.

---

## Étape 6 — La façade, et le legacy disparaît

Le code : [`Garage.java`](Garage.java), quatre lignes.

**Question — les refactorings faits :**
- **renommer** (`t` → `parts`, `f` → `loyal`…) : **Maj+F6** ;
- **extraire une constante** (`1800` → `PRICE_PER_QUARTER`, `20000` → `LOYALTY_THRESHOLD`) : **Ctrl+Alt+C** ;
- **extraire une méthode** (`loyaltyDiscount`, `laborDiscount`, `sum`, `requireLength`, `optional`) : **Ctrl+Alt+M** ;
- **introduire un objet valeur** (`Money`), **extraire une classe** (`InvoiceCalculator`, `InvoicePrinter`, `LineParser`) : **F6** déplace les méthodes, la classe elle-même s'écrit à la main ;
- **remplacer un code de type par du polymorphisme** (`InvoiceLine` et ses records), **introduire un objet paramètre** (`Customer` remplace `String` et `boolean`) : à la main, à petits pas, les tests verts entre chaque pas.

**Question — le nombre de lignes :** le legacy : **une** méthode de 56 lignes. Le code de référence : **12** fichiers, 329 lignes avec les commentaires. Oui, il y a **plus** de code. Mais chaque morceau se lit en quelques secondes, se teste seul, et a **un seul** endroit où changer. Le prochain développeur lira `InvoiceCalculator` en une minute ; personne ne lisait `invoice` en moins d'une demi-heure. On ne mesure pas un code à sa longueur, mais au **coût de sa prochaine modification** : l'étape 7 le montre.

---

## Étape 7 — La récompense : les forfaits

Les tests : `feeLine` et `invalidFees` dans [`GarageTest.java`](GarageTest.java). Le texte attendu (vérifié) :

```
FACTURE - Client : Ba
  Pneu x4 : 200,00
  Recyclage (forfait) : 3,50
Pieces : 200,00
Main-d'oeuvre : 0,00
Forfaits : 3,50
Remise fidelite : -20,00
Sous-total HT : 183,50
TVA 20 % : 36,70
Total TTC : 220,20
```

**Question — les fichiers touchés :** **quatre** : `Fee` (complété), `LineParser` (un `case` et une méthode `fee`), `InvoiceCalculator` (la somme des forfaits, ajoutée au net) et `InvoicePrinter` (la ligne `Forfaits`). Sans les ouvrir : `Money`, `Part`, `Labor`, `Customer`, `InvoiceTotals` et `Category` (déjà prêts), `InvoiceLine` (qui permettait déjà `Fee`), et `Garage`.

**Question — le maître étalon et les forfaits :** le maître étalon compare au legacy, et le legacy **refuse** les lignes `FEE`. Il ne peut donc tester que ce qui existait avant. La nouveauté est couverte par des tests **écrits à la main** à partir de la demande du patron (`feeLine`, `invalidFees`). C'est le cycle normal : caractériser, refactorer, **puis** ajouter avec des tests ordinaires.

---

## Étape 8 — Les mutants

**Expérience — seulement le maître étalon** (vérifié) : 8 mutants sur 12 sont tués, **4 survivent** :
- **5** (le seuil fidélité exclu) : aucune facture tirée n'a 200,00 de pièces exactement ;
- **9** (les forfaits dans la remise fidélité) et **12** (le nombre de champs d'un forfait) : le legacy ne connaît pas les forfaits ;
- **11** (la quantité 0 refusée par `Part` au lieu du lecteur) : le hasard ne tire que des quantités de 1 à 5.

Le maître étalon est un excellent filet **pour le comportement moyen** ; les **limites**, les **refus** et les **nouveautés** demandent des tests écrits exprès. Le mutant 7 (240 minutes pile), lui, est tué par la répétition 27 : le hasard tombe sur 240 minutes cinq fois sur 300. C'était de la chance.
