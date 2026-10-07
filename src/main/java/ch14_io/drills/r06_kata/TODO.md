# Drill de rappel 6 — Kata : flux standard, `Console`, choisir la bonne classe

> Première fois ? Lis d'abord le mode d'emploi [`ch14_io/PARCOURS.md`](../../PARCOURS.md).

**Chrono cible :** 10 min, puis 5 min.

**Règles :**
- Tout se fait de mémoire, **imports compris**.
- Crée la classe **`Recall06`** dans le paquet `ch14_io.drills.r06_kata`. Le `main` déclare `throws IOException`. Elle a :
  - `static int sum(InputStream in)` : un `Scanner` sur `in`, qui additionne tant que `hasNextInt()` ;
  - `static int vowels(Reader r) throws IOException` : compte les voyelles (`aeiouy`, sans tenir compte de la casse), caractère par caractère.

**Les notions de ce drill ont été apprises dans :** projets 1 à 7 : c'est le test final du chapitre. Si un défi te semble totalement inconnu, ce drill arrive trop tôt : refais d'abord la leçon de ce projet.

<details><summary><b>Comment faire ce drill, concrètement</b> (à lire la 1re fois)</summary>

1. **Note l'heure** de départ (montre ou téléphone).
2. **Crée la classe** : clic droit sur le dossier `r06_kata` → **New** → **Java Class** → `Recall06`. S'il faut d'autres classes, place-les comme le disent les **Règles** ci-dessus ; si elles ne précisent rien, écris-les dans le même fichier, sous `Recall06`, sans `public`.
3. **Écris `main`**, puis **une ligne par défi**. La ligne qui suit la flèche `→` est **exactement** ce que ton programme doit afficher : `System.out.println("D01 : " + …);`, où les `…` sont des valeurs que **Java** calcule, jamais recopiées.
4. **Lance `Recall06`** avec la flèche verte, pour voir tes lignes.
5. **Bloqué plus de 3 minutes sur un défi ?** Écris un commentaire `// D03 : ✗` à sa place, et passe au suivant.
6. **Lance `Check.java`** (flèche verte). Une ligne `[FAIL]` te montre `attendu` et `obtenu`.
7. **Ensuite seulement**, ouvre la **carte mémoire** tout en bas (en aperçu Markdown, clique sur le triangle « Ouvrir la carte »), relis tes ✗, et fais les **expériences** (écris la ligne, compile, lis le message, efface la ligne).
8. **Note** la date, ton temps et tes ✗ dans le tableau de [`drills/README.md`](../README.md).

</details>

## Défis

- ☐ **D01.** `sum` d'un `ByteArrayInputStream` de `"4 8 15 16 23 42"` (en UTF-8), puis `vowels(new StringReader("Entrees Sorties"))`.
  → `D01 : 108 6`
- ☐ **D02.** Redirige `System.out` vers un `PrintStream` sur un `ByteArrayOutputStream` (avec `System.setOut`), affiche `capture`, puis **restaure** l'original. Affiche le contenu capturé.
  → `D02 : capture`
- ☐ **D03.** `Console console = System.console()` : rend `"pas de console"` si elle vaut `null`, sinon `"console presente"`. Affiche si la valeur est l'une des deux.
  → `D03 : true`
- ☐ **D04.** `k.txt` = `"ligne 1\nligne 2\nligne 3"`, dans `build/ch14/r06_kata`. Affiche :
  - la 2e ligne (`Files.lines` avec `skip(1).findFirst()`, dans un try-with-resources) ;
  - `readString(…).length()` ;
  - `Files.size(…)`.
  → `D04 : ligne 2 23 23`

## Expériences (hors sortie attendue)

1. Dans un vrai terminal, utilise `console.readLine("Nom ? ")` et `console.readPassword("Code ? ")`. Pourquoi `readPassword` rend-il un `char[]`, et pas une `String` ?
2. Lance D03 depuis l'IDE, puis depuis un terminal : `System.console()` change-t-il ?
3. Ferme `System.in` (avec un try-with-resources sur un `Scanner`), puis relis : que se passe-t-il ?

## Sortie attendue complète

```
D01 : 108 6
D02 : capture
D03 : true
D04 : ligne 2 23 23
```

## Carte mémoire (à lire **après** le drill)

<details><summary>Ouvrir la carte</summary>

- **Les flux standard :**
  - `System.in` est un `InputStream` ;
  - `System.out` et `System.err` sont des `PrintStream` ;
  - on peut les remplacer avec `System.setIn`, `System.setOut`, `System.setErr`.
- **Ne ferme pas `System.in`** (ni un `Scanner` dessus) si tu dois relire plus tard.
- **`Console`** (`System.console()`, qui peut être `null`) :
  - `readLine()`, `readLine(format, args)` ;
  - `readPassword()`, qui rend un `char[]` à **effacer** après usage ;
  - `format()`/`printf()` ;
  - `writer()` (un `PrintWriter`) et `reader()` (un `Reader`) ;
  - pas d'exception vérifiée.
- **Choisir sa classe :**
  - du texte ligne à ligne → `BufferedReader`, ou `Files.lines` ;
  - un fichier entier → `readString` / `readAllLines` ;
  - du binaire → `InputStream` ;
  - des objets → `ObjectInputStream` ;
  - du formaté → `PrintWriter`.

</details>
