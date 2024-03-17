package life.macchiato.courses.util;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import pro.macchiato.cli.Cli;
import pro.macchiato.cli.ProgressCallback;

import java.time.Instant;

import static org.assertj.core.api.Assertions.*;


@Slf4j
class TransCliTest {

    private Cli cli;
    private TransCli.Callback callback;

    @BeforeEach
    void setUp() {
        callback = new TransCli.Callback();
    }

    @AfterEach
    void tearDown() {
    }

    @Test @Disabled
    void shouldCalculateElapsed() throws InterruptedException {
        Thread.sleep(1000);
        long elapsed = callback.getElapsed();
        assertThat(elapsed).isEqualTo(1);
    }

    @Test
    void shouldProcessLine() {

        String line = "Progress: 1.5%, dl from 2 of 8 peers (0 kB/s), ul to 0 (0 kB/s) [12.5]";
        callback.processLine(line);

        assertThat(callback.getProgress()).isEqualTo(1.5f);
        assertThat(callback.getRuntime()).isEqualTo(12.5f);
        assertThat(callback.getPeers()).isEqualTo("2 of 8");
    }
}