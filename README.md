
# Zen-Auth Package

## Overview
The Zen-Auth package provides an authentication system based on JWT (JSON Web Tokens) for Spring Boot applications. It includes features for generating, parsing, and validating JWT tokens and integrates seamlessly with Spring Security to authenticate users. This package ensures secure communication and user authentication for RESTful APIs.

---

## Features
1. **JWT Token Generation and Validation**
   - Generate access tokens with user-specific claims.
   - Generate refresh tokens for extended session management.
   - Validate and parse JWT tokens securely.

2. **Custom Authentication**
   - Implements a custom authentication filter (`JwtAuthenticationFilter`) for validating JWT tokens in incoming requests.
   - Provides a custom authentication token (`ZenAuthenticationToken`) to manage user authentication and authorization.

3. **User Management**
   - Fetch user details using the `ZenAuthService` interface.

---

## Package Structure

```
com.zendot.auth.jwt
├── JwtAuthenticationFilter.kt   # Custom filter for validating JWT tokens.
├── JwtTokenUtils.kt             # Utility class for generating and parsing JWT tokens.
├── ZenAuthenticationToken.kt    # Custom Spring Security token.

com.zendot.auth.service
├── ZenAuthService               # Interface for fetching user details from the database.

com.zendot.auth.model
├── ZenUser                      # Interface representing the user model.
```

---

## How It Works
1. **Token Generation**
   - The `JwtTokenUtils` class generates JWT tokens with user-specific claims such as `id` and expiration details.
   - Uses `HmacSHA256` for signing tokens.

2. **Token Validation**
   - The `JwtAuthenticationFilter` extracts the token from the `Authorization` header of incoming HTTP requests.
   - Validates the token and retrieves the user ID from the token payload.

3. **Authentication**
   - The `ZenAuthService` fetches user details based on the extracted user ID.
   - Creates a `ZenAuthenticationToken` with the user details and sets it in the Spring Security context.

---

## Integration

### Prerequisites
- A Spring Boot project with Spring Security dependency.
- Database for storing user information.

### Adding Zen-Auth Package
1. Add the `Zen-Auth` package to your project as a dependency.

#### Using Maven
Add the following dependency to your `pom.xml`:
```xml
<dependency>
    <groupId>com.zendot</groupId>
    <artifactId>zen-auth</artifactId>
    <version>1.0.0</version>
</dependency>
```
```
Add below in `settings.xml` file of your local
password is basically PAT which is found in zen-auth repo
 <server>
        <id>zen-auth</id>
        <username>abdurrakib321</username>
        <password>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</password>
</server>
```
#### Using Gradle
Add the following dependency to your `build.gradle`:
```kotlin
implementation("com.zendot:zen-auth:1.0.0")
```

2. Configure your Spring Boot application to recognize the JWT filter.

### Configuration
Add the following properties to your `application.yml` or `application.properties` file:
```yaml
jwt.lifetime: 3600 # JWT expiration time in seconds
jwt.refreshToken.lifetime: 86400 # Refresh token expiration time in seconds
jwt.secret: <base64-encoded-refresh-token-key>
```





### Generating Tokens
Use the `JwtTokenUtils` class to generate tokens for authenticated users:
```kotlin
val jwtToken = tokenProvider.generateToken(user)
val refreshToken = tokenProvider.generateRefreshToken(user)
```

Include the token in the `Authorization` header for subsequent requests:
```http
Authorization: Bearer <token>
```

---

## Testing
1. **Unit Tests**
   - Test the `JwtTokenUtils` methods for token generation and validation.
   - Mock the `ZenAuthService` to test `JwtAuthenticationFilter` behavior.

2. **Integration Tests**
   - Test secured endpoints with valid and invalid tokens.

---

## Troubleshooting
- **Invalid Token**: Ensure the secret keys match between token generation and validation.
- **401 Unauthorized**: Verify the token is included in the `Authorization` header with the correct `Bearer` prefix.

---

