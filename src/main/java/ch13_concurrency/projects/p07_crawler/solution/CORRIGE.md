# Projet 7 — Corrigé étape par étape

> **Quand le lire :** ouvre **seulement la section de l'étape** que tu viens de terminer. Le programme complet est dans ce dossier.
>
> Les résultats ci-dessous ont été obtenus en direct avec **JDK 17** (17.0.18).

---

## Étape 1 — Le robot

**Le code :** `Crawler.java`. Chaque niveau est une liste de `Callable` passée à `invokeAll`, qui attend qu'elles soient **toutes** finies. Les `Future` sont ensuite lus dans l'ordre : le niveau suivant est donc construit dans un ordre stable.

**Question — pourquoi `if (!visited.contains(l)) visited.add(l)` est-il faux ?** Entre le test et l'ajout, un autre thread peut ajouter le même lien : les deux threads le croient nouveau, et la page est traitée **deux fois**. Vérifié avec 8 threads qui testent puis ajoutent les mêmes 100 000 nombres (3 lancements) : l'ensemble finit avec 100 000 éléments, mais les « premiers ajouts » comptés sont **297 080**, **236 585**, puis **222 436** au lieu de 100 000. `visited.add(l)` seul fait le test et l'ajout **atomiquement**, et rend `true` à un seul thread.

---

## Étape 2 — Le programme

**Question — pourquoi 80 découvertes, mais 63 téléchargées ?** Les niveaux 0 à 4 (`Data.MAX_DEPTH`) sont téléchargés : 1 + 3 + 9 + 22 + 28 = **63** pages. Les liens trouvés **sur** le niveau 4 sont ajoutés à `visited` (ils sont donc **découverts**), mais la boucle s'arrête avant de les télécharger, puisque `depth` dépasserait `maxDepth`. Cela fait 80 − 63 = **17** pages connues mais jamais téléchargées. Les pages cassées, elles, comptent bien parmi les téléchargées : `fetch` les compte **avant** que `Data.links` ne lève son exception.

---

## Étape 3 — Le planificateur

**Question — pourquoi pas le nombre exact de battements ?** Il dépend de la **vitesse** de la machine et du moment exact de `cancel` : entre la fin de l'attente et l'annulation, il peut y avoir un battement de plus ou non. Une sortie exacte serait différente d'un lancement à l'autre : on affiche donc seulement « au moins 5 ».

**Question — `AtFixedRate` contre `WithFixedDelay` :**
- `scheduleAtFixedRate` vise des **départs réguliers** (toutes les 5 ms depuis le début). Si une exécution dure plus longtemps que la période, la suivante démarre **dès** que la précédente finit, pour rattraper le retard ;
- `scheduleWithFixedDelay` attend toujours 5 ms **après la fin** de l'exécution précédente : les départs s'espacent si les exécutions durent longtemps.

Dans les deux cas, deux exécutions d'une même tâche ne se chevauchent jamais.
