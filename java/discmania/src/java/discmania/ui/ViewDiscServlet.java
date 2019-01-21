package discmania.ui;

import discmania.store.Disc;
import discmania.store.Track;
import org.apache.velocity.context.Context;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * Views information of a single disc.
 *
 * @author Jari Aarniala (jari.aarniala@cs.helsinki.fi)
 * @version $Id: ViewDiscServlet.java,v 1.1 2002/12/04 13:06:31 foo Exp $
 */
public class ViewDiscServlet extends BaseServlet {

    /**
     * How many discs to show (at maximum) in the
     * "Customers who bought this also bought.." -list
     */
    private final static int ALSO_BOUGHT_DISCS = 3;

    public String process(HttpServletRequest req,
                          HttpServletResponse resp,
                          Context context) throws Exception {

        int id;
        try {
            id = Integer.parseInt(req.getParameter("id"));
        } catch(Exception e){
            log.debug("Invalid disc id, " + e);
            return "disc.vm";
        }

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        PreparedStatement tracksStmt = null;
        ResultSet tracksRs = null;
        PreparedStatement alsoBoughtStmt = null;
        ResultSet alsoBoughtRs = null;

        try {
            conn = getConnection();

            /*
                First load the disc.
             */
            stmt = conn.prepareStatement("SELECT * FROM disc WHERE id = ?");
            stmt.setInt(1, id);
            rs = stmt.executeQuery();

            if( !rs.next() ){
                context.put("errors", new String[]{ "Disc not found" });
                return "disc.vm";
            }

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

            tracksStmt = conn.prepareStatement("SELECT * FROM track WHERE disc_id = disc.id AND disc_id = ? ORDER BY number");
            tracksStmt.setInt(1, id);
            tracksRs = tracksStmt.executeQuery();

            List tracks = new ArrayList();

            while( tracksRs.next() ){
                tracks.add(new Track(tracksRs.getString("name"), tracksRs.getInt("number")));
            }

            disc.setTracks(tracks);

            /*
                Then load the 'Customers who bought this disc also bought..' -list
             */
            alsoBoughtStmt = conn.prepareStatement(
                    "SELECT d.* FROM disc d, order_disc od WHERE d.id=od.disc_id AND d.removed != '1' AND d.id != ? " +
                    "AND od.order_id IN (SELECT o.id FROM discmania_order o, order_disc od " +
                    "WHERE od.order_id=o.id AND od.disc_id = ?) ORDER BY od.quantity DESC limit ?"
            );
            alsoBoughtStmt.setInt(1, id);
            alsoBoughtStmt.setInt(2, id);
            alsoBoughtStmt.setInt(3, ALSO_BOUGHT_DISCS);

            alsoBoughtRs = alsoBoughtStmt.executeQuery();

            List alsoBought = new ArrayList();
            while( alsoBoughtRs.next() ){
                Disc d = new Disc(
                        alsoBoughtRs.getString("artist"),
                        alsoBoughtRs.getInt("balance"),
                        alsoBoughtRs.getInt("id"),
                        alsoBoughtRs.getString("name"),
                        alsoBoughtRs.getDouble("price"),
                        null,
                        false
                );
                alsoBought.add(d);
            }

            context.put("disc", disc);
            context.put("alsoBought", alsoBought);

        } catch(Exception e){
            log.error("Database error", e);
            context.put("errors", new String[]{ "Database error, " + e.getMessage() });
        } finally {
            if(rs != null){ try{rs.close();}catch(Exception e){} }
            if(stmt != null){ try{stmt.close();}catch(Exception e){} }
            if(tracksRs != null){ try{tracksRs.close();}catch(Exception e){} }
            if(tracksStmt != null){ try{tracksStmt.close();}catch(Exception e){} }
            if(conn != null){ try{conn.close();}catch(Exception e){} }
        }

        return "disc.vm";

    }

}
