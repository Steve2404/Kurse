package com.example.notify.app;

import com.example.notify.api.Notifier;
import java.util.ServiceLoader;

public class Main {
    public static void main(String[] args) {
        ServiceLoader<Notifier> loader = ServiceLoader.load(Notifier.class);
        int count = 0;
        for (Notifier notifier : loader) {
            notifier.send("Commande #42 expediee");
            count++;
        }
        System.out.println("Notifieurs trouves : " + count);
    }
}
