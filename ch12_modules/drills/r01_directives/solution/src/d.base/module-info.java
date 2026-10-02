// SOLUTION D01 - exporte un paquet a tous, et un autre a UN seul module.
module d.base {
    exports d.base;
    exports d.base.hidden to d.friend;
}
