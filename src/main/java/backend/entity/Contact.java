package backend.entity;

public class Contact {

    private Long id;
    private String firstName;
    private String middleName;
    private String lastName;
    private String company;
    private Phone phone;
    private String email;
    private Address address;
    private String note;

    public Contact(Long id, String firstName, String middleName, String lastName, String company,
                   Phone phone, String email, Address address, String note) {
        this.id = id;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.company = company;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.note = note;
    }

    public String getFullName() {
        return String.join(" ", firstName, middleName, lastName);
    }

    @Override
    public String toString() {
        return String.join("    ",
                String.valueOf(id), firstName, middleName, lastName, String.valueOf(phone.getTelNumber()),
                company, email, address.getFullAddress());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public Phone getPhone() {
        return phone;
    }

    public void setPhone(Phone phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
