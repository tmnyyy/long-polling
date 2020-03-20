package ru.cbr.ehdapo.customs.publication.longpolling.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;
import ru.cbr.ehdapo.customs.publication.longpolling.dto.DeferredJSON;
import ru.cbr.ehdapo.customs.publication.longpolling.dto.Message;
import ru.cbr.ehdapo.customs.publication.longpolling.observer.Impl.Subscriber;
import ru.cbr.ehdapo.customs.publication.longpolling.resolver.Resolver;
import ru.cbr.ehdapo.customs.publication.longpolling.service.MessageService;
import ru.cbr.ehdapo.customs.publication.longpolling.service.UserStatusService;
import ru.kamatech.commons.interfaces.LdapService;


@RestController
@RequestMapping(value = "/api/v1/publication/calc-control")
@Slf4j
public class PublicationCalcControl {

    private Resolver resolver;

    private LdapService ldapService;

    private MessageService messageService;

    private UserStatusService statusService;

    @Autowired
    public PublicationCalcControl(@Qualifier("resolver") Resolver resolver, LdapService ldapService, MessageService messageService, UserStatusService statusService) {
        this.resolver = resolver;
        this.ldapService = ldapService;
        this.messageService = messageService;
        this.statusService = statusService;
    }

    @Scheduled(fixedRate = 5000)
    private void processQueues() {
        messageService.processQueues();
    }


    @RequestMapping(value = "/subscribe", method = RequestMethod.GET)
    public @ResponseBody
    DeferredJSON getNewStatus() {
        String login = ldapService.getCurrentUserData().getDisplayName();
        DeferredJSON result = new DeferredJSON(resolver);
        Subscriber subscriber = new Subscriber(login, result);
        messageService.addToClientList(subscriber);
        return result;
    }

    @RequestMapping(value = "/status", method = RequestMethod.GET)
    public ResponseEntity<Message> getCurrentStatus() throws Exception {
        String login = ldapService.getCurrentUserData().getDisplayName();
        Message message = statusService.getCurrentStatus(login);
        return ResponseEntity.ok(message);
    }

    @RequestMapping(value = "/toggle", method = RequestMethod.POST)
    public ResponseEntity<Message> toggleStatus(@RequestParam(name = "key") Boolean key) {
        String login = ldapService.getCurrentUserData().getDisplayName();
        Message message = null;

        if (key) {
            message = statusService.setStatusStop(login);
            messageService.sendMessageToClient(login);
        } else {
            message = statusService.setStatusStart(login);
            messageService.sendMessageToClient();
        }

        return ResponseEntity.ok(message);
    }
}
