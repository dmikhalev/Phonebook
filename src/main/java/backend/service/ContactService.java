package backend.service;

import backend.entity.Contact;
import backend.jdbc.ContactDAO;
import backend.utils.ContactComparator;

import java.util.ArrayList;
import java.util.List;


public class ContactService {

    private Long userId;
    private final ContactDAO contactDAO;

    public ContactService(Long userId) {
        this.userId = userId;
        this.contactDAO = new ContactDAO( userId);
    }

    public void sortContacts(List<Contact> contacts, boolean ascending, ContactComparator... sortKeys) {
        if (ascending) {
            contacts.sort(ContactComparator.ascending(ContactComparator.getComparator(sortKeys)));
        } else {
            contacts.sort(ContactComparator.descending(ContactComparator.getComparator(sortKeys)));
        }
    }

    public List<Contact> findContactsBy(ContactComparator searchKey, String keyWords) {
        if (keyWords == null) {
            return getAllContacts();
        }
        String keyWord = keyWords.replace(" ", "").toLowerCase();
        List<Contact> foundContacts = new ArrayList<>();
        getAllContacts().forEach(contact -> {
            String contactStringParam = searchKey.getStringParam(contact).toLowerCase();
            if (contactStringParam.contains(keyWord)) {
                foundContacts.add(contact);
            }
        });
        return foundContacts;
    }

    public boolean createContact(Contact contact) {
        return contactDAO.create(contact) > 0;
    }

    public boolean dropContact(Long id) {
        return contactDAO.delete(id);
    }

    public boolean editContact(Contact contact) {
        return contactDAO.update(contact);
    }

    public List<Contact> getAllContacts() {
        return contactDAO.getAll(userId);
    }

}
