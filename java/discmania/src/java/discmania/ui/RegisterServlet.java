package discmania.ui;

import discmania.store.User;
import org.apache.velocity.context.Context;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

/**
 * Registers new users.
 *
 * @author Jari Aarniala (jari.aarniala@cs.helsinki.fi)
 * @version $Id: RegisterServlet.java,v 1.1 2002/12/04 13:06:31 foo Exp $
 */
public class RegisterServlet extends BaseServlet {

    private static final String FORM = "register_form.vm";
    private static final String OK   = "register.vm";

    public String process(HttpServletRequest req, HttpServletResponse resp, Context context) {

        String action = req.getParameter("action");
        if( action == null || !action.equals("register") ){
            // show the form
            return FORM;
        }

        // do registration
        String address = req.getParameter("address");
        String email = req.getParameter("email");
        String login = req.getParameter("username");
        String name = req.getParameter("name");
        String password = req.getParameter("password");
        String password2 = req.getParameter("password2");
        String phone = req.getParameter("phone");

        List errors = new ArrayList();

        // error checking
        if( login == null || login.length() < 1 ){
            errors.add("Username is required");
        }
        if( password == null || password.length() < 5 ){
            errors.add("Please provide a reasonably long password (at least 5 characters)");
        } else if( password2 == null || !password.equals(password2) ){
            errors.add("Passwords didn't match");
        }
        if( name == null || name.length() < 1){
            errors.add("Name is required");
        }

        // if there were errors, show the form
        if( errors.size() > 0 ){
            // errors
            context.put("errors", errors);
            // field values
            context.put("address", address);
            context.put("email", email);
            context.put("username", login);
            context.put("name", name);
            context.put("phone", phone);
            return FORM;
        }

        boolean ok = false;
        Connection conn = null;
        PreparedStatement ps = null;
        try {

            conn = getConnection();
            ps = conn.prepareStatement(
                "INSERT INTO discmania_user (login, password, name, address, email, phone, type) VALUES "
                    + "(?, ?, ?, ?, ?, ?, ?);"
            );
            ps.setString(1, login);
            ps.setString(2, password);
            ps.setString(3, name);
            ps.setString(4, address);
            ps.setString(5, email);
            ps.setString(6, phone);
            ps.setInt(7, User.CUSTOMER);
            ps.executeUpdate();

            ok = true;

        } catch (Exception e) {
            log.error("Database error!", e);
            context.put("errors", new String[]{ e.getMessage() });
        } finally {
            if(ps != null){
                try { ps.close(); } catch(Exception e){}
            }
            if(conn != null){
                try { conn.close(); } catch(Exception e){}
            }
        }

        return ok ? OK : FORM;

    }

}
