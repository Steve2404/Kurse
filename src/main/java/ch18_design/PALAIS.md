# 🏠 Palais mental — chapitre 18 : les plafonds des chambres 2 et 3

> **Avant :** lis une fois [`PALAIS_MENTAL.md`](../../../../PALAIS_MENTAL.md) et sa section « Le 2e circuit : les plafonds ». Choisis 12 points au plafond de la **chambre 2** et 12 au plafond de la **chambre 3** (le lustre, un coin, la tringle, le haut de l'armoire, le détecteur de fumée…), toujours dans le même sens.
> **Quand :** après le capstone du chapitre (en deux fois : la chambre 2, puis la chambre 3), puis avant chaque répétition des drills.
> **Comment :** lis la règle, lève les yeux, joue la scène 10 secondes, redis la règle à voix haute.
>
> La **chambre 2** garde les **principes** (SOLID et le refactoring) ; la **chambre 3** garde les **patrons** de conception.

---

## Le plafond de la chambre 2 : les principes (stations 1 à 12)

### 1. 👃 Le coin au-dessus de la porte — les odeurs du code

- **Image :** dans le coin, une **poubelle pendue** au plafond déborde : une méthode si longue qu'elle descend jusqu'au sol, des étiquettes `t`, `t2`, `x`, le même trognon recopié **neuf fois**, un `true` muet, et des `if (type.equals("PART"))` qui grouillent. Ça **sent**, et tu te bouches le nez.
- **À retenir :**
  - les odeurs : méthode longue, noms obscurs, code dupliqué, nombres magiques, code de type, paramètre booléen, responsabilités mêlées ;
  - une odeur n'est pas un bug : c'est un code **cher à changer** ;
  - la conception, c'est rendre la **prochaine** modification facile.
- **Mon image :** …

### 2. 🕸️ Le lustre — le filet : la caractérisation et le maître étalon

- **Image :** sous le lustre, un **filet de cirque**. Un acrobate (toi) refait le numéro de l'ancien (le legacy) ; un juge compare **300 sauts tirés au sort** avec ceux de l'ancien. Mais le tireur au sort a un **dé truqué** (`new Random(1, 2, 3…)` ne sort jamais POST ni EXPRESS) : on le remplace par un dé bien mélangé, `SplittableRandom`.
- **À retenir :**
  - on ne refactore **jamais** sans tests ; les tests de caractérisation figent ce que le code **fait**, même ses bizarreries ;
  - maître étalon : des centaines d'entrées au hasard, **graine fixe** par répétition, comparées à l'ancien code ;
  - le hasard ne tombe presque jamais **pile** sur un seuil : les limites, les refus et les nouveautés se testent **à la main** ;
  - regarder ce que le hasard tire vraiment.
- **Mon image :** …

### 3. 🪜 La poutre — les gestes du refactoring

- **Image :** sur la poutre, tu avances à **tout petits pas**, avec une lampe verte au bout de chaque pas (les tests). Une lampe rouge ? Tu **recules** d'un pas (Ctrl+Z), sans chercher. À ta ceinture, des outils gravés : **Maj+F6**, **Ctrl+Alt+M**, **Ctrl+Alt+C**, **Ctrl+Alt+V**.
- **À retenir :**
  - refactorer = changer la **forme** sans changer le **comportement** ;
  - renommer (Maj+F6), extraire une méthode (Ctrl+Alt+M), une constante (Ctrl+Alt+C), une variable (Ctrl+Alt+V) ;
  - un geste, les tests, un geste, les tests ; corriger un bug est une étape **à part**.
- **Mon image :** …

### 4. 💶 La tringle — l'objet valeur

- **Image :** sur la tringle pendent des **billets** `Money` qui refusent de devenir négatifs (ils crient « montant negatif ! ») et qui savent s'écrire eux-mêmes : `0,05`, jamais `0,5`.
- **À retenir :**
  - un `record` pour une notion (l'argent, une adresse) : immuable, comparé par valeur, il **refuse** les valeurs impossibles et **porte** ses calculs ;
  - `String.format("%d,%02d", …)`, l'arrondi `(c * p + 50) / 100` écrit **une** fois.
- **Mon image :** …

### 5. 🎭 Le haut des rideaux — remplacer le code de type par du polymorphisme

- **Image :** en haut des rideaux, des **acteurs masqués** : chacun sait son rôle (`label()`, `price()`) ; le metteur en scène ne demande plus jamais « qui es-tu ? ». Le rideau est **scellé** (`sealed … permits`) : personne d'autre ne monte sur scène.
- **À retenir :**
  - un `if` sur un code de type, partout, devient **un type par sorte** ;
  - une **fabrique** (le lecteur de lignes) est le **seul** endroit autorisé à faire le `switch` ;
  - une méthode `default` évite un `instanceof`.
- **Mon image :** …

### 6. 1️⃣ Le plafonnier — S : une responsabilité

- **Image :** le plafonnier a **trois ampoules** séparées : une qui lit (le lecteur), une qui calcule, une qui imprime. Changer la couleur de l'impression n'éteint pas le calcul.
- **À retenir :**
  - une classe = **une raison de changer** ;
  - un calcul séparé de la mise en page se teste sans comparer de texte ;
  - la **façade** garde la vieille signature et assemble.
- **Mon image :** …

### 7. 🚪 La trappe du grenier — O : ouvert/fermé et la stratégie

- **Image :** la trappe est **verrouillée** (fermée à la modification), mais un **monte-charge** permet d'y faire passer de nouveaux transporteurs (ouverte à l'extension) : le fret arrive par une nouvelle classe et **une ligne** dans `Shop`, le vélo n'existe que dans un test.
- **À retenir :**
  - ajouter un comportement sans rouvrir le code qui marche ;
  - Stratégie : une interface, une classe par façon de faire ; un **registre** (`Map` par code) remplace le `switch` ;
  - on ferme contre les changements **prévus**, pas contre tout (YAGNI).
- **Mon image :** …

### 8. 🧩 La moulure — la composition plutôt que l'héritage

- **Image :** la moulure est faite de **pièces de puzzle** qui s'emboîtent sur n'importe quel transporteur : « fragile », « douane ». Avec l'héritage, la moulure aurait **32 classes** ; avec les pièces, 7.
- **À retenir :**
  - l'héritage multiplie les classes quand les variantes se combinent ;
  - la composition ajoute de petits objets ; une stratégie d'une méthode s'écrit en **lambda** (`@FunctionalInterface`) ;
  - chaque surcharge se calcule sur le prix **de base**.
- **Mon image :** …

### 9. 🟥 La fissure — L : Liskov

- **Image :** une fissure en forme de **carré** qui prétend être un rectangle : quand tu tires sur sa largeur, sa hauteur bouge aussi, et l'aire tombe de 20 à 16. Dans la fissure, une archive hurle `UnsupportedOperationException` quand on lui demande d'écrire.
- **À retenir :**
  - un sous-type doit pouvoir remplacer son type **partout**, sans surprise ;
  - le signe : une implémentation qui lance `UnsupportedOperationException` ;
  - de bons types rendent l'erreur **impossible à compiler** (`Archive cannot be converted to WritableStorage`).
- **Mon image :** …

### 10. 📋 Le détecteur de fumée — I : des interfaces étroites, et le test de contrat

- **Image :** le détecteur n'a qu'**un bouton** : il ne demande que ce dont il a besoin. Sous lui, une **feuille de contrat** abstraite que chaque stockage signe en héritant : JUnit fait passer **tous** les tests du contrat à chacun.
- **À retenir :**
  - plusieurs petites interfaces (`ReadableStorage`, `WritableStorage extends ReadableStorage`) plutôt qu'une grosse ;
  - un client demande le **plus petit** contrat possible ;
  - le test de contrat : une classe de test **abstraite** avec une méthode abstraite qui fabrique l'objet ; une sous-classe par implémentation.
- **Mon image :** …

### 11. 📸 Le haut de l'armoire — la copie défensive

- **Image :** en haut de l'armoire, deux photos de la même pièce : une **vraie photo** (`Map.copyOf`) qui ne bouge plus, et une **fenêtre** (`Collections.unmodifiableMap`) où l'on voit encore les gens entrer. Et la photo mélange l'ordre des meubles à chaque fois qu'on la regarde.
- **À retenir :**
  - une vue n'est **pas** une copie : `Map.copyOf`, `List.copyOf` ;
  - l'ordre de `Map.copyOf` change d'un lancement à l'autre : trier si l'ordre compte ;
  - le test : modifier la source **après** la construction.
- **Mon image :** …

### 12. 🔌 La prise au plafond — D : l'inversion des dépendances

- **Image :** une **prise** au plafond (le port) : la boulangerie y branche n'importe quel appareil (mail, SMS, console). Une **horloge arrêtée** (`Clock.fixed`) pend au fil : dans les tests, il est toujours vendredi 10 h 15. Un seul tableau électrique (`BakeryApp`) choisit les vrais appareils.
- **À retenir :**
  - le métier déclare ses **ports** (des interfaces, dans sa langue) ; les **adaptateurs** les implémentent ;
  - tout arrive par le **constructeur** (injection) ; ni `new` d'un service, ni singleton, ni `LocalDateTime.now()`, ni `new Random()` dans le métier ;
  - la **racine de composition** est le seul endroit qui fait `new` des adaptateurs.
- **Mon image :** …

---

## Le plafond de la chambre 3 : les patrons (stations 1 à 12)

### 1. 🏭 Le lustre — la fabrique statique et le poids-mouche

- **Image :** le lustre est une **usine** qui ne fabrique pas deux fois la même ampoule : `of(" Ada@Example.ORG ")` et `of("ada@example.org")` rendent **la même** (assertSame).
- **À retenir :**
  - une fabrique statique a un **nom**, prépare l'entrée, et peut rendre un objet existant (`computeIfAbsent`) ;
  - le constructeur d'un record ne peut pas être privé ;
  - un cache sans limite peut devenir une fuite de mémoire.
- **Mon image :** …

### 2. 🧱 Le coin de la fenêtre — le builder

- **Image :** dans le coin, un **maçon** empile des briques nommées (`.from(…)`, `.to(…)`, `.subject(…)`), chacune rendant le mur pour la suivante (`return this`). Au `build()`, un inspecteur vérifie **tout** et refuse le mur incomplet (`IllegalStateException`).
- **À retenir :**
  - contre les constructeurs **télescopiques** et les setters ;
  - classe imbriquée `static final Builder`, constructeur privé, valeurs par défaut, `build()` valide dans un ordre fixe ;
  - `toBuilder()` pour fabriquer une variante.
- **Mon image :** …

### 3. 🧊 La poutre — l'objet immuable

- **Image :** la poutre est un **bloc de glace** : classe `final`, champs `final`, un constructeur privé, et des copies de toutes les listes. Le maçon continue à travailler à côté : la glace ne bouge pas.
- **À retenir :**
  - `final` partout, constructeur privé, pas de setter, `List.copyOf` ;
  - le test d'indépendance : modifier le builder **après** `build()`.
- **Mon image :** …

### 4. 🔌 La tringle — l'adaptateur

- **Image :** sur la tringle, un **adaptateur de prise** anglaise : d'un côté, des Fahrenheit et des codes de station ; de l'autre, des Celsius et des noms de villes. Il arrondit (`Math.round`) : 71 °F donne 22, et −11,67 donne −12.
- **À retenir :**
  - implémente **notre** interface et traduit vers la bibliothèque étrangère ;
  - le **seul** endroit qui connaît la bibliothèque ;
  - `(int)` tronque vers zéro, `Math.round` arrondit au plus proche.
- **Mon image :** …

### 5. 🗄️ Le haut de l'armoire — le proxy

- **Image :** un **majordome** garde les réponses dans un tiroir pendant 10 minutes : à 9 min 59, il répond lui-même ; à 10 min **pile**, il redemande. Une erreur, il ne la garde jamais.
- **À retenir :**
  - même interface que l'objet remplacé, il **contrôle l'accès** (cache, droits, création paresseuse) ;
  - valable **strictement avant** l'expiration (`isBefore`) ;
  - tester le temps : une horloge qu'on **avance à la main** (`extends Clock`).
- **Mon image :** …

### 6. 🪆 Le plafonnier — le décorateur, et l'ordre des enveloppes

- **Image :** des **poupées russes** suspendues : le journal dehors, le cache dedans, le secours, puis les tentatives autour de l'adaptateur. Tu les ouvres dans un autre ordre : le secours répond trop tôt, le journal ne voit plus qu'une demande.
- **À retenir :**
  - implémente l'interface **et** enveloppe un objet de cette interface ; ajoute son comportement, puis délègue ;
  - s'empile dans n'importe quel ordre, mais **l'ordre change le comportement** ;
  - relancer **la même** exception (`throw e`).
- **Mon image :** …

### 7. ☎️ La moulure — la chaîne de secours et le composite

- **Image :** un **standard téléphonique** passe l'appel de poste en poste jusqu'à ce que quelqu'un réponde ; si personne, il tend une liasse de toutes les excuses (`addSuppressed`). À côté, un **sac de sacs** : la moyenne de (10 et 20) et de 30 donne 23, pas 20.
- **À retenir :**
  - chaîne : essayer dans l'ordre, s'arrêter à la première réponse ;
  - composite : un groupe derrière la même interface, qui peut contenir des groupes ;
  - un nœud imbriqué compte pour **un**.
- **Mon image :** …

### 8. 📚 Le détecteur de fumée — la commande, annuler et refaire

- **Image :** deux **piles d'assiettes** au plafond : « fait » et « défait ». Annuler : l'assiette du dessus passe sur l'autre pile. Une **nouvelle** assiette ? La pile « défait » tombe et se brise (on vide « refaire »).
- **À retenir :**
  - une action devient un **objet** qui sait `execute` et `undo` ;
  - deux `Deque` (`push`, `pop`), une capacité qui oublie la plus ancienne ;
  - chaque commande ne garde que **ce qui a changé** (10 000 caractères au lieu de 49 995 000).
- **Mon image :** …

### 9. 📷 La trappe — le memento et la macro

- **Image :** dans la trappe, une **photo scellée** que seul le document peut ouvrir. Une macro enlève ses vêtements dans l'**ordre inverse** de celui où elle les a mis ; dans l'autre ordre, `tarte` devient `La te`.
- **À retenir :**
  - memento : une photographie **opaque** (constructeur privé, aucun accesseur) quand l'inverse est impossible ;
  - macro : défaire du dernier au premier (`for (i = size - 1; i >= 0; i--)`).
- **Mon image :** …

### 10. 📢 La fissure — l'observateur

- **Image :** la fissure est un **haut-parleur** : le document annonce chaque changement, et les abonnés écoutent. Un abonné qui s'en va **pendant** l'annonce faisait tout exploser (`ConcurrentModificationException`) : on annonce sur une **copie** de la liste.
- **À retenir :**
  - le sujet ne connaît que l'interface des abonnés ;
  - prévenir sur `List.copyOf(listeners)` ;
  - ne pas s'abonner dans son propre constructeur : une fabrique `attachTo` construit **puis** abonne.
- **Mon image :** …

### 11. 🚦 La prise au plafond — le patron État

- **Image :** un **feu tricolore** au plafond : chaque couleur sait ce qu'elle accepte. Par défaut, elle **refuse** (une méthode `default` qui lance l'exception) ; le carrefour (le contexte) ne fait que transmettre : `state = state.pay(this)`, sans aucun `if`.
- **À retenir :**
  - un état = un objet ; chaque classe n'écrit que **ses** transitions permises ;
  - le contexte délègue ; un refus lance **avant** le changement ;
  - tester **toutes** les cases du tableau (états × actions).
- **Mon image :** …

### 12. 🖼️ La tringle des rideaux — la méthode modèle

- **Image :** un **cadre doré** fixé au plafond (le squelette `final`) : en-tête, lignes, pied. Chaque sous-classe peint seulement l'intérieur des cases ; le pied est une case **facultative** (le crochet). Sur le cadre : « Ne nous appelez pas, nous vous appellerons. »
- **À retenir :**
  - squelette dans une méthode `final`, étapes `protected abstract`, crochet déjà écrit (souvent vide) ;
  - le bon usage de l'héritage : un algorithme fixe, de petites étapes variables ;
  - si les étapes se combinent ou changent à l'exécution : des stratégies.
- **Mon image :** …

---

## ⚡ La balade éclair

1. Chambre 2, station 2 : pourquoi `new Random(n).nextInt(4)` est-il un piège pour un maître étalon ?
2. Chambre 2, station 3 : que fais-tu quand un test devient rouge pendant un refactoring ?
3. Chambre 2, station 7 : que faut-il modifier pour ajouter un transporteur ?
4. Chambre 2, station 9 : quel est le signe le plus courant d'une violation de Liskov ?
5. Chambre 2, station 10 : comment un test de contrat est-il lancé sur chaque implémentation ?
6. Chambre 2, station 12 : quelles sont les cinq dépendances cachées qu'on ne met jamais dans le métier ?
7. Chambre 3, station 2 : pourquoi `build()` lance-t-il une `IllegalStateException` ?
8. Chambre 3, station 5 : à 10 minutes pile, le cache répond-il ?
9. Chambre 3, station 6 : où met-on le journal pour compter les demandes ? et les appels payants ?
10. Chambre 3, station 8 : que devient la pile « refaire » après une nouvelle action ?
11. Chambre 3, station 10 : pourquoi prévenir les abonnés sur une copie de la liste ?
12. Chambre 3, station 12 : pourquoi la méthode du squelette est-elle `final` ?
