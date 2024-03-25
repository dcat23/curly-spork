package life.macchiato.youtube.util;

import static org.assertj.core.api.Assertions.*;

import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;


class GoogleApiUtilTest {

    @Test
    void authorize() {

            String dir = System.getProperty("user.dir");
            String credPath = dir + "/tokens/path/StoredCredential";
            String secretPath = "src/main/resources/client_secret.json";

            File credential = new File(credPath);
            File secret = new File(secretPath);

            assertThat(secret.exists()).isTrue();

        try {
            GoogleApiUtil.getYoutubeService();
            assertThat(credential.exists()).isTrue();

        } catch (GeneralSecurityException | IOException e) {
            throw new RuntimeException(e);
        }
        
    }

    @Test
    void getYoutubeService() {

        try {
            YouTube youtubeApi = GoogleApiUtil.getYoutubeService();
            SearchListResponse response = youtubeApi.search().list("snippet")
                    .setQ("macchiato")
                    .execute();
            assertThat(response.getEtag()).isNotEmpty();

        } catch (GeneralSecurityException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}