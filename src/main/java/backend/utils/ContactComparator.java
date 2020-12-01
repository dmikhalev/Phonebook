package backend.utils;

import backend.entity.Contact;

import java.util.Comparator;

public enum ContactComparator implements Comparator<Contact> {
    ALL{
        public int compare(Contact c1, Contact c2) {
            return c1.toString().compareTo(c2.toString());
        }

        public String getStringParam(Contact c) {
            return c.toString();
        }
    },
    FULL_NAME {
        public int compare(Contact c1, Contact c2) {
            return c1.getFullName().compareTo(c2.getFullName());
        }

        public String getStringParam(Contact c) {
            return c.getFullName();
        }
    },
    FIRST_NAME {
        public int compare(Contact c1, Contact c2) {
            return c1.getFirstName().compareTo(c2.getFirstName());
        }

        public String getStringParam(Contact c) {
            return c.getFirstName();
        }
    },
    MIDDLE_NAME {
        public int compare(Contact c1, Contact c2) {
            return c1.getMiddleName().compareTo(c2.getMiddleName());
        }

        public String getStringParam(Contact c) {
            return c.getMiddleName();
        }
    },
    LAST_NAME {
        public int compare(Contact c1, Contact c2) {
            return c1.getMiddleName().compareTo(c2.getMiddleName());
        }

        public String getStringParam(Contact c) {
            return c.getLastName();
        }
    },
    COMPANY {
        public int compare(Contact c1, Contact c2) {
            return c1.getCompany().compareTo(c2.getCompany());
        }

        public String getStringParam(Contact c) {
            return c.getCompany();
        }
    },
    TELEPHONE_NUMBER {
        public int compare(Contact c1, Contact c2) {
            return c1.getPhone().getTelNumber().compareTo(c2.getPhone().getTelNumber());
        }

        public String getStringParam(Contact c) {
            return String.valueOf(c.getPhone().getTelNumber());
        }
    },
    PHONE {
        public int compare(Contact c1, Contact c2) {
            return c1.getPhone().getModel().compareTo(c2.getPhone().getModel());
        }

        public String getStringParam(Contact c) {
            return c.getPhone().getModel();
        }
    },
    EMAIL_ADDRESS {
        public int compare(Contact c1, Contact c2) {
            return c1.getEmail().compareTo(c2.getEmail());
        }

        public String getStringParam(Contact c) {
            return c.getEmail();
        }
    },
    ADDRESS {
        public int compare(Contact c1, Contact c2) {
            return c1.getAddress().getFullAddress().compareTo(c2.getAddress().getFullAddress());
        }

        public String getStringParam(Contact c) {
            return c.getAddress().getFullAddress();
        }
    },
    NOTE {
        public int compare(Contact c1, Contact c2) {
            return c1.getNote().compareTo(c2.getNote());
        }

        public String getStringParam(Contact c) {
            return c.getNote();
        }
    };

    public String getStringParam(Contact c) {
        return "";
    }

    public ContactComparator[] getAllSortParams() {
        return new ContactComparator[]{FULL_NAME, COMPANY, TELEPHONE_NUMBER, EMAIL_ADDRESS};
    }

    public static Comparator<Contact> ascending(final Comparator<Contact> comparator) {
        return comparator;
    }

    public static Comparator<Contact> descending(final Comparator<Contact> comparator) {
        return (c1, c2) -> -1 * comparator.compare(c1, c2);
    }

    public static Comparator<Contact> getComparator(final ContactComparator... options) {
        return (c1, c2) -> {
            for (ContactComparator option : options) {
                int result = option.compare(c1, c2);
                if (result != 0) {
                    return result;
                }
            }
            return 0;
        };
    }
}
