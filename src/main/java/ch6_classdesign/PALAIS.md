# 🏠 Palais mental — chapitre 6 : la chambre 1, stations 7 à 12

> **Avant :** lis une fois [`PALAIS_MENTAL.md`](../../../../PALAIS_MENTAL.md). Les stations 1 à 6 de la chambre 1 gardent le chapitre 5.
> **Quand :** après le capstone du chapitre, puis avant chaque répétition des drills.
> **Comment :** lis la règle, ferme les yeux, joue la scène 10 secondes, redis la règle à voix haute.

---

### 7. 🚪 L'armoire — l'héritage et les constructeurs

- **Image :** l'armoire a **un seul parent** accroché au-dessus d'elle (une seule classe mère), et tout en haut, l'ancêtre de toutes les armoires : **`Object`**. Pour ouvrir l'armoire (le constructeur), tu dois **d'abord** frapper au parent, en **première ligne** : `super(…)`, ou passer par une autre porte de l'armoire avec `this(…)`. Si tu n'écris **aucune** porte, le menuisier en pose une vide. Mais si le parent n'a **pas de porte sans clé** (pas de constructeur sans argument), l'armoire **ne se monte pas**.
- **À retenir :**
  - une classe hérite d'**une seule** classe ; toute classe hérite d'`Object` ;
  - la première ligne d'un constructeur est `this(…)` ou `super(…)` ; sinon Java ajoute `super();` ;
  - le constructeur par défaut n'est ajouté que si **aucun** constructeur n'est écrit ;
  - si le parent n'a pas de constructeur sans argument, l'enfant doit appeler `super(arguments)` **explicitement**.
- **Mon image :** …

### 8. 🪞 Le miroir — redéfinir (`override`) et masquer

- **Image :** l'enfant se regarde dans le miroir et **refait le geste du parent** avec la **même signature**. Le miroir vérifie quatre règles : le geste est **au moins aussi visible** (pas plus fermé), le cadeau rendu est **le même ou plus précis** (retour covariant), et l'enfant ne lance **pas de nouvelle exception vérifiée plus large**. Les méthodes **`static`** et les **champs** ne se reflètent pas : ils sont **cachés** derrière, et c'est le **type de la référence** qui décide lequel on voit. Une méthode **`private`** du parent est invisible dans le miroir.
- **À retenir :**
  - redéfinir : même nom, mêmes paramètres ; accès **égal ou plus ouvert** ; retour **identique ou sous-type** ; pas d'exception vérifiée nouvelle ou plus large ;
  - une méthode d'instance redéfinie est choisie selon l'**objet réel** (à l'exécution) ;
  - une méthode `static` de même signature **masque** (choisie selon le **type de la référence**) ; `static` contre non-`static` ne compile pas ;
  - un champ de même nom **masque** aussi ; une méthode `private` n'est pas redéfinie.
- **Mon image :** …

### 9. 🖥️ Le bureau — les classes abstraites

- **Image :** sur le bureau, un **plan d'architecte** marqué `abstract`. Tu ne peux pas **habiter un plan** (`new` interdit). Certaines pièces du plan sont **dessinées en pointillés** (méthodes `abstract`, sans corps, terminées par `;`). Le premier enfant **qui construit vraiment** (la première classe concrète) doit remplir **toutes** les pièces en pointillés. Une pièce en pointillés ne peut pas être **fermée à clé** (`private`), **scellée** (`final`) ou **commune** (`static`).
- **À retenir :**
  - une classe `abstract` ne s'instancie pas ; elle peut avoir des constructeurs, des champs, des méthodes concrètes ;
  - une méthode `abstract` n'a **pas de corps** et n'existe que dans une classe abstraite (on verra les interfaces au chapitre 7) ;
  - `abstract` ne se combine **pas** avec `private`, `final` ou `static` ;
  - la **première sous-classe concrète** doit implémenter toutes les méthodes abstraites héritées.
- **Mon image :** …

### 10. 🪑 La chaise — l'ordre d'initialisation avec l'héritage

- **Image :** tu montes sur la chaise et tu regardes une **fusée à étages**. Au premier décollage de la classe, les **réservoirs `static`** s'allument : **le parent d'abord, puis l'enfant**, une seule fois. Puis, à chaque `new`, la fusée monte : **le parent** (ses champs et blocs, puis son constructeur), **ensuite l'enfant** (ses champs et blocs, puis son constructeur).
- **À retenir :**
  - au chargement de la classe (une fois) : `static` du parent, puis `static` de l'enfant, dans l'ordre du fichier ;
  - à chaque `new` : champs et blocs du parent, constructeur du parent, puis champs et blocs de l'enfant, constructeur de l'enfant ;
  - une méthode redéfinie appelée depuis le constructeur du parent s'exécute **côté enfant**, alors que les champs de l'enfant ont encore leur valeur **par défaut**.
- **Mon image :** …

### 11. 🪟 La fenêtre — `final`

- **Image :** trois cadenas sur la fenêtre. Sur la **classe** : personne ne peut **agrandir** la fenêtre (pas de sous-classe). Sur une **méthode** : personne ne peut **repeindre** ce geste (pas de redéfinition). Sur une **variable** : la vitre est posée **une seule fois**. Un champ d'instance `final` doit avoir sa vitre posée **avant la fin de chaque constructeur**, sinon la fenêtre reste **béante** et le compilateur refuse.
- **À retenir :**
  - classe `final` : pas de sous-classe ; méthode `final` : pas de redéfinition ; variable `final` : une seule affectation ;
  - un champ d'instance `final` s'affecte à la déclaration, dans un bloc d'initialisation, ou dans **chaque** constructeur ;
  - un champ `static final` s'affecte à la déclaration ou dans un bloc `static`.
- **Mon image :** …

### 12. 🟫 Le tapis — les objets immuables, `this` et `super`

- **Image :** un **tapis brodé** qu'on ne peut plus changer (immuable) : la classe est **cousue `final`**, les fils sont **`private final`**, il n'y a **aucun ciseau** (pas de setter), et quand quelqu'un te prête un **tableau** à broder, tu en fais **une copie** avant (et tu rends une copie quand on te la demande). Sur le tapis, deux enfants jouent : **`this`** montre l'objet lui-même, **`super`** montre la version du parent.
- **À retenir :**
  - immuable : classe `final` (ou constructeur privé), champs `private final`, pas de setter, **copies défensives** des objets mutables reçus ou rendus ;
  - `this.x` = le champ quand un paramètre a le même nom ; `super.m()` appelle la version du parent ;
  - `this(…)` / `super(…)` (appels de constructeur) **contre** `this.` / `super.` (accès à un membre).
- **Mon image :** …

---

## ⚡ La balade éclair

1. Station 7 : quand Java n'ajoute-t-il **pas** le constructeur par défaut ?
2. Station 8 : une méthode redéfinie peut-elle devenir moins visible ?
3. Station 9 : peut-on écrire `private abstract void m();` ?
4. Station 10 : qui s'initialise en premier, l'instance du parent ou le `static` de l'enfant ?
5. Station 11 : où peut-on affecter un champ d'instance `final` ?
6. Station 12 : pourquoi faire une copie d'un tableau reçu dans le constructeur ?
