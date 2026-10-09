# Drill de rappel 6 — Kata final : le bowling (test final du chapitre)

> Première fois ? Lis d'abord le mode d'emploi [`ch16_testing/PARCOURS.md`](../../PARCOURS.md). À faire après le capstone p08.

**Chrono cible :** 35 min, puis 20 min.

**Règles :**
- Tout se fait de mémoire, **imports compris**.
- Dans le paquet `ch16_testing.drills.r06_kata_bowling` : **`BowlingGame`** (avec `public void roll(int pins)` et `public int score()`) et **`BowlingGameTest`**.
- **Sur l'honneur :** en TDD, un test rouge à la fois.

**Les notions de ce drill ont été apprises dans :** tout le chapitre : projets 1 à 4 surtout (le TDD au projet 4, les valeurs limites aux projets 1 et 3, `@BeforeEach` au projet 2).

<details><summary><b>Comment faire ce drill, concrètement</b> (à lire la 1re fois)</summary>

1. **Note l'heure** de départ.
2. **Commence par le test** de la partie « dans la rigole » (20 lancers à 0 : score 0). Un `@BeforeEach` crée une partie neuve, et une petite méthode `rollMany(fois, quilles)` t'évite de répéter les boucles.
3. Une règle à la fois, dans l'ordre ci-dessous. Relance tout à chaque fois.
4. **Bloqué plus de 5 minutes sur une règle ?** `// règle 5 : ✗`, et la suivante.
5. **Lance `Check.java`** : tes tests, le code de référence, et 10 mutants.
6. **Ensuite seulement**, la carte mémoire.
7. **Note** date, temps et ✗ dans [`drills/README.md`](../README.md).

</details>

## Les règles du bowling, dans l'ordre

Une partie a **10 carreaux**. Dans chaque carreau, le joueur a **2 boules** pour abattre **10 quilles**.

1. **Le score de base :** la somme des quilles abattues. 20 lancers à 0 : **0** ; 20 lancers à 1 : **20**.
2. **La réserve** (*spare*) : les 10 quilles en 2 boules. Le carreau vaut 10 **plus la boule suivante**. Exemple : 5, 5, puis 3, puis que des 0 : **16**.
3. **Le strike :** les 10 quilles à la 1re boule ; le carreau s'arrête là. Il vaut 10 **plus les deux boules suivantes**. Exemple : 10, puis 3, 4, puis que des 0 : **24**.
4. **Le 10e carreau :** une réserve donne **une** boule de bonus, un strike en donne **deux**. La partie parfaite (12 strikes) vaut **300** ; 21 boules à 5 valent **150**. Exemple : 18 boules à 0, puis 7, 3, 4 : **14**.
5. **Une partie finie** n'accepte plus de lancer : `IllegalStateException("partie terminee")`. Exemple : 18 boules à 0, puis 7, 2 (score 9) ; un lancer de plus est refusé.
6. **Un lancer impossible** : moins de 0, ou plus que les quilles **encore debout**, lance `IllegalArgumentException("quilles invalides : " + quilles)`. Exemples : −1 ; 11 ; 5 puis 6 dans le même carreau ; au 10e carreau, un strike, puis 7, puis 4 (il ne reste que 3 quilles debout).
7. **Le score d'une partie inachevée** est refusé : `IllegalStateException("partie incomplete")`.

**Dans tes tests :** au moins **12** tests, un `@BeforeEach`, au moins un `@ParameterizedTest`.

## Ce que `Check` affiche quand tout est juste

```
=== Verification des tests de ch16_testing.drills.r06_kata_bowling ===
[PASS] tes tests sur TON code : 13 tests, 13 reussis
[PASS] tes tests sur le code de REFERENCE : 13 tests, 13 reussis
[PASS] les tests de REFERENCE sur TON code : 13 tests, 13 reussis
   mutant 1 : tue (par …)
   …
[PASS] mutants : 10/10 tues
--- API de ton code ---
[PASS] API : tous les elements vises sont utilises
--- API de tes tests ---
[PASS] API : tous les elements vises sont utilises

*** PROJET REUSSI : tes tests passent, attrapent tous les mutants, et ton code est juste. ***
```

(Le nombre de tests est le tien.)

<details><summary>Ouvrir la carte</summary>

**Deux idées qui simplifient tout :**
- **garder la liste de tous les lancers**, et calculer le score **à la fin**, carreau par carreau, avec un indice qui avance de 1 (strike) ou de 2 (sinon) ;
- **suivre les quilles encore debout** pendant les lancers : elles disent si un lancer est possible et quand un carreau se termine.

**Le calcul du score :**

```
pour chacun des 10 carreaux :
    strike  (lancers[i] == 10)              : 10 + lancers[i+1] + lancers[i+2] ; i += 1
    réserve (lancers[i] + lancers[i+1] == 10) : 10 + lancers[i+2]               ; i += 2
    sinon                                    : lancers[i] + lancers[i+1]       ; i += 2
```

**Le 10e carreau :** au départ, 2 boules permises. Si les quilles tombent toutes (strike, ou réserve en 2 boules) avant la 3e boule : **3** boules permises, et les 10 quilles se redressent. La partie est finie quand le nombre de boules permises est atteint.

**Les mutants** : le bonus du strike à une seule boule ; la réserve sans bonus ; un strike qui ne termine pas son carreau ; pas de boule de bonus au 10e carreau ; « plus de 10 quilles » vérifié au lieu de « plus que les quilles debout » ; un lancer accepté après la fin ; le score d'une partie inachevée accepté ; −1 accepté ; les quilles qui ne se redressent pas au 10e carreau ; un strike qui fait avancer l'indice de 2.

</details>
