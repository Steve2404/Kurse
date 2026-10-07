# Drill de rappel 4 — `static`, `final` et l'ordre d'initialisation

> Première fois ? Lis d'abord le mode d'emploi [`ch5_methods/PARCOURS.md`](../../PARCOURS.md).

**Chrono cible :** 15 min, puis 8 min.

**Règles :**
- Tout se fait de mémoire.
- Deux classes dans le paquet `ch5_methods.drills.r04_static` :
  - **`Counter`**, qui laisse des traces ;
  - **`Recall04`**, le `main`.
- **Avant de lancer**, écris sur papier l'ordre exact des 14 lignes.

**Les notions de ce drill ont été apprises dans :** projet 2 (étape 1) et projet 3 (étapes 1 à 4). Si un défi te semble totalement inconnu, ce drill arrive trop tôt : refais d'abord la leçon de ce projet.

<details><summary><b>Comment faire ce drill, concrètement</b> (à lire la 1re fois)</summary>

1. **Note l'heure** de départ (montre ou téléphone).
2. **Crée la classe** : clic droit sur le dossier `r04_static` → **New** → **Java Class** → `Recall04`. S'il faut d'autres classes, place-les comme le disent les **Règles** ci-dessus ; si elles ne précisent rien, écris-les dans le même fichier, sous `Recall04`, sans `public`.
3. **Écris `main`**, puis **une ligne par défi**. La ligne qui suit la flèche `→` est **exactement** ce que ton programme doit afficher : `System.out.println("D01 : " + …);`, où les `…` sont des valeurs que **Java** calcule, jamais recopiées.
4. **Lance `Recall04`** avec la flèche verte, pour voir tes lignes.
5. **Bloqué plus de 3 minutes sur un défi ?** Écris un commentaire `// D03 : ✗` à sa place, et passe au suivant.
6. **Lance `Check.java`** (flèche verte). Une ligne `[FAIL]` te montre `attendu` et `obtenu`.
7. **Ensuite seulement**, ouvre la **carte mémoire** tout en bas (en aperçu Markdown, clique sur le triangle « Ouvrir la carte »), relis tes ✗, et fais les **expériences** (écris la ligne, compile, lis le message, efface la ligne).
8. **Note** la date, ton temps et tes ✗ dans le tableau de [`drills/README.md`](../README.md).

</details>

## Défis

**`Counter`**, dans cet ordre :
1. `static int base = trace("champ static base", 10);` ;
2. `static final int STEP;` ;
3. un bloc `static` qui fait `STEP = base / 5;` puis `trace("bloc static", STEP);` ;
4. `static int created;` ;
5. `final int id;` ;
6. `int value = trace("champ d'instance value", base);` ;
7. un bloc d'instance `{ id = ++created; trace("bloc d'instance id", id); }`.

Les méthodes :
- `static int trace(String what, int v)` affiche `D03 : <what> = <v>` et rend v ;
- `int next()` ajoute `STEP` à `value` et rend `value` ;
- `static int total()` rend `created`.

**`Recall04`** : un bloc `static` affiche `D01 : Recall04 charge`. `main` affiche d'abord `D02 : main`.

- ☐ **D01 à D03.** Crée deux `Counter`, `a` et `b`. Les traces `D03` sortent dans l'ordre de l'initialisation.
- ☐ **D04.** Affiche :
  - `a.id`, `b.id` et `Counter.total()` ;
  - `a.next()` deux fois ;
  - `b.next()`.
  → `D04 : 1 2 2 12 14 12`
- ☐ **D05.** Fais `Counter.base = 100;`, puis crée un troisième `Counter c` (nouvelles traces). Avec `Counter none = null;`, affiche :
  - `c.value` ;
  - `none.total()` ;
  - `b.total()`.
  → `D05 : 100 3 3`
- ☐ **D06.** Deux variables `final` :
  - `final int[] box = {1};` puis `box[0]++` ;
  - `final StringBuilder sb = new StringBuilder("a");` puis `sb.append("b")`.
  → `D06 : 2 ab`
- ☐ **D07.** Avec `import static java.lang.Math.max;` et `import static java.lang.Math.PI;` : `max(3, 8)`, puis `Math.round(PI * 100)`.
  → `D07 : 8 314`

## Expériences (hors sortie attendue)

1. Dans `total()`, rends `value` au lieu de `created` : quelle erreur ? (Pas de `this` dans un contexte `static`.)
2. Supprime `STEP = base / 5;` : quelle erreur ? Et une deuxième affectation de `STEP` dans un autre bloc `static` ?
3. `box = new int[2];` après la déclaration `final` : quelle erreur ?
4. `import static java.lang.Math;` (sans membre) : que dit `javac` ?
5. Deux imports static du même nom, depuis deux classes : que se passe-t-il quand on l'utilise ?
6. Pourquoi `STEP` vaut-il toujours 2, même après `Counter.base = 100` ?

## Sortie attendue complète

```
D01 : Recall04 charge
D02 : main
D03 : champ static base = 10
D03 : bloc static = 2
D03 : champ d'instance value = 10
D03 : bloc d'instance id = 1
D03 : champ d'instance value = 10
D03 : bloc d'instance id = 2
D04 : 1 2 2 12 14 12
D03 : champ d'instance value = 100
D03 : bloc d'instance id = 3
D05 : 100 3 3
D06 : 2 ab
D07 : 8 314
```

## Carte mémoire (à lire **après** le drill)

<details><summary>Ouvrir la carte</summary>

**L'ordre d'initialisation :**
1. **La classe**, une seule fois, à son premier usage : les champs `static` et les blocs `static`, dans l'ordre du fichier. La classe du `main` est chargée avant `main`.
2. **Chaque objet**, à chaque `new` : les champs d'instance et les blocs `{ }`, dans l'ordre du fichier (puis le constructeur, au chapitre 6).

**`static` :**
- un membre `static` appartient à la **classe**, pas à un objet : une seule copie, partagée ;
- il s'appelle par `Classe.membre`. Via une référence, même `null`, ça marche aussi (IntelliJ et `javac -Xlint:static` le signalent, `javac` seul ne dit rien), car seul le **type déclaré** compte ;
- une méthode `static` ne voit pas directement les membres d'instance (pas de `this`) ;
- une méthode d'instance voit tout.

**`final` :**
- `static final` : affecté une seule fois, sur sa ligne ou dans un bloc `static` ;
- `final` d'instance : sur sa ligne, dans un bloc `{ }` (ou dans un constructeur) ;
- `final` sur une référence : on ne peut pas la **réassigner**, mais l'objet reste modifiable.

**L'import static :**
- `import static paquet.Classe.membre;` ou `import static paquet.Classe.*;` ;
- il importe des **membres**, pas des classes.

</details>
