import java.util.*;

public class CodeSmellDetector {

    //Lista de todos los smells encontrados
    ArrayList<CodeSmell> codeSmells = new ArrayList<>();

    CodeSmellDetector (){

    }

    //Funcion para agregar un nuevo Smell a la lista
    public void AddCodeSmell(SMELL type, int line, int column) {
        CodeSmell smell =  new CodeSmell();
        smell.type = type;
        smell.line = line;
        smell.colum = column;
        codeSmells.add(smell);
    }

    //metodo para imprimir
    public String print (){
        String smells = "";
        for( CodeSmell c: codeSmells){
            smells += "Type: "+ c.type+   "\t" + " in position,  line: "+c.line + "\n";
        }
        return smells;
    }

}
