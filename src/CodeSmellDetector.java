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
    public void print (){
        for( CodeSmell c: codeSmells){
            System.out.println("Type: "+ c.type+ " in position,  line: "+c.line+" column: "+ c.colum );
        }
    }

}
