// TODO : le ServiceLoader exige que ce module annonce QUEL service il
// compte consommer. Ajoute la ligne "uses" manquante (voir ENONCE.md).
module notify.app {
    requires notify.api;
}
