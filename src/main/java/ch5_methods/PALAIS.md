# 🏠 Palais mental — chapitre 5 : la chambre 1, stations 1 à 6

> **Avant :** lis une fois [`PALAIS_MENTAL.md`](../../../../PALAIS_MENTAL.md). Le salon (chapitres 1 et 2) et la cuisine (chapitres 3 et 4) viennent avant dans la balade.
> **Quand :** après le capstone du chapitre, puis avant chaque répétition des drills.
> **Comment :** lis la règle, ferme les yeux, joue la scène 10 secondes, redis la règle à voix haute.

---

### 1. 🚪 La porte de la chambre 1 — déclarer une méthode, les accès

- **Image :** la porte a **quatre serrures** de plus en plus ouvertes : un **cadenas `private`** (moi seul), une **serrure sans nom** (la famille du même paquet), une **serrure `protected`** (la famille, plus les enfants partis vivre ailleurs), et une **porte grande ouverte `public`**. Sur la porte, la plaque est toujours écrite **dans le même ordre** : accès, mots spéciaux (`static`, `final`…), **type de retour**, nom, parenthèses.
- **À retenir :**
  - accès, du plus fermé au plus ouvert : `private` < (paquet, sans mot) < `protected` < `public` ;
  - ordre : `public static final int calcule(int x)` : accès et spécificateurs **avant** le type de retour, le type **juste avant** le nom ;
  - le type de retour est **obligatoire** (`void` sinon) ; une méthode non `void` doit faire `return` d'une valeur sur **tous les chemins**.
- **Mon image :** …

### 2. 🛏️ Le lit — les varargs

- **Image :** au pied du lit, un **sac extensible `...`** : tu peux y mettre **zéro, un ou cent** doudous, ou carrément **un carton** (un tableau) déjà rempli. Il n'y a **qu'un seul** sac, et il est **toujours au bout du lit** (le dernier paramètre). Ouvert, c'est simplement un **tableau**.
- **À retenir :**
  - `void m(int a, String... noms)` : **un seul** varargs, **en dernier** ;
  - on peut l'appeler avec rien, une liste de valeurs, ou un tableau ; `m(1, null)` passe un tableau `null` ;
  - dans la méthode, `noms` est un `String[]`.
- **Mon image :** …

### 3. 🛌 L'oreiller — `static`

- **Image :** un **oreiller unique**, cousu au **lit lui-même** (la classe) : tous les dormeurs (les objets) le partagent. L'oreiller **ne connaît aucun dormeur** : depuis une méthode `static`, tu ne peux pas toucher un champ d'instance sans dire **de quel objet**. Bizarre mais vrai : même un dormeur **fantôme** (`null`) peut appeler l'oreiller, car Java regarde le **type**, pas l'objet.
- **À retenir :**
  - un membre `static` appartient à la **classe**, partagé par tous les objets ;
  - une méthode `static` ne peut **pas** utiliser `this` ni un membre d'instance sans référence ;
  - appeler un `static` par une référence marche, **même `null`** (pas de `NullPointerException`) ;
  - `import static java.lang.Math.max;` (et pas `static import`) ; `static final` = constante, à initialiser une seule fois.
- **Mon image :** …

### 4. 🕯️ La table de nuit — le passage par valeur

- **Image :** tu poses **une photocopie** de ta clé sur la table de nuit (Java copie toujours la valeur). La méthode peut **jeter** la photocopie et en prendre une autre : ta vraie clé **ne bouge pas**. Mais avec la photocopie, elle peut **ouvrir ta maison et repeindre le salon** : ça, tu le verras en rentrant.
- **À retenir :**
  - Java passe **toujours une copie** : pour un primitif, la valeur ; pour un objet, la **référence** ;
  - **réaffecter** le paramètre (`p = new …`) ne change rien chez l'appelant ;
  - **modifier l'objet** pointé (`sb.append(…)`) se voit chez l'appelant ;
  - une `String` étant immuable, une méthode ne peut **jamais** modifier la chaîne de l'appelant.
- **Mon image :** …

### 5. ⏰ Le réveil — la surcharge, qui gagne ?

- **Image :** le réveil sonne et **quatre invités** se disputent pour répondre, toujours **dans cet ordre** : le **jumeau parfait** (type exact), puis le **grand frère** (un primitif plus large), puis le **déménageur** (l'autoboxing, `int` → `Integer`), et en dernier le **sac extensible** (varargs). Personne n'a le droit de faire **deux sauts** : un `int` ne devient **jamais** un `Long`.
- **À retenir :**
  - ordre de choix : type **exact** → primitif **plus large** → **autoboxing** → **varargs** ;
  - une seule conversion : `int` → `long` oui, `int` → `Integer` oui, `int` → `Long` **non** ;
  - surcharger = même nom, **paramètres différents** ; changer seulement le type de retour ne compile pas ;
  - deux candidats aussi bons → appel **ambigu**, ne compile pas.
- **Mon image :** …

### 6. 🔦 La lampe de chevet — `final`, la portée et la récursivité

- **Image :** la lampe de chevet a un **ampoule soudée** (`final`) : une fois allumée, on ne la change plus. Elle éclaire **seulement le bloc** où elle est posée (la portée). Elle projette au mur **une lampe qui projette une lampe qui projette une lampe…** : s'il n'y a pas de **dernière lampe** (le cas de base), le mur **s'effondre** (`StackOverflowError`).
- **À retenir :**
  - une variable `final` ne s'affecte **qu'une fois** ; un objet `final` peut quand même être **modifié** de l'intérieur ;
  - une variable locale n'existe que **dans son bloc** `{ }` ;
  - une méthode récursive a besoin d'un **cas de base** qui s'arrête ; sinon → `StackOverflowError`.
- **Mon image :** …

---

## ⚡ La balade éclair

1. Station 1 : range ces accès du plus fermé au plus ouvert : `public`, `private`, `protected`, paquet.
2. Station 2 : combien de varargs, et à quelle place ?
3. Station 3 : `((Ma) null).methodeStatique()` lance-t-il une exception ?
4. Station 4 : une méthode peut-elle remplacer l'objet de l'appelant par un autre ?
5. Station 5 : `m(5)` choisit-il `m(long)`, `m(Integer)` ou `m(int...)` ?
6. Station 6 : qu'arrive-t-il à une récursion sans cas de base ?
