package ch11_exceptions.drills.exercises;

import ch11_exceptions.ExerciseChecker;

import java.util.Locale;
import java.util.Set;

/**
 * DRILL 05 - Locale et ResourceBundle
 * ===================================
 *
 * Mode d'emploi : voir Drill01_ExceptionBasics. Bundle : Ledger.BUNDLE
 * ("ch11_exceptions.messages" : racine, fr, fr_CA ; voir Ledger).
 *
 *
 * -- Les TODO (forme visee entre crochets) --
 *
 * TODO 1  : fromConstant()        [constante] Locale.CANADA_FRENCH, rendue en texte -> fr_CA.
 * TODO 2  : fromConstructor()     [new Locale(langue, pays) normalise la casse] new Locale("FR", "ca") -> fr_CA.
 * TODO 3  : fromBuilder()         [Locale.Builder] langue de, region CH -> de_CH.
 * TODO 4  : fromTag()             [Locale.forLanguageTag] "pt-BR" -> pt_BR.
 * TODO 5  : toTag()               [toLanguageTag : tiret, pas souligne] Locale.CANADA_FRENCH -> fr-CA.
 * TODO 6  : countryName()         [getDisplayCountry(Locale.US)] Locale.CANADA_FRENCH -> Canada.
 * TODO 7  : greeting(locale)      [ResourceBundle.getBundle + getString] fr_CA -> Bonjour le Canada.
 * TODO 8  : farewellFrench()      [cle absente -> le parent] "farewell" en Locale.FRENCH -> Goodbye.
 * TODO 9  : keysFrench()          [keySet contient aussi les cles des parents] les cles du bundle FRENCH, triees.
 * TODO 10 : withFormatCategory()  [Locale.setDefault(Locale.Category.FORMAT, ...)] le temps d'un formatage, FORMAT = GERMANY ;
 *                                 formater 1234.5 avec NumberFormat.getInstance() -> 1.234,5 ; RESTAURER l'ancienne valeur (finally).
 * TODO 11 : invalid(locale, raw)  [bundle + MessageFormat] le gabarit "invalidRecord" rempli avec raw -> Invalid record: x.
 *
 *
 * ---------------------------------------------------------------------
 * CARTE MEMOIRE (a ne lire qu'en cas de blocage, puis a cacher) :
 *
 *   Locale.US, Locale.FRANCE, Locale.FRENCH (langue seule), Locale.CANADA_FRENCH...
 *   new Locale("fr") / new Locale("fr", "CA") ; new Locale.Builder().setLanguage("fr").setRegion("CA").build()
 *   Locale.forLanguageTag("fr-CA") <-> toLanguageTag() ; toString() -> fr_CA ; getCountry() "" si absent
 *   Locale.getDefault() / setDefault(loc) ; getDefault(Locale.Category.DISPLAY | FORMAT) / setDefault(categorie, loc)
 *   ResourceBundle.getBundle("paquet.nom", loc) ; getString(cle) ; keySet() ; cle introuvable -> MissingResourceException
 *   ordre : demandee (fr_CA, fr), defaut (en_US, en), racine ; puis la cle remonte les PARENTS du bundle choisi
 * ---------------------------------------------------------------------
 */
public class Drill05_LocaleAndBundles {

    public static String fromConstant() {
        throw new UnsupportedOperationException("TODO 1 : implementer fromConstant()");
    }

    public static String fromConstructor() {
        throw new UnsupportedOperationException("TODO 2 : implementer fromConstructor()");
    }

    public static String fromBuilder() {
        throw new UnsupportedOperationException("TODO 3 : implementer fromBuilder()");
    }

    public static String fromTag() {
        throw new UnsupportedOperationException("TODO 4 : implementer fromTag()");
    }

    public static String toTag() {
        throw new UnsupportedOperationException("TODO 5 : implementer toTag()");
    }

    public static String countryName() {
        throw new UnsupportedOperationException("TODO 6 : implementer countryName()");
    }

    public static String greeting(Locale locale) {
        throw new UnsupportedOperationException("TODO 7 : implementer greeting()");
    }

    public static String farewellFrench() {
        throw new UnsupportedOperationException("TODO 8 : implementer farewellFrench()");
    }

    public static Set<String> keysFrench() {
        throw new UnsupportedOperationException("TODO 9 : implementer keysFrench()");
    }

    public static String withFormatCategory() {
        throw new UnsupportedOperationException("TODO 10 : implementer withFormatCategory()");
    }

    public static String invalid(Locale locale, String raw) {
        throw new UnsupportedOperationException("TODO 11 : implementer invalid()");
    }

    public static void main(String[] args) {
        ExerciseChecker.check("1  fromConstant == fr_CA", fromConstant().equals("fr_CA"));
        ExerciseChecker.check("2  fromConstructor == fr_CA", fromConstructor().equals("fr_CA"));
        ExerciseChecker.check("3  fromBuilder == de_CH", fromBuilder().equals("de_CH"));
        ExerciseChecker.check("4  fromTag == pt_BR", fromTag().equals("pt_BR"));
        ExerciseChecker.check("5  toTag == fr-CA", toTag().equals("fr-CA"));
        ExerciseChecker.check("6  countryName == Canada", countryName().equals("Canada"));
        ExerciseChecker.check("7  greeting(fr_CA) == Bonjour le Canada ; greeting(FRENCH) == Bonjour",
                greeting(Locale.CANADA_FRENCH).equals("Bonjour le Canada") && greeting(Locale.FRENCH).equals("Bonjour"));
        ExerciseChecker.check("8  farewellFrench == Goodbye", farewellFrench().equals("Goodbye"));
        ExerciseChecker.check("9  keysFrench == [farewell, greeting, invalidRecord, onlyDefault]",
                keysFrench().toString().equals("[farewell, greeting, invalidRecord, onlyDefault]"));
        Locale before = Locale.getDefault(Locale.Category.FORMAT);
        ExerciseChecker.check("10 withFormatCategory == 1.234,5 et la Locale FORMAT est restauree",
                withFormatCategory().equals("1.234,5") && Locale.getDefault(Locale.Category.FORMAT).equals(before));
        ExerciseChecker.check("11 invalid(FRENCH, x) == Invalid record: x", invalid(Locale.FRENCH, "x").equals("Invalid record: x"));

        ExerciseChecker.summary();
    }
}
