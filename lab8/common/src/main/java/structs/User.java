package structs;

import java.io.Serializable;

public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    String login;
    String password;
    boolean registered;
    String context;

    public User(String login, String password) {
        this.login = login;
        this.password = password;
        if (login == null) {
            registered = false;
        } else {
            registered = true;
        }
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public boolean isRegistered() {
        return registered;
    }
}
