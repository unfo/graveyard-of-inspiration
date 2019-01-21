package discmania.ui;

import org.apache.log.Hierarchy;
import org.apache.log.Logger;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.servlet.VelocityServlet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * Initializes database access and the Velocity system.
 *
 * @author Jari Aarniala (jari.aarniala@cs.helsinki.fi)
 * @version $Id: InitServlet.java,v 1.1 2002/12/04 13:06:31 foo Exp $
 */
public class InitServlet extends VelocityServlet {

    private Logger log = Hierarchy.getDefaultHierarchy().getLoggerFor(getClass().getName());

    /**
     * Initializes database access etc.
     */
    public void init(ServletConfig servletConfig) throws ServletException {

        log.debug("Initializing");

        // load the database driver
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            log.error("Db driver class not found!", e);
            throw new ServletException(e);
        }

        // let VelocityServlet finish the initialization
        super.init(servletConfig);

        // read our own properties from a file
        Properties p = new Properties();
        ServletContext ctx = getServletContext();
        try {
            p.load(new FileInputStream(getServletContext().getRealPath("/WEB-INF/discmania.properties")));
            // ok, put them to the context
            ctx.setAttribute("discmania_properties", p);
        } catch(Exception e){
            log.error("Couldn't load discmania.properties!", e);
        }

        log.info("Initialization done");

    }

    /**
     * Loads the velocity configuration
     */
    protected Properties loadConfiguration(ServletConfig servletConfig) throws IOException, FileNotFoundException {

        log.debug("Loading Velocity configuration");

        String path = getServletContext().getRealPath("/");
        Properties props = new Properties();
        props.setProperty( Velocity.FILE_RESOURCE_LOADER_PATH, path + "templates");
        props.setProperty( Velocity.RUNTIME_LOG, path + "velocity.log");
        return props;

    }

}
