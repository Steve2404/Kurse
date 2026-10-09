# 🏠 Palais mental — chapitre 7 : la chambre 2, stations 1 à 6

> **Avant :** lis une fois [`PALAIS_MENTAL.md`](../../../../PALAIS_MENTAL.md). Le salon, la cuisine et la chambre 1 (chapitres 1 à 6) viennent avant dans la balade.
> **Quand :** après le capstone du chapitre, puis avant chaque répétition des drills.
> **Comment :** lis la règle, ferme les yeux, joue la scène 10 secondes, redis la règle à voix haute.

---

### 1. 🚪 La porte de la chambre 2 — les interfaces

- **Image :** la porte est un **contrat géant** affiché en public. Chaque clause est **`public abstract`** sans qu'on l'écrive. Les chiffres imprimés sur le contrat sont **gravés** (`public static final`). Une clause **`default`** arrive avec un **exemple déjà rempli**, que tu peux réécrire. Si **deux contrats** donnent le même exemple, tu dois **trancher toi-même**, et tu peux citer l'un des deux : `A.super.m()`. Les clauses **`static`** restent **collées au contrat** : on les appelle par le nom de l'interface, jamais par l'objet. Les clauses **`private`** sont des notes internes.
- **À retenir :**
  - méthode abstraite d'interface : implicitement `public abstract` ; champ : implicitement `public static final` ;
  - `default` : public, avec corps, redéfinissable ; deux `default` identiques hérités → la classe **doit** redéfinir, et peut appeler `A.super.m()` ;
  - méthode `static` d'interface : **pas héritée**, appelée par `NomInterface.m()` ;
  - méthode `private` (ou `private static`) : aide interne aux `default` ;
  - implémenter une méthode avec un accès plus fermé que `public` ne compile pas.
- **Mon image :** …

### 2. 🛏️ Le lit — les `enum`

- **Image :** sur le lit, une **rangée de peluches numérotées à partir de 0** (`ordinal()`). Tu cries `values()` : elles se lèvent **toutes, dans l'ordre**. Tu cries `valueOf("OURS")` : l'ours se lève ; tu cries un nom **qui n'existe pas** : la rangée hurle (`IllegalArgumentException`). Si les peluches ont **un corps** (des champs, un constructeur), un **point-virgule** doit fermer la rangée. Leur constructeur est **secret** (`private` automatiquement).
- **À retenir :**
  - `values()`, `valueOf(String)` (nom exact, sinon `IllegalArgumentException`), `name()`, `ordinal()` (à partir de 0) ;
  - un `enum` avec des champs ou des méthodes : `;` **après** la dernière constante ;
  - son constructeur est implicitement `private` ; on ne fait jamais `new` d'un `enum` ;
  - dans un `switch`, on écrit `case OURS`, sans le nom du type ;
  - une méthode `abstract` dans un `enum` doit être implémentée par **chaque** constante.
- **Mon image :** …

### 3. 🛌 L'oreiller — les classes `sealed`

- **Image :** l'oreiller porte une **liste d'invités brodée** : `sealed … permits A, B`. Seuls A et B peuvent dormir dessus. Chacun doit déclarer, en entrant, s'il **ferme la porte** derrière lui (`final`), s'il **dresse sa propre liste** (`sealed`) ou s'il **ouvre à tout le monde** (`non-sealed`).
- **À retenir :**
  - `sealed class X permits A, B` : seules A et B peuvent hériter ;
  - chaque sous-classe directe est `final`, `sealed` ou `non-sealed` (une des trois, obligatoirement) ;
  - les sous-classes sont dans le même paquet (ou le même module nommé) ; `permits` peut être omis si elles sont **dans le même fichier** ;
  - marche aussi avec les interfaces (`sealed interface … permits …`).
- **Mon image :** …

### 4. 🕯️ La table de nuit — les `record`

- **Image :** sur la table de nuit, une **boîte en verre scellée** (`record`) : les objets à l'intérieur sont **`private final`**, la boîte est **`final`**. Pour lire un objet, tu appelles son **nom tout court** (`nom()`, pas `getNom()`). La boîte sait toute seule se **comparer** (`equals`, `hashCode`) et se **décrire** (`toString`). Son **constructeur compact** est un **douanier** sans parenthèses : il vérifie ou corrige les paramètres, mais il n'a **pas le droit** d'écrire `this.nom = …`.
- **À retenir :**
  - un `record` est `final` ; ses composants deviennent des champs `private final` et des accesseurs `nom()` ;
  - `equals`, `hashCode`, `toString` et le constructeur canonique sont générés ;
  - constructeur compact : `record P(int x) { P { x = Math.abs(x); } }` : il modifie les **paramètres**, pas `this.x` ;
  - **pas** de champ d'instance en plus (les champs `static` sont permis) ; un `record` peut implémenter des interfaces, pas hériter d'une classe.
- **Mon image :** …

### 5. ⏰ Le réveil — les classes imbriquées

- **Image :** le réveil s'ouvre comme une **poupée russe**. La **poupée intérieure** (classe interne) ne vit que **dans** une poupée extérieure : pour la créer, `exterieur.new Interne()`. La **poupée `static`** vit seule, sans extérieur. Une **poupée locale** naît dans une méthode et ne peut regarder que les variables **qui ne bougent plus** (effectivement finales). Une **poupée anonyme** apparaît d'un coup, sans nom, et se ferme par **`};`**.
- **À retenir :**
  - classe interne : a besoin d'une instance extérieure (`ext.new In()`), accède à tout, `Exterieur.this.x` pour le champ masqué ;
  - classe imbriquée `static` : pas d'instance extérieure ;
  - classe locale (dans une méthode) : n'utilise que des variables locales **effectivement finales** ;
  - classe anonyme : hérite d'**une** classe ou implémente **une** interface, se termine par `};`.
- **Mon image :** …

### 6. 🔦 La lampe de chevet — le polymorphisme et les casts

- **Image :** la lampe projette des **ombres chinoises**. L'**ombre** (le type de la référence) décide **ce que tu as le droit de demander**. Mais quand tu demandes un geste redéfini, c'est la **vraie main** (l'objet réel) qui bouge. Tu forces une ombre de `Animal` à devenir `Chien` : le compilateur accepte si c'est **possible** ; si la vraie main était un `Chat`, l'ombre **se déchire** à l'exécution (`ClassCastException`). Une ombre de `String` vers `Integer` : sans lien, refusée **dès la compilation**.
- **À retenir :**
  - le **type de la référence** décide quelles méthodes sont accessibles ; l'**objet réel** décide quelle version redéfinie s'exécute ;
  - cast vers un sous-type : compile, mais `ClassCastException` si l'objet n'en est pas un ; tester avec `instanceof` avant ;
  - cast entre classes **sans lien** : ne compile pas ; vers une interface depuis une classe non `final` : compile ;
  - les champs et les méthodes `static` ne sont **pas** polymorphes.
- **Mon image :** …

---

## ⚡ La balade éclair

1. Station 1 : comment appelle-t-on une méthode `static` d'interface ?
2. Station 2 : que fait `valueOf("INCONNU")` ?
3. Station 3 : quels sont les trois mots possibles pour une sous-classe directe d'une classe `sealed` ?
4. Station 4 : que n'a-t-on pas le droit d'écrire dans un constructeur compact ?
5. Station 5 : comment crée-t-on une instance d'une classe interne depuis l'extérieur ?
6. Station 6 : qu'est-ce qui décide de la méthode redéfinie exécutée ?
