# Projet 5 — Corrigé étape par étape

> **Quand le lire :** ouvre **seulement la section de l'étape** que tu viens de terminer, pour comparer ton code et tes réponses. Le programme complet est dans ce dossier : `Fighter`, `Warrior`, `Mage`, `Tank`, `Healer` et `Arena`.
>
> Les messages ci-dessous ont été obtenus en direct avec **JDK 17** (`javac` 17.0.18), sur des classes de test réduites (`F`, `W`, `T`).

---

## Étape 1 — Les combattants

**Le code :** [`Fighter.java`](Fighter.java), les quatre sous-classes, et `create` et `team` d'[`Arena.java`](Arena.java).

**Les trois façons de redéfinir :**

| Classe | Méthode | Style |
|---|---|---|
| `Warrior`, `Mage` | `damageTo` | **implémenter** une méthode abstraite |
| `Tank` | `takeDamage` | **réutiliser** le parent : on réduit les dégâts, puis `super.takeDamage(…)` fait le reste |
| `Healer` | `act` | **compléter** : un cas spécial (soigner), sinon `super.act(…)` |
| `Mage` | `status` | **ajouter** : `super.status() + " mana " + mana` |

**Le constructeur de copie :** `protected Fighter(Fighter other)` délègue à l'autre constructeur avec `this(…)`. Il lit les champs `private` de `other`, ce qui est permis car on est dans la classe `Fighter`. La copie a donc les mêmes caractéristiques, mais **pv pleins**, et un `Warrior` a son propre compteur `hits` remis à 0.

**`isAlive()` est `final`** : la règle « vivant = pv > 0 » est la même pour tous. Le combat en dépend.

---

## Étape 2 — Le combat

**Le code :** les méthodes `alive` et `battle`, et le `main` jusqu'à la ligne `apres`.

**L'ordre d'initiative :** les vitesses sont Merlin 8, Sabrina 7, Conan 6, Brutus 5, Mira 4, Golem 2. Le tri est stable : à vitesse égale, l'ordre A puis B serait conservé.

**Le premier coup de Merlin :** il a 30 de mana, donc il lance un sort à 20 × 3 / 2 = **30** dégâts, sur la cible ennemie la plus faible. Au début, c'est Sabrina (65 pv, moins que les 100 de Golem et les 110 de Brutus).

**`x == f` pour trouver l'équipe :** on compare des **références**. On veut savoir si c'est **le même objet**, pas un combattant « égal ».

**Question — pourquoi `Merlin` affiche son mana ?** `target` est déclaré `Fighter`, mais l'objet est un `Mage`. `status()` est une méthode d'instance **redéfinie** : la JVM exécute celle de l'**objet**, donc `Mage.status()`, qui ajoute ` mana 20`. C'est le polymorphisme : `act` est écrite **une seule fois** dans `Fighter`, et chaque cible s'affiche à sa façon.

---

## Étape 3 — La revanche avec les copies

**Le code :** la fin du `main`.

**Les copies sont indépendantes :** elles ont été faites **avant** le premier combat, et ce sont de **nouveaux objets**. Le combat a abîmé `a` et `b`, pas `a2` et `b2`. `a2[0].getMaxHp()` vaut 120, et `conanCopy` affiche `Warrior Conan 120/120`.

**Question — pourquoi `Warrior w = conan.copy();` compile sans cast ?** `Warrior` redéfinit `copy()` avec le type de retour **`Warrior`**, plus précis que `Fighter`. C'est un **type de retour covariant**, permis car un `Warrior` **est un** `Fighter` : le contrat du parent reste respecté. Appelé sur une variable de type `Warrior`, `javac` sait que le résultat est un `Warrior`.

**Et `Warrior w = someFighter.copy();` ?** **Non** :

```
error: incompatible types: F cannot be converted to W
```

(vérifié sur les classes de test.) Sur une variable de type `Fighter`, `javac` ne connaît que `Fighter copy()`. Le résultat pourrait être un `Mage`. Il faudrait un cast (chapitre 7).

**Expériences :**

| Expérience | Erreur de `javac` |
|---|---|
| `copy()` de `Warrior` rend `Object` | `copy() in W cannot override copy() in F` (`return type Object is not compatible with F`). Un type de retour peut **se préciser**, jamais s'élargir. `javac` ajoute `W is not abstract and does not override abstract method copy() in F` |
| redéfinir `isAlive()` | `isAlive() in W cannot override isAlive() in F` (`overridden method is final`) |
| `takeDamage` en `protected` dans `Tank` | `takeDamage(int) in T cannot override takeDamage(int) in F` (`attempting to assign weaker access privileges; was public`) |
