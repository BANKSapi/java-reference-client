package de.banksapi.client.model.outgoing.mgmt;

public class UserOut {

    private String username;
    private String password;
    private String firstname;
    private String lastname;

    public UserOut() {
    }

    public UserOut(String username, String password, String firstname, String lastname) {
        this.username = username;
        this.password = password;
        this.firstname = firstname;
        this.lastname = lastname;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

}
