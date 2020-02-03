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
    private JButton longShortFunctionButton;
    private JButton longShortIdentifierButton;
    private JButton complexExpressionButton;
    private JButton cyclomaticComplexityButton;
    private JButton manyParametersButton;

    public void createMainGUI(){
        JFrame frame1 = new JFrame("MainGUI");
        frame1.setContentPane(new MainGUI().PanelMain);
        frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame1.pack();
        frame1.setVisible(true);
    }

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
                    if(!route.isEmpty()){
                        GUIAnalize analizer = new GUIAnalize();
                        analizer.analizeCode(route, maxMethod, minMethod, maxNesting, maxId, minId, maxParams, maxOps);
                    }else{
                        JOptionPane.showMessageDialog(null, "Escoger un Archivo");
                    }
                }catch(Exception exp){
                    JOptionPane.showMessageDialog(null, "Escoger Parametros o un Archivo Valido");
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

        longShortFunctionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String content =    "<html> <h3>Metodo Muy Largo</h3>" +
                                    "Un método o funcion que contiene demasiadas líneas de código. " +
                                    "<br> En general, cualquier método de más de 20 lineas se puede considerar como muy largo, " +
                                    "<br> cuanto más largo sea un método o función, más difícil será entenderlo y mantenerlo. " +
                                    "<h4> Como Solucionarlo</h4> " +
                                    "Busca secciones del codigo que pueden ser extraidas en un nuevo metodo," +
                                    "<br> o en caso de que no quieras hacer eso trata de optimizar el codigo para que utilice menos lineas " +
                                    "<h3>Metodo Muy Corto</h3> No es un code smell necesariamente pero si necesitas detectarlos aqui puedes </hmtl>";
                JOptionPane.showMessageDialog(PanelMain, content,"Metodo Muy Largo/Corto" , JOptionPane.PLAIN_MESSAGE);

            }
        });
        longShortIdentifierButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String content =    "<html> <h3>Identificador Muy Largo</h3>" +
                                    "los identificadores muy largos pueden desordenar el codigo." +
                                    "<br> Adicionalmente genera una contaminacion visual al codigo que dificulta su comprension   " +
                                    "<h3>Identificador Muy Corto</h3> " +
                                    "El nombre de una variable debe reflejar su función a menos que la función sea demasiado obvia" +
                                    "<h4> Como Solucionarlo</h4> Darle a los identificadores muy cortas un nombre un poco mas relacionado a su funcion " +
                                    "<br> Y a los identificadores muy largos tratar de abreviar las palabras  o resumir su significado. </hmtl>";
                JOptionPane.showMessageDialog(PanelMain, content, "Identificador Muy Largo/Corto" , JOptionPane.PLAIN_MESSAGE);
            }
        });
        manyParametersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String content =    "<html> <h3>Demasiados Parametros</h3>" +
                        "Una larga lista de parámetros puede tener varias razones para existir <br>" +
                        "Sin embargo es difícil listas de parametros muy largos que se vuelven contradictorias y difíciles de usar a medida que crecen." +
                        "  <h4> Como Solucionarlo</h4> Si alguno de los parametros proviene de una llamada funcion, llama realiza esa llamada  dentro del metodo actual <br>" +
                        "  En lugar de una larga lista de parámetros, un método puede usar los datos de su propio objeto. <br>" +
                        "  Si el objeto actual no contiene todos los datos necesarios, se puede pasar otro objeto como parámetro de método.</hmtl> ";
                JOptionPane.showMessageDialog(PanelMain, content, "Identificador Muy Largo/Corto" , JOptionPane.PLAIN_MESSAGE);

            }
        });
        cyclomaticComplexityButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String content = "<html><h3>Complejidad ciclomatica</h3>" +
                        "A medida que el numero ciclos anidados dentro de un ciclo sea mayor la complejidad aumenta " +
                        "<br> incrementando el riesgo de error." +
                        "<h4>Solucion</h4>" +
                        "En general, debe simplificar los métodos o dividirlos.</hmtl>" ;
                JOptionPane.showMessageDialog(PanelMain, content, "Identificador Muy Largo/Corto" , JOptionPane.PLAIN_MESSAGE);
            }
        });
        complexExpressionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String content = "<html><h3>Expresiones booleanas complejas</h3>" +
                        "Cuando una expresion sobrepasa cierto numero de operadores se hace dificil de comprender  y evaluar para el programador " +
                        "<br> aumentando la probabilidad de cometer errores" +
                        "<h4>Solucion</h4>" +
                        "Divide una expresion compleja en unas mas sencillas asignandolas a variables booleanas " +
                        "<br> Luego usa las variables asignadas para construir la expresion original </hmtl>";
                JOptionPane.showMessageDialog(PanelMain, content, "Expresiones Booleanas Complejas" , JOptionPane.PLAIN_MESSAGE);
            }
        });
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
