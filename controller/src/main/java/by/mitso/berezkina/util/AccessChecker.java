package by.mitso.berezkina.util;

import jakarta.servlet.http.HttpServletRequest;

public class AccessChecker {

    private static final String ADMINISTRATOR_ROLE = "administrator";
    private static final String USER_ROLE = "user";

    public static boolean isAdministrator(HttpServletRequest req) {
        return req.isUserInRole(ADMINISTRATOR_ROLE);
    }

    public static boolean isUser(HttpServletRequest req) {
        return req.isUserInRole(USER_ROLE);
    }
}
