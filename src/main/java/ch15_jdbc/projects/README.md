# Chapitre 15 — Projets (tableau de bord)

> Le parcours complet (projets + drills + répétition, la règle du crescendo, et **les règles d'un programme qui parle à une base**) est décrit dans `../PARCOURS.md`.

Chaque projet est une **application à construire de A à Z**. Dans son dossier, tu ne trouves que :
- `TODO.md` : l'énoncé ;
- `Data.java` : les données (URL, schéma, lignes) ;
- `Check.java` : le correcteur ;
- `solution/` : à n'ouvrir qu'à la fin.

**Tous les types, c'est toi qui les crées.**

| ☐ | Projet | Notions | Classe `main` | Ce qui est dur |
|---|---|---|---|---|
| ☐ | `p01_library` — catalogue de bibliothèque | URL, `DriverManager`, `Statement`, `PreparedStatement`, `ResultSet`, `wasNull`, SQLState | `LibraryApp` | pagination jusqu'à la page vide, pièges du curseur, injection SQL |
| ☐ | `p02_bank` — virements | auto-commit, `commit`, `rollback`, isolation entre connexions, clés générées | `BankApp` | paies tout ou rien, rejeu du journal qui doit retrouver les soldes |
| ☐ | `p03_orders` — commandes | `Savepoint` : `rollback(sp)`, `releaseSavepoint`, nommés et anonymes | `ShopApp` | politiques TOUT / PARTIEL, erreur au 2e ordre d'une ligne, contrôle par jointure |
| ☐ | `p04_import` — import de fichiers clients | lots, `BatchUpdateException`, `EXECUTE_FAILED`, clés par nom de colonne | `ImportApp` | nettoyage et dédoublonnage, paquets, import strict ou tolérant |
| ☐ | `p05_procedures` — programme de fidélité | `CallableStatement` : IN, OUT, IN OUT, procédure qui rend un `ResultSet`, `CREATE ALIAS` | `LoyaltyApp` | algorithme de Luhn, fonctions utilisées en Java et en SQL |
| ☐ | `p06_report` — rapports et copie de base | `ResultSetMetaData`, `DatabaseMetaData` (tables, colonnes, clés) | `ReportApp` | tableau générique, tri topologique, dump rechargé et comparé |
| ☐ | `p07_bikes` — **capstone** vélos en libre-service | tout le chapitre | `BikeApp` | tarif par tranche, dette et remboursement, rééquilibrage en un lot |
