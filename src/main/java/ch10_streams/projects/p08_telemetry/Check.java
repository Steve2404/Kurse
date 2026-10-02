package ch10_streams.projects.p08_telemetry;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur du projet 8 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON Telemetry, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "SERVEURS : [web1, web2, db1, cache1]",
            "PREMIERS : web1@35.5 web2@30.0 db1@70.0 cache1@12.0",
            "UNIQUE db1 > 90% (findAny) : 93.5 a 14:17:20",
            "REGIONS DISTINCTES : 3",
            "OCTETS : total 21670000000, pic 3900000000 (web1 a 14:15:20)",
            "MEMOIRE : max 8150 Mo, cumul 118069657600 octets",
            "CALENDRIER : 6 instants de 14:13:20 a 14:18:20 (identique a LongStream.iterate : true)",
            "TROUS cache1 : 14:15:20, 14:16:20",
            "DERNIER INSTANT : 14:18:20",
            "CPU : 22 mesures, min 11.0, max 95.0, moy 51.2",
            "CPU MOYEN PAR REGION : {ASIA=14.1, EU=48.5, US=81.3}",
            "CPU CUMULE PAR SERVEUR : {cache1=56.5, db1=488.0, web1=364.5, web2=218.0}",
            "REGION AFRICA : 0 echantillon, cpu moyen absent",
            "ALERTE CPU>90 : 2 (web1) | masquees 1 (db1)",
            "ALERTE MEMOIRE>8000 : 0 (-) | masquees 3 (db1)",
            "ALERTE OCTETS>3000000000 : 2 (web1) | masquees 0 (-)",
            "SANTE : cache1=100, db1=40, web1=40, web2=100",
            "PROFILS CPU (deciles) : web1=349964 web2=333433 db1=778897 cache1=1111",
            "RELANCES SONDE cache1 : 1 2 4 8 16 s",
            "SUPPLIER : 22 puis 3");
            // EXPECTED-END

    static final List<String> API = List.of(
            ".findAny()", "Stream.empty()", "LongStream.iterate(", ".asLongStream()", "OptionalLong", ".getAsLong()",
            "DoubleSummaryStatistics", "summarizingDouble(", "averagingDouble(", "summingDouble(", "IntStream.generate(",
            "Supplier<Stream<", ".toArray(",
            "IntFunction<", "ToIntFunction<", "ToLongFunction<", "ToDoubleFunction<", "LongFunction<",
            "IntUnaryOperator", "IntBinaryOperator", "LongUnaryOperator", "DoubleToIntFunction", "IntToLongFunction",
            "LongPredicate", "DoublePredicate", "IntSupplier", "BooleanSupplier", "ObjIntConsumer<",
            ".andThen(", ".getAsBoolean()",
            // Crescendo : notions des chapitres 11 et 13, interdites au chapitre 10.
            "!catch (", "!extends Exception", "!extends RuntimeException", "!Locale",
            "!.parallel()", "!.parallelStream()", "!Atomic", "!Concurrent");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "Telemetry", args, EXPECTED, API);
    }
}
