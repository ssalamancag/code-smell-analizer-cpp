import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import java.io.*;

public class GUIAnalize {

    public void analizeCode(String file, int maxMethod, int minMethod, int maxNesting, int maxId, int minId, int maxParams, int maxOps) throws IOException {
        CPPLexer lexer;
        String showCode;

        showCode = addLineNumber(file);

        //Puede leer a partir del archivo imput o por consola
        lexer = new CPPLexer(CharStreams.fromFileName(file));

        // Identificar al analizador léxico como fuente de tokens para el sintactico
        CommonTokenStream tokens = new CommonTokenStream(lexer);

        // Crear el objeto correspondiente al analizador sintáctico que se alimenta a partir del buffer de tokens
        CPPParser parser = new CPPParser(tokens);

        // Iniciar el analisis sintáctico en la regla inicial
        ParseTree tree = parser.translatinunit();

        //Se crea nuestro objeto de tipo visitor
        VisitorCPP<Object> loader = new VisitorCPP<>(maxMethod, minMethod, maxNesting, maxId, minId, maxParams, maxOps);

        //se recorre el arbol
        loader.visit(tree);
        loader.nonInstancedClasses();
        loader.nonExecutedFunctions();

        ResultGUI resultado = new ResultGUI(showCode, loader.detector.print());
        resultado.createResultGUI(showCode, loader.detector.print());
        //Se llama al metodo que imprimira el arrray de smells

        //Nuestro objeto analizador lexico
    }

    public String addLineNumber(String file){
        String codeFormatted = "";
        String readLine = "";
        Integer i = 1;
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        ) {
            while ((readLine = bufferedReader.readLine()) != null) {
                String line = Integer.toString(i) + ") -> " + readLine;
                codeFormatted += line + "\n";
                i++;
            }
        } catch (IOException e) {
            System.out.println("Error.");
            e.printStackTrace();
        }
        return codeFormatted;
    }
}
