
package smartcommunity.domain;

public class Resident extends User {
    private final String address;
    private final String phone;

    public Resident(String userId, String name, String email,
                    String passwordHash, String address, String phone) {
        super(userId, name, email, passwordHash);
        this.address = address;
        this.phone = phone;
    }

    public String getAddress() { return address; }
    public String getPhone() { return phone; }
}
