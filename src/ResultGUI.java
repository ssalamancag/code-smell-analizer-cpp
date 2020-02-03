import javafx.scene.layout.Pane;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class ResultGUI {

    private JFrame frame2 = new JFrame("ResultFrame");
    private JPanel PanelResult;
    private JTextArea textAreaCode;
    private JTextArea textAreaSmells;
    private String code;

    public ResultGUI(String code, String smells) {
        this.code = code;
        textAreaCode.setText(code);
        textAreaSmells.setText(smells);
    }

    public void createResultGUI(String code, String smells){
        frame2.setContentPane(new ResultGUI(code, smells).PanelResult);
        frame2.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame2.pack();
        frame2.setVisible(true);
    }

}
