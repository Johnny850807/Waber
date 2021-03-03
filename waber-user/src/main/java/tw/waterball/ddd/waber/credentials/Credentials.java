package tw.waterball.ddd.waber.credentials;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
public class Credentials {
    private int userId;
    private String email;
    private String password; // hashed

    public Credentials(int userId, String password) {
        this.userId = userId;
        this.password = password;
    }

    public int getUserId() {
        return userId;
    }

    public String getPassword() {
        return password;
    }
}
