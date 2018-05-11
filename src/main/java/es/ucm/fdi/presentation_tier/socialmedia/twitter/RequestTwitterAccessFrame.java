package es.ucm.fdi.presentation_tier.socialmedia.twitter;

import java.awt.Dimension;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.RequestToken;
import twitter4j.conf.ConfigurationBuilder;

/**
 * A frame with a browser integrated to allow the application access to the
 * twitter user account. The frame creates a JFXPanel with a WebEngine. The
 * browser uses these components to load an authentication webpage for the user
 * to allow access to the application to log-in.
 *
 * @author Arturo Acuaviva, Inmaculada Pérez, Javier Galiana
 */
public class RequestTwitterAccessFrame extends JFrame {

    /**
     * Non-visual object capable of managing one Web page at a time.
     */
    private WebEngine webEngine;
    /**
     * Component to embed JavaFX content into Swing applications.
     */
    private JFXPanel jfxPanel = new JFXPanel();
    /**
     * A builder that can be used to construct a twitter4j configuration with
     * desired settings.
     */
    private ConfigurationBuilder cb = new ConfigurationBuilder();
    /**
     * A factory class for Twitter.
     */
    private TwitterFactory tf;
    /**
     * Instance of an implemented interface containing the current twitter
     * session.
     */
    private Twitter twitter;
    /**
     * RequestToken containing the request token url for given keys.
     */
    private RequestToken requestToken;
    /**
     * The consumer key identifies which application is making the twitter
     * request.
     */
    private String consumerKey;
    /**
     * API password that is used to authenticate with the authentication server
     * that authenticates the API.
     */
    private String consumerSecret;

    /**
     * Class constructor which creates a frame to log-in in twitter and allow
     * access to the application.
     *
     * @param consumerKey
     * @param consumerSecret
     */
    public RequestTwitterAccessFrame(String consumerKey, String consumerSecret) {
        this.consumerKey = consumerKey;
        this.consumerSecret = consumerSecret;
        initGUI();
        try {
            twitterAccessRequest();
            loadURL(requestToken.getAuthenticationURL());
        } catch (TwitterException ex) {
            JOptionPane.showMessageDialog(this, "Error while twitter requesting",
                    ex.getMessage(), JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }

    /**
     * Loads an url in the webEngine so the webbrowser is updated to the new
     * url.
     *
     * @param url
     */
    private void loadURL(final String url) {
        Platform.runLater(() -> {
            webEngine.load(url);
        });
    }

    /**
     * Initialize the graphics interface creating the JFXPanel and setting up
     * the web engine.
     */
    private void initGUI() {
        // Windows configuration
        setVisible(true);
        setPreferredSize(new Dimension(500, 500));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(jfxPanel);
        pack();
        // Configurates jfxPanel as a webbrowser
        Platform.runLater(() -> {
            WebView view = new WebView();
            webEngine = view.getEngine();
            jfxPanel.setScene(new Scene(view));
        });

    }

    /**
     * Request access and save the request in requestToken. Firstly it sets up
     * the configuration according to the given consumerKey and consumerSecret.
     * Then it gets an instance of a twitter session building the configuration
     * and make a request using the session.
     *
     * @throws TwitterException when access it is not allowed
     */
    private void twitterAccessRequest() throws TwitterException {
        //the following is set without accesstoken (desktop client)
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey(consumerKey)
                .setOAuthConsumerSecret(consumerSecret);
        tf = new TwitterFactory(cb.build());
        twitter = tf.getInstance();
        // get request token
        // this will throw IllegalStateException if access token is already available
        requestToken = twitter.getOAuthRequestToken();
    }

    /**
     * Twitter getter
     *
     * @return twitter session
     */
    public Twitter getTwitterSession() {
        return twitter;
    }

    /**
     * Request token getter
     *
     * @return request token
     */
    public RequestToken getRequestToken() {
        return requestToken;
    }
}
