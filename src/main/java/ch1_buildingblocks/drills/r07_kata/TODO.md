# Drill de rappel 7 — Kata mixte chronométré (tout le chapitre 1)

> Première fois ? Lis d'abord le mode d'emploi [`ch1_buildingblocks/PARCOURS.md`](../../PARCOURS.md).

**Chrono cible :** 20 min, **sans carte**. C'est le test final de chaque cycle de révision.

**Règles :**
- Tout se fait de mémoire.
- Crée la classe **`Recall07`** (avec `main` en **varargs**) et une classe **`Box`**.
- `Check` lance ton `main` avec `3 0x1F rouge 0.25 false`.

**Les notions de ce drill ont été apprises dans :** projets 1 à 5 : c'est le test final du chapitre. Si un défi te semble totalement inconnu, ce drill arrive trop tôt : refais d'abord la leçon de ce projet.

<details><summary><b>Comment faire ce drill, concrètement</b> (à lire la 1re fois)</summary>

1. **Note l'heure** de départ (montre ou téléphone).
2. **Crée la classe** : clic droit sur le dossier `r07_kata` → **New** → **Java Class** → `Recall07`.
3. **Écris `main`**, puis **une ligne par défi**. La ligne qui suit la flèche `→` est **exactement** ce que ton programme doit afficher. Par exemple, pour un défi `D01` qui attend `D01 : 8 16`, écris un `System.out.println("D01 : " + … + " " + …);`, où les `…` sont les valeurs que **Java** calcule.
4. **Lance `Recall07`** avec la flèche verte, pour voir tes lignes.
   Ce drill reçoit des **arguments** : lance `Recall07` une fois, puis Run → Edit Configurations… → **Recall07** → Program arguments : `3 0x1F rouge 0.25 false` (projet 0, étape 5).
5. **Bloqué plus de 3 minutes sur un défi ?** Écris un commentaire `// D03 : ✗` à sa place, et passe au suivant.
6. **Lance `Check.java`** (flèche verte). Une ligne `[FAIL]` te montre `attendu` et `obtenu`.
7. **Ensuite seulement**, ouvre la **carte mémoire** tout en bas (en aperçu Markdown, clique sur le triangle « Ouvrir la carte »), relis tes ✗, et fais les **expériences**.
8. **Note** la date, ton temps et tes ✗ dans le tableau de [`drills/README.md`](../README.md).

</details>

## Défis

- ☐ **D01.** Le 1er argument converti en `int`, multiplié par le 2e argument (un hexadécimal **avec son préfixe**).
  → `D01 : 93`
- ☐ **D02.** Le 1er argument converti en binaire, puis le 2e en hexadécimal **sans préfixe**.
  → `D02 : 11 1f`
- ☐ **D03.** `Box` construit avec le 3e argument. Son journal (un champ `String`) vaut `"1"` à la déclaration ; un bloc d'initialisation ajoute `"2"` ; le constructeur ajoute `"3"`, puis le libellé. Déclare la boîte avec `var`.
  → `D03 : 123rouge`
- ☐ **D04.** Un champ `static long total` de `Recall07` (jamais initialisé), puis le compteur `static` de boîtes créées.
  → `D04 : 0 1`
- ☐ **D05.** `total` reçoit 3 milliards (écrit avec `_`) plus le 1er argument.
  → `D05 : 3000000003`
- ☐ **D06.** Un text block d'une ligne `** ticket **`, terminé par un saut de ligne, affiché avec `print`.
  → `D06 : ** ticket **`
- ☐ **D07.** Le 4e argument en `double` (dans une variable `final`), fois 100. Puis le 4e argument converti en objet `Double`, ramené en `int`.
  → `D07 : 25.0 0`
- ☐ **D08.** Le 5e argument converti en `boolean`. Puis `'\u0041'` et `'B'` affichés collés.
  → `D08 : false AB`

## Sortie attendue complète

```
D01 : 93
D02 : 11 1f
D03 : 123rouge
D04 : 0 1
D05 : 3000000003
D06 : ** ticket **
D07 : 25.0 0
D08 : false AB
```

## Après le kata

- **Pour chaque défi raté ou trop lent :** refais le drill de son thème le lendemain.
  - r01 : `main` et arguments ;
  - r02 : littéraux ;
  - r03 : classes enveloppes ;
  - r04 : text blocks ;
  - r05 : variables ;
  - r06 : initialisation.
- **Note ton temps** dans `drills/README.md`.
