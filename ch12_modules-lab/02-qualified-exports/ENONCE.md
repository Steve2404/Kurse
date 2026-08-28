# Lab 02 - Export qualifie (`exports ... to ...`) (niveau : moyen)

Rappel express du decoupage en "boites magiques" : voir Lab01/ENONCE.md.

## Le probleme, explique comme a un tout petit enfant

Dans Lab01, `exports pkg;` ouvrait la porte a TOUT LE MONDE, sans
exception. Un export QUALIFIE (`exports pkg to moduleX;`) ouvre la porte
a UNE LISTE PRECISE d'invites nommes - meme un module qui a bien
`requires` le module hote se voit refuser l'entree s'il n'est pas sur la
liste.

## Ce qu'il y a dans ce lab

- `pricing.engine` expose `Discount`/`TenPercentDiscount`, mais avec
  `exports com.example.pricing.engine to pricing.trusted;` -
  UNIQUEMENT le module `pricing.trusted` est invite.
- `pricing.trusted` et `pricing.untrusted` font TOUS LES DEUX
  `requires pricing.engine;` et essaient TOUS LES DEUX d'utiliser
  `Discount`.

## A faire

1. Lance `./run.sh`. `pricing.trusted` doit deja fonctionner.
   `pricing.untrusted` doit echouer a la compilation - lis bien le
   message : il nomme EXPLICITEMENT le module refuse
   ("...does not export it to module pricing.untrusted").
2. L'equipe "untrusted" vient d'etre officiellement autorisee a utiliser
   ce module. Modifie `exercise/src/pricing.engine/module-info.java`
   pour ajouter `pricing.untrusted` a la liste des modules autorises
   (la clause `to` accepte plusieurs noms, separes par une virgule).
3. Relance `./run.sh` - les DEUX consommateurs doivent maintenant
   compiler et s'executer.

## Ce qu'on remarque

`requires` (cote consommateur) et `exports ... to ...` (cote
fournisseur) sont 2 listes INDEPENDANTES qui doivent TOUTES LES DEUX
donner leur accord : `requires` dit "je veux entrer", `exports to` dit
"voici EXACTEMENT qui a le droit d'entrer". Etre sur SA PROPRE liste
(`requires`) ne suffit jamais si on n'est pas sur la liste D'ACCUEIL
(`to`).

## Indices techniques (a lire seulement si bloque)

- Ligne corrigee : `exports com.example.pricing.engine to pricing.trusted, pricing.untrusted;`
- Un export qualifie n'apparait PAS dans un `exports` "tout public" -
  si un TROISIEME module non liste essayait a son tour d'y acceder, il
  serait refuse exactement comme `pricing.untrusted` l'etait au depart.
