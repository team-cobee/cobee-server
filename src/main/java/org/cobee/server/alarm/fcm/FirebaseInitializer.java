//package org.cobee.server.alarm.fcm;
//
//import com.google.auth.oauth2.GoogleCredentials;
//import com.google.firebase.FirebaseApp;
//import com.google.firebase.FirebaseOptions;
//import jakarta.annotation.PostConstruct;
//import java.io.FileNotFoundException;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Component;
//
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.io.InputStream;
//
//@Slf4j
//@Component
//public class FirebaseInitializer {
//
//    @PostConstruct
//    public void init() throws IOException {
//        String credPath = System.getenv("GOOGLE_APPLICATION_CREDENTIALS");
//        GoogleCredentials credentials;
//
//        if (credPath != null && !credPath.isBlank()) {
//            try (InputStream is = new FileInputStream(credPath)) {
//                credentials = GoogleCredentials.fromStream(is);
//            }
//        } else {
//            try {
//                credentials = GoogleCredentials.getApplicationDefault();
//            } catch (IOException e) {
//                InputStream is = getClass().getClassLoader()
//                        .getResourceAsStream("firebase/cobee-firebase.json");
//                if (is == null) {
//                    throw new FileNotFoundException(
//                            "Firebase credentials not found. Set GOOGLE_APPLICATION_CREDENTIALS or add firebase/cobee-firebase.json to classpath."
//                    );
//                }
//                credentials = GoogleCredentials.fromStream(is);
//            }
//        }
//
//        FirebaseOptions options = FirebaseOptions.builder()
//                .setCredentials(credentials)
//                .build();
//
//        if (FirebaseApp.getApps().isEmpty()) {
//            FirebaseApp.initializeApp(options);
//        }
//    }
//}
