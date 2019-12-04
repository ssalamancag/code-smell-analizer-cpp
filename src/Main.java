import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import java.util.ArrayList;
import java.io.File;
public class Main {

    public static void main(String[] args) throws  Exception{
	    //Nuestro objeto analizador lexico
        CPPLexer lexer;

        //Puede leer a partir del archivo imput o por consola
	    if(args.length > 0 )
	        lexer = new CPPLexer(CharStreams.fromFileName(args[0]));
	    else
	        lexer = new CPPLexer(CharStreams.fromStream(System.in));

        // Identificar al analizador léxico como fuente de tokens para el sintactico
        CommonTokenStream tokens = new CommonTokenStream(lexer);

        // Crear el objeto correspondiente al analizador sintáctico que se alimenta a partir del buffer de tokens
        CPPParser parser = new CPPParser(tokens);

        // Iniciar el analisis sintáctico en la regla inicial
        ParseTree tree = parser.translationunit();

        //Se crea nuestro objeto de tipo visitor
        VisitorCPP<Object> loader = new VisitorCPP<>();

        //se recorre el arbol
        loader.visit(tree);

        //Se llama al metodo que imprimira el arrray de smells
        loader.detector.print();


    }
}
