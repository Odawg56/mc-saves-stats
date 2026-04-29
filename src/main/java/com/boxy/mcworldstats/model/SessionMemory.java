package com.boxy.mcworldstats.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Static class for storing data to be persistent across a session of application use.
 */
public class SessionMemory {

    private SessionMemory(){}

    /**
     * A map of players detected, which will all have their data summed for player summaries, if requested.
     */
    private static Map<String,Player> players = new HashMap<>();

    /**
     * Get a specific player from the Session Memory. Returns a new player with the given UUID if it doesn't exist.
     * @param raw_uuid
     * @return
     */
    public static Player getPlayer(String raw_uuid) {
        if (players.containsKey(raw_uuid)) {
            return players.get(raw_uuid);
        } else {
            return new Player(raw_uuid);
        }
    }

    public static void setPlayer(Player new_player) {
        players.put(new_player.getUUID(), new_player);
    }
}
