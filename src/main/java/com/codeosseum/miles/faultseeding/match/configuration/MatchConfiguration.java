package com.codeosseum.miles.faultseeding.match.configuration;

import java.util.List;

import lombok.Value;

@Value
public class MatchConfiguration {
    private final List<String> players;
}
