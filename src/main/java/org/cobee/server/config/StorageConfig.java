package org.cobee.server.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@Configuration
public class StorageConfig {

    @Value("${GOOGLE_CREDENTIALS_JSON}")
    private String credentialsJson;

    @Value("${spring.cloud.gcp.storage.project-id}")
    private String projectId;


    @Bean
    public Storage storage() throws IOException {
        InputStream credentialsStream = new ByteArrayInputStream(credentialsJson.getBytes());
        GoogleCredentials credentials = GoogleCredentials.fromStream(credentialsStream);

        return StorageOptions.newBuilder()
                .setProjectId(projectId)
                .setCredentials(credentials)
                .build()
                .getService();
    }
}
