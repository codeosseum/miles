package com.codeosseum.miles.configuration;

import com.codeosseum.miles.util.inject.MilesModule;
import com.google.inject.Provides;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigBeanFactory;
import com.typesafe.config.ConfigFactory;

public class ConfigurationModule extends MilesModule {
    @Override
    protected void configureModule() {
        final Config configuration = ConfigFactory.load();

        bind(Config.class).toInstance(configuration);
    }

    @Provides
    public AresConfig aresConfig(final Config config) {
        return ConfigBeanFactory.create(config.getConfig("ares"), AresConfig.class);
    }

    @Provides
    public FaultSeedingConfig faultSeedingConfig(final Config config) {
        return ConfigBeanFactory.create(config.getConfig("faultSeeding"), FaultSeedingConfig.class);
    }

    @Provides
    public QuaestionesConfig quaestionesConfig(Config config) {
        return ConfigBeanFactory.create(config.getConfig("quaestiones"), QuaestionesConfig.class);
    }

    @Provides
    public SelfConfig selfConfig(Config config) {
        return ConfigBeanFactory.create(config.getConfig("self"), SelfConfig.class);
    }
}
