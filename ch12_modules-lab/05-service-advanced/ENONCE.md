# Lab 05 - Service avance : plusieurs fournisseurs, methode provider(), ServiceLoader.stream() (niveau : avance)

Rappel express du decoupage en "boites magiques" : voir Lab01/ENONCE.md.
Prerequis : le Lab04 (les 4 parties d'un service).

## Le probleme, explique comme a un tout petit enfant

Un comparateur de prix de livraison ne connait AUCUN transporteur a
l'avance : il demande au JDK "qui sait donner un prix de livraison ?"
(`ServiceLoader`), et chaque transporteur present repond. Brancher un
nouveau transporteur = ajouter un module, sans toucher au comparateur.

Quatre modules :

- `shipping.api` : l'interface `ShippingRate` (`carrier()`, `price(kg)`), exportee.
- `shipping.post` : `PostRate` (4 + 1.5 x kg). **Pas de constructeur
  public** : seulement une methode `public static PostRate provider()`.
- `shipping.express` : `ExpressRate` (9 + 0.5 x kg), constructeur public classique.
- `shipping.app` : le comparateur.

Deux regles a retenir :

- Un fournisseur doit avoir SOIT un constructeur public sans argument,
  SOIT une methode `public static provider()` (prioritaire si elle existe).
- `ServiceLoader.load(X.class).stream()` rend des `Provider<X>` :
  `type()` donne la classe SANS creer d'objet ; `get()` cree
  l'objet. Piege verifie en direct : pour un fournisseur a methode
  `provider()`, `type()` rend le type de RETOUR declare de `provider()`
  (s'il etait `ShippingRate`, on lirait `ShippingRate`, pas `PostRate`).

## A faire

1. **TODO 1** `exercise/src/shipping.post/module-info.java` : annoncer `PostRate` comme fournisseur.
2. **TODO 2** `exercise/src/shipping.express/module-info.java` : annoncer `ExpressRate`.
3. **TODO 3** `exercise/src/shipping.app/module-info.java` : declarer la consommation du service.
4. **TODO 4** `Main.providerTypes()` : les noms simples des classes fournisseurs, tries, sans instancier.
5. **TODO 5** `Main.cheapest(kg)` : le moins cher, sous la forme `Poste (7.0)`.

Lance `./run.sh` apres chaque TODO : il dit lequel manque encore. Sortie attendue :

```
Fournisseurs trouves : 2
Fournisseurs : [ExpressRate, PostRate]
2 kg : Poste (7.0)
20 kg : Express (19.0)
```

## Ce qu'on remarque

- Les packages des fournisseurs ne sont PAS exportes : le comparateur
  ne connait que l'interface. C'est tout l'interet d'un service.
- Sans `uses`, ca compile, mais `ServiceLoader.load` lance
  `ServiceConfigurationError: ... does not declare 'uses'` a l'execution.
- Sans `provides`, pas d'erreur du tout : le fournisseur est simplement
  absent (0 ou 1 fournisseur trouve).

## Indices techniques (a lire seulement si bloque)

- `provides com.example.shipping.api.ShippingRate with com.example.shipping.post.PostRate;`
- `uses com.example.shipping.api.ShippingRate;`
- `ServiceLoader.load(ShippingRate.class).stream().map(p -> p.type().getSimpleName()).sorted().collect(Collectors.toList())`
- `...stream().map(ServiceLoader.Provider::get).min(Comparator.comparingDouble(r -> r.price(kg))).orElseThrow()`
