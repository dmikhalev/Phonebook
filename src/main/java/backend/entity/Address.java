package backend.entity;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Address {

    private Long id;
    private String country;
    private String city;
    private String street;
    private String building;

    public Address() {
    }

    public Address(Long id, String country, String city, String street, String building) {
        this.id = id;
        this.country = country;
        this.city = city;
        this.street = street;
        this.building = building;
    }

    public Address(String address, String separator) {
        String[] addressParams = address.split(separator);
        if (addressParams.length > 0) {
            this.country = addressParams[0].trim();
        }
        if (addressParams.length > 1) {
            this.city = addressParams[1].trim();
        }
        if (addressParams.length > 2) {
            this.street = addressParams[2].trim();
        }
        if (addressParams.length > 3) {
            this.building = addressParams[3].trim();
        }
    }

    public String getFullAddress() {
        return Stream.of(country, city, street, building)
                .filter(s -> s != null && !s.isEmpty())
                .collect(Collectors.joining(", "));
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }
}
