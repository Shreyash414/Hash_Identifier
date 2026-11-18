import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        HashIdentifier identifier = new HashIdentifier();

        System.out.println("========================================");
        System.out.println("           HASH IDENTIFIER");
        System.out.println("========================================");
        System.out.println("Identify possible hash algorithms");
        System.out.println("Type 'exit' to quit");
        System.out.println();

        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.print("Enter hash: ");
                if (!scanner.hasNextLine()) {
                    break;
                }

                String input = scanner.nextLine();
                if (input == null) {
                    break;
                }

                String trimmed = input.trim();
                if (trimmed.equalsIgnoreCase("exit")) {
                    System.out.println("Exiting Hash Identifier. Goodbye!");
                    break;
                }

                if (trimmed.isEmpty()) {
                    System.out.println("No input provided. Please enter a hash string or type 'exit' to quit.");
                    System.out.println();
                    continue;
                }

                List<HashCandidate> candidates = identifier.identify(trimmed);

                if (candidates.isEmpty()) {
                    System.out.println();
                    System.out.println("No matching hash format identified.");
                    System.out.println("The input may be unsupported or the format may be ambiguous/unrecognized.");
                    System.out.println();
                } else {
                    System.out.println();
                    System.out.println("========================================");
                    System.out.println("IDENTIFICATION RESULTS");
                    System.out.println("========================================");
                    System.out.println();

                    for (int i = 0; i < candidates.size(); i++) {
                        HashCandidate candidate = candidates.get(i);
                        System.out.println("[" + (i + 1) + "] Algorithm: " + candidate.getAlgorithm());
                        System.out.println("    Confidence: " + candidate.getConfidence());
                        System.out.println("    Reason: " + candidate.getReason());
                        System.out.println();
                    }

                    System.out.println("High   = Strong format indicators");
                    System.out.println("Medium = Likely candidate based on format/length");
                    System.out.println("Low    = Possible candidate; insufficient evidence");
                    System.out.println();
                }
            }
        }
    }
}
