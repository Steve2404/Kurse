# Projet 4 — La validation de trames de capteurs

> Première fois ? Lis d'abord le mode d'emploi [`ch2_operators/PARCOURS.md`](../../PARCOURS.md).
>
> **Bloqué sur une étape ?** [`INDICES.md`](INDICES.md) donne deux indices repliés par étape, sans code. **Étape finie ?** [`solution/CORRIGE.md`](solution/CORRIGE.md) donne, étape par étape, le code de l'étape, les **réponses aux questions** et le résultat exact des **expériences**. N'ouvre que la section de l'étape que tu viens de faire.

**Notions visées (chapitre 2) :**
- le **court-circuit** de `&&` et `||`, face à `&` et `|` sur des booléens ;
- `^` ;
- les **effets de bord** dans une condition ;
- les incréments **préfixés et suffixés** dans une même expression ;
- l'**affectation comme expression** (`x = y = 4`, `(x += 2) * x`, `(flag = true)`) ;
- `==` et `!=` sur des **références** ;
- `instanceof`.

**Ce qui est donné :** `Check.java`. Arguments : `22 55 80 true` (température, humidité, batterie, signature).

**Ce que TU crées :** tout le programme, dans le paquet `ch2_operators.projects.p04_sensors`. La classe du `main` s'appelle **`Sensors`**. Une trame est une **autre** classe.

**Règle du crescendo :** chapitres 1 et 2. Pas de `if`, pas de boucle. Avec `instanceof`, pas de variable (`o instanceof String s` est du chapitre 3).

---

## Le problème

Une station reçoit des trames de capteurs. Une trame est valide si **quatre contrôles** passent, dans cet ordre :
1. la température est entre −20 et 50 ;
2. l'humidité est entre 0 et 100 ;
3. la batterie est au-dessus de 10 % ;
4. la trame est signée.

Chaque contrôle **coûte cher** : l'équipe veut savoir **combien de contrôles ont réellement été exécutés** selon l'opérateur utilisé.

---

## Tableau de bord

### ☐ Étape 1 — La trame et les contrôles qui se comptent

- **Une trame :** température, humidité, batterie et signature. Elle est construite avec un constructeur.
- **Quatre méthodes de contrôle :** chacune **incrémente un compteur `static`**, puis rend son verdict.
  - Un verdict « entre −20 et 50 » s'écrit avec `&&`.

### ☐ Étape 2 — `&&` contre `&`

```
trame #2 (70 C, 40 %, batterie 90 %, signee) : rejetee | && 1 controle(s), & 4 | ...
trame #3 (25 C, 120 %, batterie 5 %, non signee) : rejetee | && 2 controle(s), & 4 | ...
```
- **La validation :** valide la trame **deux fois**, en remettant le compteur à 0 avant chacune :
  - une fois en chaînant les 4 contrôles avec `&&` ;
  - une fois avec `&`.
- **À la main d'abord :** pour chacune des 5 trames de la sortie attendue, prédis les deux nombres de contrôles.
- **Questions :**
  - le **verdict** peut-il différer entre `&&` et `&` ? Et les **effets de bord** ?
  - quand préférer `&` en pratique ?

### ☐ Étape 3 — `^` et `||`

```
... | un seul defaut climat true | alerte oui
```
- **« Un seul défaut climat » :** exactement **un** des deux contrôles climatiques (température, humidité) échoue. Ce n'est pas « au moins un ». Quel opérateur exprime « l'un ou l'autre, mais pas les deux » ?
- **L'alerte :** température au-dessus de 45 **ou** batterie sous 20 %.
- **Le numéro de trame :** un compteur `static`, incrémenté dans l'expression du texte.

### ☐ Étape 4 — Incréments, affectations, références

```
increments : a = 12, b = 2, id = 5
affectations : x = 6, y = 4, z = 36, affectation (vraie)
references : f1 == f2 false, f1 == f3 true, f1 != f2 true
instanceof : Integer true, Number true, texte Integer false, null Object false
```
- **Les incréments.** Avec `id = 5`, écris exactement `a = id++ + ++id;` puis `b = id-- - --id;`.
  - **Calcule à la main** la valeur rendue **et** l'effet de chaque terme, de gauche à droite.
- **Les affectations.**
  - `x = y = 4;` : dans quel ordre ? Que vaut l'expression `y = 4` ?
  - `z = (x += 2) * x;` : quelle valeur de `x` est utilisée à droite ?
  - `(flag = true) ? … : …` compile alors que `flag` valait `false`. Pourquoi ? C'est le piège classique `=` au lieu de `==`.
- **Les références.** Deux trames **identiques** créées avec `new` sont-elles `==` ? Et deux variables qui désignent **le même** objet ?
- **`instanceof` sur des `Object` :**
  - un `Integer` est-il un `Number` ?
  - `null instanceof Object` vaut-il `true` ou `false` ?
  - **Expérience :** `"texte" instanceof Integer` écrit **directement** (pas via une variable `Object`). Que dit `javac`, et pourquoi ?

---

## Checklist (vérifiée par `Check`)

| Élément | Étape | ☐ |
|---|---|---|
| `&&` (au moins 3), `&` entre deux appels | 2 | ☐ |
| ` ^ `, `||` | 3 | ☐ |
| `++`, `--` | 1, 4 | ☐ |
| `x = y = 4`, `(x += 2)`, `(flag = true)` | 4 | ☐ |
| `==`, `!=` | 4 | ☐ |
| `instanceof` | 4 | ☐ |

---

## Sortie attendue complète

```
trame #1 (22 C, 55 %, batterie 80 %, signee) : valide | && 4 controle(s), & 4 | meme verdict true | un seul defaut climat false | alerte non
trame #2 (70 C, 40 %, batterie 90 %, signee) : rejetee | && 1 controle(s), & 4 | meme verdict true | un seul defaut climat true | alerte oui
trame #3 (25 C, 120 %, batterie 5 %, non signee) : rejetee | && 2 controle(s), & 4 | meme verdict true | un seul defaut climat true | alerte oui
trame #4 (-30 C, 150 %, batterie 50 %, signee) : rejetee | && 1 controle(s), & 4 | meme verdict true | un seul defaut climat false | alerte non
trame #5 (10 C, 10 %, batterie 11 %, non signee) : rejetee | && 4 controle(s), & 4 | meme verdict true | un seul defaut climat false | alerte oui
increments : a = 12, b = 2, id = 5
affectations : x = 6, y = 4, z = 36, affectation (vraie)
references : f1 == f2 false, f1 == f3 true, f1 != f2 true
instanceof : Integer true, Number true, texte Integer false, null Object false
trames analysees : 5
```
