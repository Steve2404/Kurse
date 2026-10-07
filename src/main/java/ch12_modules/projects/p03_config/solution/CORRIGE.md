# Projet 3 — Corrigé étape par étape

> **Quand le lire :** ouvre **seulement la section de l'étape** que tu viens de terminer. Les modules de la correction sont dans `ch12_modules/p03_config/solution/`, et le script dans [`build.sh`](build.sh).
>
> Les messages ci-dessous ont été obtenus en direct avec **JDK 17** (17.0.18), sur une copie de la solution. Chaque message est traduit et expliqué.

---

## Étape 1 — Les modules

```java
module config.model {
    exports config.model;                    // accès normal (compilation + exécution)
    opens config.model to config.binder;     // réflexion profonde, pour config.binder seulement
    // config.model.internal et config.model.secret : ni exportés, ni ouverts
}

open module config.legacy {                  // TOUT le module est ouvert à la réflexion, pour tous
    exports config.legacy;
}

module config.binder {
    exports config.binder;                   // il ne requiert pas les modules qu'il inspecte
}

module config.app {
    requires config.model;
    requires config.legacy;
    requires config.binder;
}
```

**`exports` contre `opens` :**

| | `exports p` | `opens p` |
|---|---|---|
| utiliser les types `public` dans le code | oui (compilation + exécution) | non |
| réflexion sur les membres `private` (`setAccessible`) | non | oui (exécution seulement) |

---

## Étape 2 — Le binder

**Le code :** `ch12_modules/p03_config/solution/src/config.binder/config/binder/Binder.java`. Les points clés :
- `type.getDeclaredConstructor()`, `setAccessible(true)`, `newInstance()` créent l'objet, même si le constructeur n'est pas `public` ;
- `getDeclaredField(nom)`, `setAccessible(true)`, `set(objet, convert(texte, field.getType()))` remplissent un champ `private`.

`setAccessible(true)` ne marche **que** si le paquet est **ouvert** au module qui l'appelle (ici `config.binder`).

---

## Étape 3 — Le programme

**Les lignes `isOpen`** : `config.model` est ouvert à `config.binder` (`true`) mais pas à `config.app` (`false`), car l'`opens` est qualifié. `config.legacy`, module ouvert, l'est à tous. `config.model.secret` ne l'est pas, d'où `false`.

**Question — pourquoi le refus du coffre parle-t-il de `exports` ?** Le message complet est :

```
Unable to make public config.model.secret.Vault() accessible: module config.model does not "exports config.model.secret" to module config.binder
```

Le constructeur de `Vault` est **`public`**. Pour rendre accessible un membre **public**, il suffit que le paquet soit **exporté** au module appelant. Java signale donc la directive **la plus faible** qui manque : `exports`. Pour un membre **privé**, il réclamerait `opens`. C'est le cas dans l'expérience 1 ci-dessous.

**Le refus de l'interne :**

```
IllegalAccessError - class config.app.Main (in module config.app) cannot access class config.model.internal.Defaults (in module config.model) because module config.model does not export config.model.internal to module config.app
```

Traduction : « `Main` ne peut pas accéder à `Defaults`, car `config.model` n'exporte pas `config.model.internal` vers `config.app` ». La compilation est passée grâce à `--add-exports` (étape 4), mais le lancement **sans** l'option refuse.

---

## Étape 4 — Le script `build.sh`

**La compilation sans `--add-exports`** échoue (c'est voulu) :

```
error: package config.model.internal is not visible
(package config.model.internal is declared in module config.model, which does not export it)
```

**Expérience 1 — `exports` seul, sans `opens` :** le programme s'arrête **dès le premier `bind`**, sans afficher de `dump` :

```
Exception in thread "main" java.lang.reflect.InaccessibleObjectException: Unable to make field private boolean config.model.ServerConfig.debug accessible: module config.model does not "opens config.model" to module config.binder
```

Le champ `debug` est **privé** : cette fois, il faut `opens`. (C'est `debug` qui échoue en premier, car la `TreeMap` donne les clés dans l'ordre alphabétique.)

**Expérience 2 — `opens config.legacy;` dans un `open module` :**

```
error: 'opens' only allowed in strong modules
```

Traduction : « `opens` n'est permis que dans les modules "forts" (non ouverts) ». Un `open module` est déjà entièrement ouvert : la directive serait redondante, et `javac` la refuse.

**Expérience 3 — retirer `--add-exports` au lancement de l'étape 4 :** la dernière ligne redevient `interne refuse : IllegalAccessError - …`, comme dans le lancement sans option. `--add-exports` doit être donné **à la compilation et à l'exécution** : les deux contrôles sont séparés.

**Les `describe-module` :** `qualified opens config.model to config.binder`, et `contains` pour les deux paquets ni exportés ni ouverts. Pour `config.legacy`, la ligne d'en-tête brute est `config.legacy file:///…/config.legacy/ open` (vérifié) : le mot `open` signale un module ouvert. Le filtre `sed 's/ file:.*//'` l'efface avec le chemin, d'où `config.legacy` seul dans la sortie attendue.
