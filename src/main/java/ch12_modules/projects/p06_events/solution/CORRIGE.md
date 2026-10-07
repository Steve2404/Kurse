# Projet 6 (capstone) — Corrigé étape par étape

> **Quand le lire :** ouvre **seulement la section de l'étape** que tu viens de terminer. Les fichiers de la correction sont dans `ch12_modules/p06_events/solution/`, et le script dans [`build.sh`](build.sh).
>
> Les résultats ci-dessous ont été obtenus en direct avec **JDK 17** (17.0.18), sur la solution et sur une copie.

---

## Étape 1 — Le jar hérité et le modèle

```java
module events.model {
    exports events.model;
}

module events.api {
    requires transitive events.model;    // Notifier.send montre Attendee et Event
    exports events.api;
}
```

Le jar hérité `geo-tools-1.0.jar`, sans `module-info`, devient le **module automatique** `geo.tools` (projet 4).

---

## Étape 2 — Le cœur

```java
module events.core {
    requires transitive events.api;
    requires geo.tools;                           // le module automatique
    exports events.core;
    exports events.core.internal to events.app;   // export qualifié
    opens events.core.state to events.audit;      // ouverture qualifiée
    uses events.api.Notifier;                     // le localisateur
}
```

**L'allocation à la main** : E1 (9 h–10 h) salle 1 ; E2 (9 h 30) salle 2 ; E3 (10 h) réutilise la salle 1, libérée à 10 h ; E4 (10 h 15) : les salles 1 et 2 sont occupées, d'où la salle 3 ; E5 (11 h) reprend la plus petite libre, la 2 ; E6 (11 h 30) reprend la 1. Résultat : `{E1=1, E2=2, E3=1, E4=3, E5=2, E6=1}`.

---

## Étape 3 — Fournisseurs, audit, application

**Question — pourquoi `events.app` lit-il `events.model` sans le requérir ?** Par une **chaîne** de `requires transitive` : `events.app` requiert `events.core`, qui requiert **transitivement** `events.api`, qui requiert **transitivement** `events.model`. La lisibilité se propage le long de la chaîne, d'où `events.app lit events.model true`.

---

## Étape 4 — Le script `build.sh`

**Les lignes clés de la sortie :**
- `--limit-modules events.app,events.email` retire `events.sms` : `canaux : [email]`, puis `Bob : pas de canal sms` ;
- `describe-module events.core` montre chaque directive, dont `qualified opens events.core.state to events.audit` et `uses events.api.Notifier` ;
- `jlink` refuse : `Error: automatic module cannot be used with jlink: geo.tools` (« un module automatique ne peut pas être utilisé avec `jlink` »). Un module automatique n'a pas de descripteur fiable, et `jlink` exige que **tous** les modules soient nommés.

**Question — comment rendre l'application « jlinkable » ?** Par une migration **du bas vers le haut** (*bottom-up*) : on donne à `geo-tools` son propre `module-info.java` :

```java
module geo.tools {
    exports com.geo;
}
```

Vérifié : après avoir compilé ce module dans un jar `geo.tools.jar` (à la place de l'ancien), `jlink --add-modules events.app` **réussit**, et l'image contient `geo.tools` comme module nommé.

**Attention, piège vérifié :** cette image affiche `canaux : []`. Les fournisseurs `events.email` et `events.sms` ne sont **requis** par personne : `jlink` ne les ajoute pas. Il faut les nommer : `--add-modules events.app,events.email,events.sms`, et l'image affiche alors `canaux : [email, sms]`. Ou bien utiliser `--bind-services` (projet 5).
