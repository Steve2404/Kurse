// SOLUTION - un module qui depend de java.sql (qui, lui, requiert transitivement java.logging et java.xml).
module r.report {
    requires java.sql;
}
