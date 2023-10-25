package de.tudortmund.webtech2.service;

import de.tudortmund.webtech2.entity.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.DefaultHashService;
import org.apache.shiro.crypto.hash.Hash;
import org.apache.shiro.crypto.hash.HashRequest;
import org.apache.shiro.crypto.hash.HashService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
public class SecurityService {

    static final int USERNAME_MIN_LENGTH = 2;
    static final int PASSWORD_MIN_LENGTH = 6;

    private final UserService userService;

    @Autowired
    public SecurityService(UserService userService) {
        this.userService = userService;
    }

    /**
     * Checks if the username for length and existence in db
     * @param username username to check
     * @return true if the username is valid, false otherwise
     */
    public boolean sanitizeUsername(String username){
        System.out.println(username);
        if(username.length() < USERNAME_MIN_LENGTH) return false;
        return !userService.exists(username);
    }

    /**
     * Checks the password for length
     * @param password password to check
     * @return true if the password is valid, false otherwise
     */
    public boolean sanitizePassword(String password){
        System.out.println(password);
        return password.length() >= PASSWORD_MIN_LENGTH;
    }

    /**
     * Hashes a password
     * @param password password string to be hashed
     * @return hashed string
     */
    public static byte[] hashPassword(String password){
        HashService hashService = new DefaultHashService();
        HashRequest request = new HashRequest.Builder()
                .setSource(password)
                .build();
        Hash hash = hashService.computeHash(request);
        return hash.getBytes();
    }

    /**
     * Generates a salt
     * @return salt
     */
    private String generateSalt(){
        RandomNumberGenerator rng = new SecureRandomNumberGenerator();
        byte[] saltBytes = rng.nextBytes().getBytes();
        return new String(Base64.getEncoder().encode(saltBytes));
    }

    /**
     * Checks if the user is logged in
     * @return username if logged in
     * @throws IllegalStateException if not logged in
     */
    public String getPrincipalName() throws IllegalStateException{
        Object principal = SecurityUtils.getSubject().getPrincipal();
        if(principal == null){
            throw new AuthenticationException("There is no user logged in!");
        }
        return principal.toString();
    }

    /**
     * Checks if the user is logged in
     * @return User obj if logged in
     * @throws IllegalStateException if not logged in
     * @throws Exception some server error
     */
    public User getPrincipal() throws IllegalStateException, Exception {
        try {
            String principal = getPrincipalName();
            User user = userService.getUser(principal);
            if(user == null){
                throw new IllegalStateException();
            }
            return user;
        } catch (IllegalStateException e){
            throw new AuthenticationException();
        }
    }

}
