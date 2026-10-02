// SOLUTION - fournisseur "email".
module events.email {
    requires events.api;
    provides events.api.Notifier with events.email.EmailNotifier;
}
