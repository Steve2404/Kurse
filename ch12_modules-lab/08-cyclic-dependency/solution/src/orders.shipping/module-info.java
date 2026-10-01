module orders.shipping {
    // shipping non plus ne requiert plus processing : fin du cycle (que javac refuse toujours).
    requires orders.common;
    exports com.example.orders.shipping;
}
