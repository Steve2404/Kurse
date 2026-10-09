# Drill de rappel 1 — Refactorer une fonction sans changer son résultat

> Première fois ? Lis d'abord le mode d'emploi [`ch18_design/PARCOURS.md`](../../PARCOURS.md). À faire après le projet p01.

**Chrono cible :** 20 min, puis 10 min.

**Règles :**
- Tout se fait de mémoire, **imports compris**.
- `Data.legacyTicket` (fourni, ne pas modifier) calcule le prix d'une place de cinéma, d'un seul bloc. Crée **`public final class Recall01`** dans le paquet `ch18_design.drills.r01_refactor`, avec **`public static int ticket(int age, boolean student, String day, int hour)`**, qui rend **exactement** le même prix pour **toutes** les entrées.
- Contraintes de forme : au moins **4** constantes nommées (`static final int`), **aucune** méthode de plus de **5 lignes**, pas de `else if`, et ton code n'appelle pas `legacyTicket`.
- Tu n'écris pas de tests : les tests de référence comparent ton prix à celui du legacy sur les 33 936 entrées possibles.

**Les notions de ce drill ont été apprises dans :** projet 1 (étapes 1 à 6).

<details><summary><b>Comment faire ce drill, concrètement</b> (à lire la 1re fois)</summary>

1. Note l'heure. Lis `Data.legacyTicket` **une fois**, ferme-le, et écris les règles sur papier.
2. Crée `Recall01` et écris-le **directement** sous sa forme propre (pas de copie du legacy).
3. Lance `Check.java` (flèche verte). Un échec dit la première entrée fausse (`âge étudiant jour heure`).
4. Après seulement : la carte mémoire, puis note ton temps dans [`drills/README.md`](../README.md).

</details>

## Défis

- ☐ **D01.** Le même prix que le legacy, pour tous les âges de 0 à 100, étudiant ou non, les 7 jours, les 24 heures.
  → `d01 : 1 executions, 1 reussies`
- ☐ **D02.** Les mêmes refus : un âge négatif, une heure négative ou supérieure à 23 lancent `IllegalArgumentException("entree invalide")`.
  → `d02 : 3 executions, 3 reussies`

## Sortie attendue complète

```
d01 : 1 executions, 1 reussies
d02 : 3 executions, 3 reussies
```

<details><summary>Ouvrir la carte</summary>

**Les règles du legacy :** moins de 4 ans : **0** (rien d'autre ne s'applique). Sinon, la base : moins de 14 ans 6,00 ; 65 ans et plus 7,50 ; étudiant 8,00 ; sinon 11,00. Puis −1,00 le mercredi pour les moins de 14 ans, −2,00 avant midi, +1,50 le samedi et le dimanche.

**La forme :**

```java
public static int ticket(int age, boolean student, String day, int hour) {
    if (age < 0 || hour < 0 || hour > 23) throw new IllegalArgumentException("entree invalide");
    return age < 4 ? 0 : base(age, student) - reductions(age, day, hour) + surcharge(day);
}
```

- Une méthode par règle (`base`, `reductions`, `surcharge`), chacune en 2 ou 3 lignes, avec des `return` ou des ternaires au lieu de `else if`.
- Les nombres deviennent des constantes : `CHILD`, `SENIOR`, `STUDENT`, `FULL`, `MORNING`…
- Le piège : la gratuité des moins de 4 ans **court-circuite** tout ; une matinée à −2,00 sur un prix de 0 donnerait −200.
- Un domaine **petit** se teste **en entier** (des boucles imbriquées), sans hasard.

</details>
