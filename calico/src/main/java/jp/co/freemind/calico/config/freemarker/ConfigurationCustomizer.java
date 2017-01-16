package jp.co.freemind.calico.config.freemarker;

import freemarker.template.Configuration;

public interface ConfigurationCustomizer {
    void customize(Configuration configuration);
}
