package org.openapps.mongoconfig.repository;

import static org.springframework.data.mongodb.core.query.Criteria.where;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openapps.mongoconfig.domain.ConfigItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"/META-INF/spring/applicationContext-mongo.xml",
		"/META-INF/spring/applicationContext.xml" })
public class ConfigItemRepositoryIntegrationTest {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(ConfigItemRepositoryIntegrationTest.class);

	@Autowired
	private MongoTemplate mongoTemplate;

	@Test
	public void saveConfigItemToDb() {
		mongoTemplate.dropCollection(ConfigItem.class);

		ConfigItem configItem = new ConfigItem();
		configItem.setName("server xml");
		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put("port", 8080);
		properties.put("loglevel", "INFO");
		configItem.setProperties(properties);
		configItem.setDescription("Tomcat Configuration Server xml for SIT");
		mongoTemplate.save(configItem);

		printAllConfigItems();
		
		Query query = Query.query(where("name").is("server xml"));
		query.addCriteria(where("properties.port").gt(8000));
		
		List<ConfigItem> result = mongoTemplate.find(query, ConfigItem.class);
		ConfigItem foundItem = result.get(0);
		Assert.assertEquals(8080, foundItem.getProperties().get("port"));
	}

	private void printAllConfigItems() {
		List<ConfigItem> allConfigs = mongoTemplate.findAll(ConfigItem.class);
		for (ConfigItem config : allConfigs) {
			LOGGER.info("config item Id: {}, name: {}", config.getId(),
					config.getName());
		}
	}
}
