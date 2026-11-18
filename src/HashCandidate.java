public class HashCandidate {
    String algorithm;
    Confidence confidence;
    String reason;


    public HashCandidate(String algorithm, Confidence confidence, String reason) {
        this.algorithm=algorithm;
        this.confidence=confidence;
        this.reason=reason;
    }
    public String getAlgorithm() {
        return algorithm;
    }

    public Confidence getConfidence() {
        return confidence;
    }

    public String getReason() {
        return reason;
    }
}
