# Lab 01 - `module-info.java`, `exports` et `requires` (niveau : moyen)

Rappel express du decoupage en "boites magiques" : voir les exercices des
autres chapitres (`Exercise01_*.java`). Ici, la "boite" c'est un MODULE
entier plutot qu'une methode - le principe reste le meme : chaque module
a une responsabilite claire, et ne montre aux autres QUE ce qu'il choisit
explicitement de montrer.

## Le probleme, explique comme a un tout petit enfant

Un module Java, c'est comme une maison avec une porte d'entree fermee a
cle. A l'interieur, il peut y avoir plusieurs pieces (des *packages*),
mais les visiteurs (les autres modules) ne peuvent entrer QUE dans les
pieces dont la porte est explicitement ouverte avec `exports`. Meme si
`greeting.app` a la cle de la maison (`requires greeting.api` - "je sais
que cette maison existe et j'en ai besoin"), ca ne lui donne PAS le droit
d'entrer dans une piece que la maison n'a pas explicitement ouverte au
public.

## Ce qu'il y a dans ce lab

Deux modules :

- `greeting.api` contient une interface `Greeter` et une implementation
  `SimpleGreeter`, dans le package `com.example.greeting.api`.
- `greeting.app` a un `Main` qui utilise ces deux classes.

`greeting.app/module-info.java` declare deja `requires greeting.api;`
(le lien entre les deux modules existe). Mais dans `exercise/`,
`greeting.api/module-info.java` est VOLONTAIREMENT incomplet :

```java
module greeting.api {
}
```

## A faire

1. Lance `./run.sh` tel quel. Lis attentivement le message d'erreur du
   compilateur - il te dit EXACTEMENT ce qui manque, avec le nom du
   package et du module concernes.
2. Essaie de deviner, avant de regarder `solution/`, quelle ligne
   ajouter a `exercise/src/greeting.api/module-info.java` pour que
   `com.example.greeting.api` devienne visible depuis `greeting.app`.
3. Ajoute cette ligne toi-meme dans `exercise/src/greeting.api/module-info.java`,
   puis relance `./run.sh` - la partie EXERCICE doit maintenant
   compiler et afficher "Bonjour, Steve !".
4. Compare avec `solution/src/greeting.api/module-info.java` seulement
   apres avoir essaye.

## Ce qu'on remarque

`requires` et `exports` repondent a 2 questions COMPLETEMENT
differentes :

- `requires X` (cote de celui qui A BESOIN) : "je depends du module X."
- `exports pkg` (cote de celui qui EST utilise) : "ce package precis est
  ouvert a tout module qui me `requires`."

Un module peut tres bien `requires` un autre sans que CELUI-CI accepte
de lui montrer quoi que ce soit - c'est precisement le bug de ce lab.

## Indices techniques (a lire seulement si bloque)

- La ligne manquante est : `exports com.example.greeting.api;`
- Compilation multi-module : `javac -d mods --module-source-path src $(find src -name "*.java")`
- Execution : `java --module-path mods --module greeting.app/com.example.greeting.app.Main`
- Le message d'erreur exact a attendre AVANT correction :
  `package com.example.greeting.api is not visible (package com.example.greeting.api is declared in module greeting.api, which does not export it)`
