package org.openapps.mongoconfig.web;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.openapps.mongoconfig.domain.ConfigItem;
import org.openapps.mongoconfig.service.ConfigItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

@RequestMapping("/configitems")
@Controller
@RooWebScaffold(path = "configitems", formBackingObject = ConfigItem.class)
public class ConfigItemController {

	@Autowired
    ConfigItemService configItemService;

	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid ConfigItem configItem, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, configItem);
            return "configitems/create";
        }
        uiModel.asMap().clear();
        configItemService.saveConfigItem(configItem);
        return "redirect:/configitems/" + encodeUrlPathSegment(configItem.getId().toString(), httpServletRequest);
    }

	@RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel) {
        populateEditForm(uiModel, new ConfigItem());
        return "configitems/create";
    }

	@RequestMapping(value = "/{id}", produces = "text/html")
    public String show(@PathVariable("id") BigInteger id, Model uiModel) {
        uiModel.addAttribute("configitem", configItemService.findConfigItem(id));
        uiModel.addAttribute("itemId", id);
        return "configitems/show";
    }

	@RequestMapping(produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("configitems", configItemService.findConfigItemEntries(firstResult, sizeNo));
            float nrOfPages = (float) configItemService.countAllConfigItems() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("configitems", configItemService.findAllConfigItems());
        }
        return "configitems/list";
    }

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid ConfigItem configItem, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, configItem);
            return "configitems/update";
        }
        uiModel.asMap().clear();
        configItemService.updateConfigItem(configItem);
        return "redirect:/configitems/" + encodeUrlPathSegment(configItem.getId().toString(), httpServletRequest);
    }

	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("id") BigInteger id, Model uiModel) {
        populateEditForm(uiModel, configItemService.findConfigItem(id));
        return "configitems/update";
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("id") BigInteger id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        ConfigItem configItem = configItemService.findConfigItem(id);
        configItemService.deleteConfigItem(configItem);
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/configitems";
    }

	void populateEditForm(Model uiModel, ConfigItem configItem) {
        uiModel.addAttribute("configItem", configItem);
    }

	String encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
        String enc = httpServletRequest.getCharacterEncoding();
        if (enc == null) {
            enc = WebUtils.DEFAULT_CHARACTER_ENCODING;
        }
        try {
            pathSegment = UriUtils.encodePathSegment(pathSegment, enc);
        } catch (UnsupportedEncodingException uee) {}
        return pathSegment;
    }
}
