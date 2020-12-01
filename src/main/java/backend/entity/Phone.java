package backend.entity;

public class Phone {

    private Long id;
    private String telNumber;
    private String model;
    private PhoneType phoneType;

    public Phone() {
    }

    public Phone(Long id, String telNumber, String model, PhoneType phoneType) {
        this.id = id;
        this.telNumber = telNumber;
        this.model = model;
        this.phoneType = phoneType;
    }

    public String getTelNumber() {
        return telNumber;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTelNumber(String telNumber) {
        this.telNumber = telNumber;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public PhoneType getPhoneType() {
        return phoneType;
    }

    public void setPhoneType(PhoneType phoneType) {
        this.phoneType = phoneType;
    }
}
