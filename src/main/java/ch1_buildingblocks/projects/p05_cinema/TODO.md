# Projet 5 (CAPSTONE) — La billetterie d'un cinéma

> Première fois ? Lis d'abord le mode d'emploi [`ch1_buildingblocks/PARCOURS.md`](../../PARCOURS.md) : comment lire cette fiche, lancer `Check`, quoi faire en cas de blocage.
>
> **Bloqué sur une étape ?** [`INDICES.md`](INDICES.md) donne deux indices repliés par étape, sans code. **Étape finie ?** [`solution/CORRIGE.md`](solution/CORRIGE.md) donne, étape par étape, le code de l'étape, les **réponses aux questions** et le résultat exact des **expériences**. N'ouvre que la section de l'étape que tu viens de faire.

**Notions visées :** tout le chapitre 1 à la fois.
- `main` et ses arguments ;
- **deux paquets** et des imports **explicites** ;
- des classes publiques avec constructeurs et `this` ;
- l'**ordre d'initialisation** (journalisé) et les valeurs par défaut ;
- `var`, `final`, un champ `static` ;
- les classes enveloppes : `parseInt`, `valueOf`/`intValue`, **`decode`**, `parseBoolean`, `toBinaryString`, `toOctalString` ;
- **deux text blocks** ;
- les montants en centimes.

**Ce qui est donné :** `Check.java`.

**Ce que TU crées :** tout le programme, dans les sous-paquets `app` et `model` de `ch1_buildingblocks.projects.p05_cinema`. La classe du `main` est **`app.Cinema`**.

**Règle du crescendo :** seulement le chapitre 1, plus `+ - * / %`. Pas de `if`, pas de boucle, pas de méthodes de `String`, pas de `+=` ni de `++`.

**Indication :** ici, peu d'indices. Tout a été vu dans les projets 1 à 4. Avant de coder, recalcule chaque ligne de la sortie attendue à la main.

---

## Le problème

La caisse d'un cinéma est lancée en ligne de commande :

```
java ch1_buildingblocks.projects.p05_cinema.app.Cinema "Dune 2" VO 2 1150 3 850 0x0F true
```

Les 8 arguments, dans l'ordre :
1. le film (attention : il contient une espace) ;
2. la version ;
3. le nombre de places plein tarif ;
4. le tarif plein en centimes ;
5. le nombre de places à tarif réduit ;
6. le tarif réduit en centimes ;
7. un **code promo écrit en hexadécimal**, qui est le pourcentage de remise ;
8. la carte de fidélité.

Le programme construit la séance (son initialisation est journalisée), puis imprime le billet.

---

## Tableau de bord

### ☐ Étape 1 — Le modèle (paquet `model`)

- **La séance** connaît son numéro de salle (7), sa capacité (120), le film et la version.
- **Son initialisation est journalisée** dans cet ordre exact :
  ```
  [1] Seance : champ salle = 7
  [2] Seance : bloc d'initialisation (capacite = 0)
  [3] Seance : champ capacite = 120
  [4] Seance : constructeur (film = Dune 2)
  ```
  - Le `0` de la ligne 2 doit venir de **Java**, pas être écrit à la main. Revois le projet 3.
- **Une ligne de tarif** : libellé, nombre de places, prix unitaire. Elle donne son total et sa ligne de billet.
- **L'affichage des montants** (centimes vers `11.50`) vit dans sa propre classe.
- Les classes utilisées depuis `app` sont **publiques**.

### ☐ Étape 2 — Le lanceur (paquet `app`)

- **Les imports :** importe chaque classe de `model` **explicitement** (pas de joker).
- **Les variables :** déclare la séance et les deux lignes de tarif avec `var`. Le pourcentage de remise est `final`.
- **Les conversions :**
  - les nombres du tarif plein avec `parseInt` ;
  - ceux du tarif réduit avec `valueOf(...).intValue()` ;
  - le **code promo `0x0F`** : `parseInt` lèverait une exception. Quelle méthode d'`Integer` comprend les préfixes `0x`, `#` et `0` ?
  - la fidélité avec `parseBoolean`.

### ☐ Étape 3 — Le billet

```
+==============================+
|        CINEMA LE PHARE       |
+==============================+
Film    : Dune 2 (VO), salle 7
Plein   : 2 x 11.50 = 23.00
Reduit  : 3 x 8.50 = 25.50
Sous-total       : 48.50
Code promo 0x0F : -15% = -7.27
A PAYER          : 41.23
Places restantes : 115
Fidelite : true, code en binaire 1111, en octal 17
     Bonne seance !
```
- **À la main d'abord :**
  - la remise vaut 4850 × 15 / 100 en division entière ;
  - les places restantes valent la capacité moins les places vendues.
- **L'en-tête et le pied** sont deux text blocks. Le pied commence par **5 espaces**, donnés par la position des `"""` fermants.
- **Question :** que vaudrait `Integer.decode("010")` ? Et `Integer.parseInt("010")` ? Pourquoi ?

### ☐ Étape 4 — Revue finale (à écrire en commentaire dans `Cinema`)

1. **Les fichiers :** combien de fichiers `.java`, de classes publiques et de paquets compte ton projet ? Où chaque fichier **doit-il** se trouver, et pourquoi ?
2. **La compilation à la main :** écris la commande `javac -d` qui compile ton projet, puis la commande `java -cp` qui le lance avec les arguments ci-dessus. Reprends le projet 4, puis **essaie-les** dans un terminal.
3. **Le ramasse-miettes :** à la fin du `main`, quels objets sont éligibles au ramasse-miettes ?

---

## Checklist (vérifiée par `Check`)

| Élément | Étape | ☐ |
|---|---|---|
| paquets `…app` et `…model`, import explicite d'une classe de `model` | 1, 2 | ☐ |
| au moins 2 `public class` | 1 | ☐ |
| bloc d'initialisation, `this.`, `static int` | 1 | ☐ |
| `var`, `final int` | 2 | ☐ |
| `Integer.parseInt`, `Integer.valueOf` + `intValue()`, `Integer.decode`, `Boolean.parseBoolean` | 2 | ☐ |
| `Integer.toBinaryString`, `Integer.toOctalString` | 3 | ☐ |
| 2 text blocks | 3 | ☐ |

---

## Sortie attendue complète

```
[1] Seance : champ salle = 7
[2] Seance : bloc d'initialisation (capacite = 0)
[3] Seance : champ capacite = 120
[4] Seance : constructeur (film = Dune 2)
+==============================+
|        CINEMA LE PHARE       |
+==============================+
Film    : Dune 2 (VO), salle 7
Plein   : 2 x 11.50 = 23.00
Reduit  : 3 x 8.50 = 25.50
Sous-total       : 48.50
Code promo 0x0F : -15% = -7.27
A PAYER          : 41.23
Places restantes : 115
Fidelite : true, code en binaire 1111, en octal 17
     Bonne seance !
```
