package io.github.retrooper.packetevents.manager.logger.jul;

import java.util.logging.Logger;

public class JULoggerFactory {
    public static Logger createLogger(String name) {
        try {
            return new Slf4jBackedJULogger(name);
        } catch (NoClassDefFoundError | Exception ignored) {
        }
        try {
            return new Log4jBackedJULogger(name);
        } catch (NoClassDefFoundError | Exception ignored) {
        }

        return Logger.getLogger(name);
    }
}
