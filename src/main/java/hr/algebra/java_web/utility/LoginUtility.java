package hr.algebra.java_web.utility;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public class LoginUtility {
    private static final String USER_ID_ATTRIBUTE = "id";
    private static final String USER_TYPE_ATTRIBUTE = "userType";
    private static final String USER_TYPE_JWUSER = "JWUser";
    private static final String USER_TYPE_ADMIN = "Admin";

    public static boolean isLogged(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session != null && session.getAttribute(USER_ID_ATTRIBUTE) != null;
    }

    public static boolean isUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session != null && USER_TYPE_JWUSER.equals(session.getAttribute(USER_TYPE_ATTRIBUTE));
    }

    public static boolean isAdmin(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session != null && USER_TYPE_ADMIN.equals(session.getAttribute(USER_TYPE_ATTRIBUTE));
    }
}
