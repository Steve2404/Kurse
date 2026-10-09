# Revue de la demande de fusion « programme de fidélité » (revue de référence)

> Une revue modèle : chaque remarque dit **où**, **quoi**, **quand ça casse** et **comment corriger**. On commente le code, jamais la personne. Les numéros de ligne sont ceux de `Data.java`.

**Résumé pour l'auteur :** merci, la structure est claire et le scénario de démonstration aide beaucoup à comprendre. Mais en l'état, je ne peux pas l'accepter : plusieurs défauts donnent des résultats **faux sans aucun message** (des points perdus, un import incomplet), et le service ne supporte ni plusieurs fils ni plusieurs instances, deux exigences du cahier des charges. Je propose une version corrigée, avec un test par remarque.

---

## Bloquant : le résultat est faux

1. **Ligne 37 : `equals` sans `hashCode`.** Deux clients égaux ont des `hashCode` différents : un `HashSet` en garde deux (la démo l'affiche : `2 client(s)`), et un `HashMap<PrCustomer, …>` ne retrouve pas le client. *Correction :* un `record`, qui écrit les deux, toujours d'accord.

2. **Lignes 50 et 104-105 : l'argent en `double`.** `0.1 + 0.2` donne `0.30000000000000004` (la démo l'affiche). Sur des milliers d'achats, les centimes dérivent, et une comparaison `total == 100.0` peut échouer. *Correction :* des centimes dans un `long` ; à l'import, `BigDecimal` pour convertir le texte exactement.

3. **Lignes 75 et 78 : `==` entre chaînes.** Vrai pour deux littéraux (la démo du code écrit en dur donne 100 points), faux pour un code tapé par un client ou lu dans un fichier (50 points au lieu de 100). *Correction :* `"DOUBLE".equals(code)` ; et une `enum` pour les paliers.

4. **Ligne 97 : `>` au lieu de `>=`.** Le cahier des charges dit GOLD **à partir de** 1 000 points : avec exactement 1 000, le client reste SILVER (la démo l'affiche). Même erreur pour SILVER à 300. *Correction :* `>=`, et des tests **aux limites** (299, 300, 999, 1 000).

5. **Ligne 74 : `Math.round`.** 9,99 € donnent 10 points ; le cahier des charges dit « par euro **entier** » : 9. *Correction :* une division entière des centimes par 100.

6. **Lignes 73 à 82 et 86 à 92 : les points bonus ne sont jamais crédités.** `record` calcule les doublements (GOLD, `DOUBLE`) et les **rend**, mais ne les garde pas : `balance` recalcule depuis les montants, sans bonus. Le client voit « 100 points » à la caisse, et son solde n'en compte que 50. *Correction :* enregistrer les points **gagnés** de chaque achat, avec leur date.

7. **Ligne 88 : `plusDays(365)`.** Une année bissextile a 366 jours : des points gagnés le 15 janvier 2024 expirent le 14 janvier 2025 au lieu du 15. *Correction :* `plusYears(1)`.

8. **Lignes 112 à 123 : l'erreur avalée.** Une ligne fausse arrête la lecture **en silence** et rend ce qui a été lu (la démo : `1 achat(s) lus sur 3 lignes`). L'appelant croit l'import réussi. *Correction :* une exception qui donne le numéro de ligne et la raison ; aucun achat n'est importé si le fichier est faux.

## Bloquant : robustesse et exigences

9. **Lignes 65-66 : des `Map` `static`.** Toutes les instances partagent les mêmes clients (la démo : une nouvelle instance « voit » C1). Deux magasins se mélangent, et les tests s'influencent les uns les autres. *Correction :* des champs d'instance.

10. **Lignes 65-66 et 70 : `HashMap` et `ArrayList` partagées entre fils.** Le service sera appelé par le serveur HTTP en parallèle : des ajouts simultanés se perdent (ou lèvent une exception). *Correction :* `ConcurrentHashMap` et `CopyOnWriteArrayList` (ou des verrous).

11. **Ligne 88 : `LocalDate.now()`.** Une dépendance cachée : impossible de tester l'expiration sans attendre un an, et le résultat dépend du fuseau de la machine. *Correction :* une `Clock` injectée dans le constructeur.

12. **Ligne 115 : le flux n'est jamais fermé.** Même sans erreur, `reader` n'est pas refermé ; avec une erreur, encore moins. *Correction :* `try (BufferedReader reader = …)`.

13. **Ligne 101 : la liste interne est rendue.** L'appelant peut ajouter ou effacer des achats dans le dos du service. *Correction :* `List.copyOf(...)`.

14. **Lignes 68-71 : une double inscription efface l'historique.** `register` d'un identifiant existant remplace le client **et** sa liste d'achats par une liste vide. *Correction :* refuser une double inscription.

15. **`record` et `balance` : un client inconnu donne une `NullPointerException`.** Le message ne dit pas ce qui ne va pas. *Correction :* `NoSuchElementException("client inconnu : C9")`.

16. **`record` : aucune vérification de la date.** Un achat daté du futur est accepté, et ses points vivent un an de trop. *Correction :* refuser une date après aujourd'hui.

## Vie privée

17. **Ligne 43 : l'adresse dans `toString`.** Les `toString` finissent dans les journaux, lus par beaucoup de monde, et une adresse est une donnée personnelle (RGPD). *Correction :* masquer : `a***@example.org`.

## Suggestions (non bloquantes)

- **Ligne 118 : `split(";")`** ignore les champs vides en fin de ligne (`"C1;10;2026-10-01;"` donne 3 champs) ; `split(";", -1)` est plus prévisible.
- Les champs `public final` des classes de données : un `record` dit la même chose, en plus court.
- Le scénario de démonstration est utile : il mériterait de devenir des **tests**, qui tourneraient à chaque modification.
