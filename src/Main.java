import javax.swing.*;
import java.io.File;


public class Main {
    public static void main(String[] args) throws  Exception{
        JFrame frame = new JFrame("MainGUI");
        frame.setContentPane(new MainGUI().PanelMain);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
