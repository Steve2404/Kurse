# Drill de rappel 2 — Les méthodes `default`, `static` et `private` des interfaces

> Première fois ? Lis d'abord le mode d'emploi [`ch7_beyondclasses/PARCOURS.md`](../../PARCOURS.md).

**Chrono cible :** 15 min, puis 8 min.

**Règles :**
- Tout se fait de mémoire.
- Fichier `Recall02.java`, paquet `ch7_beyondclasses.drills.r02_defaults`. Sous `Recall02`, déclare :

| Type | Contenu |
|---|---|
| `interface Walker` | `default String move()` rend `marche` ; `default String describe()` rend `"je " + move() + label()` ; `static String info()` rend `bipede` ; `private String label()` rend ` (walker)` |
| `interface Swimmer` | `default String move()` rend `nage` |
| `class Duck implements Walker, Swimmer` | `move()` rend `Walker.super.move() + "+" + Swimmer.super.move()` |
| `class Robot implements Walker` | vide |
| `class Fish implements Swimmer` | `move()` rend `fretille` |
| `interface Counter` | `default int next()` rend `step() * 2` ; `private int step()` rend 3 ; `static int reset()` rend `zero()` ; `private static int zero()` rend 0 |
| `class Ticker implements Counter` | vide |
| `class Base` | `public String move()` rend `classe` |
| `class Frog extends Base implements Swimmer` | vide |
| `interface Lazy extends Walker` | redéclare `String move();` comme **abstraite** |
| `class Sloth implements Lazy` | `move()` rend `dort` |

**Les notions de ce drill ont été apprises dans :** projet 1 (étapes 1 et 2) et projet 6 (étape 1). Si un défi te semble totalement inconnu, ce drill arrive trop tôt : refais d'abord la leçon de ce projet.

<details><summary><b>Comment faire ce drill, concrètement</b> (à lire la 1re fois)</summary>

1. **Note l'heure** de départ (montre ou téléphone).
2. **Crée la classe** : clic droit sur le dossier `r02_defaults` → **New** → **Java Class** → `Recall02`. S'il faut d'autres classes, place-les comme le disent les **Règles** ci-dessus ; si elles ne précisent rien, écris-les dans le même fichier, sous `Recall02`, sans `public`.
3. **Écris `main`**, puis **une ligne par défi**. La ligne qui suit la flèche `→` est **exactement** ce que ton programme doit afficher : `System.out.println("D01 : " + …);`, où les `…` sont des valeurs que **Java** calcule, jamais recopiées.
4. **Lance `Recall02`** avec la flèche verte, pour voir tes lignes.
5. **Bloqué plus de 3 minutes sur un défi ?** Écris un commentaire `// D03 : ✗` à sa place, et passe au suivant.
6. **Lance `Check.java`** (flèche verte). Une ligne `[FAIL]` te montre `attendu` et `obtenu`.
7. **Ensuite seulement**, ouvre la **carte mémoire** tout en bas (en aperçu Markdown, clique sur le triangle « Ouvrir la carte »), relis tes ✗, et fais les **expériences** (écris la ligne, compile, lis le message, efface la ligne).
8. **Note** la date, ton temps et tes ✗ dans le tableau de [`drills/README.md`](../README.md).

</details>

## Défis

- ☐ **D01.** `new Duck().move()`, `new Robot().move()` et `new Fish().move()`.
  → `D01 : marche+nage marche fretille`
- ☐ **D02.** `Walker.info()`, puis `new Robot().describe()`.
  → `D02 : bipede je marche (walker)`
- ☐ **D03.** `new Ticker().next()`, puis `Counter.reset()`.
  → `D03 : 6 0`
- ☐ **D04.** `new Frog().move()`, puis `new Sloth().move()`.
  → `D04 : classe dort`
- ☐ **D05.** `Walker w = new Duck();` et `Swimmer s = new Duck();`. Affiche `w.move()` et `s.move()`.
  → `D05 : marche+nage marche+nage`

## Expériences (hors sortie attendue)

1. Retire `move()` de `Duck` : quelle erreur ?
2. `new Robot().info()` et `Robot.info()` : pourquoi sont-ils refusés ?
3. Appelle `label()` depuis `Robot` : quelle erreur ?
4. Une méthode `default` sans corps, ou une `default` dans une **classe** : quelles erreurs ?
5. Dans `Duck`, écris `Walker.super.label()` : quelle erreur ?
6. Pourquoi `Frog` n'a-t-il pas besoin de redéfinir `move()`, alors que `Duck` le doit ?
7. Écris `interface Runner extends Walker {}` puis `Runner.info()` : quelle erreur ? (Une méthode `static` d'interface n'est héritée ni par les classes, ni par les sous-interfaces.)
8. `static default String x()` ou `private default String y()` : quelles erreurs ?

## Sortie attendue complète

```
D01 : marche+nage marche fretille
D02 : bipede je marche (walker)
D03 : 6 0
D04 : classe dort
D05 : marche+nage marche+nage
```

## Carte mémoire (à lire **après** le drill)

<details><summary>Ouvrir la carte</summary>

| Méthode d'interface | Corps | Héritée par les classes | S'appelle |
|---|---|---|---|
| abstraite | non | à implémenter | sur un objet |
| `default` | oui | oui (redéfinissable) | sur un objet ; `X.super.m()` dans une classe ou interface **qui étend X directement** |
| `static` | oui | **non** | `NomInterface.m()` seulement |
| `private` | oui | non | depuis les `default` et `private` de l'interface |
| `private static` | oui | non | depuis toute méthode de l'interface |

**Les conflits :**
1. **la classe gagne** : une méthode héritée d'une classe l'emporte sur une `default` ;
2. sinon, **la plus spécifique gagne** : une sous-interface l'emporte sur l'interface qu'elle étend ;
3. sinon (deux `default` égales) : la classe **doit** redéfinir, et peut appeler `A.super.m()` ou `B.super.m()`.

**Redéclarer :** une sous-interface peut redéclarer une `default` en abstraite. Les classes doivent alors la fournir.

</details>
