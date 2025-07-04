# Password Manager

A secure command-line password manager built in Java with AES-128 encryption and SHA-256 authentication.

## Features

- **Secure Storage**: Passwords are encrypted using AES-128 encryption with keys derived from SHA-256 hashing
- **PIN Protection**: 4-digit PIN authentication to access your password vault
- **Multiple Passwords**: Store multiple passwords per account/username combination
- **Hash Table Implementation**: Efficient storage using a custom hash table with linked list collision resolution
- **Command-Line Interface**: Easy-to-use menu-driven interface

## Security Features

- **AES-128 Encryption**: All passwords are encrypted before storage
- **SHA-256 Key Derivation**: PIN is hashed using SHA-256 to create encryption keys
- **Encrypted PIN Verification**: PIN validation without storing plaintext
- **In-Memory Processing**: Passwords are decrypted only when needed and not stored in plaintext

## Getting Started

### Prerequisites

- Java 8 or higher
- Java Cryptography Extension (JCE) - included in most modern Java installations

### Installation

1. Clone the repository:
   ```bash
   git clone <repository-url>
   cd password-manager
   ```

2. Compile the Java files:
   ```bash
   javac *.java
   ```

3. Run the application:
   ```bash
   java PasswordClient
   ```

### First Time Setup

When you first run the application, you'll be prompted to create a 4-digit PIN. This PIN will be used to encrypt and decrypt your passwords.

## Usage

### Main Menu Options

1. **Store New Username/Password** - Add a new password for an account
2. **Search for Existing Password** - Find and display a stored password
3. **List All Passwords for Account Type** - View all usernames/passwords for a specific account type
4. **Delete Specific Password** - Remove a password from an account/username
5. **Erase All Passwords for Account Type** - Delete all data for a specific account type
6. **Account Count** - Display the number of accounts created
7. **Delete Account** - Remove an entire account
8. **Quit** - Exit the application

### Example Workflow

1. Run the application and enter your PIN
2. Choose option 1 to store a new password
3. Enter account type (e.g., "Gmail")
4. Enter username (e.g., "john.doe@gmail.com")
5. Enter password (minimum 8 characters)
6. Password is encrypted and stored securely

## Architecture

### Core Components

- **Password.java**: Main engine containing the hash table implementation and encryption methods
- **PasswordClient.java**: Command-line interface and user interaction logic
- **LLQueue.java**: Linked list queue implementation for storing multiple passwords per account

### Data Structure

The password manager uses a hash table with linked list collision resolution:
- Hash table size: 50 buckets
- Each bucket contains a linked list of account nodes
- Each node can store multiple passwords using a queue structure

### Encryption Process

1. User enters 4-digit PIN
2. PIN is hashed using SHA-256
3. First 16 bytes of hash become AES-128 key
4. Passwords encrypted with AES before storage
5. Passwords decrypted only when retrieved

## File Structure

```
password-manager/
├── Password.java          # Core password manager engine
├── PasswordClient.java    # Command-line interface
├── LLQueue.java          # Queue implementation (dependency)
└── vault.check           # PIN verification file (created on first run)
```

## Security Considerations

- **PIN Storage**: PIN is never stored in plaintext; only an encrypted verification string
- **Password Encryption**: All passwords are encrypted before being stored in memory
- **Key Derivation**: Uses industry-standard SHA-256 for key derivation
- **Memory Management**: Passwords are only decrypted when needed for display

## Limitations

- Passwords are stored in memory only (not persistent across sessions)
- Maximum of 50 account types due to fixed hash table size
- No password strength validation beyond minimum length
- Single-user application (no multi-user support)

## Contributing

This is an educational project demonstrating:
- Object-oriented programming principles
- Hash table implementation
- Cryptographic best practices
- Command-line interface design

## Version

- **Version**: 1.0
- **Author**: Miguel Ocque
- **Date**: May 2025

## License

This project is for educational purposes. Please ensure compliance with local laws and regulations when using encryption software.

## Disclaimer

This is an educational implementation. For production use, consider established password managers with additional security features like secure storage, backup/sync capabilities, and professional security auditing.
