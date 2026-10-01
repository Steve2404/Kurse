module orders.processing {
    // processing ne requiert plus shipping : il ne depend que du module commun.
    requires orders.common;
    exports com.example.orders.processing;
}
