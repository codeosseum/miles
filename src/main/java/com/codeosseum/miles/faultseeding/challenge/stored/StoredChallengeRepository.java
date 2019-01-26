package com.codeosseum.miles.faultseeding.challenge.stored;

import java.util.List;

public interface StoredChallengeRepository {
    List<Challenge> loadAllStoredChallenges();
}
