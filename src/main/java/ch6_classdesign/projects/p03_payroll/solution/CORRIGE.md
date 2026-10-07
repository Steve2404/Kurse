# Projet 3 — Corrigé étape par étape

> **Quand le lire :** ouvre **seulement la section de l'étape** que tu viens de terminer, pour comparer ton code et tes réponses. Le programme complet est dans ce dossier : `Employee`, `Manager`, `Engineer`, `Intern` et `Payroll`.
>
> Les messages ci-dessous ont été obtenus en direct avec **JDK 17** (`javac` 17.0.18), sur des classes de test réduites (`E` et `Mg`).

---

## Étape 1 — La hiérarchie

**Le code :** [`Employee.java`](Employee.java), [`Manager.java`](Manager.java), [`Engineer.java`](Engineer.java) et [`Intern.java`](Intern.java).

**`super.pay()`** réutilise le calcul du parent au lieu de recopier `base`. Si la règle de base change, une seule classe est à modifier.

**`Intern` sans `pay()`** : la version héritée d'`Employee` suffit. Le plafond est appliqué **une fois pour toutes** dans le constructeur, avec `Math.min(stipend, CAP)` dans l'appel à `super`.

**`protected final long base`** : les sous-classes peuvent le **lire**, mais personne ne peut le modifier après la construction.

---

## Étape 2 — L'organigramme

**Le code :** le `main` de [`Payroll.java`](Payroll.java) jusqu'à la masse salariale, et les méthodes `children`, `print` et `cost`.

**Pourquoi calculer les `reports` après la lecture ?** Le nombre de subordonnés d'Alice dépend de lignes **plus bas** dans `STAFF`. Il faut donc avoir lu tout le monde d'abord.

**`instanceof Manager m`** : seul un `Manager` a `setReports`. Le pattern matching donne directement une variable `m` de type `Manager` (le cast arrive au chapitre 7).

**La récursion sur un arbre :**
- `print` fait un parcours **en profondeur** : la personne, puis tout le sous-arbre de son 1er subordonné, puis celui du 2e…
- `cost` additionne de la même façon. `cost(1)` est la masse salariale complète, car Alice est à la racine.

**La paie d'Alice :** elle a 2 subordonnés directs (Bruno et Chloé). 6000.00 + 2 × 150.00 = **6300.00**.

---

## Étape 3 — Ancêtre commun et plus longue chaîne

**Le code :** les méthodes `depth`, `lca` et `find`, et la fin du `main` jusqu'à la chaîne.

**`lca(David, Ines)` à la main :**
- David (4) a pour manager Bruno, puis Alice : profondeur 2. Ines (9) a pour manager Hugo, puis Chloé, puis Alice : profondeur 3.
- On fait monter Ines d'un cran : Hugo (profondeur 2).
- On monte les deux ensemble : David devient Bruno et Hugo devient Chloé, puis Bruno et Chloé deviennent tous deux Alice. **Alice**.

**`insert(0, …)`** : on remonte d'Ines vers Alice, mais on veut afficher d'Alice vers Ines. Insérer chaque nom **devant** inverse l'ordre sans tableau.

---

## Étape 4 — Masquer ou redéfinir, en direct

**Le code :** le bloc `if (byId[8] instanceof Manager hugo)` à la fin du `main`.

**Le tableau à retenir :**

| Membre | Mécanisme | Choisi selon | Ligne |
|---|---|---|---|
| champ `type` | **masqué** (*hidden*) : deux champs coexistent dans l'objet | le type de la **référence** | `hugo.type` = manager, `asEmployee.type` = employe |
| `static category()` | **masquée** | le type utilisé dans l'appel (`Manager.` ou `Employee.`) | encadrement / employe |
| `pay()` | **redéfinie** (*overridden*) | l'**objet** réel | 4500.00 des deux côtés |
| `private rate()` | ni l'un ni l'autre : deux méthodes **sans lien** | la classe où le code est écrit | `raise()`, écrit dans `Employee`, appelle le `rate()` d'`Employee` : 3 % |
| `final badge()` | **non redéfinissable** | un seul code pour tous | `#8 Hugo` |

**`typeSeenFromInside()`** rend `type + "/" + super.type`. Depuis `Manager`, `type` désigne son propre champ et `super.type` celui du parent, d'où `manager/employe`.

**La hausse de Hugo :** 4200.00 × 3 / 100 = **126.00**, et non 420.00. Le `rate()` privé de `Manager` est invisible pour `Employee.raise()`.

**Expériences :**

| Expérience | Erreur de `javac` |
|---|---|
| `@Override` sur `rate()` dans `Manager` | `method does not override or implement a method from a supertype` : une méthode `private` n'est pas héritée, il n'y a donc rien à redéfinir |
| redéfinir `badge()` | `badge() in Mg cannot override badge() in E` (`overridden method is final`) |
| `category()` non `static` dans `Manager` seulement | `category() in Mg cannot override category() in E` (`overridden method is static`). Une méthode d'instance ne peut pas « remplacer » une méthode static, ni l'inverse |
| `pay()` redéfinie en `protected` | `pay() in Mg cannot override pay() in E` (`attempting to assign weaker access privileges; was public`) |

**Les règles d'une redéfinition valide :**
- même signature ;
- type de retour identique ou plus précis (**covariant**) ;
- accès **égal ou plus large** ;
- pas de nouvelle exception vérifiée plus large (chapitre 11) ;
- les deux méthodes sont d'instance, ou les deux sont static (et alors on parle de masquage).
