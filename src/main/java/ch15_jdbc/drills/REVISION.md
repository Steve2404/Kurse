# Chapitre 15 (JDBC) — parcours, drills et plan de révision

Les **exercices** (`ch15_jdbc/exercises`, 01 → 20) t'apprennent les notions.
Les **drills** (`ch15_jdbc/drills/exercises`, 01 → 04) te les font répéter jusqu'à ce
qu'elles sortent toutes seules. Tous les drills utilisent les mêmes données : `drills/LibraryDb.java`
charge **la bibliothèque du chapitre 10 en tables SQL** (`books`, `members`, `loans`, plus une
procédure `LATE_FEE`) dans une base **H2 en mémoire** toute neuve à chaque `open()`. Aucun Docker.

Les corrigés (`solutions/` et `drills/solutions/`) sont **commentés** : chaque méthode
explique pourquoi on l'écrit ainsi et quel piège elle évite. Lis-les **après** avoir réussi.

⚠️ Les exercices 16 à 19 (plusieurs fournisseurs) ont besoin de Docker pour Postgres et MySQL
(`docker compose up -d` dans `ch15_jdbc-lab/`). Sans Docker, leurs tests affichent `[SAUTE]` :
ce n'est pas un échec. Tout le reste (exercices 01 à 15, 20, et tous les drills) tourne sur H2 seul.

---

## Par quoi commencer : exercices ou drills ?

**Les deux, en alternant, thème par thème.** Jour 1 : les exercices du thème
(comprendre). Jour 2 : le drill du thème (mémoriser).

| Étape | Thème | Jour 1 — exercices | Jour 2 — drills |
|---|---|---|---|
| 1 | Connexion, requêtes, `ResultSet` | 01 → 02 → 03 → 04 → 05 → 06 | Drill01 |
| 2 | Procédures, transactions, fermeture | 07 → 08 → 09 → 10 → 11 | Drill02 (TODO 9) puis Drill03 |
| 3 | Fonctions avancées : batch, clés, métadonnées | 12 → 13 → 14 → 15 | Drill02 |
| 4 | Plusieurs fournisseurs (Docker) | 16 → 17 → 18 → 19 | refaire Drill01 |
| 5 | Synthèse | 20 (capstone : service d'emprunts) | Drill04 (kata mélangé) |

**Séance type (≈ 1 h) :** 1) les révisions dues (10 – 20 min), 2) la nouveauté,
3) 2 minutes de « carte vierge » : les 5 interfaces (`Driver`, `Connection`, `PreparedStatement`,
`CallableStatement`, `ResultSet`), `execute` / `executeQuery` / `executeUpdate` avec leur type de
retour, les index qui commencent à **1**, l'ordre de fermeture, le trio `setAutoCommit(false)` /
`commit` / `rollback` et le `Savepoint`.

**Conseil propre à ce chapitre :** toute valeur qui vient de l'extérieur passe par un `?`
(jamais de concaténation dans le SQL) ; tout objet JDBC se ferme (try-with-resources) ; toute
transaction remet l'auto-commit dans un `finally`.

| Drill | Contenu | TODO |
|---|---|---|
| 01 `ConnectAndQuery` | `DriverManager`, `Statement`, `PreparedStatement`, `next`, `getString` / `getInt` / `getDouble`, `NULL`, résultat vide, métadonnées, `execute`, paramètre numérique | 10 |
| 02 `UpdatesAndCalls` | `executeUpdate` (INSERT, UPDATE, DELETE, CREATE), `setNull`, réutiliser un `PreparedStatement`, batch, clé générée, `CallableStatement`, 0 ligne touchée | 10 |
| 03 `Transactions` | auto-commit, `commit`, `rollback`, `Savepoint`, annuler sur erreur, `finally`, deux savepoints, connexion fermée | 8 |
| 04 `MixedKata` | 8 questions sur la bibliothèque en SQL, **sans indiquer la forme** | 8 |

---

## Comment faire un drill

1. Lance un chronomètre.
2. Remplis les TODO **sans regarder la « CARTE MÉMOIRE »** en bas du fichier.
3. Bloqué plus d'une minute ? Regarde la carte, **cache-la, puis réécris de mémoire**.
   Mets une croix à côté de ce TODO : c'est un point faible.
4. Lance `main()` jusqu'à 100 %.
5. Note ton temps, ton score au premier lancement et tes TODO « croix » dans le tableau.

## Pour ne plus oublier

- **Rappel actif** : refaire depuis une page blanche vaut dix relectures.
- **Répétition espacée** : J, J+1, J+3, J+7, J+14, J+30, puis tous les 2 mois.
- **Mélange** : le Drill04 chaque semaine pendant la révision de l'examen.
- **Lecture de code** : l'exercice 05 compare ta règle au **vrai H2** (`executeQuery` sur un UPDATE,
  `executeUpdate` sur un SELECT, lire avant `next()`, `getInt(0)`, `getInt` sur NULL → 0 et
  `wasNull()`, `previous()` sur un ResultSet en avance seule, objets fermés…). Relis sa Javadoc avant
  l'examen, puis fais les questions de révision du livre.
- Pièges vus en direct : `YEAR` et `MONTH` sont des mots réservés pour H2 (d'où `pub_year`,
  `loan_month`) ; un `CREATE TABLE` valide implicitement la transaction dans beaucoup de bases.

## Remettre un fichier à zéro pour le refaire

```
git restore src/main/java/ch15_jdbc/drills/exercises/Drill01_ConnectAndQuery.java
```

(tant que tes réponses ne sont pas commitées ; sinon `git restore --source=origin/main -- <chemin>`).
⚠️ Cela efface ta version : c'est voulu pour un drill.

## Tableau de suivi

Format : `date – temps – score au 1er lancement` (ex. `30/09 – 8 min – 9/10`).

| Drill | J | J+1 | J+3 | J+7 | J+14 | J+30 | TODO faibles |
|---|---|---|---|---|---|---|---|
| 01 Connexion et lecture | | | | | | | |
| 02 Écritures et appels | | | | | | | |
| 03 Transactions | | | | | | | |
| 04 Kata mélangé | | | | | | | |
