# Lab 09 - Quiz de comprehension (migration, JDK, service) (niveau : examen OCP)

Comme `collections/exercises/Exercise10_WildcardsQuiz.java` ou
`lambdas/exercises/Exercise09_FunctionalInterfaceQuiz.java` : reponds
d'abord toi-meme (sur une feuille), PUIS regarde les "Reponses
officielles" tout en bas. Une partie (les comptes de modules JDK) est
verifiable REELLEMENT avec `./run.sh` sur TA machine.

## Question 1 - Migration top-down vs bottom-up

Une equipe a une grosse application avec 5 JAR non modularises :
`core` (sans dependance), `utils` (depend de `core`), `services`
(depend de `utils`), `web` (depend de `services`), et `reports`
(depend de `services`).

a) En migration TOP-DOWN, quel JAR migre-t-on EN PREMIER (celui qu'on
   dote d'un vrai `module-info.java`, en laissant tous les autres sur
   le module-path SANS module-info) ?

b) En migration BOTTOM-UP, quel JAR migre-t-on EN PREMIER (celui qu'on
   sort du classpath pour le mettre sur le module-path AVEC un vrai
   `module-info.java`, en laissant les autres purement sur le
   classpath) ?

c) Pourquoi la strategie BOTTOM-UP evite-t-elle plus naturellement les
   soucis de dependances manquantes des le debut de la migration ?

## Question 2 - Les 4 parties d'un service

Rappel du Lab04 : nomme les 4 roles d'un service JPMS, et dis
precisement quelle DIRECTIVE (ou quelle CLASSE du JDK) correspond a
chacun.

## Question 3 - Cycles

Rappel du Lab06 : que se passe-t-il si `module A` fait `requires B;`
et `module B` fait `requires A;` en meme temps ? A quel MOMENT
(compilation ou execution) le probleme se manifeste-t-il ?

## Question 4 - Comptes de modules du JDK

D'apres le resume du chapitre : "There are about 20 other modules
provided by the JDK that begin with java.* and about 30 that begin
with jdk.*". Lance `./run.sh` pour compter les VRAIS chiffres sur TA
machine, avec TA version du JDK - est-ce que ca correspond exactement
au livre ?

---

## Reponses officielles (ne regarde qu'apres avoir repondu toi-meme)

**Q1a (top-down)** : on migre `web` (ou `reports`) EN PREMIER - celui
qui a le PLUS de dependances (tout le reste : services, utils, core).
Tous les autres restent sur le module-path SANS module-info, donc
deviennent des automatic modules (voir Lab07) le temps que la
migration avance.

**Q1b (bottom-up)** : on migre `core` EN PREMIER - celui qui n'a
AUCUNE dependance. Tous les autres (utils, services, web, reports)
restent purement sur le CLASSPATH (unnamed modules), pas encore
touches.

**Q1c** : en bottom-up, le tout premier module migre (`core`) n'a par
definition RIEN dont il depende - impossible de tomber sur une
dependance non encore migree. En top-down, le module migre en premier
(`web`) depend de TOUT LE RESTE, qui n'est pas encore modularise :
c'est le module-path AVEC des automatic modules qui absorbe ce
decalage temporairement, plutot que de bloquer la migration.

**Q2** : Interface du service (SPI) = une interface Java normale
(ex: `Notifier`), declaree dans un module et `exports`-ee.
Fournisseur (provider) = une classe qui l'implemente, annoncee via
`provides Interface with Implementation;`.
Localisateur de service (service locator) = `java.util.ServiceLoader`
(classe du JDK, rien a coder soi-meme).
Consommateur (consumer) = le module qui declare `uses Interface;` et
appelle `ServiceLoader.load(Interface.class)`.

**Q3** : `javac` REFUSE de compiler - c'est une erreur de COMPILATION
("cyclic dependence involving..."), jamais une erreur d'execution. Le
Java Platform Module System interdit purement et simplement les
cycles, sans aucune exception possible.

**Q4** : "about 20"/"about 30" sont des ORDRES DE GRANDEUR
volontairement approximatifs dans le livre, PAS des chiffres exacts a
memoriser au module pres - ils changent legerement a chaque version
du JDK (de nouveaux modules `jdk.*` apparaissent regulierement). Ce
qui compte pour l'examen, c'est de savoir que `java.base` est
TOUJOURS present et implicite, et qu'il existe UNE VINGTAINE de
`java.*` et UNE TRENTAINE (OU PLUS, selon la version) de `jdk.*` - pas
d'apprendre un nombre fige par coeur.
