package discmania.ui;

import discmania.store.Disc;
import discmania.store.Track;
import discmania.store.User;
import org.apache.velocity.context.Context;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages the disk catalogue at the store.
 *
 * @author Jari Aarniala (jari.aarniala@cs.helsinki.fi)
 * @version $Id: DiscManagementServlet.java,v 1.1 2002/12/04 13:06:31 foo Exp $
 */
public class DiscManagementServlet extends BaseServlet {

    public String process(HttpServletRequest req,
                          HttpServletResponse resp,
                          Context context) throws Exception {

        HttpSession session = req.getSession();
        User user = (User)session.getAttribute("user");

        if( user == null){
            showLoginPage(resp, "marketing");
            return null;
        }

        if( user.getType() != User.MARKETING ){
            context.put("errors", new String[]{ "Must be from marketing to access this page" });
            return "error.vm";
        }

        String action = req.getParameter("action");
        context.put("action", action);

        String form = req.getParameter("form");
        boolean submitted = form != null;

        if( action == null ){

            // default is to list the discs

            List discs = new ArrayList();

            Connection conn = null;
            Statement stmt = null;
            ResultSet rs = null;
            try {

                conn = getConnection();

                stmt = conn.createStatement();
                rs = stmt.executeQuery("SELECT * FROM disc ORDER BY removed, artist, name");
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

        } else if( action.equals("add") || action.equals("edit") ){

            boolean adding = action.equals("add");
            int id = -1;
            if( !adding ){
                try {
                    id = Integer.parseInt(req.getParameter("id"));
                } catch(Exception e){}
            }

            if(!submitted){
                // show form

                if( !adding ){
                    // if in edit mode need to get the data first
                    Connection conn = null;
                    PreparedStatement stmt = null;
                    ResultSet rs = null;
                    try {

                        conn = getConnection();

                        stmt = conn.prepareStatement("SELECT * FROM disc WHERE id = ?");
                        stmt.setInt(1, id);
                        rs = stmt.executeQuery();

                        if( rs.next() ){
                            context.put("artist", rs.getString("artist"));
                            context.put("name", rs.getString("name"));
                            context.put("price", new Double(rs.getDouble("price")));
                            context.put("id", new Integer(id));
                        } else {
                            context.put("errors", new String[] { "No such disc!"} );
                        }

                    } catch(Exception e){
                        log.error("Database error", e);
                        context.put("errors", new String[]{ "Error! " + e.getMessage() });
                    } finally {
                        if(rs != null){ try{rs.close();}catch(Exception e){} }
                        if(stmt != null){ try{stmt.close();}catch(Exception e){} }
                        if(conn != null){ try{conn.close();}catch(Exception e){} }
                    }
                }

                // either way show the same template
                return "marketing_edit.vm";

            } else {
                // add / edit the disc

                String artist = req.getParameter("artist");
                String name = req.getParameter("name");
                String priceStr = req.getParameter("price");

                // validate price
                double price = -1.0;
                try {
                    price = Double.parseDouble(priceStr);
                } catch(Exception e){}

                List errors = new ArrayList();

                if( price < 0.0 ){
                    errors.add("Please give a valid price");
                }

                // validate id if in edit mode
                if( !adding && id < 0 ){
                    errors.add("Not a valid id");
                }

                // validate artist and name
                if( artist == null || artist.length() < 1 ){
                    errors.add("Give the artist's name");
                }
                if( name == null || name.length() < 1 ){
                    errors.add("Give the name of the record");
                }

                // put the values to the context
                context.put("artist", artist);
                context.put("name", name);
                context.put("price", priceStr);
                context.put("id", new Integer(id));

                // if errors, show form and error messages
                if( errors.size() > 0 ){
                    context.put("errors", errors);
                    return "marketing_edit.vm";
                }

                // all's well, add/update the disc
                boolean ok = false;
                Connection conn = null;
                PreparedStatement ps = null;
                try {

                    conn = getConnection();

                    if( adding ){
                        // new discs are removed per default
                        ps = conn.prepareStatement(
                            "INSERT INTO disc (artist, name, price, removed) values (?, ?, ?, '1')"
                        );
                    } else {
                        ps = conn.prepareStatement(
                            "UPDATE disc SET artist = ?, name = ?, price = ? WHERE id = ?"
                        );
                    }
                    ps.setString(1, artist);
                    ps.setString(2, name);
                    ps.setDouble(3, price);
                    if( !adding ){
                        ps.setInt(4, id);
                    }

                    ps.executeUpdate();

                    ok = true;

                } catch (Exception e) {
                    log.error("Database error!", e);
                    context.put("errors", new String[]{ e.getMessage() });
                } finally {
                    if(ps != null){
                        try { ps.close(); } catch(Exception e){}
                    }
                    if(conn != null){
                        try { conn.close(); } catch(Exception e){}
                    }
                }

                if( ok ){
                    // show the disc list
                    resp.sendRedirect("marketing");
                    return null;
                }

            }

        } else if( action.equals("edit_tracks") ){

            int id = -1;
            try {
                id = Integer.parseInt(req.getParameter("id"));
            } catch(Exception e){}
            context.put("id", new Integer(id));

            int number = -1;
            try {
                number = Integer.parseInt(req.getParameter("track"));
            } catch(Exception e){}

            String name = req.getParameter("name");

            Connection conn = null;
            PreparedStatement stmt = null;
            ResultSet rs = null;

            PreparedStatement deleteStmt = null;

            try {

                conn = getConnection();

                if( submitted && form.equals("delete")){
                    // delete the specified track
                    deleteStmt = conn.prepareStatement("DELETE FROM track WHERE disc_id = ? AND number = ?");
                    deleteStmt.setInt(1, id);
                    deleteStmt.setInt(2, number);
                    deleteStmt.executeUpdate();
                } else if(submitted && form.equals("add")){
                    // insert the given track
                    deleteStmt = conn.prepareStatement("INSERT INTO track (disc_id, number, name) VALUES (?, ?, ?)");
                    deleteStmt.setInt(1, id);
                    deleteStmt.setInt(2, number);
                    deleteStmt.setString(3, name);
                    deleteStmt.executeUpdate();
                }

                // show the track listing
                List tracks = new ArrayList();


                stmt = conn.prepareStatement("SELECT number, name FROM track WHERE disc_id = ? ORDER BY number");
                stmt.setInt(1, id);
                rs = stmt.executeQuery();

                while( rs.next() ){
                    Track track = new Track(rs.getString("name"), rs.getInt("number"));
                    tracks.add(track);
                }

                context.put("tracks", tracks);

            } catch(Exception e){
                log.error("Database error", e);
                context.put("errors", new String[]{ "Error! " + e.getMessage() });
            } finally {
                if(deleteStmt != null){ try{deleteStmt.close();}catch(Exception e){} }
                if(rs != null){ try{rs.close();}catch(Exception e){} }
                if(stmt != null){ try{stmt.close();}catch(Exception e){} }
                if(conn != null){ try{conn.close();}catch(Exception e){} }
            }

            return "marketing_edit_tracks.vm";

        } else if( action.equals("summon") || action.equals("remove") ){

            int id = -1;
            try {
                id = Integer.parseInt(req.getParameter("id"));
            } catch(Exception e){}
            context.put("id", new Integer(id));

            boolean ok = false;

            Connection conn = null;
            PreparedStatement stmt = null;

            try {

                conn = getConnection();

                String state = action.equals("summon") ? "0" : "1";
                stmt = conn.prepareStatement("UPDATE disc SET removed = ? WHERE id = ?");
                stmt.setString(1, state);
                stmt.setInt(2, id);
                stmt.executeUpdate();

                ok = true;

            } catch(Exception e){
                log.error("Database error", e);
                context.put("errors", new String[]{ "Error! " + e.getMessage() });
            } finally {
                if(stmt != null){ try{stmt.close();}catch(Exception e){} }
                if(conn != null){ try{conn.close();}catch(Exception e){} }
            }

            if(ok){
                resp.sendRedirect("marketing");
                return null;
            }

        }

        return "marketing.vm";

    }

}
