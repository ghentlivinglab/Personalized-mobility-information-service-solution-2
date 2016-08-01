package database_v2.models.relational;

import database_v2.models.Attribute;
import database_v2.models.AttributeType;
import database_v2.models.CRUDModel;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * This class models the account table from the relational database.
 * <p>
 * Schema:
 * <pre>
 * +-----------------------+----------+-------------+
 * |      Columnname       | datatype | foreign key |
 * +-----------------------+----------+-------------+
 * | accountid             | serial   |             |
 * | email                 | text     |             |
 * | refresh_token         | text     |             |
 * | first_name            | text     |             |
 * | last_name             | text     |             |
 * | password              | text     |             |
 * | salt                  | bytea    |             |
 * | mute_notifications    | boolean  |             |
 * | email_validated       | boolean  |             |
 * | is_operator           | boolean  |             |
 * | is_admin              | boolean  |             |
 * +-----------------------+----------+-------------+
 * </pre>
 *
 */
public class AccountDBModel implements CRUDModel {

    private Integer accountId;
    private String email;
    private String refreshToken;
    private String firstname;
    private String lastname;
    private String password;
    private byte[] salt;
    private Boolean muteNotifications;
    private Boolean emailValidated;
    private Boolean isAdmin, isOperator;
    private String pushToken;

    private static final String tableName = "account";

    public static final Attribute IS_ADMIN_ATTRIBUTE
            = new Attribute("isAdmin", "is_admin", AttributeType.BOOLEAN, tableName, false);
    public static final Attribute IS_OPERATOR_ATTRIBUTE
            = new Attribute("isOperator", "is_operator", AttributeType.BOOLEAN, tableName, false);
    public static final Attribute PUSH_TOKEN_ATTRIBUTE
            = new Attribute("pushToken", "push_token", AttributeType.TEXT, tableName, false);

    private static List<Attribute> allAttributes = fillAttributes();

    private static List<Attribute> fillAttributes() {
        List<Attribute> out = new ArrayList<>();
        // required
        out.add(new Attribute("email", "email", AttributeType.TEXT, tableName, true));
        out.add(new Attribute("password", "password", AttributeType.TEXT, tableName, true));
        out.add(new Attribute("salt", "salt", AttributeType.BYTE, tableName, true));

        // not required
        out.add(new Attribute("refreshToken", "refresh_token", AttributeType.TEXT, tableName, false));
        out.add(new Attribute("firstname", "first_name", AttributeType.TEXT, tableName, false));
        out.add(new Attribute("lastname", "last_name", AttributeType.TEXT, tableName, false));
        out.add(new Attribute("muteNotifications", "mute_notifications", AttributeType.BOOLEAN, tableName, false));
        out.add(new Attribute("emailValidated", "email_validated", AttributeType.BOOLEAN, tableName, false));
        out.add(IS_ADMIN_ATTRIBUTE);
        out.add(IS_OPERATOR_ATTRIBUTE);
        out.add(PUSH_TOKEN_ATTRIBUTE);
        return out;
    }

    /**
     * Default empty constructor for the CRUDdao
     */
    public AccountDBModel() {
        // fill in accoring to default values
        this.muteNotifications = false;
        this.emailValidated = false;
        this.isAdmin = false;
        this.isOperator = false;
    }

    public AccountDBModel(String email, String firstname, String lastname, String password,
            boolean muteNotifications, boolean emailValidated, String refreshToken, byte[] salt,
            boolean isOperator, boolean isAdmin, String pushToken) {
        this.email = email;
        this.firstname = firstname;
        this.lastname = lastname;
        this.password = password;
        this.muteNotifications = muteNotifications;
        this.emailValidated = emailValidated;
        this.refreshToken = refreshToken;
        this.salt = salt;
        this.isAdmin = isAdmin;
        this.isOperator = isOperator;
        this.pushToken = pushToken;
    }

    /**
     *
     * @param email
     * @param password
     * @param salt
     */
    public AccountDBModel(String email, String password, byte[] salt) {
        this.email = email;
        this.password = password;
        this.salt = salt;
        // default values;
        this.muteNotifications = false;
        this.emailValidated = false;
        this.isAdmin = false;
        this.isOperator = false;
    }

    @Override
    public void setId(int id) {
        setAccountId(id);
    }

    @Override
    public int getId() {
        return getAccountId();
    }

    @Override
    public List<Attribute> getActiveAttributeList() {
        List<Attribute> out = new ArrayList<>();
        // first all required. Must be filled in
        out.add(new Attribute("email", "email", AttributeType.TEXT, "account", true));
        out.add(new Attribute("password", "password", AttributeType.TEXT, "account", true));
        out.add(new Attribute("salt", "salt", AttributeType.BYTE, tableName, true));
        out.add(IS_ADMIN_ATTRIBUTE);
        out.add(IS_OPERATOR_ATTRIBUTE);

        // now check for each non required field if it's filled in
        if (firstname != null) {
            out.add(new Attribute("firstname", "first_name", AttributeType.TEXT, "account", false));
        }
        if (lastname != null) {
            out.add(new Attribute("lastname", "last_name", AttributeType.TEXT, "account", false));
        }
        if (muteNotifications != null) {
            out.add(new Attribute("muteNotifications", "mute_notifications", AttributeType.BOOLEAN, "account", false));
        }
        if (emailValidated != null) {
            out.add(new Attribute("emailValidated", "email_validated", AttributeType.BOOLEAN, tableName, false));
        }
        if (refreshToken != null) {
            out.add(new Attribute("refreshToken", "refresh_token", AttributeType.TEXT, tableName, false));
        }
        if(pushToken != null) {
            out.add(PUSH_TOKEN_ATTRIBUTE);
        }
        return out;
    }

    @Override
    public List<Attribute> getAllAttributeList() {
        return allAttributes;
    }

    @Override
    public String getTableName() {
        return tableName;
    }

    @Override
    public String getIdColumnName() {
        return "accountid";
    }

    /**
     *
     * @return
     */
    public Integer getAccountId() {
        return accountId;
    }

    /**
     *
     * @return
     */
    public static Attribute getAccountIdAttribute() {
        return new Attribute("accountId", "accountid", AttributeType.INTEGER, tableName, false);
    }

    /**
     *
     * @param accountId
     */
    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    /**
     *
     * @return
     */
    public String getEmail() {
        return email;
    }

    /**
     *
     * @return
     */
    public static Attribute getEmailAttribute() {
        return new Attribute("email", "email", AttributeType.TEXT, tableName, true);
    }

    /**
     *
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     *
     * @return
     */
    public String getFirstname() {
        return firstname;
    }

    /**
     *
     * @return
     */
    public static Attribute getFirstNameAttribute() {
        return new Attribute("firstname", "first_name", AttributeType.TEXT, tableName, false);
    }

    /**
     *
     * @param firstname
     */
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    /**
     *
     * @return
     */
    public String getLastname() {
        return lastname;
    }

    /**
     *
     * @return
     */
    public static Attribute getLastNameAttribute() {
        return new Attribute("lastname", "last_name", AttributeType.TEXT, tableName, false);
    }

    /**
     *
     * @param lastname
     */
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    /**
     *
     * @return
     */
    public String getPassword() {
        return password;
    }

    /**
     *
     * @return
     */
    public static Attribute getPasswordAttribute() {
        return new Attribute("password", "password", AttributeType.TEXT, tableName, true);
    }

    /**
     *
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     *
     * @return
     */
    public Boolean getMuteNotifications() {
        return muteNotifications;
    }

    /**
     *
     * @return
     */
    public static Attribute getMuteNotificationsAttribute() {
        return new Attribute("muteNotifications", "mute_notifications", AttributeType.BOOLEAN, tableName, false);
    }

    /**
     *
     * @param muteNotifications
     */
    public void setMuteNotifications(Boolean muteNotifications) {
        this.muteNotifications = muteNotifications;
    }

    /**
     *
     * @return
     */
    public Boolean getEmailValidated() {
        return emailValidated;
    }

    /**
     *
     * @return
     */
    public static Attribute getEmailValidatedAttribute() {
        return new Attribute("emailValidated", "email_validated", AttributeType.BOOLEAN, tableName, false);
    }

    /**
     *
     * @param emailValidated
     */
    public void setEmailValidated(Boolean emailValidated) {
        this.emailValidated = emailValidated;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public static Attribute getRefreshTokenAttribute() {
        return new Attribute("refreshToken", "refresh_token", AttributeType.TEXT, tableName, false);
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public byte[] getSalt() {
        return salt;
    }

    public void setSalt(byte[] salt) {
        this.salt = salt;
    }

    public Boolean getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(Boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public Boolean getIsOperator() {
        return isOperator;
    }

    public void setIsOperator(Boolean isOperator) {
        this.isOperator = isOperator;
    }

    public String getPushToken() {
        return pushToken;
    }

    public void setPushToken(String pushToken) {
        this.pushToken = pushToken;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof AccountDBModel)) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        AccountDBModel oth = (AccountDBModel) obj;
        return new EqualsBuilder()
                .append(accountId, oth.accountId)
                .append(email, oth.email)
                .append(firstname, oth.firstname)
                .append(lastname, oth.lastname)
                .append(password, oth.password)
                .append(muteNotifications, oth.muteNotifications)
                .append(emailValidated, oth.emailValidated)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(5395, 335)
                .append(accountId)
                .append(email)
                .append(firstname)
                .append(lastname)
                .append(password)
                .append(muteNotifications)
                .append(emailValidated)
                .toHashCode();
    }

}
