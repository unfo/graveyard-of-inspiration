package discmania.ui;

import discmania.store.Disc;
import discmania.store.Item;
import discmania.store.Order;
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
 * Allows customers to view the orders they've made.
 *
 * @author Jari Aarniala (jari.aarniala@cs.helsinki.fi)
 * @version $Id: ViewOrdersServlet.java,v 1.1 2002/12/04 13:06:31 foo Exp $
 */
public class ViewOrdersServlet extends BaseServlet {

    public String process(HttpServletRequest req,
                          HttpServletResponse resp,
                          Context context) throws Exception {

        HttpSession session = req.getSession();
        User user = (User)session.getAttribute("user");

        if( user == null ){
            showLoginPage(resp, "orders");
            return null;
        }

        // the id of the order whose contents to view
        int viewId = -1;
        try {
            viewId = Integer.parseInt(req.getParameter("viewId"));
            context.put("viewId", new Integer(viewId));
        } catch(Exception e){ }

        Connection conn = null;
        PreparedStatement getOrdersStmt = null;
        ResultSet ordersRs = null;
        PreparedStatement getDiscsStmt = null;
        ResultSet discsRs = null;

        List orders = new ArrayList();

        try {

            conn = getConnection();

            getOrdersStmt = conn.prepareStatement("SELECT * FROM discmania_order WHERE user_id = ? ORDER BY arrived");
            getDiscsStmt = conn.prepareStatement(
                    "SELECT d.id, d.artist, d.name, d.balance, d.removed, od.price, od.quantity " +
                    "FROM disc d, order_disc od WHERE od.order_id = ? AND " +
                    "d.id = od.disc_id ORDER BY d.artist, d.name"
            );

            getOrdersStmt.setString(1, user.getLogin());
            ordersRs = getOrdersStmt.executeQuery();

            while( ordersRs.next() ){
                // get orders
                int orderId = ordersRs.getInt("id");
                Order order = new Order(
                        ordersRs.getTimestamp("arrived"),
                        orderId,
                        null,
                        ordersRs.getTimestamp("processed")
                );
                getDiscsStmt.setInt(1, orderId);
                discsRs = getDiscsStmt.executeQuery();
                List items = new ArrayList();
                while( discsRs.next() ){
                    // get the items in each order
                    String removed = discsRs.getString("removed");
                    Disc disc = new Disc(
                            discsRs.getString("artist"),
                            discsRs.getInt("balance"),
                            discsRs.getInt("id"),
                            discsRs.getString("name"),
                            discsRs.getDouble("price"),
                            null,
                            removed != null && removed.equals("1")
                    );
                    Item item = new Item(disc, discsRs.getInt("quantity"));
                    items.add(item);
                }
                discsRs.close();
                order.setItems(items);
                orders.add(order);
            }

        } catch(Exception e){
            log.error("Database error", e);
            context.put("errors", new String[]{ "Database error, " + e.getMessage() });
        } finally {
            if(ordersRs != null){ try{ordersRs.close();}catch(Exception e){} }
            if(getDiscsStmt != null){ try{getDiscsStmt.close();}catch(Exception e){} }
            if(getOrdersStmt != null){ try{getOrdersStmt.close();}catch(Exception e){} }
            if(conn != null){ try{conn.close();}catch(Exception e){} }
        }

        context.put("orders", orders);

        return "order_history.vm";

    }

}
