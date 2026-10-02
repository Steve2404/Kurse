// SOLUTION - le modele de configuration.
module config.model {
    // exports : les types publics sont utilisables a la COMPILATION et a l'execution (acces normal).
    exports config.model;
    // opens ... to : la REFLEXION PROFONDE (champs prives, setAccessible) est permise, mais seulement a config.binder.
    opens config.model to config.binder;
    // config.model.internal et config.model.secret : ni exportes, ni ouverts.
}
