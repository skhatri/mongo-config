package org.openapps.mongoconfig.service;

import java.math.BigInteger;
import java.util.List;
import org.openapps.mongoconfig.domain.ConfigItem;
import org.openapps.mongoconfig.repository.ConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class ConfigItemServiceImpl implements ConfigItemService {

	@Autowired
    ConfigRepository configRepository;

	public long countAllConfigItems() {
        return configRepository.count();
    }

	public void deleteConfigItem(ConfigItem configItem) {
        configRepository.delete(configItem);
    }

	public ConfigItem findConfigItem(BigInteger id) {
        return configRepository.findOne(id);
    }

	public List<ConfigItem> findAllConfigItems() {
        return configRepository.findAll();
    }

	public List<ConfigItem> findConfigItemEntries(int firstResult, int maxResults) {
        return configRepository.findAll(new org.springframework.data.domain.PageRequest(firstResult / maxResults, maxResults)).getContent();
    }

	public void saveConfigItem(ConfigItem configItem) {
        configRepository.save(configItem);
    }

	public ConfigItem updateConfigItem(ConfigItem configItem) {
        return configRepository.save(configItem);
    }
}
