package tw.waterball.ddd.robots;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import tw.waterball.ddd.robots.view.LivesMonitor;

import java.awt.*;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
@SpringBootApplication(scanBasePackages = "tw.waterball.ddd")
public class Main {
    public static void main(String[] args) {
        var context = new SpringApplicationBuilder(Main.class)
                .headless(false).run(args);
        Framework framework = context.getBean(Framework.class);
        if (args.length != 0 && args[0].equals("--monitor")) {
            EventQueue.invokeLater(() -> {
                LivesMonitor livesMonitor = new LivesMonitor();
                framework.addLivesListener(livesMonitor);
                livesMonitor.launch();
            });
        }
        framework.start();
    }
}
