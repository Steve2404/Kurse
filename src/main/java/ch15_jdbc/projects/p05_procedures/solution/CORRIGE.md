# Projet 5 — Corrigé étape par étape

> **Quand le lire :** ouvre **seulement la section de l'étape** que tu viens de terminer. Le programme complet est dans [`StoredProcs.java`](StoredProcs.java) et [`LoyaltyApp.java`](LoyaltyApp.java).
>
> Les résultats ci-dessous ont été obtenus en direct avec **JDK 17** (17.0.18) et **H2 2.3.232**, sur la solution, sur une copie modifiée et sur de petits programmes d'essai.

---

## Étape 1 — Écrire et enregistrer les procédures

**Question — pourquoi `getName()` ?** H2 retrouve la méthode par le **nom complet** de sa classe (paquet compris). Ta classe est dans `ch15_jdbc.projects.p05_procedures`, celle de la solution dans `…p05_procedures.solution` : un nom écrit à la main ne marcherait que pour l'une des deux. `StoredProcs.class.getName()` donne toujours le bon nom, et suit un éventuel renommage.

**Expérience 1 — `StoredProcs` non `public`** (vérifié) : le `CREATE ALIAS` **réussit** (les 6 alias sont enregistrés). L'erreur n'arrive qu'au **1er appel** de `LUHN` dans un `SELECT` :

```
General error: "java.lang.IllegalAccessException: class org.h2.schema.FunctionAlias$JavaMethod cannot access a member of class …StoredProcs with modifiers "public static""
```

H2, qui est dans un autre paquet, n'a pas le droit d'appeler une méthode d'une classe non publique, même si la méthode est `public static`.

---

## Étape 2 — OUT et IN OUT

**Question — `getInt(1)` sans `registerOutParameter` ?** Vérifié avec H2 sur `{? = call ABS(?)}` : une `SQLException` de SQLState `90008` (`Invalid value "1" for parameter "parameterIndex"`). Le paramètre 1 n'a pas été déclaré comme paramètre de **sortie** : il n'y a rien à lire.

---

## Étape 3 — Les procédures qui travaillent sur la base

**Question — pourquoi `topCustomers` ne ferme-t-elle pas son `PreparedStatement` ?** Fermer un `Statement` ferme son `ResultSet` (projet 1, étape 5). Si la procédure le fermait, l'appelant recevrait un `ResultSet` **déjà fermé**, inutilisable.

**Question — pourquoi ne rien modifier dans une telle procédure ?** H2 l'appelle une fois de plus **pendant** `prepareCall`, juste pour connaître les colonnes du résultat. Une procédure qui modifiait la base ferait donc ses modifications **deux fois**.

---

## Étape 4 — Les erreurs

- procédure inconnue : `90022` ;
- paramètre IN oublié : `90012`.

**Expérience 2 — `{call LUHN(?)}` avec `executeQuery()` :** vérifié sur une fonction semblable : le `ResultSet` contient **une** colonne (nommée d'après l'appel, ici `PUBLIC.PAIR(?1)`) et **une** ligne, avec la valeur rendue (`true`). H2 présente le résultat d'une fonction comme une petite table.

**Expérience 3 — avec PostgreSQL ou MySQL :** seul **`StoredProcs`** (et les `CREATE ALIAS`) changeraient : la procédure s'écrirait en SQL dans la base. Le code **JDBC** de `LoyaltyApp` (`prepareCall`, `registerOutParameter`, `execute`…) resterait le même : c'est l'intérêt de JDBC.
