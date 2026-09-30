# Chapitre 6 (Class Design) — parcours, drills et plan de révision

Les **exercices** (`ch6_classdesign/exercises`, 01 → 18) t'apprennent les notions.
Les **drills** (`ch6_classdesign/drills/exercises`, 01 → 05) te les font répéter jusqu'à ce
qu'elles sortent toutes seules. Les données partagées sont le zoo de `drills/Zoo.java`
(4 noms, 4 sortes, des pattes, de la nourriture). Chaque drill contient aussi ses petites
classes à compléter (constructeurs, redéfinitions, classes abstraites, objets immuables).

Les corrigés (`solutions/` et `drills/solutions/`) sont **commentés** : chaque méthode
explique pourquoi on l'écrit ainsi et quel piège elle évite. Lis-les **après** avoir réussi.

---

## Par quoi commencer : exercices ou drills ?

**Les deux, en alternant, thème par thème.** Jour 1 : les exercices du thème
(comprendre). Jour 2 : le drill du thème (mémoriser).

| Étape | Thème | Jour 1 — exercices | Jour 2 — drills |
|---|---|---|---|
| 1 | Héritage, `this` et `super` | 01 → 02 → 03 | Drill01 (TODO 3, 5, 6, 8 à 10) |
| 2 | Constructeurs et initialisation | 04 → 05 → 06 → 07 → 08 | Drill01 (TODO 1, 2, 4, 7), Drill04 (TODO 6 à 10) |
| 3 | Redéfinition et cachage | 09 → 10 → 11 → 12 | Drill02 |
| 4 | Classes abstraites | 13 → 14 → 15 | Drill03 |
| 5 | Objets immuables | 16 → 17 | Drill04 (TODO 1 à 5) |
| 6 | Synthèse | 18 (capstone : la flotte) | Drill05 (kata mélangé) |

**Séance type (≈ 1 h) :** 1) les révisions dues (10 – 20 min), 2) la nouveauté,
3) 2 minutes de « carte vierge » : réécrire l'ordre d'initialisation, les 6 règles de
redéfinition et la recette d'un objet immuable.

**Conseil propre à ce chapitre :** devant `Parent p = new Enfant();`, demande-toi toujours
**qui choisit** : l'objet réel (méthode d'instance redéfinie) ou le type de la variable
(champ, méthode `static`, liste des méthodes appelables). C'est le piège favori de l'examen.

| Drill | Contenu | TODO |
|---|---|---|
| 01 `InheritanceAndConstructors` | affecter des `final`, `this(...)`, `super(...)`, `super.methode()`, tableau du type parent, `getClass()` | 10 |
| 02 `OverridingAndHiding` | `toString`, `equals(Object)`, `hashCode`, `super.price()`, retour covariant, champ et `static` via le parent, `instanceof`, `HashSet` | 10 |
| 03 `AbstractAndFinal` | remplir des méthodes `abstract`, méthode template `final`, classe anonyme, varargs du type abstrait | 10 |
| 04 `ImmutabilityAndInit` | copie défensive, objet valeur, compteur `static`, blocs `static` et d'instance, `final` affecté dans le constructeur | 10 |
| 05 `MixedKata` | une hiérarchie complète sur le zoo, **sans indiquer la forme** | 10 |

---

## Comment faire un drill

1. Lance un chronomètre.
2. Remplis les TODO **sans regarder la « CARTE MÉMOIRE »** en bas du fichier.
3. Bloqué plus d'une minute ? Regarde la carte, **cache-la, puis réécris de mémoire**.
   Mets une croix à côté de ce TODO : c'est un point faible.
4. Lance `main()` jusqu'à 100 %.
5. Note ton temps, ton score au premier lancement et tes TODO « croix » dans le tableau.

## Pour ne plus oublier

- **Rappel actif** : refaire depuis une page blanche vaut dix relectures.
- **Répétition espacée** : J, J+1, J+3, J+7, J+14, J+30, puis tous les 2 mois.
- **Mélange** : le Drill05 chaque semaine pendant la révision de l'examen.
- **Lecture de code** : les Javadoc des exercices 05, 08, 10 et 14 contiennent les
  **verdicts réels de `javac`** (`constructor P in class P cannot be applied to given types;`,
  `call to super must be first statement in constructor`, `recursive constructor invocation`,
  `variable x might not have been initialized`, `attempting to assign weaker access privileges`,
  `overridden method does not throw`, `is not abstract and does not override abstract method`…) :
  relis-les avant l'examen, puis fais les questions de révision du livre.

## Remettre un fichier à zéro pour le refaire

```
git restore src/main/java/ch6_classdesign/drills/exercises/Drill01_InheritanceAndConstructors.java
```

(tant que tes réponses ne sont pas commitées ; sinon `git restore --source=origin/main -- <chemin>`).
⚠️ Cela efface ta version : c'est voulu pour un drill.

## Tableau de suivi

Format : `date – temps – score au 1er lancement` (ex. `30/09 – 12 min – 9/10`).

| Drill | J | J+1 | J+3 | J+7 | J+14 | J+30 | TODO faibles |
|---|---|---|---|---|---|---|---|
| 01 Héritage et constructeurs | | | | | | | |
| 02 Redéfinition et cachage | | | | | | | |
| 03 Abstract et final | | | | | | | |
| 04 Immuabilité et initialisation | | | | | | | |
| 05 Kata mélangé | | | | | | | |
