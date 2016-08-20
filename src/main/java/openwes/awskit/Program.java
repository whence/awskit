package openwes.awskit;

import com.beust.jcommander.JCommander;
import openwes.awskit.commands.Adfs;
import openwes.awskit.commands.Root;

import java.util.HashMap;
import java.util.Map;

public class Program {
    public static void main(String[] args) {
        Root root = new Root();
        JCommander jc = new JCommander(root);
        jc.setProgramName("awskit");

        Map<String, Runnable> commands = new HashMap<>();
        commands.put("adfs", new Adfs());

        for (Map.Entry<String, Runnable> entry : commands.entrySet()) {
            jc.addCommand(entry.getKey(), entry.getValue());
        }

        jc.parse(args);

        String command = jc.getParsedCommand();
        if (command != null) {
            commands.get(command).run();
        } else {
            jc.usage();
        }
    }
}
