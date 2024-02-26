package ast;
import java.util.Stack;
public class AST {

    // private members
    private Node root;

    // TODO: Create AST structure of your choice
    /*
        Constructor gets passed the root aka and instance of the Computation class
        Should return the AST with all the nodes connected through the root.
        I feel like this is solely for correct design as the Computation class itself has the same tree
        structure, but it lacks member functions like print.
    */
    public AST(Computation computation) {


        root = computation;
        // Root = parser.Parse;
        //throw new RuntimeException("implement AST");
    }

    public String printPreOrder(){
        PrettyPrinter printer = new PrettyPrinter();
        if(root == null){
            return "";
        }
        root.accept(printer);
        return printer.toString();


    }
    public Node getRoot(){
        return root;
    }
}
