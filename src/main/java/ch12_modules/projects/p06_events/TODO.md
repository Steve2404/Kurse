# Projet 6 (CAPSTONE) — La plateforme d'événements

> Première fois ? Lis d'abord le mode d'emploi [`ch12_modules/PARCOURS.md`](../../PARCOURS.md).
>
> **Bloqué sur une étape ?** [`INDICES.md`](INDICES.md) donne deux indices repliés par étape, sans code. **Étape finie ?** [`solution/CORRIGE.md`](solution/CORRIGE.md) donne, étape par étape, le code de l'étape, les **réponses aux questions** et le résultat exact des **expériences**. N'ouvre que la section de l'étape que tu viens de faire.

**Notions visées :** tout le chapitre 12, dans une seule application de **8 modules** (7 à toi, plus un jar hérité) :
- `requires transitive` (deux fois en chaîne), l'**export qualifié**, l'**`opens` qualifié** ;
- un **service** avec deux fournisseurs, l'un classique, l'autre par `provider()` ;
- un **module automatique** (un jar hérité sans `module-info`) ;
- `--limit-modules`, `jar --describe-module`, `jdeps -s -R` ;
- et **`jlink` qui refuse** un module automatique.

Côté algorithmes :
- **l'allocation de salles** par balayage, avec deux tas : les salles **occupées** (par heure de fin) et les salles **libres** (plus petit numéro d'abord) ;
- **la distance** entre villes (haversine), dans le jar hérité ;
- **l'envoi** à chaque participant de l'événement le plus proche, par son canal préféré.

**Ce que TU crées :** dans `ch12_modules/p06_events/` :
- `legacy/com/geo/Distance.java` ;
- tes 7 modules dans `src/` : `events.model`, `events.api`, `events.core`, `events.email`, `events.sms`, `events.audit`, `events.app`.

Et ton script `build.sh`, dans ce dossier.

**Règle du crescendo :** chapitres 1 à 12.

**C'est le projet-bilan du chapitre 12.** Quand tu bloques, relis la leçon d'origine :

| Tu dois… | Leçon à relire |
|---|---|
| `requires transitive`, `exports … to …` | projet 1, étape 1 |
| un service : `uses`, `provides … with …`, `ServiceLoader`, `provider()` | projet 2, étapes 1 et 2 |
| `opens … to …` et la réflexion (`setAccessible`) | projet 3, étapes 1 et 2 |
| un jar hérité utilisé comme module automatique | projet 4, étapes 1 et 3 |
| `--limit-modules` | projet 2, étape 3 |
| `jar --describe-module`, `jdeps -s -R`, `jlink` | projets 1 et 5 |
| deux `PriorityQueue` | chapitre 9, projets 2 et 5 |

**Tes outils pour ce projet :** tes fichiers dans `ch12_modules/p06_events/`, ton script `build.sh` à côté de ce `TODO.md`.

```
& "C:\Program Files\Git\bin\bash.exe" src/main/java/ch12_modules/projects/p06_events/build.sh
```

---

## Tableau de bord

### ☐ Étape 1 — Le jar hérité et le modèle

- **`legacy/com/geo/Distance.java`** (sans module) : `static long km(String from, String to)`, par la formule de **haversine**, avec un rayon de 6371 km, arrondie au km (`Math.round`). Les coordonnées, dans une `Map.of` :
  ```
  Paris 48.8566 2.3522 | Lyon 45.7640 4.8357 | Lille 50.6292 3.0573 | Marseille 43.2965 5.3698 | Bordeaux 44.8378 -0.5792 | Nice 43.7102 7.2620
  ```
  `h = sin²(dLat/2) + cos(lat1)·cos(lat2)·sin²(dLon/2)`, puis `distance = 2 · 6371 · asin(√h)`.
- **`events.model`** (exporte `events.model`) : `record Event(String id, int start, int end, String city)` (minutes depuis minuit) et `record Attendee(String name, String channel, String city)`.
- **`events.api`** : `requires transitive events.model`, exporte `events.api`, et l'interface `Notifier` avec `String channel()` et `String send(Attendee, Event)`.

### ☐ Étape 2 — Le cœur

- **`events.core/module-info.java`** :
  ```
  requires transitive events.api;   requires geo.tools;
  exports events.core;   exports events.core.internal to events.app;
  opens events.core.state to events.audit;   uses events.api.Notifier;
  ```
- **`events.core.state.Counters`** : `private int sent`, `private int missing`, et les méthodes `sent()` et `missing()` qui les incrémentent.
- **`events.core.internal.Rooms.allocate(List<Event>)`** rend une `TreeMap` id → numéro de salle :
  1. trie par début, puis par id ;
  2. avant chaque événement, libère les salles occupées dont la fin est ≤ son début ;
  3. prend la plus petite salle libre, ou ouvre une salle neuve (1, 2, 3…).
- **`events.core.Dispatcher`** :
  - charge les `Notifier` dans une `TreeMap` canal → notifieur (`ServiceLoader.load(…).stream()`) ;
  - `List<String> channels()` et `Counters counters()` ;
  - **`String dispatch(Attendee a, List<Event> events)`** :
    1. cherche l'événement le plus proche de la ville du participant (`Distance.km`, puis l'id à égalité) ;
    2. sans notifieur pour son canal : `missing()`, et rend `<nom> : pas de canal <canal>` ;
    3. sinon : `sent()`, et rend `<send(...)> (<km> km)`.

### ☐ Étape 3 — Fournisseurs, audit, application

| Module | `module-info.java` | Contenu |
|---|---|---|
| `events.email` | `requires events.api;` et `provides events.api.Notifier with events.email.EmailNotifier;` | canal `email`, message `email a <nom> : <id> a <ville>` |
| `events.sms` | `requires events.api;` et `provides events.api.Notifier with events.sms.SmsGateway;` | `SmsGateway.provider()` : canal `sms`, message `sms a <nom> : <id> <start / 60>h` |
| `events.audit` | exporte `events.audit` | `Auditor.inspect(Object)` rend `NomSimple{champ=valeur, ...}` (champs triés, via `setAccessible`) |
| `events.app` | `requires events.core;` et `requires events.audit;` | `events.app.Main` |

- **`Main`** :
  ```java
  static final List<Event> EVENTS = List.of(new Event("E1", 540, 600, "Paris"), new Event("E2", 570, 660, "Lyon"), new Event("E3", 600, 690, "Paris"),
          new Event("E4", 615, 645, "Lille"), new Event("E5", 660, 720, "Marseille"), new Event("E6", 690, 750, "Lyon"));
  static final List<Attendee> ATTENDEES = List.of(new Attendee("Ana", "email", "Bordeaux"), new Attendee("Bob", "sms", "Nice"),
          new Attendee("Chloe", "email", "Lyon"), new Attendee("Dan", "fax", "Paris"));
  ```
  **Les lignes :**
  - `salles : <allocate>` ;
  - `canaux : <channels>` ;
  - pour chaque participant, `  <dispatch>` (deux espaces devant) ;
  - `audit : <inspect(counters)>` ;
  - `lectures : events.app lit events.model <canRead>, Distance dans <les requires de events.core dont le nom commence par geo>`.

### ☐ Étape 4 — Le script `build.sh`

```
--- sans le fournisseur sms
canaux : [email]
  email a Ana : E2 a Lyon (436 km)
  Bob : pas de canal sms
--- jlink
Error: automatic module cannot be used with jlink: geo.tools
```
- **En tête :** `P=ch12_modules/p06_events` et `OUT=build/ch12/p06_events`, puis `mkdir -p "$OUT/jars"`.
- **Les commandes :**
  1. compile `legacy/` (comme au projet 4), puis `jar --create --file "$OUT/jars/geo-tools-1.0.jar" -C "$OUT/legacy" .` ;
  2. `javac -d "$OUT/mods" -p "$OUT/jars" --module-source-path "$P/src" -m events.app,events.email,events.sms` ;
  3. une boucle `for m in events.model events.api events.core events.email events.sms events.audit; do … done`, qui crée `$OUT/jars/$m.jar` ; puis `events.app.jar` avec `--main-class events.app.Main` ;
  4. `echo "--- complet"`, puis `java -p "$OUT/jars" -m events.app` ;
  5. `echo "--- sans le fournisseur sms"`, puis le même lancement avec `--limit-modules events.app,events.email`, et `| sed -n '2,4p'` ;
  6. `echo "--- describe-module events.core"`, puis `jar --describe-module --file "$OUT/jars/events.core.jar" | sed 's/ jar:.*//' | sort` ;
  7. `echo "--- jdeps"`, puis `jdeps -s -R --module-path "$OUT/jars" -m events.app | grep -v "^java\."` ;
  8. `echo "--- jlink"`, puis `jlink --module-path "$OUT/jars" --add-modules events.app --output "$OUT/image"`, filtré par :
     ```bash
     2>&1 | grep -i "automatic" | sed 's/ from file:.*//' || true
     ```
- **Questions :**
  - Pourquoi `events.app` lit-il `events.model` sans le requérir ?
  - Comment rendre l'application « jlinkable » (indice : migration *bottom-up* de `geo-tools`) ?

---

## Checklist (vérifiée par `Check`)

- **Dans les `module-info`** : `requires transitive events.model;`, `requires transitive events.api;`, `requires geo.tools;`, `exports events.core.internal to events.app;`, `opens events.core.state to events.audit;`, `uses events.api.Notifier;`, et les deux `provides …` du tableau.
- **Dans le Java** : `public static Notifier provider()`, `PriorityQueue<`, `ServiceLoader.load(`, `Math.asin(`, `.setAccessible(true)`.
- **Dans le script** : `geo-tools-1.0.jar`, `-m events.app,events.email,events.sms`, `--main-class events.app.Main`, `--limit-modules events.app,events.email`, `jar --describe-module --file`, `jdeps -s -R --module-path`, `jlink --module-path`.

---

## Sortie attendue complète

```
--- complet
salles : {E1=1, E2=2, E3=1, E4=3, E5=2, E6=1}
canaux : [email, sms]
  email a Ana : E2 a Lyon (436 km)
  sms a Bob : E5 11h (159 km)
  email a Chloe : E2 a Lyon (0 km)
  Dan : pas de canal fax
audit : Counters{missing=1, sent=3}
lectures : events.app lit events.model true, Distance dans [geo.tools]
--- sans le fournisseur sms
canaux : [email]
  email a Ana : E2 a Lyon (436 km)
  Bob : pas de canal sms
--- describe-module events.core

events.core
exports events.core
qualified exports events.core.internal to events.app
qualified opens events.core.state to events.audit
requires events.api transitive
requires geo.tools
requires java.base mandated
uses events.api.Notifier
--- jdeps
events.api -> events.model
events.api -> java.base
events.app -> events.audit
events.app -> events.core
events.app -> events.model
events.app -> java.base
events.audit -> java.base
events.core -> events.api
events.core -> events.model
events.core -> geo.tools
events.core -> java.base
events.model -> java.base
geo.tools -> java.base
--- jlink
Error: automatic module cannot be used with jlink: geo.tools
```
