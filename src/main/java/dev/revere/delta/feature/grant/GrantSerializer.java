package dev.revere.delta.feature.grant;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Emmy
 * @project Delta
 * @date 25/06/2024 - 21:38
 */
@UtilityClass
public class GrantSerializer {

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static List<String> serialize(List<Grant> grants) {
        if (grants == null || grants.isEmpty()) {
            return Collections.emptyList();
        }

        List<String> serializedGrants = new ArrayList<>();
        for (Grant grant : grants) {
            serializedGrants.add(serialize(grant));
        }

        return serializedGrants;
    }

    public static List<Grant> deserialize(List<String> grants) {
        if (grants == null || grants.isEmpty()) {
            return Collections.emptyList();
        }

        List<Grant> deserializedGrants = new ArrayList<>();
        for (String grant : grants) {
            deserializedGrants.add(deserialize(grant));
        }

        return deserializedGrants;
    }

    private String serialize(Grant grant) {
        return gson.toJson(grant);
    }

    private Grant deserialize(String json) {
        return gson.fromJson(json, Grant.class);
    }
}