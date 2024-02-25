package ast;
import javax.swing.plaf.nimbus.State;
import java.util.ArrayList;


//Class initalized with an empty list of statements
public class StatementSequence extends Node{
    private ArrayList<Statement> statements;

    public StatementSequence(int lineNum, int charPos) {
        super(lineNum, charPos);
        statements  = new ArrayList<>();
    }

    public void addStatement(Statement statement){
        statements.add(statement);
    }

    public ArrayList<Statement> getStatements(){
        return this.statements;
    }
}
