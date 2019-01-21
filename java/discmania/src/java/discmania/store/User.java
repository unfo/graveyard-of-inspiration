package discmania.store;

import java.util.List;

/**
 * A user of the webstore. Can be a customer or an employee.
 *
 * @author Jari Aarniala (jari.aarniala@cs.helsinki.fi)
 * @version $Id: User.java,v 1.1 2002/12/04 13:06:31 foo Exp $
 */
public class User {

    // constant values representing user types.

    /**
     * A customer
     */
    public static final int CUSTOMER   = 0;

    /**
     * An employee working in marketing
     */
    public static final int MARKETING  = 1;

    /**
     * An employee working at the warehouse
     */
    public static final int WAREHOUSE  = 2;

    /**
     * An employee working at the information management
     */
    public static final int MANAGEMENT = 3;

    // member variables

    /**
     * The login name of this user
     */
    protected String m_login;

    /**
     * Password of this user
     */
    protected String m_password;

    /**
     * The name of this user
     */
    protected String m_name;

    /**
     * User's email address
     */
    protected String m_email;

    /**
     * Snail mail (shipping) address
     */
    protected String m_address;

    /**
     * Phone number
     */
    protected String m_phone;

    /**
     * User type. Can be checked in order to restrict access to certain pages.
     */
    protected int    m_type;

    /**
     * The orders made by this customer.
     */
    protected List   m_orders;

    // constructors

    /**
     * Creates a new User with empty values
     */
    public User() {
    }

    /**
     * Creates a new User with the specified values
     *
     * @param address
     * @param email
     * @param login
     * @param name
     * @param password
     * @param phone
     * @param type
     * @param orders
     */
    public User(String address, String email, String login, String name, String password, String phone, int type, List orders) {
        m_address = address;
        m_email = email;
        m_login = login;
        m_name = name;
        m_password = password;
        m_phone = phone;
        m_type = type;
        m_orders = orders;
    }

    // methods concerning user types

    public boolean isCustomer(){
        return m_type == CUSTOMER;
    }

    public boolean isFromMarketing(){
        return m_type == MARKETING;
    }

    public boolean isFromWarehouse(){
        return m_type == WAREHOUSE;
    }

    public boolean isFromManagement(){
        return m_type == MANAGEMENT;
    }

    // getters and setters

    public String getAddress() {
        return m_address;
    }

    public void setAddress(String address) {
        m_address = address;
    }

    public String getEmail() {
        return m_email;
    }

    public void setEmail(String email) {
        m_email = email;
    }

    public String getLogin() {
        return m_login;
    }

    public void setLogin(String login) {
        m_login = login;
    }

    public String getName() {
        return m_name;
    }

    public void setName(String name) {
        m_name = name;
    }

    public String getPassword() {
        return m_password;
    }

    public void setPassword(String password) {
        m_password = password;
    }

    public String getPhone() {
        return m_phone;
    }

    public void setPhone(String phone) {
        m_phone = phone;
    }

    public int getType() {
        return m_type;
    }

    public void setType(int type) {
        m_type = type;
    }

    public List getOrders() {
        return m_orders;
    }

    public void setOrders(List orders) {
        m_orders = orders;
    }

    public String toString() {
        return "User { login=" + m_login + ", type=" + m_type + " }";
    }

}
