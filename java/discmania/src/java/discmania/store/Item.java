package discmania.store;

/**
 * An item in an order.
 *
 * @author Jari Aarniala (jari.aarniala@cs.helsinki.fi)
 * @version $Id: Item.java,v 1.1 2002/12/04 13:06:31 foo Exp $
 */
public class Item {

    /**
     * The disc this item holds
     */
    protected Disc m_disc;

    /**
     * How many discs of this kind are there?
     */
    protected int  m_quantity;

    public Item(Disc disc, int quantity){
        m_disc = disc;
        m_quantity = quantity;
    }

    public Disc getDisc() {
        return m_disc;
    }

    public void setDisc(Disc disc) {
        m_disc = disc;
    }

    public int getQuantity() {
        return m_quantity;
    }

    public void setQuantity(int quantity) {
        m_quantity = quantity;
    }

    /**
     * Calculates the total price for this Item.
     */ 
    public double getTotalPrice(){
        return m_disc.getPrice() * m_quantity;
    }

}
