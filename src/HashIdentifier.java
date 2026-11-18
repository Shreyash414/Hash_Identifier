import java.util.*;

public class HashIdentifier {
    public List<HashCandidate> identify(String input){
        List<HashCandidate> candidates = new ArrayList<>();

        if(input == null || input.isBlank()){
            return new ArrayList<>();
        }
        input = input.trim();

        if(isNetNTLMv2(input)){
            candidates.add(new HashCandidate(
                    "NetNTLMv2",
                    Confidence.High,
                    "user::domain:challenge:hmac(32 hex):blob shape"
            ));
            return candidates;
        }

        if(isNetNTLMv1(input)){
            candidates.add(new HashCandidate(
                    "NetNTLMv1",
                    Confidence.High,
                    "user::domain:lm(48 hex):nt(48 hex):challenge shape"
            ));
            return candidates;
        }

        if(isMySQL5(input)){
            candidates.add(new HashCandidate(
                    "MySQL5",
                    Confidence.High,
                    "starts with * followed by 40 uppercase hex chars"
            ));
            return candidates;
        }

        if(isDesCrypt(input)){
            candidates.add(new HashCandidate(
                    "DES crypt",
                    Confidence.High,
                    "13 chars in ./0-9A-Za-z - legacy /etc/passwd format"
            ));
            return candidates;
        }

        for(PrefixRule rule : PrefixRules.RULES){
            if(input.startsWith(rule.getPrefix())){
                HashCandidate candidate = new HashCandidate(
                        rule.getAlgorithm(),
                        Confidence.High,
                        rule.getReason()
                );
                candidates.add(candidate);
            }
        }
        if(!candidates.isEmpty()){
            return candidates;
        }

        if(isHex(input)){
            List<String> algorithms = HashRules.map.get(input.length());
            if(algorithms != null){
                for (int i = 0; i < algorithms.size(); i++) {
                    String algorithm = algorithms.get(i);

                    Confidence confidence =
                            (i == 0) ? Confidence.Medium : Confidence.Low;

                    String reason = input.length() + " hex chars - " +
                            (i == 0
                                    ? "most likely candidate at this length"
                                    : "alternative candidate with same digest length");

                    candidates.add(new HashCandidate(
                            algorithm,
                            confidence,
                            reason
                    ));
                }
                return candidates;
            }
        }

        if(isGenericPHC(input)){
            String algorithmName = extractPHCAlgorithm(input);
            candidates.add(new HashCandidate(
                    "PHC string (" + algorithmName + ")",
                    Confidence.Low,
                    algorithmName + "$... shape - generic PHC, no specific rule"
            ));
            return candidates;
        }

        if(isJWT(input)){
            candidates.add(new HashCandidate(
                    "JWT (not a hash)",
                    Confidence.Low,
                    "leading eyJ is base64 of {\" - JWT, not a hash"
            ));
            return candidates;
        }

        if(isBase64(input)){
            candidates.add(new HashCandidate(
                    "Base64 blob (not a hash)",
                    Confidence.Low,
                    "contains base64 characters (+, /, =)"
            ));
            return candidates;
        }

        return candidates;
    }

    public boolean isMySQL5(String input){
        if(input.length()!=41 || !input.startsWith("*"))
            return false;

        String body = input.substring(1);
        for(int i=0;i<body.length(); i++){
            char c=body.charAt(i);

            boolean isDigit = c>='0' && c<='9';
            boolean isUpperHex = c>='A' && c<='F';

            if(!isDigit && !isUpperHex){
                return false;
            }
        }
        return true;
    }

    public boolean isDesCrypt(String input){
        String allowed="./0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        if(input.length()!=13){
            return false;
        }
        for(int i=0;i<input.length();i++){
            if(allowed.indexOf(input.charAt(i))==-1){
                return false;
            }
        }
        return true;
    }

    public boolean isHex(String input){
        if(input.isEmpty())
            return false;

        String allowed= "0123456789abcdefABCDEF";
        for(int i=0;i<input.length();i++){
            if(allowed.indexOf(input.charAt(i))==-1){
                return false;
            }
        }
        return true;
    }

    public boolean isNetNTLMv1(String input){
        if(input.indexOf("::")==-1){
            return false;
        }
        String[] parts= input.split(":");
        if(parts.length<6){
            return false;
        }
        if(parts[3].length()!=48 && !isHex(parts[3])){
            return false;
        }
        return true;
    }

    public boolean isNetNTLMv2(String input){
        if(input.indexOf("::")==-1){
            return false;
        }
        String[] parts= input.split(":");
        if(parts.length<6){
            return false;
        }
        if(parts[4].length()!=32 || !isHex(parts[4])){
            return false;
        }
        return true;
    }

    public boolean isGenericPHC(String input){
        if(input == null || !input.startsWith("$")){
            return false;
        }
        int secondDollar = input.indexOf('$', 1);
        if(secondDollar == -1){
            return false;
        }
        String algo = input.substring(1, secondDollar);
        if(algo.isEmpty()){
            return false;
        }
        for(int i = 0; i < algo.length(); i++){
            char c = algo.charAt(i);
            boolean isLetter = (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
            boolean isDigit = c >= '0' && c <= '9';
            boolean isAllowedSpecial = c == '-' || c == '_';

            if(!isLetter && !isDigit && !isAllowedSpecial){
                return false;
            }
        }
        return true;
    }

    public String extractPHCAlgorithm(String input){
        if(input == null || !input.startsWith("$")){
            return "";
        }
        int secondDollar = input.indexOf('$', 1);
        if(secondDollar == -1){
            return "";
        }
        return input.substring(1, secondDollar);
    }

    public boolean isJWT(String input){
        if(input == null){
            return false;
        }
        return input.startsWith("eyJ");
    }

    public boolean isJwt(String input){
        return isJWT(input);
    }

    public boolean isBase64(String input){
        if(input == null || input.length() <= 8){
            return false;
        }
        String allowed = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";
        boolean hasSpecial = false;

        for(int i = 0; i < input.length(); i++){
            char c = input.charAt(i);
            if(allowed.indexOf(c) == -1){
                return false;
            }
            if(c == '+' || c == '/' || c == '='){
                hasSpecial = true;
            }
        }
        return hasSpecial;
    }

    public boolean isBase64Like(String input){
        return isBase64(input);
    }
}
