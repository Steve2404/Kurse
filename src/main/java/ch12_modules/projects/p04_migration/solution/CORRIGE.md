# Projet 4 — Corrigé étape par étape

> **Quand le lire :** ouvre **seulement la section de l'étape** que tu viens de terminer. Les fichiers de la correction sont dans `ch12_modules/p04_migration/solution/`, et le script dans [`build.sh`](build.sh).
>
> Les messages ci-dessous ont été obtenus en direct avec **JDK 17** (17.0.18), sur la solution et sur une copie. Chaque message est traduit et expliqué.

---

## Étape 1 — Le vieux code (sans `module-info`)

**Le code :** `legacy/com/acme/…` dans la solution, et `manifest.txt` :

```
Automatic-Module-Name: com.acme.utils
```

**La ligne `classpath`** (vérifiée) : `classpath : bonjour-le-monde ; module nomme false, nom null`. Lancé avec `-cp`, tout le code est dans le **module sans nom** : `isNamed()` vaut `false`, et `getName()` vaut `null`.

---

## Étape 2 — Le module nommé et le cycle

```java
module blog.app {
    requires acme.text;          // nom déduit du fichier acme-text-2.1.jar
    requires com.acme.utils;     // nom lu dans le manifeste du jar old_utils.jar
}
```

**Les trois lignes de modules** (vérifiées) :
- `Main` est dans `blog.app`, module **nommé** : non automatique, et il **ne lit pas** le module sans nom ;
- `Slugify` et `Strings` sont dans des modules **automatiques** : ils lisent **tous** les modules, y compris le module sans nom.

**Le cycle :**

```
error: cyclic dependence involving cycle.a
error: cyclic dependence involving cycle.b
```

Traduction : « dépendance cyclique impliquant `cycle.a` ». Deux modules ne peuvent pas se requérir l'un l'autre, même indirectement.

---

## Étape 3 — Le script `build.sh`

**Le script :** [`build.sh`](build.sh).

**Les `describe-module` des jars :** `No module descriptor found. Derived automatic module.` (« aucun descripteur de module : module automatique déduit »), puis `acme.text@2.1 automatic` : le nom **et** la version viennent du nom du fichier `acme-text-2.1.jar`. Un module automatique **exporte tous** ses paquets.

**La compilation sans module path :**

```
error: module not found: acme.text
error: module not found: com.acme.utils
```

« Module introuvable » : sans `-p`, `javac` ne voit pas les jars.

**Question — pourquoi pas `old.utils` ?** Parce que le manifeste contient `Automatic-Module-Name: com.acme.utils`. Ce nom **l'emporte** sur le nom déduit du fichier. Sans lui, `old_utils.jar` aurait donné `old.utils` (le `_` devient un `.`).

**Question — dans quel module vit `Demo` ?** Dans le **module sans nom** : il est lancé depuis le classpath. Vérifié : même lancé avec `-p` en plus, `Demo` affiche toujours `module nomme false, nom null`. Un module nommé ne peut **pas** requérir le module sans nom : celui-ci n'a pas de nom à écrire dans `requires`. C'est pourquoi la migration se fait **du haut vers le bas** (*top-down*) : l'application devient un module, et ses vieilles dépendances deviennent des modules automatiques.

**Expérience 1 — renommer en `acme_text.jar` :** le module s'appelle `acme.text`, **sans** version (vérifié : `acme.text automatic`). Le `_` est remplacé par un `.`, et il n'y a plus de numéro à lire.

**Expérience 2 — `acme-text-2.1.jar` sur le classpath :**

```
error: module not found: acme.text
```

Sur le classpath, le jar entre dans le **module sans nom**, que `blog.app` ne peut pas requérir. Il doit être sur le **module path** pour devenir le module automatique `acme.text`.

**`jdeps -s`** résume les dépendances : `acme-text-2.1.jar -> java.base`, puis les trois dépendances de `blog.app`.
