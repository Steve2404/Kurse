# Projet 2 — Corrigé étape par étape

> **Quand le lire :** ouvre **seulement la section de l'étape** que tu viens de terminer. Le programme complet est dans [`Bank.java`](Bank.java) et [`BankApp.java`](BankApp.java).
>
> Les résultats ci-dessous ont été obtenus en direct avec **JDK 17** (17.0.18) et **H2 2.3.232**, sur la solution et sur des copies modifiées.

---

## Étape 1 — Ouvrir la banque

**Question — les comptes sont-ils déjà validés ?** Oui. Ils ont été insérés quand l'auto-commit était **encore actif** : chaque `INSERT` a été validé aussitôt. Couper l'auto-commit ensuite ne change que les ordres **suivants**.

---

## Étape 2 — Les virements

**Le cœur :**

```java
try {
    int seq = move(from, to, amount);
    conn.commit();                        // tout le virement devient définitif
    return "ok (journal #" + seq + ")";
} catch (SQLException e) {
    conn.rollback();                      // tout ce qui a été fait depuis le dernier commit est défait
    return "refuse : " + reason(e) + ", rien n'a change";
}
```

**Question — pour `D4>Z9:100`, qui annule le débit de D4 ?** Le **`rollback()`** du `catch`. Le débit a réussi, puis le crédit de Z9 a échoué (compte inconnu) : `rollback()` défait le débit.

**Expérience 1 — sans ce `rollback()`** (vérifié) : le débit de D4 **reste en attente** dans la transaction, et le `commit()` du virement **suivant** (`D4>C3:250`) le valide avec lui. L'argent disparaît : les soldes finissent à `D4=700` au lieu de 800, `total 1600`, `conserve false`, et le rejeu du journal ne retrouve plus les soldes.

**Question — qui détecte le solde insuffisant ?** La **base**, grâce à la contrainte `CHECK (balance >= 0)` : l'`UPDATE` qui rendrait le solde négatif est refusé avec le SQLState `23513`. Java ne fait que lire ce code.

---

## Étape 3 — Les paies groupées et le rejeu

**Question — pourquoi les deux lignes de journal de la 2e paie n'apparaissent-elles pas ?** Elles ont été insérées **dans** la transaction de la paie, et le `rollback()` les a défaites avec les virements.

**Quel `seq` pour le prochain virement réussi ?** **10**. Le journal contient 1 à 7 ; la 2e paie avait pris les numéros 8 et 9 avant d'être annulée, et une base **ne rend pas** les numéros consommés. Vérifié sur un petit essai : après une ligne 1, deux insertions annulées, puis une nouvelle insertion, les numéros présents sont `1` et `4`.

---

## Étape 4 — Ce que voit une autre connexion

**Question — pourquoi l'observateur ne voit-il pas le `+1000` ?** Tant qu'une transaction n'est pas validée, ses changements ne sont visibles que de **sa propre** connexion. L'observateur voit l'état **validé** : 450.

**Question — sans `setAutoCommit(true)`, à la fermeture de `conn` ?** Avec **H2**, le changement est **annulé** : vérifié avec deux connexions, une mise à jour non validée disparaît quand sa connexion se ferme. La norme JDBC ne fixe pas ce comportement : d'autres pilotes valident à la fermeture. D'où la règle : valide (`commit`) ou annule (`rollback`) **toujours** toi-même.

**Expérience 2 — `commit()` en auto-commit :** vérifié avec H2 : accepté, sans exception. La Javadoc de `commit()` autorise pourtant une `SQLException` dans ce cas, et d'autres bases la lèvent.

**Expérience 3 — vérifier le solde en Java :** entre la **lecture** du solde et l'**écriture**, une autre connexion peut faire un virement : les deux lisent « 100 », les deux débitent 80, et le solde finit négatif. La contrainte `CHECK` est vérifiée **par la base**, au moment de l'écriture : elle ne peut pas être contournée.
