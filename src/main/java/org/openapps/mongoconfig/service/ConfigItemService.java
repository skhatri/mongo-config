package org.openapps.mongoconfig.service;

import java.math.BigInteger;
import java.util.List;
import org.openapps.mongoconfig.domain.ConfigItem;
import org.springframework.roo.addon.layers.service.RooService;

@RooService(domainTypes = { org.openapps.mongoconfig.domain.ConfigItem.class })
public interface ConfigItemService {

	public abstract long countAllConfigItems();


	public abstract void deleteConfigItem(ConfigItem configItem);


	public abstract ConfigItem findConfigItem(BigInteger id);


	public abstract List<ConfigItem> findAllConfigItems();


	public abstract List<ConfigItem> findConfigItemEntries(int firstResult, int maxResults);


	public abstract void saveConfigItem(ConfigItem configItem);


	public abstract ConfigItem updateConfigItem(ConfigItem configItem);

}
