# Projet 3 — Corrigé étape par étape

> **Quand le lire :** ouvre **seulement la section de l'étape** que tu viens de terminer. Le programme complet est dans [`Rle.java`](Rle.java) et [`StreamLab.java`](StreamLab.java).
>
> Les résultats ci-dessous ont été obtenus en direct avec **JDK 17** (17.0.18).

---

## Étape 1 — RLE et Adler-32

**La compression :** les données sont faites de longues séries d'octets identiques : chaque série devient un couple `(compte, octet)`, d'où 59 800 → 922 octets.

**Question — pourquoi `read()` rend-il un `int` ?** Un octet peut prendre les 256 valeurs de 0 à 255 : il n'en reste aucune pour dire « fin du fichier ». En rendant un `int`, `read()` peut rendre les 256 valeurs d'octet (0 à 255) **et** -1 pour la fin. Un `byte` Java va de -128 à 127 : un octet valant 255 serait lu comme -1, et confondu avec la fin.

---

## Étape 2 — Texte, ajout, encodages

**Question — pourquoi `é` prend-il 2 octets en UTF-8, et 1 en ISO-8859-1 ?** ISO-8859-1 code **chaque** caractère sur 1 octet, mais ne connaît que 256 caractères. UTF-8 code les caractères ASCII (a–z, chiffres…) sur 1 octet, et les autres sur **2 à 4** octets, pour pouvoir représenter tous les caractères du monde. Vérifié : `été` fait 3 caractères, 5 octets en UTF-8, 3 en ISO-8859-1.

**Relire avec le mauvais encodage** : chaque octet est lu comme un caractère ISO-8859-1. Les 4 caractères accentués de `Data.ACCENTS`, codés sur 2 octets chacun, deviennent 2 caractères bizarres chacun : 10 caractères deviennent **14**.

---

## Étape 3 — `mark`/`reset`, données typées

**`donnees.bin` fait 17 octets :** `writeInt` écrit 4 octets, `writeDouble` 8, et `writeUTF("fin")` 2 octets de longueur plus 3 octets de texte. 4 + 8 + 2 + 3 = 17.

**Expérience 1 — sans try-with-resources sur le `BufferedOutputStream` :** vérifié avec 59 800 octets écrits : sans `close()`, le fichier ne fait que **57 344** octets, soit 7 blocs de 8 192 (la taille du tampon). Le reste attend dans le tampon, qui n'est vidé sur le disque qu'à la fermeture (ou par `flush()`). Après `close()`, il fait bien 59 800 octets.

**Expérience 2 — `System.console()` :** elle vaut `null` quand le programme n'a **pas** de console interactive, ce qui est le cas dans la fenêtre Run d'IntelliJ (vérifié aussi dans notre environnement de test, sans terminal interactif : `null`). Lancé dans un vrai terminal, `System.console()` rend un objet `Console`, qui permet par exemple de lire un mot de passe sans l'afficher.
