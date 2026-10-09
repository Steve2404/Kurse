# Projet 7 — La revue de code : relire, prouver, corriger

> Première fois ? Lis d'abord le mode d'emploi [`ch19_final/PARCOURS.md`](../../PARCOURS.md).
>
> **Bloqué sur une étape ?** [`INDICES.md`](INDICES.md) donne deux indices repliés par étape, sans code. **Étape finie ?** [`solution/CORRIGE.md`](solution/CORRIGE.md) donne, étape par étape, le code de l'étape, les **réponses aux questions** et le résultat exact des **expériences**. N'ouvre que la section de l'étape que tu viens de faire.

**Notions visées (chapitre 19) :**
- la **revue de code** (*code review*) : ce qu'on cherche, dans quel ordre, et comment on l'écrit ;
- une **liste de contrôle** de développeur senior : l'égalité, l'argent, les chaînes, les limites, le temps, les erreurs, les ressources, l'état partagé, la concurrence, les données personnelles ;
- **prouver** chaque défaut par un test, avant de le corriger ;
- reconnaître des défauts qui **marchent en démo** et cassent en production.

**Ce qui est FOURNI :** `Data.java` contient la **demande de fusion** (*pull request*) d'un collègue : le programme de fidélité de l'atelier (`PrCustomer`, `PrPurchase`, `PrLoyaltyService`). « Tout marche, j'ai essayé à la main », dit-il. Son `main` est sa démonstration. Tu ne modifies pas ce fichier.

**Ce que TU crées :** `REVIEW.md` (ta revue écrite, dans le dossier du projet), et dans `ch19_final.projects.p07_review` : `Customer`, `Purchase`, `Tier`, `PurchaseImporter`, `LoyaltyService`, `ReviewDemo`, et tes tests (par exemple `ReviewTest`).

**Règle du crescendo :** tout Java 17, JUnit et Mockito. Aucune méthode de plus de **18 lignes**. Dans ton code : ni `double`, ni `static final Map`, ni `LocalDate.now()` sans horloge, ni `plusDays(365)`, ni `== "`, ni `printStackTrace` ; `PurchaseImporter` n'avale pas les `IOException` ; `LoyaltyService` ne fait pas `new ArrayList`. Dans tes tests : ni `System.out`, ni `Thread.sleep`, ni `systemDefaultZone`.

**Le cahier des charges du programme de fidélité** (c'est lui qui fait foi, pas le code du collègue) :
1. un client a un identifiant, un nom et une adresse ; deux clients aux mêmes valeurs sont **le même** client, y compris dans un `HashSet` ;
2. un achat a un client, un montant **exact au centime**, une date (jamais dans le futur) et un code promo facultatif ;
3. **1 point par euro entier** dépensé (9,99 € donnent 9 points) ; un client **GOLD** gagne le double ; le code promo `DOUBLE` double aussi (les deux se cumulent : × 4) ;
4. les points d'un achat valent **un an** : gagnés le 15 janvier 2024, ils comptent jusqu'au 14 janvier 2025 inclus ;
5. les paliers : **GOLD à partir de 1 000** points valables, **SILVER à partir de 300**, sinon BRONZE ;
6. l'historique des achats d'un client se consulte, sans pouvoir être modifié de l'extérieur ;
7. l'import d'un fichier d'achats (`C1;19.99;2026-10-01` ou `C1;19.99;2026-10-01;DOUBLE`) s'arrête à la première ligne fausse, en disant **laquelle** et **pourquoi** ; le fichier est toujours refermé ;
8. le service sera appelé par le serveur HTTP, **par plusieurs fils à la fois** ; une application peut créer plusieurs services indépendants (un par magasin) ;
9. les journaux (`toString`) ne doivent jamais montrer une adresse en entier.

---

## Tableau de bord

### ☐ Étape 1 — Relire comme un senior

**📖 La leçon : à quoi sert une revue.** Avant d'entrer dans le code commun, chaque modification est relue par un collègue. Une revue cherche, dans cet ordre :
1. **le comportement** : le code fait-il ce que dit le cahier des charges, y compris aux **limites** (0, 1 000 pile, une année bissextile, une ligne vide) ?
2. **la robustesse** : que se passe-t-il quand ça va mal (une ligne fausse, un fichier, plusieurs fils, une deuxième instance) ?
3. **la sécurité et la vie privée** : injection, données personnelles, secrets ;
4. **la lisibilité** : noms, longueur, duplication (chapitre 18) ; on la regarde **en dernier**, parce qu'un code joli mais faux reste faux.

**📖 La leçon : la liste de contrôle.** Les défauts qui passent une démo ont des visages connus. Garde cette liste sous les yeux :

| Question | Le piège classique |
|---|---|
| `equals` sans `hashCode` ? | deux objets « égaux » deux fois dans un `HashSet` |
| de l'argent en `double` ? | `0.1 + 0.2 = 0.30000000000000004` |
| `==` entre deux `String` ? | vrai pour deux littéraux, faux pour un texte lu ou tapé |
| `>` ou `>=` ? | le cahier des charges dit « à partir de » |
| `LocalDate.now()` ? | une dépendance cachée (chapitre 18), intestable |
| des durées en jours ? | une année n'a pas toujours 365 jours |
| un `catch` qui ne fait rien ? | l'erreur disparaît, le résultat est faux en silence |
| une ressource ouverte sans `try (…)` ? | elle reste ouverte si une exception passe |
| un `get` qui rend une collection interne ? | l'appelant la modifie dans ton dos |
| un champ `static` modifiable ? | toutes les instances partagent tout |
| une `HashMap` ou une `ArrayList` touchée par plusieurs fils ? | des données perdues |
| un `toString` avec des données personnelles ? | elles finissent dans les journaux |

**👉 À toi :** lance `Data` et lis sa sortie. Puis relis `Data.java` ligne par ligne, avec la liste de contrôle et le cahier des charges.

**👉 Puis :** écris `REVIEW.md`, dans le dossier du projet (clic droit sur `p07_review` → **New → File**). Pour **chaque** défaut : la ligne, ce qui ne va pas, un **scénario réel** où ça casse, et la correction proposée. Écris pour un collègue : on commente le **code**, pas la personne (« cette méthode perd l'erreur », pas « tu as oublié »), et chaque remarque se termine par une solution.

**❓ Questions :**
- Combien de défauts as-tu trouvés ? (Il y en a au moins douze.)
- Le collègue a « essayé à la main ». Pourquoi chacun de ces défauts passe-t-il une démonstration ?

### ☐ Étape 2 — L'égalité, l'argent, les paliers

**📖 La leçon : un record règle trois défauts d'un coup.** Un `record` écrit `equals`, `hashCode` et `toString`, toujours d'accord entre eux. Le contrat de `Object` (chapitre 6) : deux objets égaux **doivent** avoir le même `hashCode`, sinon un `HashSet` les range dans deux cases différentes et ne les compare jamais.

**👉 À toi :**
- `public record Customer(String id, String name, String email)` : aucun champ `null` ; `toString` → `Customer[C1, Ada, a***@example.org]` (la première lettre, `***`, puis tout à partir du `@` ; sans `@`, ou avec `@` en premier : `***`) ;
- `public record Purchase(String customerId, long cents, LocalDate date, String promoCode)` : le montant en **centimes** ; un montant négatif → `IllegalArgumentException("montant negatif : -1")` ;
- `public enum Tier { BRONZE, SILVER, GOLD }` avec `static Tier of(int points)`, selon le cahier des charges (**à partir de**).

**🧪 Les tests (`ReviewTest`) :** deux clients égaux dans un `HashSet` (taille 1, même `hashCode`) ; les six paliers aux limites (0, 299, 300, 999, 1 000, 5 000) ; quatre `toString` masqués.

### ☐ Étape 3 — Le service : le temps, les chaînes, l'état

**📖 La leçon : `==` entre chaînes.** `==` compare les **objets**, pas leur contenu. Deux littéraux `"DOUBLE"` écrits dans le code sont le **même** objet (Java les range une seule fois : l'*interning*), d'où une démo qui marche. Un code tapé par le client, ou lu dans un fichier, est un **autre** objet, avec le même texte : `==` rend `false`. On compare toujours avec `equals`, en mettant le littéral à gauche (`"DOUBLE".equals(code)`), ce qui marche même si `code` est `null`.

**📖 La leçon : une année n'a pas 365 jours.** `date.plusDays(365)` à partir du 15 janvier 2024 donne le 14 janvier 2025 : 2024 est bissextile (29 février). `plusYears(1)` donne le 15 janvier 2025.

**👉 À toi :** `public final class LoyaltyService`, constructeur `(Clock clock)` :
- `void register(Customer customer)` : un identifiant déjà inscrit → `IllegalArgumentException("client deja inscrit : C1")` ;
- `int record(Purchase purchase)` : client inconnu → `NoSuchElementException("client inconnu : C9")` ; date après aujourd'hui → `IllegalArgumentException("achat dans le futur : 2025-01-15")` ; sinon les points du cahier des charges (le palier GOLD est celui **avant** l'achat), l'achat ajouté à l'historique, et rend les points gagnés ;
- `int balance(String customerId)` (les points encore valables), `Tier tier(String customerId)`, `List<Purchase> history(String customerId)` (une **copie** non modifiable), `long totalSpentCents(String customerId)` ;
- l'état dans des champs **d'instance** ; des `ConcurrentHashMap`, et des `CopyOnWriteArrayList` pour les listes de chaque client (sûres pour plusieurs fils) ; les points de chaque achat dans un petit record privé (points, date d'obtention).

**🧪 Les tests :** une horloge fixe **loin d'aujourd'hui** (le 14 janvier 2025) ; 10 + 20 centimes font 30 ; le code `DOUBLE` tapé (`new String("DOUBLE")`) ; GOLD gagne double (et × 4 avec le code) ; 9,99 € → 9 points, 0,99 € → 0, 1,50 € → 1 ; un achat du 15 janvier 2024 compte encore le 14 janvier 2025, celui du 14 janvier 2024 non ; un achat dans le futur ; l'historique non modifiable (et qui ne bouge pas quand le service continue) ; **deux services indépendants** ; un client inconnu ; une double inscription.

**❓ Question :** pourquoi l'horloge des tests est-elle en janvier **2025**, et pas aujourd'hui ?

### ☐ Étape 4 — L'import : les erreurs et les ressources

**📖 La leçon : une erreur avalée est pire qu'une erreur.** Le collègue attrape tout et rend « ce qui a été lu ». L'appelant croit l'import réussi : des achats manquent, des clients perdent leurs points, et personne ne saura jamais pourquoi. Une ligne fausse doit **arrêter** l'import avec un message qui permet de corriger le fichier : le numéro de ligne et la raison.

**📖 La leçon : toujours refermer.** Un flux ouvert garde une ressource du système (un fichier, une connexion). Sans `try (…)`, une exception au milieu le laisse ouvert : sous Windows, le fichier ne peut plus être supprimé ni réécrit, et sur un serveur, les descripteurs finissent par manquer.

**👉 À toi :** `public final class PurchaseImporter` (constructeur privé), `public static List<Purchase> read(Reader in) throws IOException` :
- le flux est **toujours** fermé (`try (BufferedReader reader = new BufferedReader(in))`) ;
- les lignes vides sont sautées ; les lignes sont numérotées à partir de 1 (les vides comptent) ;
- 3 ou 4 champs séparés par `;` (un 4e champ vide = pas de code), sinon `IllegalArgumentException("ligne 4 : 3 ou 4 champs attendus")` ;
- le montant en euros devient des centimes **exacts** : `new BigDecimal(texte).movePointRight(2).longValueExact()` (trois décimales, `abc` ou un montant négatif → `"ligne 2 : montant invalide : dix"`) ;
- une date illisible → `"ligne 1 : date invalide : 2025-13-01"`.

**🧪 Les tests :** cinq montants exacts (`19.99` → 1999, `0.29` → 29, `0.1`, `10`, `1234567.89`) ; six fichiers faux (avec le message exact, dont un sur la ligne 2) ; un import complet avec une ligne vide et un 4e champ vide ; un **lecteur espion** (une classe imbriquée qui étend `Reader` et note si `close()` a été appelé) : fermé après un succès, **et** après une erreur ; un code `DOUBLE` lu dans un fichier.

**🧪 Expérience :** dans une méthode jetable, affiche `(long) (Double.parseDouble("19.99") * 100)` et `(long) (Double.parseDouble("0.29") * 100)`. Puis la même chose avec `BigDecimal`.

### ☐ Étape 5 — Plusieurs fils

**👉 À toi :** rien de nouveau à écrire si l'étape 3 est juste : prouve-le.

**🧪 Le test :** 8 fils démarrés ensemble par un `CountDownLatch` (projet 4), qui enregistrent chacun 500 achats de 1 € pour le même client : l'historique contient **4 000** achats, et le total vaut 400 000 centimes.

**🧪 Expérience :** remplace **une** `CopyOnWriteArrayList` (celle des achats) par une `ArrayList`, et lance ce test plusieurs fois. Que se passe-t-il ? Remets-la.

**❓ Question :** le test vérifie le nombre d'achats et le total, mais pas le solde de points. Pourquoi le solde n'est-il pas une bonne valeur à vérifier ici ?

### ☐ Étape 6 — La démonstration, et les mutants

**👉 À toi :** `public final class ReviewDemo` avec `main` : le scénario de `Data.main`, rejoué avec ton code (une horloge système, c'est une démo) : le total de 10 + 20 centimes, les deux clients dans un `HashSet`, le code `DOUBLE` tapé sur un achat de 50 €, l'import du fichier à la ligne fausse (`import refuse : …`), le `toString` de Bob, le palier de 1 000 points.

**👉 Puis :** lance `Check`. Chacun des **16 mutants** remet dans ton code corrigé **un** défaut de la demande de fusion : si un mutant survit, c'est qu'un de tes tests ne prouve pas le défaut qu'il devait prouver.

**👉 Enfin :** compare ta `REVIEW.md` avec [`solution/REVIEW.md`](solution/REVIEW.md) : as-tu trouvé tous les défauts ? Tes remarques proposent-elles toutes une solution ?

---

## Checklist (vérifiée par `Check`)

- **Ton code :** `record Customer(`, `record Purchase(`, `enum Tier`, `final class PurchaseImporter`, `final class LoyaltyService`, `final class ReviewDemo`, `BigDecimal`, `longValueExact()`, `try (`, `plusYears(1)`, `LocalDate.now(clock)`, `ConcurrentHashMap`, `List.copyOf(`, `"DOUBLE".equals(` ; ni `double`, ni `static final Map`, ni `LocalDate.now()`, ni `plusDays(365)`, ni `== "`, ni `printStackTrace`.
- **La conception :** aucune méthode de plus de **18 lignes** ; `PurchaseImporter.java` ne contient pas `catch (IOException` ; `LoyaltyService.java` ne contient pas `new ArrayList`.
- **Tes tests :** au moins **35** tests, `HashSet`, `new String("DOUBLE")`, `Clock.fixed(`, `extends Reader`, `CountDownLatch`, `@ParameterizedTest`, `UnsupportedOperationException`, `assertThrows(` ; ni `System.out`, ni `Thread.sleep`, ni `systemDefaultZone`.
- **Les 16 mutants** sont tués.
- `REVIEW.md` n'est pas vérifiée par `Check` : c'est toi qui la compares avec la revue de référence.

---

## Ce que `Check` affiche quand tout est juste

```
=== Verification des tests de ch19_final.projects.p07_review ===
[PASS] tes tests sur TON code : … tests, … reussis
[PASS] tes tests sur le code de REFERENCE : … tests, … reussis
[PASS] les tests de REFERENCE sur TON code : 36 tests, 36 reussis
   mutant 1 : tue (par …)
   …
[PASS] mutants : 16/16 tues
--- API de ton code ---
[PASS] API : tous les elements vises sont utilises
[PASS] conception : toutes les regles de structure sont respectees
--- API de tes tests ---
[PASS] API : tous les elements vises sont utilises

*** PROJET REUSSI : tes tests passent, attrapent tous les mutants, et ton code est juste. ***
```
