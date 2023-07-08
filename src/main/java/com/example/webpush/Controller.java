package com.example.webpush;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import nl.martijndwars.webpush.Notification;
import nl.martijndwars.webpush.PushService;
import nl.martijndwars.webpush.Subscription;
import org.jose4j.lang.JoseException;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Slf4j
@RestController
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class Controller {

    private final WebPushConfig webPushConfig;
    private final PushService pushService;


    @GetMapping("/public-key")
    public Map<String, String> publicKey() {
        var response = new HashMap<String, String>();
        response.put("publicKey", webPushConfig.getPublicKey());
        return response;
    }

    @PostMapping("/web-push")
    public void webPush(@RequestBody Subscription subscription) {
        var message = new Message("タイトルだよー",
                """
                        ボディだよー
                        ボディだよー
                        ボディだよー""");
        try {
            pushService.send(new Notification(subscription, message.getJSONString()));
        } catch (InterruptedException e) {
            log.error("InterruptedException: ", e);
            Thread.currentThread().interrupt();
        } catch (
                GeneralSecurityException
                | IOException
                | JoseException
                | ExecutionException e
        ) {
            log.error("errorだよ", e);
        }
    }

    @Value
    private static class Message {
        String title;
        String body;

        String getJSONString() throws JsonProcessingException {
            var objectMapper = new ObjectMapper();
            try {
                return objectMapper.writeValueAsString(this);
            } catch (JsonProcessingException e) {
                throw e;
            }
        }
    }
}
