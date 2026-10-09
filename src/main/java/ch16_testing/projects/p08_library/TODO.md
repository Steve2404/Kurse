# Projet 8 — **Capstone** : la médiathèque (tout le chapitre 16)

> Première fois ? Lis d'abord le mode d'emploi [`ch16_testing/PARCOURS.md`](../../PARCOURS.md).
>
> **Bloqué sur une étape ?** [`INDICES.md`](INDICES.md) donne deux indices repliés par étape, sans code. **Étape finie ?** [`solution/CORRIGE.md`](solution/CORRIGE.md) donne, étape par étape, le code de l'étape, les **réponses aux questions** et le résultat exact des **expériences**. N'ouvre que la section de l'étape que tu viens de faire.

**C'est le projet-bilan :** peu d'indices, des règles métier à transformer **toi-même** en tests. Personne ne te dit quels cas tester : c'est exactement le travail d'un développeur.

**Ce que tu dois savoir faire, et où le relire :**

| Tu dois… | Leçon à relire |
|---|---|
| écrire un test, vérifier une exception et son message | projet 1, étapes 2 et 3 |
| préparer chaque test avec `@BeforeEach`, ranger avec `@Nested` et `@DisplayName` | projet 2, étapes 1 et 3 |
| trouver les valeurs limites, écrire un tableau de cas | projet 1 étape 4, projet 3 étape 2 |
| figer la date avec `Clock` | projet 5, étape 2 |
| simuler une dépendance, vérifier un appel ou son absence, attraper un argument | projet 6, étapes 2 à 4 |
| comprendre un mutant qui survit | projet 1 étape 6, projet 4 étape 7 |

**Ce que TU crées :** dans `ch16_testing.projects.p08_library` :
- **`Member`**, **`Loan`**, **`Catalog`**, **`LoanRepository`**, **`MemberDirectory`**, **`Mailer`** et **`LibraryService`** (les signatures sont imposées) ;
- **`LibraryServiceTest`**.

**Règle du crescendo :** chapitres 1 à 15, plus JUnit et Mockito. Dans ton code : jamais l'heure réelle (`LocalDate.now()`, `LocalDateTime.now()`, `Instant.now()`), ni `double`, ni `float`. Dans tes tests : ni `System.out`, ni `Thread.sleep`.

> **🧰 Tes outils pour ce projet**
>
> - Tout ce que tu as utilisé aux projets 1 à 6.
> - **La couverture :** clic droit sur `LibraryServiceTest` → **More Run/Debug** → **Run 'LibraryServiceTest' with Coverage**. IntelliJ colore chaque ligne de `LibraryService` : vert = exécutée par au moins un test, rouge = jamais exécutée.

---

## Les règles de la médiathèque

**Les types :**
- `public record Member(String id, String name, boolean premium)` ;
- `public record Loan(String isbn, String memberId, LocalDate borrowed, LocalDate due)` : le livre doit revenir au plus tard le jour `due` (inclus) ;
- `public interface Catalog` : `int copies(String isbn)`, le nombre d'exemplaires (0 = livre inconnu) ;
- `public interface LoanRepository` : `List<Loan> activeLoansOfMember(String memberId)`, `List<Loan> activeLoansOfBook(String isbn)`, `void save(Loan loan)`, `void close(Loan loan, LocalDate returned)` (les listes ne contiennent que les prêts **en cours**) ;
- `public interface MemberDirectory` : `Optional<Member> find(String id)` ;
- `public interface Mailer` : `void send(String memberId, String text)` ;
- `public final class LibraryService`, constructeur `public LibraryService(Catalog catalog, LoanRepository loans, MemberDirectory members, Mailer mailer, Clock clock)`.

**L'amende** — `public static long fine(long daysLate, boolean premium)` :
- 20 centimes par jour de retard ;
- un adhérent **premium** a **3 jours** de retard **offerts** (on les retire avant de compter) ;
- au plus **1000** centimes ;
- un retard de 0 jour, ou négatif (rendu en avance), ne coûte rien.

**Emprunter** — `public Loan borrow(String memberId, String isbn)`. Les refus, **dans cet ordre** :
1. adhérent inconnu : `NoSuchElementException("membre inconnu : " + memberId)` ;
2. livre inconnu (0 exemplaire) : `NoSuchElementException("livre inconnu : " + isbn)` ;
3. un de ses prêts en cours est **en retard** (sa date `due` est **avant** aujourd'hui ; un prêt qui expire **aujourd'hui** n'est pas en retard) : `IllegalStateException("retard en cours : " + memberId)` ;
4. il a déjà **3** prêts en cours (**5** pour un premium) : `IllegalStateException("limite atteinte : " + memberId)` ;
5. tous les exemplaires sont prêtés : `IllegalStateException("plus d'exemplaire : " + isbn)`.

Sinon : un prêt d'aujourd'hui, pour **21 jours** (**28** pour un premium), **enregistré** avec `save`, et rendu.

**Rendre** — `public long giveBack(String memberId, String isbn)` :
1. adhérent inconnu : `NoSuchElementException("membre inconnu : " + memberId)` ;
2. aucun prêt en cours de ce livre pour cet adhérent : `NoSuchElementException("pret introuvable : " + memberId + " / " + isbn)` ;
3. sinon : **clos** le prêt avec la date du jour (`close`), calcule le retard en jours (`ChronoUnit.DAYS.between(due, aujourdhui)`) et l'amende ; si l'amende est **positive**, envoie `"Amende : " + montant + " centimes pour " + isbn` ; rends le montant.

---

## Tableau de bord

### ☐ Étape 1 — L'amende, en tableau

**👉 À toi :** les types, puis `fine`, puis un test paramétré qui couvre **toutes** les limites des règles de l'amende (pour les deux sortes d'adhérents). Chaque ligne a un nom lisible.

**❓ Question :** quelles sont les limites ? Fais la liste **avant** d'écrire le tableau.

### ☐ Étape 2 — Le décor : simulacres et horloge

**👉 À toi :** un `@BeforeEach` qui crée le service avec quatre simulacres et une horloge figée au **15 mai 2026** (à 10 h UTC).

Beaucoup de tests auront besoin des mêmes réponses (« `M1` est Ana », « le livre `ISBN-1` a 2 exemplaires »). Les préparer dans le `@BeforeEach` est tentant, mais Mockito strict refusera les réponses inutilisées par un test. Deux solutions : les préparer dans chaque test, ou les marquer **`lenient()`** : `lenient().when(…).thenReturn(…)` (« cette réponse peut ne pas servir »).

**❓ Question :** pourquoi ne peut-on pas utiliser `@InjectMocks` ici ?

### ☐ Étape 3 — Emprunter

**👉 À toi :** `borrow`, et ses tests, rangés dans un groupe `@Nested` : chaque refus (avec son message), chaque **limite** (2 prêts, 3 prêts ; 4 et 5 pour un premium ; un exemplaire libre sur deux, aucun), la durée de prêt des deux sortes d'adhérents, le prêt **enregistré**, et rien d'enregistré en cas de refus.

**❓ Questions :**
- Quel test prouve que l'**ordre** des refus est respecté ? Écris-le.
- Le 15 mai 2026 + 21 jours, puis + 28 jours : quelles dates ?

### ☐ Étape 4 — Rendre

**👉 À toi :** `giveBack` et ses tests, dans un groupe `@Nested` : à l'heure (aucun courriel), en retard (le bon prêt clos, avec la bonne date, et le courriel exact), un premium dans ses jours offerts, un prêt introuvable (rien de clos). Un adhérent qui a **deux** prêts en cours doit rendre **le bon**.

**🧪 Expérience :** lance tes tests **avec couverture**. Toutes les lignes de `LibraryService` sont-elles vertes ? Si oui, tes tests sont-ils forcément bons ?

### ☐ Étape 5 — Les mutants

**👉 À toi :** lance `Check` et tue les **16** mutants. Ici, pas d'indice tout de suite : pour chaque survivant, relis la règle concernée, cherche le cas qui manque, et écris-le. Le palier 2 de `INDICES.md` ne sert qu'en dernier recours.

---

## Checklist (vérifiée par `Check`)

- **Ton code :** les sept types aux signatures exactes, `static long fine(long daysLate, boolean premium)`, `Loan borrow(String memberId, String isbn)`, `long giveBack(String memberId, String isbn)`, `LocalDate.now(clock)`, `ChronoUnit.DAYS.between(` ; jamais `LocalDate.now()`, `LocalDateTime.now()`, `Instant.now()`, `double`, `float`.
- **Tes tests :** au moins **22** tests, `@ExtendWith(MockitoExtension.class)`, `@Mock`, `Clock.fixed(`, `@BeforeEach`, `@Nested`, `@DisplayName(`, `@ParameterizedTest`, `@CsvSource(`, `when(`, `verify(`, `never()`, `verifyNoInteractions(`, `ArgumentCaptor`, `assertThrows(`, `assertAll(` ; ni `System.out` ni `Thread.sleep`.
- **Les 16 mutants** sont tués.

---

## Ce que `Check` affiche quand tout est juste

```
=== Verification des tests de ch16_testing.projects.p08_library ===
[PASS] tes tests sur TON code : 22 tests, 22 reussis
[PASS] tes tests sur le code de REFERENCE : 22 tests, 22 reussis
[PASS] les tests de REFERENCE sur TON code : 22 tests, 22 reussis
   mutant 1 : tue (par followsTheRules [1 jour(s) de retard, premium false : 20 centimes])
   …
   mutant 16 : tue (par overdueIsReportedBeforeTheLimit [le retard passe avant la limite])
[PASS] mutants : 16/16 tues
--- API de ton code ---
[PASS] API : tous les elements vises sont utilises
--- API de tes tests ---
[PASS] API : tous les elements vises sont utilises

*** PROJET REUSSI : tes tests passent, attrapent tous les mutants, et ton code est juste. ***
```
