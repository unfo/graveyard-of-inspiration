package discmania.ui;

import discmania.store.User;
import org.apache.log.Hierarchy;
import org.apache.log.Logger;
import org.apache.velocity.Template;
import org.apache.velocity.context.Context;
import org.apache.velocity.servlet.VelocityServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

/**
 * A base class that all the other servlets extend.
 * The class itself extends VelocityServlet.
 *
 * @see org.apache.velocity.servlet.VelocityServlet
 * @author Jari Aarniala (jari.aarniala@cs.helsinki.fi)
 * @version $Id: BaseServlet.java,v 1.1 2002/12/04 13:06:31 foo Exp $
 */
public abstract class BaseServlet extends VelocityServlet {

    // cached database properties
    private String m_databaseUrl;
    private String m_databaseUser;
    private String m_databasePassword;

    // logging
    protected Logger log = Hierarchy.getDefaultHierarchy().getLoggerFor(getClass().getName());

    /**
     * Overrides the corresponding method in VelocityServlet.
     * Handles a http request.
     */
    protected Template handleRequest(HttpServletRequest req,
                                     HttpServletResponse resp,
                                     Context context)
            throws Exception
    {

        // get a session. if no session, new one will be created
        HttpSession session = req.getSession();

        // if this is a logged-on User, set the context parameter
        if( !session.isNew() ){
            User user = (User)session.getAttribute("user");
            if( user != null ){
                context.put("user", user);
            }
        }

        // do the processing
        String result = null;
        try {
            // process is the abstract method all the
            // instances of BaseServlet must implement.
            result = process(req, resp, context);
        } catch(Exception e){
            log.error("Error in " + getClass().getName() + ".process()", e);
            throw new ServletException(e);
        }

        // a null result means that we should stop here.
        // a redirect etc. has most likely already been issued.
        if( result == null ){
            return null;
        }

        // set the template that will format the content
        context.put("content_template", result);

        // return the base template
        Template template = null;

        try {
            template = getTemplate("base.vm");
        } catch (Exception e) {
            log.error("Exception in getting the template", e);
            throw e;
        }

        return template;

    }

    /**
     * Returns a connection to the database.
     *
     * @return a connection to the db
     * @throws Exception if something fails
     */
    protected Connection getConnection() throws Exception {
        if( m_databaseUrl == null ){
            Properties p = (Properties)getServletContext().getAttribute("discmania_properties");
            m_databaseUrl = p.getProperty("database_url");
            m_databaseUser = p.getProperty("database_user");
            m_databasePassword = p.getProperty("database_password");
        }
        return DriverManager.getConnection(
                m_databaseUrl, m_databaseUser, m_databasePassword
        );
    }

    /**
     * Shows the login page. The user will be redirected to
     * <code>page</code> after succesful login.
     *
     * @param resp
     * @param page
     */
    protected void showLoginPage(HttpServletResponse resp, String page) throws IOException {
        resp.sendRedirect("login?page=" + page);
    }

    /**
     * All subclasses of this servlet must implement their logic in this method
     * and return the name of the template that should be shown.
     *
     * @param req        the http request
     * @param resp       the http response
     * @param context    the velocity context
     * @return           the name of the template to render, i.e. "disc.vm"
     * @throws Exception if something goes wrong
     */
    public abstract String process( HttpServletRequest req,
                                    HttpServletResponse resp,
                                    Context context) throws Exception;

}
