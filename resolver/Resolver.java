package ru.cbr.ehdapo.customs.publication.longpolling.resolver;

import org.json.simple.JSONObject;

import java.util.Optional;

public interface Resolver {
    Optional<JSONObject> resolve(String login);
    void setIsNewNotification(boolean isNewNotification);
}
