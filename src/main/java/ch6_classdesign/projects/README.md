# Chapitre 6 — Projets (tableau de bord)

> Le parcours complet (projets + drills + répétition, et la règle du crescendo) est décrit dans `../PARCOURS.md`.

Chaque projet est une **application à construire de A à Z**. Dans son dossier, tu ne trouves que :
- `TODO.md` : l'énoncé ;
- `Data.java` : les données, s'il y en a ;
- `Check.java` : le correcteur ;
- `solution/` : à n'ouvrir qu'à la fin.

**Toutes les classes de la hiérarchie, c'est toi qui les crées.**

| ☐ | Projet | Notions | Classe `main` | Ce qui est dur |
|---|---|---|---|---|
| ☐ | `p01_shapes` — formes géométriques | `extends`, classe abstraite, `super(...)`, `this(...)`, constructeur `protected`, méthode modèle `final`, `super.extra()` | `ShapesApp` | enveloppe convexe d'Andrew, aire du lacet, point dans un polygone |
| ☐ | `p02_tracer` — traceur d'initialisation | ordre classe puis objet, parent puis enfant, `this()`/`super()`, piège de la méthode redéfinie dans le constructeur parent, constante de compilation | `TracerApp` | prédire 37 lignes de trace |
| ☐ | `p03_payroll` — paie et organigramme | redéfinir et `super.pay()`, masquer `static` et champs, `private` redéclaré, `final` | `Payroll` | arbre hiérarchique, coût d'un sous-arbre, ancêtre commun |
| ☐ | `p04_immutable` — objets immuables | 5 règles, copies défensives, `equals`/`hashCode`, constructeur `private` et fabrique | `ImmutableLab` | répartition sans perte, fractions, puissance de matrice, déterminant exact |
| ☐ | `p05_arena` — arène de combat | abstraite, constructeur de copie, retour covariant, `super.act()`, `final` | `Arena` | simulation au tour par tour, initiative stable, ciblage |
| ☐ | `p06_algebra` — calcul formel | 3 niveaux abstraits, `toString` `final`, classe avec état (`Parser`) | `Algebra` | analyseur récursif, dérivée symbolique, simplification, Newton |
| ☐ | `p07_media` — **capstone** médiathèque | tout le chapitre | `MediaApp` | doublons par `equals`, Jaccard, clé ISBN, playlist par sac à dos |
