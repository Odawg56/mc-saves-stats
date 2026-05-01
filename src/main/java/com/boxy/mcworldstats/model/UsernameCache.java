package com.boxy.mcworldstats.model;

import com.boxy.mcworldstats.util.AppDataAccessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.node.ObjectNode;

import java.io.File;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

/**
 * This class effectively exists to prevent excessive API calls to Mojang.
 */
public class UsernameCache extends AppDataAccessor {
    private static final Logger logger = LoggerFactory.getLogger(UsernameCache.class);

    private static File cache_source = getCacheFile();


    /**
     * Gets the userCache.json {@code File} from the app's directory in appdata, and creates the app directory
     * and/or JSON file if they don't exist
     * TODO: change fail mode to create a file in working or temp directory instead, such that this method can't fail
     * @return the JSON File to cache usernames in
     */
    private static File getCacheFile() {
        return getAppDataFile("userCache.json");
    }

    private UsernameCache() {
        throw new IllegalStateException("This is a utility class and cannot be instantiated.");
    }


    public static String LookupUsername(String raw_uuid) throws Exception {
        if (!cache_source.exists() || !cache_source.isFile()) {
            // force set if it somehow isn't present
            cache_source = getCacheFile();
        }


        ObjectMapper mapper = new ObjectMapper();
        JsonNode cacheJson = mapper.readTree(cache_source);
        if (!(cacheJson instanceof ObjectNode)) {
            throw new IllegalArgumentException("Root JSON is not an object");
        }

        // Attempt to read username, and return empty if it's not there.
        if (cacheJson.get(raw_uuid) != null) {
            // if exists, return the value
            return cacheJson.get(raw_uuid).asString("");
        } else {
            // if not in data, lookup and add to JSON
            String name = FetchNameFromAPI(raw_uuid);
            if (name.isEmpty()) {
                logger.warn("API request failed, would need to input name manually at this stage.");
                Scanner nameScan = new Scanner(System.in);
                System.out.println("Manually lookup UUID "+raw_uuid+" at https://mcuuid.net/: ");
                return nameScan.nextLine();
            } else {
                try {
                    ObjectNode cacheObject = (ObjectNode) cacheJson;
                    cacheObject.put(raw_uuid,name);
                    mapper.writerWithDefaultPrettyPrinter().writeValue(cache_source,cacheObject);

                    return name;
                } catch (Exception e) {
                    // Write to cache fails
                    logger.error("Writing to username cache failed: {}",e.getMessage());
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
                    logger.info("Got name from API: {}", received_name);
                    return received_name;
                } else {
                    logger.warn("Mojang JSON Response not readable for some reason...");
                }
            } else if (response.statusCode() == 204) {
                logger.warn("Mojang API hitting rate limit!");
            } else {
                logger.warn("Name request returned other code:" + response.statusCode());
            }
        } catch (Exception e) {
            return "";
        }
        return "";
    }

    /**
     * Trims the given UUID, removing all the '-' characters.
     * @param full_uuid the raw UUID, including '-' characters
     * @return the trimmed UUID, suitable for passing to the Mojang API.
     */
    private static String TrimUUID(String full_uuid) {
        return full_uuid.replace("-","");
    }
}
