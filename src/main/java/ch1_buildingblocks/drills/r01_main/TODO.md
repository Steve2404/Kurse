# Drill de rappel 1 — `main`, arguments et ligne de commande

> Première fois ? Lis d'abord le mode d'emploi [`ch1_buildingblocks/PARCOURS.md`](../../PARCOURS.md).

**Chrono cible :** 20 min la 1re fois, puis 10 min.

**Règles :**
- Tout se fait **de mémoire** : ni carte, ni Javadoc, ni solution pendant le drill.
- Crée la classe **`Recall01`** dans ce paquet, puis le script **`commandes.sh`** dans ce dossier.
- `Check` lance ton `main` avec les arguments `alpha 42 3.5 TRUE "Bonjour le monde"`.
- Chapitre 1 seulement : pas de `if`, pas de boucle.

**Les notions de ce drill ont été apprises dans :** projet 0 (étapes 5 à 7), projet 1 (étapes 1 et 3) et projet 4 (étapes 4 et 5). Si un défi te semble totalement inconnu, ce drill arrive trop tôt : refais d'abord la leçon de ce projet.

<details><summary><b>Comment faire ce drill, concrètement</b> (à lire la 1re fois)</summary>

1. **Note l'heure** de départ (montre ou téléphone).
2. **Crée la classe** : clic droit sur le dossier `r01_main` → **New** → **Java Class** → `Recall01`.
3. **Écris `main`**, puis **une ligne par défi**. La ligne qui suit la flèche `→` est **exactement** ce que ton programme doit afficher. Par exemple, pour un défi `D01` qui attend `D01 : 8 16`, écris un `System.out.println("D01 : " + … + " " + …);`, où les `…` sont les valeurs que **Java** calcule.
4. **Lance `Recall01`** avec la flèche verte, pour voir tes lignes.
   Ce drill reçoit des **arguments** : lance `Recall01` une fois, puis Run → Edit Configurations… → **Recall01** → Program arguments : `alpha 42 3.5 TRUE "Bonjour le monde"` (projet 0, étape 5).
5. **Bloqué plus de 3 minutes sur un défi ?** Écris un commentaire `// D03 : ✗` à sa place, et passe au suivant.
6. **Lance `Check.java`** (flèche verte). Une ligne `[FAIL]` te montre `attendu` et `obtenu`.
7. **Le script `commandes.sh`** (défi D06) : clic droit sur le dossier `r01_main` → **New** → **File** → `commandes.sh`. Pour le lancer toi-même, depuis le dossier `Kurse`, dans le terminal : `& "C:\Program Files\Git\bin\bash.exe" src/main/java/ch1_buildingblocks/drills/r01_main/commandes.sh` (projet 4, étape 5).
8. **Ensuite seulement**, ouvre la **carte mémoire** tout en bas (en aperçu Markdown, clique sur le triangle « Ouvrir la carte »), relis tes ✗, et fais les **expériences**.
9. **Note** la date, ton temps et tes ✗ dans le tableau de [`drills/README.md`](../README.md).

</details>

## Défis

- ☐ **D00.** Écris `main` avec un paramètre **à la fois `final` et varargs**.
- ☐ **D01.** Les deux premiers arguments.
  → `D01 : alpha puis 42`
- ☐ **D02.** Le 2e argument converti en `int` plus 8. Puis, **dans la même ligne**, le 2e argument suivi de `8` sans conversion, pour voir la différence.
  → `D02 : 50 et non 428`
- ☐ **D03.** Le 3e argument converti en `double`, fois 2.
  → `D03 : 7.0`
- ☐ **D04.** Le 4e argument converti en `boolean`, puis le 1er converti de la même façon.
  → `D04 : true false`
- ☐ **D05.** Le 5e argument entre crochets.
  → `D05 : [Bonjour le monde]`
- ☐ **D06. Le script `commandes.sh`**, lancé depuis la racine du dépôt, enchaîne 4 actions :
  1. **compiler** ton `Recall01.java` dans `build/ch1-r01` (`javac -d`), puis le **lancer** depuis ce dossier (`java -cp`) avec `un 1 0.25 TRUE "deux mots"` ;
  2. **créer un jar exécutable** `build/ch1-r01/r01.jar` (point d'entrée : `Recall01`) qui contient tes classes, puis le **lancer** avec `java -jar` et `trois 3 1.5 false "  espaces  "` ;
  3. **lancer le fichier source directement**, sans `javac`, avec `quatre 4 2 true seul`.

## Expériences (hors sortie attendue)

1. Lance sans arguments : quelle exception, à quelle ligne ?
2. Écris `main` sans `public`. Ça compile ? Que dit `java` ?
3. Écris `public static void main(String args)` : que se passe-t-il au lancement ?

## Sortie attendue complète

```
D01 : alpha puis 42
D02 : 50 et non 428
D03 : 7.0
D04 : true false
D05 : [Bonjour le monde]
```

## Sortie attendue du script

```
D01 : un puis 1
D02 : 9 et non 18
D03 : 0.5
D04 : true false
D05 : [deux mots]
D01 : trois puis 3
D02 : 11 et non 38
D03 : 3.0
D04 : false false
D05 : [  espaces  ]
D01 : quatre puis 4
D02 : 12 et non 48
D03 : 4.0
D04 : true false
D05 : [seul]
```

## Carte mémoire (à lire **après** le drill)

<details><summary>Ouvrir la carte</summary>

**Les signatures valides de `main` :**
- `public static void main(String[] args)` ;
- `String args[]` et `String... args` sont équivalents ;
- `final` est permis, le nom du paramètre est libre ;
- l'ordre `static public` est permis.

**Les signatures invalides comme point d'entrée** (ça compile, mais `java` refuse de lancer) :
- pas `public` ;
- pas `static` ;
- un retour autre que `void` ;
- un paramètre qui n'est pas un tableau de `String`.

**Les arguments :**
- ce sont **toujours** des `String` ;
- les guillemets du terminal regroupent plusieurs mots en un seul argument ;
- un argument manquant donne `ArrayIndexOutOfBoundsException`.

| Commande | Rôle |
|---|---|
| `javac -d dossier Fichiers.java` | compile ; range les `.class` dans des dossiers qui suivent les paquets |
| `java -cp dossier nom.complet.Classe args` | lance, avec le nom **complet**, **sans** `.class` |
| `-cp`, `-classpath`, `--class-path` | trois écritures de la même option |
| `jar --create --file x.jar -C dossier .` (ou `jar -cf x.jar -C dossier .`) | empaquette |
| `--main-class` / option `e` | écrit le point d'entrée dans le manifeste |
| `java -jar x.jar args` | lance un jar exécutable |
| `java Fichier.java args` | compile en mémoire et lance un programme d'**un seul fichier** |

</details>
