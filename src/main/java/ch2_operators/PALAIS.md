# 🏠 Palais mental — chapitre 2 : le salon, stations 7 à 12

> **Avant :** lis une fois [`PALAIS_MENTAL.md`](../../../../PALAIS_MENTAL.md). Les stations 1 à 6 du salon gardent le chapitre 1 : commence chaque balade par elles.
> **Quand :** après le capstone du chapitre, puis avant chaque répétition des drills.
> **Comment :** lis la règle, ferme les yeux, joue la scène 10 secondes, redis la règle à voix haute.

---

### 7. 🪟 La fenêtre — l'ordre de priorité

- **Image :** la fenêtre a **une échelle** de 14 barreaux devant elle. Les opérateurs grimpent dans cet ordre, du haut (plus fort) vers le bas : en haut, **`x++`** qui se pousse, puis **`++x` et `!`**, puis **`* / %`** qui multiplient les échelons, **`+ -`**, les **flèches `<< >>`**, les **comparateurs `< > <= >=` et `instanceof`**, les **jumeaux `== !=`**, puis **`&`, `^`, `|`**, puis **`&&`, `||`**, le **point d'interrogation `?:`**, et tout en bas, sur le sol, le **seau `=`** qui reçoit ce qui tombe.
- **À retenir :**
  - priorité, de la plus forte à la plus faible : post-unaires, pré-unaires, `* / %`, `+ -`, décalages, relationnels, `== !=`, `&`, `^`, `|`, `&&`, `||`, `?:`, affectations ;
  - à priorité égale : de gauche à droite, **sauf** les affectations et `?:` (de droite à gauche) ;
  - des parenthèses changent tout : en cas de doute, calcule à la main de l'intérieur vers l'extérieur.
- **Mon image :** …

### 8. 🎭 Les rideaux — `x++` contre `++x`

- **Image :** deux acteurs derrière les rideaux. **`++x`** ouvre le rideau, **grandit d'abord**, puis salue : le public voit la **nouvelle** taille. **`x++`** salue d'abord avec son **ancienne** taille, et grandit **dans le dos** du public, rideau fermé.
- **À retenir :**
  - `++x` : incrémente, **puis** donne la nouvelle valeur ;
  - `x++` : donne l'**ancienne** valeur, **puis** incrémente ;
  - piège : `x = x++;` laisse `x` **inchangé** (l'ancienne valeur est réécrite par-dessus).
- **Mon image :** …

### 9. 🟫 Le tapis — la promotion numérique

- **Image :** sur le tapis, de petits nombres **`byte`, `short`, `char`** veulent faire une opération ensemble. Dès qu'ils se touchent, **pouf**, ils gonflent tous en **`int`**, même s'ils étaient deux `short`. Quand un entier touche un **décimal**, il devient décimal. Le plus petit prend toujours la taille du **plus grand**.
- **À retenir :**
  - un opérateur binaire sur `byte`, `short`, `char` donne **au moins un `int`** : `short s = s1 + s2;` **ne compile pas** ;
  - deux types différents : le plus petit est promu vers le plus grand ;
  - entier avec décimal : le résultat est décimal ;
  - le résultat a le type des opérandes **après** promotion.
- **Mon image :** …

### 10. 🪑 Le fauteuil — l'affectation composée et le cast

- **Image :** le fauteuil a un **bouton `+=`** caché sous l'accoudoir : il fait le calcul **et** te recoupe à ta taille (un cast caché), donc `b += 1` marche même pour un `byte`. Si tu fais `b = b + 1` à la main, le résultat est trop gros pour le fauteuil : **ça ne rentre pas**. Un **couteau `(int)`** coupe les décimales sans arrondir : 3,9 devient **3**. Un `(byte)` sur 300 enroule le nombre autour du fauteuil : il reste **44**.
- **À retenir :**
  - `x op= y` contient un **cast implicite** vers le type de `x` ;
  - un cast vers un type plus petit **tronque** : `(int) 3.9` vaut 3 ; `(byte) 300` vaut 44 (on garde les 8 bits du bas) ;
  - une affectation a une **valeur** : `int a = (b = 3) + 1;` donne 4.
- **Mon image :** …

### 11. 🪴 La plante — `&&` et `||` court-circuitent

- **Image :** la plante a deux bras. Le bras **`&&`** regarde le premier pot : s'il est **sec** (`false`), il **n'arrose même pas** le deuxième. Le bras **`||`** voit le premier pot **plein** (`true`) et part dormir. Les bras simples **`&` et `|`** arrosent **toujours les deux pots**, même pour rien.
- **À retenir :**
  - `a && b` : `b` n'est **pas évalué** si `a` est `false` ; `a || b` : `b` n'est pas évalué si `a` est `true` ;
  - piège : un `x++` à droite d'un `&&` peut **ne jamais s'exécuter** ;
  - `&` et `|` évaluent **toujours** les deux côtés.
- **Mon image :** …

### 12. 🕰️ L'horloge — division, modulo, débordement

- **Image :** l'horloge ne sait compter qu'en **nombres entiers** : 7 divisé par 2 donne **3**, et les miettes (1) tombent dans le **tiroir `%`**. Si le nombre est négatif, les miettes sont **négatives** aussi : `-7 % 3` donne **−1**. Diviser un entier par **zéro** fait **exploser** l'horloge ; diviser un `double` par zéro la fait tourner à l'**infini**. Quand l'aiguille dépasse le plus grand `int`, elle **fait le tour** et repart du plus petit.
- **À retenir :**
  - `/` entre entiers **tronque** (`7 / 2` vaut 3) ;
  - `%` a le **signe du dividende** (`-7 % 3` vaut −1) ;
  - entier `/ 0` → `ArithmeticException` ; `double / 0` → `Infinity` ;
  - `Integer.MAX_VALUE + 1` vaut `Integer.MIN_VALUE` (débordement silencieux) ;
  - `==` sur deux objets compare les **références**, pas le contenu.
- **Mon image :** …

---

## ⚡ La balade éclair

1. Station 7 : qui passe en premier, `&&` ou `==` ?
2. Station 8 : que vaut `x` après `int x = 5; x = x++;` ?
3. Station 9 : pourquoi `short s = s1 + s2;` ne compile-t-il pas ?
4. Station 10 : pourquoi `b += 1` compile mais pas `b = b + 1` (avec `byte b`) ?
5. Station 11 : quand le côté droit d'un `||` ne s'exécute-t-il pas ?
6. Station 12 : que valent `-7 % 3` et `1.0 / 0` ?
