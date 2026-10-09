# 🏠 Palais mental — chapitre 16 : le plafond du salon, stations 1 à 12

> **Avant :** lis une fois [`PALAIS_MENTAL.md`](../../../../PALAIS_MENTAL.md), et sa section « Le 2e circuit : les plafonds ». Choisis 12 points **au plafond** du salon (le lustre, un coin, une poutre, le haut des rideaux…), toujours dans le même sens.
> **Quand :** après le capstone du chapitre, puis avant chaque répétition des drills.
> **Comment :** lis la règle, lève les yeux, joue la scène 10 secondes, redis la règle à voix haute.

---

### 1. 💡 Le lustre — `assertEquals(attendu, obtenu)`

- **Image :** le lustre est une **balance à deux plateaux**. Sur le plateau de **gauche**, une étiquette dorée **« ATTENDU »** ; à droite, **« OBTENU »**. Si tu poses les poids à l'envers, le lustre **ment** : il crie « le code devait rendre 400 ! » alors que c'est toi qui t'es trompé.
- **À retenir :**
  - `assertEquals(attendu, obtenu)` : la valeur **attendue d'abord** ; sinon le message `expected: <…> but was: <…>` est inversé ;
  - un 3e argument texte s'ajoute devant le message : `msg ==> expected: …` ;
  - deux `double` : `assertEquals(0.3, x, 1e-9)` ; de l'argent : des `long` en centimes ;
  - un test sans assertion est vert… et ne vérifie rien.
- **Mon image :** …

### 2. 🚨 Le détecteur de fumée — les mutants

- **Image :** le détecteur de fumée doit **hurler** quand quelqu'un allume un feu. Chaque nuit, un **pyromane** (`Check`) allume **un seul petit feu** dans une copie de la maison. Si le détecteur sonne, le feu est **tué** 🎯. S'il reste muet, le feu **survit**. Un feu peint sur un tableau ne brûle rien : aucun détecteur ne peut le voir (le **mutant équivalent**).
- **À retenir :**
  - un **mutant** = le code de référence avec **un seul** petit bug (`<` au lieu de `<=`, un `+ 50` oublié) ;
  - un bon jeu de tests **tue** chaque mutant (au moins un test échoue) ;
  - un mutant **équivalent** ne change aucun résultat : il révèle du code en trop, pas un test manquant ;
  - **100 % de couverture** ne suffit pas : une ligne exécutée n'est pas une ligne **vérifiée**.
- **Mon image :** …

### 3. 🚪 Le coin au-dessus de la porte — valeurs limites et classes d'équivalence

- **Image :** au-dessus de la porte, une **toise** marquée « de 0 à 100 ». Quatre enfants sont collés au plafond : **−1** et **101** (refusés, ils pleurent), **0** et **100** (acceptés, ils rient). L'enfant de **50** dort dans un coin : il ne sert à rien, il passe avec tous les bugs.
- **À retenir :**
  - les bugs aiment les **frontières** : teste **sur** chaque limite et **juste à côté** ;
  - une **classe d'équivalence** = une famille d'entrées qui se comportent pareil : **un** représentant suffit ;
  - un cas = **une seule** règle cassée, sinon on ne sait pas laquelle est testée ;
  - un arrondi a une limite aussi : 0,49 descend, 0,50 monte.
- **Mon image :** …

### 4. 🪟 La tringle à rideaux — le cycle de vie

- **Image :** sur la tringle, des **rideaux neufs** se déroulent avant **chaque** scène (`@BeforeEach`) et sont jetés après (`@AfterEach`). Un **grand rideau de scène** se lève **une seule fois** au début du spectacle (`@BeforeAll`) et tombe à la fin (`@AfterAll`). Il est **cloué au mur** (`static`), car il existe avant les acteurs. Chaque scène a son **propre acteur** : JUnit crée **une instance neuve** par test.
- **À retenir :**
  - `@BeforeEach` / `@AfterEach` : autour de **chaque** test ; `@BeforeAll` / `@AfterAll` : une fois, et `static` ;
  - **une instance par test** : un champ ne se partage jamais (sauf `@TestInstance(Lifecycle.PER_CLASS)`) ;
  - l'**ordre** des tests n'est pas garanti : un test ne dépend jamais d'un autre ;
  - `@Disabled` : non exécuté ; `@RepeatedTest(n)` : n fois.
- **Mon image :** …

### 5. 📚 Le haut de la bibliothèque — `@Nested`, `@DisplayName`, `assertAll`

- **Image :** en haut de la bibliothèque, des **boîtes rangées dans des boîtes** (`@Nested`), chacune avec une **étiquette en français** (`@DisplayName`). Quand tu ouvres une petite boîte, la grande s'ouvre **d'abord** (son `@BeforeEach` passe avant). Un **filet** (`assertAll`) attrape **tous** les livres qui tombent, au lieu de s'arrêter au premier.
- **À retenir :**
  - `@Nested` : une classe interne **non `static`** = un groupe de tests ; son `@BeforeEach` passe **après** celui de l'extérieur ;
  - `@DisplayName("…")` : le nom affiché, une phrase ;
  - `assertAll(() -> …, () -> …)` : toutes les vérifications, tous les échecs signalés ;
  - un **refus** ne doit rien changer : vérifie l'exception **et** l'état après.
- **Mon image :** …

### 6. 🔆 Le plafonnier — les tests paramétrés

- **Image :** le plafonnier a des **ampoules** : une ampoule **par cas**. `@ValueSource` en visse une par valeur ; `@CsvSource` une par ligne (sauf la ligne d'**en-tête**, qui reste éteinte) ; `@NullAndEmptySource` en visse **deux** (une vide, une absente) ; `@EnumSource` une par constante ; `@MethodSource` les reçoit d'une **usine `static`**.
- **À retenir :**
  - `@ParameterizedTest` **remplace** `@Test` ;
  - `@CsvSource` : `'…'` protège une virgule, une colonne vide = `null`, `nullValues = "N/A"`, `useHeadersInDisplayName` ;
  - `@MethodSource("nom")` : méthode **`static`**, `Stream<Arguments>` pour plusieurs colonnes ;
  - `name = "{index} : {0} -> {1}"` ; chaque cas compte comme un test.
- **Mon image :** …

### 7. 🪵 La poutre — le TDD

- **Image :** sur la poutre, un **feu tricolore** qui ne tourne que dans un sens : 🔴 **rouge** (un petit test qui échoue, même un test qui ne compile pas), 🟢 **vert** (le code **le plus bête** qui passe : `return "I";`), 🔵 **bleu** (on range l'atelier, tout reste vert). Jamais de vert sans rouge avant.
- **À retenir :**
  - jamais de code sans un test rouge qui le réclame ;
  - le code le plus simple ; c'est le **test suivant** qui force la généralisation ;
  - le refactoring se fait **au vert**, code **et** tests ;
  - quand les `if` se répètent, une **table de données** + une boucle (le glouton des chiffres romains).
- **Mon image :** …

### 8. 🎭 Le haut de l'armoire — les doublures de test

- **Image :** en haut de l'armoire, cinq **costumes** : le **mannequin** (*dummy*, on le passe, personne ne le touche), le **bouchon** (*stub*, il répond une phrase apprise par cœur, ou il s'effondre exprès), l'**espion** (*spy*, un carnet à la main, il note tout), le **faux** (*fake*, une vraie petite maison en carton qui fonctionne), et le **simulacre** (*mock*, il vérifie lui-même qu'on lui a bien parlé).
- **À retenir :**
  - un objet **reçoit** ses dépendances par son constructeur (injection), sous forme d'**interfaces** ;
  - faux = dépôt en mémoire ; espion = liste des appels ; bouchon = réponse préparée ou panne exprès ;
  - tester le **chemin d'échec** : une dépendance qui casse ne doit rien laisser à moitié fait ;
  - simule les **frontières** (banque, courriel, base), pas tes petites classes ni ce qui ne t'appartient pas.
- **Mon image :** …

### 9. 🕷️ L'araignée dans le coin — Mockito, préparer

- **Image :** une **araignée-marionnettiste** tisse des fils : `when(…).thenReturn(…)` accroche une réponse à un appel ; plusieurs `thenReturn(1, 2, 3)` accrochent une **guirlande** dont la dernière perle se répète. Une marionnette sans fil répond **le vide** (`null`, 0, `Optional.empty()`). Un fil tendu **pour rien** fait **hurler** l'araignée stricte (`UnnecessaryStubbingException`).
- **À retenir :**
  - `@ExtendWith(MockitoExtension.class)`, `@Mock`, `@InjectMocks` (ou `mock(Type.class)`) ;
  - `thenReturn`, `thenThrow`, `thenAnswer(inv -> …)` ; valeurs **vides** par défaut ;
  - méthode `void` : `doThrow(…).when(m).f()` ; espion : `doReturn(v).when(spy).f()` ;
  - une réponse jamais utilisée échoue (Mockito strict) ; `lenient()` si c'est voulu.
- **Mon image :** …

### 10. 🧵 La fissure du plafond — Mockito, vérifier

- **Image :** dans la fissure, un **détective** avec une loupe : `verify(m).f(x)` (« on t'a appelé, avec ça ? »), `times(2)`, `never()`. Il note l'**ordre** des visites dans un carnet numéroté (`InOrder`). Avec une **pince**, il attrape l'objet qu'on a passé pour l'examiner (`ArgumentCaptor`). Son règlement : soit **tous** les arguments sont des jokers (`any()`, `eq(…)`), soit **aucun**.
- **À retenir :**
  - `verify(m)`, `times(n)`, `never()`, `atLeastOnce()`, `verifyNoInteractions`, `verifyNoMoreInteractions` ;
  - `InOrder o = inOrder(a, b); o.verify(a)…; o.verify(b)…;` ;
  - `ArgumentCaptor.forClass(T.class)`, `capture()`, `getValue()` (le dernier), `getAllValues()` ;
  - matchers : tous ou aucun (`InvalidUseOfMatchersException`), `eq(v)` pour une valeur précise.
- **Mon image :** …

### 11. 🕰️ L'horloge accrochée au plafond — `Clock`

- **Image :** une horloge **figée**, les aiguilles **collées à la glu** sur le lundi 2 mars à 9 h. Tous les tests la regardent : demain, dans dix ans, sur toutes les machines, il est **toujours** 9 h. Une **molette** permet de la faire avancer d'un coup (`Clock.offset`). Une vraie horloge qui tourne (`now()`) est **interdite** dans le code : elle rend les tests faux le lendemain.
- **À retenir :**
  - le code reçoit une `Clock` et appelle `LocalDateTime.now(clock)` ;
  - test : `Clock.fixed(Instant.parse("2026-03-02T09:00:00Z"), ZoneOffset.UTC)` ;
  - « plus tard » : `Clock.offset(horloge, Duration.ofHours(2))`, jamais `Thread.sleep` ;
  - production : `Clock.systemDefaultZone()`.
- **Mon image :** …

### 12. 🌀 Le ventilateur — le débogueur et les six bugs classiques

- **Image :** le ventilateur s'**arrête net** sur un point rouge (le point d'arrêt). Tu le fais tourner **pale par pale** : **F8** (pale suivante), **F7** (tu entres dans le moteur), **Maj+F8** (tu ressors), **F9** (il repart). Avec **Alt+F8**, tu poses une question au moteur. Sur chaque pale est gravé un bug : `==` sur des `String`, `>` au lieu de `>=`, `int * int` qui déborde, une division qui tronque, `==` sur des `Integer` au-delà de 127, une boucle qui commence à 1.
- **À retenir :**
  - point d'arrêt : clic dans la marge (**Ctrl+F8**) ; mode Debug ; la ligne bleue n'est **pas encore** exécutée ;
  - point d'arrêt **conditionnel** (clic droit → *Condition*) ; sur **exception** (**Ctrl+Maj+F8**) ;
  - reproduire, isoler, comprendre, corriger, puis **un test par bug** ;
  - un bug peut en **cacher** un autre : corriger un par un, relancer à chaque fois.
- **Mon image :** …

---

## ⚡ La balade éclair (réponds de tête, puis vérifie)

1. Station 1 : dans quel ordre écrit-on les deux valeurs d'`assertEquals` ?
2. Station 2 : que veut dire « mutant tué » ? Et « mutant équivalent » ?
3. Station 3 : quelles quatre valeurs teste-t-on pour une règle « de 0 à 100 » ?
4. Station 4 : pourquoi `@BeforeAll` est-il `static` ?
5. Station 5 : dans quel ordre passent le `@BeforeEach` extérieur et celui d'un `@Nested` ?
6. Station 6 : combien de cas produit `@NullAndEmptySource` ? Et `@EnumSource(DayOfWeek.class)` ?
7. Station 7 : quelles sont les trois couleurs du TDD, et la règle de chacune ?
8. Station 8 : quelle différence entre un faux et un espion ?
9. Station 9 : comment faire lancer une exception à une méthode `void` d'un simulacre ?
10. Station 10 : à quoi sert un `ArgumentCaptor` ?
11. Station 11 : comment simuler « deux heures plus tard » sans attendre ?
12. Station 12 : pourquoi `(Integer) 1000 == (Integer) 1000` est-il faux ?
