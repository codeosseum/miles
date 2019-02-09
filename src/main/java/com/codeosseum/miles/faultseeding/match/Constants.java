package com.codeosseum.miles.faultseeding.match;

public final class Constants {
    public static final String MODE = "fault-seeding";

    public final class Stage {
        public static final String WAITING_FOR_PLAYERS = "waiting-for-players";

        public static final String MATCH_STARTING = "match-starting";

        public static final String IN_PROGRESS = "in-progress";

        public static final String CLEANUP = "clean-up";

        private Stage() {

        }
    }

    private Constants() {

    }
}
