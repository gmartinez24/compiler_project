package ast;

public class PrettyPrinter implements NodeVisitor {

    private int depth = 0;
    private StringBuilder sb = new StringBuilder();

    private void println (Node n, String message) {
        String indent = "";
        for (int i = 0; i < depth; i++) {
            indent += "  ";
        }
        sb.append(indent + n.getClassInfo() + message + "\n");
    }

    @Override
    public String toString () {
        return sb.toString();
    }


    @Override
    public void visit(BoolLiteral node) {
        println(node, "[" + node.value() + "]");
    }

    @Override
    public void visit(IntegerLiteral node) {
        println(node, "[" + node.value() + "]");
    }

    @Override
    public void visit(FloatLiteral node) {
        println(node, "[" + node.value() + "]");
    }


    @Override
    public void visit(Identifier node) {
        println(node, "[" + node.symbol().toString() + "]");
    }

    @Override
    public void visit(LogicalNot node) {
        println(node, "");
        depth++;
        node.expression().accept(this);
        depth--;
    }

    @Override
    public void visit(Power node) {
        println(node, "");
        depth++;
        node.lhs().accept(this);
        node.rhs().accept(this);
        depth--;
    }

    @Override
    public void visit(Multiplication node) {
        println(node, "");
        depth++;
        node.lhs().accept(this);
        node.rhs().accept(this);
        depth--;
    }

    @Override
    public void visit(Division node) {
        println(node, "");
        depth++;
        node.lhs().accept(this);
        node.rhs().accept(this);
        depth--;
    }

    @Override
    public void visit(Modulo node) {
        println(node, "");
        depth++;
        node.lhs().accept(this);
        node.rhs().accept(this);
        depth--;
    }

    @Override
    public void visit(LogicalAnd node) {
        println(node, "");
        depth++;
        node.lhs().accept(this);
        node.rhs().accept(this);
        depth--;
    }

    @Override
    public void visit(Addition node) {
        println(node, "");
        depth++;
        node.lhs().accept(this);
        node.rhs().accept(this);
        depth--;
    }

    @Override
    public void visit(Subtraction node) {
        println(node, "");
        depth++;
        node.lhs().accept(this);
        node.rhs().accept(this);
        depth--;
    }

    @Override
    public void visit(LogicalOr node) {
        println(node, "");
        depth++;
        node.lhs().accept(this);
        node.rhs().accept(this);
        depth--;
    }

    @Override
    public void visit(Relation node) {
        println(node, "[" + node.operator() + "]");
        depth++;
        node.lhs().accept(this);
        node.rhs().accept(this);
        depth--;
    }

    @Override
    public void visit(Assignment node) {
        println(node, "");
        depth++;
        node.lhs().accept(this);
        node.rhs().accept(this);
        depth--;
    }

    @Override
    public void visit(ArgumentList node) {
        println(node, "");
        depth++;
        for (Expression e : node.args()) {
            e.accept(this);
        }
        depth--;
    }

    @Override
    public void visit(FunctionCall node) {
        println(node,"[" + node.symbol().toString() + "]");
        depth++;
        node.args().accept(this);
        depth--;
    }

    @Override
    public void visit(IfStatement node) {
        println(node, "");
        depth++;
        node.relation().accept(this);
        node.ifBlock().accept(this);
        node.elseBlock().accept(this);
        depth--;
    }

    @Override
    public void visit(WhileStatement node) {
        println(node, "");
        depth++;
        node.relation().accept(this);
        node.statSeq().accept(this);
        depth--;

    }

    @Override
    public void visit(RepeatStatement node) {
        println(node, "");
        depth++;
        node.repeatBlock().accept(this);
        node.relation().accept(this);
        depth--;
    }

    @Override
    public void visit(ReturnStatement node) {
        println(node, "");
        depth++;
        node.relation().accept(this);
        depth--;
    }

    // TODO: implement visit functions from NodeVisitor in here
    // Each visit calls the accept function on that object which
    // then calls the correct visit function
    @Override
    public void visit (StatementSequence node) {
        println(node, "");
        depth++;
        for (Statement s : node.getStatements()) {
            s.accept(this);
        }
        depth--;
    }

    @Override
    public void visit (VariableDeclaration node) {
        println(node, "[" + node.symbol().toString() + "]");
    }

    @Override
    public void visit(FunctionBody node) {
        println(node, "");
        if(node.vars() != null){
            node.vars().accept(this);
        }
        node.funcSeq().accept(this);

    }

    @Override
    public void visit (FunctionDeclaration node) {
        println(node, "[" + node.function() + "]");
        depth++;
        node.body().accept(this);
        depth--;
    }

    @Override
    public void visit (DeclarationList node) {
        if (node.empty()) return;
        println(node, "");
        depth++;
        for (Declaration d : node.declarationList()) {
            d.accept(this);
        }
        depth--;
    }

    @Override
    public void visit (Computation node) {
        println(node, "[" + node.main() + "]");
        depth++;
        if(node.variables() != null){
            node.variables().accept(this);
        }
        if(node.functions() != null){
            node.functions().accept(this);

        }
        node.mainStatementSequence().accept(this);
        depth--;
    }
}
