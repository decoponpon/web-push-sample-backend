package com.example.webpush;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.martijndwars.webpush.PushService;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.GeneralSecurityException;
import java.security.Security;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class WebPushConfig {

    @Value("${vapid.public-key}")
    private String publicKey;
    @Value("${vapid.private-key}")
    private String privateKey;

    @Bean
    public PushService pushService() throws GeneralSecurityException {
        Security.addProvider(new BouncyCastleProvider());
        return new PushService(publicKey, privateKey);
    }

    public String getPublicKey() {
        return this.publicKey;
    }
}
