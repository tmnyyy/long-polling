package ru.cbr.ehdapo.customs.publication.longpolling.observer;

public interface AbstractSubscriber {

    void deliver();

    boolean execute();

    void update();

    String getLogin();
}
