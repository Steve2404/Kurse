module notify.email {
    // Le fournisseur a besoin de l'interface qu'il implemente.
    requires notify.api;
    // provides ... with : l'implementation n'a PAS besoin d'etre exportee, ServiceLoader la trouve quand meme.
    provides com.example.notify.api.Notifier with com.example.notify.email.EmailNotifier;
}
