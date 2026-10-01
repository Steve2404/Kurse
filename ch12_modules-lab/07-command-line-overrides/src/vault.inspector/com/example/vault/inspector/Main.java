package com.example.vault.inspector;

import com.example.vault.api.Vault;
import com.example.vault.internal.Secrets;

import java.lang.reflect.Field;

public class Main {

    public static void main(String[] args) throws Exception {
        // 1. Un package NON exporte, utilise directement dans le code.
        System.out.println(Secrets.hint());

        // 2. Un champ PRIVE lu par reflexion profonde.
        Field code = Vault.class.getDeclaredField("code");
        code.setAccessible(true);
        System.out.println("code : " + code.get(new Vault()));

        // 3. Une classe d'un module que personne ne requiert, chargee par reflexion.
        Class<?> audit = Class.forName("com.example.vault.audit.Audit");
        System.out.println(audit.getMethod("stamp").invoke(null));
    }
}
