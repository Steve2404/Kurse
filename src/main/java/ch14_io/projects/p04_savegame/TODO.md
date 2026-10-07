# Projet 4 — La sauvegarde de partie (sérialisation)

> Première fois ? Lis d'abord le mode d'emploi [`ch14_io/PARCOURS.md`](../../PARCOURS.md).
>
> **Bloqué sur une étape ?** [`INDICES.md`](INDICES.md) donne deux indices repliés par étape, sans code. **Étape finie ?** [`solution/CORRIGE.md`](solution/CORRIGE.md) donne, étape par étape, le code de l'étape, les **réponses aux questions** et le résultat exact des **expériences**. N'ouvre que la section de l'étape que tu viens de faire.

**Notions visées (chapitre 14) :**
- **`Serializable`** : une interface **sans méthode** ; `serialVersionUID` ;
- **`ObjectOutputStream.writeObject`** et **`ObjectInputStream.readObject`** : `readObject` rend un `Object` (il faut caster), et lève `ClassNotFoundException` (vérifiée) ;
- **ce qui n'est pas écrit :**
  - les champs **`transient`** (relus à 0, `false` ou `null`) ;
  - les champs **`static`** ;
- **les constructeurs à la relecture :**
  - aucun constructeur d'une classe sérialisable ne s'exécute, **sauf** le constructeur **sans argument** de la première classe mère **non** sérialisable ;
  - un **record** passe par son constructeur **canonique** ;
- **`NotSerializableException`** ; **`EOFException`** quand on lit au-delà de la fin ;
- **plusieurs objets** dans un même flux, relus dans le **même ordre** ; `writeInt`/`readInt` ;
- `ByteArrayOutputStream`/`ByteArrayInputStream` (un flux en mémoire).

Côté algorithme : une **pile d'annulation**. Avant chaque action, on empile une **copie profonde** de l'état, obtenue par sérialisation puis désérialisation en mémoire.

**Ce que TU crées :** dans `ch14_io.projects.p04_savegame` :
- `Entity`, le record `Item` et `Hero` ;
- **`SaveGame`** (le `main`).

**Règle du crescendo :** chapitres 1 à 14.

**Tes outils pour ce projet** (pas d'arguments, `Data.java` donné) :

```
javac -d build/ch14-p04 -sourcepath src/main/java src/main/java/ch14_io/projects/p04_savegame/SaveGame.java
java "-Duser.language=fr" -cp build/ch14-p04 ch14_io.projects.p04_savegame.SaveGame
```

---

## Tableau de bord

### ☐ Étape 1 — Les classes

**📖 La leçon : la sérialisation, sauver un objet entier.** Un objet d'une classe qui réalise `Serializable` peut être écrit **tel quel** dans un flux, avec tous ses champs (et les objets qu'ils contiennent), puis relu plus tard :

```java
class Gateau implements Serializable {
    private static final long serialVersionUID = 1L;   // le numéro de version du format
    String nom = "tarte";
    transient int cuisson = 40;                        // transient : NON sauvegardé
    List<String> ingredients = new ArrayList<>(List.of("farine"));
}

try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("g.ser"))) {
    out.writeObject(new Gateau());
}
try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("g.ser"))) {
    Gateau g = (Gateau) in.readObject();     // un cast : readObject rend un Object
    // g.nom = "tarte", g.cuisson = 0 (transient), g.ingredients = [farine]
}
```

`readObject` lance aussi une `ClassNotFoundException` (vérifiée) : déclare-la.

**👉 À toi :**

- **`Entity`** (non sérialisable) :
  - `static int constructions` et `protected String origin` ;
  - son constructeur sans argument incrémente `constructions`, et met `origin = "neuf"`.
- **`record Item(String name, int power) implements Serializable`** :
  - `static int validations` ;
  - le constructeur compact incrémente `validations`, et lève `IllegalArgumentException` si la puissance est négative.
- **`Hero extends Entity implements Serializable`** :
  - `private static final long serialVersionUID = 1L` et `static int heroes` ;
  - `name`, `level = 1`, une `List<Item> inventory` (une `ArrayList`) et `transient int sessionMinutes` ;
  - le constructeur `(String name)` fait `heroes++` et met `origin = "cree par le constructeur de Hero"` ;
  - les méthodes `play(int minutes)`, `levelUp()`, `loot(Item)`, `drop(String)` (avec `removeIf`) et `inventory()` ;
  - `toString()` rend `<nom> niv <niveau> [<objet>(<puissance>), ...] session <m> min, origine <origin>`.

### ☐ Étape 2 — Sauver, relire

```
sauve  : Ayla niv 1 [baton(2)] session 45 min, origine cree par le constructeur de Hero
relu   : Ayla niv 1 [baton(2)] session 0 min, origine neuf ; suite fin de sauvegarde 42 puis EOFException
constructeurs : Entity 1 (sans argument, classe mere non serialisable), Item 1 (record), Hero.heroes reste 99 (static), meme objet false
ecriture refusee : NotSerializableException (java.lang.Object)
```

**📖 Rappel :** `writeObject` et `readObject` dans le même ordre (étape 1). Lire après la fin lance une `EOFException` (chapitre 11 pour le `try`/`catch`).

**👉 À toi :**

- **L'écriture :** `createDirectories(Data.SANDBOX)`, puis le fichier `partie.ser`.
  1. Un héros `Ayla`, avec `loot(new Item("baton", 2))` et `play(45)` ;
  2. note `Entity.constructions` et `Item.validations` ;
  3. écris le héros, la chaîne `"fin de sauvegarde"`, puis `writeInt(42)`.
- **Après l'écriture :** `Hero.heroes = 99`.
- **La relecture**, dans le même ordre :
  1. le héros (avec un cast) ;
  2. la chaîne ;
  3. `readInt()` ;
  4. un `readObject()` de trop → `EOFException`.
- **Les compteurs :** les deux augmentations depuis la note, `Hero.heroes`, puis `loaded == hero`.
- **Le refus :** écris une `List<Object>` contenant `"ok"` et `new Object()` → `catch (NotSerializableException e)` : affiche le nom simple et le message.
- **Questions :**
  - Pourquoi `origin` vaut-il `neuf` à la relecture ?
  - Pourquoi `sessionMinutes` vaut-il 0 ?
  - Que se passerait-il si `Entity` n'avait pas de constructeur sans argument ?

### ☐ Étape 3 — La pile d'annulation

```
loot epee 7     -> Bram niv 1 [epee(7)] (pile 1)
...
undo            -> Bram niv 2 [epee(7), arc(5)] (pile 3)
copie profonde independante : original 2 objets, copie 3
```

**📖 La leçon : des flux en mémoire.** `ByteArrayOutputStream` est un flux qui écrit dans un **tableau d'octets** en mémoire, au lieu d'un fichier ; `toByteArray()` le rend. `ByteArrayInputStream(tableau)` le relit. En les combinant avec la sérialisation, on fabrique une copie complète d'un objet.

**👉 À toi :**

- **`static <T> T deepCopy(T object) throws IOException, ClassNotFoundException`** : `writeObject` vers un `ByteArrayOutputStream`, puis `readObject` depuis un `ByteArrayInputStream`.
- **Un héros `Bram`** et une `Deque<Hero> undo`. Pour chaque action de `Data.ACTIONS` :
  - `undo` : dépile, s'il y a quelque chose ;
  - sinon, `push(deepCopy(game))`, puis l'action (`loot nom puissance`, `level`, `drop nom`).
  - Affiche `String.format("%-16s", action) + "-> " + game.toString()`, sans la partie qui commence à ` session` (`replaceAll(" session.*", "")`), puis ` (pile <taille>)`.
- **La fin :** une copie profonde de l'état final, plus une potion ; compare les tailles des inventaires.
- **Expérience :** change `serialVersionUID` à 2 et relis un `partie.ser` écrit avec 1. Quelle exception ?

---

## Checklist (vérifiée par `Check`)

- `Data.SANDBOX`, `Data.ACTIONS` ;
- `implements Serializable`, `serialVersionUID`, `transient`, `record Item(` ;
- `new ObjectOutputStream(`, `new ObjectInputStream(`, `.writeObject(`, `.readObject()`, `.writeInt(`, `.readInt()` ;
- `catch (EOFException`, `catch (NotSerializableException` ;
- `ByteArrayOutputStream`, `ByteArrayInputStream`, `Deque<Hero>`.

---

## Sortie attendue complète

```
sauve  : Ayla niv 1 [baton(2)] session 45 min, origine cree par le constructeur de Hero
relu   : Ayla niv 1 [baton(2)] session 0 min, origine neuf ; suite fin de sauvegarde 42 puis EOFException
constructeurs : Entity 1 (sans argument, classe mere non serialisable), Item 1 (record), Hero.heroes reste 99 (static), meme objet false
ecriture refusee : NotSerializableException (java.lang.Object)
loot epee 7     -> Bram niv 1 [epee(7)] (pile 1)
level           -> Bram niv 2 [epee(7)] (pile 2)
loot bouclier 4 -> Bram niv 2 [epee(7), bouclier(4)] (pile 3)
undo            -> Bram niv 2 [epee(7)] (pile 2)
loot arc 5      -> Bram niv 2 [epee(7), arc(5)] (pile 3)
level           -> Bram niv 3 [epee(7), arc(5)] (pile 4)
drop epee       -> Bram niv 3 [arc(5)] (pile 5)
undo            -> Bram niv 3 [epee(7), arc(5)] (pile 4)
undo            -> Bram niv 2 [epee(7), arc(5)] (pile 3)
level           -> Bram niv 3 [epee(7), arc(5)] (pile 4)
copie profonde independante : original 2 objets, copie 3
```
