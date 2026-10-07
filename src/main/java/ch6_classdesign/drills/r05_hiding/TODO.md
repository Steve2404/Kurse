# Drill de rappel 5 — Masquer (hide) ou redéfinir (override)

> Première fois ? Lis d'abord le mode d'emploi [`ch6_classdesign/PARCOURS.md`](../../PARCOURS.md).

**Chrono cible :** 12 min, puis 6 min.

**Règles :**
- Tout se fait de mémoire.
- Fichier `Recall05.java`, paquet `ch6_classdesign.drills.r05_hiding`. Les classes :

| Classe | Contenu |
|---|---|
| `P` | `String name = "P"` ; `static int count = 1` ; `static String s()` rend `P.s` ; `String i()` rend `P.i` ; `String readName()` rend `name` ; `private String secret()` rend `secret de P` ; `String reveal()` rend `secret()` |
| `C extends P` | `String name = "C"` ; `static int count = 2` ; `static String s()` rend `C.s` ; `i()` redéfini rend `C.i` ; sa propre `private String secret()` rend `secret de C` ; `String ownSecret()` rend `secret()` ; `String both()` rend `name + "/" + super.name + "/" + this.name` |

**Les notions de ce drill ont été apprises dans :** projet 3 (étape 4). Si un défi te semble totalement inconnu, ce drill arrive trop tôt : refais d'abord la leçon de ce projet.

<details><summary><b>Comment faire ce drill, concrètement</b> (à lire la 1re fois)</summary>

1. **Note l'heure** de départ (montre ou téléphone).
2. **Crée la classe** : clic droit sur le dossier `r05_hiding` → **New** → **Java Class** → `Recall05`. S'il faut d'autres classes, place-les comme le disent les **Règles** ci-dessus ; si elles ne précisent rien, écris-les dans le même fichier, sous `Recall05`, sans `public`.
3. **Écris `main`**, puis **une ligne par défi**. La ligne qui suit la flèche `→` est **exactement** ce que ton programme doit afficher : `System.out.println("D01 : " + …);`, où les `…` sont des valeurs que **Java** calcule, jamais recopiées.
4. **Lance `Recall05`** avec la flèche verte, pour voir tes lignes.
5. **Bloqué plus de 3 minutes sur un défi ?** Écris un commentaire `// D03 : ✗` à sa place, et passe au suivant.
6. **Lance `Check.java`** (flèche verte). Une ligne `[FAIL]` te montre `attendu` et `obtenu`.
7. **Ensuite seulement**, ouvre la **carte mémoire** tout en bas (en aperçu Markdown, clique sur le triangle « Ouvrir la carte »), relis tes ✗, et fais les **expériences** (écris la ligne, compile, lis le message, efface la ligne).
8. **Note** la date, ton temps et tes ✗ dans le tableau de [`drills/README.md`](../README.md).

</details>

## Défis

`C c = new C();` et `P asP = c;`.

- ☐ **D01.** `asP.name`, `c.name`, `asP.readName()` et `c.both()`.
  → `D01 : P C P C/P/C`
- ☐ **D02.** `asP.s()`, `c.s()`, `P.s()` et `C.s()`. Ces appels via une référence compilent : IntelliJ et `javac -Xlint:static` les signalent, `javac` seul ne dit rien.
  → `D02 : P.s C.s P.s C.s`
- ☐ **D03.** `asP.i()` et `c.i()`.
  → `D03 : C.i C.i`
- ☐ **D04.** `c.reveal()` et `c.ownSecret()`.
  → `D04 : secret de P secret de C`
- ☐ **D05.** `asP.count`, `c.count`, `P.count` et `C.count`.
  → `D05 : 1 2 1 2`

## Expériences (hors sortie attendue)

1. Rends `s()` non `static` dans `C` seulement : quelle erreur ? Et l'inverse (`i()` `static` dans `C`) ?
2. Mets `@Override` sur `s()` dans `C` : quelle erreur ?
3. Rends `secret()` non privée dans les deux classes : que devient `c.reveal()` ?

## Sortie attendue complète

```
D01 : P C P C/P/C
D02 : P.s C.s P.s C.s
D03 : C.i C.i
D04 : secret de P secret de C
D05 : 1 2 1 2
```

## Carte mémoire (à lire **après** le drill)

<details><summary>Ouvrir la carte</summary>

| Membre | Mécanisme | Choisi d'après | Moment |
|---|---|---|---|
| méthode d'instance | **redéfinition** | le type de l'**objet** | exécution |
| méthode `static` | **masquage** | le type de la **référence** | compilation |
| champ (`static` ou non) | **masquage** | le type de la **référence** | compilation |
| méthode `private` | ni l'un ni l'autre : une méthode **distincte** | la classe où l'appel est écrit | compilation |

**Les règles de cohérence :**
- `static` dans le parent, `static` dans l'enfant : masquage ;
- d'instance dans le parent, d'instance dans l'enfant : redéfinition ;
- un mélange des deux : erreur de compilation.

**Les champs :**
- un champ masqué existe **deux fois** dans l'objet ;
- `super.name` désigne celui du parent.

</details>
