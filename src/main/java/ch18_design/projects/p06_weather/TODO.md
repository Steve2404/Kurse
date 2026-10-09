# Projet 6 — La météo du site (adaptateur, proxy, décorateurs, composite)

> Première fois ? Lis d'abord le mode d'emploi [`ch18_design/PARCOURS.md`](../../PARCOURS.md).
>
> **Bloqué sur une étape ?** [`INDICES.md`](INDICES.md) donne deux indices repliés par étape, sans code. **Étape finie ?** [`solution/CORRIGE.md`](solution/CORRIGE.md) donne, étape par étape, le code de l'étape, les **réponses aux questions** et le résultat exact des **expériences**. N'ouvre que la section de l'étape que tu viens de faire.

**Notions visées (chapitre 18) :**
- les patrons de **structure** : comment assembler des objets ;
- l'**adaptateur** (*Adapter*) : faire parler une bibliothèque étrangère comme **notre** interface ;
- le **proxy** : un remplaçant qui contrôle l'accès (ici, un **cache** avec une durée de vie) ;
- le **décorateur** (*Decorator*) : ajouter un comportement (réessayer, journaliser) **autour** d'un objet, sans le modifier et sans héritage ;
- la **chaîne de responsabilité** (des sources de secours) et le **composite** (un groupe qui se présente comme un seul) ;
- l'**ordre d'emboîtement** des enveloppes, qui change le comportement ;
- tester le temps avec une **horloge qu'on avance à la main**.

**Ce qui est FOURNI :** `Data.java` contient `OldMeteoApi`, la bibliothèque d'un fournisseur extérieur : elle parle en degrés **Fahrenheit**, avec des **codes de station** à trois lettres, et ses premiers appels peuvent échouer (`station injoignable`). On ne peut pas la modifier.

**Ce que TU crées :** dans `ch18_design.projects.p06_weather` : `Forecast`, `WeatherService`, `MeteoAdapter`, `CachingWeather`, `RetryingWeather`, `LoggingWeather`, `FallbackWeather`, `AverageWeather`, `Weather`, et tes tests (par exemple `WeatherTest`).

**Règle du crescendo :** chapitres 1 à 17, JUnit et Mockito. Pas de `System.out` ni de `Thread.sleep`. Dans ton code : **aucun** `extends` (on compose, on n'hérite pas), pas d'`instanceof`, et aucune méthode de plus de **12 lignes**.

---

## Tableau de bord

### ☐ Étape 1 — Notre interface, et l'adaptateur

**📖 La leçon : l'adaptateur.** Une prise anglaise ne rentre pas dans une prise française : on met un **adaptateur** entre les deux, et ni l'appareil ni le mur ne changent. En code, c'est pareil : notre application veut **sa** façon de parler (des noms de villes, des degrés Celsius) ; la bibliothèque du fournisseur a la **sienne** (des codes de station, des Fahrenheit). Une classe **adaptateur** implémente **notre** interface et traduit vers la bibliothèque. C'est le **seul** endroit qui connaît la bibliothèque : si l'on change de fournisseur, on change d'adaptateur, et rien d'autre.

**👉 À toi :**
- lance `Data` : il montre les pannes du fournisseur ;
- `public record Forecast(String city, int celsius, String source)` ;
- `@FunctionalInterface public interface WeatherService` avec `Forecast forecast(String city)` ;
- `public final class MeteoAdapter implements WeatherService`, construit avec un `Data.OldMeteoApi` (écris `Data.OldMeteoApi` : c'est une classe imbriquée de `Data`, dans le même paquet, sans `import`) :
  - le code de station est fait des **trois premières lettres** de la ville, en **majuscules** (`"Paris"` → `"PAR"`) ;
  - la température : `(F − 32) × 5 / 9`, **arrondie** au degré le plus proche (`Math.round`), pas tronquée ;
  - la source est `"meteo"`, la ville est celle reçue ; les erreurs du fournisseur passent telles quelles.

**🧪 Les tests :** un `@ParameterizedTest` sur Paris (64,4 °F), Lyon (71 °F), Brest (55 °F), Nice (11 °F) et `"paris"` en minuscules, avec un `new Data.OldMeteoApi(Data.OldMeteoApi.READINGS, 0)` (0 panne). Calcule chaque résultat **sur papier** d'abord. Et une ville inconnue (`"Rome"`) : le message du fournisseur.

**❓ Question :** pour Lyon et pour Nice, que donnerait un simple `(int)` au lieu de `Math.round` ? Pourquoi Nice est-il un cas à part ?

### ☐ Étape 2 — Le proxy : un cache avec une durée de vie

**📖 La leçon : le proxy.** Un **proxy** a **la même interface** que l'objet qu'il remplace, et se place devant lui pour **contrôler l'accès** : vérifier des droits, retarder une création coûteuse, ou, ici, **garder en mémoire** les réponses (un cache). Le fournisseur est lent et payant : une prévision demandée il y a moins de 10 minutes peut être resservie.

Pour tester un cache, il faut faire passer le temps **sans attendre**. `Clock` est une classe abstraite : une petite sous-classe **dans tes tests** (`MutableClock extends Clock`, avec une méthode `advance(Duration)`) fait avancer l'heure d'un coup. Elle doit écrire trois méthodes : `instant()`, `getZone()` et `withZone(ZoneId)`.

**👉 À toi :** `public final class CachingWeather implements WeatherService`, constructeur `CachingWeather(WeatherService inner, Clock clock, Duration ttl)` :
- une prévision est gardée **par ville** ; elle est resservie **strictement avant** `instant de la demande + ttl` (à 10 minutes pile, on redemande) ;
- l'heure se lit avec `clock.instant()` ;
- une **erreur** n'est jamais gardée : la demande suivante rappelle `inner`.

**🧪 Les tests :** avec une doublure **écrite à la main** qui joue un scénario (`ScriptedWeather implements WeatherService` : une réponse ou une exception par appel, et un compteur `calls`) : une deuxième demande à 9 min 59 s ne rappelle pas `inner`, une demande à 10 min pile le rappelle ; deux villes ont chacune leur entrée ; une erreur n'est pas gardée.

**❓ Question :** pourquoi le proxy doit-il avoir **exactement** la même interface que le service qu'il remplace ? Que gagne le code appelant ?

### ☐ Étape 3 — Les décorateurs : réessayer, journaliser

**📖 La leçon : le décorateur.** Pour ajouter « réessayer en cas de panne » à un service, l'héritage donnerait `RetryingMeteoAdapter extends MeteoAdapter`, puis `RetryingLoggingMeteoAdapter`… Un **décorateur** fait mieux : il **implémente** l'interface **et enveloppe** un objet de cette interface, ajoute son comportement, puis **délègue**. Comme il enveloppe **n'importe quel** `WeatherService`, on les **empile** comme des poupées russes, dans l'ordre qu'on veut. C'est ainsi que sont faits les flux d'entrée et de sortie de Java (chapitre 14) : `new BufferedReader(new InputStreamReader(new FileInputStream(…)))`.

**Exemple sur un autre sujet :** un café, puis « avec lait » autour, puis « avec sucre » autour du tout : chaque enveloppe ajoute son prix et délègue le reste au café qu'elle contient.

**👉 À toi :**
- `public final class RetryingWeather implements WeatherService`, constructeur `RetryingWeather(WeatherService inner, int attempts)` : refuse `attempts < 1` (`"au moins une tentative : " + attempts`) ; appelle `inner` **au plus** `attempts` fois ; à la première réponse, la rend ; après le dernier échec, relance **la dernière** exception reçue ;
- `public final class LoggingWeather implements WeatherService`, constructeur `LoggingWeather(WeatherService inner, List<String> log)` : ajoute au journal `"Paris -> 18 C (meteo)"` en cas de réponse, ou `"Paris -> erreur : " + message` en cas d'exception, qu'il **relance telle quelle** (la même exception, pas une nouvelle).

**🧪 Les tests :** deux pannes puis une réponse, avec 3 tentatives (réussi, 3 appels) et avec 2 tentatives (le message de la **deuxième** panne, 2 appels) ; le refus de 0 tentative ; le journal d'une réponse et d'une erreur, et `assertSame` sur l'exception relancée.

**❓ Question :** `RetryingWeather` relance la **dernière** exception. Quelle autre information serait utile à celui qui la reçoit, et comment la lui donner (pense à l'étape 4) ?

### ☐ Étape 4 — Plusieurs sources : le secours et la moyenne

**📖 La leçon : la chaîne et le composite.** Deux façons de grouper des `WeatherService` **derrière la même interface** :
- la **chaîne de responsabilité** : on essaie les sources **dans l'ordre**, la première qui répond gagne (comme un standard téléphonique qui passe l'appel au suivant) ;
- le **composite** : on interroge **toutes** les sources et l'on combine leurs réponses. Un composite peut contenir d'autres composites : c'est un **arbre**, que l'appelant manipule comme **un seul** objet (comme un dossier qui contient des fichiers et d'autres dossiers, et dont on demande « la taille »).

**👉 À toi :**
- `public final class FallbackWeather implements WeatherService`, construit avec une `List<WeatherService>` (copiée) : essaie les sources dans l'ordre et rend la première réponse, **sans** appeler les suivantes ; si toutes échouent, lance `IllegalStateException("aucune source pour " + city)` en y ajoutant **chaque** cause avec `addSuppressed` (chapitre 11) ;
- `public final class AverageWeather implements WeatherService`, construit avec une `List<WeatherService>` : demande à **toutes** les sources ; celles qui échouent ne comptent pas ; rend la moyenne **arrondie** des réponses, avec la source `"moyenne(" + nombre de réponses + ")"` ; aucune réponse : `IllegalStateException("aucune source pour " + city)`.

**🧪 Les tests :** le secours qui s'arrête à la première réponse (la source d'après n'est jamais appelée) ; toutes en panne (le message, et les messages des causes avec `getSuppressed()`) ; la moyenne de 18 et 19 ; une source en panne dans la moyenne ; aucune réponse ; et un composite **dans** un composite (la moyenne de « la moyenne de 10 et 20 » et de 30 : calcule-la sur papier).

**❓ Question :** dans le composite imbriqué, la réponse n'est pas la moyenne de 10, 20 et 30. Pourquoi ? Est-ce un bug ?

### ☐ Étape 5 — Tout emboîter : l'ordre compte

**📖 La leçon : l'ordre des enveloppes.** Les mêmes pièces emboîtées dans un autre ordre donnent un **autre** programme. Un journal **à l'extérieur** du cache voit **chaque** demande ; **à l'intérieur**, il ne voit que les vrais appels au fournisseur. Des réessais **à l'intérieur** du secours insistent sur le fournisseur avant de passer au secours ; **à l'extérieur**, ils ne serviraient à rien.

**👉 À toi :** `public final class Weather` (constructeur privé) avec `public static WeatherService standard(Data.OldMeteoApi api, WeatherService backup, Clock clock, List<String> log)` qui emboîte, **de l'intérieur vers l'extérieur** :
1. l'adaptateur sur `api`, enveloppé de **3** tentatives ;
2. un secours : d'abord ce service, puis `backup` ;
3. un cache de **10 minutes** ;
4. le journal, tout à l'extérieur.

**🧪 Les tests (l'intégration) :** un fournisseur avec 2 pannes, un secours en lambda (`city -> new Forecast(city, 20, "secours")`) : la première demande réussit par la météo après 3 appels au fournisseur ; 5 minutes plus tard, la même demande ne rappelle **pas** le fournisseur, mais le journal a **deux** lignes. Un fournisseur avec 3 pannes : le secours répond. Tout en panne : le journal note l'erreur `aucune source pour …`.

**❓ Questions :**
- Si l'on mettait les 3 tentatives **autour** du secours (au lieu de dedans), que se passerait-il avec un fournisseur qui a 2 pannes ?
- Si l'on mettait le journal **dans** le cache, combien de lignes aurait le journal dans ton test d'intégration ? Laquelle des deux places est la bonne pour un journal de demandes ? Et pour un journal des coûts du fournisseur ?

### ☐ Étape 6 — Les mutants

**👉 À toi :** lance `Check`. Les 17 mutants touchent chacun une pièce : la conversion, une limite du cache, le nombre de tentatives, le journal, les causes supprimées, l'arrondi de la moyenne, l'ordre d'emboîtement…

---

## Checklist (vérifiée par `Check`)

- **Ton code :** `record Forecast(`, `interface WeatherService`, et les classes `final class MeteoAdapter`, `CachingWeather`, `RetryingWeather`, `LoggingWeather`, `FallbackWeather`, `AverageWeather`, toutes `implements WeatherService` ; `final class Weather`, `Math.round(`, `addSuppressed(`, `clock.instant()` ; ni `extends`, ni `instanceof`, ni `System.out`, ni `Thread.sleep`.
- **La conception :** aucune méthode de plus de **12 lignes** ; `CachingWeather`, `RetryingWeather` et `LoggingWeather` contiennent `private final WeatherService inner` ; `AverageWeather` contient `List<WeatherService>` ; seuls `MeteoAdapter` et `Weather` nomment `OldMeteoApi`.
- **Tes tests :** au moins **15** tests, `implements WeatherService`, `extends Clock`, `Data.OldMeteoApi`, `Weather.standard(`, `getSuppressed()`, `assertSame(`, `assertThrows(` ; ni `System.out` ni `Thread.sleep`.
- **Les 17 mutants** sont tués.

---

## Ce que `Check` affiche quand tout est juste

```
=== Verification des tests de ch18_design.projects.p06_weather ===
[PASS] tes tests sur TON code : … tests, … reussis
[PASS] tes tests sur le code de REFERENCE : … tests, … reussis
[PASS] les tests de REFERENCE sur TON code : 17 tests, 17 reussis
   mutant 1 : tue (par …)
   …
[PASS] mutants : 17/17 tues
--- API de ton code ---
[PASS] API : tous les elements vises sont utilises
[PASS] conception : toutes les regles de structure sont respectees
--- API de tes tests ---
[PASS] API : tous les elements vises sont utilises

*** PROJET REUSSI : tes tests passent, attrapent tous les mutants, et ton code est juste. ***
```
