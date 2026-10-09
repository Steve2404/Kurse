# 🏠 Palais mental — chapitre 17 : les plafonds de la cuisine et de la chambre 1

> **Avant :** lis une fois [`PALAIS_MENTAL.md`](../../../../PALAIS_MENTAL.md) et sa section « Le 2e circuit : les plafonds ». Choisis 12 points au plafond de la **cuisine** (la hotte, le plafonnier, une poutre, un coin…) et 12 au plafond de la **chambre 1**, toujours dans le même sens.
> **Quand :** après le capstone du chapitre (en deux fois : la cuisine, puis la chambre), puis avant chaque répétition des drills.
> **Comment :** lis la règle, lève les yeux, joue la scène 10 secondes, redis la règle à voix haute.

---

## Le plafond de la cuisine (stations 1 à 12)

### 1. 🌀 La hotte — la complexité O

- **Image :** la hotte aspire des **sacs de farine** : 1 sac → 1 seconde (O(1)) ; 1 million de sacs → **20** secondes si elle coupe chaque tas en deux (O(log n)), 1 million de secondes si elle les prend un par un (O(n)), et elle **explose** si elle compare chaque sac à tous les autres (O(n²) : mille milliards).
- **À retenir :**
  - O(1) < O(log n) < O(n) < O(n log n) < O(n²) < O(2ⁿ) < O(n!) ;
  - un ordinateur fait environ un milliard d'étapes simples par seconde ;
  - **mesurer** : quand n double, ×2 = linéaire, ×4 = quadratique, ×1 = logarithmique.
- **Mon image :** …

### 2. 💡 Le plafonnier — la dichotomie

- **Image :** le plafonnier est un **jeu du plus ou moins** : à chaque question, la moitié des ampoules **s'éteint**. 100 ampoules : 7 questions ; un million : 20. Une inscription brille : `lo + (hi - lo) / 2`, car `(lo + hi) / 2` fait **sauter les plombs** (débordement).
- **À retenir :**
  - l'invariant : la réponse est toujours entre `lo` et `hi` ;
  - `binary` : `while (lo <= hi)`, rend `-(lo + 1)` si absent ;
  - toujours `mid = lo + (hi - lo) / 2`.
- **Mon image :** …

### 3. 📏 La poutre — les bornes et la dichotomie sur la réponse

- **Image :** sur la poutre, une **règle graduée** avec deux curseurs : le premier `>= clé` (`lowerBound`) et le premier `> clé` (`upperBound`). La différence entre les deux = le nombre de doublons. Au bout, un **camion** demande « quelle est la plus petite capacité qui suffit ? » : on fait la dichotomie **sur la capacité**.
- **À retenir :**
  - demi-ouvert `[0, n[`, `while (lo < hi)`, `hi = mid` (on garde `mid`) ;
  - seule différence : `<` (lower) ou `<=` (upper) ;
  - dichotomie sur la réponse : il faut une vérification, et une réponse **monotone**.
- **Mon image :** …

### 4. 🃏 Le coin au-dessus de l'évier — insertion et fusion

- **Image :** au coin du plafond, une main **range des cartes** une à une, en les faisant glisser à leur place (insertion : O(n²), mais O(n) si presque trié). À côté, deux correcteurs **fusionnent** leurs piles triées en prenant toujours la plus petite copie du dessus (fusion : O(n log n), stable avec `<=`).
- **À retenir :**
  - insertion : décaler tant que la voisine de gauche est plus grande ;
  - fusion : trier les deux moitiés, fusionner, **copier les restes**, un seul tableau `tmp`.
- **Mon image :** …

### 5. 🥄 Le crochet à louches — le tri rapide

- **Image :** une louche plonge au **hasard** dans la marmite et en sort un **pivot**. Elle fait **trois tas** : plus petits, égaux, plus grands. Un cuisinier naïf prend **toujours la première** louche : sur une marmite déjà triée, il ne coupe plus rien, et la pile d'appels **déborde** (`StackOverflowError`).
- **À retenir :**
  - pivot au hasard (`ThreadLocalRandom`) ;
  - partition en trois (`lt`, `i`, `gt`) : les doublons ne dégénèrent plus ;
  - en moyenne O(n log n), sur place.
- **Mon image :** …

### 6. 🧂 L'étagère à épices du haut — comptage et stabilité

- **Image :** 21 **bocaux** numérotés de 0 à 20 : on y jette chaque note sans jamais comparer (tri par comptage, O(n + max)). Sur l'étiquette d'un bocal : « **stable** : à égalité, je garde l'ordre d'arrivée ».
- **À retenir :**
  - comptage : seulement pour de petites valeurs entières ;
  - un tri qui **compare** ne fait jamais mieux que O(n log n) ;
  - stabilité : `<=` dans la fusion ; elle compte pour les objets.
- **Mon image :** …

### 7. 👉👈 Le détecteur de fumée — deux pointeurs

- **Image :** deux **doigts** touchent les deux bouts d'une étagère de livres triés : trop léger, le doigt gauche avance ; trop lourd, le droit recule. Plus loin, un doigt **lit** et un doigt **écrit** pour enlever les doublons.
- **À retenir :**
  - aux deux bouts : seulement sur un tableau **trié** ; `while (i < j)` ;
  - même sens : `kept` écrit, `i` lit ;
  - les sommes en `long`.
- **Mon image :** …

### 8. 🐛 La fissure — la fenêtre glissante

- **Image :** une **chenille** avance dans la fissure : sa tête avance toujours, sa queue seulement quand la fenêtre n'est plus valide, et **jamais en arrière**. Un **train** passe sur un pont : un wagon entre, un wagon sort.
- **À retenir :**
  - fixe : `window += a[i] - a[i - k]` ;
  - variable : la gauche avance avec `while`, pas `if` ;
  - `left = Math.max(left, precedent + 1)` (le piège de `"abba"`).
- **Mon image :** …

### 9. 📅 Le calendrier accroché au plafond — intervalles et glouton

- **Image :** des **réunions** sont accrochées comme des guirlandes. On les trie, puis on les fusionne. Le glouton garde toujours la réunion qui **finit le plus tôt**. Un compteur de **salles** monte à chaque début et descend à chaque fin, et une fin à 10 h libère avant un début à 10 h.
- **À retenir :**
  - presque tout intervalle : **trier**, puis un seul passage ;
  - glouton du maximum de réunions : trier par **fin** ;
  - salles : débuts et fins triés séparément, deux pointeurs.
- **Mon image :** …

### 10. 🧥 Le porte-manteau du plafond — le hachage

- **Image :** un **vestiaire** numéroté : ton manteau sur le crochet de son numéro (`hashCode`), un seul geste. Des familles entières sous le même nom (une **signature** comme clé).
- **À retenir :**
  - « l'ai-je déjà vu ? » en O(1) : `HashMap` / `HashSet` ;
  - `merge(cle, 1, Integer::sum)`, `computeIfAbsent(cle, k -> new ArrayList<>())` ;
  - deux sommes : chercher le complément **avant** d'enregistrer.
- **Mon image :** …

### 11. 🪣 Le seau suspendu — l'intérieur d'une HashMap

- **Image :** des **seaux** pendent du plafond ; chaque clé tombe dans le seau `floorMod(hash, n)`. Deux clés dans le même seau (« Aa » et « BB ») : une petite chaîne. Quand les seaux sont pleins aux **trois quarts**, on en accroche **deux fois plus**, et chaque clé change de seau.
- **À retenir :**
  - `floorMod` (un hash peut être négatif), puis `equals` dans le seau ;
  - facteur de charge 0,75, doubler, tout re-ranger ;
  - un `hashCode` constant rend tout O(n).
- **Mon image :** …

### 12. 🔗 La chaîne de la lampe — le cache LRU

- **Image :** une **chaîne de perles** pend de la lampe : la plus ancienne en haut, la plus récente en bas. Chaque perle utilisée est **décrochée et raccrochée en bas** ; quand la chaîne est pleine, on coupe celle du haut. Un **annuaire** dit où est chaque perle.
- **À retenir :**
  - `HashMap` (trouver) + liste **doublement** chaînée (décrocher en O(1)) ;
  - deux sentinelles ;
  - un `get` est une utilisation ; chasser = retirer aussi de la `HashMap`.
- **Mon image :** …

---

## Le plafond de la chambre 1 (stations 1 à 12)

### 1. 🍽️ Le lustre — piles et gare de triage

- **Image :** le lustre est une **pile d'assiettes** à l'envers : la dernière posée est la première reprise. Les parenthèses s'emboîtent comme des assiettes. Un **aiguillage** de train (Dijkstra) envoie les nombres tout droit et gare les opérateurs.
- **À retenir :**
  - `Deque` / `ArrayDeque`, jamais `java.util.Stack` ;
  - RPN : `right = pop()` **puis** `left = pop()` ;
  - gare de triage : faire partir les opérateurs de priorité **supérieure ou égale**.
- **Mon image :** …

### 2. 🌡️ Le thermomètre au plafond — la pile monotone et le coût amorti

- **Image :** des jours **froids attendent** sur une pile ; un jour chaud arrive et les **chasse tous** d'un coup. Chaque jour n'entre et ne sort qu'une fois. Une file faite de **deux piles** verse tout d'un coup, mais rarement.
- **À retenir :**
  - une pile d'**indices**, triée ; O(n) au total malgré le `while` ;
  - coût **amorti** : compter sur tout le parcours ;
  - `MinStack` : une 2e pile du minimum à chaque hauteur.
- **Mon image :** …

### 3. 🔁 La poutre — le gabarit du retour arrière

- **Image :** un **cadenas** à molettes au plafond : on tourne une molette (choisir), on essaie la suite (explorer), on **remet** la molette (défaire). On photographie chaque code trouvé : une **copie**, sinon la photo s'efface.
- **À retenir :**
  - choisir, explorer, défaire, dans l'ordre inverse ;
  - `resultats.add(new ArrayList<>(etat))` ;
  - puissance rapide : `half` calculé **une** fois.
- **Mon image :** …

### 4. ♛ Le coin au-dessus du lit — élaguer

- **Image :** huit **reines** suspendues : dès qu'une case est attaquée, on **coupe la branche** sans descendre. Trois tableaux de fils : colonnes, diagonales `r - c + n`, anti-diagonales `r + c`. À côté, une grille de **sudoku** dont on remet la case à 0 en remontant.
- **À retenir :**
  - élaguer = refuser un choix impossible **avant** l'appel récursif ;
  - vérifier les chiffres de départ du sudoku ;
  - défaire = remettre la case à 0.
- **Mon image :** …

### 5. 🌳 La fissure en forme d'arbre — l'arbre binaire de recherche

- **Image :** une fissure se ramifie : à gauche les **plus petits**, à droite les **plus grands**. Pour supprimer un nœud à deux branches, on va chercher son **successeur** : une fois à droite, puis tout à gauche. Des clés insérées **triées** font une longue fissure droite : un arbre **dégénéré**.
- **À retenir :**
  - tout coûte la **hauteur** : O(log n) équilibré, O(n) dégénéré ;
  - infixe = trié ; préfixe = reconstruire ; par niveaux = une **file** ;
  - plancher : chaque fois qu'on part à droite, le nœud est un candidat.
- **Mon image :** …

### 6. ⛰️ Le plafonnier en pyramide — le tas binaire

- **Image :** une **pyramide** d'acrobates au plafond, chacun plus léger que ceux qu'il porte ; le plus léger tout en haut. Ils sont alignés dans un tableau : parent `(i - 1) / 2`, enfants `2i + 1` et `2i + 2`. Un nouveau **remonte**, la racine retirée est remplacée par le dernier qui **descend** vers le plus léger de ses deux porteurs.
- **À retenir :**
  - ajouter / retirer : O(log n), regarder le minimum : O(1) ;
  - descendre vers le **plus petit** enfant ;
  - tri par tas sur place : construire depuis `n / 2 - 1`, puis échanger la racine avec la fin.
- **Mon image :** …

### 7. 🏥 L'horloge du plafond — PriorityQueue, top k, médiane

- **Image :** une salle d'**urgences** au plafond : le plus grave passe d'abord (un `Comparator`). Un **petit tas de 10** garde les 10 meilleurs parmi des millions. Deux tas **dos à dos** gardent la médiane à leurs sommets.
- **À retenir :**
  - `PriorityQueue` sort le plus **petit** selon son ordre ; son affichage n'est **pas** trié ;
  - top k : un tas **min** de taille k, O(n log k) ;
  - médiane : tas max (bas) + tas min (haut), moyenne en `long`.
- **Mon image :** …

### 8. 🕸️ La toile d'araignée — BFS et DFS

- **Image :** une **toile** au plafond : une goutte tombe au centre et s'étale par **cercles** (BFS, une file, le moins d'étapes) ; une araignée suit un fil **jusqu'au bout** avant de revenir (DFS, une pile **explicite** : une récursion d'un million de niveaux déborde).
- **À retenir :**
  - marquer « vu » **en ajoutant** à la file ;
  - le chemin : retenir `previous`, remonter, retourner ;
  - composantes : un parcours par sommet non vu.
- **Mon image :** …

### 9. 🚇 Le plan du métro au plafond — Dijkstra et le tri topologique

- **Image :** un **plan de métro** lumineux : on fixe toujours la station la plus **proche** (un tas), on relâche ses lignes, et on ignore les vieilles annonces (entrées **périmées**). Dans un coin, une **recette** : on ne fait une étape que quand ses prérequis sont faits (Kahn).
- **À retenir :**
  - Dijkstra : pas de poids négatif ; O(E log V) avec un tas, O(V²) sans ;
  - Kahn : degrés entrants à 0 ; incomplet = **cycle** ;
  - le moins d'arêtes (BFS) n'est pas le plus rapide (Dijkstra).
- **Mon image :** …

### 10. 🧶 La pelote suspendue — union-find et Kruskal

- **Image :** des **pelotes** qui se nouent entre elles : chaque groupe a un **chef** (la racine) ; la petite pelote passe sous la grande, et on raccourcit les fils en remontant (compression). Des **routes** de la moins chère à la plus chère : on n'en garde une que si elle relie deux pelotes séparées.
- **À retenir :**
  - `find` avec compression, `union` par taille : presque O(1) ;
  - Kruskal : trier les arêtes par coût, union-find ;
  - connexe = `n - 1` arêtes gardées.
- **Mon image :** …

### 11. 🪜 L'escalier peint au plafond — la programmation dynamique

- **Image :** un **escalier** : pour la marche 10, on vient de la 9 ou de la 8, et on a **noté** le résultat de chaque marche sur un tableau noir, pour ne jamais le recalculer. Un **monnayeur** refuse le glouton (4 + 1 + 1) et trouve 3 + 3.
- **À retenir :**
  - la méthode : la **case**, la **relation**, le **départ**, l'**ordre** ;
  - nombre de façons : pièces à l'extérieur ; sac à dos 0/1 : capacités à l'envers ;
  - deux mots : une table `dp[i][j]`, lettre `charAt(i - 1)`, remonter pour la solution.
- **Mon image :** …

### 12. 🚚 La lucarne — Held-Karp et l'oracle

- **Image :** par la lucarne, un **camion de livraison** : sa tournée est notée par un **interrupteur** de 12 bits (un masque : quels arrêts sont faits). À côté, un **vieux sage** lent essaie tous les ordres, et on vérifie que le camion trouve toujours le même temps que lui (l'oracle).
- **À retenir :**
  - un sous-ensemble = un entier : `1 << i`, `mask & (1 << i)`, `mask | (1 << j)` ;
  - Held-Karp : O(2ᵏ × k²) au lieu de O(k!) ;
  - tester un algorithme rapide contre un **oracle** lent, sur des cas au hasard (`@RepeatedTest`).
- **Mon image :** …

---

## ⚡ La balade éclair

1. Cuisine 1 : quand n double et que le temps est ×4, quelle est la complexité ?
2. Cuisine 2 : pourquoi `lo + (hi - lo) / 2` ?
3. Cuisine 3 : quelle est la seule différence entre `lowerBound` et `upperBound` ?
4. Cuisine 5 : quels sont les deux pièges du tri rapide, et leurs remèdes ?
5. Cuisine 8 : pourquoi `Math.max(left, …)` dans une fenêtre variable ?
6. Cuisine 9 : par quoi trie le glouton du maximum de réunions ?
7. Cuisine 11 : que se passe-t-il quand une table de hachage est pleine aux trois quarts ?
8. Chambre 1 : dans quel ordre dépile-t-on les opérandes d'une RPN ?
9. Chambre 3 : pourquoi `new ArrayList<>(etat)` ?
10. Chambre 6 : où sont les enfants de la case `i` d'un tas ?
11. Chambre 9 : pourquoi Dijkstra refuse-t-il les poids négatifs ?
12. Chambre 11 : dans quel sens parcourt-on les capacités du sac à dos 0/1, et pourquoi ?
