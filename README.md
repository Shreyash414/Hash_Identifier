# Hash Identifier

A lightweight, standalone Java hash identification tool and interactive command-line interface. Identifies potential hash formats, algorithms, and encoding types by analyzing prefixes, structural shapes, character sets, and digest lengths.

---

## Features

- **Known Prefix Detection (High Confidence)**:
  - Argon2 family: Argon2id, Argon2i, Argon2d
  - bcrypt variants: `$2b$`, `$2y$`, `$2a$`, `$2x$`
  - Unix crypt(3) family: SHA-512 crypt (`$6$`), SHA-256 crypt (`$5$`), MD5 crypt (`$1$`)
- **Special Shape Formats (High Confidence)**:
  - **MySQL5**: `*` prefix followed by 40 uppercase hex characters
  - **DES crypt**: 13 characters from `./0-9A-Za-z`
  - **NetNTLMv1**: `user::domain:lm(48 hex):nt(48 hex):challenge` shape
  - **NetNTLMv2**: `user::domain:challenge:hmac(32 hex):blob` shape
- **Hex Digest Length Matching (Medium / Low Confidence)**:
  - 8 hex: CRC-32, ADLER-32
  - 16 hex: MySQL323, CRC-64
  - 32 hex: MD5, NTLM, MD4, RIPEMD-128
  - 40 hex: SHA-1, RIPEMD-160
  - 56 hex: SHA-224, SHA3-224
  - 64 hex: SHA-256, SHA3-256, BLAKE2s-256
  - 96 hex: SHA-384, SHA3-384
  - 128 hex: SHA-512, SHA3-512, BLAKE2b-512, Whirlpool
- **Generic PHC Fallback (Low Confidence)**:
  - Identifies unmapped password hashing competition (PHC) strings in `$algorithmName$...` format.
- **Non-Hash Soft Heuristics (Low Confidence)**:
  - **JWT**: Heuristically detects JSON Web Tokens beginning with `eyJ`.
  - **Base64 Blob**: Detects Base64-like blobs containing `+`, `/`, or `=` characters.

---

## Project Structure

```
Hash_Identifier/
├── src/
│   ├── Confidence.java        # Confidence enum (High, Medium, Low)
│   ├── HashCandidate.java     # Identification candidate (algorithm, confidence, reason)
│   ├── HashIdentifier.java    # Core identification engine
│   ├── HashRules.java         # Digest length mapping table
│   ├── Main.java              # Interactive command-line interface
│   ├── PrefixRule.java        # Prefix rule data class
│   └── PrefixRules.java       # Prefix rule lookup table
├── .gitignore
└── README.md
```

---

## Requirements

- **Java JDK**: 17 or higher (tested on Java 21)

---

## Building and Running

### Compile

From the project root:

```bash
javac -d out/production/Hash_Identifier src/*.java
```

### Run the Interactive CLI

```bash
java -cp out/production/Hash_Identifier Main
```

Or compile and run directly from `src/`:

```bash
cd src
javac Main.java
java Main
```

---

## Example Usage

```
========================================
           HASH IDENTIFIER
========================================
Identify possible hash algorithms
Type 'exit' to quit

Enter hash: 5f4dcc3b5aa765d61d8327deb882cf99

========================================
IDENTIFICATION RESULTS
========================================

[1] Algorithm: MD5
    Confidence: Medium
    Reason: 32 hex chars - most likely candidate at this length

[2] Algorithm: NTLM
    Confidence: Low
    Reason: 32 hex chars - alternative candidate with same digest length

[3] Algorithm: MD4
    Confidence: Low
    Reason: 32 hex chars - alternative candidate with same digest length

[4] Algorithm: RIPEMD-128
    Confidence: Low
    Reason: 32 hex chars - alternative candidate with same digest length

High   = Strong format indicators
Medium = Likely candidate based on format/length
Low    = Possible candidate; insufficient evidence

Enter hash: exit
Exiting Hash Identifier. Goodbye!
```
