module greeting.api {
    // exports : sans cette ligne, le package reste invisible meme pour un module qui fait requires greeting.api.
    exports com.example.greeting.api;
}
