module notify.email {
    requires notify.api;
    provides com.example.notify.api.Notifier with com.example.notify.email.EmailNotifier;
}
