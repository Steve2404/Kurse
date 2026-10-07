# Projet 1 — Le ticket de caisse en ligne de commande

> Première fois ? Lis d'abord le mode d'emploi [`ch1_buildingblocks/PARCOURS.md`](../../PARCOURS.md) : comment lire cette fiche, lancer `Check`, quoi faire en cas de blocage.
>
> **Bloqué sur une étape ?** [`INDICES.md`](INDICES.md) donne deux indices repliés par étape, sans code. **Étape finie ?** [`solution/CORRIGE.md`](solution/CORRIGE.md) donne, étape par étape, le code de l'étape, les **réponses aux questions** et le résultat exact des **expériences**. N'ouvre que la section de l'étape que tu viens de faire.

**Notions visées (chapitre 1) :**
- la méthode `main` et ses **arguments** ;
- deux classes dans **un** fichier ;
- les champs, un constructeur et `this` ;
- les classes enveloppes pour convertir du texte en nombre (`parseInt`, `valueOf`, `intValue`, `parseBoolean`) ;
- les **text blocks** et leurs espaces ;
- `final`.

**Ce qui est donné :** `Check.java` seulement. Les données arrivent par les **arguments de la ligne de commande**.

**Ce que TU crées :** tout le programme, dans le paquet `ch1_buildingblocks.projects.p01_receipt`. La classe du `main` s'appelle **`Receipt`**.

**Règle du crescendo :** tu n'as droit qu'au chapitre 1, plus les opérateurs `+ - * / %`. Donc pas de `if`, pas de boucle, pas de méthode de `String` (`length`, `substring`…), pas de `StringBuilder` ni de `String.format`. `Check` les refuse. Tout se fait avec des variables, des objets, des appels de méthodes et des calculs.

---

## Le problème

Une librairie veut un petit programme de caisse **lancé en ligne de commande**. Le vendeur tape les articles, une remise et la carte de fidélité :

```
java ch1_buildingblocks.projects.p01_receipt.Receipt Dune 3 1250 Fondation 2 990 10 true
```

Les 8 arguments, dans l'ordre :
1. le nom du 1er article ;
2. sa quantité ;
3. son prix unitaire **en centimes** ;
4. le nom du 2e article ;
5. sa quantité ;
6. son prix unitaire en centimes ;
7. la remise en % ;
8. la carte de fidélité (`true` ou `false`).

Le programme imprime le ticket. `Check` lance ton `main` avec exactement ces arguments. Pour l'essayer toi-même dans IntelliJ : Run → Edit Configurations → **Program arguments**.

---

## Tableau de bord

### ☐ Étape 1 — La classe `Receipt` et son `main`

- Crée `Receipt.java` avec la méthode `main`.
- **Contrainte :** écris `main` avec la forme **varargs** du paramètre, et non `String[]`.
  - **Question :** quelles autres écritures de `main` sont valides ? Pense à `final`, à la place des crochets et au nom du paramètre.
- **Expérience :** retire `static` de `main`. Ça compile ? Que dit `java` au lancement ? Remets-le.

### ☐ Étape 2 — L'article : une 2e classe dans le même fichier

- Un **article** a un nom, une quantité et un prix unitaire en centimes. Il sait calculer son total et produire sa ligne de ticket.
- **Contrainte :** écris cette classe **dans `Receipt.java`**, sous la classe `Receipt`.
  - **Question :** peut-elle être `public` ? Essaie, lis l'erreur de `javac`, puis corrige.
- **Le constructeur** reçoit les trois valeurs. Ses paramètres portent **les mêmes noms** que les champs.
  - **Expérience :** écris `name = name;` au lieu de `this.name = name;`. Qu'affiche le ticket ? Pourquoi `javac` ne dit-il rien ?

### ☐ Étape 3 — Lire les arguments

- Les arguments sont **toujours** des `String`. Pour calculer, il faut les convertir avec les classes enveloppes :
  - pour le 1er article, `Integer.parseInt(...)` (rend un `int`) ;
  - pour le 2e article, `Integer.valueOf(...)` (rend un **objet** `Integer`), puis `intValue()` pour obtenir le primitif. Le résultat est le même, mais la nature du retour diffère ;
  - pour la carte de fidélité, `Boolean.parseBoolean(...)`.
- La remise est une variable locale `final` : elle ne doit plus changer une fois lue.
- **Expériences :**
  - lance avec seulement 3 arguments : quelle exception, et pourquoi ?
  - remplace `1250` par `12.50` : quelle exception lève `parseInt` ?
  - écris `Boolean.parseBoolean("TRUE")`, puis `("oui")` : que rend chacun ?

### ☐ Étape 4 — Les montants

```
Dune x 3 a 12.50 = 37.50
Fondation x 2 a 9.90 = 19.80
```
- **Tous les calculs se font en centimes** (`int`).
- Pour afficher `3750` sous la forme `37.50`, **sans `if` ni `String.format`**, écris une méthode qui n'utilise que `/`, `%` et la concaténation.
  - Indice : 3750 donne 37 euros, puis le chiffre des dizaines de centimes, puis celui des unités.
  - Calcule à la main pour `990`, `573` et `5157`.
- **Question :** pourquoi les centimes en `int`, plutôt que `12.50` en `double` ?

### ☐ Étape 5 — Le ticket : deux text blocks

```
+--------------------------------+
|       LIBRAIRIE DU PORT        |
|  12, quai des Brumes - Nantes  |
+--------------------------------+
...
    Merci de votre visite !
      "Lire, c'est voyager."
```
- **L'en-tête** est un text block.
  - **Contrainte :** dans ton code source, la ligne de l'adresse est écrite sur **deux lignes**, mais elle s'affiche sur **une seule**. Quel caractère, en fin de ligne d'un text block, supprime le saut de ligne ?
- **Le pied** est un autre text block. Ses lignes commencent par **4 et 6 espaces**.
  - Ces espaces viennent de la **position des `"""` fermants**, et non d'espaces tapés dans des guillemets. Comment Java calcule-t-il l'indentation « accidentelle » qu'il retire ?
  - Si ta sortie diffère, `Check` affiche les espaces de tête sous forme de points `·`.
- Les guillemets de la citation **ne s'échappent pas** dans un text block. Quand faudrait-il un `\"` ?

### ☐ Étape 6 — Le résumé

```
Articles   : 5
Sous-total : 57.30
Remise 10% : -5.73
TOTAL      : 51.57
Carte fidelite : true
```
- La remise vaut `sous-total × % / 100` en centimes (division entière). Calcule-la à la main.
- **Question :** que vaudrait `"Articles : " + 3 + 2` ? Et `"Articles : " + (3 + 2)` ? Pourquoi ? (Le chapitre 2 détaillera cette règle.)

---

## Checklist (vérifiée par `Check`)

| Élément | Étape | ☐ |
|---|---|---|
| `main(String... args)` | 1 | ☐ |
| 2 classes (`class` ×2) dans ton code | 2 | ☐ |
| `this.` | 2 | ☐ |
| `args[7]` | 3 | ☐ |
| `Integer.parseInt`, `Integer.valueOf` + `intValue()`, `Boolean.parseBoolean` | 3 | ☐ |
| `final int` | 3 | ☐ |
| 2 text blocks (`"""` ×4) | 5 | ☐ |
| `\` en fin de ligne dans un text block | 5 | ☐ |

---

## Sortie attendue complète

```
+--------------------------------+
|       LIBRAIRIE DU PORT        |
|  12, quai des Brumes - Nantes  |
+--------------------------------+
Dune x 3 a 12.50 = 37.50
Fondation x 2 a 9.90 = 19.80
--------------------------------
Articles   : 5
Sous-total : 57.30
Remise 10% : -5.73
TOTAL      : 51.57
Carte fidelite : true
    Merci de votre visite !
      "Lire, c'est voyager."
```
