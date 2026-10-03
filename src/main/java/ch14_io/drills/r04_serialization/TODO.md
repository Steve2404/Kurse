# Drill de rappel 4 — La sérialisation

> Première fois ? Lis d'abord le mode d'emploi [`ch14_io/PARCOURS.md`](../../PARCOURS.md).

**Chrono cible :** 12 min, puis 6 min.

**Règles :**
- Tout se fait de mémoire, **imports compris**.
- Crée la classe **`Recall04`** dans le paquet `ch14_io.drills.r04_serialization`. Le `main` déclare `throws IOException, ClassNotFoundException`. Elle a une méthode `static byte[] save(Object... objects)`, qui les écrit tous dans un `ObjectOutputStream` sur un `ByteArrayOutputStream`.
- Dans le même fichier, crée :
  - **`Account implements Serializable`** : `serialVersionUID = 1L`, `static String bank`, `final String owner`, `final int balance`, `transient String pin` ;
  - **`Base`** (non sérialisable) : `static int calls`, `int baseValue = 1`, et un constructeur qui fait `calls++` ;
  - **`Child extends Base implements Serializable`** : `int childValue`, `transient boolean initialized = true` ;
  - **`Unsafe implements Serializable`**, avec un champ `Object notSerializable = new Object()` ;
  - **`record Point(int x, int y) implements Serializable`** : `static int built`, et un constructeur compact qui fait `built++`.

## Défis

- ☐ **D01.**
  1. `a = new Account("ana", 100, "secret")`, puis `Account.bank = "Banque A"` ;
  2. `save(a, "suite", 7)` ;
  3. `Account.bank = "Banque B"` ;
  4. relis l'objet.
  
  Affiche `owner`, `balance`, `pin`, `Account.bank`, `a == b`, puis les deux objets suivants.
  → `D01 : ana 100 null Banque B false suite 7`
- ☐ **D02.** Un `readObject()` de plus.
  → `D02 : EOFException`
- ☐ **D03.** `Base.calls = 0`, puis `c = new Child()`, avec `baseValue = 5` et `childValue = 9`. Note `calls`, sauve, puis relis. Affiche :
  - `calls` avant, puis après ;
  - `baseValue`, `childValue` et `initialized` de la copie.
  → `D03 : 1 2 1 9 false`
- ☐ **D04.** `save(new Unsafe())`.
  → `D04 : NotSerializableException`
- ☐ **D05.** `Point.built = 0`, puis `save(new Point(1, 2), List.of(new Point(3, 4)))`, puis relis les deux. Affiche le point, la liste, puis `Point.built`.
  → `D05 : Point[x=1, y=2] [Point[x=3, y=4]] 4`

## Expériences (hors sortie attendue)

1. Retire le constructeur sans argument de `Base` (donne-lui un paramètre) : que se passe-t-il à la relecture ?
2. Une classe avec un champ `Thread` non `transient` : erreur à l'écriture ou à la lecture ?
3. Un `final` non `transient` est-il relu ? Et un `transient final` ?

## Sortie attendue complète

```
D01 : ana 100 null Banque B false suite 7
D02 : EOFException
D03 : 1 2 1 9 false
D04 : NotSerializableException
D05 : Point[x=1, y=2] [Point[x=3, y=4]] 4
```

## Carte mémoire (à lire **après** le drill)

<details><summary>Ouvrir la carte</summary>

- **`Serializable`** : une interface marqueur (aucune méthode). Tous les champs non `transient` doivent être sérialisables, sinon `NotSerializableException` **à l'écriture**.
- **Ne sont pas écrits :** les champs `static` (ils appartiennent à la classe) et `transient` (relus à 0, `false` ou `null`).
- **À la relecture**, aucun constructeur des classes sérialisables ne s'exécute, ni leurs initialisations de champs (`initialized = true` n'est pas rejoué). Seul le constructeur sans argument de la **première classe mère non sérialisable** s'exécute (il doit exister et être accessible) : ses champs repartent de leur valeur initiale.
- **Un record** est relu par son constructeur **canonique** (les validations s'exécutent).
- **`readObject()`** rend `Object` ; il lève `ClassNotFoundException` (vérifiée), et `EOFException` à la fin.
- **`serialVersionUID`** : s'il diffère entre l'écriture et la lecture, `InvalidClassException`.

</details>
