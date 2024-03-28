package server.services;

import java.security.SecureRandom;

public class AdminPasswordService {
    private final static int PASSWORD_LENGTH = 20;
    private static String password;

    // this is incredibly rudimentary in a real application I would setup a token service that would validate a user
    // And give them JTW tokens and then validate each request they make using this
    public static boolean validatePassword(String enteredPassword) {
        return enteredPassword.equals(password);
    }

    public static String getPassword() {
        return password;
    }

    public static void setPassword(String password) {
        AdminPasswordService.password = password;
    }

    public static void generateAdminPassword() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder genPassword = new StringBuilder();

        for (int i = 0; i < PASSWORD_LENGTH; i++) {
            int randomIndex = random.nextInt(characters.length());
            genPassword.append(characters.charAt(randomIndex));
        }

        password = genPassword.toString();
        System.out.println("Admin Password: " + password);
    }
}
