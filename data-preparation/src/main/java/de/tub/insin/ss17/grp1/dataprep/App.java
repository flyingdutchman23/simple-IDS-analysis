package de.tub.insin.ss17.grp1.dataprep;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;

import de.tub.insin.ss17.grp1.shared.SharedConstants;
import de.tub.insin.ss17.grp1.shared.SharedUtil;


/**
 * Main Class of the data-preparation section
 *
 * @author Joris Clement
 *
 */
public class App {

    private final static Logger log = LoggerFactory.getLogger(App.class);

    /**
     * Main method of the App class
     *
     * @param argv String array with all the parameters
     */
    public static void main(String[] argv) {
        log.debug("--- START ---");
        DataCliManager cliManager = new DataCliManager();
        JCommander commander = JCommander.newBuilder()
                .addObject(cliManager)
                .build();
        try {
            commander.parse(argv);
            if (SharedUtil.helpNeeded(commander)) {
                commander.usage();
            } else {
                cliManager.run();
            }
        } catch (ParameterException e) {
            log.error("Wrong input, explanation: " + System.lineSeparator()
                    + e.getMessage());
            log.info(SharedConstants.HELP_TXT);
        } catch (IllegalArgumentException e) {
            log.error("Wrong input, explanation: " + System.lineSeparator()
                    + e.getMessage());
            log.info(SharedConstants.HELP_TXT);
        }
        log.debug("--- FINISHED ---");
    }
}
