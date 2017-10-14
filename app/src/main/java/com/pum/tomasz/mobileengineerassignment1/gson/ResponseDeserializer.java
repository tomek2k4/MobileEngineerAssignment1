package com.pum.tomasz.mobileengineerassignment1.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.pum.tomasz.mobileengineerassignment1.activity.RepositoriesActivity;
import com.pum.tomasz.mobileengineerassignment1.model.GithubSearchResultWrapper;
import com.pum.tomasz.mobileengineerassignment1.model.RepositoryItem;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ResponseDeserializer implements JsonDeserializer<GithubSearchResultWrapper> {

    private Type repositoryItemType = new TypeToken<RepositoryItem>() {
    }.getType();

    private Gson gson = new GsonBuilder()
            .setFieldNamingPolicy(RepositoriesActivity.API_JSON_NAMING_POLICY)
            .create();

    @Override
    public GithubSearchResultWrapper deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {

        GithubSearchResultWrapper responseWrapper = null;
        JsonObject jsonObject = json.getAsJsonObject();
        if (jsonObject.has(RepositoryItem.JSON_ARRAY_NAME)) {
            JsonArray jsonArray = jsonObject.getAsJsonArray(RepositoryItem.JSON_ARRAY_NAME);
            int size = jsonArray.size();
            List<RepositoryItem> repos = new ArrayList<>(size);
            JsonElement jsonElement;
            for (int i = 0; i < size; i++) {
                jsonElement = jsonArray.get(i);
                RepositoryItem repositoryItem = parseRepositoryItem(jsonElement);
                repos.add(repositoryItem);
            }
            responseWrapper = new GithubSearchResultWrapper<List<RepositoryItem>>();
            responseWrapper.body = repos;
        }

        return responseWrapper;
    }

    private RepositoryItem parseRepositoryItem(JsonElement jsonElement) {
        return gson.fromJson(jsonElement, repositoryItemType);
    }
}
