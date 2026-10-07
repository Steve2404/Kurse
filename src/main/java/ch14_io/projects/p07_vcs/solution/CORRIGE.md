# Projet 7 (capstone) — Corrigé étape par étape

> **Quand le lire :** ouvre **seulement la section de l'étape** que tu viens de terminer. Le programme complet est dans [`Commit.java`](Commit.java), [`Repository.java`](Repository.java) et [`MiniGit.java`](MiniGit.java).
>
> Les résultats ci-dessous ont été obtenus en direct avec **JDK 17** (17.0.18).

---

## Étape 1 — Le dépôt

**Le principe de Git, en petit :**
- chaque contenu de fichier est rangé **une seule fois**, sous son **empreinte** (son *hash*) ;
- un commit ne recopie pas les fichiers : il note seulement « chemin → empreinte » ;
- revenir à un commit (`checkout`), c'est recopier les objets aux bons chemins.

**Le `diff`** utilise la plus longue sous-suite commune (programmation dynamique, comme le sac à dos du chapitre 12, projet 5) : les lignes communes restent, les autres sont marquées `-` (retirée) ou `+` (ajoutée).

---

## Étape 2 — Le scénario

**Question — pourquoi 5 objets après 3 commits de 2 fichiers ?** Un contenu **déjà stocké** n'est pas recopié. Vérifié dans la sortie : 2 objets après le commit 1, 4 après le 2, 5 après le 3. Le commit 2 ajoute seulement les contenus **nouveaux** (`notes.txt` et la nouvelle `recette.txt`) ; le commit 3 n'en ajoute qu'un (la recette sans œufs), car `notes.txt` n'a pas changé : même contenu, même empreinte.

**Question — pourquoi `status` est-il vide juste après `checkout 1` ?** `checkout 1` remet les fichiers **exactement** dans l'état du commit 1, et `HEAD` vaut 1. `status` compare les fichiers au commit courant : ils sont identiques, d'où `status : {}`.
