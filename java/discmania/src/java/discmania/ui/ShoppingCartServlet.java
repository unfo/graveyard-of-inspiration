package discmania.ui;

import discmania.store.Disc;
import discmania.store.Item;
import discmania.store.Order;
import org.apache.velocity.context.Context;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Maintains the shopping cart.
 *
 * @author Jari Aarniala (jari.aarniala@cs.helsinki.fi)
 * @version $Id: ShoppingCartServlet.java,v 1.1 2002/12/04 13:06:31 foo Exp $
 */
public class ShoppingCartServlet extends BaseServlet {

    public String process(HttpServletRequest req, HttpServletResponse resp, Context context) {

        HttpSession session = req.getSession();
        Order cart = (Order)session.getAttribute("cart");
        String action = req.getParameter("action");
        int id = -1;
        try {
            id = Integer.parseInt(req.getParameter("id"));
        } catch(Exception e){}

        if( cart == null ){
            cart = new Order();
            cart.setItems(new ArrayList());
        }

        if(action == null || action.equals("view")){
            // view cart (no need to do anything)
        } else if(action.equals("add")){
            // add items to cart

            // no id specified
            if( id == -1 ){
                return "cart.vm";
            }

            // first check if the item is already in the cart
            boolean found;
            found = updateItem(cart, id, true, 0);

            if(!found){

                Connection conn = null;
                PreparedStatement stmt = null;
                ResultSet rs = null;

                try {
                    conn = getConnection();
                    stmt = conn.prepareStatement("SELECT * FROM disc WHERE id = ?");
                    stmt.setInt(1, id);
                    rs = stmt.executeQuery();

                    if( !rs.next() ){
                        context.put("errors", new String[]{ "Disc not found" });
                    } else {

                        String rem = rs.getString("removed");
                        boolean removed = rem != null && rem.equals("1");

                        if(removed){
                            context.put("errors", new String[]{ "Can't add disc - it's been removed from catalogue!" });
                        } else {

                            Disc disc = new Disc(
                                    rs.getString("artist"),
                                    rs.getInt("balance"),
                                    rs.getInt("id"),
                                    rs.getString("name"),
                                    rs.getDouble("price"),
                                    null,
                                    removed
                            );

                            List list = cart.getItems();
                            list.add(new Item(disc, 1));
                            cart.setItems(list);

                        }
                    }

                } catch(Exception e){
                    log.error("Database error", e);
                    context.put("errors", new String[]{ "Database error, " + e.getMessage() });
                } finally {
                    if(rs != null){ try{rs.close();}catch(Exception e){} }
                    if(stmt != null){ try{stmt.close();}catch(Exception e){} }
                    if(conn != null){ try{conn.close();}catch(Exception e){} }
                }

            }

        } else if(action.equals("update")){
            if( id == -1 ){
                return "cart.vm";
            }
            int quantity;
            try {
                quantity = Integer.parseInt(req.getParameter("quantity"));
                updateItem(cart, id, false, quantity);
            } catch(Exception e){
                // ignored
            }
        } else if(action.equals("clear")){
            // clear cart contents
            if( cart != null ){
                cart.setItems(new ArrayList());
            }
        }

        session.setAttribute("cart", cart);
        context.put("cart", cart);

        return "cart.vm";
    }

    /**
     * Updates an item in the cart .
     * If <code>increment</code> is true, the quantity of the item
     * is incremented by one. If it is false the quantity
     * is set to <code>quantity</code>. If <code>quantity</code>
     * is less than one, the item is removed from the cart.
     *
     * @param cart          the cart
     * @param id            id of the disc
     * @param increment     what to do
     * @param quantity      quantity to set (if any)
     * @return true if item was found in the cart
     */
    private boolean updateItem(Order cart, int id, boolean increment, int quantity){
        boolean found = false;
        if( cart != null && cart.getItems() != null ){
            Iterator i = cart.getItems().iterator();
            while(i.hasNext()){
                Item item = (Item)i.next();
                if(item.getDisc().getId() == id){
                    // found the disc
                    if( increment ){
                        item.setQuantity(item.getQuantity() + 1);
                    } else {
                        if( quantity < 1 ){
                            i.remove();
                        } else {
                            item.setQuantity(quantity);
                        }
                    }
                    found = true;
                    break;
                }
            }
        }
        return found;
    }

}
