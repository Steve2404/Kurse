# Drill de rappel 5 — L'API `Map`

> Première fois ? Lis d'abord le mode d'emploi [`ch9_collections/PARCOURS.md`](../../PARCOURS.md).

**Chrono cible :** 12 min, puis 6 min.

**Règles :**
- Tout se fait de mémoire, **imports compris**.
- Crée la classe **`Recall05`** dans le paquet `ch9_collections.drills.r05_map`.

## Défis

- ☐ **D01.** `Map<String, Integer> m = new TreeMap<>()`.
  1. `before = m.put("a", 1)` ;
  2. `replaced = m.put("a", 2)` ;
  3. `m.put("b", 3)`.
  
  Affiche `before`, `replaced`, `m`, `get("z")`, `getOrDefault("z", 0)`, `containsKey("b")`, puis `containsValue(2)`.
  → `D01 : null 1 {a=2, b=3} null 0 true true`
- ☐ **D02.**
  1. `putIfAbsent("a", 99)` ;
  2. `putIfAbsent("c", 5)` ;
  3. `gone = m.remove("b")` ;
  4. `notRemoved = m.remove("c", 99)`.
  
  Affiche `m`, `gone`, puis `notRemoved`.
  → `D02 : {a=2, c=5} 3 false`
- ☐ **D03.** Compte les mots de `"le chat et le chien et le rat"` dans une `TreeMap counts`, avec `merge(w, 1, Integer::sum)`.
  → `D03 : {chat=1, chien=1, et=2, le=3, rat=1}`
- ☐ **D04.** Sur `counts`, dans cet ordre :
  1. `merge("et", 0, (old, v) -> null)` ;
  2. `compute("rat", (k, v) -> v == null ? 1 : v + 10)` ;
  3. `computeIfAbsent("loup", k -> k.length())` ;
  4. `computeIfPresent("chat", (k, v) -> v * 100)` ;
  5. `replaceAll((k, v) -> k.equals("le") ? 0 : v)`.
  → `D04 : {chat=100, chien=1, le=0, loup=4, rat=11}`
- ☐ **D05.** Deux parcours :
  - avec `entrySet()` et `Map.Entry`, ajoute la première lettre de la clé, puis la valeur ;
  - avec `forEach((k, v) -> …)`, ajoute la longueur de la clé.
  
  Affiche les deux, puis `keySet()` et `values()`.
  → `D05 : c100c1l0l4r11 45243 [chat, chien, le, loup, rat] [100, 1, 0, 4, 11]`
- ☐ **D06.** Deux maps :
  - une `LinkedHashMap insertion` : `put("z", 1)`, `put("a", 2)`, puis `put("m", 3)`, et une `HashMap` copiée d'elle ;
  - une `HashMap nulls` : `put(null, 0)`, puis `put("k", null)`.
  
  Affiche `insertion`, la taille de la copie, `nulls.get(null)`, `nulls.containsKey("k")`, puis `nulls.get("k")`.
  → `D06 : {z=1, a=2, m=3} 3 0 true null`

## Expériences (hors sortie attendue)

1. `merge("x", 1, …)` sur une clé absente : la fonction est-elle appelée ?
2. `new TreeMap<String, Integer>().put(null, 1)` : que se passe-t-il ?
3. Pourquoi `get("k") == null` ne suffit-il pas à savoir si la clé existe ?

## Sortie attendue complète

```
D01 : null 1 {a=2, b=3} null 0 true true
D02 : {a=2, c=5} 3 false
D03 : {chat=1, chien=1, et=2, le=3, rat=1}
D04 : {chat=100, chien=1, le=0, loup=4, rat=11}
D05 : c100c1l0l4r11 45243 [chat, chien, le, loup, rat] [100, 1, 0, 4, 11]
D06 : {z=1, a=2, m=3} 3 0 true null
```

## Carte mémoire (à lire **après** le drill)

<details><summary>Ouvrir la carte</summary>

| Méthode | Effet |
|---|---|
| `put(k, v)` | rend l'ancienne valeur, ou `null` |
| `putIfAbsent(k, v)` | n'écrit que si la clé est absente (ou associée à `null`) |
| `remove(k)` / `remove(k, v)` | rend la valeur / un `boolean`, et ne retire que si la valeur correspond |
| `merge(k, v, f)` | absente → met v ; présente → `f(ancienne, v)` ; résultat `null` → **supprime** |
| `compute(k, f)` | `f(k, ancienne ou null)` ; `null` → supprime |
| `computeIfAbsent(k, f)` | `f(k)` si absente |
| `computeIfPresent(k, f)` | `f(k, v)` si présente |
| `replaceAll(f)` | `f(k, v)` pour chaque entrée |

**Les `null` :** `HashMap` accepte une clé `null` et des valeurs `null`. `TreeMap` refuse une clé `null`.

**L'ordre :** `HashMap` aucun, `LinkedHashMap` celui d'insertion, `TreeMap` celui des clés.

</details>
