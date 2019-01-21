package discmania.ui;

import discmania.store.Disc;
import discmania.store.Order;
import discmania.store.User;
import org.apache.velocity.context.Context;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Used by the employees at warehouse to control the disc balances etc.
 *
 * @author Jari Aarniala (jari.aarniala@cs.helsinki.fi)
 * @version $Id: WarehouseServlet.java,v 1.1 2002/12/04 13:06:31 foo Exp $
 */
public class WarehouseServlet extends BaseServlet {

    public String process(HttpServletRequest req,
                          HttpServletResponse resp,
                          Context context) throws Exception {

        HttpSession session = req.getSession();
        User user = (User)session.getAttribute("user");

        if( user == null){
            showLoginPage(resp, "warehouse");
            return null;
        }

        if( user.getType() != User.WAREHOUSE ){
            context.put("errors", new String[]{ "Must be a warehouse employee to access this page" });
            return "error.vm";
        }

        String action = req.getParameter("action");
        context.put("action", action);

        if( "balances".equals(action) ){

            List discs = new ArrayList();

            Connection conn = null;
            Statement stmt = null;
            ResultSet rs = null;
            try {
                conn = getConnection();

                stmt = conn.createStatement();
                rs = stmt.executeQuery("SELECT * FROM disc ORDER BY artist, name");
                while( rs.next() ){
                    String removed = rs.getString("removed");
                    Disc disc = new Disc(
                            rs.getString("artist"),
                            rs.getInt("balance"),
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getDouble("price"),
                            null,
                            removed != null && removed.equals("1")
                    );
                    discs.add(disc);
                }

            } catch(Exception e){
                log.error("Database error", e);
                context.put("errors", new String[]{ "Error! " + e.getMessage() });
            } finally {
                if(rs != null){ try{rs.close();}catch(Exception e){} }
                if(stmt != null){ try{stmt.close();}catch(Exception e){} }
                if(conn != null){ try{conn.close();}catch(Exception e){} }
            }

            context.put("discs", discs);

        } else if( "set_balance".equals(action) ){

            int id = -1;
            int balance = -1;
            boolean dataOk = true;
            try {
                id = Integer.parseInt(req.getParameter("id"));
                balance = Integer.parseInt(req.getParameter("balance"));
            } catch(Exception e){
                dataOk = false;
            }

            if(!dataOk){
                context.put("errors", new String[]{ "Bad id / balance" });
                return "error.vm";
            }

            Connection conn = null;
            PreparedStatement stmt = null;
            ResultSet rs = null;
            try {
                conn = getConnection();

                stmt = conn.prepareStatement("UPDATE disc SET balance = ? WHERE id = ?");
                stmt.setInt(1, balance);
                stmt.setInt(2, id);
                stmt.executeUpdate();

            } catch(Exception e){
                log.error("Database error", e);
                context.put("errors", new String[]{ "Error! " + e.getMessage() });
            } finally {
                if(rs != null){ try{rs.close();}catch(Exception e){} }
                if(stmt != null){ try{stmt.close();}catch(Exception e){} }
                if(conn != null){ try{conn.close();}catch(Exception e){} }
            }

        } else if( "process".equals(action) ){

            Connection conn = null;
            Statement waitingOrdersStmt = null;
            ResultSet waitingOrders = null;
            List orders = new ArrayList();

            try {

                conn = getConnection();

                waitingOrdersStmt = conn.createStatement();
                waitingOrders = waitingOrdersStmt.executeQuery("SELECT id, arrived FROM discmania_order WHERE processed IS NULL ORDER BY arrived");

                while( waitingOrders.next() ){
                    // get orders
                    int orderId = waitingOrders.getInt("id");
                    Order order = new Order(
                            waitingOrders.getTimestamp("arrived"),
                            orderId,
                            null,
                            null
                    );
                    orders.add(order);
                }

            } catch(Exception e){
                log.error("Database error", e);
                context.put("errors", new String[]{ "Database error, " + e.getMessage() });
            } finally {
                if(waitingOrdersStmt != null){ try{waitingOrdersStmt.close();}catch(Exception e){} }
                if(waitingOrders != null){ try{waitingOrders.close();}catch(Exception e){} }
                if(conn != null){ try{conn.close();}catch(Exception e){} }
            }

            context.put("orders", orders);

        } else if( "process_order".equals(action) ){

            int id = -1;
            try {
                id = Integer.parseInt(req.getParameter("id"));
            } catch(Exception e){
                context.put("errors", new String[]{ "Bad id / balance" });
                return "error.vm";
            }

            Connection conn = null;

            PreparedStatement updateOrderStmt = null;

            try {

                conn = getConnection();

                updateOrderStmt = conn.prepareStatement("UPDATE discmania_order SET processed = ? WHERE id = ?");
                updateOrderStmt.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
                updateOrderStmt.setInt(2, id);
                updateOrderStmt.executeUpdate();

            } catch(Exception e){
                log.error("Database error", e);
                context.put("errors", new String[]{ "Database error, " + e.getMessage() });
            } finally {
                if(updateOrderStmt != null){ try{updateOrderStmt.close();}catch(Exception e){} }
                if(conn != null){ try{conn.close();}catch(Exception e){} }
            }

        }

        return "warehouse.vm";

    }

}
