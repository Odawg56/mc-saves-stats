package com.boxy.mcworldstats.model;

import org.junit.jupiter.api.Test;

public class TestUsernameCache {
    @Test
    public void testSomething() {
        String uuid = "1e8c4c71-fa03-4618-8f7d-a81d3b1d10ea";
        try {
            System.out.println(UsernameCache.LookupUsername(uuid));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @Test
    public void testAnotherThing() {
        System.out.println(System.getProperty("user.home"));
        System.out.println(System.getenv("APPDATA"));
    }
}
