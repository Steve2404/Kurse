# Drill de rappel 3 — Builder, fabrique statique, objet immuable

> Première fois ? Lis d'abord le mode d'emploi [`ch18_design/PARCOURS.md`](../../PARCOURS.md). À faire après le projet p05.

**Chrono cible :** 25 min, puis 12 min.

**Règles :**
- Tout se fait de mémoire, **imports compris**, dans le paquet `ch18_design.drills.r03_builder`.
- Crée les types ci-dessous, **exactement** avec ces noms et ces signatures.
- Pas de setter (`public void set…`), pas de constructeur public pour `Trip`.
- Tu n'écris pas de tests : les tests de référence vérifient ton code.

**Les notions de ce drill ont été apprises dans :** projet 5 (étapes 2 à 4).

<details><summary><b>Comment faire ce drill, concrètement</b> (à lire la 1re fois)</summary>

Comme le drill 1 : crée les fichiers dans l'ordre des défis, `// D04 : ✗` après 3 minutes bloqué, lance `Check.java`, puis la carte mémoire, puis note ton temps dans [`drills/README.md`](../README.md).

</details>

## Défis

- ☐ **D01.** `public record City(String code)` : trois lettres majuscules, sinon `IllegalArgumentException("ville invalide : " + code)` ; `public static City of(String raw)` enlève les espaces autour, met en majuscules et rend l'objet d'un **cache** (le même objet pour `" par "` et `"PAR"`).
  → `d01 : 1 executions, 1 reussies`
- ☐ **D02.** Les codes invalides (`"PA"`, `"PARI"`, `"P4R"`, `""`) sont refusés.
  → `d02 : 4 executions, 4 reussies`
- ☐ **D03.** `public final class Trip` (accesseurs `from()`, `to()` en `City`, `date()`, `passengers()`, `options()`) et sa classe imbriquée `public static final class Builder`, obtenue par `Trip.builder()` : `from(String)`, `to(String)`, `date(LocalDate)`, `passengers(int)` (1 par défaut), `option(String)` (ajoute) ; `build()` lance `IllegalStateException`, dans cet ordre : `"depart et arrivee obligatoires"`, `"depart et arrivee identiques"`, `"date obligatoire"`, `"passagers : de 1 a 9"`.
  → `d03 : 1 executions, 1 reussies`
- ☐ **D04.** Un voyage construit ne change plus, même si l'on continue à modifier le builder ; ses options refusent `add`.
  → `d04 : 1 executions, 1 reussies`
- ☐ **D05.** `public Builder toBuilder()` : un builder prérempli, pour fabriquer le retour (on change départ, arrivée et date ; les passagers et les options restent).
  → `d05 : 1 executions, 1 reussies`
- ☐ **D06.** `public static Trip oneWay(String from, String to, LocalDate date)` : un passager, sans option.
  → `d06 : 1 executions, 1 reussies`

## Sortie attendue complète

```
d01 : 1 executions, 1 reussies
d02 : 4 executions, 4 reussies
d03 : 1 executions, 1 reussies
d04 : 1 executions, 1 reussies
d05 : 1 executions, 1 reussies
d06 : 1 executions, 1 reussies
```

<details><summary>Ouvrir la carte</summary>

- **La fabrique avec cache** : `private static final Map<String, City> CACHE = new ConcurrentHashMap<>();` puis `return CACHE.computeIfAbsent(raw.strip().toUpperCase(), City::new);`. La validation reste dans le constructeur compact.
- **L'objet immuable** : classe `final`, champs `private final`, constructeur **privé** `Trip(Builder b)` qui **copie** la liste (`List.copyOf`).
- **Le builder** : classe imbriquée `static final`, constructeur privé, champs mutables avec leurs valeurs par défaut, chaque méthode finit par `return this;`.
- **`build()`** : une petite méthode `check(condition, message)` qui lance `IllegalStateException` ; l'ordre des vérifications fixe le message.
- **`toBuilder()`** : `builder().from(from.code()).to(to.code()).date(date).passengers(passengers)`, puis `b.options.addAll(options)`.
- **Le test d'indépendance** : construire, **modifier le builder**, vérifier que l'objet construit n'a pas bougé.

</details>
