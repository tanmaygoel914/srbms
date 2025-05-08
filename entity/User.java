package entity;

public abstract class User {
    protected String email;
    protected String password;
    protected String role;

    public User(String email, String password, String role) {
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getRole() { return role; }

    public abstract void displayMenu();
}