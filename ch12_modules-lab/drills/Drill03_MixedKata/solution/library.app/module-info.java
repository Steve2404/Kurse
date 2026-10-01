module library.app {
    // Une seule dependance directe : library.model arrive par transitivite.
    requires library.service;
    uses com.example.library.service.Catalog;
}
