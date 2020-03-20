package ru.cbr.ehdapo.customs.publication.longpolling.service;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.cbr.ehdapo.customs.publication.longpolling.dto.Message;
import ru.cbr.ehdapo.customs.publication.longpolling.observer.AbstractPublisher;
import ru.cbr.ehdapo.customs.publication.longpolling.observer.AbstractSubscriber;
import ru.cbr.ehdapo.customs.publication.longpolling.repository.UserStatusRepository;

import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Service
public class MessageService implements AbstractPublisher {

    private final Queue<AbstractSubscriber> subscriberList = new ConcurrentLinkedQueue<>();

    public MessageService() {
    }

    @Override
    public void addToClientList(AbstractSubscriber subscriber) {
        if (subscriberList.contains(subscriber)) {
            subscriberList.remove(subscriber);
        }
        subscriberList.add(subscriber);
    }

    @Override
    public void deleteFromClientList(AbstractSubscriber subscriber) {
        subscriberList.remove(subscriber);
    }

    @Override
    public  void sendMessageToClient() {
        for (AbstractSubscriber subscriber : subscriberList) {
            subscriber.deliver();
        }

    }

    public void sendMessageToClient(String login)  {
            for (AbstractSubscriber subscriber : subscriberList) {
                if(subscriber.getLogin().equals(login)) {
                    subscriber.update();
                }
            }
        sendMessageToClient();
    }




    public void processQueues() {
        for (AbstractSubscriber cmd : subscriberList) {
            if(cmd.execute()) {
                subscriberList.remove(cmd);
            }
        }
    }


}


