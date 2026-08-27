# Docker explique simplement (pour quelqu'un qui ne connait pas du tout)

## Le probleme que Docker resout

Le chapitre 15 (JDBC) demande de VRAIMENT parler a des bases de donnees
comme PostgreSQL et MySQL - pas juste lire leur documentation. Mais les
installer "en vrai" sur ta machine serait long, fragile, et risquerait
d'entrer en conflit avec d'autres logiciels deja installes.

## L'idee de Docker, avec une image simple

Imagine que tu veuilles gouter a un plat tres complique a cuisiner, sans
jamais salir ta cuisine ni acheter tous les ingredients toi-meme.
Docker, c'est comme recevoir ce plat DEJA CUISINE, dans une boite a
emporter scellee, prete a manger immediatement. Tu n'as rien prepare
toi-meme, et si tu n'aimes pas, tu jettes juste la boite - ta cuisine
(ton ordinateur) n'a jamais ete touchee.

Une telle "boite" s'appelle un **conteneur**. Une boite Docker pour
PostgreSQL contient PostgreSQL DEJA installe, DEJA configure, pret a
recevoir des connexions - comme si c'etait un tout petit ordinateur
a part, isole du reste de ta machine.

## Le fichier `docker-compose.yml`

C'est simplement une LISTE DE COURSES ecrite pour Docker : "prepare-moi
2 boites, avec tel logiciel dedans, tel nom d'utilisateur, tel mot de
passe, branchees sur tel port de ma machine." Une seule commande lit
cette liste et allume tout d'un coup :

```bash
cd jdbc-lab
docker compose up -d
```

(Le `-d` veut dire "en arriere-plan" - le terminal te rend la main tout
de suite, les boites continuent de tourner discretement.)

Pour verifier qu'elles tournent bien :

```bash
docker compose ps
```

Pour tout arreter (et liberer la memoire qu'elles utilisaient) :

```bash
docker compose down
```

## Pourquoi des ports "bizarres" (15432 et 13306) ?

PostgreSQL utilise HABITUELLEMENT le port 5432, et MySQL le port 3306.
Mais en verifiant cette machine avant de commencer, j'ai decouvert
qu'un AUTRE de tes projets fait deja tourner son PROPRE PostgreSQL sur
le port 5432 habituel. Si j'avais utilise le meme port pour ma boite,
les deux se seraient marche dessus (un seul programme peut "occuper"
un port a la fois).

Solution : ma boite PostgreSQL ecoute bien sur son port INTERIEUR
normal (5432, DANS la boite), mais je la branche sur le port 15432 de
TA machine plutot que 5432. Vu de l'exterieur (depuis ton code Java),
il faut donc se connecter a `localhost:15432` pour Postgres, et
`localhost:13306` pour MySQL - jamais les ports habituels, precisement
pour ne jamais toucher a ce qui tournait deja chez toi.

## Ce qu'il faut retenir pour les exercices

- Les exercices H2 (01 a 08) n'ont besoin d'AUCUNE de ces boites -
  H2 tourne directement "en memoire", integre au programme Java
  lui-meme.
- Les exercices multi-fournisseurs (09 a 15) ont besoin que les 2
  boites soient DEMARREES (`docker compose up -d`) AVANT de lancer
  leur `main()` - sinon la connexion echoue avec un message explicite
  te demandant de les demarrer.
- Rien de tout ca n'est permanent : `docker compose down` fait
  disparaitre les boites (et leurs donnees) sans laisser aucune trace
  sur ta machine.
