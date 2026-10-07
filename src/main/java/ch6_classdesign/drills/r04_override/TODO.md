# Drill de rappel 4 — Redéfinir (override) ou surcharger (overload)

> Première fois ? Lis d'abord le mode d'emploi [`ch6_classdesign/PARCOURS.md`](../../PARCOURS.md).

**Chrono cible :** 12 min, puis 6 min.

**Règles :**
- Tout se fait de mémoire.
- Fichier `Recall04.java`, paquet `ch6_classdesign.drills.r04_override`. Les classes :

| Classe | Contenu |
|---|---|
| `A` | `String hello()` rend `A` ; `Number value()` rend `1.5` ; `String say()` rend `a` ; `protected String visible()` rend `A protected` ; `final String fixed()` rend `final de A` ; `toString()` rend `"objet " + getClass().getSimpleName()` |
| `B extends A` | `hello()` rend `"B+" + super.hello()` |
| `C extends B` | `hello()` rend `"C+" + super.hello()` ; `Integer value()` rend `7` (covariant) ; `say()` rend `c` ; la **surcharge** `String say(String word)` rend `"c dit " + word` ; `public String visible()` rend `C public` |

- Mets `@Override` sur chaque redéfinition.

**Les notions de ce drill ont été apprises dans :** projet 1 (étape 1), projet 3 (étape 4) et chapitre 5, projet 5 (la surcharge). Si un défi te semble totalement inconnu, ce drill arrive trop tôt : refais d'abord la leçon de ce projet.

<details><summary><b>Comment faire ce drill, concrètement</b> (à lire la 1re fois)</summary>

1. **Note l'heure** de départ (montre ou téléphone).
2. **Crée la classe** : clic droit sur le dossier `r04_override` → **New** → **Java Class** → `Recall04`. S'il faut d'autres classes, place-les comme le disent les **Règles** ci-dessus ; si elles ne précisent rien, écris-les dans le même fichier, sous `Recall04`, sans `public`.
3. **Écris `main`**, puis **une ligne par défi**. La ligne qui suit la flèche `→` est **exactement** ce que ton programme doit afficher : `System.out.println("D01 : " + …);`, où les `…` sont des valeurs que **Java** calcule, jamais recopiées.
4. **Lance `Recall04`** avec la flèche verte, pour voir tes lignes.
5. **Bloqué plus de 3 minutes sur un défi ?** Écris un commentaire `// D03 : ✗` à sa place, et passe au suivant.
6. **Lance `Check.java`** (flèche verte). Une ligne `[FAIL]` te montre `attendu` et `obtenu`.
7. **Ensuite seulement**, ouvre la **carte mémoire** tout en bas (en aperçu Markdown, clique sur le triangle « Ouvrir la carte »), relis tes ✗, et fais les **expériences** (écris la ligne, compile, lis le message, efface la ligne).
8. **Note** la date, ton temps et tes ✗ dans le tableau de [`drills/README.md`](../README.md).

</details>

## Défis

`A a = new C();` et `C c = new C();`.

- ☐ **D01.** `a.hello()`, `new B().hello()` et `new A().hello()`.
  → `D01 : C+B+A B+A A`
- ☐ **D02.** `Number n = a.value();` puis `Integer i = c.value();` (sans cast). Affiche n, i, puis `a.value().getClass().getSimpleName()`.
  → `D02 : 7 7 Integer`
- ☐ **D03.** `c.say()`, `c.say("bonjour")` et `a.say()`.
  → `D03 : c c dit bonjour c`
- ☐ **D04.** `a.visible()` et `c.fixed()`.
  → `D04 : C public final de A`
- ☐ **D05.** `a` (son `toString`), puis `c.equals(new C())`.
  → `D05 : objet C false`

## Expériences (hors sortie attendue)

1. Dans `C`, redéfinis `visible()` en **`private`** : quelle erreur ?
2. Dans `C`, redéfinis `value()` avec un retour `String` : quelle erreur ? Et avec `Object` ?
3. Dans `C`, redéfinis `fixed()` : quelle erreur ?
4. Mets `@Override` sur `say(String)` : quelle erreur ? Que t'apprend-elle ?
5. Dans `C`, déclare `static String hello()` : quelle erreur ?
6. Dans `A`, `String risky() throws Exception`. Dans `C`, redéfinis-la **sans** `throws` : ça compile. Puis avec `throws Throwable` : non. Pourquoi ? (Les exceptions sont au chapitre 11 ; seule la déclaration est vue ici.)

## Sortie attendue complète

```
D01 : C+B+A B+A A
D02 : 7 7 Integer
D03 : c c dit bonjour c
D04 : C public final de A
D05 : objet C false
```

## Carte mémoire (à lire **après** le drill)

<details><summary>Ouvrir la carte</summary>

**Les règles de la redéfinition :**
1. même nom et mêmes paramètres (même signature) ;
2. accès **au moins aussi large** (`protected` → `public`, oui ; l'inverse, non) ;
3. type de retour identique ou **sous-type** (covariant) : impossible pour un primitif, qui doit être identique ;
4. pas de nouvelle exception vérifiée, ni d'exception plus large ;
5. une méthode `final` ne se redéfinit pas ; une `static` ne se redéfinit pas (elle se **masque**) ; une `private` n'est pas héritée.

**La surcharge :** même nom, **paramètres différents**. C'est une nouvelle méthode, choisie à la compilation.

**`super.m()` :** la version du parent direct, qui peut elle-même appeler la sienne.

**`@Override` :** javac vérifie qu'il s'agit bien d'une redéfinition. Mets-le partout.

</details>
