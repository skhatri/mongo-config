package org.openapps.mongoconfig.repository;

import java.math.BigInteger;
import java.util.List;
import org.openapps.mongoconfig.domain.ConfigItem;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.roo.addon.layers.repository.mongo.RooMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
@RooMongoRepository(domainType = ConfigItem.class)
public interface ConfigRepository extends PagingAndSortingRepository<ConfigItem, BigInteger> {

    List<org.openapps.mongoconfig.domain.ConfigItem> findAll();
}
