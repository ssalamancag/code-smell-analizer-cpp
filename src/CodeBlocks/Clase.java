package CodeBlocks;

import java.util.ArrayList;
import java.util.List;

public class Clase {
    String name;
    List<Funcion> metodos = new ArrayList<Funcion>();
    boolean instanced;
    int line;
    int col;

    public Clase(String name, int line, int col) {
        this.name = name;
        this.line = line;
        this.col = col;
        this.instanced = false;
    }

    public String getName() {
        return name;
    }
    public int getLine() {
        return line;
    }
    public int getCol() {
        return col;
    }
    public boolean isInstanced() {
        return instanced;
    }

    public void addMetodo(Funcion funcion) {
        this.metodos.add(funcion);
    }

    public void instance(){
        this.instanced = true;
    }
}
