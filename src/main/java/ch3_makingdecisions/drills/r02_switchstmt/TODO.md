# Drill de rappel 2 — Le `switch` instruction

> Première fois ? Lis d'abord le mode d'emploi [`ch3_makingdecisions/PARCOURS.md`](../../PARCOURS.md).

**Chrono cible :** 15 min, puis 8 min.

**Règles :**
- Tout se fait de mémoire.
- Crée la classe **`Recall02`**, avec une constante `static final int FREEZE = 0`.

**Les notions de ce drill ont été apprises dans :** projet 1 (étapes 1 à 3). Si un défi te semble totalement inconnu, ce drill arrive trop tôt : refais d'abord la leçon de ce projet.

<details><summary><b>Comment faire ce drill, concrètement</b> (à lire la 1re fois)</summary>

1. **Note l'heure** de départ (montre ou téléphone).
2. **Crée la classe** : clic droit sur le dossier `r02_switchstmt` → **New** → **Java Class** → `Recall02`. S'il faut d'autres classes, écris-les dans le même fichier, sous `Recall02` (sans `public`).
3. **Écris `main`**, puis **une ligne par défi**. La ligne qui suit la flèche `→` est **exactement** ce que ton programme doit afficher : `System.out.println("D01 : " + …);`, où les `…` sont des valeurs que **Java** calcule, jamais recopiées.
4. **Lance `Recall02`** avec la flèche verte, pour voir tes lignes.
5. **Bloqué plus de 3 minutes sur un défi ?** Écris un commentaire `// D03 : ✗` à sa place, et passe au suivant.
6. **Lance `Check.java`** (flèche verte). Une ligne `[FAIL]` te montre `attendu` et `obtenu`.
7. **Ensuite seulement**, ouvre la **carte mémoire** tout en bas (en aperçu Markdown, clique sur le triangle « Ouvrir la carte »), relis tes ✗, et fais les **expériences** (écris la ligne, compile, lis le message, efface la ligne).
8. **Note** la date, ton temps et tes ✗ dans le tableau de [`drills/README.md`](../README.md).

</details>

## Défis

- ☐ **D01.** Une méthode « compteur » : `switch (start)` avec `case 1:`, `case 2:` et `case 3:`, chacun faisant `count++`. Seul le `case 3` se termine par `break`. Un `case 4:` ajoute 10. Il n'y a pas de `default`. Affiche le résultat pour 1, 2, 3, 4 et 9.
  → `D01 : 3 2 1 10 0`
- ☐ **D02.** Une méthode « jour » :
  - `default:` **en premier** → `inconnu` ;
  - `case 6:` et `case 7:` **empilés** → `week-end` ;
  - `case 1, 2, 3, 4, 5:` → `semaine`.
  
  Teste avec 6, 3 et 0.
  → `D02 : week-end semaine inconnu`
- ☐ **D03.** Un `switch` en flèche sur le `String` `"stop"` : `go`, `stop` ou défaut.
  → `D03 : arret`
- ☐ **D04.** Un `switch` sur le `char` `'B'` : `'A'` ajoute « excellent », `'B'` « bien », `'C'` « passable » puis `break`. Le `default` donne « echec ». Laisse le fall-through agir.
  → `D04 : bien passable`
- ☐ **D05.** Un `switch` sur `temp = 0` dont le `case` est la **constante** `FREEZE`.
  → `D05 : gel (case sur une constante final)`
- ☐ **D06.** Un `switch` sur un `Integer` (objet) valant 2.
  → `D06 : deux (switch sur un Integer)`
- ☐ **D07.** Un `switch` sur un `var` valant `"medium"` :
  - `easy` ajoute 1 puis `break` ;
  - `medium` ajoute 5 (**sans** `break`) ;
  - `hard` ajoute 10.
  → `D07 : 15`
- ☐ **D08.** Un `switch` sur un `byte` 3, avec `case 1, 3, 5 ->`.
  → `D08 : impair`

## Expériences (hors sortie attendue)

1. Un `switch` sur un `long`, puis sur un `double`, puis sur un `boolean` : quelles erreurs ?
2. Un `case` dont la valeur est une variable **non `final`** : quelle erreur ?
3. Deux `case` avec la même valeur. Et un `case 300` dans un `switch` sur un `byte` ?
4. Mélange `case 1 ->` et `case 2:` dans un même `switch` : que dit `javac` ?

## Sortie attendue complète

```
D01 : 3 2 1 10 0
D02 : week-end semaine inconnu
D03 : arret
D04 : bien passable
D05 : gel (case sur une constante final)
D06 : deux (switch sur un Integer)
D07 : 15
D08 : impair
```

## Carte mémoire (à lire **après** le drill)

<details><summary>Ouvrir la carte</summary>

**Les types autorisés :**
- `int` et plus petits (`byte`, `short`, `char`), et leurs enveloppes ;
- `String` ;
- les `enum` ;
- `var` si son type est l'un des précédents ;
- **pas** `long`, `float`, `double` ni `boolean`.

**Les valeurs des `case` :**
- ce sont des **constantes de compilation** : des littéraux, ou des variables `final` initialisées par une constante ;
- elles doivent être compatibles avec le type du `switch` et toutes différentes ;
- plusieurs valeurs sont permises dans un même `case` : `case 1, 2, 3`.

**Le fall-through :**
- forme `case x:` : sans `break`, l'exécution **continue** dans les `case` suivants ;
- la place du `default` n'importe pas pour la recherche. Mais s'il est en tête **sans** `break`, il tombe dans le `case` suivant.

**La forme flèche :**
- `case x ->` : pas de fall-through, et une seule instruction ou un bloc `{ }` ;
- on ne mélange pas `:` et `->` dans un même `switch`.

</details>
