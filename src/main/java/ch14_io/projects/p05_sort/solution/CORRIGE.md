# Projet 5 — Corrigé étape par étape

> **Quand le lire :** ouvre **seulement la section de l'étape** que tu viens de terminer. Le programme complet est dans [`ExternalSort.java`](ExternalSort.java) et [`SortLab.java`](SortLab.java).
>
> Les résultats ci-dessous ont été obtenus en direct avec **JDK 17** (17.0.18).

---

## Étape 1 — Créer et analyser le gros fichier

**Question — pourquoi pas le même `Stream` trois fois ?** Un stream ne s'utilise **qu'une fois** (chapitre 10) : après son opération terminale, il est fermé. Et `Files.lines` lit le fichier **au fur et à mesure** : pour le relire, il faut le rouvrir. Chaque parcours a donc son propre `Files.lines`, fermé par son try-with-resources.

---

## Étape 2 — Le tri externe

**Le principe :** le fichier est trop gros pour être trié en mémoire d'un coup (en vrai, il pourrait faire des gigaoctets). On trie des paquets de 7 000 lignes, puis on les **fusionne** : le tas ne contient jamais plus d'une ligne par paquet (9 ici), quelle que soit la taille du fichier. C'est la fusion du tri fusion (chapitre 5, projet 6), étendue à k listes.

---

## Étape 3 — Options d'ouverture

- `writeString` sans option crée le fichier, ou le **remplace** ;
- `APPEND` ajoute à la fin ;
- `CREATE_NEW` refuse un fichier existant : `FileAlreadyExistsException`.

**Expérience — `writeString` sans option sur un fichier existant :** il **remplace** tout le contenu. Vérifié : un fichier contenant `premier texte assez long`, réécrit avec `court`, contient ensuite `court` seul (5 octets). Il n'en reste rien de l'ancien contenu : les options par défaut sont `CREATE`, `TRUNCATE_EXISTING` et `WRITE`.
