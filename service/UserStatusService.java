package ru.cbr.ehdapo.customs.publication.longpolling.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.cbr.ehdapo.customs.publication.longpolling.dto.Message;
import ru.cbr.ehdapo.customs.publication.longpolling.repository.UserStatusRepository;

@Service
public class UserStatusService {

    private UserStatusRepository repository;

    private MessageService messageService;

    @Autowired
    public UserStatusService(UserStatusRepository repository, MessageService messageService) {
        this.repository = repository;
        this.messageService = messageService;
    }

    public Message getCurrentStatus(String login) throws Exception {
        return repository.getCurrentStatus(login);
    }

    public Message setStatusStop(String login) {
        Message message = repository.setStopCalcFlgOn(login);
        return message;
    }

    public Message setStatusStart(String login) {
        return repository.setStopCalcFlgOff(login);
    }

}
