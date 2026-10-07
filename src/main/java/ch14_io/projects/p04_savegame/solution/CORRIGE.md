# Projet 4 — Corrigé étape par étape

> **Quand le lire :** ouvre **seulement la section de l'étape** que tu viens de terminer. Le programme complet est dans ce dossier : `Entity`, `Item`, `Hero` et `SaveGame`.
>
> Les résultats ci-dessous ont été obtenus en direct avec **JDK 17** (17.0.18), sur la solution et sur de petits programmes d'essai.

---

## Étape 1 — Les classes

**`Hero extends Entity implements Serializable`** : la classe mère `Entity` n'est pas sérialisable. Ses champs (`origin`) ne sont donc **pas** sauvegardés.

---

## Étape 2 — Sauver, relire

**Question — pourquoi `origin` vaut-il `neuf` ?** À la relecture, Java ne rappelle **pas** le constructeur de `Hero` : il recrée l'objet à partir des octets sauvegardés. Mais pour la partie `Entity`, non sérialisable, il n'a rien de sauvegardé : il appelle le **constructeur sans argument** d'`Entity`, qui met `origin = "neuf"`, d'où `Entity 1` sur la ligne des constructeurs.

**Question — pourquoi `sessionMinutes` vaut-il 0 ?** Il est `transient` : il n'est pas sauvegardé. À la relecture, il reçoit sa valeur par défaut, 0. Son initialisation de champ n'est pas rejouée non plus.

**Le record :** pour un `record`, Java rappelle le **constructeur canonique** à la relecture : le constructeur compact s'exécute, d'où `Item 1`. **`Hero.heroes` reste 99** : un champ `static` appartient à la classe, il n'est pas sauvegardé avec l'objet.

**Question — et si `Entity` n'avait pas de constructeur sans argument ?** Vérifié avec une mère qui n'a qu'un constructeur `(int x)` : l'**écriture** réussit, mais la **relecture** échoue :

```
InvalidClassException : Fille; no valid constructor
```

« Pas de constructeur valable » : Java ne sait pas comment recréer la partie non sérialisable.

**Le refus :** `new Object()` n'est pas sérialisable : `NotSerializableException (java.lang.Object)`.

---

## Étape 3 — La pile d'annulation

**La copie profonde** : sérialiser puis relire crée un objet **entièrement neuf**, avec ses propres listes. Modifier la copie ne touche pas l'original (2 objets contre 3).

**Expérience — `serialVersionUID` changé de 1 à 2 :** vérifié en écrivant avec 1, puis en relisant avec 2 :

```
InvalidClassException : Hero; local class incompatible: stream classdesc serialVersionUID = 1, local class serialVersionUID = 2
```

« Classe locale incompatible : le fichier a été écrit avec la version 1, la classe actuelle est la version 2 ». Le `serialVersionUID` est le numéro de version du format : le changer rend illisibles les anciennes sauvegardes.
