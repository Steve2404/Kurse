# 🏠 Palais mental — chapitre 19 : les plafonds des douches et le ciel de la terrasse

> **Avant :** lis une fois [`PALAIS_MENTAL.md`](../../../../PALAIS_MENTAL.md) et sa section « Le 2e circuit : les plafonds ». Choisis 12 points au plafond de la **douche 1**, 12 au plafond de la **douche 2**, et 12 **au-dessus de la terrasse** (le store, la lampe, la gouttière, la pergola… jusqu'au ciel), toujours dans le même sens.
> **Quand :** après le capstone (en trois fois : une pièce par jour), puis avant chaque répétition des drills.
> **Comment :** lis la règle, lève les yeux, joue la scène 10 secondes, redis la règle à voix haute.
>
> La **douche 1** garde les **données qui voyagent** (JSON, base, HTTP) ; la **douche 2** garde le **programme sous pression** (concurrence, pannes, lenteur) ; la **terrasse**, ouverte sur le dehors, garde le **regard du senior** (la revue, l'architecture, la production).

---

## Le plafond de la douche 1 : les données qui voyagent (stations 1 à 12)

### 1. 🎲 Le coin au-dessus de la porte — les six cas du JSON

- **Image :** six **gouttes** pendent du coin, chacune d'une forme : un vide (`null`), un interrupteur (`true`), un chiffre, une étiquette, une chaîne de perles (tableau), une boîte à tiroirs (objet). Un **sceau de cire** les relie : il n'y en aura jamais une septième.
- **À retenir :**
  - une interface `sealed … permits` et six `record` : un type fermé, que le compilateur connaît en entier ;
  - les nombres en `BigDecimal` (`0.1 + 0.2` ne vaut pas `0.3` en `double`) ; `1.0` n'est pas `equals` à `1.00` ;
  - l'ordre des clés : `LinkedHashMap`, puis `Collections.unmodifiableMap` ; jamais `Map.copyOf`.
- **Mon image :** …

### 2. 🪜 La lampe étanche — la descente récursive

- **Image :** une échelle descend de la lampe ; chaque barreau porte le nom d'une règle (`valeur`, `tableau`, `objet`, `chaine`, `nombre`). Un **curseur** lumineux avance d'un caractère à la fois, et chaque barreau peut rappeler l'échelle tout entière.
- **À retenir :**
  - une méthode par règle de la grammaire, un champ `pos` ;
  - le caractère courant décide : `{`, `[`, `"`, `-` ou chiffre, sinon un mot ;
  - un conteneur : vide ? sinon `do { élément } while (séparateur)` ; `peekOrFail()` évite dix tests.
- **Mon image :** …

### 3. 📍 La bouche de ventilation — dire où et quoi

- **Image :** la ventilation crache des **cartes routières** : « ligne 5, colonne 5 : `,` ou `]` attendu ». Une carte sans coordonnées s'envole et se perd.
- **À retenir :**
  - un message d'erreur sert un humain pressé : **où** (ligne, colonne, à partir de 1) et **quoi** (ce qu'on attendait) ;
  - un nombre faux pointe son **début**, un échappement faux son **antislash** ;
  - le parseur compte dans le texte reçu, pas dans le fichier source (un text block retire l'indentation).
- **Mon image :** …

### 4. 🧱 La tringle du rideau — les limites contre l'inconnu

- **Image :** la tringle est faite de **500 anneaux** ; au 501e, un gardien coupe. Plus loin, une **balance** refuse un colis de plus de 64 Ko, et un portier refuse les commandes de plus de 100 tâches.
- **À retenir :**
  - `"[".repeat(5000)` fait déborder la pile : une profondeur maximale (`MAX_DEPTH`) ;
  - lire au plus `limite + 1` octets (`readNBytes`) : 413 ; une taille de page bornée : 400 ;
  - toute entrée qui fixe une quantité de travail a une limite : sinon, un déni de service en une ligne.
- **Mon image :** …

### 5. 🔁 Le pommeau — le test de propriété

- **Image :** le pommeau pulvérise **300 documents** au hasard ; chacun tombe dans l'écrivain puis remonte par le parseur, et doit ressortir **identique**. Mais deux défauts symétriques (`\u001F` écrit, `\u001F` toléré) passent ensemble dans le jet.
- **À retenir :**
  - une propriété toujours vraie (l'aller-retour) sur des centaines d'entrées, graine fixe (`SplittableRandom`) ;
  - elle vérifie la **cohérence**, pas la **norme** : seuls 6 mutants sur 24 meurent avec elle seule ;
  - les exemples fixent le comportement exact ; la propriété explore les combinaisons.
- **Mon image :** …

### 6. 💉 Le coin moisi — l'injection SQL

- **Image :** dans le coin, une **seringue** injecte `x'; DROP TABLE task; --` dans une requête collée à la main… et la table **disparaît** pour de vrai. À côté, un `PreparedStatement` tient la valeur dans un **gant** : elle reste une donnée.
- **À retenir :**
  - jamais de valeur collée dans le texte SQL : `PreparedStatement` et des `?` ;
  - dans `LIKE`, `%` et `_` restent des jokers : `LIKE ? ESCAPE '!'` et `!%`, `!_`, `!!` ;
  - on affiche le **SQLState**, jamais `getMessage()` (il change avec la langue).
- **Mon image :** …

### 7. 🗂️ La goutte qui pend — les migrations versionnées

- **Image :** une goutte numérotée **1**, puis **2**, puis **3** tombe dans un registre (`schema_version`) ; une goutte déjà tombée est gravée : on n'y touche plus. Une goutte `CREATE TABLE` éclabousse et **valide** tout ce qui était en suspens.
- **À retenir :**
  - chaque évolution est une migration numérotée, appliquée **une** fois ; on n'en modifie jamais une appliquée : on en ajoute une ;
  - tout vérifier **avant** de changer quoi que ce soit ;
  - avec H2, MySQL et Oracle, une instruction DDL valide la transaction en cours.
- **Mon image :** …

### 8. 🧤 Le haut du carrelage — exécuter autour, tout ou rien

- **Image :** une paire de **gants** fait toujours les mêmes gestes : ouvrir, désactiver l'auto-commit, travailler, `commit`… ou, au moindre accroc, `rollback`, puis fermer. Le travail lui-même arrive dans une enveloppe (une lambda).
- **À retenir :**
  - le cadre JDBC écrit **une** fois (`Transactions.write(SqlWork)`) ;
  - annuler sur **toute** exception (`SQLException | RuntimeException`), puis la relancer ;
  - JDBC laisse le comportement à la fermeture au pilote : Oracle **valide** ce qui est en suspens.
- **Mon image :** …

### 9. 🔢 Le haut du miroir — le verrou optimiste

- **Image :** chaque tâche porte un **numéro de version** gravé sur le miroir. Ada écrit (version 0 → 1) ; Bob arrive avec sa vieille version 0 : le miroir se **brouille**, « version 0 périmée ».
- **À retenir :**
  - `UPDATE … SET …, version = version + 1 WHERE id = ? AND version = ?` ;
  - `executeUpdate() == 0` : la tâche existe ? conflit (409), sinon introuvable (404) ;
  - l'application relit et laisse décider l'utilisateur ; elle ne réessaie pas à l'aveugle.
- **Mon image :** …

### 10. 📮 La fissure de la peinture — HTTP en dix minutes

- **Image :** par la fissure passent des **lettres** : `POST /tasks`, des en-têtes, une ligne vide, un corps. Les réponses reviennent avec un **tampon** : 201, 204, 400, 404, 405, 409, 413, 415, 500.
- **À retenir :**
  - 4xx : la faute du client ; 5xx : la faute du serveur ;
  - `GET` ne modifie jamais rien ; `PUT` et `DELETE` sont idempotents, `POST` non ;
  - 201 donne `Location`, 405 donne `Allow`, et les noms d'en-têtes ne tiennent pas compte de la casse.
- **Mon image :** …

### 11. 🚦 Le crochet — le routeur et un seul guichet d'erreurs

- **Image :** un **aiguilleur** pend au crochet : `/tasks/{id}` → la voie 42. Voie inconnue : 404 ; bonne voie, mauvais train : 405 et la liste des trains permis. Derrière lui, **un seul guichet** transforme chaque panne en code, et ne dit jamais rien d'un bug au client.
- **À retenir :**
  - `split("/", -1)` : un `/` final compte ; `{id}` refuse un segment vide ;
  - les handlers **lancent**, le routeur **traduit** (dans Spring : `@ControllerAdvice`) ;
  - un 500 : `{"error":"erreur interne"}`, et le détail part dans le journal.
- **Mon image :** …

### 12. 🔌 Le coin au-dessus de la douche — l'adaptateur HTTP et le port 0

- **Image :** une **prise** au plafond : c'est le seul endroit où le serveur du JDK touche ton code. Elle se branche sur un port **choisi par le système** (0), décode les paramètres **après** les avoir coupés, et parle UTF-8.
- **À retenir :**
  - le cœur parle `Request` et `Response`, testés sans réseau ; seul `WebServer` connaît `HttpExchange` ;
  - le port 0 dans les tests ; `BindException` quand deux serveurs veulent le même port ;
  - `q=x%26y` : on coupe sur `&`, puis on décode.
- **Mon image :** …

---

## Le plafond de la douche 2 : le programme sous pression (stations 1 à 12)

### 1. ☎️ Le coin au-dessus de la porte — passager ou définitif

- **Image :** deux téléphones pendent du coin : l'un sonne **occupé** (on rappellera), l'autre répond « **numéro inconnu** » (inutile de rappeler). Un traducteur (l'adaptateur) colle sur chaque échec une étiquette : `retryable` ou non.
- **À retenir :**
  - ne réessayer que ce qui peut passer ; jamais un bug (`NullPointerException`) ;
  - l'adaptateur traduit les exceptions du service en **une** exception à nous ;
  - la dernière erreur porte les précédentes en `suppressed`.
- **Mon image :** …

### 2. 🚩 La lampe étanche — le drapeau d'interruption

- **Image :** un **drapeau rouge** se lève quand on veut arrêter un fil. Celui qui attrape `InterruptedException` voit le drapeau **retomber**… et doit le **relever** avant de partir.
- **À retenir :**
  - `catch (InterruptedException e) { Thread.currentThread().interrupt(); … }` ;
  - un drapeau perdu : le programme ne s'arrête plus proprement ;
  - `Thread.interrupted()` lit **et** baisse ; `isInterrupted()` lit seulement.
- **Mon image :** …

### 3. ⏳ La bouche de ventilation — le recul exponentiel

- **Image :** la ventilation souffle de plus en plus lentement entre deux essais : 100, 200, 400, 800 ms, jamais plus d'une seconde. Un peu de **hasard** dans chaque souffle, pour que mille clients ne soufflent pas ensemble.
- **À retenir :**
  - `firstDelay × multiplier^(n-1)`, plafonné ; un peu de *jitter* contre les rafales synchronisées ;
  - attendre est une dépendance : un `Sleeper` injecté, qui **note** les attentes dans les tests ;
  - sans attente, on va plus vite… et on empêche un service saturé de se rétablir.
- **Mon image :** …

### 4. 🚧 La tringle — le pool borné et la contre-pression

- **Image :** sur la tringle, **3 crochets** (les fils) et une **corbeille de 20 places** (la file). Corbeille pleine : le colis suivant est **refusé** sur-le-champ, au lieu de s'entasser jusqu'à faire tomber la tringle.
- **À retenir :**
  - `new ThreadPoolExecutor(n, n, 0, ms, new ArrayBlockingQueue<>(capacité), new AbortPolicy())` ;
  - `newFixedThreadPool` a une file **sans fin** : la mémoire grimpe, les délais deviennent absurdes ;
  - refuser vite (503, 429) dit à l'appelant de ralentir.
- **Mon image :** …

### 5. ⏰ Le pommeau — le délai et les erreurs emballées

- **Image :** un **minuteur** sur le pommeau coupe l'**attente** au bout de 500 ms… mais l'eau continue de couler derrière : le SMS part quand même. Les erreurs du travail arrivent dans une **enveloppe** (`CompletionException`) qu'il faut ouvrir.
- **À retenir :**
  - `orTimeout` abandonne l'attente, pas le travail ;
  - dans `exceptionally`, déballer : `e.getCause()` si c'est une `CompletionException` ; un délai arrive en `TimeoutException` directe ;
  - sans délai, un travail jamais commencé laisse un `join()` attendre pour toujours.
- **Mon image :** …

### 6. 🔑 Le coin moisi — l'idempotence et l'arrêt propre

- **Image :** une **clé unique** par rappel : la deuxième demande retrouve le même colis, rien ne repart. À la fermeture, le gardien dit « plus personne n'entre », laisse finir, puis éteint la lumière, et compte ceux qui n'ont jamais commencé.
- **À retenir :**
  - `computeIfAbsent` : un seul geste, jamais `containsKey` puis `put` ;
  - un refus n'est pas retenu (`remove(clé, futur)`) ;
  - `shutdown()`, `awaitTermination(grâce)`, puis `shutdownNow()` qui interrompt et rend les jamais commencés.
- **Mon image :** …

### 7. 🚥 La goutte qui pend — tester la concurrence sans dormir

- **Image :** des **feux** (`CountDownLatch`) au plafond : « attendre le vert », « j'ai démarré », « j'ai fini ». Le test décide **exactement** quand chaque fil avance. Un test vert huit fois sur dix, lui, ne prouve rien.
- **À retenir :**
  - `started.await()` avant d'enchaîner ; un `gate` qui libère 16 fils ensemble ;
  - une erreur de concurrence ne se montre pas à chaque fois (sans `synchronized` : 8 échecs sur 10) ;
  - vérifier une valeur qui ne dépend pas de l'ordre.
- **Mon image :** …

### 8. 🪣 Le haut du carrelage — le seau à jetons

- **Image :** un **seau** se remplit goutte à goutte (2 jetons par seconde), déborde à 3, et chaque requête y puise un jeton. Les gouttes se comptent en **millièmes**, et la demi-goutte d'un pas de 1,5 ms n'est jamais jetée.
- **À retenir :**
  - une rafale de `capacity`, puis `perSecond` par seconde ; refusé : 429 et `Retry-After` (arrondi au-dessus) ;
  - des entiers, pas des `double` (dix fois 0,1 ne fait pas 1) ; `last = last.plusMillis(écoulé)` ;
  - un seau par client dans une `Map` : une fuite de mémoire ; un seau plein est un seau neuf, on peut l'oublier.
- **Mon image :** …

### 9. ⚡ Le haut du miroir — le disjoncteur

- **Image :** le **tableau électrique** saute après 3 courts-circuits : pendant 4 s, plus rien ne passe, sans toucher l'appareil malade. Puis on relève **un** levier pour essayer : ça tient, on referme ; ça saute, on attend encore 4 s **entières**.
- **À retenir :**
  - fermé, ouvert, demi-ouvert ; un seul essai à la fois ;
  - « référence inconnue » n'est pas une panne : un `Predicate` dit ce qui compte ;
  - les transitions sont `synchronized`, pas l'appel lui-même.
- **Mon image :** …

### 10. 📊 La fissure — les percentiles et le repli

- **Image :** une fissure en forme de **courbe** : 99 personnes passent en 10 ms, une reste coincée 5 s. La moyenne (59,9 ms) ne décrit personne. Sous la fissure, une **ardoise** garde le dernier stock connu, avec son âge écrit à la craie.
- **À retenir :**
  - p50, p95, p99, max ; le rang le plus proche : `ceil(p/100 × n)` dans les valeurs triées ;
  - une fenêtre glissante dans un tableau circulaire : mémoire fixe ;
  - le repli : « 14 (il y a 12 s) » plutôt qu'une page d'erreur, sans mentir sur la fraîcheur.
- **Mon image :** …

### 11. 🔬 Le crochet — mesurer avant d'optimiser

- **Image :** une **loupe** pend au crochet, au-dessus d'un rapport qui met 3 minutes pour 4 000 lignes. Elle montre que le temps se perd dans `LinkedList.node`, là où personne n'aurait cherché.
- **À retenir :**
  - quand n double : × 2 linéaire, × 4 quadratique, × 8 cubique ;
  - JFR : `-XX:StartFlightRecording=filename=…`, puis `jfr print --events jdk.ExecutionSample --stack-depth 1` ;
  - le maître étalon **avant** d'optimiser ; les bizarreries du legacy sont gardées.
- **Mon image :** …

### 12. ⏱️ Le coin au-dessus de la douche — mesurer juste, et le test de vitesse

- **Image :** un **chronomètre** qui ne démarre qu'après l'échauffement (50 tours), qui garde la **médiane** de 21 tours, et qui jette chaque résultat dans un **trou noir** (`volatile`) pour que personne ne le supprime. À côté, une **barrière** à 2 secondes arrête la version O(n²) qui donnait pourtant le bon résultat.
- **À retenir :**
  - la chauffe du JIT (46 ms à froid, 5 ms chaud), la médiane, le résultat utilisé ; JMH pour les mesures sérieuses ;
  - un test de vitesse à limite **large** : il attrape un changement d'ordre de grandeur ;
  - `HashSet`, un passage, `StringBuilder`, un motif compilé une fois.
- **Mon image :** …

---

## Au-dessus de la terrasse : le regard du senior (stations 1 à 12)

### 1. 🔍 Le store banne — la revue de code

- **Image :** sous le store, un **relecteur** avec quatre lunettes posées dans l'ordre : le comportement, la robustesse, la sécurité, puis la lisibilité. Il commente le **code**, jamais la personne, et chaque remarque finit par une solution.
- **À retenir :**
  - chercher d'abord ce qui est **faux**, puis ce qui casse quand ça va mal, puis les failles, puis la forme ;
  - chaque défaut se **prouve** par un test, avant d'être corrigé ;
  - une démo suit un seul chemin : les défauts vivent ailleurs.
- **Mon image :** …

### 2. 👯 La lampe extérieure — `equals` et `hashCode`

- **Image :** deux **jumeaux** identiques sous la lampe ; le videur (`HashSet`) les laisse entrer **tous les deux**, parce qu'ils n'ont pas le même numéro de vestiaire (`hashCode`).
- **À retenir :**
  - deux objets égaux **doivent** avoir le même `hashCode` ;
  - un `record` écrit `equals`, `hashCode` et `toString`, toujours d'accord ;
  - une `enum` se compare avec `==`, une `String` avec `equals`.
- **Mon image :** …

### 3. 💶 La gouttière — l'argent et les chaînes

- **Image :** des **centimes** coulent dans la gouttière ; ceux en `double` fuient (0,30000000000000004 €). En bas, deux étiquettes « DOUBLE » : celle du code passe le portillon `==`, celle tapée par le client reste dehors.
- **À retenir :**
  - l'argent en centimes dans un `long` ; le texte d'un montant en `BigDecimal` (`movePointRight(2).longValueExact()`) ;
  - `(long) (19.99 * 100)` vaut 1998 ;
  - `"DOUBLE".equals(code)` : juste, et sans `NullPointerException`.
- **Mon image :** …

### 4. 📅 La poutre de la pergola — les limites et le temps

- **Image :** sur la poutre, une **marche** marquée 1 000 : le client qui y pose le pied est GOLD (`>=`). Un **calendrier** saute le 29 février : `plusDays(365)` tombe un jour trop tôt. L'horloge des tests est réglée sur **janvier 2025**, loin d'aujourd'hui.
- **À retenir :**
  - « à partir de » : `>=` ; tester **aux limites** (299, 300, 999, 1 000) ;
  - `plusYears(1)`, jamais `plusDays(365)` ;
  - une horloge de test **différente** de la vraie date : un `LocalDate.now()` oublié se voit.
- **Mon image :** …

### 5. 🕳️ La vigne qui pend — l'erreur avalée et la ressource ouverte

- **Image :** la vigne cache un **trou** où tombent les erreurs sans bruit (« 1 achat lu sur 3 », personne ne sait pourquoi). Un **robinet** resté ouvert goutte : le flux jamais refermé. Un espion (le lecteur qui note `close()`) surveille.
- **À retenir :**
  - une erreur arrête, avec la ligne et la raison ; jamais de `catch` vide ni de valeur « spéciale » (-1) ;
  - `try (…)` : fermé même après une exception ;
  - un lecteur espion (`extends Reader`) prouve la fermeture.
- **Mon image :** …

### 6. 🐦 Le nid d'hirondelles — l'état partagé et les données personnelles

- **Image :** un seul **nid** pour toutes les hirondelles (une `Map` `static`) : deux magasins pondent dans le même. Huit oiseaux y entrent en même temps et des œufs disparaissent (`ArrayList`). Sur le nid, une **adresse** masquée : `a***@example.org`.
- **À retenir :**
  - l'état appartient à l'**instance** ; jamais de collection `static` modifiable ;
  - plusieurs fils : `ConcurrentHashMap`, `CopyOnWriteArrayList`, `LongAdder` ;
  - les `toString` finissent dans les journaux : masquer les données personnelles ; rendre des copies (`List.copyOf`).
- **Mon image :** …

### 7. ⬡ Le carillon à vent — l'architecture hexagonale

- **Image :** un **carillon** : au centre, une pièce de métal pur (le cœur : `Board`), qui ne touche rien ; autour, des tiges (les ports) ; au bout, des clochettes (les adaptateurs : la base, le serveur). Le vent les agite, le centre ne bouge pas.
- **À retenir :**
  - le cœur ne connaît ni la base ni HTTP ; il se teste en microsecondes ;
  - un port est écrit **pour** le métier, qui décide de ce qu'il contient ;
  - les flèches de dépendance vont **vers** le cœur.
- **Mon image :** …

### 8. 🏃 Le fil électrique — vérifier puis agir

- **Image :** deux **coureurs** lisent ensemble « 1 place libre » sur le panneau, puis s'élancent tous les deux : la limite « En cours » saute. Un **tourniquet** (`synchronized`) n'en laisse passer qu'un à la fois entre la lecture et l'action.
- **À retenir :**
  - lire puis agir n'est pas un seul geste (*check-then-act*) ;
  - une instance : `synchronized` ; plusieurs serveurs : un verrou **dans la base** ;
  - le test : 6 fils, une limite de 2, exactement 2 réussites.
- **Mon image :** …

### 9. 🪆 L'antenne — les filtres, et leur ordre

- **Image :** l'antenne porte des **poupées russes** : `MetricsFilter`, dedans `RateLimitFilter`, dedans le `Router`. Inverse les deux premières, et le compteur ne voit plus les clients refusés.
- **À retenir :**
  - un filtre est un décorateur : il reçoit le suivant, travaille avant ou après, et lui passe la main ;
  - la mesure **dehors**, pour tout voir, y compris les 429 ;
  - `finally` : un plantage compte quand même, comme un 500.
- **Mon image :** …

### 10. ☁️ Un nuage — la racine de composition et les tests de bout en bout

- **Image :** un **nuage** au-dessus de la maison : c'est le seul endroit d'où tombent toutes les pièces, assemblées. Pour les tests, il pleut une base neuve, un port libre et une horloge manuelle ; pour la production, un fichier et la vraie heure.
- **À retenir :**
  - `AtelierApp.create` : le seul endroit qui connaît toutes les classes concrètes ;
  - de bout en bout : vraie base, vrai serveur (port 0), vrai client, horloge manuelle ;
  - un second démarrage sur la même base : les migrations ne refont rien.
- **Mon image :** …

### 11. 🌙 La lune — en production : mesurer, trouver, corriger

- **Image :** la nuit, la **lune** éclaire `/metrics` : 104 ms pour lire trois nombres. Un hibou montre la cause : la base se referme et se rouvre à chaque requête. `;DB_CLOSE_DELAY=-1`, et l'aiguille retombe à 1 ms.
- **À retenir :**
  - les mesures écrites par toi servent en production ;
  - mesurer, trouver la cause, corriger, remesurer ;
  - en entreprise : un pool de connexions (HikariCP).
- **Mon image :** …

### 12. ⭐ L'étoile du berger — et après : Spring Boot, et continuer

- **Image :** l'**étoile** montre le chemin : chaque pièce écrite à la main a son équivalent dans Spring Boot (`@RestController`, `@ControllerAdvice`, `@Transactional`, Flyway, Jackson, Micrometer, Resilience4j). Tu sais ce qu'elles font, puisque tu les as construites.
- **À retenir :**
  - Spring fait en annotations ce que tu as écrit en milliers de lignes ;
  - quand un 415, un 429, un rollback manqué ou une requête lente apparaîtront, tu sauras où chercher ;
  - un senior continue d'apprendre : lire du code d'autres, relire, mesurer, refaire les drills.
- **Mon image :** …

---

## ⚡ La balade éclair

1. Douche 1, station 1 : pourquoi `BigDecimal` et pas `double` pour un nombre JSON ? Et pourquoi pas `Map.copyOf` ?
2. Douche 1, station 4 : que fait `"[".repeat(5000)` à un parseur sans limite ?
3. Douche 1, station 6 : que devient `x'; DROP TABLE task; --` collé dans une requête, et avec un `PreparedStatement` ?
4. Douche 1, station 9 : quelle requête fait le verrou optimiste, et que rend l'API quand il échoue ?
5. Douche 1, station 11 : quelle différence entre un 404 et un 405 ?
6. Douche 2, station 2 : que dois-tu faire en attrapant une `InterruptedException` ?
7. Douche 2, station 5 : un délai dépassé arrête-t-il le travail ?
8. Douche 2, station 8 : pourquoi compter les jetons en millièmes dans un `long` ?
9. Douche 2, station 12 : quelles sont les trois précautions d'une micro-mesure ?
10. Terrasse, station 3 : combien vaut `(long) (19.99 * 100)` ?
11. Terrasse, station 8 : pourquoi `move` est-il `synchronized`, et que faudrait-il avec deux serveurs ?
12. Terrasse, station 11 : pourquoi chaque requête prenait-elle 100 ms, et quelle est la vraie réponse en entreprise ?
