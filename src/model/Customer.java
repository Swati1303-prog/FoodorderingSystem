package model;

public class Customer {
    private int id;
    private String firstName;
    private String lastName;
    private String middleName;
    private String email;
    private String phoneNumber;
    private String landline;
    private String username;
    private String password;
    private String accountStatus;
    private String profileImage;

    public Customer() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String v) { this.firstName = v; }

    public String getLastName() { return lastName; }
    public void setLastName(String v) { this.lastName = v; }

    public String getMiddleName() { return middleName; }
    public void setMiddleName(String v) { this.middleName = v; }

    public String getEmail() { return email; }
    public void setEmail(String v) { this.email = v; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String v) { this.phoneNumber = v; }

    public String getLandline() { return landline; }
    public void setLandline(String v) { this.landline = v; }

    public String getUsername() { return username; }
    public void setUsername(String v) { this.username = v; }

    public String getPassword() { return password; }
    public void setPassword(String v) { this.password = v; }

    public String getAccountStatus() { return accountStatus; }
    public void setAccountStatus(String v) { this.accountStatus = v; }

    public String getProfileImage() { return profileImage; }
    public void setProfileImage(String v) { this.profileImage = v; }
}
