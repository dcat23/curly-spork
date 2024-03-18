package life.macchiato.courses.util;

import life.macchiato.courses.util.TransCli.Callback;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.assertj.core.api.Assertions.*;


@Slf4j
class TransCliTest {

    private static final String torrentFile = "https://get.freecoursesonline.me/wp-content/uploads/2024/02/freecoursesonline.me-linkedin-full-stack-web-applications-with-rust-and-leptos.torrent";

    private TransCli cli;
    private Callback callback;

    @BeforeEach
    void setUp() {
        cli = new TransCli(torrentFile);
        callback = new Callback("Linkedin-Rust-Leptos");
    }

    @AfterEach
    void tearDown() {
        callback = null;
        cli = null;
    }

    @Test @Disabled
    void shouldCalculateElapsed() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        String elapsed = callback.getElapsed();
        assertThat(elapsed).isEqualTo("1");
    }

    @Test
    void shouldProcessLine() {

        String line = "Progress: 1.5%, dl from 2 of 8 peers (0 kB/s), ul to 0 (0 kB/s) [12.5]";
        callback.processLine(line);
        assertThat(callback.getProgress()).isEqualTo(1);

        line = "   Progress: 95.8%, dl from 4 of 4 peers (3.19 MB/s), ul to 0 (0 kB/s) [12.5]      ";
        callback.processLine(line);


        assertThat(callback.getProgress()).isEqualTo(95);
//        assertThat(callback.getRuntime()).isEqualTo(12.5f);
//        assertThat(callback.getPeers()).isEqualTo("4 of 4");
    }

    @Test
    void shouldSetOutDirectory() {
        cli.setOutDirectory("downloads");
        File outDir = new File(cli.getOutDirectory());
        assertThat(outDir.exists()).isTrue();
    }


}