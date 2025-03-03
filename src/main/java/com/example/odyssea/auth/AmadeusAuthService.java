package com.example.odyssea.auth;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.Instant;

@Service
public class AmadeusAuthService {

    private final String TOKEN_URL = "https://test.api.amadeus.com/v1/security/oauth2/token";

    @Value("${amadeus.client_id}")
    private String clientId;

    @Value("${amadeus.client_secret}")
    private String clientSecret;

    private String accessToken;
    private Instant tokenExpiration;

    /**
     * Récupère dynamiquement un token et le stocke temporairement.
     */
    public synchronized String getAccessToken() {
        if (accessToken != null && tokenExpiration != null && Instant.now().isBefore(tokenExpiration)) {
            return accessToken;
        }

        try {
            URL url = new URL(TOKEN_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setDoOutput(true);

            String body = "grant_type=client_credentials&client_id=" + clientId + "&client_secret=" + clientSecret;
            try (OutputStream os = connection.getOutputStream()) {
                os.write(body.getBytes(StandardCharsets.UTF_8));
            }

            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new RuntimeException("Erreur lors de l'obtention du token : " + connection.getResponseCode());
            }

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonResponse = objectMapper.readTree(connection.getInputStream());

            accessToken = jsonResponse.get("access_token").asText();
            int expiresIn = jsonResponse.get("expires_in").asInt(); // Durée en secondes
            tokenExpiration = Instant.now().plusSeconds(expiresIn - 60); // Marge de sécurité de 60s

            connection.disconnect();
            return accessToken;

        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la récupération du token d'accès", e);
        }
    }
}
