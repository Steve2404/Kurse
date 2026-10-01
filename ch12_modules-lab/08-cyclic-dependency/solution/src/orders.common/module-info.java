module orders.common {
    // Le type partage (OrderId) est sorti dans un module commun : c'est lui qui casse le cycle.
    exports com.example.orders.common;
}
