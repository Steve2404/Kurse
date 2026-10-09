# Projet 5 — Les e-mails de la boulangerie (builder, fabriques statiques, objets immuables)

> Première fois ? Lis d'abord le mode d'emploi [`ch18_design/PARCOURS.md`](../../PARCOURS.md).
>
> **Bloqué sur une étape ?** [`INDICES.md`](INDICES.md) donne deux indices repliés par étape, sans code. **Étape finie ?** [`solution/CORRIGE.md`](solution/CORRIGE.md) donne, étape par étape, le code de l'étape, les **réponses aux questions** et le résultat exact des **expériences**. N'ouvre que la section de l'étape que tu viens de faire.

**Notions visées (chapitre 18) :**
- les patrons de **création** : comment fabriquer un objet proprement ;
- les **constructeurs télescopiques** et les **setters** : pourquoi ils posent problème ;
- la **fabrique statique** (`EmailAddress.of`, `Email.welcome`) : un nom, une normalisation, un **cache** (le poids-mouche, *flyweight*) ;
- le patron **Builder** : une classe imbriquée, des méthodes qui s'enchaînent, une validation **complète** au `build()` ;
- l'**objet immuable** : constructeur privé, champs `final`, **copies défensives** ;
- `toBuilder()` : fabriquer une **variante** sans toucher à l'original.

**Ce qui est FOURNI :** `Data.java` contient `LegacyEmail`, l'ancienne classe des e-mails : trois constructeurs (2, 4 et 5 paramètres) et des setters. Son `main` montre trois surprises. Tu ne modifies pas ce fichier.

**Ce que TU crées :** dans `ch18_design.projects.p05_messages` : `EmailAddress`, `Priority`, `Attachment`, `Email` (avec sa classe imbriquée `Email.Builder`), et tes tests (par exemple `EmailTest`).

**Règle du crescendo :** chapitres 1 à 17, JUnit et Mockito. Pas de `System.out` ni de `Thread.sleep` dans tes tests. Pas de setter (`public void set…`) dans ton code, et aucune méthode de plus de **12 lignes**.

---

## Tableau de bord

### ☐ Étape 1 — Les trois surprises du legacy

**📖 La leçon : les constructeurs télescopiques.** Quand un objet a beaucoup de champs, dont certains facultatifs, on écrit souvent un constructeur à 2 paramètres, puis à 4, puis à 5… : des constructeurs **télescopiques**. À l'appel, `new LegacyEmail("a", "b", "c", "d", true)` ne dit **rien** : quel paramètre est le sujet ? que veut dire `true` ? Deux `String` inversés compilent sans erreur. Et pour les champs facultatifs, on ajoute des **setters** : l'objet existe alors **à moitié rempli** entre sa création et le dernier setter, et n'importe qui peut le modifier plus tard.

**👉 À toi :** lance `Data` et lis `LegacyEmail`.

**❓ Questions :**
- Pour chaque surprise (1, 2 et 3), explique ce qui se passe et pourquoi c'est dangereux.
- Pour ajouter un champ facultatif `cc`, combien de constructeurs faudrait-il dans le legacy pour couvrir toutes les combinaisons des champs facultatifs (`subject`, `body`, `urgent`, `cc`) ?

### ☐ Étape 2 — La fabrique statique : `EmailAddress.of`

**📖 La leçon : nommer la création.** Une **fabrique statique** est une méthode `static` qui rend une instance : `List.of(…)`, `Optional.empty()`, `LocalDate.of(2026, 10, 9)`. Par rapport à `new`, elle a trois avantages :
1. elle a un **nom** (`Optional.empty()` se lit mieux que `new Optional(null)`) ;
2. elle peut **préparer** l'entrée avant de construire (nettoyer, convertir) ;
3. elle n'est **pas obligée de créer** un nouvel objet : elle peut rendre un objet **déjà existant**, gardé dans un cache. C'est le patron **poids-mouche** (*flyweight*) : des milliers d'e-mails envoyés à la même adresse partagent **un seul** objet `EmailAddress`.

**👉 À toi :** `public record EmailAddress(String value)` :
- le constructeur compact refuse une valeur qui ne correspond pas à `[a-z0-9._-]+@[a-z0-9.-]+\.[a-z]{2,}` : `IllegalArgumentException("adresse invalide : " + value)` ;
- `public static EmailAddress of(String raw)` : enlève les espaces autour (`strip()`), met en minuscules, puis rend l'objet du **cache** (une `ConcurrentHashMap` statique et `computeIfAbsent`) ;
- `toString()` rend l'adresse seule.

**🧪 Les tests :** `of(" Ada@Example.ORG ")` et `of("ada@example.org")` rendent **le même objet** (`assertSame`) ; deux adresses différentes ne le sont pas ; un `@ParameterizedTest` sur des adresses invalides (`"ada"`, `"ada@example"`, `"ada@@example.org"`, `"a b@example.org"`, `"@example.org"`, `"ada@example.o"`).

**🧪 Expérience :** essaie de rendre le constructeur du record **privé** (`private EmailAddress { … }`) pour obliger tout le monde à passer par `of`. Que dit le compilateur ? Remets-le public.

**❓ Questions :**
- Vu l'expérience, que peut encore faire un appelant qui écrit `new EmailAddress("Ada@example.org")` ? Est-ce grave ici ?
- Le cache ne se vide jamais. Dans quel cas cela poserait-il un problème ?

### ☐ Étape 3 — L'e-mail immuable et son builder

**📖 La leçon : le patron Builder.** Un **builder** est un objet **mutable et temporaire** qui sert à préparer un objet **immuable**. On l'obtient par une méthode statique, on l'enchaîne (chaque méthode rend `this`), et l'on termine par `build()`, qui **valide tout** et crée l'objet final. L'appel se lit comme une phrase, chaque valeur a son nom, les champs facultatifs se sautent, et un objet invalide **ne peut pas exister**.

```java
Email email = Email.builder().from("contact@boulangerie.fr").to("ada@example.org")
        .subject("Promo").priority(Priority.HIGH).build();
```

Pour que l'objet soit vraiment **immuable** : classe `final`, champs `private final`, **constructeur privé** (seul le builder l'appelle), pas de setter, et des **copies** des listes (`List.copyOf`) : sinon le builder, qui continue à vivre après `build()`, pourrait encore modifier l'e-mail construit (la surprise 3).

**Exemple sur un autre sujet :** `HttpRequest.newBuilder().uri(…).header("Accept", "text/html").GET().build()` dans le JDK (Java 11).

**👉 À toi :**
- `public enum Priority { LOW, NORMAL, HIGH }` ;
- `public record Attachment(String name, int sizeKb)` : refuse une taille `< 1` (`"taille invalide : " + sizeKb`) ; `toString()` rend `menu.pdf (120 Ko)` ;
- `public final class Email` : les champs `from` (`EmailAddress`), `to` et `cc` (`List<EmailAddress>`), `subject`, `body`, `attachments` (`List<Attachment>`), `priority`, et un accesseur pour chacun (`from()`, `to()`…) ; un constructeur **privé** `Email(Builder b)` qui **copie** les listes ;
- `public static Builder builder()` et la classe imbriquée `public static final class Builder` (constructeur privé) :
  - `from(EmailAddress)` et `from(String)`, `to(String...)` et `cc(String...)` (qui **ajoutent**, avec `EmailAddress.of`), `subject(String)`, `body(String)`, `attach(String name, int sizeKb)`, `priority(Priority)` : chacune rend `this` ;
  - valeurs par défaut : sujet et corps vides (`""`), priorité `NORMAL` ;
  - `public Email build()` valide, **dans cet ordre**, et lance une `IllegalStateException` avec le message : `"expediteur manquant"`, `"aucun destinataire"`, `"sujet vide"` (un sujet fait d'espaces est vide), `"pieces jointes trop lourdes : " + total + " Ko"` (au plus 10 000 Ko au total, la limite **comprise**), `"destinataire en double : " + adresse` (la première adresse qui apparaît deux fois dans `to` **et** `cc` réunis).

**🧪 Les tests :** chaque refus avec son message ; 10 000 Ko acceptés, 10 001 refusés ; et l'**indépendance** : construis un e-mail, continue à modifier le builder (un destinataire, une pièce jointe, le sujet), construis un second e-mail : le premier n'a **pas** bougé, et ses listes refusent `add` et `clear`.

**❓ Questions :**
- Pourquoi `build()` lance-t-il une `IllegalStateException` et pas une `IllegalArgumentException` ?
- Qu'est-ce qui empêche un autre développeur de créer un `Email` sans passer par `build()`, donc sans validation ?

### ☐ Étape 4 — Le rendu, `toBuilder()` et les fabriques d'e-mails

**👉 À toi :**
- `public String render()` : le texte de l'e-mail, une ligne par champ, les lignes facultatives **seulement** si elles ont un contenu :

```
De : contact@boulangerie.fr
A : ada@example.org, bob@example.org
Cc : chef@boulangerie.fr                                 (s'il y a des copies)
Sujet : Menu de la semaine
Priorite : LOW                                           (si la priorité n'est pas NORMAL)
Pieces jointes : menu.pdf (120 Ko), plan.png (80 Ko)     (s'il y en a)

Voir le menu en piece jointe.
```

  (une ligne vide, puis le corps, puis un saut de ligne final ; les adresses et les pièces jointes sont séparées par `", "`) ;
- `public Builder toBuilder()` : un builder **prérempli** avec toutes les valeurs de cet e-mail, pour en fabriquer une variante ;
- deux **fabriques statiques**, envoyées par `contact@boulangerie.fr` :
  - `static Email welcome(String to)` : sujet `"Bienvenue"`, corps `"Merci de votre inscription !"` ;
  - `static Email orderReady(String to, String orderId)` : sujet `"Commande " + orderId + " prete"`, corps `"Votre commande vous attend au comptoir."`, priorité `HIGH`.

**🧪 Les tests :** le rendu complet (avec un bloc de texte `"""`), le rendu minimal (aucune ligne facultative), une variante faite avec `toBuilder()` (l'original ne change pas, la variante garde **tout** le reste : copies, corps, pièces jointes, priorité), et les deux fabriques.

**❓ Question :** `render()` mélange-t-il deux responsabilités (le contenu de l'e-mail et sa présentation) ? Quand faudrait-il la sortir dans une classe à part (projet 1) ?

### ☐ Étape 5 — Les mutants

**👉 À toi :** lance `Check`. Les 16 mutants enlèvent une validation, cassent une copie défensive, oublient un champ dans `toBuilder`, changent une valeur par défaut ou le cache des adresses.

---

## Checklist (vérifiée par `Check`)

- **Ton code :** `record EmailAddress(`, `static EmailAddress of(String`, `computeIfAbsent(`, `enum Priority`, `record Attachment(`, `public final class Email`, `private Email(Builder`, `public static final class Builder`, `public static Builder builder()`, `public Builder toBuilder()`, `public Email build()`, `return this;`, `static Email welcome(`, `static Email orderReady(`, `List.copyOf(` ; pas de `public void set…`, rien de `Legacy`.
- **La conception :** aucune méthode de plus de **12 lignes** ; pas de `public Email(` ; `Email.java` contient `private Builder()`.
- **Tes tests :** au moins **12** tests, `Email.builder()`, `toBuilder()`, `assertSame(`, `assertThrows(`, `@ParameterizedTest`, un bloc de texte `"""` ; ni `System.out` ni `Thread.sleep`.
- **Les 16 mutants** sont tués.

---

## Ce que `Check` affiche quand tout est juste

```
=== Verification des tests de ch18_design.projects.p05_messages ===
[PASS] tes tests sur TON code : … tests, … reussis
[PASS] tes tests sur le code de REFERENCE : … tests, … reussis
[PASS] les tests de REFERENCE sur TON code : 14 tests, 14 reussis
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
