package com.boxy.mcworldstats.model;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Player {
    private String uuid = "";
    private String display_name = "";
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

    public void setUUID(String uuid) {
        this.uuid = uuid;
    }

    public String getDisplayName() {
        return display_name;
    }

    public double getTotalHrs() {
        return total_hrs;
    }

    public void setTotalHrs(double total_hrs) {
        this.total_hrs = total_hrs;
    }

    public int getTotalDeaths() {
        return total_deaths;
    }

    public void setTotalDeaths(int total_deaths) {
        this.total_deaths = total_deaths;
    }

    public int getTotalPlayerKills() {
        return total_player_kills;
    }

    public void setTotalPlayerKills(int total_player_kills) {
        this.total_player_kills = total_player_kills;
    }


}
