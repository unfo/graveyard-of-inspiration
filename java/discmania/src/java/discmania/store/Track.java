package discmania.store;

/**
 * A track in a Disc.
 *
 * @author Jari Aarniala (jari.aarniala@cs.helsinki.fi)
 * @version $Id: Track.java,v 1.1 2002/12/04 13:06:31 foo Exp $
 */
public class Track {

    /**
     * The number of this track
     */
    protected int    m_number;

    /**
     * The name of this Track
     */
    protected String m_name;

    /**
     * Creates a new Track
     * @param name
     * @param number
     */
    public Track(String name, int number) {
        m_name = name;
        m_number = number;
    }

    public String getName() {
        return m_name;
    }

    public void setName(String name) {
        m_name = name;
    }

    public int getNumber() {
        return m_number;
    }

    public void setNumber(int number) {
        m_number = number;
    }

    public String toString(){
        return "Track #" + m_number + ", " + m_name;
    }

}
