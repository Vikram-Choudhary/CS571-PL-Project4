
public class ParserImpl extends Parser {

    /*
     * Implements a recursive-descent parser for the following CFG:
     * 
     * T -> F AddOp T { if ($2.type == TokenType.PLUS) { $$ = new PlusExpr($1,$3); }
     * else { $$ = new MinusExpr($1, $3); } }
     * T -> F { $$ = $1; }
     * F -> Lit MulOp F { if ($2.type == TokenType.Times) { $$ = new
     * TimesExpr($1,$3); } else { $$ = new DivExpr($1, $3); } }
     * F -> Lit { $$ = $1; }
     * Lit -> NUM { $$ = new FloatExpr(Float.parseFloat($1.lexeme)); }
     * Lit -> LPAREN T RPAREN { $$ = $2; }
     * AddOp -> PLUS { $$ = $1; }
     * AddOp -> MINUS { $$ = $1; }
     * MulOp -> TIMES { $$ = $1; }
     * MulOp -> DIV { $$ = $1; }
     */
    @Override
    public Expr do_parse() throws Exception {
        return parseT();
    }

    private Expr parseT() throws Exception {
        Expr left = parseF();
        if (peek(TokenType.PLUS, 0) || peek(TokenType.MINUS, 0)) {
            Expr right = parseT();

            Token op = consume(peek(TokenType.PLUS, 0) ? TokenType.PLUS : TokenType.MINUS);
            return (op.ty == TokenType.PLUS) ? new PlusExpr(left, right) : new MinusExpr(left, right);
        }
        return left;
    }

    private Expr parseF() throws Exception {
        Expr left = parseLit();
        if (peek(TokenType.TIMES, 0) || peek(TokenType.DIV, 0)) {
            Expr right = parseF();

            Token op = consume(peek(TokenType.TIMES, 0) ? TokenType.TIMES : TokenType.DIV);
            return (op.ty == TokenType.TIMES) ? new TimesExpr(left, right) : new DivExpr(left, right);
        }
        return left;
    }

    private Expr parseLit() throws Exception {
        if (peek(TokenType.NUM, 0)) {
            Token num = consume(TokenType.NUM);

            return new FloatExpr(Float.parseFloat(num.lexeme));
        } else if (peek(TokenType.LPAREN, 0)) {
            Expr expr = parseT();

            consume(TokenType.LPAREN);
            consume(TokenType.RPAREN);
            return expr;
        } else {
            throw new Exception("Literal or Parenthesis expected");
        }
    }

}
