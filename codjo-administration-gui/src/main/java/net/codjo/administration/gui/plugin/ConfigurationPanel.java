package net.codjo.administration.gui.plugin;
import net.codjo.administration.common.AdministrationOntology;
import net.codjo.administration.common.ConfigurationOntology;
import static net.codjo.administration.gui.plugin.ActionType.DISABLE_SERVICE;
import static net.codjo.administration.gui.plugin.ActionType.ENABLE_SERVICE;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
/**
 *
 */
public class ConfigurationPanel {
    private JPanel mainPanel;
    private JButton recordHandlerStatisticsButton;
    private JButton recordMemoryUsageButton;
    private javax.swing.JTextField directoryLog;
    private JButton undoButton;
    private JButton applyButton;
    private JButton resetButton;
    private ImageIcon enableIcon = new ImageIcon(getClass().getResource("play.png"));
    private ImageIcon disableIcon = new ImageIcon(getClass().getResource("pause.png"));
    private ImageIcon undoIcon = new ImageIcon(getClass().getResource("undo.gif"));
    private ImageIcon applyIcon = new ImageIcon(getClass().getResource("apply.png"));
    private ImageIcon resetIcon = new ImageIcon(getClass().getResource("reload.png"));
    private UndoActionListener undoActionListener;


    public ConfigurationPanel() {

    }


    public JPanel getMainPanel() {
        return mainPanel;
    }


    public void init(ActionListener actionListener) {
        recordHandlerStatisticsButton.addActionListener(actionListener);
        recordMemoryUsageButton.addActionListener(actionListener);
        directoryLog.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                e.getComponent().setForeground(Color.BLUE);
                if (!undoButton.isEnabled()) {
                    undoButton.setEnabled(true);
                }
                if (!applyButton.isEnabled()) {
                    applyButton.setEnabled(true);
                }
            }
        });
        undoButton.setIcon(undoIcon);
        undoActionListener = new UndoActionListener();
        undoButton.addActionListener(undoActionListener);

        applyButton.setIcon(applyIcon);
        applyButton.setActionCommand(ActionType.CHANGE_LOG_DIR.name());
        applyButton.addActionListener(actionListener);

        resetButton.setIcon(resetIcon);
        resetButton.setActionCommand(ActionType.RESTORE_LOG_DIR.name());
        resetButton.addActionListener(actionListener);

        recordHandlerStatisticsButton.setName(ConfigurationOntology.RECORD_HANDLER_STATISTICS);
        recordMemoryUsageButton.setName(ConfigurationOntology.RECORD_MEMORY_USAGE);
        directoryLog.setName(ConfigurationOntology.AUDIT_DESTINATION_DIR);
    }


    public void initService(String service) {
        String[] spStrings = service.split(" ");

        if (spStrings[1].equals(AdministrationOntology.ENABLE_SERVICE_ACTION)) {
            enableService(spStrings[0]);
        }
        else if (spStrings[1].equals(AdministrationOntology.DISABLE_SERVICE_ACTION)) {
            disableService(spStrings[0]);
        }
        else if (spStrings[0].equals(directoryLog.getName())) {
            directoryLog.setText(spStrings[1]);
            undoActionListener.setInitialText(spStrings[1]);
            applyButton.setEnabled(false);
            undoButton.setEnabled(false);
        }
    }


    public void disableService(String serviceName) {
        if (recordHandlerStatisticsButton.getName().equals(serviceName)) {
            changeStateButton(recordHandlerStatisticsButton, enableIcon, ENABLE_SERVICE, "Activer");
        }
        else if (recordMemoryUsageButton.getName().equals(serviceName)) {
            changeStateButton(recordMemoryUsageButton, enableIcon, ENABLE_SERVICE, "Activer");
        }
    }


    public void enableService(String serviceName) {
        if (recordHandlerStatisticsButton.getName().equals(serviceName)) {
            changeStateButton(recordHandlerStatisticsButton, disableIcon, DISABLE_SERVICE, "Désactiver");
        }
        else if (recordMemoryUsageButton.getName().equals(serviceName)) {
            changeStateButton(recordMemoryUsageButton, disableIcon, DISABLE_SERVICE, "Désactiver");
        }
    }


    private void changeStateButton(JButton button, ImageIcon icon, ActionType command, String enableText) {
        button.setActionCommand(command.name());
        button.setIcon(icon);
        button.setToolTipText(enableText);
    }


    private class UndoActionListener implements ActionListener {
        private Color initialForeground;
        private String initialText;


        UndoActionListener() {
            initialForeground = directoryLog.getForeground();
        }


        public void setInitialText(String initialText) {
            this.initialText = initialText;
        }


        public void actionPerformed(ActionEvent e) {
            directoryLog.setForeground(initialForeground);
            directoryLog.setText(initialText);

            if (undoButton.isEnabled()) {
                undoButton.setEnabled(false);
                undoButton.transferFocus();
            }
            if (applyButton.isEnabled()) {
                applyButton.setEnabled(false);
                applyButton.transferFocus();
            }
        }
    }


    public String getDirectoryLog() {
        return directoryLog.getText();
    }
}
