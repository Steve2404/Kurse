import sun.security.x509.X500Name;

/** SOLUTION - du code du CLASSPATH qui utilise une API INTERNE du JDK (a eviter !). */
public class Peek {
    public static void main(String[] args) throws Exception {
        System.out.println(new X500Name("CN=Ada").getCommonName());
    }
}
