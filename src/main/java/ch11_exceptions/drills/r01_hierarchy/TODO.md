# Drill de rappel 1 — La hiérarchie des exceptions

> Première fois ? Lis d'abord le mode d'emploi [`ch11_exceptions/PARCOURS.md`](../../PARCOURS.md).

**Chrono cible :** 10 min, puis 5 min.

**Règles :**
- Tout se fait de mémoire, **imports compris**.
- Crée la classe **`Recall01`** dans le paquet `ch11_exceptions.drills.r01_hierarchy`.
- Dans le même fichier, crée la classe package-private **`Fragile`** : `static final int VALUE = Integer.parseInt("oops");`.

**Les notions de ce drill ont été apprises dans :** projet 1 (étapes 1 et 3) et projet 2 (étape 4). Si un défi te semble totalement inconnu, ce drill arrive trop tôt : refais d'abord la leçon de ce projet.

<details><summary><b>Comment faire ce drill, concrètement</b> (à lire la 1re fois)</summary>

1. **Note l'heure** de départ (montre ou téléphone).
2. **Crée la classe** : clic droit sur le dossier `r01_hierarchy` → **New** → **Java Class** → `Recall01`. S'il faut d'autres classes, place-les comme le disent les **Règles** ci-dessus ; si elles ne précisent rien, écris-les dans le même fichier, sous `Recall01`, sans `public`.
3. **Écris `main`**, puis **une ligne par défi**. La ligne qui suit la flèche `→` est **exactement** ce que ton programme doit afficher : `System.out.println("D01 : " + …);`, où les `…` sont des valeurs que **Java** calcule, jamais recopiées.
4. **Lance `Recall01`** avec la flèche verte, pour voir tes lignes.
5. **Bloqué plus de 3 minutes sur un défi ?** Écris un commentaire `// D03 : ✗` à sa place, et passe au suivant.
6. **Lance `Check.java`** (flèche verte). Une ligne `[FAIL]` te montre `attendu` et `obtenu`.
7. **Ensuite seulement**, ouvre la **carte mémoire** tout en bas (en aperçu Markdown, clique sur le triangle « Ouvrir la carte »), relis tes ✗, et fais les **expériences** (écris la ligne, compile, lis le message, efface la ligne).
8. **Note** la date, ton temps et tes ✗ dans le tableau de [`drills/README.md`](../README.md).

</details>

## Défis

- ☐ **D01.** Écris `static String chain(Class<?> type)`. Elle remonte `getSuperclass()` jusqu'à `Object` (exclu) et joint les noms simples par ` > `. Applique-la à `NumberFormatException.class`.
  → `D01 : NumberFormatException > IllegalArgumentException > RuntimeException > Exception > Throwable`
- ☐ **D02.** `chain(FileNotFoundException.class)`.
  → `D02 : FileNotFoundException > IOException > Exception > Throwable`
- ☐ **D03.** `chain(StackOverflowError.class)`.
  → `D03 : StackOverflowError > VirtualMachineError > Error > Throwable`
- ☐ **D04.** Écris `static String kind(Throwable t)`, qui rend `erreur`, `non verifiee` ou `verifiee` (avec `instanceof`). Applique-la, séparée par `, `, à :
  - `new IOException()` ;
  - `new ParseException("x", 0)` ;
  - `new DateTimeParseException("x", "t", 0)` ;
  - `new ArithmeticException()` ;
  - `new AssertionError()` ;
  - `new Exception()`.
  → `D04 : verifiee, verifiee, non verifiee, non verifiee, erreur, verifiee`
- ☐ **D05.** Lis `Fragile.VALUE` deux fois, chaque fois dans son propre `try` :
  - le 1er attrape `ExceptionInInitializerError` et note `<nom simple> cause <nom simple de la cause>` ;
  - le 2e attrape `NoClassDefFoundError` et note son nom simple.
  → `D05 : ExceptionInInitializerError cause NumberFormatException ; NoClassDefFoundError`
- ☐ **D06.** `Object text = "java";`, puis `(Integer) text`. Attrape l'exception, et affiche son nom simple et `instanceof RuntimeException`.
  → `D06 : ClassCastException true`

## Expériences (hors sortie attendue)

1. `catch (FileNotFoundException e)` autour d'un bloc qui ne peut pas la lever : compile-t-il ? Et `catch (IllegalStateException e)` ?
2. `catch (Exception e) { } catch (IOException e) { }` : quelle erreur ?
3. Une méthode avec `throw new Exception();` sans `throws` : quelle erreur ? Et avec `throw new RuntimeException();` ?
4. `static void f() throws Error` : est-ce permis ? Est-ce utile ?

## Sortie attendue complète

```
D01 : NumberFormatException > IllegalArgumentException > RuntimeException > Exception > Throwable
D02 : FileNotFoundException > IOException > Exception > Throwable
D03 : StackOverflowError > VirtualMachineError > Error > Throwable
D04 : verifiee, verifiee, non verifiee, non verifiee, erreur, verifiee
D05 : ExceptionInInitializerError cause NumberFormatException ; NoClassDefFoundError
D06 : ClassCastException true
```

## Carte mémoire (à lire **après** le drill)

<details><summary>Ouvrir la carte</summary>

```
Throwable
├── Error                      (non verifiee : StackOverflowError, ExceptionInInitializerError, NoClassDefFoundError, AssertionError...)
└── Exception                  (VERIFIEE : IOException, FileNotFoundException, ParseException, SQLException...)
    └── RuntimeException       (non verifiee : Arithmetic, ArrayIndexOutOfBounds, ClassCast, NullPointer,
                                IllegalArgument > NumberFormat, IllegalState, UnsupportedOperation, DateTimeException...)
```

- **Vérifiée** = `Exception` hors `RuntimeException`. Il faut la **traiter** (`catch`) ou la **déclarer** (`throws`).
- **Un `catch` d'exception vérifiée** que le `try` ne peut pas lever : erreur de compilation. Sauf `Exception` et `Throwable`.
- **`ExceptionInInitializerError`** : un initialiseur `static` a échoué. Au 2e accès, c'est `NoClassDefFoundError`.

</details>
