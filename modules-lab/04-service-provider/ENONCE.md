# Lab 04 - Un service complet : `provides`/`uses` et ServiceLoader (niveau : difficile)

Rappel express du decoupage en "boites magiques" : voir Lab01/ENONCE.md.

## Le probleme, explique comme a un tout petit enfant

Imagine une petite annonce a 4 roles :

1. **L'interface du service (SPI)** : une fiche de poste ("je cherche
   quelqu'un qui sait `send(String message)`"), ici `Notifier`, dans le
   module `notify.api`.
2. **Le fournisseur (provider)** : quelqu'un qui se presente et
   REMPLIT la fiche de poste - `EmailNotifier`, dans le module
   `notify.email`, qui declare `provides Notifier with EmailNotifier;`.
3. **Le localisateur de service (service locator)** : l'agence qui
   CHERCHE, PARMI TOUS les modules presents, ceux qui ont rempli cette
   fiche de poste precise - c'est litteralement la classe
   `java.util.ServiceLoader` du JDK, tu n'as rien a ecrire toi-meme
   pour cette partie.
4. **Le consommateur (consumer)** : celui qui a BESOIN du service, sans
   jamais connaitre A L'AVANCE quel fournisseur precis va repondre -
   ici `notify.app`, qui declare `uses Notifier;` et appelle
   `ServiceLoader.load(Notifier.class)`.

Le point cle : `notify.app` ne fait JAMAIS `requires notify.email` -
il ne connait meme pas son existence a la compilation ! Le lien se fait
UNIQUEMENT via `provides`/`uses`, resolu par la JVM au demarrage.

## A faire

1. Lance `./run.sh`. La compilation reussit (contrairement aux labs
   precedents !) - c'est a L'EXECUTION que ca casse. Lis bien le
   message : `ServiceConfigurationError: ... module notify.app does
   not declare \`uses\``.
2. Ajoute la ligne manquante dans
   `exercise/src/notify.app/module-info.java`.
3. Relance `./run.sh` : le `ServiceLoader` doit maintenant trouver
   `EmailNotifier` tout seul, sans que `notify.app` n'ait jamais
   ecrit son nom nulle part.

## Ce qu'on remarque

`provides`/`uses` est le SEUL des 2 directives de ce chapitre qui peut
casser a L'EXECUTION plutot qu'a la COMPILATION - `uses` n'est pas
verifie par le compilateur de la meme facon que `exports`/`requires`,
c'est le `ServiceLoader` lui-meme, au demarrage de la JVM, qui exige que
le module appelant ait bien annonce quel type de service il compte
consommer.

## Indices techniques (a lire seulement si bloque)

- Ligne manquante : `uses com.example.notify.api.Notifier;`
- `ServiceLoader.load(Notifier.class)` renvoie un `Iterable<Notifier>`
  paresseux : le parcourir avec un for-each declenche la RECHERCHE et
  l'INSTANCIATION de chaque fournisseur trouve sur le module-path.
- Le module fournisseur (`notify.email`) doit etre present sur le
  `--module-path` au moment de l'execution - meme sans `requires`
  direct de `notify.app` vers lui, c'est le module-path GLOBAL qui
  determine ce que le `ServiceLoader` peut trouver.
