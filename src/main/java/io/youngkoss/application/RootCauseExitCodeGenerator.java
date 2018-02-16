package io.youngkoss.application;

import java.io.IOException;
import java.net.ConnectException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.core.io.support.PropertiesLoaderUtils;

/**
 * @author ykoss
 *
 */
public class RootCauseExitCodeGenerator implements ExitCodeGenerator {

	/**
	 * 
	 */
	private static final String ERRORCODES_PROPERTIES = "exitcodes.properties"; //$NON-NLS-1$
	private int exitcode;
	private String message = null;

	/**
	 * 
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(RootCauseExitCodeGenerator.class);

	/**
	 * @param e
	 */
	public RootCauseExitCodeGenerator(final Throwable e) {
		if (e == null) {
			exitcode = 0;
			message = ""; //$NON-NLS-1$
			return;
		}
		if (e instanceof ConnectException) {
			exitcode = 44;
		}

		try {
			final Properties prop = PropertiesLoaderUtils.loadAllProperties(ERRORCODES_PROPERTIES);
			message = prop.getProperty(String.valueOf(exitcode));
		} catch (final IOException eio) {
			LOGGER.warn("Error reading from " + ERRORCODES_PROPERTIES + ". Is it available?", eio); //$NON-NLS-1$//$NON-NLS-2$
			message = e.getLocalizedMessage();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.boot.ExitCodeGenerator#getExitCode()
	 */
	@Override
	public int getExitCode() {
		return exitcode;
	}

	/**
	 * @return
	 */
	public String getMessage() {
		return message;
	}

}
