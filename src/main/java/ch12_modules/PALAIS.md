# 🏠 Palais mental — chapitre 12 : la douche 2, stations 1 à 6

> **Avant :** lis une fois [`PALAIS_MENTAL.md`](../../../../PALAIS_MENTAL.md). Le salon, la cuisine, les trois chambres et la douche 1 (chapitres 1 à 11) viennent avant dans la balade.
> **Quand :** après le capstone du chapitre, puis avant chaque répétition des drills.
> **Comment :** lis la règle, ferme les yeux, joue la scène 10 secondes, redis la règle à voix haute.

---

### 1. 🚪 La porte de la douche 2 — `module-info.java`

- **Image :** sur la porte, un **règlement intérieur** affiché à la racine du module : `module com.zoo { … }`. Chaque ligne est un panneau : **`requires`** (« j'ai besoin de… »), **`exports`** (« j'ouvre ce paquet au public »), **`exports … to`** (« seulement à lui »), **`opens`** (« la réflexion peut fouiller ici »), **`uses`** (« je consomme ce service »), **`provides … with`** (« je fournis ce service avec cette classe »).
- **À retenir :**
  - `module-info.java` est à la **racine** du module (à côté des dossiers de paquets) ;
  - `requires m;`, `requires transitive m;`, `exports p;`, `exports p to m;`, `opens p;`, `uses I;`, `provides I with C;` ;
  - on exporte des **paquets**, on requiert des **modules** ;
  - `java.base` est requis **automatiquement**.
- **Mon image :** …

### 2. 🪞 Le miroir — les commandes

- **Image :** sur le miroir, des **lettres de buée** : `-p` (le **chemin des modules**, `--module-path`), `-d` (le dossier de sortie de `javac`, ou `--describe-module` pour `java`), `-m` (le **module à lancer**, `module/classe`). Un pot de crème **`jar --create --file x.jar -C dossier .`** emballe un module. Une loupe **`jdeps`** dit de qui un jar dépend.
- **À retenir :**
  - compiler : `javac -p mods -d out/m src/m/module-info.java src/m/paquet/*.java` ;
  - lancer : `java -p mods -m module/paquet.Classe` ;
  - emballer : `jar --create --file mods/m.jar -C out/m .` ;
  - inspecter : `java --list-modules`, `java -p mods --describe-module m` (`-d`), `java --show-module-resolution`, `jdeps --module-path mods m.jar`, `jdeps --jdk-internals` ;
  - `jmod` crée des fichiers `.jmod` (à l'examen : on sait seulement qu'il existe).
- **Mon image :** …

### 3. 🚰 Le lavabo — les trois sortes de modules

- **Image :** trois savons dans le lavabo. Le **savon gravé** (module **nommé**) a son `module-info`. Le **savon sans gravure posé sur le chemin des modules** devient un module **automatique** : il prend le nom inscrit dans son `MANIFEST` (`Automatic-Module-Name`) ou, sinon, le **nom du fichier sans la version** (`zoo-food-1.0.jar` → `zoo.food`), et il **exporte tout**. Le **savon tombé dans la baignoire** (le classpath) est dans le module **sans nom** : il voit tout le monde, mais **aucun module nommé** ne peut le voir.
- **À retenir :**
  - module **nommé** : un `module-info`, sur le module path ;
  - module **automatique** : un jar sans `module-info` sur le module path ; exporte et lit tout ; nom : `Automatic-Module-Name`, sinon le nom du fichier (version retirée, `-` → `.`) ;
  - module **sans nom** : tout ce qui est sur le classpath ; lit tous les modules, mais n'est lisible par **aucun** module nommé.
- **Mon image :** …

### 4. 🚿 Le robinet — `requires transitive` et les interdits

- **Image :** le robinet A est branché sur le tuyau B, branché `transitive` sur C : l'eau de C **arrive aussi** chez A, sans tuyau direct (lisibilité implicite). Deux tuyaux qui se branchent **l'un sur l'autre en boucle** : la plomberie **refuse** (pas de cycle). Deux modules qui exportent **le même paquet** : **conflit**, la douche ne démarre pas.
- **À retenir :**
  - si B `requires transitive C`, tout module qui requiert B **lit aussi** C ;
  - **pas de dépendance cyclique** entre modules ;
  - un même paquet dans deux modules lus par le même module = erreur (paquets coupés) ;
  - `exports` sans `opens` : accès normal au code public, mais pas à la réflexion profonde.
- **Mon image :** …

### 5. 🚿 Le pommeau — les services

- **Image :** quatre personnages sous le pommeau. L'**interface du service** (le contrat) est dans un module qui l'**exporte**. Le **fournisseur** crie `provides Service with Implementation`. Le **consommateur** chuchote `uses Service` et appelle **`ServiceLoader.load(Service.class)`**. Le **localisateur** (service locator), souvent un module à part, fait l'appel à `ServiceLoader` pour les autres. Personne ne connaît le nom de la classe d'implémentation : on découvre ce qui est installé.
- **À retenir :**
  - interface de service : `exports` de son paquet ;
  - fournisseur : `requires` le module du service, `provides I with C;` (pas besoin d'exporter `C`) ;
  - consommateur : `requires`, `uses I;`, puis `ServiceLoader.load(I.class)` ;
  - `ServiceLoader` est `Iterable` ; `stream()` rend des `Provider<I>`, d'où `.map(Provider::get)`.
- **Mon image :** …

### 6. 🧼 Le savon — migrer une application

- **Image :** une maison de savons à migrer. **De bas en haut** : tu graves d'abord le **savon du fond** (celui qui ne dépend de personne, et dont les autres dépendent), puis tu remontes ; les autres restent dans la baignoire (classpath) en attendant. **De haut en bas** : tu poses **tous** les savons sur le chemin des modules (ils deviennent **automatiques**), puis tu graves d'abord celui **du haut**.
- **À retenir :**
  - **bottom-up** : migrer d'abord les modules **sans dépendance** (le bas) ; ceux pas encore migrés restent sur le classpath ;
  - **top-down** : tout mettre sur le module path (modules automatiques), puis migrer d'abord le **haut** (celui que personne ne requiert) ;
  - `opens` pour les frameworks qui utilisent la réflexion ; `java.base` toujours présent.
- **Mon image :** …

---

## ⚡ La balade éclair

1. Station 1 : exporte-t-on un module ou un paquet ?
2. Station 2 : quelle commande lance la classe `zoo.Main` du module `zoo` ?
3. Station 3 : quel nom prend `animal-care-2.1.jar` posé sur le module path ?
4. Station 4 : que permet `requires transitive` ?
5. Station 5 : quels mots-clés écrit le fournisseur, et le consommateur ?
6. Station 6 : en migration top-down, où sont posés tous les jars au départ ?
