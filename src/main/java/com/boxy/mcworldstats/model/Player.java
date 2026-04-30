package com.boxy.mcworldstats.model;

public class Player {
    private String uuid = null;
    private String display_name = null;
    private double total_hrs = 0.0;
    private int total_deaths = 0;
    private int total_player_kills = 0;

    public Player() {}

    public Player(String raw_uuid) {
        this.uuid = raw_uuid;
    }

    public String getUUID() {
        return uuid;
    }

    @Deprecated
    public void setUUID(String uuid) {
        this.uuid = uuid;
    }

    public String getDisplayName() {
        return display_name;
    }

    public void ensureDisplayName() {
        if (display_name == null) {
            try {
                display_name = UsernameCache.LookupUsername(this.uuid);
            } catch (Exception e) {
                // This should very rarely happen.
                display_name = this.uuid;
            }
        }
    }

    public double getTotalHrs() {
        return total_hrs;
    }

    public void incrementTotalHrs(double hours) {
        this.total_hrs += hours;
    }

    public int getTotalDeaths() {
        return total_deaths;
    }

    public void incrementDeaths(int deaths) {
        this.total_deaths += total_deaths;
    }

    public int getTotalPlayerKills() {
        return total_player_kills;
    }

    public void incrementPlayerKills(int player_kills) {
        this.total_player_kills += player_kills;
    }


}
