# Projet 1 — Corrigé étape par étape

> **Quand le lire :** ouvre **seulement la section de l'étape** que tu viens de terminer, pour comparer ton code et tes réponses. Les modules de la correction sont dans `ch12_modules/p01_library/solution/` (à la racine du dépôt), et le script dans [`build.sh`](build.sh).
>
> Les messages ci-dessous ont été obtenus en direct avec **JDK 17** (17.0.18), sur une copie de la solution. `javac` écrit en anglais : chaque message est traduit et expliqué.

---

## Étape 1 — Les trois modules

**Les trois `module-info.java` :**

```java
module library.model {
    exports library.model;
}

module library.service {
    requires transitive library.model;
    exports library.service;
    exports library.service.internal to library.app;
}

module library.app {
    requires library.service;
}
```

**Question — pourquoi `requires transitive` ?** `Catalog.byAuthor` rend une `List<Book>`, et `Book` est déclaré dans `library.model`. `Main` utilise donc `Book` (`Book::title`), alors que `library.app` ne déclare que `requires library.service`. Avec `transitive`, tout module qui lit `library.service` lit **aussi** `library.model`.

Sans `transitive` (vérifié), `javac` refuse `Main` :

```
error: package library.model is not visible
  (package library.model is declared in module library.model, but module library.app does not read it)
```

Traduction : « le paquet `library.model` n'est pas visible : il est déclaré dans le module `library.model`, mais le module `library.app` ne le lit pas ».

**La règle :** si l'API publique d'un module **montre** les types d'un autre module (en paramètre ou en retour), il doit le requérir en `transitive`.

---

## Étape 2 — Le programme

**Le code :** `ch12_modules/p01_library/solution/src/library.app/library/app/Main.java`.

**Les deux dernières lignes, expliquées :**
- `isExported("library.service.internal", app)` vaut `true` : l'export qualifié vise `library.app` ;
- vers `library.model`, il vaut `false` : il n'est pas dans la liste du `to` ;
- `isExported("library.service.internal")` (sans cible) vaut `false` : le paquet n'est **pas** exporté à tout le monde ;
- `app.canRead(model)` vaut `true` **grâce au `transitive`** de l'étape 1 ;
- `model.canRead(app)` vaut `false` : `library.model` ne requiert rien (à part `java.base`, implicite).

`isNamed()` vaut `true` : un module déclaré par un `module-info` est **nommé**. Le code lancé sans module path est dans le module **anonyme** (*unnamed*), et `isNamed()` y vaut `false`.

---

## Étape 3 — L'intrus

**Le message attendu** (dernière ligne de la sortie) :

```
error: package library.service.internal is not visible
(package library.service.internal is declared in module library.service, which does not export it to module library.intruder)
```

Traduction : « le paquet `library.service.internal` n'est pas visible : il est déclaré dans `library.service`, qui ne l'exporte pas au module `library.intruder` ». C'est la preuve que l'export qualifié de l'étape 1 ne s'ouvre **qu'à** `library.app`. `requires` ne suffit pas : il faut **aussi** que le paquet soit exporté vers le module qui lit.

---

## Étape 4 — Le script `build.sh`

**Le script :** [`build.sh`](build.sh), commenté ligne par ligne.

**Expérience 1 — retirer `transitive` :** voir l'étape 1 (`package library.model is not visible`).

**Expérience 2 — retirer `library.model` de `-m` :** il est **quand même compilé**. Vérifié : le dossier de sortie contient `library.app`, `library.model` et `library.service`. `javac` trouve dans `--module-source-path` les modules **requis** par ceux qu'on lui demande, et les compile aussi.

**Expérience 3 — `java -p "$OUT/mods" -m library.app`, sans classe :**

```
module library.app does not have a ModuleMainClass attribute, use -m <module>/<main-class>
```

En français (`-Duser.language=fr`) : `le module library.app n'a pas d'attribut MainClass, utilisez -m <module>/<main-class>`. Un module compilé dans un dossier ne sait pas quelle est sa classe principale. C'est `jar --create … --main-class library.app.Main` qui l'inscrit dans le descripteur du module. C'est pourquoi `java -p "$OUT/jars" -m library.app` marche ensuite **sans** nom de classe.

**Les lignes de `--describe-module` :**
- `requires java.base mandated` : `java.base` est requis **implicitement** par tout module ;
- `qualified exports … to library.app` : l'export qualifié ;
- dans le jar : `main-class library.app.Main`, et `contains library.app` (un paquet présent mais **non** exporté).
