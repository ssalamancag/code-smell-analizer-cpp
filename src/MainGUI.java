import sun.tools.jar.Main;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class MainGUI {

    private JTextField txtRoute;
    private JButton buscarButton;
    private JButton DETECTAROLORESDECODIGOButton;
    public JPanel PanelMain;
    private JTextField jTextMaxFun;
    private JTextField jTextMinFun;
    private JTextField jTextMinId;
    private JTextField jTextMaxId;
    private JTextField jTextMaxBol;
    private JTextField jTextMaxCyc;
    private JTextField jTextMaxPar;


    public MainGUI() {
        DETECTAROLORESDECODIGOButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    String route = txtRoute.getText();
                    int maxMethod = Integer.parseInt(jTextMaxFun.getText());
                    int minMethod = Integer.parseInt(jTextMinFun.getText());
                    int maxNesting = Integer.parseInt(jTextMaxCyc.getText());
                    int maxId = Integer.parseInt(jTextMaxId.getText());
                    int minId = Integer.parseInt(jTextMinId.getText());
                    int maxOps = Integer.parseInt(jTextMaxBol.getText());
                    int maxParams = Integer.parseInt(jTextMaxPar.getText());

                    GUIAnalize analizer = new GUIAnalize();
                    analizer.analizeCode(route, maxMethod, minMethod, maxNesting, maxId, minId, maxParams, maxOps);
                }catch(Exception exp){
                    System.out.print("GONORREA PONGA NUMEROS");
                }
            }
        });


        buscarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String inputPath =  "";

                JFileChooser file = new JFileChooser();
                file.setMultiSelectionEnabled(true);
                file.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                file.setFileHidingEnabled(false);
                if (file.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    java.io.File f = file.getSelectedFile();
                    inputPath = f.getPath();
                }

                txtRoute.setText(inputPath);
            }
        });
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
