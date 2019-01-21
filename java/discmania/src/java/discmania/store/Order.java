package discmania.store;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * An order made by a customer.
 *
 * @author Jari Aarniala (jari.aarniala@cs.helsinki.fi)
 * @version $Id: Order.java,v 1.1 2002/12/04 13:06:31 foo Exp $
 */
public class Order {

    /**
     * The unique identifier of this order.
     */
    protected int m_id;

    /**
     * Time of arrival
     */
    protected Date m_arrived;

    /**
     * Time of processing
     */
    protected Date m_processed;

    /**
     * The items featured in this order.
     * @see discmania.store.Item
     */
    protected List m_items;

    /**
     * Creates a new order with empty values.
     */
    public Order() {

    }

    /**
     * Creates a new order.
     *
     * @param arrived    time of arrival
     * @param id         unique id
     * @param items      discs included
     * @param processed  time of processing
     */
    public Order(Date arrived, int id, List items, Date processed) {
        m_arrived = arrived;
        m_id = id;
        m_items = items;
        m_processed = processed;
    }

    // accessor and setter methods follow

    public Date getArrived() {
        return m_arrived;
    }

    public void setArrived(Date arrived) {
        m_arrived = arrived;
    }

    public int getId() {
        return m_id;
    }

    public void setId(int id) {
        m_id = id;
    }

    public List getItems() {
        return m_items;
    }

    public void setItems(List items) {
        m_items = items;
    }

    public Date getProcessed() {
        return m_processed;
    }

    public void setProcessed(Date processed) {
        m_processed = processed;
    }

    /**
     * Calculates the total price of this Order.
     * @return total price of this Order
     */
    public double getTotalPrice(){
        double total = 0.0;
        if(m_items == null){
            return total;
        }
        for(Iterator i = m_items.iterator(); i.hasNext(); ){
            total += ((Item)i.next()).getTotalPrice();
        }
        return total;
    }

    /**
     * Returns the number of items in this order.
     * @return
     */
    public int getTotalItems(){
        int total = 0;
        if(m_items == null){
            return total;
        }
        for(Iterator i = m_items.iterator(); i.hasNext(); ){
            total += ((Item)i.next()).getQuantity();
        }
        return total;
    }

    public String toString() {
        return "Order { id=" + m_id + ", arrived=" + m_arrived + ", processed=" + m_processed + ", " + m_items.size() + " discs }";
    }

}
