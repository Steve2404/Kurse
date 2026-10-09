# Projet 6 — Le service de paiement (Mockito)

> Première fois ? Lis d'abord le mode d'emploi [`ch16_testing/PARCOURS.md`](../../PARCOURS.md).
>
> **Bloqué sur une étape ?** [`INDICES.md`](INDICES.md) donne deux indices repliés par étape, sans code. **Étape finie ?** [`solution/CORRIGE.md`](solution/CORRIGE.md) donne, étape par étape, le code de l'étape, les **réponses aux questions** et le résultat exact des **expériences**. N'ouvre que la section de l'étape que tu viens de faire.

**Notions visées (chapitre 16) :**
- **Mockito**, la bibliothèque de doublures la plus utilisée en Java :
  - `@ExtendWith(MockitoExtension.class)`, `@Mock`, `@InjectMocks` ;
  - **préparer** une réponse : `when(…).thenReturn(…)`, `thenThrow(…)`, et les réponses **successives** ;
  - **vérifier** un appel : `verify(…)`, `times(n)`, `never()`, `verifyNoInteractions`, `verifyNoMoreInteractions` ;
  - **l'ordre** des appels : `InOrder` ;
  - **attraper** un argument pour l'examiner : `ArgumentCaptor` ;
  - les **matchers** : `anyString()`, `anyLong()`, `any()`, `eq(…)` ;
- **les valeurs par défaut** d'un simulacre, et **les stubs inutiles** que Mockito refuse ;
- **quand ne pas** utiliser de simulacre.

Côté algorithmes : une nouvelle tentative après un délai dépassé, un traitement par lot qui compte ses succès.

**Ce que TU crées :** dans `ch16_testing.projects.p06_payment` :
- **`Order`**, **`OrderStatus`**, **`ChargeResult`**, **`GatewayTimeoutException`**, **`PaymentGateway`**, **`OrderRepository`**, **`Mailer`** et **`PaymentService`** (leurs signatures sont imposées) ;
- **`PaymentServiceTest`**, tes tests. Ici, **aucune** doublure écrite à la main : Mockito les fabrique.

**Règle du crescendo :** chapitres 1 à 15, plus JUnit et Mockito. Pas de `System.out` ni de `Thread.sleep` dans tes tests.

Chaque étape commence par une **📖 leçon**, avec un exemple sur un autre sujet : une station météo qui prévient les agriculteurs.

> **🧰 Tes outils pour ce projet**
>
> - **Les imports :** `org.mockito.Mock`, `org.mockito.InjectMocks`, `org.mockito.junit.jupiter.MockitoExtension`, `org.junit.jupiter.api.extension.ExtendWith`, et en `import static` : `org.mockito.Mockito.*` (`when`, `verify`, `times`, `never`…) et `org.mockito.ArgumentMatchers.*` (`any`, `anyString`, `eq`…). **Alt+Entrée** les propose.
> - **Une ligne étrange au lancement :** `OpenJDK 64-Bit Server VM warning: Sharing is only supported for boot loader classes because bootstrap classpath has been appended`. C'est Mockito qui s'installe dans la JVM : ignore-la.
> - **Lire un échec Mockito :** ses messages tiennent sur plusieurs lignes, avec **Wanted** (ce que ton test attendait) et **Actual** ou **But was** (ce qui s'est vraiment passé). Clique sur les liens `-> at …` pour aller à la ligne.

---

## Tableau de bord

### ☐ Étape 1 — Le modèle et le chemin heureux

**👉 À toi :** les types, avec ces signatures exactes :
- **`public enum OrderStatus { NEW, PAID, FAILED }`** ;
- **`public record Order(String id, String customer, String email, long totalCents, OrderStatus status)`**, avec **`public Order withStatus(OrderStatus newStatus)`** qui rend une **nouvelle** commande (un record est immuable) ;
- **`public record ChargeResult(boolean approved, String transactionId, String reason)`** ;
- **`public class GatewayTimeoutException extends RuntimeException`**, avec un constructeur `(String message)` ;
- **`public interface PaymentGateway`** : `ChargeResult charge(String customer, long cents)` (elle peut lancer `GatewayTimeoutException`) ;
- **`public interface OrderRepository`** : `Optional<Order> find(String id)`, `List<Order> findByStatus(OrderStatus status)`, `void save(Order order)` ;
- **`public interface Mailer`** : `void send(String to, String subject, String body)` ;
- **`public final class PaymentService`**, constructeur **`public PaymentService(OrderRepository orders, PaymentGateway gateway, Mailer mailer)`**, et pour cette étape le cas qui marche de **`public String pay(String orderId)`** :
  1. trouve la commande ;
  2. `gateway.charge(customer, totalCents)` ;
  3. si c'est accepté : **enregistre** la commande au statut `PAID`, **puis** envoie à son `email` le sujet `"Recu " + orderId` et le texte `"Montant : " + totalCents + " ; transaction " + transactionId` ;
  4. rends le numéro de transaction.

### ☐ Étape 2 — Ton premier simulacre

**📖 La leçon : Mockito fabrique les doublures.** Au projet 5, tu as écrit un faux et un espion à la main. Mockito les fabrique **à partir de l'interface**, en une ligne :

```java
@ExtendWith(MockitoExtension.class)        // branche Mockito dans JUnit
class StationMeteoTest {

    @Mock Capteur capteur;                 // un simulacre de l'interface Capteur
    @Mock Alerte alerte;                   // un simulacre de l'interface Alerte
    @InjectMocks StationMeteo station;     // Mockito appelle new StationMeteo(capteur, alerte) pour toi

    @Test
    void unGelPrevientLesAgriculteurs() {
        when(capteur.temperature()).thenReturn(-3);      // PRÉPARER : « si on te demande la température, réponds -3 »

        station.releve();                                // AGIR

        verify(alerte).envoie("gel : -3 degres");        // VÉRIFIER : « a-t-on appelé envoie avec exactement ce texte ? »
    }
}
```

- `when(simulacre.methode(arguments)).thenReturn(valeur)` : un **bouchon** (projet 5), préparé en une ligne ;
- `verify(simulacre).methode(arguments)` : un **espion**, vérifié en une ligne ; les arguments doivent être **égaux** (`equals`) ;
- une méthode **non préparée** rend une valeur **vide** : `null`, 0, `false`, `Optional.empty()`, une liste vide…

**👉 À toi :** dans `PaymentServiceTest` : `@ExtendWith`, trois `@Mock`, un `@InjectMocks PaymentService`. Un test du paiement accepté pour la commande `new Order("C-1", "ana", "ana@mail.test", 4250, OrderStatus.NEW)` : prépare `find` et `charge`, vérifie le résultat rendu, l'enregistrement (la commande au statut `PAID`) et le courriel exact.

**🧪 Expériences** (supprime ensuite ces tests) :
1. Un test qui ne prépare **rien** et appelle `service.pay("C-1")` : que se passe-t-il, et pourquoi ? Ajoute aussi `assertEquals(Optional.empty(), orders.find("Z"))` et `assertNull(gateway.charge("x", 1))` : vert ou rouge ?
2. Un test qui prépare `when(gateway.charge("bob", 1)).thenReturn(null)` mais n'appelle jamais `charge` avec ces arguments. Recopie le **nom** de l'exception.

### ☐ Étape 3 — Vérifier l'ordre des appels : `InOrder`

**📖 La leçon : l'ordre compte.** `verify` dit **si** un appel a eu lieu, pas **quand**. Pour vérifier un ordre :

```java
InOrder ordre = inOrder(capteur, alerte);
ordre.verify(capteur).temperature();       // d'abord ceci…
ordre.verify(alerte).envoie(anyString());  // … puis cela
```

**👉 À toi :** dans ton test du paiement accepté, vérifie avec `InOrder` que la banque est appelée, **puis** le dépôt, **puis** le courriel.

**🧪 Expériences** (remets ensuite ton test) :
1. Remplace le texte attendu du courriel par `"Montant : 42,50"`. Lis le message : comment Mockito montre-t-il la différence ?
2. Vérifie l'ordre **à l'envers** (le courriel avant l'enregistrement). Recopie les deux premières lignes du message.

**❓ Question :** pourquoi l'ordre « dépôt **puis** courriel » est-il important pour le client ?

### ☐ Étape 4 — Les refus, et l'`ArgumentCaptor`

**📖 La leçon : examiner un argument.** Parfois, on ne veut pas vérifier un argument **en entier**, mais seulement une partie (son statut, son id…). Un `ArgumentCaptor` **attrape** l'objet passé, pour l'examiner après :

```java
ArgumentCaptor<String> texte = ArgumentCaptor.forClass(String.class);
verify(alerte).envoie(texte.capture());               // attrape ce qui a été passé
assertTrue(texte.getValue().startsWith("gel"));       // puis on l'examine
```

Les **matchers** acceptent n'importe quelle valeur d'un type : `anyString()`, `anyLong()`, `any()`. **Piège :** dans un même appel, soit **tous** les arguments sont des matchers, soit **aucun** ; pour une valeur précise au milieu de matchers, on l'enveloppe dans `eq(…)`.

`never()` et `verifyNoInteractions(…)` vérifient que **rien** n'a été appelé.

**👉 À toi :** complète `pay`, contrôles **dans cet ordre** :
1. commande introuvable : `NoSuchElementException("commande inconnue : " + orderId)` ;
2. statut `PAID` : `IllegalStateException("deja payee : " + orderId)`, **sans** appeler la banque ;
3. montant `<= 0` : `IllegalStateException("montant invalide : " + orderId)`, sans appeler la banque ;
4. refus de la banque (`approved` faux) : enregistre la commande au statut `FAILED`, envoie le sujet `"Paiement refuse"` avec le texte `reason`, et rends `"REFUSE"`.

**Tes tests :**
- le refus : prépare `charge` avec `anyString()` et `anyLong()` ; attrape la commande enregistrée avec un `ArgumentCaptor<Order>` et vérifie son statut **et** son id ; vérifie le courriel ;
- une commande déjà payée : l'exception, `verifyNoInteractions(gateway, mailer)` et `verify(orders, never()).save(any())` ;
- un montant de 0 refusé, un montant de **1** centime accepté ;
- une commande inconnue.

**🧪 Expérience :** écris `when(gateway.charge(anyString(), 4250)).thenReturn(null);`. Recopie le nom de l'exception et sa 2e ligne.

### ☐ Étape 5 — Les pannes : réponses successives

**📖 La leçon : une réponse différente à chaque appel.** On enchaîne les réponses : la 1re pour le 1er appel, la 2e pour le 2e, et la dernière se répète ensuite.

```java
when(capteur.temperature())
        .thenThrow(new CapteurMuetException())     // 1er appel : une panne
        .thenReturn(12);                           // 2e appel (et suivants) : 12
verify(capteur, times(2)).temperature();           // exactement deux appels
```

**👉 À toi :** dans `pay`, un délai dépassé (`GatewayTimeoutException`) est **retenté une fois**. Après **deux** délais dépassés : enregistre la commande `FAILED`, envoie le sujet `"Paiement impossible"` avec le texte `"Reessayez plus tard"`, et rends `"INDISPONIBLE"`. Pas de 3e essai.

**Tes tests :** un délai puis une acceptation (le numéro de transaction rendu, et `times(2)`) ; deux délais (`"INDISPONIBLE"`, `times(2)`, l'enregistrement et le courriel).

**❓ Question :** pourquoi vérifier `times(2)` dans le test des deux délais, alors que le résultat `"INDISPONIBLE"` est déjà vérifié ?

### ☐ Étape 6 — Le traitement par lot

**👉 À toi :** **`public int retryFailed()`** : pour chaque commande de `findByStatus(OrderStatus.FAILED)`, appelle `pay`, et compte celles qui sont **vraiment** payées (ni `"REFUSE"`, ni `"INDISPONIBLE"`). Rends ce nombre.

**Ton test :** trois commandes en échec : la banque accepte celle d'`ana`, refuse celle de `bob`, et ne répond pas pour `cid` (utilise `eq("ana")` avec `anyLong()`). Le résultat vaut 1 ; vérifie les trois courriels, puis `verifyNoMoreInteractions(mailer)` (aucun autre courriel).

### ☐ Étape 7 — Les mutants, et quand ne pas « mocker »

**📖 La leçon : ne pas tout simuler.** Un simulacre vérifie **comment** le code travaille (qui il appelle, dans quel ordre). C'est puissant, mais un test qui vérifie trop de détails casse dès qu'on réorganise le code, même sans bug. Les règles des pros :
- simule les **frontières** (la banque, le courriel, la base) ; pas tes propres petites classes (un `record`, une calculatrice : utilise les vraies) ;
- ne simule pas ce qui ne t'appartient pas (une `List`, une `String`) ;
- vérifie les appels qui **comptent** pour le métier (un débit, un courriel), pas chaque appel de lecture.

**👉 À toi :** lance `Check` et tue les **13** mutants. Bloqué ? Le palier 2 de `INDICES.md` dit, replié, ce que change chaque mutant.

### Expériences (hors sortie attendue)

1. Dans ton `pay`, échange l'enregistrement et le courriel du paiement accepté. Quel test échoue, avec quelle exception ?
2. Retire `@ExtendWith(MockitoExtension.class)`. Que valent alors tes champs `@Mock` ? Recopie l'erreur d'un test.

---

## Checklist (vérifiée par `Check`)

- **Ton code :** les huit types aux signatures exactes, `String pay(String orderId)`, `int retryFailed()`, `catch (GatewayTimeoutException`.
- **Tes tests :** au moins **9** tests, `@ExtendWith(MockitoExtension.class)`, `@Mock`, `@InjectMocks`, `when(`, `.thenReturn(`, `.thenThrow(`, `verify(`, `times(2)`, `never()`, `verifyNoInteractions(`, `verifyNoMoreInteractions(`, `ArgumentCaptor`, `.capture()`, `.getValue()`, `inOrder(`, `anyString()`, `anyLong()`, `eq(` ; ni `System.out` ni `Thread.sleep`.
- **Les 13 mutants** sont tués.

---

## Ce que `Check` affiche quand tout est juste

```
=== Verification des tests de ch16_testing.projects.p06_payment ===
OpenJDK 64-Bit Server VM warning: Sharing is only supported for boot loader classes because bootstrap classpath has been appended
[PASS] tes tests sur TON code : 9 tests, 9 reussis
[PASS] tes tests sur le code de REFERENCE : 9 tests, 9 reussis
[PASS] les tests de REFERENCE sur TON code : 9 tests, 9 reussis
   mutant 1 : tue (par alreadyPaidOrderNeverReachesTheBank)
   …
   mutant 13 : tue (par approvedPaymentSavesPaidOrderThenSendsAReceipt)
[PASS] mutants : 13/13 tues
--- API de ton code ---
[PASS] API : tous les elements vises sont utilises
--- API de tes tests ---
[PASS] API : tous les elements vises sont utilises

*** PROJET REUSSI : tes tests passent, attrapent tous les mutants, et ton code est juste. ***
```
