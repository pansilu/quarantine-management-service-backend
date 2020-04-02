package lk.uom.fit.qms.util;

/**
 * @author Yasas Pansilu Jayasuriya
 * @version 1.0
 * @E-mail jayasuriyay@gmail.com
 * @Telephone +94777332170
 * @project qms
 * @user Yasas_105071
 * @created on 3/31/2020
 * @Package lk.uom.fit.qms.util.
 */
public class Constant {

    private Constant() {
        throw new IllegalStateException("Utility class");
    }

    public static final String LOG_IDENTIFIER_KEY = "UUID";
    public static final String AUTHORIZATION_HEADER = "Authorization";

    public static final String USER_ID_KEY = "userId";
    public static final String USER_NAME_KEY = "username";
    public static final String USER_DEFAULT_NAME_KEY = "name";
    public static final String USER_MOBILE_KEY = "mobile";
    public static final String USER_PHONE_KEY = "phone";
    public static final String USER_ROLE_KEY = "roles";
    public static final String USER_INSPECT_DETAIL_KEY = "inspectUsers";

    public static final String JWT_HEADER_TYPE_KEY = "typ";
    public static final String JWT_HEADER_TYPE_VALUE = "JWT";

    public static final String USER_CREATE_PERMISSION = "create";
}
