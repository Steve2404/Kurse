# Prompt : produire ou étendre un chapitre d'exercices OCP Java (format projets)

> Ce fichier est le mode d'emploi pour un agent IA (ou un humain) qui doit créer, compléter ou corriger les exercices d'un chapitre de ce dépôt. Les 15 chapitres suivent déjà ce format : prends-en un comme modèle (le chapitre 15, `src/main/java/ch15_jdbc/`, est le plus récent).

---

## 0. Objectif

Ni résumer un chapitre, ni faire « remplir des trous ». Il faut :
- faire **construire de vraies applications**, pour comprendre ;
- faire **retrouver l'API de mémoire**, à intervalles espacés, pour retenir.

Chaque notion des objectifs officiels OCP 17 du chapitre doit apparaître dans au moins un projet et un drill.

## 1. Les préférences de l'apprenant (prioritaires sur tout le reste)

1. **Pas de quiz.** Uniquement du code, de plus en plus difficile. Un piège « ça compile ? que ça affiche ? » devient :
   - une ligne de sortie à produire ;
   - ou une **expérience** à faire à la main (hors sortie vérifiée).
2. **De vrais projets, conçus par l'apprenant.**
   - Il crée lui-même **tous** ses types : records, classes, interfaces, `main`.
   - L'agent ne crée **jamais** ses fichiers. Il écrit seulement `TODO.md`, `Data.java`, `Check.java` et `solution/`.
3. **Difficile et algorithmique**, avec une couverture **complète** de l'API du chapitre.
4. **Chaque ligne attendue est dérivable du `TODO.md`** : chaque appel, chaque argument, chaque valeur initiale et chaque format d'affichage y est écrit.
5. **Le crescendo :** le chapitre N n'utilise que les chapitres 1 à N. Les notions suivantes sont refusées par `Check`.
6. **Corrigés commentés :** chaque méthode de `solution/` porte un court commentaire « pourquoi / quel piège ».
7. **En français** : des phrases courtes, des listes plutôt que des paragraphes.

## 2. Structure d'un chapitre

```
src/main/java/chN_nom/
├── PARCOURS.md                 ← le mode d'emploi du chapitre
├── projects/
│   ├── README.md               ← la liste des projets, à cocher
│   └── pNN_sujet/
│       ├── TODO.md             ← l'énoncé
│       ├── Data.java           ← les données
│       ├── Check.java          ← le correcteur (généré)
│       └── solution/           ← paquet chN_nom.projects.pNN_sujet.solution
└── drills/
    ├── README.md               ← quand faire quel drill, tableau de suivi J0 → R5
    └── rNN_theme/
        ├── TODO.md             ← défis D01…, sortie attendue, carte mémoire
        ├── Check.java
        └── solution/RecallNN.java
```

**Le nommage :**
- `chN_` est le numéro du chapitre du livre : un paquet Java ne peut pas commencer par un chiffre ;
- ne jamais nommer un type de l'apprenant `Check` ou `Data`.

**Le correcteur commun :** `src/main/java/projectkit/ProjectChecker.java`.

**Les exceptions :**
- **chapitre 12** : les modules sont hors Maven, dans `ch12_modules/<item>/` (un `module-info.java` par module casserait la compilation Maven). L'apprenant écrit un `build.sh`, que `ProjectChecker.checkModules` exécute ;
- **chapitre 15** : Docker facultatif dans `ch15_jdbc-lab/`.

## 3. Le `PARCOURS.md` d'un chapitre

Il contient, dans cet ordre :
1. ce que tu vas faire (projets / drills) ;
2. la règle du crescendo (ce qui est permis, ce qui est exclu et ce qu'on fait à la place) ;
3. les règles propres au chapitre, s'il y en a (sortie déterministe, bac à sable…) ;
4. par où commencer, et l'ordre complet `p01 → r01…` ;
5. la disposition des dossiers ;
6. comment faire un projet ;
7. comment faire un drill ;
8. comment savoir que le chapitre est acquis.

## 4. Le `TODO.md` d'un projet

1. **L'en-tête :**
   - les notions visées, en listes ;
   - les algorithmes ;
   - **« Ce que TU crées »** : les noms des types et du `main` ;
   - la règle du crescendo.
2. **Le tableau de bord**, en étapes ☐. Chaque étape contient :
   - un extrait de sa sortie ;
   - les types et les signatures ;
   - les **appels exacts**, les données et les formats ;
   - des **questions**.
   
   Puis viennent les **expériences**, hors sortie attendue.
3. **La checklist** : les éléments que `Check` cherche dans le code.
4. **La sortie attendue complète** : injectée automatiquement depuis la solution.

## 5. Le `TODO.md` d'un drill

- **L'en-tête :**
  - un chrono cible (10 à 15 min, puis la moitié) ;
  - des règles : tout de mémoire, imports compris.
- **Les défis D01…D10**, chacun avec sa consigne exacte et sa ligne attendue (`→ D03 : …`).
- **La fin du fichier :**
  - les expériences ;
  - la sortie attendue complète ;
  - une **carte mémoire** dans `<details>`, à lire seulement après.
- **La répétition espacée :** J0, J+1, J+3, J+7, J+14, J+30, notée dans `drills/README.md`.

## 6. Le correcteur `Check.java`

- Il lance le `main` de l'apprenant (ou celui de `solution/` avec l'argument `solution`), puis compare la sortie **ligne par ligne**.
- Il cherche ensuite une liste d'éléments d'API dans le code de l'apprenant :

  | Forme | Sens |
  |---|---|
  | `"x"` | obligatoire |
  | `"!x"` | **interdit** (le crescendo), cherché hors des chaînes de caractères |
  | `"3xfoo("` | au moins 3 fois |
  | `"re:REGEX##libellé"` | une expression régulière (libellés en ASCII) |

- `Check.java`, les listes d'API et la sortie attendue du `TODO.md` sont **générés** depuis la solution par script. Ne jamais les éditer à la main.

## 7. Règle n°1 : tout vérifier en direct

1. **Rien sans exécution.** Ne jamais écrire un comportement, un message ou un nombre sans l'avoir **exécuté**.
2. **Simuler l'apprenant :**
   - copier chaque `solution/*.java` dans le paquet du projet, en retirant `.solution` du paquet ;
   - compiler **tout le dépôt** avec `javac --release 17` ;
   - lancer chaque `Check` avec et sans `solution`, plusieurs fois ;
   - puis supprimer les copies.
3. **Auditer la dérivabilité :**
   - chaque ligne citée dans un `TODO.md` doit figurer dans la sortie attendue ;
   - chaque appel de la solution doit être décrit dans le `TODO.md`.
4. **Auditer la couverture** en fin de chapitre : confronter aux objectifs officiels, puis ajouter ce qui manque (défis ou drill bonus).

## 8. Des sorties déterministes

- **Les locales :**
  - la machine de référence est en `de_DE` : toujours une `Locale` explicite, ou `Locale.setDefault` ;
  - jamais `%f` sans locale ;
  - les espaces insécables sont rendus visibles (`_`).
- **La concurrence :**
  - seul `main` affiche ;
  - les résultats sont fusionnés dans l'ordre, ou triés ;
  - un état s'observe par attente active ;
  - jamais d'interblocage réellement exécuté ;
  - `Check` est lancé au moins 3 fois.
- **Les fichiers :**
  - un bac à sable `build/chN/<item>`, recréé à chaque lancement ;
  - des chemins relatifs, affichés avec `/` ;
  - des parcours triés et des dates fixées ;
  - jamais `System.console()` dans une sortie vérifiée.
- **Les bases de données :**
  - H2 en mémoire et `ORDER BY` partout ;
  - les erreurs s'affichent par `getSQLState()`, jamais par `getMessage()` (le message est traduit) ;
  - les procédures H2 s'enregistrent par `CREATE ALIAS` avec `Classe.class.getName()`.
- **Toujours interdits :** `now()`, `System.exit`, `printStackTrace`, et le `toString` d'une exception personnalisée.

## 9. Les pièges d'environnement déjà rencontrés

- **`pom.xml`** doit fixer `maven.compiler.release` à 17 et l'encodage à UTF-8.
- **Windows :**
  - les scripts tournent avec Git Bash, et `.gitattributes` force les fins de ligne LF des `.sh` ;
  - la langue de `jar` est forcée en anglais (`-J-Duser.language=en`) ;
  - les sorties de `--describe-module` sont triées.
- **Le nettoyage :** ne jamais supprimer avec un motif générique. Toujours lister les chemins exacts.

## 10. Le workflow Git

- **Les commits :**
  - un commit par chapitre converti, puis un commit d'audit ;
  - le message est en anglais et résume ;
  - il se termine par la ligne `Co-Authored-By` en usage dans le dépôt.
- **Ce qu'on stage :** seulement le chapitre (`git add src/main/java/chN_nom`), jamais `.idea/`, jamais un fichier que l'apprenant est en train d'écrire.
- **Interdits :** pas de push forcé, pas de `--no-verify`.
- **Après les changements :** mettre à jour le tableau de bord Notion (Java › Chapitres OCP › Exercices) : la page du chapitre et la ligne du tableau global.
