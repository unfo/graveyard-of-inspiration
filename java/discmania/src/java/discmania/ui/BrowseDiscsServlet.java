package discmania.ui;

import discmania.store.Disc;
import org.apache.velocity.context.Context;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * For browsing and searching the discs.
 * <p>
 * If the action parameter passed to the servlet equals "search",
 * then the servlet will perform a search on the given query.
 * </p>
 * <p>
 * Otherwise the servlet will just fetch a listing of the discs
 * in the database and allow the user to browse them.
 * </p>
 *
 * @author Jari Aarniala (jari.aarniala@cs.helsinki.fi)
 * @version $Id: BrowseDiscsServlet.java,v 1.1 2002/12/04 13:06:31 foo Exp $
 */
public class BrowseDiscsServlet extends BaseServlet {

    /** Size of a result page */
    private static final int PAGE_SIZE = 10;

    public String process(HttpServletRequest req,
                          HttpServletResponse resp,
                          Context context) throws Exception {

        List discs = new ArrayList();

        // for search
        String action = req.getParameter("action");
        String query = req.getParameter("query");
        String field = req.getParameter("field");
        boolean doSearch = action != null && action.equals("search");

        // paging the results
        int pageStart = -1;
        try {
            pageStart = Integer.parseInt(req.getParameter("start"));
        } catch(Exception e){}

        // db stuff
        Connection conn = null;
        Statement stmt = null;
        PreparedStatement searchStmt = null;
        ResultSet rs = null;
        try {
            conn = getConnection();

            if(doSearch){
                if(query != null && query.length() > 0){
                    /*
                        We're in seach mode and a query has been given.
                        We use a PostgreSQL -specific search syntax
                        here, that allows for regexp searches (note the
                        " ~* 'something' " -syntax in the queries instead
                        of LIKE '%blaah%')
                    */
                    if(field != null && field.equals("artist")){
                        // search by artist
                        searchStmt = conn.prepareStatement(
                            "SELECT * FROM disc WHERE removed != '1' AND artist ~* ? ORDER BY artist, name"
                        );
                    } else {
                        // search by album name
                        searchStmt = conn.prepareStatement(
                            "SELECT * FROM disc WHERE removed != '1' AND name ~* ? ORDER BY artist, name"
                        );
                    }
                    searchStmt.setString(1, query);
                    rs = searchStmt.executeQuery();
                }
            } else {
                // if not searching, just select discs.
                stmt = conn.createStatement();
                rs = stmt.executeQuery("SELECT * FROM disc WHERE removed != '1' ORDER BY artist, name");
            }

            if( rs != null ){
                // total number of results
                int results = 0;
                // results on this page
                int pageResults = 0;
                // if there is a start index specified, get only the results after it
                if( pageStart > 0 ){
                    for( int skip = 0; skip < pageStart && rs.next(); skip++ ){
                        results++;
                    }
                }
                // get results
                while( rs.next() ){
                    results++;
                    if( pageResults >= PAGE_SIZE ){
                        // don't need to fetch the rest of the results
                        continue;
                    }
                    pageResults++;
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

                // a list containing the result pages
                List pages = new ArrayList();
                for(int page = 1, curr = 0; curr < results; curr += PAGE_SIZE, page++){
                    pages.add(new ResultPage(page, curr, curr >= pageStart && curr < pageStart + PAGE_SIZE));
                }
                context.put("pages", pages);
                context.put("results", new Integer(results));
            }

        } catch(Exception e){
            log.error("Database error", e);
            context.put("errors", new String[]{ "Error! " + e.getMessage() });
        } finally {
            if(rs != null){ try{rs.close();}catch(Exception e){} }
            if(stmt != null){ try{stmt.close();}catch(Exception e){} }
            if(searchStmt != null){ try{searchStmt.close();}catch(Exception e){} }
            if(conn != null){ try{conn.close();}catch(Exception e){} }
        }

        // set the correct values to the context
        context.put("discs", discs);
        context.put("doSearch", new Boolean(doSearch));
        context.put("query", query);
        context.put("field", field);

        return "discs.vm";

    }

}
