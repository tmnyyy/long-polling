package ru.cbr.ehdapo.customs.publication.longpolling.observer;

public interface AbstractPublisher {

    void addToClientList(AbstractSubscriber subscriber);

    void deleteFromClientList(AbstractSubscriber subscriber);

    void sendMessageToClient() throws InterruptedException;

}
