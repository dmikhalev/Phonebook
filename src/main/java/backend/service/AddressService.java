package backend.service;

import backend.entity.Address;
import backend.jdbc.AddressDAO;

public class AddressService {

    private final AddressDAO addressDAO;

    public AddressService() {
        this.addressDAO = new AddressDAO();
    }

    public boolean createAddress(Address address) {
        return addressDAO.create(address) > 0;
    }

    public boolean dropAddress(Long id) {
        return addressDAO.delete(id);
    }

    public boolean editAddress(Address address) {
        return addressDAO.update(address);
    }

    public Address getAddress(Long id) {
        return addressDAO.read(id);
    }
}
