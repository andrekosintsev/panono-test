package io.youngkoss.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.NestedRuntimeException;

@SpringBootApplication(scanBasePackages = { "io.youngkoss.application" })
public class ApplicationPanono {

	public static void main(final String[] args) throws Exception {

		ConfigurableApplicationContext context = null;
		try {
			context = SpringApplication.run(ApplicationPanono.class, args);
		} catch (final RuntimeException e) {
			final RootCauseExitCodeGenerator exitCodeGenerator = new RootCauseExitCodeGenerator(
					getRootCauseThrowable(e));
			if (context != null) {
				final int exit = SpringApplication.exit(context, exitCodeGenerator);
				if (exit == 0) {
					System.exit(exit);
				}
			} else {
				System.exit(0);
			}
		}

	}

	private static Throwable getRootCauseThrowable(final RuntimeException e) {
		if (e instanceof NestedRuntimeException) {
			return ((NestedRuntimeException) e).getRootCause();
		}
		return e;
	}

}