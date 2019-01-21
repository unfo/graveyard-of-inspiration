package discmania.ui;

import discmania.store.User;
import org.apache.velocity.context.Context;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * Logs Users in and out of the system.
 *
 * @author Jari Aarniala (jari.aarniala@cs.helsinki.fi)
 * @version $Id: LoginServlet.java,v 1.1 2002/12/04 13:06:31 foo Exp $
 */
public class LoginServlet extends BaseServlet {

    public String process(HttpServletRequest req,
                          HttpServletResponse resp,
                          Context context) throws Exception {

        String action = req.getParameter("action");
        String page = req.getParameter("page");

        context.put("page", page);

        // if logging out
        if( action != null && action.equals("logout") ){
            // destroy session
            HttpSession session = req.getSession(false);
            if( session != null ){
                session.invalidate();
            }
            context.put("user", null);
            return "logged_out.vm";
        }

        // if no action specified, show login form
        if( action == null || !action.equals("login") ){
            return "login.vm";
        }

        // log in
        List errors = new ArrayList();
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        if( username == null || password == null ){
            errors.add("Please give username and password");
            context.put("errors", errors);
            return "login.vm";
        }

        boolean ok = false;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            ps = conn.prepareStatement("SELECT password, name, address, " +
                    "phone, email, type FROM discmania_user WHERE login = ?");
            ps.setString(1, username);
            rs = ps.executeQuery();
            if( !rs.next() ){
                errors.add("Invalid username");
            } else {
                String realPassword = rs.getString("password");
                if( !realPassword.equals(password) ){
                    errors.add("Invalid password");
                } else {
                    User user = new User(
                            rs.getString("address"),
                            rs.getString("email"),
                            username,
                            rs.getString("name"),
                            rs.getString("password"),
                            rs.getString("phone"),
                            rs.getInt("type"),
                            null
                    );
                    log.debug("User logged in : " + user);
                    HttpSession session = req.getSession();
                    session.setAttribute("user", user);
                    ok = true;
                }
            }
        } catch(Exception e){
            log.error("Database error", e);
            errors.add("Database error: " + e.getMessage());
        } finally {
            if( rs != null ){ try { rs.close(); } catch(Exception e){} }
            if( ps != null ){ try { ps.close(); } catch(Exception e){} }
            if( conn != null ){ try { conn.close(); } catch(Exception e){} }
        }

        // if ok, forward to frontpage
        if(ok){
            resp.sendRedirect( page == null ? "browse" : page );
        } else {
            context.put("username", username);
            context.put("errors", errors);
        }

        return ok ? null : "login.vm";
    }

}
