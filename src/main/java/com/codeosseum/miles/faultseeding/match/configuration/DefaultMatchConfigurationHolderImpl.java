package com.codeosseum.miles.faultseeding.match.configuration;

public class DefaultMatchConfigurationHolderImpl implements MatchConfigurationHolder {
    private MatchConfiguration matchConfiguration;

    @Override
    public MatchConfiguration get() {
        return matchConfiguration;
    }

    @Override
    public void set(final MatchConfiguration matchConfiguration) {
        this.matchConfiguration = matchConfiguration;
    }
}
