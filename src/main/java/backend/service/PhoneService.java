package backend.service;

import backend.entity.Phone;
import backend.jdbc.PhoneDAO;

public class PhoneService {

    private final PhoneDAO phoneDAO;

    public PhoneService() {
        this.phoneDAO = new PhoneDAO();
    }

    public boolean createPhone(Phone phone) {
        return phoneDAO.create(phone) > 0;
    }

    public boolean dropPhone(Long id) {
        return phoneDAO.delete(id);
    }

    public boolean editPhone(Phone phone) {
        return phoneDAO.update(phone);
    }

    public Phone getPhone(Long id) {
        return phoneDAO.read(id);
    }
}
