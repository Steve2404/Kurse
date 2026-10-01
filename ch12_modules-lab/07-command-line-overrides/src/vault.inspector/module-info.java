module vault.inspector {
    // Il lit vault.core, mais NE requiert PAS vault.audit : ce module ne sera pas charge sans aide.
    requires vault.core;
}
