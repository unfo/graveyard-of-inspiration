package discmania.ui;

import discmania.store.User;
import org.apache.velocity.context.Context;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * For managing users.
 *
 * @author Jari Aarniala (jari.aarniala@cs.helsinki.fi)
 * @version $Id: UserManagementServlet.java,v 1.1 2002/12/04 13:06:31 foo Exp $
 */
public class UserManagementServlet extends BaseServlet {

    /**
     * Different user types in an array
     */
    private static final String[] TYPES = new String[] {
        "Customer", "Marketing", "Warehouse", "Management"
    };

    public String process(HttpServletRequest req,
                          HttpServletResponse resp,
                          Context context) throws Exception {

        HttpSession session = req.getSession();
        User user = (User)session.getAttribute("user");

        if( user == null){
            showLoginPage(resp, "users");
            return null;
        }

        if( user.getType() != User.MANAGEMENT ){
            context.put("errors", new String[]{ "Must be from management to access this page" });
            return "error.vm";
        }

        String action = req.getParameter("action");
        String login  = req.getParameter("login");

        if( action == null ){
            // default action: show users

            Connection conn = null;
            Statement stmt = null;
            ResultSet rs = null;
            try {
                conn = getConnection();

                stmt = conn.createStatement();
                rs = stmt.executeQuery("SELECT * FROM discmania_user ORDER BY login");

                List users = new ArrayList();

                while( rs.next() ){
                    User u = new User(
                            rs.getString("address"),
                            rs.getString("email"),
                            rs.getString("login"),
                            rs.getString("name"),
                            rs.getString("password"),
                            rs.getString("phone"),
                            rs.getInt("type"),
                            null
                    );
                    users.add(u);
                }

                context.put("users", users);

            } catch(Exception e){
                log.error("Database error", e);
                context.put("errors", new String[]{ "Error! " + e.getMessage() });
            } finally {
                if(rs != null){ try{rs.close();}catch(Exception e){} }
                if(stmt != null){ try{stmt.close();}catch(Exception e){} }
                if(conn != null){ try{conn.close();}catch(Exception e){} }
            }

        } else if( action.equals("modify") ){

            // change user type

            int type = -1;
            try {
                type = Integer.parseInt(req.getParameter("type"));
            } catch(Exception e){}

            if( type < 0 || type > 3 || login == null ){
                context.put("errors", new String[] { "type/login invalid" });
                return "error.vm";
            }

            // all should be fine

            Connection conn = null;
            PreparedStatement stmt = null;
            try {
                conn = getConnection();

                stmt = conn.prepareStatement("UPDATE discmania_user SET type = ? WHERE login = ?");
                stmt.setInt(1, type);
                stmt.setString(2, login);
                stmt.executeUpdate();

            } catch(Exception e){
                log.error("Database error", e);
                context.put("errors", new String[]{ "Error! " + e.getMessage() });
            } finally {
                if(stmt != null){ try{stmt.close();}catch(Exception e){} }
                if(conn != null){ try{conn.close();}catch(Exception e){} }
            }

        } else if( action.equals("delete") ){

            // delete user

            if( login == null ){
                context.put("errors", new String[] { "Can't delete if I don't know the login.." });
                return "error.vm";
            }

            // all should be fine

            Connection conn = null;
            PreparedStatement stmt = null;
            try {
                conn = getConnection();

                stmt = conn.prepareStatement("DELETE FROM discmania_user WHERE login = ?");
                stmt.setString(1, login);
                stmt.executeUpdate();

            } catch(Exception e){
                log.error("Database error", e);
                context.put("errors", new String[]{ "Error! " + e.getMessage() });
            } finally {
                if(stmt != null){ try{stmt.close();}catch(Exception e){} }
                if(conn != null){ try{conn.close();}catch(Exception e){} }
            }

        }

        context.put("action", action);
        context.put("types", TYPES);

        return "users.vm";
    }

}
