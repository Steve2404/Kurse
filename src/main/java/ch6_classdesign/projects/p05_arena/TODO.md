# Projet 5 — L'arène de combat

> Première fois ? Lis d'abord le mode d'emploi [`ch6_classdesign/PARCOURS.md`](../../PARCOURS.md).
>
> **Bloqué sur une étape ?** [`INDICES.md`](INDICES.md) donne deux indices repliés par étape, sans code. **Étape finie ?** [`solution/CORRIGE.md`](solution/CORRIGE.md) donne, étape par étape, le code de l'étape, les **réponses aux questions** et le résultat exact des **expériences**. N'ouvre que la section de l'étape que tu viens de faire.

**Notions visées (chapitre 6) :**
- une **classe abstraite** qui contient l'état commun et un comportement par défaut ;
- des **méthodes abstraites** : `damageTo`, `copy` ;
- un **constructeur de copie** chaîné (`this(...)` dans le parent, `super(other)` dans les enfants) ;
- **redéfinir en réutilisant** le parent : `super.takeDamage(...)`, `super.act(...)`, `super.status()` ;
- les **types de retour covariants** : `Warrior copy()` redéfinit `Fighter copy()`, donc aucun cast n'est nécessaire ;
- une **méthode `final`** : `isAlive()` ;
- des constructeurs `private` dans les sous-classes.

Côté algorithmes :
- une **simulation** déterministe au tour par tour ;
- l'ordre d'**initiative** (tri stable par vitesse) ;
- le choix de la **cible** (le plus faible) ;
- un soigneur qui décide entre soigner et frapper.

**Ce qui est donné :** `Data.java` (deux équipes) et `Check.java`.

**Ce que TU crées :** dans le paquet `ch6_classdesign.projects.p05_arena` :
- `Fighter` (abstraite), `Warrior`, `Mage`, `Tank`, `Healer` ;
- **`Arena`** (le `main`).

**Règle du crescendo :** chapitres 1 à 6. Pas de collection ni de cast d'objet.

**Tes outils pour ce projet** (pas d'arguments, `Data.java` donné) :

```
javac -d build/ch6-p05 -sourcepath src/main/java src/main/java/ch6_classdesign/projects/p05_arena/Arena.java
java "-Duser.language=fr" -cp build/ch6-p05 ch6_classdesign.projects.p05_arena.Arena
```

---

## Tableau de bord

### ☐ Étape 1 — Les combattants

```
equipes : Warrior Conan 120/120 Mage Merlin 70/70 mana 30 Healer Mira 80/80 VS Tank Golem 100/100 Warrior Brutus 110/110 Mage Sabrina 65/65 mana 30
```

**📖 La leçon : le constructeur de copie.** Un constructeur qui reçoit un objet de **sa propre classe** et en recopie les champs. Il délègue souvent à l'autre constructeur avec `this(…)` (projet 1).

**📖 La leçon : le retour covariant.** Une fille qui redéfinit une méthode peut rendre un type **plus précis** que celui de la mère :

```java
class Animal { Animal copie() { return new Animal(); } }
class Chien extends Animal {
    @Override
    Chien copie() { return new Chien(); }    // rend un Chien, pas seulement un Animal
}
Chien c = new Chien();
Chien copie = c.copie();                     // pas besoin de cast
```

**📖 Rappel :** `getClass().getSimpleName()` donne le nom de la classe de l'objet (projet 1, étape 3). Une redéfinition peut **compléter** la version de la mère avec `super.methode(…)`.

**👉 À toi :**

- **`Fighter`** (`abstract`) :
  - champs : `private final String name`, `private final int maxHp`, `private int hp`, `protected final int attack`, `private final int speed` ;
  - `protected Fighter(String name, int hp, int attack, int speed)` ;
  - le **constructeur de copie** `protected Fighter(Fighter other)`, qui délègue avec `this(other.name, other.maxHp, …)` : la copie repart à pv pleins ;
  - `protected abstract int damageTo(Fighter target)` et `public abstract Fighter copy()` ;
  - `public int takeDamage(int damage)` rend les dégâts **réellement** encaissés (les pv ne descendent pas sous 0) ;
  - `public int heal(int amount)` rend les pv réellement rendus (les pv ne dépassent pas le maximum) ;
  - **`public final boolean isAlive()`** ;
  - `static Fighter weakest(Fighter[] team)` : le vivant qui a le moins de pv, le premier en cas d'égalité ;
  - `public String act(Fighter[] allies, Fighter[] enemies)` frappe le plus faible ennemi et rend `nom -> cible : dégâts (statut de la cible)` ;
  - `status()` = `nom pv/max` ;
  - `toString()` = `NomDeClasse status()`, avec `getClass().getSimpleName()`.
- **`Warrior`** : un coup sur trois est doublé (compteur de coups). Son `copy()` rend un **`Warrior`**.
- **`Mage`** :
  - `mana = 30` et `SPELL_COST = 10` ;
  - avec assez de mana, le sort fait `attack * 3 / 2` et coûte 10 ; sinon, `attack / 2` ;
  - `status()` = `super.status() + " mana " + mana`.
- **`Tank`** : `ARMOR = 4`. `takeDamage` est redéfini : `return super.takeDamage(Math.max(1, damage - ARMOR));`.
- **`Healer`** :
  - `HEAL = 25` ;
  - `act` est redéfini : si l'allié le plus faible est **sous la moitié** de ses pv (`hp * 2 < max`), le soigneur le soigne et rend `nom soigne allie +soin (statut)` ;
  - sinon, `return super.act(allies, enemies);`.
- Chaque sous-classe a un constructeur `private` de copie, qui appelle `super(other)`.
- **Dans `Arena`**, `static Fighter create(String line)` utilise un `switch` sur W, M, T et H, et `static Fighter[] team(String[])` construit une équipe.

### ☐ Étape 2 — Le combat

```
T1 Merlin -> Sabrina : 30 (Sabrina 35/65 mana 30)
T1 Sabrina -> Merlin : 33 (Merlin 37/70 mana 20)
...
vainqueur : equipe A
apres : Conan 120/120 Merlin 0/70 mana 0 Mira 49/80
```

**📖 Rappel :** l'objet décide de la version d'une méthode (projet 1, étape 3). Le tri par insertion stable (chapitre 4, projet 7).

**👉 À toi :**

- **Avant le combat**, fais la copie des deux équipes (`copy()`), et un `Warrior conanCopy = new Warrior("Conan", 120, 18, 6).copy();` **sans cast**.
- **`static String battle(Fighter[] a, Fighter[] b, boolean verbose)`** :
  1. réunis les 6 combattants dans un tableau, A d'abord ;
  2. trie-les par vitesse décroissante (insertion, **stable**) ;
  3. tour par tour, tant que les deux équipes vivent et que le tour ne dépasse pas `Data.MAX_ROUNDS` : chaque combattant vivant agit, sauf si une équipe vient de tomber ;
  4. un combattant de A agit avec `act(a, b)`, un combattant de B avec `act(b, a)` ;
  5. en mode verbeux, affiche `T<tour> ` puis le texte de l'action ;
  6. rends `A`, `B` ou `nul`.
- Dans `main`, affiche ensuite :
  - `vainqueur : equipe ` + le résultat de `battle(a, b, true)` ;
  - puis `apres :` suivi du `status()` de chaque membre de A, séparés par un espace.
- La ligne `equipes :` affiche le `toString()` de chaque membre de A, puis ` VS`, puis ceux de B.
- **Question :** dans la ligne `T1 Sabrina -> Merlin`, pourquoi `Merlin` affiche-t-il son mana, alors qu'on appelle `target.status()` sur un `Fighter` ?

### ☐ Étape 3 — La revanche avec les copies

```
revanche avec les copies, B en premier : equipe B ; les copies partaient a 120 pv ; Warrior Conan 120/120
```

**📖 Rappel :** le retour covariant (étape 1). Ce qui compte pour savoir ce qu'on a le droit d'écrire, c'est le type **déclaré** de la variable (projet 1, étape 3).

**👉 À toi :**

- `battle(b2, a2, false)` : les copies de B passent en **premier** paramètre.
- Affiche ensuite `a2[0].getMaxHp()` et `conanCopy`.
- **Question :** pourquoi `Warrior w = conan.copy();` compile-t-il sans cast ? Et `Warrior w = someFighter.copy();` ?
- **Expériences :**
  - dans `Warrior`, change le retour de `copy()` en `Object` ;
  - redéfinis `isAlive()` ;
  - mets `takeDamage` en `protected` dans `Tank`.

---

## Checklist (vérifiée par `Check`)

- `Data.TEAM_A`, `Data.TEAM_B` et `Data.MAX_ROUNDS` ;
- `abstract class Fighter`, `protected abstract int damageTo(`, `public abstract Fighter copy()` ;
- `public final boolean isAlive()` ;
- les 4 `copy()` covariants ;
- `super.takeDamage(`, `super.act(`, `super.status()` ;
- `protected Fighter(Fighter` et `super(other)`.

---

## Sortie attendue complète

```
equipes : Warrior Conan 120/120 Mage Merlin 70/70 mana 30 Healer Mira 80/80 VS Tank Golem 100/100 Warrior Brutus 110/110 Mage Sabrina 65/65 mana 30
T1 Merlin -> Sabrina : 30 (Sabrina 35/65 mana 30)
T1 Sabrina -> Merlin : 33 (Merlin 37/70 mana 20)
T1 Conan -> Sabrina : 18 (Sabrina 17/65 mana 20)
T1 Brutus -> Merlin : 16 (Merlin 21/70 mana 20)
T1 Mira soigne Merlin +25 (Merlin 46/70 mana 20)
T1 Golem -> Merlin : 14 (Merlin 32/70 mana 20)
T2 Merlin -> Sabrina : 17 (Sabrina 0/65 mana 20)
T2 Conan -> Golem : 14 (Golem 86/100)
T2 Brutus -> Merlin : 16 (Merlin 16/70 mana 10)
T2 Mira soigne Merlin +25 (Merlin 41/70 mana 10)
T2 Golem -> Merlin : 14 (Merlin 27/70 mana 10)
T3 Merlin -> Golem : 26 (Golem 60/100)
T3 Conan -> Golem : 32 (Golem 28/100)
T3 Brutus -> Merlin : 27 (Merlin 0/70 mana 0)
T3 Mira -> Golem : 2 (Golem 26/100)
T3 Golem -> Mira : 14 (Mira 66/80)
T4 Conan -> Golem : 14 (Golem 12/100)
T4 Brutus -> Mira : 16 (Mira 50/80)
T4 Mira -> Golem : 2 (Golem 10/100)
T4 Golem -> Mira : 14 (Mira 36/80)
T5 Conan -> Golem : 10 (Golem 0/100)
T5 Brutus -> Mira : 16 (Mira 20/80)
T5 Mira soigne Mira +25 (Mira 45/80)
T6 Conan -> Brutus : 36 (Brutus 74/110)
T6 Brutus -> Mira : 32 (Mira 13/80)
T6 Mira soigne Mira +25 (Mira 38/80)
T7 Conan -> Brutus : 18 (Brutus 56/110)
T7 Brutus -> Mira : 16 (Mira 22/80)
T7 Mira soigne Mira +25 (Mira 47/80)
T8 Conan -> Brutus : 18 (Brutus 38/110)
T8 Brutus -> Mira : 16 (Mira 31/80)
T8 Mira soigne Mira +25 (Mira 56/80)
T9 Conan -> Brutus : 36 (Brutus 2/110)
T9 Brutus -> Mira : 32 (Mira 24/80)
T9 Mira soigne Mira +25 (Mira 49/80)
T10 Conan -> Brutus : 2 (Brutus 0/110)
vainqueur : equipe A
apres : Conan 120/120 Merlin 0/70 mana 0 Mira 49/80
revanche avec les copies, B en premier : equipe B ; les copies partaient a 120 pv ; Warrior Conan 120/120
```
