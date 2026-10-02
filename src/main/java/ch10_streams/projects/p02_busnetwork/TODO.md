# Projet 2 — Le calculateur d'itinéraires d'un réseau de bus

> Première fois ? Lis d'abord le mode d'emploi [`ch10_streams/PARCOURS.md`](../../PARCOURS.md) : comment lire cette fiche, lancer `Check`, quoi faire en cas de blocage.

**API visée :** créer des streams (`iterate`, `generate`, `concat`, `ofNullable`), leur **paresse**, les opérations intermédiaires (`flatMap`, `distinct`, `sorted`, `skip`, `limit`, `peek`, `takeWhile`, `dropWhile`) et les opérations terminales de recherche (`findFirst`, `min`, `anyMatch`, `allMatch`, `noneMatch`).

**Ce qui est donné :** `Data.java` et `Check.java`. Rien d'autre.

**Ce que TU crées :** tous les fichiers du programme, dans le paquet `ch10_streams.projects.p02_busnetwork`. Seul nom imposé : la classe du `main` s'appelle **`BusNetwork`**.

**Pour vérifier :** lance `Check.java`. Il te montre la première ligne fausse et les méthodes de l'API pas encore utilisées.

---

## Le problème

Une agglomération a 4 lignes de bus (`Data.LINES`). Chaque ligne roule dans un seul sens. Elle part du premier arrêt à intervalles réguliers, entre une heure de premier départ et une heure de dernier départ, puis met un nombre fixe de minutes entre deux arrêts consécutifs.

Ton programme répond à des requêtes de voyageurs (`Data.COMMANDS`). Il cherche le prochain bus, les horaires, un trajet direct ou un trajet avec une correspondance.

**Contrainte centrale : les horaires ne sont JAMAIS stockés.** Une ligne de 06:00 à 21:00 toutes les 20 minutes fait 46 départs. Ton programme les **génère à la demande** avec un stream, et il s'arrête dès qu'il a sa réponse. Le correcteur le vérifie : certaines lignes de sortie affichent combien d'horaires ont réellement été calculés.

Utilise `java.time.LocalTime` pour les heures. `LocalTime.parse("07:52")`, `plusMinutes`, `isBefore`, `isAfter` et `toString()` (qui donne `07:52`) suffisent.

---

## Tableau de bord

### ☐ Étape 1 — Modéliser une ligne

Ligne de donnée : `L1;06:00;21:00;20;Gare:0,Centre:4,Musee:6,Port:5`

- Les minutes de la donnée sont **entre deux arrêts**. Toi, tu as besoin du **décalage cumulé** depuis le premier arrêt : Gare 0, Centre 4, Musee 10, Port 15.
- **Question :** une somme cumulée, c'est un état qui avance d'élément en élément. Un stream est-il le bon outil ici ? Tranche, et justifie-le dans un commentaire.
- Décide tes types : la ligne, l'arrêt, et plus tard un « trajet » (quelle ligne, où et quand on monte, où et quand on descend).
- Ta ligne doit savoir :
  - **produire ses départs** sous forme de `Stream<LocalTime>`, sans liste. Lequel des deux `Stream.iterate` permet d'écrire la condition d'arrêt **dans la source** ? Avec l'autre, que faudrait-il ajouter, et pourquoi est-ce un piège ici ?
  - **trouver un arrêt** par son nom, avec un `Optional` ;
  - **dire si elle va de A à B.** Les deux arrêts doivent être desservis **et** A doit être avant B ;
  - **donner sa durée totale.**

### ☐ Étape 2 — `ARRETS`, `PAGE <n> <taille>`

```
ARRETS : Centre, Gare, Hopital, Musee, Phare, Plage, Port, Stade, Universite
PAGE 2 : Musee, Phare, Plage
PAGE 4 : vide
```
- Pour `ARRETS`, une seule chaîne de stream va des lignes à une liste d'arrêts sans doublon et triée. Quelle opération transforme « une ligne » en « plusieurs arrêts » ?
- `PAGE` réutilise **la même** chaîne. Est-ce que `skip(...).limit(...)` équivaut à `limit(...).skip(...)` ? Calcule à la main sur la page 2.
- Une page au-delà de la fin affiche `vide`, sans `if` sur la taille de la liste des arrêts.

### ☐ Étape 3 — `LIGNES`

```
LIGNES : L1 (4 arrets, 15 min), L2 (4 arrets, 20 min), L4 (3 arrets, 16 min), L3 (3 arrets, 21 min)
```
L'ordre de tri :
1. le plus d'arrêts d'abord ;
2. à égalité, la plus courte ;
3. puis l'id.

**Piège OCP :** `comparing(a).thenComparing(b).reversed()` n'inverse pas que `a`. Que fait-il exactement ? Construis le comparateur pour que **seul** le premier critère soit inversé.

### ☐ Étape 4 — `CIRCUIT <ligne> <ligne>`

```
CIRCUIT L1 + L3 : Gare -> Centre -> Musee -> Port -> Plage -> Phare
```
- Ce sont les arrêts de la première ligne puis ceux de la deuxième, sans répéter `Port`. L'ordre est conservé.
- Utilise `Stream.concat`.
- **Contrainte :** si un id de ligne est inconnu, cette ligne est simplement ignorée, sans exception ni `if`. Quelle fabrique de `Stream` (Java 9) transforme une valeur peut-être `null` en un stream de 0 ou 1 élément ?

### ☐ Étape 5 — `PROCHAIN <ligne> <arrêt> <heure>` : la paresse prouvée

```
PROCHAIN L1 a Musee : 08:10 (7 horaires calcules)
PROCHAIN L3 a Phare : 18:36 (16 horaires calcules)
PROCHAIN L3 a Phare : plus de bus (17 horaires calcules)
REFUS : L2 ne dessert pas Plage
REFUS : ligne inconnue L9
```
- C'est le premier passage au **premier arrêt** dont le passage à l'arrêt demandé est ≥ l'heure demandée.
- **Le nombre entre parenthèses** est le nombre de départs que ton stream a **réellement générés**. Compte-les avec `peek` placé juste après la source.
- **Calcule à la main** avant de coder : L1 part à 06:00, 06:20… et Musee est à +10. Pourquoi exactement 7 ? Pourquoi 17 et pas 16 quand il n'y a plus de bus ?
- **À tester toi-même :** si tu remplaces la fin de ta chaîne par `.toList()` puis une recherche dans la liste, combien affiches-tu ? Tu verras alors ce que « paresseux » veut dire.
- Pour compter depuis une lambda, il faut une variable *effectivement finale* (chapitre 8). Un `int` local ne peut donc pas être incrémenté dans `peek`. Quelle structure du chapitre 9, référencée par une variable qui ne change pas, peut recevoir chaque départ ?
- **Les refus** (ligne inconnue, arrêt non desservi) se font **sans `if` ni exception**, comme au projet 1 : la chaîne `Optional` rend soit la réponse, soit `REFUS : …`.
  - PROCHAIN et HORAIRES ont besoin d'une ligne **et** d'un arrêt. Écris **une** méthode « avec ligne et arrêt » qui reçoit le traitement en `BiFunction<…>` et qui gère les deux refus une seule fois.

### ☐ Étape 6 — `HORAIRES <ligne> <arrêt> <de> <à>`

```
HORAIRES L4 a Gare : 22:01 22:16 22:31 22:46
```
- Ce sont les passages à l'arrêt dans l'intervalle [de, à].
- **Contrainte :** ni `filter` ni `toList`. Les passages sont **triés** : « avant de » est un **préfixe** et « après à » est un **suffixe**. Quelles deux opérations (Java 9) l'exploitent ?
- **Question :** sur un stream **non trié**, qu'est-ce que `takeWhile` rendrait de faux ?

### ☐ Étape 7 — `DIRECT <départ> <arrivée> <heure>`

```
DIRECT Gare -> Port : L1 depart 07:00 arrivee 07:15
DIRECT Centre -> Stade : L2 depart 08:17 arrivee 08:30
DIRECT Stade -> Centre : aucun
```
- On cherche, parmi toutes les lignes qui vont **de** départ **à** arrivée (dans ce sens !), le premier bus de chaque ligne qui passe au départ à partir de l'heure demandée. On garde celui qui **arrive le plus tôt**.
- **Contrainte :** une seule chaîne. Chaque ligne candidate donne un `Optional<trajet>`. Comment l'aplatir dans le stream ? (Tu l'as vu au projet 1.)
- Prends le meilleur avec `min` et un comparateur.
- **Question :** pourquoi `Stade -> Centre` donne `aucun`, alors que L2 dessert les deux ?

### ☐ Étape 8 — `CORRESPONDANCE <départ> <arrivée> <heure>` : l'algorithme

```
CORRESPONDANCE Universite -> Port : L2 07:10 -> Centre 07:17, L1 07:24 -> Port 07:35
CORRESPONDANCE Hopital -> Port : L4 08:06 -> Gare 08:16, L1 08:20 -> Port 08:35
CORRESPONDANCE Stade -> Plage : aucune
```
On cherche **exactement un changement**, par recherche exhaustive :
1. prends chaque ligne A qui dessert le départ ;
2. pour chaque arrêt S **après** le départ sur A, prends le premier trajet A départ → S ;
3. pour chaque ligne B **différente de A** qui va de S à l'arrivée, prends le premier trajet B S → arrivée qui part au moins `Data.TRANSFER_MINUTES` minutes après l'arrivée en S ;
4. garde la combinaison qui arrive le plus tôt. À égalité, garde celle qui part le plus **tard**, car on attend moins.

**Contraintes :**
- Tout s'écrit en **une seule** chaîne de stream, avec des `flatMap` imbriqués.
- L'échange à un même arrêt ne compte que s'il respecte le temps de changement.

**Calcule à la main** `Hopital -> Port`, puis explique pourquoi L2 depuis Hopital ne mène nulle part.

**Question :** combien de trajets candidats ton programme évalue-t-il pour `Universite -> Port` ?

### ☐ Étape 9 — `ACCESSIBLE <ligne>`

```
ACCESSIBLE L1 : non (Musee)
ACCESSIBLE L2 : oui
```
- La réponse vient de **un seul** appel terminal court-circuitant, sur les arrêts et `Data.NOT_ACCESSIBLE`. Choisis entre `anyMatch`, `allMatch` et `noneMatch` celui qui se lit comme la phrase « aucun arrêt n'est inaccessible ».
- Les coupables ne sont listés que si la réponse est `non`.

### ☐ Étape 10 — `TICKETS <n>`

```
TICKETS : T001, T002, T003
TICKETS : T004, T005
```
- Les numéros viennent d'un `Stream.generate`, une source **infinie** que tu bornes.
- La numérotation **continue** d'un appel à l'autre. Où doit vivre le compteur ? Pourquoi une variable locale ne compilerait pas dans la lambda ?
- **Question :** que se passe-t-il si tu oublies `limit` ?

### ☐ Étape 11 — `RESEAU`

```
RESEAU : toutes les lignes ont au moins 4 arrets : non
RESEAU : une ligne dessert Phare : oui
RESEAU : aucune ligne ne part avant 05:00 : oui
```
- Chacune des trois lignes utilise un `xxxMatch` différent.
- **Question piège :** sur un stream **vide**, que rendent `allMatch`, `anyMatch` et `noneMatch` ?

### ☐ Étape 12 — `main`

- Toute autre commande produit `REFUS : commande inconnue RETARD`.
- Une `switch` choisit le traitement. Chaque commande affiche sa réponse, refus compris.

---

## Checklist API (vérifiée par `Check`)

| Méthode | Étape | ☐ |
|---|---|---|
| `Stream.iterate` (3 arguments) | 1 | ☐ |
| `Stream.generate` | 10 | ☐ |
| `Stream.concat` | 4 | ☐ |
| `Stream.ofNullable` | 4 | ☐ |
| `flatMap` | 2, 7, 8 | ☐ |
| `distinct` | 2, 4 | ☐ |
| `sorted`, `reversed`, `thenComparing` | 2, 3 | ☐ |
| `skip`, `limit` | 2, 10 | ☐ |
| `peek` | 5 | ☐ |
| `dropWhile`, `takeWhile` | 6 | ☐ |
| `findFirst` | 1, 5 | ☐ |
| `min` | 7, 8 | ☐ |
| `anyMatch`, `allMatch`, `noneMatch` | 9, 11 | ☐ |
| `Collectors.joining` | partout | ☐ |
| `Optional::stream` | 7, 8 | ☐ |

---

## Sortie attendue complète

```
ARRETS : Centre, Gare, Hopital, Musee, Phare, Plage, Port, Stade, Universite
PAGE 2 : Musee, Phare, Plage
PAGE 4 : vide
LIGNES : L1 (4 arrets, 15 min), L2 (4 arrets, 20 min), L4 (3 arrets, 16 min), L3 (3 arrets, 21 min)
CIRCUIT L1 + L3 : Gare -> Centre -> Musee -> Port -> Plage -> Phare
PROCHAIN L1 a Musee : 08:10 (7 horaires calcules)
PROCHAIN L3 a Phare : 18:36 (16 horaires calcules)
PROCHAIN L3 a Phare : plus de bus (17 horaires calcules)
REFUS : L2 ne dessert pas Plage
REFUS : ligne inconnue L9
HORAIRES L4 a Gare : 22:01 22:16 22:31 22:46
DIRECT Gare -> Port : L1 depart 07:00 arrivee 07:15
DIRECT Centre -> Stade : L2 depart 08:17 arrivee 08:30
DIRECT Stade -> Centre : aucun
CORRESPONDANCE Universite -> Port : L2 07:10 -> Centre 07:17, L1 07:24 -> Port 07:35
CORRESPONDANCE Hopital -> Port : L4 08:06 -> Gare 08:16, L1 08:20 -> Port 08:35
CORRESPONDANCE Stade -> Plage : aucune
ACCESSIBLE L1 : non (Musee)
ACCESSIBLE L2 : oui
TICKETS : T001, T002, T003
TICKETS : T004, T005
RESEAU : toutes les lignes ont au moins 4 arrets : non
RESEAU : une ligne dessert Phare : oui
RESEAU : aucune ligne ne part avant 05:00 : oui
REFUS : commande inconnue RETARD
```
