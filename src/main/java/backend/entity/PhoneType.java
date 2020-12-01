package backend.entity;

import java.util.Arrays;

public enum PhoneType {
    MOBILE,
    HOME,
    WORK;

    public static PhoneType getPhoneTypeByName(String name) {
        return Arrays.stream(PhoneType.values())
                .filter(type -> type.toString().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);

    }

}
