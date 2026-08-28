package com.example.notify.email;

import com.example.notify.api.Notifier;

public class EmailNotifier implements Notifier {
    @Override
    public void send(String message) {
        System.out.println("Email envoye : " + message);
    }
}
