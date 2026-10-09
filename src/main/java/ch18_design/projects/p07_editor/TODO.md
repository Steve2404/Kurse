# Projet 7 — L'éditeur de texte (commande, annuler/refaire, observateur, memento)

> Première fois ? Lis d'abord le mode d'emploi [`ch18_design/PARCOURS.md`](../../PARCOURS.md).
>
> **Bloqué sur une étape ?** [`INDICES.md`](INDICES.md) donne deux indices repliés par étape, sans code. **Étape finie ?** [`solution/CORRIGE.md`](solution/CORRIGE.md) donne, étape par étape, le code de l'étape, les **réponses aux questions** et le résultat exact des **expériences**. N'ouvre que la section de l'étape que tu viens de faire.

**Notions visées (chapitre 18) :**
- les patrons de **comportement** : comment des objets se répartissent le travail ;
- la **commande** (*Command*) : une action devenue un objet, qu'on garde, annule, refait, regroupe ;
- **annuler et refaire** avec deux piles, et une capacité maximale ;
- la **commande composite** (une macro) ;
- le **memento** : une photographie **opaque** de l'état, pour revenir en arrière quand l'opération inverse est difficile ;
- l'**observateur** (*Observer*) : des abonnés prévenus de chaque changement, que le sujet ne connaît pas ;
- deux pièges : se désabonner **pendant** une notification, et publier `this` dans un constructeur.

**Ce qui est FOURNI :** `Data.java` contient `LegacyEditor`, l'ancien éditeur : un gros `switch` sur le nom de l'action, une annulation qui garde une **copie complète** du texte après chaque action, pas de « refaire », et un compteur de mots que l'éditeur met à jour lui-même… sauf quand il oublie. Tu ne modifies pas ce fichier.

**Ce que TU crées :** dans `ch18_design.projects.p07_editor` : `DocumentEvent`, `DocumentListener`, `Document` (avec sa classe imbriquée `Document.Snapshot`), `Command`, `InsertCommand`, `DeleteCommand`, `ReplaceAllCommand`, `MacroCommand`, `History`, `WordCounter`, `ChangeLog`, et tes tests (par exemple `EditorTest`).

**Règle du crescendo :** chapitres 1 à 17, JUnit et Mockito. Pas de `System.out` ni de `Thread.sleep` dans tes tests. Dans ton code : ni `switch`, ni `instanceof`, et aucune méthode de plus de **10 lignes**.

---

## Tableau de bord

### ☐ Étape 1 — Ce que coûte le legacy

**👉 À toi :** lance `Data` et lis `LegacyEditor`.

**❓ Questions :**
- Que montrent les trois premières lignes de la sortie ? Quel oubli du legacy explique la troisième ?
- Combien de caractères le legacy garde-t-il pour pouvoir annuler 10 000 frappes ? D'où vient ce nombre (calcule-le) ? Combien en faudrait-il si l'on gardait seulement **ce qui a changé** à chaque frappe ?
- Pour ajouter une action « mettre en majuscules », que faut-il modifier dans le legacy ?

### ☐ Étape 2 — Le document et ses observateurs

**📖 La leçon : l'observateur.** Dans le legacy, l'éditeur met à jour **lui-même** le compteur de mots : il doit connaître chaque élément de l'écran, et il en oublie (l'annulation). Le patron **observateur** inverse la relation : le **sujet** (le document) garde une liste d'**abonnés** qui implémentent une petite interface, et les **prévient** après chaque changement. Il ne sait pas qui ils sont : un compteur de mots, un journal, l'écran, une sauvegarde automatique… On en ajoute sans toucher au document.

Deux pièges classiques :
- un abonné qui se **désabonne pendant** qu'on le prévient modifie la liste qu'on est en train de parcourir : `ConcurrentModificationException`. On prévient donc sur une **copie** de la liste ;
- un objet qui s'abonne **dans son propre constructeur** (`document.addListener(this)`) se publie avant d'être fini de construire. On passe par une **fabrique statique** qui construit, **puis** abonne.

**Exemple sur un autre sujet :** une chaîne YouTube ne connaît pas ses abonnés un par un ; elle publie une vidéo, et chaque abonné reçoit la notification et fait ce qu'il veut (regarder, ignorer).

**👉 À toi :**
- `public record DocumentEvent(String kind, int position, String text)` : `kind` vaut `"insert"`, `"delete"` ou `"restore"` ; `text` est le texte inséré, **retiré**, ou remis ;
- `@FunctionalInterface public interface DocumentListener` avec `void changed(DocumentEvent event)` ;
- `public final class Document` (le texte dans un `StringBuilder`) :
  - `String text()` ;
  - `void insert(int position, String text)` : une position de 0 à la longueur **comprise** (on peut insérer à la fin), sinon `IllegalArgumentException("position invalide : " + position)` ;
  - `String delete(int position, int length)` : même contrôle de la position, puis `"longueur invalide : " + length` si la longueur est négative ou dépasse la fin ; **rend** le texte retiré ;
  - `addListener` et `removeListener` ; chaque `insert` et chaque `delete` prévient **tous** les abonnés, sur une copie de la liste ;
- `public final class ChangeLog implements DocumentListener` : note chaque événement sous la forme `insert 0 'Bonjour'` ; `List<String> entries()` rend une copie ;
- `public final class WordCounter implements DocumentListener` : constructeur **privé** ; `public static WordCounter attachTo(Document document)` crée le compteur, l'abonne et compte une première fois ; `int count()` ; les mots sont séparés par des blancs, quel que soit leur nombre (`"\\s+"`), et un texte vide ou blanc en a 0.

**🧪 Les tests :** les opérations et leurs refus ; un abonné qui reçoit tous les événements ; un abonné désabonné qui n'entend plus rien ; un abonné qui **se désabonne lui-même** pendant la notification (une classe anonyme qui appelle `doc.removeListener(this)`), avec un second abonné qui doit **quand même** être prévenu ; le comptage des mots (`""`, `"   "`, `"un"`, `"  deux   mots "`, un texte avec une tabulation).

**🧪 Expérience :** dans `fire`, parcours directement `listeners` au lieu d'une copie, et relance le test du désabonnement. Quelle exception ? Remets la copie.

### ☐ Étape 3 — Les commandes et l'historique

**📖 La leçon : la commande.** Le legacy reçoit des actions sous forme de **texte** (`"insert"`, `"delete"`) et les traite dans un `switch`. Le patron **commande** fait de chaque action un **objet** : il sait s'exécuter (`execute`) **et se défaire** (`undo`). Comme ce sont des objets, on peut les **ranger dans une pile** : annuler, c'est dépiler la dernière commande et appeler son `undo` ; refaire, c'est la ré-exécuter. Chaque commande ne garde que **ce qui a changé** (le texte inséré, le texte retiré), pas une copie de tout le document.

**Exemple sur un autre sujet :** dans un jeu d'échecs, chaque coup est un objet `Coup(pièce, départ, arrivée, pièce prise)` : on peut revenir en arrière coup par coup, rejouer une partie, ou l'envoyer à l'adversaire.

**👉 À toi :**
- `public interface Command` : `void execute()`, `void undo()`, `String label()` ;
- `public final class InsertCommand implements Command`, construite avec `(Document document, int position, String text)` : annuler, c'est supprimer ce qu'on a inséré ; libellé `inserer 'abc'` ;
- `public final class DeleteCommand implements Command`, construite avec `(Document document, int position, int length)` : elle garde le texte retiré **à l'exécution** pour le remettre ; libellé `supprimer 3` ;
- `public final class History`, construite avec une capacité (`< 1` : `"capacite invalide : " + capacity`), deux piles (`Deque<Command>`, `ArrayDeque`) :
  - `void run(Command command)` : exécute, empile pour annuler, oublie la **plus ancienne** au-delà de la capacité, et **vide** la pile « refaire » (une nouvelle action efface l'avenir) ;
  - `boolean undo()` et `boolean redo()` : `false` s'il n'y a rien à annuler ou à refaire ;
  - `List<String> undoLabels()` : les libellés de ce qu'on peut annuler, **la plus récente d'abord**.

**🧪 Les tests :** deux commandes, deux annulations, deux rétablissements, et les `false` aux bouts ; une nouvelle action après une annulation rend « refaire » impossible ; une capacité de 2 avec trois commandes.

**🧪 Expérience :** dans une petite classe à part, fais 10 000 `InsertCommand` d'un caractère dans un `History(10_000)`. Combien de caractères l'historique garde-t-il ? Compare avec le legacy.

**❓ Question :** `History` ne nomme aucune commande concrète. Que faut-il modifier dans `History` pour ajouter une commande « mettre en majuscules » ?

### ☐ Étape 4 — Le memento et la macro

**📖 La leçon : le memento.** Pour certaines actions, l'opération inverse est difficile, voire impossible : « remplacer partout `pain` par `pain bio` », puis annuler en remplaçant `pain bio` par `pain`… casserait un `pain bio` qui existait **avant**. Le patron **memento** prend une **photographie** de l'état avant l'action, et la remet pour annuler. La photographie est **opaque** : elle ne se lit et ne se fabrique que dans le document (une classe imbriquée avec un constructeur **privé** et aucun accesseur). Les autres ne peuvent que la **garder** et la **rendre**.

**👉 À toi :**
- dans `Document` : `public static final class Snapshot` (un champ privé, un constructeur **privé**, aucun accesseur), `Snapshot snapshot()` et `void restore(Snapshot snapshot)`, qui prévient les abonnés avec l'événement `"restore"` (position 0, le texte remis) ;
- `public final class ReplaceAllCommand implements Command`, construite avec `(Document document, String target, String replacement)` : `execute` prend une photographie, puis remplace le texte entier ; `undo` restaure la photographie ; libellé `remplacer 'pain' par 'pain bio'` ;
- `public final class MacroCommand implements Command`, construite avec `(String label, List<Command> steps)` : exécute les étapes dans l'ordre, les **défait dans l'ordre inverse**.

**🧪 Les tests :** un remplacement annulé et refait ; une macro dont les étapes **dépendent** l'une de l'autre (sur `"tarte"` : insérer `"La "` au début, supprimer ces 3 caractères, insérer `" aux pommes"` à la position 5), annulée d'un seul coup ; et le test complet des observateurs à travers l'historique (un `ChangeLog` et un `WordCounter` pendant des commandes, une annulation et une restauration).

**❓ Questions :**
- Pourquoi la macro doit-elle défaire ses étapes dans l'ordre **inverse** ? Que donnerait l'ordre normal sur ton exemple ?
- Le memento garde une copie **complète** du texte, comme le legacy. Alors pourquoi est-ce acceptable ici et pas dans le legacy ?

### ☐ Étape 5 — Les mutants

**👉 À toi :** lance `Check`. Les 16 mutants touchent le document (une limite, la copie des abonnés, un événement oublié), les commandes, l'historique (vider « refaire », la capacité, l'ordre des piles), la macro, le memento et le compteur de mots.

---

## Checklist (vérifiée par `Check`)

- **Ton code :** `record DocumentEvent(`, `interface DocumentListener`, `final class Document`, `static final class Snapshot`, `interface Command`, les quatre commandes `final class … implements Command` (`InsertCommand`, `DeleteCommand`, `ReplaceAllCommand`, `MacroCommand`), `final class History`, `final class WordCounter implements DocumentListener`, `final class ChangeLog implements DocumentListener`, `static WordCounter attachTo(`, `Deque<Command>` ; ni `switch`, ni `instanceof`, ni rien de `Legacy`.
- **La conception :** aucune méthode de plus de **10 lignes** ; `Document.java` contient `private Snapshot(String` et ne nomme ni `WordCounter` ni `ChangeLog` ; `History.java` ne nomme aucune commande concrète ; `ReplaceAllCommand.java` contient `snapshot()`.
- **Tes tests :** au moins **12** tests, `new History(`, `new MacroCommand(`, `new ReplaceAllCommand(`, `WordCounter.attachTo(`, `removeListener(`, `undoLabels()`, `@ParameterizedTest`, `assertThrows(` ; ni `System.out` ni `Thread.sleep`.
- **Les 16 mutants** sont tués.

---

## Ce que `Check` affiche quand tout est juste

```
=== Verification des tests de ch18_design.projects.p07_editor ===
[PASS] tes tests sur TON code : … tests, … reussis
[PASS] tes tests sur le code de REFERENCE : … tests, … reussis
[PASS] les tests de REFERENCE sur TON code : 14 tests, 14 reussis
   mutant 1 : tue (par …)
   …
[PASS] mutants : 16/16 tues
--- API de ton code ---
[PASS] API : tous les elements vises sont utilises
[PASS] conception : toutes les regles de structure sont respectees
--- API de tes tests ---
[PASS] API : tous les elements vises sont utilises

*** PROJET REUSSI : tes tests passent, attrapent tous les mutants, et ton code est juste. ***
```
