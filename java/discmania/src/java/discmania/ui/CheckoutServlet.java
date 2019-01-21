package discmania.ui;

import discmania.store.Disc;
import discmania.store.Item;
import discmania.store.Order;
import discmania.store.User;
import org.apache.velocity.context.Context;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.*;
import java.util.Iterator;
import java.util.List;

/**
 * Checkout servlet. Places an order and simulates the payment.
 *
 * @author Jari Aarniala (jari.aarniala@cs.helsinki.fi)
 * @version $Id: CheckoutServlet.java,v 1.1 2002/12/04 13:06:31 foo Exp $
 */
public class CheckoutServlet extends BaseServlet {

    public String process(HttpServletRequest req,
                          HttpServletResponse resp,
                          Context context) throws Exception {

        String phase = req.getParameter("phase");

        HttpSession session = req.getSession();
        User user = (User)session.getAttribute("user");
        if( user == null ){
            // not logged on!
            showLoginPage(resp, "checkout");
            return null;
        }

        Order order = (Order)session.getAttribute("cart");
        context.put("order", order);

        List list = null;
        if(order == null || (list = order.getItems()) == null || list.size() == 0){
            context.put("errors", new String[]{ "You must have some discs in your order" });
            return "error.vm";
        }

        if( phase != null && phase.equals("confirmed") ){
            // final phase: save the order to database

            boolean ok = false;

            Connection conn = null;
            PreparedStatement insertOrderStmt = null;
            Statement getOrderIdStmt = null;
            ResultSet orderIdRs = null;
            PreparedStatement insertDiscsStmt = null;
            PreparedStatement updateDiscBalanceStmt = null;

            try {

                conn = getConnection();

                // insert the order
                insertOrderStmt = conn.prepareStatement("INSERT INTO discmania_order (arrived, user_id) VALUES (?, ?)");
                insertOrderStmt.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
                insertOrderStmt.setString(2, user.getLogin());
                insertOrderStmt.executeUpdate();

                // get the assigned id (this is PostgreSQL -specific..)
                getOrderIdStmt = conn.createStatement();
                orderIdRs = getOrderIdStmt.executeQuery("SELECT currval('order_seq')");
                int assignedId = -1;
                while( orderIdRs.next() ){
                    assignedId = orderIdRs.getInt(1);
                    log.debug("Order got id #" + assignedId);
                }

                // insert the discs
                insertDiscsStmt = conn.prepareStatement(
                        "INSERT INTO order_disc (disc_id, order_id, price, quantity) " +
                        "VALUES(?, ?, ?, ?)"
                );
                for(Iterator i = order.getItems().iterator(); i.hasNext(); ){
                    Item item = (Item)i.next();
                    Disc disc = item.getDisc();
                    insertDiscsStmt.setInt(1, disc.getId());
                    insertDiscsStmt.setInt(2, assignedId);
                    insertDiscsStmt.setDouble(3, disc.getPrice());
                    insertDiscsStmt.setInt(4, item.getQuantity());
                    insertDiscsStmt.executeUpdate();
                    log.debug("Added disc #" + disc.getId() + " to order");
                }

                // finally, decrement the disk balances
                updateDiscBalanceStmt = conn.prepareStatement("UPDATE disc SET balance = balance - ? WHERE id = ?");

                for(Iterator i = order.getItems().iterator(); i.hasNext(); ){
                    Item item = (Item)i.next();
                    updateDiscBalanceStmt.setInt(1, item.getQuantity());
                    updateDiscBalanceStmt.setInt(2, item.getDisc().getId());
                    updateDiscBalanceStmt.executeUpdate();
                }

                conn.commit();

                ok = true;

            } catch(Exception e){
                if( conn != null ){
                    conn.rollback();
                }
                log.error("Database error", e);
                context.put("errors", new String[]{ "Database error, " + e.getMessage() });
            } finally {
                if(insertDiscsStmt != null){ try{insertDiscsStmt.close();}catch(Exception e){} }
                if(orderIdRs != null){ try{orderIdRs.close();}catch(Exception e){} }
                if(insertOrderStmt != null){ try{insertOrderStmt.close();}catch(Exception e){} }
                if(updateDiscBalanceStmt != null){ try{updateDiscBalanceStmt.close();}catch(Exception e){} }
                if(conn != null){ try{conn.close();}catch(Exception e){} }
            }

            if(ok){
                // empty the cart
                session.setAttribute("cart", null);
                return "order_placed.vm";
            } else {
                return "error.vm";
            }

        } else {
            // first phase: ask for confirmation
            return "order_confirmation.vm";
        }

    }

}
