package ru.cbr.ehdapo.customs.publication.longpolling.observer.Impl;

import org.json.simple.JSONObject;
import ru.cbr.ehdapo.customs.publication.longpolling.dto.DeferredJSON;
import ru.cbr.ehdapo.customs.publication.longpolling.observer.AbstractSubscriber;

import java.util.Objects;
import java.util.Optional;

public class Subscriber implements AbstractSubscriber {

    private DeferredJSON deferred;

    private String login;

    private boolean canActivate;

    public Subscriber() {
    }

    public Subscriber(String login, DeferredJSON deferred) {
        this.login = login;
        this.deferred = deferred;
        this.canActivate = false;
    }

    @Override
    public void deliver() {
        deferred.getResolver().setIsNewNotification(true);
    }

    @Override
    public boolean execute() {
        Optional<JSONObject> resolverResult = deferred.getResolver().resolve(login);

        if (resolverResult.isPresent()) {
            deferred.setResult(resolverResult.get());
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void update() {
        this.canActivate = true;
    }

    public String getLogin() {
        return login;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subscriber that = (Subscriber) o;
        return login.equals(that.login);
    }

    @Override
    public int hashCode() {
        return Objects.hash(login);
    }
}
