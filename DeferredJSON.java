package ru.cbr.ehdapo.customs.publication.longpolling;

import org.json.simple.JSONObject;
import org.springframework.web.context.request.async.DeferredResult;
import ru.cbr.ehdapo.customs.publication.longpolling.resolver.Resolver;

import java.util.Optional;

public class DeferredJSON extends DeferredResult<JSONObject> {

    private Resolver resolver;

    public DeferredJSON(Resolver resolver) {
        this.resolver = resolver;
    }

//    public boolean execute() {
//        Optional<JSONObject> resolverResult = resolver.resolve();
//
//        if (resolverResult.isPresent()) {
//            this.setResult(resolverResult.get());
//            return true;
//        } else {
//            return false;
//        }
//    }

    public Resolver getResolver() {
        return resolver;
    }
}
