package by.mitso.berezkina.access;

import static by.mitso.berezkina.access.ApplicationRole.*;

import jakarta.servlet.http.HttpServletRequest;

public class AccessChecker {

    public static boolean isAdministrator(HttpServletRequest req) {
        return req.isUserInRole(ADMINISTRATOR_ROLE.getName());
    }

    public static boolean isUser(HttpServletRequest req) {
        return req.isUserInRole(USER_ROLE.getName());
    }
}
