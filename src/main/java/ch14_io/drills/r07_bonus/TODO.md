# Drill de rappel 7 (BONUS) — Liens symboliques, POSIX, attributs par nom, `Console`

> Première fois ? Lis d'abord le mode d'emploi [`ch14_io/PARCOURS.md`](../../PARCOURS.md). À faire après le drill r06.

**Chrono cible :** 12 min, puis 6 min.

**Particularité :** ces outils dépendent du **système** :
- les liens symboliques demandent souvent des droits sous Windows ;
- la vue POSIX n'existe que sous Linux et macOS ;
- `System.console()` vaut `null` dans un IDE.

Le programme n'affiche donc que des **vérités valables partout** (« cohérent »), et `Check` les vérifie. La partie interactive se fait **à la main**, dans un vrai terminal.

**Règles :**
- Tout se fait de mémoire, **imports compris**.
- Crée la classe **`Recall07`** dans le paquet `ch14_io.drills.r07_bonus`. Le `main` déclare `throws IOException`.
- Ajoute `static boolean checkPassword(char[] typed)` : elle compare à `"s3cret".toCharArray()` avec `Arrays.equals`, puis **efface** `typed` avec `Arrays.fill(typed, '\0')` dans un `finally`.
- Le bac à sable `build/ch14/r07_bonus` est supprimé au départ (avec `Files.exists(box, LinkOption.NOFOLLOW_LINKS)`), puis tu crées `donnees/a.txt` (contenu `contenu`).

## Défis

- ☐ **D01.** `Files.createSymbolicLink(box/raccourci, cible.toAbsolutePath())`, où la cible est le dossier `donnees`.
  - **Si la création réussit**, `coherent` vaut vrai quand **toutes** ces conditions sont vraies :
    - `isSymbolicLink(lien)` ;
    - `readSymbolicLink(lien)` égale la cible absolue ;
    - `isSameFile(lien, cible)` ;
    - `isDirectory(lien)`, mais `!isDirectory(lien, LinkOption.NOFOLLOW_LINKS)`.
    
    Puis compte `Files.walk(box, FileVisitOption.FOLLOW_LINKS)` et `Files.walk(box)`.
  - **Si elle échoue** (`catch (FileSystemException | UnsupportedOperationException e)`) : `coherent` = le lien n'existe pas.
  
  Affiche `coherent`, puis si le parcours avec liens voit plus de chemins (ou s'il n'a pas pu être fait).
  → `D01 : coherent true, walk avec FOLLOW_LINKS voit plus true`
- ☐ **D02.** `posix = FileSystems.getDefault().supportedFileAttributeViews().contains("posix")`. Puis `Files.readAttributes(a.txt, PosixFileAttributes.class)` :
  - s'il réussit, c'est cohérent si `posix` est vrai et que `PosixFilePermissions.toString(permissions)` a 9 caractères (`rw-r--r--`) ;
  - en cas d'`UnsupportedOperationException`, c'est cohérent si `posix` est faux.
  
  Affiche aussi si la vue `basic` est supportée.
  → `D02 : posix coherent true, vue basic toujours presente true`
- ☐ **D03.** `Files.readAttributes(a.txt, "basic:size,isRegularFile")` (une `Map`). Affiche `size`, `isRegularFile`, puis la taille de la map.
  → `D03 : 7 true 2`
- ☐ **D04.** `Console console` vaut `System.console()` **seulement si** `args[0]` est `"console"`, sinon `null`.
  - **Avec une console :** `readLine("Ton nom ? ")`, `readPassword("Mot de passe (%s) ? ", "s3cret")`, puis `console.writer().printf("Bonjour %s%n", name)` et `flush()`.
  - **Sans console :** lis le nom et le mot de passe depuis `new BufferedReader(new StringReader("Ada\ns3cret\n"))`.
  
  Affiche `entree simulee` ou `console`, le nom, `checkPassword(password)`, puis si le tableau est bien effacé.
  → `D04 : entree simulee Ada true efface true`

## À faire à la main, dans un vrai terminal (hors `Check`)

Depuis la racine du dépôt, compile ta classe, puis lance-la avec l'argument `console` :

```bash
javac -d build/manuel src/main/java/ch14_io/drills/r07_bonus/Recall07.java
java -cp build/manuel ch14_io.drills.r07_bonus.Recall07 console
```

- Tape ton nom, puis `s3cret` : le mot de passe ne doit **pas** s'afficher pendant la saisie.
- Relance avec `| more` à la fin (sortie redirigée) : que vaut `System.console()` ?
- Sous Windows, active le **mode développeur** (ou lance un terminal administrateur) : le lien symbolique est-il créé maintenant ?

## Expériences (hors sortie attendue)

1. Pourquoi `readPassword` rend-il un `char[]`, et pas une `String` ?
2. `Files.delete` sur un lien symbolique : supprime-t-il le lien, ou la cible ?
3. `Files.walk(box, FileVisitOption.FOLLOW_LINKS)` avec un lien qui pointe vers un dossier parent : que se passe-t-il ? (Indice : `FileSystemLoopException`.)

## Sortie attendue complète

```
D01 : coherent true, walk avec FOLLOW_LINKS voit plus true
D02 : posix coherent true, vue basic toujours presente true
D03 : 7 true 2
D04 : entree simulee Ada true efface true
```

## Carte mémoire (à lire **après** le drill)

<details><summary>Ouvrir la carte</summary>

- **Les liens symboliques :**
  - `Files.createSymbolicLink(lien, cible)`, `isSymbolicLink`, `readSymbolicLink` ;
  - la plupart des méthodes **suivent** le lien, sauf avec `LinkOption.NOFOLLOW_LINKS` ;
  - `Files.walk` ne les suit **pas**, sauf avec `FileVisitOption.FOLLOW_LINKS` (une boucle lève alors `FileSystemLoopException`) ;
  - `delete` supprime le **lien**, pas la cible.
- **Les vues d'attributs :**
  - `basic` (partout) ;
  - `posix` (Unix : propriétaire, groupe, permissions `rwxr-x---`) ;
  - `dos` (Windows : caché, archive, lecture seule).
  
  `supportedFileAttributeViews()` dit lesquelles existent.
- **Par nom :** `Files.readAttributes(p, "basic:size,lastModifiedTime")` rend une `Map` ; `Files.getAttribute(p, "size")` ; `Files.setAttribute(p, "lastModifiedTime", time)`.
- **`Console`** (`System.console()`, qui peut être `null`) :
  - `readLine([format, args])` ;
  - `readPassword([format, args])` : un `char[]`, sans écho ;
  - `writer()` (un `PrintWriter`), `reader()`, `format`/`printf`, `flush` ;
  - aucune exception vérifiée.
  
  Efface le mot de passe après usage : `Arrays.fill(tableau, '\0')`.

</details>
