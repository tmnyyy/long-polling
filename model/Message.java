package ru.cbr.ehdapo.customs.publication.longpolling.model;

import lombok.Getter;
import lombok.Setter;
import org.json.simple.JSONObject;

@Getter
@Setter
public class Message {

    private Boolean isRunning;
    private Boolean canActivate;
    private String reason;



    public Message() {
    }


    public Message(Boolean isRunning,  String reason, Boolean canActivate) {
        this.isRunning = isRunning;
        this.reason = reason;
        this.canActivate = canActivate;
    }


    public JSONObject toJSONObject() {
        JSONObject json = new JSONObject();
        json.put("isRunning", getIsRunning());
        json.put("reason", getReason());
        json.put("canActivate", getCanActivate());
        return json;
    }


}
