# Drill de rappel 3 — L'ordre d'initialisation avec héritage

> Première fois ? Lis d'abord le mode d'emploi [`ch6_classdesign/PARCOURS.md`](../../PARCOURS.md).

**Chrono cible :** 15 min, puis 8 min.

**Règles :**
- Tout se fait de mémoire.
- Fichier `Recall03.java`, paquet `ch6_classdesign.drills.r03_initorder`. `Recall03` a le même `LOG` et le même `flush()` qu'au drill 2.
- **Prédis chaque ligne sur papier avant de lancer.**
- Les classes (chaque trace est ajoutée avec un espace devant) :

| Classe | Contenu |
|---|---|
| `Parent` | bloc `static` → ` Ps` ; bloc `{ }` → ` Pi` ; constructeur → ` Pc`, puis ` vu=` + `show()` ; `String show()` rend `parent` |
| `Child extends Parent` | `int value = 5;` ; bloc `static` → ` Cs` ; bloc `{ }` → ` Ci` ; constructeur → ` Cc` ; `show()` redéfini rend `String.valueOf(value)` |
| `Other` | `static final String CONST = "K";` ; `static int counter = 7;` ; bloc `static` → ` Os` |
| `Base` | `static int shared = 3;` ; bloc `static` → ` Bs` |
| `Sub extends Base` | bloc `static` → ` Ss` |

**Les notions de ce drill ont été apprises dans :** projet 2 (étapes 1 à 3). Si un défi te semble totalement inconnu, ce drill arrive trop tôt : refais d'abord la leçon de ce projet.

<details><summary><b>Comment faire ce drill, concrètement</b> (à lire la 1re fois)</summary>

1. **Note l'heure** de départ (montre ou téléphone).
2. **Crée la classe** : clic droit sur le dossier `r03_initorder` → **New** → **Java Class** → `Recall03`. S'il faut d'autres classes, place-les comme le disent les **Règles** ci-dessus ; si elles ne précisent rien, écris-les dans le même fichier, sous `Recall03`, sans `public`.
3. **Écris `main`**, puis **une ligne par défi**. La ligne qui suit la flèche `→` est **exactement** ce que ton programme doit afficher : `System.out.println("D01 : " + …);`, où les `…` sont des valeurs que **Java** calcule, jamais recopiées.
4. **Lance `Recall03`** avec la flèche verte, pour voir tes lignes.
5. **Bloqué plus de 3 minutes sur un défi ?** Écris un commentaire `// D03 : ✗` à sa place, et passe au suivant.
6. **Lance `Check.java`** (flèche verte). Une ligne `[FAIL]` te montre `attendu` et `obtenu`.
7. **Ensuite seulement**, ouvre la **carte mémoire** tout en bas (en aperçu Markdown, clique sur le triangle « Ouvrir la carte »), relis tes ✗, et fais les **expériences** (écris la ligne, compile, lis le message, efface la ligne).
8. **Note** la date, ton temps et tes ✗ dans le tableau de [`drills/README.md`](../README.md).

</details>

## Défis

- ☐ **D01.** `new Child();`, puis `flush()`.
  → `D01 : Ps Cs Pi Pc vu=0 Ci Cc`
- ☐ **D02.** Un deuxième `new Child();`.
  → `D02 : Pi Pc vu=0 Ci Cc`
- ☐ **D03.** `new Parent();`.
  → `D03 : Pi Pc vu=parent`
- ☐ **D04.** `Child c = new Child();`. Affiche le journal **à partir de** `vu=` (`substring` et `indexOf`), puis ` apres=` + `c.value`.
  → `D04 : vu=0 Ci Cc apres=5`
- ☐ **D05.** Dans cet ordre :
  1. `String constant = Other.CONST;` ;
  2. `first = flush()` ;
  3. `int counter = Other.counter;`.
  
  Affiche `[first]`, `constant`, `[` + `flush()` + `]` et `counter`.
  → `D05 : [] K [Os] 7`
- ☐ **D06.** `int inherited = Sub.shared;`. Affiche `[` + `flush()` + `]` et `inherited`.
  → `D06 : [Bs] 3`

## Expériences (hors sortie attendue)

1. Rends `value` `final` (`final int value = 5;`) : que devient `vu=` ? (Une constante de compilation est remplacée par sa valeur.)
2. Retire `final` de `CONST` : que devient D05 ?
3. Ajoute `static int own = 1;` dans `Sub`, puis lis `Sub.own` : quelles traces apparaissent, et dans quel ordre ?

## Sortie attendue complète

```
D01 : Ps Cs Pi Pc vu=0 Ci Cc
D02 : Pi Pc vu=0 Ci Cc
D03 : Pi Pc vu=parent
D04 : vu=0 Ci Cc apres=5
D05 : [] K [Os] 7
D06 : [Bs] 3
```

## Carte mémoire (à lire **après** le drill)

<details><summary>Ouvrir la carte</summary>

**1. Le chargement de la classe** (une fois, au premier usage actif) :
- le parent d'abord, puis l'enfant ;
- pour chacun, les champs `static` et les blocs `static`, dans l'ordre du fichier.

**2. La construction d'un objet** (à chaque `new`) :
1. le constructeur appelé délègue (`this(...)`) ou remonte (`super(...)`) jusqu'à `Object` ;
2. puis, **du parent vers l'enfant** : les champs d'instance et les blocs `{ }` (dans l'ordre du fichier), puis le reste du corps du constructeur.

**Le piège :** une méthode redéfinie appelée dans le constructeur du parent s'exécute dans l'enfant **avant** que ses champs soient initialisés (0, `false`, `null`).

**Ce qui ne charge PAS une classe :**
- lire une **constante de compilation** (`static final` d'un type primitif ou `String`, initialisée par une constante) ;
- `Sub.shared` charge seulement `Base`, la classe qui **déclare** le champ.

</details>
