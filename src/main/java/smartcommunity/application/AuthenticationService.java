
package smartcommunity.application;

import smartcommunity.domain.User;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class AuthenticationService {

    private final Map<String, User> users = new HashMap<>();

    public void registerDemoUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User is required.");
        }
        users.put(user.getEmail().toLowerCase(), user);
    }

    public User authenticate(String email, String password) {
        if (email == null || password == null) {
            throw new SecurityException("Invalid email or password.");
        }

        User user = users.get(email.trim().toLowerCase());

        if (user == null ||
                !MessageDigest.isEqual(
                        user.getPasswordHash().getBytes(StandardCharsets.UTF_8),
                        hashPassword(password).getBytes(StandardCharsets.UTF_8))) {
            throw new SecurityException("Invalid email or password.");
        }

        return user;
    }

    public void logout(User user) {
        // The console clears its active user session.
    }

    public Collection<User> getDemoUsers() {
        return java.util.List.copyOf(users.values());
    }

    public static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(
                    password.getBytes(StandardCharsets.UTF_8));

            return java.util.HexFormat.of().formatHex(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 is unavailable.", e);
        }
    }
}
