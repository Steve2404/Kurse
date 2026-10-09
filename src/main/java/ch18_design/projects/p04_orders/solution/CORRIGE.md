# Projet 4 — Corrigé étape par étape

> **Quand le lire :** ouvre **seulement la section de l'étape** que tu viens de terminer. Le code de référence est dans ce dossier, les tests de référence dans [`OrderServiceTest.java`](OrderServiceTest.java) et [`AdaptersTest.java`](AdaptersTest.java).
>
> Les valeurs ci-dessous ont été obtenues en direct avec **JDK 17**, **JUnit 5.11.4** et **Mockito 5.14.2**, un vendredi soir (9 octobre 2026, vers 19 h 20, heure de Paris).

---

## Étape 1 — Essayer de tester le legacy

**Question — deux lancements :** vers 19 h 20 (vérifié), les deux donnent `refus : boutique fermee`. Lancé dans la journée, la sortie change **à chaque fois** : le numéro est tiré au hasard. Avec le fuseau de New York (13 h 20 là-bas), deux lancements ont donné (vérifié) :

```
MAIL a ada@example.org : commande CMD-95413 confirmee, total 340 centimes
commande enregistree : CMD-95413 -> ada@example.org [baguette, croissant, croissant] 340
```

puis `CMD-40488`. Même code, mêmes entrées, trois résultats différents selon **l'heure**, le **fuseau** et le **hasard**.

**Question — les dépendances cachées :**

| Dépendance cachée | Pourquoi elle empêche de tester |
|---|---|
| `LocalDateTime.now()` | le résultat dépend de l'heure du lancement : impossible de tester « dimanche » ou « 6 h 59 » quand on veut |
| le fuseau horaire de la JVM (lu par `now()`) | le même test passe à Paris et échoue à Tokyo |
| `new Random()` | le numéro est imprévisible : impossible d'écrire `assertEquals("CMD-…", id)` |
| `LegacyDatabase.INSTANCE` | un état **partagé** par tout le programme : un test laisse des commandes que le suivant voit, et rien ne permet de vider la base |
| `new LegacySmtpMailer()` | chaque test enverrait un vrai mail, et rien ne permet de **vérifier** ce qui a été envoyé |
| `PRICES` (statique et privé) | impossible de tester avec un autre catalogue |
| `System.out` (dans le mailer) | la sortie part sur la console, pas dans une variable qu'un test pourrait lire |

**Question — le fuseau horaire :** un test qui appellerait `placeOrder` testerait surtout **l'horloge de la machine** qui le lance : il passerait le matin, échouerait le soir, et autrement sur le serveur d'intégration continue réglé en UTC. Un test qui dépend du moment où on le lance n'est pas un test.

---

## Étape 2 — Les ports

Le code : [`Order.java`](Order.java), [`OrderRepository.java`](OrderRepository.java), [`Notifier.java`](Notifier.java), [`Catalog.java`](Catalog.java), [`IdGenerator.java`](IdGenerator.java).

**Question — `Notifier` plutôt que `MailSender` :** le port est écrit **pour le métier**, et c'est le métier qui le **possède**. `orderConfirmed(Order)` dit **ce qui se passe** dans la boulangerie ; le mail n'est qu'**une** façon de prévenir. Demain, un SMS, une notification sur le téléphone, ou les trois à la fois : de nouveaux adaptateurs, et le service ne change pas. Avec `send(String to, String text)`, le service devrait fabriquer lui-même le texte du mail (un détail de présentation), et un adaptateur SMS recevrait un texte écrit pour un mail. C'est le sens de l'**inversion** : ce n'est pas le métier qui s'adapte à la technique, c'est la technique qui s'adapte au métier.

---

## Étape 3 — Le service

Le code : [`OrderService.java`](OrderService.java). Les tests : [`OrderServiceTest.java`](OrderServiceTest.java), avec la doublure `RecordingNotifier`.

**Question — faire avancer le temps :** deux services sur **le même** dépôt, chacun avec son horloge fixe : `serviceAt(10 h 15)` commande, `serviceAt(10 h 45)` annule. Une autre solution : une petite doublure d'horloge **modifiable** (une sous-classe de `Clock` dont on avance l'instant). Les deux marchent parce que le temps est **injecté**.

**Question — `isAfter` :** à 10 h 45 pile, `now` est **égal** à la limite (10 h 15 + 30 minutes). `now.isAfter(limite)` est faux, donc l'annulation est acceptée : la 30e minute est comprise, comme demandé. `!now.isBefore(limite)` serait vrai, donc refusé. Le test `cancelWithinThirtyMinutesIncluded` (annulation à 10 h 45 pile) le vérifie ; `cancelTooLateOrUnknownIsRefused` vérifie 10 h 45 et 1 seconde. C'est le mutant 10.

---

## Étape 4 — Les pannes, avec Mockito

Les tests : `failingNotifierDoesNotLoseTheOrder` et `refusedOrderNeverReachesTheNotifier`.

**Question — avaler l'exception :** non, pas en silence. Ici, le choix métier est juste (la commande est prise, le mail est secondaire), mais une vraie application doit au moins **journaliser** l'erreur (avec un *logger*, pour que l'équipe voie que les mails ne partent plus), et le plus souvent **ranger** la notification à refaire (une file d'attente, ou une table « à envoyer » relue plus tard : le patron *outbox*). Une panne invisible est une panne qui dure des semaines.

---

## Étape 5 — Les adaptateurs et la racine de composition

Le code : [`InMemoryOrderRepository.java`](InMemoryOrderRepository.java), [`ConsoleNotifier.java`](ConsoleNotifier.java), [`SequentialIds.java`](SequentialIds.java), [`BakeryApp.java`](BakeryApp.java). Les tests : [`AdaptersTest.java`](AdaptersTest.java).

**Question — les `new` d'adaptateurs :** **une seule** classe de production, `BakeryApp` (les tests en font aussi, pour fabriquer leurs doublures). C'est le signe que les dépendances sont **choisies à un seul endroit** : pour changer de base ou de canal de notification, on sait où aller, et le reste du code ne le sait même pas.

**Question — passer à H2 :** on **ajoute** un adaptateur `JdbcOrderRepository implements OrderRepository` (chapitre 15), et l'on change **une ligne** de `BakeryApp.create`. `OrderService` ne bouge pas, et **tous** les tests de `OrderServiceTest` restent valables tels quels : ils n'ont jamais connu le dépôt en mémoire de production. Seul le nouvel adaptateur demande ses propres tests ; l'idéal est un **test de contrat** de `OrderRepository` (projet 3), passé par les deux implémentations.

---

## Étape 6 — Les mutants

**Expérience — `BakeryApp` avec la vraie horloge** (vérifié) : à Paris vers 19 h 20, `refus : boutique fermee` ; avec `-Duser.timezone=America/New_York` :

```
MAIL a ada@example.org : commande CMD-1 confirmee, total 340 centimes
Order[id=CMD-1, customer=ada@example.org, items=[baguette, croissant, croissant], totalCents=340, placedAt=2026-10-09T13:23:48.115429600]
```

Le programme réel dépend toujours de l'heure : c'est voulu, une boulangerie ferme le soir. Mais tes **tests** donnent toujours le même résultat, parce qu'ils n'utilisent **jamais** l'horloge du système : chacun injecte une horloge `Clock.fixed(…)` avec un fuseau **explicite** (`ZoneOffset.UTC`). La dépendance au temps n'a pas disparu : elle a été **déplacée** à un seul endroit (`BakeryApp.main`), qui n'a plus de règle à tester.
