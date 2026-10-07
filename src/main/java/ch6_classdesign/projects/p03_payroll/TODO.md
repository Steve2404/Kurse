# Projet 3 — La paie et l'organigramme

> Première fois ? Lis d'abord le mode d'emploi [`ch6_classdesign/PARCOURS.md`](../../PARCOURS.md).
>
> **Bloqué sur une étape ?** [`INDICES.md`](INDICES.md) donne deux indices repliés par étape, sans code. **Étape finie ?** [`solution/CORRIGE.md`](solution/CORRIGE.md) donne, étape par étape, le code de l'étape, les **réponses aux questions** et le résultat exact des **expériences**. N'ouvre que la section de l'étape que tu viens de faire.

**Notions visées (chapitre 6) :** ce qu'on **hérite**, ce qu'on **redéfinit**, ce qu'on **masque** :
- **redéfinir** une méthode d'instance (choix à l'**exécution**, d'après l'objet) en réutilisant `super.pay()` ;
- **masquer** (*hide*) une méthode `static` (choix à la **compilation**, d'après le type de la référence) ;
- **masquer un champ** : deux champs `type` dans le même objet, et `super.type` ;
- **redéclarer** une méthode `private` : c'est une **autre** méthode, sans liaison dynamique ;
- une méthode **`final`** ;
- une méthode **abstraite** ;
- un calcul dans l'appel à `super(...)`.

Côté algorithmes :
- un **organigramme** (arbre stocké par « id du manager ») ;
- parcours en profondeur avec indentation ;
- coût d'un **sous-arbre** (récursif) ;
- **plus proche ancêtre commun** ;
- plus longue chaîne hiérarchique.

**Ce qui est donné :** `Data.java` (le personnel) et `Check.java`.

**Ce que TU crées :** dans le paquet `ch6_classdesign.projects.p03_payroll` :
- `Employee` (abstraite), `Manager`, `Engineer`, `Intern` ;
- **`Payroll`** (le `main`).

**Règle du crescendo :** chapitres 1 à 6. **Pas de cast d'objet** `(Manager) e` (chapitre 7) : utilise `if (e instanceof Manager m)` (chapitre 3). Les montants sont en centimes (`long`).

---

## Tableau de bord

### ☐ Étape 1 — La hiérarchie

- **`Employee`** (`abstract`) :
  - un champ package-private `String type = "employe";` ;
  - des champs `private final` (`id`, `name`), `protected final long base`, et un `managerId` modifiable ;
  - un constructeur `protected Employee(int id, String name, long base)` ;
  - `static String category()`, qui rend `"employe"` ;
  - `public long pay()`, qui rend `base` ;
  - `public abstract String role()` ;
  - **`public final String badge()`**, qui rend `#id nom` ;
  - **`private int rate()`**, qui rend 3, et `public long raise()`, qui rend `base * rate() / 100` ;
  - une méthode `static String money(long cents)`, qui écrit `4500.00`.
- **`Manager`** :
  - `BONUS_PER_REPORT = 15000` ;
  - son propre champ `String type = "manager";` (il masque celui du parent) ;
  - `static String category()`, qui rend `"encadrement"` (elle masque celle du parent) ;
  - `setReports(int)` ;
  - `pay()`, qui rend `super.pay() + BONUS_PER_REPORT * reports` ;
  - `role()`, qui rend `manager de N` ;
  - sa **propre** `private int rate()`, qui rend 10 ;
  - `typeSeenFromInside()`, qui rend `type + "/" + super.type`.
- **`Engineer`** : `HOURLY = 2500` et les heures supplémentaires. `pay()` = `super.pay() + overtime * HOURLY`. `role()` = `ingenieur +Nh`.
- **`Intern`** : `CAP = 100000`. Son constructeur appelle `super(id, name, Math.min(stipend, CAP))`. Il ne redéfinit **pas** `pay()`. `role()` = `stagiaire`.

### ☐ Étape 2 — L'organigramme

```
#1 Alice [manager de 2] 6300.00
  #2 Bruno [manager de 3] 4950.00
    #4 David [ingenieur +10h] 4050.00
...
masse salariale 38825.00 ; equipe Bruno 13900.00 ; equipe Chloe 18625.00
```
- Lis `Data.STAFF` dans un tableau `byId` (case 0 inutilisée). Crée le bon type selon `M`, `E` ou `I`.
- **Le nombre de subordonnés directs** de chaque manager se calcule **après** la lecture, avec `instanceof Manager m`, puis `m.setReports(…)`.
- `static int[] children(int id)` : les subordonnés directs, par id croissant.
- `static void print(int id, int level)` : deux espaces par niveau, puis `badge() [role()] money(pay())`, puis la récursion sur chaque enfant.
- `static long cost(int id)` : la paie de la personne, plus le coût de chaque sous-arbre enfant.
  - Affiche `cost(1)` (masse salariale), `cost(2)` (équipe Bruno) et `cost(3)` (équipe Chloe).

### ☐ Étape 3 — Ancêtre commun et plus longue chaîne

```
manager commun : David+Ines=Alice Ines+Jules=Hugo Gina+Hugo=Chloe Emma+Farid=Bruno
plus longue chaine : Alice > Chloe > Hugo > Ines (4 niveaux)
```
- `static int depth(int id)` (récursif) et `static int lca(int a, int b)` :
  1. fais monter le plus profond jusqu'au même niveau ;
  2. puis fais monter les deux ensemble jusqu'à ce qu'ils se rejoignent.
  - Les paires viennent de `Data.PAIRS`, et `static int find(String name)` retrouve un id.
- **La plus longue chaîne** : la personne la plus profonde (la première trouvée en cas d'égalité). Remonte jusqu'au sommet en construisant `A > B > …`, avec `insert(0, …)`.

### ☐ Étape 4 — Masquer ou redéfinir, en direct

```
champ masque : manager / employe / manager/employe
static masquee : encadrement / employe ; redefinie : 4500.00 = 4500.00
prive redeclare : hausse de Hugo 126.00 (3 %, pas 10 %) ; badge final #8 Hugo
```
- Avec `if (byId[8] instanceof Manager hugo)`, puis `Employee asEmployee = hugo;` :
  - **champs** : `hugo.type`, `asEmployee.type`, `hugo.typeSeenFromInside()` ;
  - **static** : `Manager.category()` et `Employee.category()` ;
  - **redéfini** : `money(asEmployee.pay())` et `money(hugo.pay())`. Même résultat : c'est l'objet qui décide ;
  - **privé** : `money(hugo.raise())` utilise le `rate()` d'`Employee` (3 %) ;
  - **final** : `asEmployee.badge()`.
- **Expériences :**
  - mets `@Override` sur `rate()` dans `Manager` ;
  - essaie de redéfinir `badge()` ;
  - rends `category()` non `static` dans `Manager` seulement ;
  - redéfinis `pay()` avec un accès `protected`.

---

## Checklist (vérifiée par `Check`)

- `Data.STAFF` et `Data.PAIRS` ;
- `abstract class Employee`, 3 `extends Employee`, `public abstract String role()` ;
- `super.pay()` et 2 `static String category()` ;
- 2 `String type = ` et `super.type` ;
- `public final String badge()` et 2 `private int rate()` ;
- `instanceof Manager` et `@Override`.

---

## Sortie attendue complète

```
#1 Alice [manager de 2] 6300.00
  #2 Bruno [manager de 3] 4950.00
    #4 David [ingenieur +10h] 4050.00
    #5 Emma [ingenieur +0h] 4000.00
    #6 Farid [stagiaire] 900.00
  #3 Chloe [manager de 2] 5000.00
    #7 Gina [ingenieur +5h] 4025.00
    #8 Hugo [manager de 2] 4500.00
      #9 Ines [ingenieur +20h] 4100.00
      #10 Jules [stagiaire] 1000.00
masse salariale 38825.00 ; equipe Bruno 13900.00 ; equipe Chloe 18625.00
manager commun : David+Ines=Alice Ines+Jules=Hugo Gina+Hugo=Chloe Emma+Farid=Bruno
plus longue chaine : Alice > Chloe > Hugo > Ines (4 niveaux)
champ masque : manager / employe / manager/employe
static masquee : encadrement / employe ; redefinie : 4500.00 = 4500.00
prive redeclare : hausse de Hugo 126.00 (3 %, pas 10 %) ; badge final #8 Hugo
```
