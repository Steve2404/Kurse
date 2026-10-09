# Chapitre 18 — Projets (tableau de bord)

> Le parcours complet (projets + drills + répétition, et **comment `Check` vérifie la conception**) est décrit dans `../PARCOURS.md`.

Chaque projet est une **application** dont le code a un problème de conception (souvent un legacy fourni dans `Data.java`). Dans son dossier : `TODO.md` (l'énoncé, avec une leçon par étape), `INDICES.md`, `Check.java`, et `solution/` (avec `CORRIGE.md`), à n'ouvrir qu'à la fin.

**Tout le code et tous les tests, c'est toi qui les écris.** `Check` vérifie aussi la **forme** du code : la longueur des méthodes, et ce qu'un fichier a le droit de connaître.

| ☐ | Projet | Notions | Ce qui est dur |
|---|---|---|---|
| ☐ | `p01_invoice` — la facture du garage | odeurs, caractérisation, maître étalon, refactorings d'IntelliJ, objet valeur, polymorphisme, responsabilité unique, façade | ne **rien** changer, même les bizarreries |
| ☐ | `p02_shipping` — les frais de port | ouvert/fermé, stratégie, registre, composition, lambdas, racine de composition | le piège des graines voisines de `Random` |
| ☐ | `p03_storage` — le stockage des notes | Liskov, interfaces étroites, test de contrat abstrait, vue, enveloppe, copie défensive | le contrat juste assez précis |
| ☐ | `p04_orders` — la boulangerie | inversion des dépendances, ports et adaptateurs, injection, `Clock`, Mockito, test d'intégration | trouver toutes les dépendances cachées |
| ☐ | `p05_messages` — les e-mails | fabrique statique, cache, builder, objet immuable, `toBuilder` | la copie défensive oubliée |
| ☐ | `p06_weather` — la météo | adaptateur, proxy cache, décorateurs, chaîne de secours, composite | l'ordre d'emboîtement |
| ☐ | `p07_editor` — l'éditeur de texte | commande, annuler/refaire, macro, memento, observateur | se désabonner pendant une notification |
| ☐ | `p08_workflow` — la vie d'une commande | machine à états, patron État, méthode modèle | tester les 30 cases du tableau |
| ☐ | `p09_cheese` — **capstone** la cave à fromages | un refactoring complet, puis une nouveauté avec un décorateur | choisir soi-même les gestes |
