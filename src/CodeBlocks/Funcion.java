package CodeBlocks;

public class Funcion {
    String name;
    boolean executed;
    int line;
    int col;

    public Funcion(String name, int line, int col, boolean executed) {
        this.name = name;
        this.line = line;
        this.col = col;
        if(name.equals("main")){
            this.executed = true;
        }else{
            this.executed = executed;
        }

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
    public boolean isExecuted() {
        return executed;
    }

    public void execute(){
        this.executed = true;
    }
}
