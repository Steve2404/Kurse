// SOLUTION - l'application. Elle ne requiert QUE library.service : library.model lui arrive par transitivite.
module library.app {
    requires library.service;
}
