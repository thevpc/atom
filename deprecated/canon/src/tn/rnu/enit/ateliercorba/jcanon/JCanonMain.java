package tn.rnu.enit.ateliercorba.jcanon;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author Taha Ben Salah (thevpc@walla.com)
 * @creationtime 16 oct. 2007 17:38:52
 */
public class JCanonMain {
    public static void main(String[] args) {
        JLabel label = new JLabel("Select ");
        JCheckBox server = new JCheckBox("Host New Server", true);
        JCheckBox client = new JCheckBox("Start Client", false);
        ButtonGroup group = new ButtonGroup();
        group.add(server);
        group.add(client);

        JTextField port = new JTextField("1050");
        JTextField host = new JTextField("");
        JLabel hostLabel = new JLabel("Name Server Address");
        JLabel portLabel = new JLabel("Name Server Port");
        JPanel panel = new JPanel(
                new GridBagLayout2()
                        .addLine("[<label.           ]")
                        .addLine("[<server.          ]")
                        .addLine("[<client.          ]")
                        .addLine("[<hostLabel][<=-host ]")
                        .addLine("[<portLabel][<=-port ]")
                        .setInsets(".*", new Insets(3, 3, 3, 3))
        );
        panel.add(label, "label");
        panel.add(server, "server");
        panel.add(client, "client");
        panel.add(hostLabel, "hostLabel");
        panel.add(portLabel, "portLabel");
        panel.add(port, "port");
        panel.add(host, "host");

        int i = JOptionPane.showConfirmDialog(null, panel, "JCanon (Enit 2007)", JOptionPane.OK_CANCEL_OPTION);
        if (i == JOptionPane.OK_OPTION) {
            ArrayList<String> args2 = new ArrayList<String>(Arrays.asList(args));
            if (port.getText().trim().length() > 0) {
                args2.add("-ORBInitialPort");
                args2.add(port.getText());
            }
            if (host.getText().trim().length() > 0) {
                args2.add("-ORBInitialHost");
                args2.add(host.getText());
            }

            args = args2.toArray(new String[args2.size()]);

            if (client.isSelected()) {
                JCanonClient.main(args);
            } else if (server.isSelected()) {
                JCanonServer.main(args);
            }
        }
    }
}
