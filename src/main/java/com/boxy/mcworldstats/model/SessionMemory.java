package com.boxy.mcworldstats.model;

import java.util.ArrayList;

public class SessionMemory {
    private static ArrayList<Player> players = new ArrayList<>();

    public static ArrayList<Player> getPlayers() {
        return players;
    }

    public static void setPlayers(ArrayList<Player> players) {
        SessionMemory.players = players;
    }
}
