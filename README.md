# 🔐 Password Manager

A secure command-line password manager built in Java featuring AES-128 encryption, SHA-256 authentication, and persistent file storage.

## ✨ Features

### 🛡️ Security
- **AES-128 Encryption**: All passwords encrypted using industry-standard AES encryption
- **SHA-256 Key Derivation**: Secure PIN hashing for encryption key generation
- **PIN Protection**: 4-digit PIN authentication with encrypted verification
- **Zero Plaintext Storage**: Passwords never stored in readable format

### 💾 Data Management
- **Persistent Storage**: Saves and loads encrypted passwords from file
- **Multiple Passwords**: Store multiple passwords per account/username combination
- **Efficient Storage**: Custom hash table with linked list collision resolution
- **Data Integrity**: Secure file format maintains encryption throughout storage

### 🖥️ User Experience
- **Intuitive CLI**: Clean, menu-driven command-line interface
- **Account Organization**: Group passwords by account type and username
- **Flexible Operations**: Add, search, list, delete individual passwords or entire accounts
- **Session Management**: Seamless loading on startup, optional saving on exit

## 🚀 Getting Started

### Prerequisites
- Java 8 or higher
- Java Cryptography Extension (JCE) - included in modern Java installations

### Installation

1. **Clone the repository**:
   ```bash
   git clone <repository-url>
   cd password-manager
   ```

2. **Compile the Java files**:
   ```bash
   javac *.java
   ```

3. **Run the application**:
   ```bash
   java PasswordClient
   ```

### First Run
On your first run, you'll create a 4-digit PIN that serves as your master key. This PIN encrypts all your passwords and is required for access.

## 📋 Usage Guide

### Main Menu Options

| Option | Description |
|--------|-------------|
| **1** | Store new username/password for an account |
| **2** | Search for existing password by account and username |
| **3** | List all passwords for a specific account/username |
| **4** | Delete a specific password |
| **5** | Erase all passwords for an account type |
| **6** | View total number of accounts created |
| **7** | Delete an entire account |
| **8** | Quit application |

### Example Workflow

```
1. Enter your PIN → Access granted
2. Choose option 1 → Store new password
3. Enter "gmail" → Account type
4. Enter "john.doe@gmail.com" → Username
5. Enter "MySecurePass123!" → Password (min 8 chars)
6. Password encrypted and saved ✓
```

### File Persistence

- **Automatic Loading**: On startup, previously saved passwords are automatically loaded
- **Optional Saving**: Choose whether to save your session when exiting
- **Secure Format**: All data remains encrypted in storage files

## 🏗️ Architecture

### Core Components

```
Password.java         → Core engine (hash table, encryption, operations)
PasswordClient.java   → User interface and session management
LLQueue.java         → Queue implementation for multiple passwords
```

### Data Structure

```
Hash Table (50 buckets)
├── Bucket 0: Account Node → Account Node → ...
├── Bucket 1: Account Node → ...
├── ...
└── Bucket 49: Account Node → ...

Each Account Node:
├── Account Type (e.g., "gmail")
├── Username (e.g., "user@gmail.com")
├── Password Queue → [Encrypted Pass 1] → [Encrypted Pass 2] → ...
└── Next Node Pointer
```

### Security Flow

```
PIN Input → SHA-256 Hash → AES-128 Key → Encrypt/Decrypt Passwords
    ↓
[Never stored in plaintext] → [File: encrypted verification] → [Memory: encrypted passwords]
```

## 📁 File Structure

```
password-manager/
├── Password.java          # Core password manager engine
├── PasswordClient.java    # Command-line interface
├── LLQueue.java          # Queue implementation
├── vault.check           # PIN verification (auto-created)
├── passwords.dat         # Encrypted password storage (auto-created)
└── README.md             # This file
```

## 🔒 Security Features

### Encryption Details
- **Algorithm**: AES-128 in ECB mode
- **Key Derivation**: SHA-256 hash of PIN, truncated to 128 bits
- **Data Format**: Base64 encoded encrypted strings
- **Verification**: PIN verified through encrypted challenge string

### Security Practices
- PIN never stored directly
- Passwords encrypted before any storage operation
- Decryption only occurs for display purposes
- Memory cleared after operations (Java GC dependent)

## 🎯 Use Cases

Perfect for:
- **Learning**: Understanding encryption, data structures, and Java development
- **Personal Use**: Secure local password storage without cloud dependencies
- **Development**: Base for more advanced password management systems
- **Education**: Demonstrating cryptographic principles and OOP design

## ⚠️ Important Notes

### Current Limitations
- **Local Only**: No cloud sync or backup features
- **Single User**: Designed for individual use
- **Hash Table Size**: Fixed at 50 buckets (suitable for moderate use)
- **Platform**: Command-line interface only

### Security Considerations
- This is an educational implementation
- For production use, consider established password managers
- Regular backups of `passwords.dat` recommended
- PIN recovery not implemented - forgotten PIN means data loss

## 🔄 Version History

- **Version 1.0** (May 2025)
  - Initial release with core functionality
  - AES encryption and hash table implementation
  - Command-line interface

- **Version 1.1** (Planned)
  - File persistence
  - Automatic session loading
  - Enhanced user experience

## 👨‍💻 Author

**Miguel Ocque** - Educational implementation demonstrating secure password management principles

## 📄 License

Educational use. When using encryption software, ensure compliance with local laws and regulations.

---

*🔒 Keep your PIN secure - it's the key to all your passwords!*
