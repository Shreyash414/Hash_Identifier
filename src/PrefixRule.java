public class PrefixRule {
    private final String prefix;
    private final String algorithm;
    private final String reason;

    PrefixRule(String prefix, String algorithm, String reason){
        this.prefix=prefix;
        this.algorithm=algorithm;
        this.reason=reason;
    }

    public String getPrefix(){
        return prefix;
    }

    public String getAlgorithm(){
        return algorithm;
    }

    public String getReason(){
        return reason;
    }
}
