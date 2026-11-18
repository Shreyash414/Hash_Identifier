import java.util.*;

public final class HashRules {

    private HashRules(){}

    public static Map<Integer, List<String>> map =
            Map.of(
                    8, List.of("CRC-32", "ADLER-32"),

                    16, List.of("MySQL323", "CRC-64"),

                    32, List.of("MD5", "NTLM", "MD4", "RIPEMD-128"),

                    40, List.of("SHA-1", "RIPEMD-160"),

                    56, List.of("SHA-224", "SHA3-224"),

                    64, List.of("SHA-256", "SHA3-256", "BLAKE2s-256"),

                    96, List.of("SHA-384", "SHA3-384"),

                    128, List.of(
                            "SHA-512",
                            "SHA3-512",
                            "BLAKE2b-512",
                            "Whirlpool"
                    )
            );

}
