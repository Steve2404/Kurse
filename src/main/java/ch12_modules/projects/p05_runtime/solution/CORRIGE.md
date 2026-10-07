# Projet 5 — Corrigé étape par étape

> **Quand le lire :** ouvre **seulement la section de l'étape** que tu viens de terminer. Les modules de la correction sont dans `ch12_modules/p05_runtime/solution/`, et le script dans [`build.sh`](build.sh).
>
> Les résultats ci-dessous ont été obtenus en direct avec **JDK 17** (17.0.18), sur Windows.

---

## Étape 1 — Les modules

```java
module inv.core {
    requires java.logging;     // Logger est dans java.logging, pas dans java.base
    exports inv.core;
}

module inv.app {
    requires inv.core;
}
```

**Le sac à dos à la main** (budget 30) : le meilleur choix est `cafe` (12, 30) + `the` (7, 14) + `miel` (11, 25), pour un coût de 30 et une valeur de 69. Aucune autre combinaison de coût ≤ 30 ne dépasse 69.

---

## Étape 2 — Le programme

**Les lignes vérifiées :** `inv.core requiert [java.base, java.logging]` (`java.base` est toujours là, implicitement), et l'ordre de chargement `[java.base, java.logging, inv.core, inv.app]` : chaque module apparaît **après** ses dépendances.

**Question — pourquoi `version 1.2` seulement depuis le jar ?** La version est inscrite dans le descripteur du module par `jar --create … --module-version 1.2`. Un module compilé dans un dossier n'en a pas. Vérifié, en lançant depuis `$OUT/mods` :

```
inv.app : paquets [inv.app], classe principale aucune, version aucune
```

Depuis le dossier, il n'y a **ni** version **ni** classe principale : les deux viennent du jar.

---

## Étape 3 — Le script `build.sh`

**`jdeps -s -R`** : `inv.app -> inv.core`, `inv.app -> java.base`, `inv.core -> java.base`, `inv.core -> java.logging`. **`--print-module-deps`** rend la liste à donner à `jlink` : `inv.core,java.base`.

**Question — pourquoi 4 modules seulement ?** `jlink` part de `inv.app` et n'ajoute que les modules **requis**, de proche en proche : `inv.core`, puis `java.logging` et `java.base`. Le JDK complet en a **71** (vérifié avec `java --list-modules`). L'image pèse environ **38 Mo**.

**Question — que contient `$OUT/image/bin` ?** Vérifié sous Windows :
- les lanceurs créés par `--launcher` : `inventaire` (script pour bash) et `inventaire.bat` (pour Windows) ;
- `java.exe` et `javaw.exe`, `keytool.exe`, et des bibliothèques `.dll` ;
- **pas** de `javac` : une image d'exécution sert à **lancer**, pas à compiler.

**Expérience — `--bind-services` :** l'image passe de 4 à **40** modules, et d'environ 38 Mo à 120 Mo (vérifié). `java.base` déclare des `uses` (services) : avec cette option, `jlink` ajoute aussi **tous les modules qui fournissent** ces services (projet 2), même si personne ne les requiert.
