package com.ctrip.apollo.client.demo;

import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.ConfigService;
import com.ctrip.framework.apollo.model.ConfigChange;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("index")
public class IndexController {
    Config config;

    @Value("${timeout}")
    private String timeout;

    @GetMapping("get/timeout")
    public String getTimeout() {
        return timeout;
    }

    public IndexController() {
        config = ConfigService.getAppConfig();
        config.addChangeListener(configChangeEvent -> {
            log.info("Changes for namespace {}", configChangeEvent.getNamespace());
            for (String key: configChangeEvent.changedKeys()) {
                ConfigChange change = configChangeEvent.getChange(key);
                log.info("Change - key: {}, oldValue: {}, newValue: {}, changeType: {}", change.getPropertyName(),
                        change.getOldValue(), change.getNewValue(), change.getChangeType());
                if (key.equals("timeout")) {
                    timeout = change.getNewValue();
                }
            }
        });
    }
}
