# Chapitre 15 (JDBC) — Mode d'emploi

Lis ce fichier **en entier une fois** avant de commencer. Le fonctionnement est le même qu'aux chapitres précédents :

> **Comment sont faits les énoncés.** Chaque étape d'un projet suit le même schéma :
> - **📖 La leçon** : la notion expliquée simplement, avec un exemple sur **un autre sujet** que le projet ;
> - **👉 À toi** : ce que tu construis ;
> - **🧪 Expériences** et **❓ Questions** : tu essaies, tu observes, tu réponds en commentaire.
>
> Les gestes de base sont expliqués une fois pour toutes dans le **projet 0 du chapitre 1** (`ch1_buildingblocks/projects/p00_bonjour`) : créer une classe, lancer, `Check`, arguments, terminal, lire une erreur. Relis-le si l'un d'eux te manque. Chaque projet rappelle aussi ses commandes exactes.
- des **projets** à construire de A à Z, pour **comprendre** ;
- des **drills** chronométrés, répétés à intervalles espacés, pour **retenir**.

C'est le **dernier** chapitre : le capstone p07 et le drill r06 sont aussi une révision de tout le livre (records, lambdas, collections, exceptions).

---

## 1. Ce que tu vas faire

| | Projets (`projects/`) | Drills de rappel (`drills/`) |
|---|---|---|
| Combien | 7 | 6 (+ 1 bonus) |
| But | de vraies applications sur une base : bibliothèque, banque, boutique, import de fichiers, fidélité, rapports et copie de base, vélos en libre-service | retrouver vite et sans aide l'API JDBC et ses pièges |
| Durée | 2 à 4 h chacun | 12 à 15 min chacun |
| Combien de fois | une fois ; p02 et p07 refaits 2 à 3 semaines plus tard | 6 fois chacun (J0, J+1, J+3, J+7, J+14, J+30) |
| Aide autorisée | Javadoc, papier, réflexion | **aucune** pendant le drill |

---

## 2. La règle du crescendo : chapitres 1 à 15

**Tu as droit à tout le livre**, et à tout le **chapitre 15** :
- **se connecter :** l'URL JDBC, `DriverManager.getConnection`, la fermeture ;
- **exécuter :**
  - `Statement`, `PreparedStatement` (les `?`), `CallableStatement` (IN, OUT, IN OUT) ;
  - `executeQuery`, `executeUpdate`, `execute` ;
- **lire :** `ResultSet` (`next`, les `getXxx` par nom ou par index, `wasNull`, `getObject`) ;
- **les transactions :** l'auto-commit, `commit`, `rollback`, `Savepoint` ;
- **les lots :** `addBatch`/`executeBatch`, `BatchUpdateException` ;
- **les clés générées** et les **métadonnées** (`ResultSetMetaData`, `DatabaseMetaData`) ;
- **`SQLException`** : `getSQLState`, `getErrorCode`, et ses sous-classes.

**Ce qui reste exclu :** `System.exit`, `printStackTrace` et l'heure réelle (`now()`). La sortie doit être identique à chaque lancement. `Check` les refuse avec le message `[FAIL] API : interdit ici`.

---

## 3. Les règles d'un programme qui parle à une base

`Check` compare ta sortie, ligne par ligne, et elle doit être la même sur toutes les machines :
- **H2 en mémoire** (`jdbc:h2:mem:<nom>`) :
  - la base naît à la 1re connexion et **meurt** quand la dernière se ferme : chaque lancement repart de zéro ;
  - aucune installation : le pilote H2 vient de Maven (`pom.xml`), comme ceux de PostgreSQL et MySQL ;
- **toujours un `ORDER BY`** : sans lui, l'ordre des lignes n'est **pas garanti** ;
- **les erreurs s'affichent par leur SQLState** (`23505`), jamais par `getMessage()` de la base : le texte change d'une version à l'autre (et il est en allemand sur une machine allemande !) ;
- **toujours fermer** : `Connection`, `Statement`, `PreparedStatement`, `ResultSet` vont dans un try-with-resources ;
- **les procédures stockées de H2** sont tes propres méthodes Java `public static`, enregistrées par `CREATE ALIAS` avec `Classe.class.getName()`. La classe doit être `public`.

Lance `Check` **depuis la racine du dépôt** (`Kurse`) : c'est le répertoire de travail par défaut dans IntelliJ.

---

## 4. Par où commencer (aujourd'hui)

1. Lis ce fichier jusqu'au bout.
2. Ouvre `projects/p01_library/TODO.md` en aperçu Markdown.
3. Suis la section 6.

**L'ordre complet :**

```
p01 → r01
p02 → r02
p03 → r04
p04 → (révision r02)
p05 → r03
p06 → r05
p07 → r06 (test final)
puis r07 (bonus : pilotes de plusieurs bases, curseurs défilants, isolation ; une partie avec Docker, à la main)
```

---

## 5. La disposition des dossiers

```
ch15_jdbc/
├── PARCOURS.md              ← ce fichier
├── projects/
│   ├── README.md            ← la liste des 7 projets, à cocher
│   └── p01_library/
│       ├── TODO.md          ← L'ÉNONCÉ
│       ├── INDICES.md       ← 2 indices repliés par étape, sans code (si tu bloques)
│       ├── Data.java        ← les données (URL, schéma, lignes) : tu les lis, tu ne les modifies pas
│       ├── Check.java       ← le correcteur : tu le LANCES
│       ├── solution/        ← la correction : à la fin seulement
│       │   └── CORRIGE.md   ← étape par étape : code, réponses aux questions, résultats des expériences
│       └── (tes types)      ← Book, Catalog, LibraryApp... : c'est TOI qui les crées
└── drills/ …

ch15_jdbc-lab/               ← (racine du dépôt) Docker : PostgreSQL et MySQL, pour le drill bonus r07
```

---

## 6. Comment faire un projet

### 6.1 Lire le `TODO.md`

| Partie | Ce que tu en fais |
|---|---|
| **En-tête** | les notions visées, l'algorithme, les types à créer |
| **Tableau de bord** (étapes ☐) | les types et leurs méthodes, les **requêtes exactes**, les **lignes exactes**, les questions et les expériences |
| **Checklist** | ce que `Check` cherchera dans ton code |
| **Sortie attendue complète** | le contrat exact |

### 6.2 Travailler, étape par étape

1. **Écris le SQL d'abord dans ta tête** : quelles lignes doit-il rendre ? Puis seulement le code Java.
2. **Fais une étape à la fois.** Lance `Check`, corrige, puis coche ☐ → ☑.
3. **Fais les expériences** : provoque chaque `SQLException` au moins une fois, et lis son SQLState.
4. **Réponds aux questions par écrit**, en commentaire dans ton code.
5. **Vérifie l'étape** : ouvre la section de cette étape (et **seulement** elle) dans `solution/CORRIGE.md`. Compare tes réponses et le résultat de tes expériences. Une réponse fausse : corrige ton commentaire avec tes propres mots.

### 6.3 Lire la réponse de `Check`

| Ligne | Signification | Que faire |
|---|---|---|
| `[ERREUR] … SQLException … 42S02` (ou `42102`) | une table inconnue | le schéma n'est pas créé, ou le `PreparedStatement` a été préparé **avant** le `CREATE TABLE` |
| `[ERREUR] … 90012` | un `?` sans valeur | un `setXxx` oublié |
| `[ERREUR] … 90022` ou `ClassNotFoundException` | un alias H2 introuvable | la classe ou la méthode n'est pas `public static`, ou le nom n'utilise pas `getName()` |
| `[FAIL] sortie : …` | une ligne diffère | souvent un `ORDER BY` oublié, ou un `wasNull` mal placé |
| `[FAIL] API : …` | un élément manque, ou est interdit | la checklist |
| `*** PROJET REUSSI ***` | tout est juste | compare avec la solution |

### 6.4 Quand tu bloques

| Palier | Temps | Ce que tu fais |
|---|---|---|
| 1 | jusqu'à 20 min | exécute ta requête **seule** et affiche toutes ses lignes ; relis le SQLState |
| 2 | 20 min de plus | ouvre l'**indice 1** de l'étape dans `INDICES.md`, puis l'**indice 2** s'il ne suffit pas ; relis la carte mémoire du drill du même thème, ou demande-moi un **indice** |
| 3 | en dernier recours | lis **uniquement** la section de l'étape dans `solution/CORRIGE.md` (ou la partie concernée de `solution/`), ferme, réécris |

---

## 7. Comment faire un drill

1. Note l'heure. Crée `RecallNN.java`, de mémoire.
2. Lance `Check`.
3. **Après seulement :** la carte mémoire, puis les expériences.
4. Note date, temps et ✗ dans `drills/README.md`. Avant chaque répétition, supprime ton `RecallNN.java`.

---

## 8. Comment savoir que le chapitre 15 est acquis

- [ ] Les 7 projets affichent `PROJET REUSSI`, et toutes les questions ont une réponse écrite.
- [ ] Les 6 drills ont passé la répétition R3 (J+7, sans carte).
- [ ] Tu sais, sans hésiter :
  - découper une URL JDBC, et dire ce qui arrive avec une URL qu'aucun pilote n'accepte ;
  - choisir entre `executeQuery`, `executeUpdate` et `execute`, et dire ce que rend chacune ;
  - numéroter les `?`, et dire ce qui arrive avec un mauvais index ou un `?` oublié ;
  - lire un `ResultSet` (le curseur avant la 1re ligne, les index à partir de 1, `wasNull`) ;
  - écrire un appel `CallableStatement` avec IN, OUT et IN OUT ;
  - dire ce que voit une autre connexion avant et après `commit`, et ce que fait un `Savepoint` ;
  - dire qui ferme quoi quand une `Connection` ou un `Statement` se ferme.
- [ ] Tu sais écrire sans aide : un import par lots avec clés générées, une transaction tout-ou-rien, un rapport générique avec `ResultSetMetaData`.
- [ ] p02 et p07 ont été refaits **depuis un dossier vide**, 2 à 3 semaines plus tard.
