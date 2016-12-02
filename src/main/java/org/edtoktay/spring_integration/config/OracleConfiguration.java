/**
 * 
 */
package org.edtoktay.spring_integration.config;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.edtoktay.spring_integration.props.OracleProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import oracle.jdbc.pool.OracleDataSource;

/**
 * @author deniz.toktay
 *
 */
@Configuration
@ConditionalOnExpression("${oracle.active:true}")
public class OracleConfiguration {
	private static final Logger log = LoggerFactory.getLogger(OracleConfiguration.class);
	@Autowired
	OracleProperties oracleProperties;
	@Bean
	DataSource dataSource() throws SQLException{
		log.info("Data Source Bean Creation Initialized");
		OracleDataSource dataSource = new OracleDataSource();
		dataSource.setUser(oracleProperties.getUsername());
		dataSource.setPassword(oracleProperties.getPassword());
		dataSource.setURL(oracleProperties.getUrl());
		dataSource.setImplicitCachingEnabled(true);
		dataSource.setFastConnectionFailoverEnabled(true);
		return dataSource;
	}
}
