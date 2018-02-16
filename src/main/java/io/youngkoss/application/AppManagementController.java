package io.youngkoss.application;

import java.util.concurrent.TimeoutException;

import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.youngkoss.application.db.DataSource;



/**
 * @author ykoss
 *
 */
@RestController
public class AppManagementController {
	/**
	 * 
	 */
	@Autowired
	@Qualifier("DataSource")
	private DataSource dataSource;

	/**
	 * @throws TimeoutException
	 */
	@RequestMapping("/shutdown")
	@PreDestroy
	public void shutdown() throws TimeoutException {
		dataSource.close();
	}

}
