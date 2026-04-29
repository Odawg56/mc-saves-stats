package com.boxy.mcworldstats.model;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * This class effectively exists to prevent excessive API calls to Mojang.
 */
public class UsernameCache {
    public static File cache_source = getCacheFile();

    /**
     * Gets the userCache.json {@code File} from the app's directory in appdata, and creates the app directory
     * and/or JSON file if they don't exist
     * TODO: change fail mode to create a file in working or temp directory instead, such that this method can't fail
     * @return the JSON File to cache usernames in
     */
    private static File getCacheFile() {
        // Create path for appdata folder for this app
        String appDataPath = System.getenv("APPDATA");
        if (appDataPath == null) {
            throw new RuntimeException("APPDATA environment variable not found. This code is intended for Windows.");
        }
        // Create a subdirectory for your application
        File appDir = new File(appDataPath, "McWorldStats");
        if (!appDir.exists()) {
            if (!appDir.mkdirs()) {
                System.err.println("Failed to create directory: " + appDir.getAbsolutePath());
            }
        }
        File cacheFile = new File(appDir, "userCache.json");
        try {
            if (!cacheFile.exists()) {
                if (!cacheFile.createNewFile()) {
                    System.err.println("Failed to create file: " + cacheFile.getAbsolutePath());
                }
            }
        } catch (IOException e) {
            System.err.println("An error occurred while creating the file: " + e.getMessage());
            e.printStackTrace();
        }

        return cacheFile;
    }

    private UsernameCache() {
        throw new IllegalStateException("This is a utility class and cannot be instantiated.");
    }
    public static String LookupUsername(String raw_uuid) throws Exception {
        if (!cache_source.exists() || !cache_source.isFile()) {
            // force set if it somehow isn't present
            cache_source = getCacheFile();
        }

        // Attempt to read username, and return empty if it's not there.
        ObjectMapper mapper = new ObjectMapper();
        JsonNode cacheJson = mapper.readTree(cache_source);


        if (cacheJson.get(raw_uuid) != null) {
            // if exists, return the value
            return cacheJson.get(raw_uuid).asString("");
        } else {
            // if not in data, lookup and add to JSON
            String name = FetchNameFromAPI(raw_uuid);
            if (name.isEmpty()) {
                // TODO: get user to manually lookup the username via https://mcuuid.net/ and input into app
                System.out.println("API request failed, would need to input name manually at this stage.");
                return "NoNameBozo";
            } else {
                try {
                    Map<String, String> data = new LinkedHashMap<>();
                    data.put(raw_uuid,name);
                    mapper.writeValue(cache_source,data);

                    return name;
                } catch (Exception e) {
                    // Write to cache fails

                    System.out.println(e.getMessage());
                    throw new Exception(e.getMessage());
                }
            }

        }

    }

    /**
     * Fetch a display name from Mojang API, given a raw UUID
     * @param raw_uuid the raw UUID (including dashes)
     * @return The name on success, and an empty string on failure.
     */
    private static String FetchNameFromAPI(String raw_uuid) {
        try (HttpClient client = HttpClient.newHttpClient()) {

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.mojang.com/user/profile/"+TrimUUID(raw_uuid)))
                    .GET() // Optional, GET is default
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode root = mapper.readTree(response.body());
                String received_name = root.get("name").asString("");

                if (!received_name.isEmpty()) {
                    System.out.println("Got name from API: " + received_name);
                    return received_name;
                } else {
                    System.out.println("JSON Response not readable for some reason...");
                }
            } else if (response.statusCode() == 204) {
                System.err.println("Mojang API hitting rate limit!");
            } else {
                System.out.println("Name request returned other code:" + response.statusCode());
            }
        } catch (Exception e) {
            return "";
        }
        return "";
    }

    private static String TrimUUID(String full_uuid) {
        return full_uuid.replace("-","");
    }
}
