package com.codeosseum.miles.challenge.repository;

import java.util.List;

public interface ChallengeRepository {
    List<String> findAllChallengesForMode(String mode);
}
