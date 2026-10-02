// SOLUTION - un cycle volontaire : a requiert b, b requiert a. Le JPMS le refuse a la compilation.
module cycle.a {
    requires cycle.b;
}
