# Security Policy

## Supported Versions

The following versions of the project receive security updates:

| Version | Supported          |
| ------- | ------------------ |
| 1.1.x   | :white_check_mark: |
| 1.0.x   | :white_check_mark: |

## Reporting a Vulnerability

If you discover a security vulnerability in this project:

### How to Report

Please **DO NOT open a public issue**. Instead, use one of these options:

1. **GitHub Security Advisories** (recommended):
   - Go to the "Security" tab of the repository
   - Click on "Report a vulnerability"
   - Fill out the private form

2. **Private issue**:
   - Open an issue and mark it as security-related if GitHub allows it in this repo

### What to Include in Your Report

- Clear description of the vulnerability
- Steps to reproduce it
- Affected version(s)
- Potential impact
- If possible, suggested fixes

### What to Expect

- **Initial response**: I'll try to respond within 3-5 days
- **Resolution**: Depending on severity, I'll work on a fix as soon as possible
- **Credit**: You'll be credited in the changelog if you wish

## Dependencies

This project uses third-party dependencies. Stay alert for security updates by regularly running:
```bash
mvn dependency:check
```

Thank you for helping keep this project secure.
