module pricing.engine {
    // export QUALIFIE : seuls les modules cites apres "to" voient le package ; tout autre module a un "not visible".
    exports com.example.pricing.engine to pricing.trusted, pricing.untrusted;
}
