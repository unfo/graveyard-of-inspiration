package discmania.ui;

/**
 * A simple java bean representing a result page in a disc listing.
 *
 * @author Jari Aarniala (jari.aarniala@cs.helsinki.fi)
 * @version $Id$
 */
public class ResultPage {

    /**
     * Page number
     */
    protected int m_number;

    /**
     * Start index of the page
     */
    protected int m_start;

    /**
     * Is this the current page?
     */
    protected boolean m_current;

    public ResultPage(int number, int start, boolean current){
        m_number = number;
        m_start = start;
        m_current = current;
    }

    public boolean getCurrent() {
        return m_current;
    }

    public void setCurrent(boolean current) {
        m_current = current;
    }

    public int getNumber() {
        return m_number;
    }

    public void setNumber(int number) {
        m_number = number;
    }

    public int getStart() {
        return m_start;
    }

    public void setStart(int start) {
        m_start = start;
    }

}
