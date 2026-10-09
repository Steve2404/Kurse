package ch19_final.drills.r02_jdbc;

import java.util.List;

/** FOURNI (ne pas modifier) : les scripts du schema de la banque de l'atelier ; le script i est la version i + 1. */
public final class Data {

    private Data() {
    }

    public static final List<String> SCRIPTS = List.of(
            "CREATE TABLE account (id BIGINT AUTO_INCREMENT PRIMARY KEY, owner VARCHAR(50) NOT NULL, "
                    + "cents BIGINT NOT NULL CHECK (cents >= 0))",
            "ALTER TABLE account ADD COLUMN version INT DEFAULT 0 NOT NULL");
}
