package discmania.store;

import java.util.List;

/**
 * Represents a single Disc in the store.
 *
 * @author Jari Aarniala (jari.aarniala@cs.helsinki.fi)
 * @version $Id: Disc.java,v 1.1 2002/12/04 13:06:31 foo Exp $
 */
public class Disc {

    /**
     * The id of this Disc in the database
     */
    protected int m_id;

    /**
     * The name of this Disc
     */
    protected String m_name;

    /**
     * The author of this record
     */
    protected String m_artist;

    /**
     * The price of this Disc
     */
    protected double m_price;

    /**
     * Current warehouse balance of this Disc
     */
    protected int m_balance;

    /**
     * The Tracks this Disc contains.
     */
    protected List m_tracks;

    /**
     * If true, this Disc is no longer in the catalogue
     */
    protected boolean m_removed;

    /**
     * Creates a new Disc with empty values.
     */
    public Disc(){

    }

    /**
     * Creates a new Disc with the specified values.
     *
     * @param artist
     * @param balance
     * @param id
     * @param name
     * @param price
     * @param tracks
     * @param removed
     */
    public Disc(String artist, int balance, int id, String name, double price, List tracks, boolean removed) {
        m_artist = artist;
        m_balance = balance;
        m_id = id;
        m_name = name;
        m_price = price;
        m_tracks = tracks;
        m_removed = removed;
    }

    public String getArtist() {
        return m_artist;
    }

    public void setArtist(String artist) {
        m_artist = artist;
    }

    public int getBalance() {
        return m_balance;
    }

    public void setBalance(int balance) {
        m_balance = balance;
    }

    public int getId() {
        return m_id;
    }

    public void setId(int id) {
        m_id = id;
    }

    public String getName() {
        return m_name;
    }

    public void setName(String name) {
        m_name = name;
    }

    public double getPrice() {
        return m_price;
    }

    public void setPrice(double price) {
        m_price = price;
    }

    public List getTracks() {
        return m_tracks;
    }

    public void setTracks(List tracks) {
        m_tracks = tracks;
    }

    public boolean isRemoved() {
        return m_removed;
    }

    public void setRemoved(boolean removed) {
        m_removed = removed;
    }

    public String toString() {
        return "Disc { artist=" + m_artist + ", name=" + m_name + " }";
    }

}
