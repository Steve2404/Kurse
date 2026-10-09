package ch18_design.projects.p03_storage.solution;

import java.util.List;
import java.util.Optional;

/**
 * Ce que TOUT stockage sait faire : lire. Le CONTRAT (verifie par ReadableContractTest) :
 * - read d'une cle absente rend Optional.empty() ; d'une cle invalide, IllegalArgumentException ;
 * - keys() rend les cles triees, dans une liste NON modifiable.
 */
public interface ReadableStorage {

    Optional<String> read(String key);

    List<String> keys();
}
