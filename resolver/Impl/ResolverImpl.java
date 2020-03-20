package ru.cbr.ehdapo.customs.publication.longpolling.resolver.Impl;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.cbr.ehdapo.customs.publication.longpolling.dto.Message;
import ru.cbr.ehdapo.customs.publication.longpolling.repository.UserStatusRepository;
import ru.cbr.ehdapo.customs.publication.longpolling.resolver.Resolver;

import java.util.Optional;

@Component("resolver")
public class ResolverImpl implements Resolver {

    private UserStatusRepository repository;

    private boolean isNewNotification = false;


    @Autowired
    public ResolverImpl(UserStatusRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<JSONObject> resolve(String login) {
        if (!isNewNotification) {
            return Optional.empty();
        } else {
            try {
                Message message = repository.getCurrentStatus(login);
                ResolverImpl notificationResolver = this;

                new java.util.Timer().schedule(
                        new java.util.TimerTask() {
                            @Override
                            public void run() {
                                notificationResolver.setIsNewNotification(false);
                            }
                        }, 5000
                );

                return Optional.of(message.toJSONObject());
            } catch (Exception e) {
                e.printStackTrace();
                return Optional.empty();
            }
        }
    }

    @Override
    public void setIsNewNotification(boolean isNewNotification) {
        this.isNewNotification = isNewNotification;
    }


}
