/** Un vieux code (classpath) qui utilise une API INTERNE du JDK : jdeps --jdk-internals doit la signaler. */
public class Legacy {

    public static Object unsafeClass() {
        return sun.misc.Unsafe.class;
    }
}
