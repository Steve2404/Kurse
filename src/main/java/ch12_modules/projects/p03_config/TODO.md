# Projet 3 — La configuration par réflexion (`opens`, `--add-opens`, `--add-exports`)

> Première fois ? Lis d'abord le mode d'emploi [`ch12_modules/PARCOURS.md`](../../PARCOURS.md).

**Notions visées (chapitre 12) :**
- **`exports` contre `opens`** :
  - `exports` donne l'accès **normal** aux types publics, à la compilation et à l'exécution ;
  - `opens` donne la **réflexion profonde** (membres privés, `setAccessible`), à l'exécution seulement ;
- **`opens … to …`** (qualifié) et le **module ouvert** `open module` ;
- **les options de ligne de commande :**
  - `--add-exports module/paquet=cible`, à la compilation **et** à l'exécution ;
  - `--add-opens module/paquet=cible` ;
- **les erreurs :**
  - `package … is not visible` (compilation) ;
  - `IllegalAccessError` (exécution) ;
  - `InaccessibleObjectException` (réflexion) ;
- **à l'exécution :** `Module.isOpen(…)`.

Côté algorithme : un petit **« binder »** de configuration :
- il remplit les **champs privés** d'un objet à partir de couples `clé → texte`, en convertissant les types ;
- il produit une description **récursive** des champs (le *dump*).

**L'API de réflexion** (hors programme de l'examen, mais indispensable pour voir `opens` agir) :
- `getDeclaredConstructor()`, `getDeclaredField(nom)` et `getDeclaredFields()` ;
- `setAccessible(true)` ;
- `field.set(objet, valeur)`, `field.get(objet)` et `field.getType()` ;
- `Class.forName(nom)`.

**Ce que TU crées :** tes 4 modules dans `ch12_modules/p03_config/src/` (`config.model`, `config.legacy`, `config.binder`, `config.app`), et ton script `build.sh` dans ce dossier.

**Règle du crescendo :** chapitres 1 à 12.

---

## Tableau de bord

### ☐ Étape 1 — Les modules

| Module | `module-info.java` | Contenu |
|---|---|---|
| `config.model` | `exports config.model;` et `opens config.model to config.binder;` (rien d'autre) | `config.model.ServerConfig`, `config.model.Limits`, `config.model.internal.Defaults` et `config.model.secret.Vault` |
| `config.legacy` | **`open module config.legacy`**, qui exporte `config.legacy` | `LegacyConfig`, avec `private String mode` et `private int retries` |
| `config.binder` | exporte `config.binder` ; **ne requiert pas** les modules qu'il inspecte | `final class Binder` |
| `config.app` | requiert `config.model`, `config.legacy` et `config.binder` | `config.app.Main` |

- **`ServerConfig`** :
  - champs privés `host`, `port` (int), `debug` (boolean), `tags` (`List<String>`), et `limits = new Limits()` ;
  - les accesseurs `host()` et `port()` ;
  - `toString()` = `host:port`, suivi de ` (debug)` si `debug` est vrai, puis ` ` et les tags.
- **`Limits`** : `private int maxClients = 100;` et `private long timeoutMs = 3_000;`.
- **`Defaults`** : une classe **publique**, dans un paquet **non exporté**, avec `PORT = 8080` et `describe()` = `port par defaut 8080`.
- **`Vault`** : `private String token;`.

### ☐ Étape 2 — Le binder

- **`static Object convert(String text, Class<?> type)`** convertit vers `int`, `long`, `boolean`, `List` (découpée sur `,` avec `List.of`), et sinon garde le texte.
- **`static <T> T bind(Class<T> type, Map<String, String> values) throws ReflectiveOperationException`** :
  1. crée l'objet avec le constructeur sans argument, rendu accessible ;
  2. pour chaque entrée, `getDeclaredField(clé)`, `setAccessible(true)`, puis `set` avec la valeur convertie.
- **`static String dump(Object o) throws IllegalAccessException`** : `{nom=valeur, ...}`, avec les champs triés par nom. Un champ ni primitif ni `null`, dont le type est dans un module **nommé** qui ne commence pas par `java.`, est décrit à son tour (récursion).

### ☐ Étape 3 — Le programme

```
lie : example.org:8443 (debug) [web, api] ; dump {debug=true, host=example.org, limits={maxClients=100, timeoutMs=3000}, port=8443, tags=[web, api]}
coffre refuse : InaccessibleObjectException - Unable to make public config.model.secret.Vault() accessible: ...
interne refuse : IllegalAccessError - class config.app.Main (in module config.app) cannot access class config.model.internal.Defaults ...
```
- **`main(String[]) throws ReflectiveOperationException`**, dans l'ordre :
  1. `bind(ServerConfig.class, …)`, avec une `TreeMap` de `host=example.org`, `port=8443`, `debug=true` et `tags=web,api`. Affiche la config, puis son `dump` ;
  2. `bind(LegacyConfig.class, Map.of("mode", "batch", "retries", "3"))`, puis affiche `module ouvert : ` et son `dump` ;
  3. la ligne `isOpen` :
     - `config.model` ouvert à `config.binder`, puis à `config.app` ;
     - `config.legacy` ouvert à tous (`isOpen` sans cible) ;
     - `config.model.secret` ouvert à `config.binder` ;
  4. `Class.forName("config.model.secret.Vault")`, puis `bind` avec `token=s3cret` et `dump`. Dans un `catch (RuntimeException e)` : `coffre refuse : <nom simple> - <message>` ;
  5. `Defaults.describe()`. Dans un `catch (IllegalAccessError e)` : `interne refuse : <nom simple> - <message>`.
- **Question :** pourquoi le refus du coffre parle-t-il de `exports`, alors qu'on cherche à ouvrir ?

### ☐ Étape 4 — Le script `build.sh`

- **En tête :** `P=ch12_modules/p03_config` et `OUT=build/ch12/p03_config`.
- **Les commandes :**
  1. `echo "--- javac sans --add-exports"`, puis `javac -d "$OUT/mods" --module-source-path "$P/src" -m config.app,config.model,config.legacy,config.binder`, filtré comme au projet 1 (`grep -E "error:|declared in"`…, puis `|| true`) ;
     - **Nomme les 4 modules dans `-m` :** sinon `javac` ne compile que les classes **atteignables**, et `Vault` (chargée par son nom) manquerait ;
  2. la même compilation avec `--add-exports config.model/config.model.internal=config.app` ;
  3. `echo "--- java sans option"`, puis `java -p "$OUT/mods" -m config.app/config.app.Main` ;
  4. `echo "--- java avec --add-exports et --add-opens"`, puis le même lancement avec `--add-exports config.model/config.model.internal=config.app` et `--add-opens config.model/config.model.secret=config.binder`, et `| tail -3` ;
  5. `echo "--- describe-module"`, puis `--describe-module config.legacy`, puis `config.model`, chacun avec `| sed 's/ file:.*//' | sort`.
- **Expériences :**
  - remplace `opens config.model to config.binder` par `exports` seul : que dit le `dump` ?
  - ajoute `opens config.legacy;` dans `config.legacy` : quelle erreur ?
  - à l'étape 4, retire `--add-exports` : que se passe-t-il ?

---

## Checklist (vérifiée par `Check`)

- **Dans les `module-info`** : `exports config.model;`, `opens config.model to config.binder;`, `open module config.legacy`, `module config.binder`, `requires config.binder;`.
- **Dans le Java** : `.setAccessible(true)`, `.getDeclaredField(`, `.getDeclaredConstructor()`, `.getDeclaredFields()`, `Class.forName(`, `.isOpen(`, `catch (IllegalAccessError`, `throws ReflectiveOperationException`.
- **Dans le script** : les deux options `--add-exports config.model/config.model.internal=config.app` et `--add-opens config.model/config.model.secret=config.binder`, `--describe-module config.legacy` et `config.model`.

---

## Sortie attendue complète

```
--- javac sans --add-exports
error: package config.model.internal is not visible
(package config.model.internal is declared in module config.model, which does not export it)
--- java sans option
lie : example.org:8443 (debug) [web, api] ; dump {debug=true, host=example.org, limits={maxClients=100, timeoutMs=3000}, port=8443, tags=[web, api]}
module ouvert : {mode=batch, retries=3}
config.model ouvert a config.binder true, a config.app false ; config.legacy ouvert a tous true ; secret ouvert a config.binder false
coffre refuse : InaccessibleObjectException - Unable to make public config.model.secret.Vault() accessible: module config.model does not "exports config.model.secret" to module config.binder
interne refuse : IllegalAccessError - class config.app.Main (in module config.app) cannot access class config.model.internal.Defaults (in module config.model) because module config.model does not export config.model.internal to module config.app
--- java avec --add-exports et --add-opens
config.model ouvert a config.binder true, a config.app false ; config.legacy ouvert a tous true ; secret ouvert a config.binder true
coffre : {token=s3cret}
interne : port par defaut 8080
--- describe-module
config.legacy
exports config.legacy
requires java.base mandated
config.model
contains config.model.internal
contains config.model.secret
exports config.model
qualified opens config.model to config.binder
requires java.base mandated
```
