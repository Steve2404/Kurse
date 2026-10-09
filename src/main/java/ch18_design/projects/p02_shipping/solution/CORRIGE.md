# Projet 2 — Corrigé étape par étape

> **Quand le lire :** ouvre **seulement la section de l'étape** que tu viens de terminer. Le code de référence est dans ce dossier, les tests de référence dans [`ShippingTest.java`](ShippingTest.java) et [`RatesTest.java`](RatesTest.java).
>
> Les valeurs ci-dessous ont été obtenues en direct avec **JDK 17** et **JUnit 5.11.4**.

---

## Étape 1 — Lire le legacy

La sortie de `Data` (vérifiée) :

```
POST 1,2 kg FR : 750
EXPRESS 1,2 kg DE fragile : 3784
PICKUP 3 kg BE : 390
POST 800 g CH : 2300
le moins cher, 2 kg FR : PICKUP:390
le moins cher, 25 kg US : POST:7900
le moins cher, 31 kg FR : AUCUN
```

**Question — les règles :**

| Transporteur | Pays | Poids max | Prix en France | Ailleurs |
|---|---|---|---|---|
| POST | tous | 30 000 g inclus | 4,95 jusqu'à 500 g inclus ; 7,50 jusqu'à 2 000 g ; 11,50 jusqu'à 5 000 g ; puis 11,50 + 1,20 par kilo commencé au-delà de 5 kg | le double |
| EXPRESS | tous | 30 000 g inclus | 12,90 + 2,50 par kilo commencé | + 15,00 |
| PICKUP | FR et BE seulement | 20 000 g inclus | 3,90 | 3,90 (Belgique) |

Surcharges, calculées sur le prix de base `p` et additionnées : **fragile** + 15 % arrondi au centime le plus proche ; **douane** + 8,00 hors de l'Union européenne (FR, BE, DE, ES, IT, LU, NL). Exemple vérifié : EXPRESS 1,2 kg en Allemagne, fragile = 12,90 + 2 × 2,50 + 15,00 = 32,90, plus 4,94 de fragilité = **37,84**.

**Question — ajouter `FREIGHT` :** il faut rouvrir **deux** endroits : un nouveau `case` dans le `switch` de `price`, et le tableau `{"POST", "EXPRESS", "PICKUP"}` de `cheapest`. Oublier le second est **silencieux** : le fret marcherait dans `price` mais ne serait jamais proposé. Et il faut retester les trois transporteurs qui marchaient, puisque leur méthode a été modifiée.

**Question — le `try`/`catch` :** il sert à **sauter** un transporteur qui refuse le colis. Mauvaise idée : une exception annonce une situation **anormale**, pas une réponse normale (« je ne livre pas en Allemagne » est une réponse normale) ; on ne voit plus la différence entre un refus et un **vrai** bug (une `IllegalArgumentException` venue d'ailleurs serait avalée en silence) ; et lancer une exception coûte cher (la pile d'appels est copiée). La bonne question se **pose** avant d'agir : `accepts(parcel)`.

---

## Étape 2 — Le filet de sécurité, et le piège des graines

Le code : `outcome`, `goldenMasterPrice`, `goldenMasterCheapest` dans [`ShippingTest.java`](ShippingTest.java).

**Expérience — `new Random(n).nextInt(4)` pour n de 1 à 400** (vérifié) : `[0, 0, 321, 79]`. Le transporteur 0 (`POST`) et le transporteur 1 (`EXPRESS`) ne sont **jamais** tirés ! Avec `new SplittableRandom(n)` : `[105, 83, 106, 106]`, une répartition normale.

**Question — le danger :** avec `Random`, les 400 répétitions du maître étalon n'auraient testé que `PICKUP` et le transporteur inconnu. Tes tests passeraient **tous**, et tu croirais avoir vérifié la poste et l'express : un filet troué qu'on croit solide, c'est pire que pas de filet. L'explication : `Random` fabrique son premier nombre par une simple multiplication de la graine ; deux graines **voisines** (1, 2, 3…) donnent des premiers nombres très proches, donc les mêmes bits de poids fort, et `nextInt(4)` lit justement ces bits-là quand la borne est une puissance de 2. `SplittableRandom` **mélange** la graine d'abord. Vérifié aussi : avec `nextInt(7)` (projet 1), `Random` se répartit normalement (`[57, 59, 56, 58, 56, 56, 58]`). C'est la borne en puissance de 2 qui piège.

La leçon générale : **regarde ce que tes tests au hasard tirent vraiment**. Un comptage de deux minutes suffit.

**Question — les deux maîtres étalons :** ils ne vérifient pas la même chose. Celui des prix tire **tous** les cas, refus et entrées invalides compris, transporteur par transporteur. Celui du moins cher vérifie ce que le premier ne voit pas : quels transporteurs **acceptent**, le **tri** des devis, le cas `AUCUN`.

---

## Étape 3 — Le colis se valide lui-même

Le code : [`Parcel.java`](Parcel.java).

**Question — l'ordre des vérifications :** dans le constructeur de `Parcel`, donc **avant** même d'appeler le calculateur : `shop.price(carrier, new Parcel(grams, country, fragile))` construit le colis d'abord. Le legacy aussi vérifie le poids, puis le pays, puis seulement le transporteur. Les deux programmes donnent donc le même premier message : `"TRUCK"` avec 0 g donne `poids invalide : 0` chez les deux, pas `transporteur inconnu`.

---

## Étape 4 — Une stratégie par transporteur

Le code : [`ShippingRate.java`](ShippingRate.java), [`PostRate.java`](PostRate.java), [`ExpressRate.java`](ExpressRate.java), [`PickupRate.java`](PickupRate.java). Les tests : `post`, `express`, `acceptance` dans [`RatesTest.java`](RatesTest.java).

**Question — `accepts` à part :** pour **demander** sans provoquer d'erreur. `quotes` doit filtrer les transporteurs qui acceptent : avec une exception, il faudrait le `try`/`catch` du legacy. Deux méthodes rendent aussi l'intention lisible (« acceptes-tu ? », puis « combien ? ») et se testent séparément.

---

## Étape 5 — Les surcharges

Le code : [`Surcharge.java`](Surcharge.java), [`Surcharges.java`](Surcharges.java). Le test : `surcharges` dans [`RatesTest.java`](RatesTest.java).

**Question — compter les classes :** avec l'héritage, chaque transporteur existe avec ou sans chacune des 3 surcharges : 2 × 2 × 2 = 8 variantes, donc 4 × 8 = **32 classes**. Avec la composition : 4 transporteurs + 3 surcharges = **7**. Et une cinquième surcharge doublerait les 32 en 64, contre 8 avec la composition.

**Question — le prix de base :** c'est la règle du legacy (`extra` est calculé sur `p`, pas sur `p + extra`). Elle rend aussi l'**ordre** des surcharges sans importance : la fragilité d'un colis pour la Suisse vaut 15 % du transport, pas 15 % du transport **plus la douane**. Le mutant 14 fait l'erreur inverse : il est tué, parce que `Shop` met la douane **avant** la fragilité.

---

## Étape 6 — Le calculateur

Le code : [`Quote.java`](Quote.java), [`ShippingCalculator.java`](ShippingCalculator.java), [`Shop.java`](Shop.java).

**Question — `Optional<Quote>` :** `"POST:750"` est un texte à **redécouper** (`split(":")`, `parseLong`) pour s'en servir, et `"AUCUN"` est une valeur magique qu'on peut oublier de tester. `Optional<Quote>` dit dans son **type** que la réponse peut manquer, et le compilateur oblige à traiter ce cas (`map`, `orElse`, `ifPresent`) ; le prix est déjà un `long`.

**Question — fermé à tout ?** Non. Une surcharge ne reçoit que le colis et le prix de base, **pas le transporteur** : pour « fragile gratuit chez EXPRESS », il faudrait changer l'interface `Surcharge` (lui donner le code du transporteur), donc toutes ses implémentations et l'appel dans `finalPrice`. Un code n'est jamais fermé à **tout** : il est fermé contre les changements **prévus** (ici : de nouveaux transporteurs, de nouvelles surcharges indépendantes). Choisir ces points d'extension demande de l'expérience. En prévoir partout rend le code lourd pour rien : on ouvre quand un **deuxième** besoin réel apparaît (le principe *YAGNI*, *You Aren't Gonna Need It*).

---

## Étape 7 — Deux transporteurs de plus

Le code : [`FreightRate.java`](FreightRate.java). Les tests : `newCarrierWithoutTouchingTheCalculator`, `surchargesAreComputedOnTheBasePrice`, `freight`, `sameStrategyCodeTwiceIsRefused`.

Les valeurs (vérifiées) : fret à 30 001 g : **4 990** ; à 40 000 g : **5 800**, et **6 670** fragile (5 800 + 870) ; à 30 000 g, le moins cher reste la poste à **4 150** ; à 30 001 g en Belgique, personne : `Optional.empty()`. Le vélo : `[Quote[carrier=BIKE, priceCents=345], Quote[carrier=POST, priceCents=569]]` pour 400 g fragile, `[Quote[carrier=POST, priceCents=1150]]` pour 4 000 g. Les deux surcharges en lambda sur le point relais : 390 + 100 + 195 = **685**.

**Question — les fichiers modifiés :** pour `FREIGHT`, **un** fichier créé (`FreightRate`) et **une** ligne ajoutée dans `Shop`. Pour `BIKE`, **aucun** fichier de production : le vélo n'existe que dans le test, et le calculateur l'utilise sans le connaître. C'est exactement « ouvert à l'extension, fermé à la modification ».

**Question — les maîtres étalons encore verts :** celui des prix ne tire jamais le code `"FREIGHT"` (le transporteur inconnu tiré est `"TRUCK"`) ; celui du moins cher tire au plus 30 000 g, où le fret refuse. Le fret n'apparaît que là où le legacy ne disait rien. Il est testé par les tests écrits exprès.

---

## Étape 8 — Les mutants

**Expérience — seulement les deux maîtres étalons** (vérifié) : 10 mutants sur 16 sont tués, **6 survivent** : **1** (500 g pile), **7** (20 000 g pile), **8** (1 000 g pile), **12** (le code en double), **15** et **16** (le fret). Ce qu'ils ont en commun : soit une **limite exacte** que le hasard ne tire presque jamais, soit un comportement que le **legacy ne connaît pas**. Encore la leçon du projet 1 : le maître étalon couvre la masse, les tests écrits exprès couvrent les bords et les nouveautés.
