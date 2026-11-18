
import java.util.List;

public final class PrefixRules {

    private PrefixRules() {
        // Prevent object creation
    }

    public static final List<PrefixRule> RULES = List.of(
            new PrefixRule("$argon2id$", "Argon2id",
                    "Modern PHC string, the current standard"),

            new PrefixRule("$argon2i$", "Argon2i",
                    "PHC string, side-channel-resistant variant"),

            new PrefixRule("$argon2d$", "Argon2d",
                    "PHC string, GPU-resistant variant"),

            new PrefixRule("$2y$", "bcrypt",
                    "bcrypt 2y variant (PHP)"),

            new PrefixRule("$2b$", "bcrypt",
                    "bcrypt 2b variant (current)"),

            new PrefixRule("$2a$", "bcrypt",
                    "bcrypt 2a variant (legacy)"),

            new PrefixRule("$2x$", "bcrypt",
                    "bcrypt 2x variant (legacy fix)"),

            new PrefixRule("$6$", "SHA-512 crypt",
                    "Unix crypt using SHA-512"),

            new PrefixRule("$5$", "SHA-256 crypt",
                    "Unix crypt using SHA-256"),

            new PrefixRule("$1$", "MD5 crypt",
                    "Unix crypt using MD5 (legacy, weak)")
    );
}
