# Projet 6 — Corrigé étape par étape

> **Quand le lire :** ouvre **seulement la section de l'étape** que tu viens de terminer, pour comparer ton code et tes réponses. Le programme complet est dans ce dossier : `Person`, `Order`, `Sorter` et `SortingApp`.
>
> Les messages et les sorties ci-dessous ont été obtenus en direct avec **JDK 17** (`javac` / `java` 17.0.18), sur une copie de la solution.

---

## Étape 1 — `Person` et `Order`

**Le code :** [`Person.java`](Person.java) et [`Order.java`](Order.java).

**Question — pourquoi `Order` reste fonctionnelle avec `String toString();` ?** Une méthode **publique d'`Object`** redéclarée dans une interface (`toString`, `equals`, `hashCode`) ne compte **pas** comme méthode abstraite. Toute classe qui implémentera l'interface en héritera de toute façon par `Object`. Il reste une seule méthode abstraite, `compare`. C'est pour ça que `java.util.Comparator`, qui déclare `equals`, reste fonctionnel.

**Des ordres qu'on compose :**
- `by` et `byText` sont des **fabriques** : on donne une clé, et on obtient un ordre.
- `reversed` et `then` sont des **combinateurs** : on prend un ordre, et on obtient un autre ordre.

Tu viens de réécrire à la main ce que `Comparator.comparing(…).reversed().thenComparing(…)` fera au chapitre 9.

**`Integer.compare` plutôt que `a - b` :** si a = 2 000 000 000 et b = −2 000 000 000, alors `a - b` déborde et devient **négatif**. On conclurait à tort que a < b.

---

## Étape 2 — Le trieur

**Le code :** [`Sorter.java`](Sorter.java).

**Compter avec une lambda :** `counting(order)` rend un **nouvel** ordre, qui compare comme `order` mais incrémente le champ au passage. Les tris n'ont pas à savoir qu'on les mesure. C'est un **décorateur** fait d'une lambda.

**`comparisons` doit être un champ.** Une variable locale ne peut pas être modifiée depuis une lambda. Un champ est accessible par `this`, que la lambda capture.

**`top` par sélection partielle :** k passes de n comparaisons au plus, soit O(k × n). Trier tout le tableau coûterait O(n log n). Pour un petit k, la sélection est plus rapide.

---

## Étape 3 — Les tris

**Le code :** le début du `main` de [`SortingApp.java`](SortingApp.java).

**La stabilité, démontrée :**
1. On trie par **nom**.
2. On re-trie ce résultat par **ville**. Un tri **stable** conserve l'ordre précédent entre deux personnes de la **même** ville, et elles restent donc triées par nom.
3. Le résultat est identique à un tri direct par « ville puis nom ».

C'est la technique classique pour trier selon plusieurs critères avec un tri stable : on trie d'abord par le critère **le moins** important.

**Insertion contre fusion :** 24 comparaisons contre 25 ici. Sur 10 éléments, la différence ne se voit pas. L'insertion est en O(n²) dans le pire cas, et la fusion **toujours** en O(n log n). L'avantage de la fusion apparaît sur de grands tableaux.

---

## Étape 4 — Sélection, dichotomie, capture

**Le code :** la fin du `main`.

**La dichotomie avec une clé fonctionnelle :** `Sorter.search(…, Person::age)` cherche **par âge** dans un tableau de personnes. La clé est passée comme une fonction, et le même `search` chercherait par score avec `Person::score`.
- 29 donne 2 (Emma, la **première** de 29 ans).
- 30 donne −5 : il n'y a personne, et 30 s'insérerait en position 4.

**`closeTo30` capture `target`**, une variable locale effectively final. L'ordre « par proximité de 30 ans » dépend d'une valeur choisie au moment de **créer** la lambda.

**Expériences :**
- **`target++` après la lambda** (vérifié sur le même schéma au projet 2) :

  ```
  error: local variables referenced from a lambda expression must be final or effectively final
  ```

- **Une variable locale incrémentée dans `counting`** (vérifié) : la même erreur, en `Sorter.java`. Seul un champ (ou la case d'un tableau) peut servir de compteur.
- **La fusion avec `< 0` au lieu de `<= 0`** : la stabilité **casse**. À égalité, l'élément de **droite** passe d'abord, et l'ordre précédent s'inverse. Vérifié : la ligne devient `stabilite : par ville apres par nom Noah Lea Adam Zoe Theo Emma Lina Ines Hugo Bob = ville puis nom Adam Lea Noah Emma Theo Zoe Bob Hugo Ines Lina`. Dans chaque ville, les noms sont maintenant à l'envers.
