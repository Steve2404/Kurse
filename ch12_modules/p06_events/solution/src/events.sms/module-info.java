// SOLUTION - fournisseur "sms", cree par une methode provider().
module events.sms {
    requires events.api;
    provides events.api.Notifier with events.sms.SmsGateway;
}
