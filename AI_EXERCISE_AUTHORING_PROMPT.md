# Prompt : générateur de chapitre d'exercices OCP Java (style "Kurse")

Colle ce document en entier au début d'une conversation avec n'importe
quelle IA codante (avec accès à un terminal/shell et à un dépôt Git),
puis colle le texte "Summary" + "Exam Essentials" du chapitre du livre
que tu veux traiter. L'IA doit suivre CES règles exactement, sans les
réinterpréter.

## 0. Contexte du projet

Le dépôt est un ensemble d'exercices Java auto-rédigés qui suivent
l'ordre des chapitres d'un livre de préparation à la certification
**OCP Java** (style Selikoff/Boyarsky, "OCP Oracle Certified
Professional Java SE 17 Developer Study Guide"). Chaque chapitre du
livre devient **un package Java** sous `src/main/java/`, avec son
propre jeu d'exercices "à trous" et leurs corrigés.

Le but n'est pas de résumer le chapitre, mais de le faire
**pratiquer** : chaque règle citée dans le Summary/Exam Essentials
doit se retrouver incarnée dans au moins un exercice où l'utilisateur
écrit du vrai code Java, ou dans un quiz "ça compile ou pas ?" où il
doit prédire un comportement réel avant de le vérifier.

## 1. Convention de nommage des dossiers

Chaque package de chapitre est nommé `chN_nomduchapitre` (tout en
minuscules, mots collés sans séparateur sauf le underscore après le
numéro). Exemples déjà en place : `ch1_buildingblocks`,
`ch2_operators`, `ch3_makingdecisions`, `ch4_coreapis`, `ch5_methods`,
`ch6_classdesign`, `ch7_beyondclasses`, `ch8_lambdas`,
`ch9_collections`, `ch10_streams`, `ch11_exceptions`,
`ch13_concurrency`, `ch14_io`, `ch15_jdbc` (+ `ch12_modules-lab/` et
`ch15_jdbc-lab/` à la racine du dépôt, hors `src/`, pour des labs qui
ne sont pas de simples packages Java).

**Pourquoi ce préfixe `chN_` et pas juste un chiffre** : un nom de
package Java ne peut jamais commencer par un chiffre (identifiant
Java invalide). `ch` + le numéro est le style choisi par l'utilisateur
pour ce dépôt (alternatives rejetées : `chapterN_...` trop long,
`_N_...` moins lisible) — **si tu commences un tout nouveau dépôt
sans convention existante, demande à l'utilisateur son style préféré
avant de choisir** ; ne réutilise `chN_` que si c'est déjà la
convention du dépôt cible.

Avant de créer un nouveau chapitre, vérifie toujours (`ls
src/main/java/`) quels chapitres existent déjà et quel numéro
correspond au chapitre demandé, pour ne jamais écraser ou dupliquer
un package existant.

## 2. Structure exacte de chaque package de chapitre

```
src/main/java/chN_nom/
├── ExerciseChecker.java          (une copie PAR package, pas partagée)
├── exercises/
│   ├── Exercise01_Sujet.java
│   ├── Exercise02_AutreSujet.java
│   └── ...
└── solutions/
    ├── Solution01_Sujet.java     (absent si Exercise01 est un quiz)
    └── ...
```

### 2.1 `ExerciseChecker.java`

Copier-coller exactement ce fichier (en changeant juste le nom de
package) dans chaque nouveau chapitre :

```java
package chN_nom;

/**
 * Petit helper d'auto-verification pour les exercices, sans dependance
 * a un framework de test. Chaque exercice appelle check(...) dans son
 * main() et affiche PASS/FAIL avec le libelle donne.
 */
public final class ExerciseChecker {

    private static int total = 0;
    private static int passed = 0;

    private ExerciseChecker() {
    }

    public static void check(String label, boolean condition) {
        total++;
        if (condition) {
            passed++;
            System.out.println("[PASS] " + label);
        } else {
            System.out.println("[FAIL] " + label);
        }
    }

    public static void summary() {
        System.out.println("\n--- Resultat : " + passed + "/" + total + " tests passes ---\n");
        total = 0;
        passed = 0;
    }
}
```

### 2.2 Fichier `exercises/ExerciseNN_Nom.java` (exercice "comportemental")

Un exercice comportemental fait écrire de la VRAIE logique Java
(contrairement au quiz, voir 2.3). Structure obligatoire :

1. `package chN_nom.exercises;`
2. `import chN_nom.ExerciseChecker;` (+ imports Java standard requis)
3. Un Javadoc au-dessus de la classe, **en français**, avec CE format
   exact (ne pas s'en écarter) :
   - Ligne de titre : `EXERCICE N - <titre> (niveau : <difficulte>)`
     suivie d'une ligne de `=` de la même longueur que le titre.
   - Pour le tout premier exercice du chapitre : un paragraphe
     "-- Rappel du decoupage en "boites magiques" --" qui explique la
     métaphore centrale (voir 2.4). Pour tous les exercices suivants,
     un simple rappel d'une ligne : "Rappel express du decoupage en
     "boites magiques" : voir Exercise01_XXX.java."
   - Pour chaque TODO, un bloc séparé par une bannière `====` :
     ```
     ==================================================================
     TODO N : signature_de_la_methode(params)
     ==================================================================
     ```
     suivi de :
     - `-- Le probleme, explique comme a un tout petit enfant --`
       (obligatoire, jamais sauté) : une analogie concrète, imagée,
       compréhensible par un enfant, qui explique POURQUOI la règle
       Java existe, pas seulement CE QU'elle fait.
     - `-- Essayons a la main --` (souvent présent, surtout si des
       calculs/exemples numériques aident) : un exemple concret
       chiffré/textuel, calculé à la main, AVANT le code.
     - `-- Le plan --` : les étapes en langage clair, numérotées,
       PAS de code Java dans cette section.
     - Une note fermant le TODO : "-- Ce plan a-t-il besoin d'une
       boite magique separee ? --" avec la réponse (souvent "Non :
       ... tient en une ligne", parfois "Oui" si une sous-étape se
       raconte seule, revient plusieurs fois, ou cache sa propre
       petite recette).
   - Après tous les TODO : "Exemple a verifier :" avec les résultats
     attendus précis pour CHAQUE cas testé dans `main()`.
   - Tout en bas : "Indices techniques Java (a lire seulement si le
     plan a la main est clair mais que la traduction en code
     bloque) :" — de vrais indices de syntaxe Java (signatures de
     méthodes, pièges de syntaxe), à lire seulement en dernier
     recours.
4. Le corps de la classe : chaque méthode TODO a pour SEUL contenu
   `throw new UnsupportedOperationException("TODO N : implementer nomMethode()");`
   — jamais de logique partielle, jamais de valeur de retour
   placeholder (sauf l'exception documentée en 4.1 ci-dessous).
5. `public static void main(String[] args)` : construit les données
   de test, appelle les méthodes TODO, vérifie chaque résultat via
   `ExerciseChecker.check("description en francais", condition)`,
   termine par `ExerciseChecker.summary();`.

#### 4.1 Exception au patron `throw ...` : constructeurs non-canoniques

Un constructeur de record NON canonique (qui ne prend pas TOUS les
composants dans le même ordre que le constructeur canonique) DOIT
avoir un appel `this(...)` ou `super(...)` comme TOUTE PREMIÈRE
instruction — impossible d'y mettre un `throw` en première ligne. Dans
ce cas précis, mets un appel **volontairement faux/bidon** (qui
compile mais donne le mauvais résultat) en première ligne, PUIS le
`throw new UnsupportedOperationException(...)` juste après, et
explique ce choix dans le Javadoc du TODO (voir
`ch7_beyondclasses/exercises/Exercise08_RecordBasics.java`, TODO 2,
comme référence exacte).

### 2.3 Fichier `exercises/ExerciseNN_NomQuiz.java` (exercice "quiz")

Utilisé quand il n'y a pas UNE seule implémentation correcte, mais une
question de type "est-ce que ce code compile ?" ou "quel est le
résultat ?". Pas de `solutions/SolutionNN_...java` associé — le
corrigé vit dans le MÊME fichier.

Structure :
1. Javadoc de classe expliquant qu'il n'y a pas de `main()` à lancer,
   que chaque bloc doit être testé À LA MAIN (recopier/décommenter
   dans un fichier à part si besoin, notamment pour les quiz
   multi-fichiers sur les packages), et rappelant les règles
   générales testées par le quiz.
2. Une série de blocs, chacun :
   ```java
   // ------------------------------------------------------------------
   // Bloc A
   //
   // Histoire : <mise en situation imagee expliquant le piege
   // qu'illustre ce bloc precis, sans jamais donner la reponse>
   //
   // Reponse : (a completer : compile / ne compile pas + pourquoi)
   // ------------------------------------------------------------------
   // <code Java du bloc, ENTIEREMENT commente>
   ```
3. À la fin, un commentaire bloc `/* ... */` intitulé
   "Reponses officielles (ne regardez qu'apres avoir repondu
   vous-meme) :" qui donne, pour chaque bloc, le verdict et le
   message d'erreur RÉEL du compilateur (voir section 3 —
   **obligatoire de le vérifier, jamais de l'inventer**).

Un quiz multi-fichiers (ex. règles d'accès `private`/`package`/
`protected`/`public` à travers plusieurs paquets) représente les
fichiers séparés avec des commentaires `--- Fichier chemin/Nom.java ---`
à l'intérieur du même bloc.

### 2.4 La métaphore "boites magiques"

Toujours présente dans l'Exercise01 du chapitre, sous cette forme (à
adapter au minimum) :

> Une methode, c'est une boite magique : tu la nourris d'ingredients
> (parametres), et elle rend un resultat, sans que tu aies besoin de
> savoir comment elle travaille dedans. Pour CHAQUE etape d'un plan,
> demande-toi : est-ce qu'elle se raconte seule ? revient-elle
> plusieurs fois ? cache-t-elle sa propre petite recette ? Si oui a au
> moins une question, elle merite sa propre boite.

### 2.5 Fichier `solutions/SolutionNN_Nom.java`

- Même package/sous-package (`chN_nom.solutions`), mêmes signatures de
  méthodes que l'exercice (recopier exactement noms de classes
  imbriquées, types, etc.).
- Javadoc minimal : `Corrige de l'exercice N. A ne consulter qu'apres
  avoir essaye par vous-meme dans chN_nom.exercices.ExerciseNN_Nom.`
- Implémentation réelle et correcte, **aucun commentaire narratif**,
  pas de `main()`.

## 3. Discipline de vérification — RÈGLE LA PLUS IMPORTANTE

**N'écris jamais une affirmation sur le comportement de Java sans
l'avoir vérifiée réellement dans un terminal au préalable.** Ceci
s'applique à absolument tout : un message d'erreur exact de `javac`,
un résultat numérique, l'ordre d'exécution d'un bloc statique, le
comportement d'une méthode de bibliothèque, etc. Ne jamais se fier à
la mémoire du modèle ou à "ça doit être comme ça".

Procédure standard :

1. **Avant d'écrire un quiz block**, écrire le code du bloc dans un
   fichier scratch (`/tmp/xxx/A.java`), le compiler avec `javac
   --release 17 A.java` (adapter la version au JDK du projet), noter
   le message d'erreur EXACT (ou confirmer que ça compile), puis
   citer ce message tel quel dans les "Reponses officielles".
2. **Avant d'écrire un exemple numérique/comportemental** (résultat
   d'une expression, ordre d'un log, sortie d'une méthode), écrire un
   petit programme scratch qui l'exécute réellement et imprime le
   résultat, plutôt que de calculer "de tête".
3. Après avoir écrit CHAQUE Exercise + Solution, compiler tout le
   projet (`mvn -q compile`) et corriger immédiatement toute erreur.
4. Après avoir écrit un lot d'exercices comportementaux (pas
   nécessairement après chacun), vérifier que chaque `SolutionNN`
   satisfait réellement les assertions de son `ExerciseNN` : comme
   `main()` de l'exercice lance volontairement une exception tant que
   le TODO n'est pas résolu, écrire une classe de vérification
   "jetable" (même package que la solution, ex.
   `chN_nom.solutions.VerifyNN`, compilée dans un dossier temporaire
   avec `javac --release 17 -cp target/classes -d /tmp/verify ...`
   puis exécutée avec `java -cp /tmp/verify:target/classes ...`) qui
   appelle directement les méthodes de `SolutionNN` avec les mêmes
   valeurs que l'énoncé et vérifie les mêmes égalités. Supprimer ce
   dossier temporaire une fois la vérification terminée.
5. Ne jamais déclarer un exercice "terminé" sur la base d'un calcul
   mental, même simple (ex: `Math.PI * 2 * 2` a été mal calculé de
   tête une fois dans ce projet — "12.57" au lieu du vrai
   "12.566370614359172" — et détecté seulement grâce à cette étape de
   vérification).

## 4. Discipline anti-flakiness (code concurrent/aléatoire)

Si le chapitre touche au multithreading ou à `Math.random()` :
- Concevoir les assertions pour être déterministes par construction
  quand c'est possible (`join()`/`awaitTermination` avant d'asserter,
  `CountDownLatch` plutôt que des `sleep()` devinés, des propriétés
  correctes plutôt que des timings).
- Si une démonstration est intrinsèquement probabiliste (ex. montrer
  une race condition), ne JAMAIS asserter un résultat précis — asserter
  seulement qu'elle se termine proprement, et laisser l'observation
  s'afficher.
- Ne jamais laisser du code de deadlock/livelock RÉELLEMENT exécutable
  dans un `main()` — le garder définitivement en commentaire, avec un
  avertissement explicite.
- Pour `Math.random()` : répéter le tirage des dizaines de fois dans
  `main()` et vérifier une PROPRIÉTÉ (ex. "reste toujours dans
  l'intervalle [min, max]"), jamais une valeur exacte.

## 5. Couverture complète — auto-relecture obligatoire

Avant de considérer un chapitre terminé, refaire une passe qui
confronte **chaque phrase** du Summary/Exam Essentials collé par
l'utilisateur à la liste des exercices écrits. Si une règle explicite
n'est couverte par aucun exercice, ajouter un ou plusieurs exercices
supplémentaires pour la combler — ne JAMAIS s'arrêter juste parce que
le nombre d'exercices "semble suffisant". Historique : les chapitres
`exceptions`, `io`, `beyondclasses` et `ch3_makingdecisions` ont tous
été étendus après une telle relecture qui a débusqué un point cité
dans le texte source mais pas encore exercé.

Si le texte source du chapitre est vague/générique (ne cite aucune
règle précise, contrairement aux autres chapitres) : le signaler
explicitement à l'utilisateur, puis s'appuyer sur la connaissance
standard du contenu de CE chapitre précis dans un vrai manuel OCP —
mais vérifier quand même chaque comportement affirmé avec `javac`/
`java` avant de l'écrire (la règle de la section 3 s'applique QUAND
MÊME).

## 6. Pièges d'environnement déjà rencontrés (à vérifier à nouveau sur toute nouvelle machine)

- **`pom.xml` sans `maven.compiler.release`** : par défaut Maven peut
  compiler en Java 8, ce qui casse silencieusement tout exercice
  utilisant `var`, les records, `sealed`, le pattern matching, etc.
  Vérifier `pom.xml` dès qu'un chapitre a besoin d'une syntaxe post-Java-8,
  et ajouter/corriger :
  ```xml
  <properties>
    <maven.compiler.release>17</maven.compiler.release>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
  </properties>
  ```
  (adapter `17` à la version réellement installée : vérifier avec
  `java --version`).
- **Locale par défaut de la machine** : ne JAMAIS supposer `en_US`.
  Toute utilisation de `NumberFormat`, `DecimalFormat`,
  `DateTimeFormatter`, ou `ResourceBundle.getBundle(name)` (überload à
  1 argument) doit recevoir une `Locale` explicite partout dans
  l'exercice ET dans les assertions, sinon le résultat attendu ne sera
  correct que sur une machine dont la locale par défaut coïncide par
  hasard.
- **Nettoyage de fichiers temporaires** : ne jamais utiliser un
  `find -name "prefixe*" | xargs rm -rf` générique dans un dossier qui
  mélange sources et artefacts générés — un dossier source peut
  partager le même préfixe qu'un dossier de build et se faire supprimer
  par erreur. Toujours lister précisément les chemins à supprimer, ou
  isoler les artefacts générés dans un sous-dossier dédié
  (`build/`, `.out/`) qui ne peut jamais matcher un nom de source.

## 7. Workflow attendu, du message de l'utilisateur au push

Quand l'utilisateur écrit quelque chose comme `chap N: <Titre>,
Summary` suivi du texte du Summary/Exam Essentials :

1. Déterminer/rappeler le numéro de chapitre `N` et le nom de package
   `chN_nom` (vérifier qu'il n'existe pas déjà).
2. Créer `src/main/java/chN_nom/exercises/` et
   `src/main/java/chN_nom/solutions/`, plus `ExerciseChecker.java`
   (section 2.1).
3. Lire attentivement le Summary/Exam Essentials, découper le
   chapitre en sous-thèmes distincts, et esquisser une liste
   d'exercices (mélange d'exercices comportementaux et de quiz, en
   général 10 à 16 exercices au total selon la densité du chapitre)
   qui couvre CHAQUE règle citée.
4. Écrire les exercices dans l'ordre, par petits lots (2-3 à la fois),
   en respectant STRICTEMENT le format des sections 2.2/2.3/2.5, et en
   appliquant la discipline de vérification de la section 3 pour
   CHAQUE fait affirmé (surtout les quiz).
5. Après chaque lot : `mvn -q compile`, corriger toute erreur
   immédiatement.
6. Après avoir écrit tous les exercices comportementaux : vérifier
   TOUTES les solutions comportementales via des classes de
   vérification jetables (section 3, point 4), et nettoyer les
   fichiers temporaires ensuite.
7. Faire la passe d'auto-relecture de couverture (section 5). Ajouter
   des exercices si des trous sont trouvés, et re-vérifier ces
   nouveaux exercices (retour au point 5-6 pour eux uniquement).
8. Résumer à l'utilisateur ce qui a été fait, en listant les sujets
   couverts par numéro d'exercice, en confirmant que tout compile et
   que toutes les vérifications passent, et **NE PAS committer sans
   qu'on te le demande explicitement**.
9. Quand (et seulement quand) l'utilisateur demande explicitement de
   committer/pusher :
   - `git status --porcelain` pour voir ce qui a changé.
   - Stager UNIQUEMENT les chemins pertinents du nouveau chapitre
     (`git add src/main/java/chN_nom/` etc.) — jamais `git add -A` ni
     `git add .` à l'aveugle, pour ne jamais inclure des dossiers
     d'IDE (`.idea/`) ou d'autres travaux en cours de l'utilisateur.
   - Un seul commit avec un message qui résume ce que couvre le
     chapitre (liste concise des sujets, mention des exercices quiz
     s'il y en a), signé `Co-Authored-By: <Nom du modele> <noreply@anthropic.com>`
     si c'est la convention du dépôt (vérifier les commits précédents
     avec `git log`).
   - `git push origin <branche>`.
   - Ne jamais forcer un push, ne jamais utiliser `--no-verify`.

## 8. Adapter ce prompt à un AUTRE langage que Java

Tout ce document a été écrit en observant un dépôt Java/Maven, mais
la méthode elle-même (métaphore pédagogique + TODO à trous + corrigé
séparé + quiz "prédire puis vérifier" + vérification empirique
obligatoire + auto-relecture de couverture + workflow git) est
**indépendante du langage**. Pour l'appliquer à un autre langage
(JavaScript, Python, Rust, peu importe), il suffit de remplacer les
paramètres suivants — le reste des règles (sections 2 à 7) reste
valable tel quel, juste en substituant le vocabulaire.

| Paramètre Java (ce depot) | À redéfinir pour un autre langage |
|---|---|
| Commande de vérification (`javac --release 17 X.java && java X`) | La commande réelle qui exécute/compile un fichier dans CE langage |
| "Package" (`src/main/java/chN_nom/`) | Dossier/module équivalent selon la convention idiomatique du langage |
| `ExerciseChecker.java` (helper `check`/`summary`) | Le même helper minimal, réécrit dans la syntaxe du langage cible |
| Un quiz "ça compile ou pas ?" | Reformuler selon ce que CE langage peut réellement révéler avant/à l'exécution (voir ci-dessous) |
| `mvn -q compile` (vérif de tout le projet) | La commande de build/lint globale du langage cible, si elle existe |
| Contrainte "un dossier ne peut pas commencer par un chiffre" | Ne s'applique QUE si le langage exige que le nom de dossier soit un identifiant valide (comme un package Java) — sinon, ignorer cette contrainte |

### Le point le plus important à re-décider : que veut dire "quiz" ici ?

Le format quiz de ce dépôt ("ça compile ou pas ?") repose sur le fait
que Java est un langage **compilé et statiquement typé** : une
énorme partie des pièges de l'examen se révèlent AVANT toute
exécution. Ce n'est vrai que pour certains langages cibles :

- **Langage compilé/typé statiquement** (TypeScript avec `tsc`, Rust,
  Go, C#...) : garder le format "ça compile ou pas ?" tel quel, en
  remplaçant juste la commande de vérification (ex. `tsc --noEmit
  fichier.ts` pour TypeScript).
- **Langage interprété/dynamiquement typé** (JavaScript "vanilla",
  Python, Ruby...) : il n'y a presque jamais d'erreur AVANT
  l'exécution — reformuler le quiz en **"est-ce que ça plante A
  L'EXECUTION, et avec quelle erreur ? Sinon, qu'est-ce que ça
  affiche/renvoie exactement ?"**. La discipline de vérification de la
  section 3 s'applique alors encore plus fort : il faut RÉELLEMENT
  exécuter chaque bloc et copier le message d'erreur/la sortie réelle,
  jamais deviner "ce que JavaScript devrait faire".

### Exemple concret : adapter ce prompt pour du JavaScript (Node.js)

Voici les valeurs à donner à l'IA si tu veux la même méthode appliquée
à des exercices JavaScript (à coller en complément de ce document, ou
à dire explicitement en une phrase) :

- **Langage cible** : JavaScript, exécuté avec Node.js (préciser la
  version si tu en as une installée : `node --version`).
- **Commande de vérification** : `node fichier.js` (ajouter
  `"type": "module"` dans un `package.json` si tu veux `import`/
  `export` ES modules plutôt que `require`/`module.exports` — décide
  et impose UN SEUL style dans tout le dépôt, ne jamais mélanger les
  deux).
- **Format quiz** : JavaScript n'a pas de vraie étape de compilation
  (sauf si le chapitre porte sur TypeScript) — remplacer tous les
  quiz "ça compile ou pas ?" par **"est-ce que ça leve une exception,
  et laquelle ? Sinon, qu'est-ce que ça affiche/renvoie exactement ?"**,
  vérifié en exécutant réellement chaque bloc avec `node` et en
  recopiant la sortie/l'erreur RÉELLE (message d'erreur Node.js exact,
  pas une reformulation).
- **`ExerciseChecker` équivalent** (à adapter en `exerciseChecker.mjs`
  ou `.js`, une copie par dossier de chapitre comme en Java) :
  ```js
  let total = 0;
  let passed = 0;

  export function check(label, condition) {
      total++;
      if (condition) {
          passed++;
          console.log(`[PASS] ${label}`);
      } else {
          console.log(`[FAIL] ${label}`);
      }
  }

  export function summary() {
      console.log(`\n--- Resultat : ${passed}/${total} tests passes ---\n`);
      total = 0;
      passed = 0;
  }
  ```
- **Structure de dossier** : JavaScript n'exige PAS qu'un nom de
  dossier soit un identifiant valide (pas de règle "package = nom de
  dossier" comme en Java) — la contrainte `chN_nom` (section 1) ne
  s'applique donc plus techniquement. Choisis quand même une
  convention numérotée cohérente pour t'y retrouver, par exemple
  `ch1-basics/`, `ch2-fonctions/`, etc. (tiret au lieu de underscore
  est tout a fait idiomatique en JS), avec la même structure interne :
  ```
  ch1-basics/
  ├── exerciseChecker.mjs
  ├── exercises/
  │   ├── exercise01-sujet.mjs
  │   └── ...
  └── solutions/
      ├── solution01-sujet.mjs
      └── ...
  ```
- **Build/vérification globale du "projet"** : s'il y a un
  `package.json`, ajoute un script (`npm run check-all` par exemple)
  qui exécute tous les fichiers `exercises/*.mjs` ET `solutions/*.mjs`
  à la suite pour détecter vite toute régression — l'équivalent du
  `mvn -q compile` de ce dépôt Java (mais attention : contrairement à
  `javac`, `node` n'échoue PAS avant l'exécution sur une erreur de
  syntaxe dans un AUTRE fichier non importé — il faut donc explicitement
  exécuter/importer chaque fichier pour être sûr qu'il n'a pas
  d'erreur de syntaxe, `mvn compile` en Java, lui, vérifie TOUT le
  projet en un seul coup).

**En pratique**, pour lancer ça avec une autre IA : colle ce document
en entier, puis ajoute une phrase du genre *"Adapte ce prompt à
JavaScript avec Node.js : vérifie chaque comportement en exécutant
réellement `node` sur un fichier scratch avant de l'écrire dans
l'exercice, remplace les quiz 'ça compile ou pas' par 'ça plante ou
pas, avec quel message, sinon quelle sortie exacte', et utilise la
structure de dossiers `chN-nom/exercises/` + `chN-nom/solutions/`
avec un `exerciseChecker.mjs` par chapitre"*, puis colle le
plan/sommaire du chapitre JavaScript que tu veux traiter (remplace le
rôle du "Summary/Exam Essentials" du livre OCP par la table des
matières ou le résumé du chapitre de TON support JavaScript).

## 9. Mémoire à tenir à jour (si l'IA a un mécanisme de mémoire long terme)

Si l'outil dispose d'un système de mémoire persistante inter-session,
consigner après chaque chapitre : son numéro, son nom de package, la
liste condensée des sujets couverts par exercice, les éventuels
"gaps" trouvés et comblés lors de l'auto-relecture, et tout piège
d'environnement nouvellement découvert (ajouter à la section 6
ci-dessus si ce document est réutilisé comme référence vivante).
