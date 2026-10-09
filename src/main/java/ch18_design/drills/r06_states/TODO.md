# Drill de rappel 6 — Le patron État et la méthode modèle

> Première fois ? Lis d'abord le mode d'emploi [`ch18_design/PARCOURS.md`](../../PARCOURS.md). À faire après le projet p08.

**Chrono cible :** 25 min, puis 12 min.

**Règles :**
- Tout se fait de mémoire, **imports compris**, dans le paquet `ch18_design.drills.r06_states`.
- Crée les types ci-dessous, **exactement** avec ces noms et ces signatures. Ni `switch`, ni `instanceof` ; `Turnstile` ne contient ni `if (` ni `equals(`.
- Tu n'écris pas de tests : les tests de référence vérifient ton code.

**Les notions de ce drill ont été apprises dans :** projet 8 (étapes 2 à 4).

**Le tourniquet d'un métro** (une case vide est un refus) :

<table>
<tr><th>État \ action</th><th>piece</th><th>pousser</th><th>panne</th><th>reparer</th></tr>
<tr><td><b>verrouille</b></td><td>→ deverrouille (une pièce de plus)</td><td></td><td>→ en panne</td><td></td></tr>
<tr><td><b>deverrouille</b></td><td></td><td>→ verrouille (un passage de plus)</td><td>→ en panne</td><td></td></tr>
<tr><td><b>en panne</b></td><td></td><td></td><td></td><td>→ verrouille</td></tr>
</table>

Un refus lance `IllegalStateException(action + " refuse (" + état + ")")`, par exemple `pousser refuse (verrouille)`, et ne change rien.

<details><summary><b>Comment faire ce drill, concrètement</b> (à lire la 1re fois)</summary>

Comme le drill 1 : crée les fichiers dans l'ordre des défis, `// D03 : ✗` après 3 minutes bloqué, lance `Check.java`, puis la carte mémoire, puis note ton temps dans [`drills/README.md`](../README.md).

</details>

## Défis

- ☐ **D01.** `public interface GateState` (`String label()`, et les actions `GateState coin(Turnstile)`, `push`, `breakDown`, `repair`, avec des méthodes par défaut), les états `Locked`, `Unlocked`, `Broken` (`implements GateState`), et `public final class Turnstile` : `coin()`, `push()`, `breakDown()`, `repair()`, `String status()`. Le test parcourt les 12 cases du tableau.
  → `d01 : 12 executions, 12 reussies`
- ☐ **D02.** `int coins()` et `int passages()` comptent les pièces acceptées et les passages.
  → `d02 : 1 executions, 1 reussies`
- ☐ **D03.** `List<String> events()` : chaque changement, `"verrouille -> deverrouille"`, dans une copie non modifiable.
  → `d03 : 1 executions, 1 reussies`
- ☐ **D04.** `public abstract class Report` avec `public final String render(Turnstile gate)` (en-tête, une ligne par événement, pied), les étapes `protected abstract String header()` et `protected abstract String line(String event)`, et le crochet `protected String footer(Turnstile gate)` vide ; `PlainReport` (`Journal du tourniquet`, `- événement`, pied `1 piece(s), 1 passage(s)`) et `CsvReport` (`evenement`, l'événement seul, pas de pied), chaque partie finie par un saut de ligne.
  → `d04 : 1 executions, 1 reussies`

## Sortie attendue complète

```
d01 : 12 executions, 12 reussies
d02 : 1 executions, 1 reussies
d03 : 1 executions, 1 reussies
d04 : 1 executions, 1 reussies
```

<details><summary>Ouvrir la carte</summary>

- **État** : chaque état est un objet ; l'interface donne des méthodes `default` qui **refusent** (`throw gate.refused("piece");`), et chaque état n'écrit que **ses** cases. Ici, `breakDown` est accepté presque partout : sa méthode par défaut **accepte** (`return new Broken();`), et seul `Broken` la refuse.
- **Le contexte délègue** : `public void coin() { change(state.coin(this)); }` ; `change` note l'événement puis change l'état ; un refus lance l'exception **avant**.
- **Les portes de service** (visibles dans le paquet seulement) : `refused(action)`, `countCoin()`, `countPassage()`.
- **Méthode modèle** : le squelette dans une méthode `final` de la classe abstraite, les étapes `protected abstract`, un crochet `protected` déjà écrit (vide) que les sous-classes **peuvent** redéfinir.

</details>
