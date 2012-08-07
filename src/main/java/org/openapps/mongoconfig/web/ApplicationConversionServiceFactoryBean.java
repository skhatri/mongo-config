package org.openapps.mongoconfig.web;

import java.math.BigInteger;
import org.openapps.mongoconfig.domain.ConfigItem;
import org.openapps.mongoconfig.service.ConfigItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.support.FormattingConversionServiceFactoryBean;
import org.springframework.roo.addon.web.mvc.controller.converter.RooConversionService;

@Configurable
/**
 * A central place to register application converters and formatters. 
 */
@RooConversionService
public class ApplicationConversionServiceFactoryBean extends FormattingConversionServiceFactoryBean {

	@Override
	protected void installFormatters(FormatterRegistry registry) {
		super.installFormatters(registry);
		// Register application converters and formatters
	}

	@Autowired
    ConfigItemService configItemService;

	public Converter<ConfigItem, String> getConfigItemToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<org.openapps.mongoconfig.domain.ConfigItem, java.lang.String>() {
            public String convert(ConfigItem configItem) {
                return new StringBuilder().append(configItem.getName()).append(' ').append(configItem.getDescription()).toString();
            }
        };
    }

	public Converter<BigInteger, ConfigItem> getIdToConfigItemConverter() {
        return new org.springframework.core.convert.converter.Converter<java.math.BigInteger, org.openapps.mongoconfig.domain.ConfigItem>() {
            public org.openapps.mongoconfig.domain.ConfigItem convert(java.math.BigInteger id) {
                return configItemService.findConfigItem(id);
            }
        };
    }

	public Converter<String, ConfigItem> getStringToConfigItemConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, org.openapps.mongoconfig.domain.ConfigItem>() {
            public org.openapps.mongoconfig.domain.ConfigItem convert(String id) {
                return getObject().convert(getObject().convert(id, BigInteger.class), ConfigItem.class);
            }
        };
    }

	public void installLabelConverters(FormatterRegistry registry) {
        registry.addConverter(getConfigItemToStringConverter());
        registry.addConverter(getIdToConfigItemConverter());
        registry.addConverter(getStringToConfigItemConverter());
    }

	public void afterPropertiesSet() {
        super.afterPropertiesSet();
        installLabelConverters(getObject());
    }
}
