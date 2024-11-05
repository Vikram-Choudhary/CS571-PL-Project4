public class CompilerFrontendImpl extends CompilerFrontend {
    public CompilerFrontendImpl() {
        super();
    }

    public CompilerFrontendImpl(boolean debug_) {
        super(debug_);
    }

    /*
     * Initializes the local field "lex" to be equal to the desired lexer.
     * The desired lexer has the following specification:
     * 
     * NUM: [0-9]*\.[0-9]+
     * PLUS: \+
     * MINUS: -
     * TIMES: \*
     * DIV: /
     * WHITE_SPACE (' '|\n|\r|\t)*
     */
    @Override
    protected void init_lexer() {
        LexerImpl lexer = new LexerImpl();

        // NUM: [0-9]*\\.[0-9]+
        AutomatonImpl numAutomaton = new AutomatonImpl();
        int startNum = 0, decimalNum = 1, fractionNum = 2;
        numAutomaton.addState(startNum, true, false);
        numAutomaton.addState(decimalNum, false, false);
        numAutomaton.addState(fractionNum, false, true);

        for (char c = '0'; c <= '9'; c++) {
            numAutomaton.addTransition(startNum, c, startNum);
            numAutomaton.addTransition(decimalNum, c, fractionNum);
            numAutomaton.addTransition(fractionNum, c, fractionNum);
        }
        numAutomaton.addTransition(startNum, '.', decimalNum);
        lexer.add_automaton(TokenType.NUM, numAutomaton);

        // PLUS: +
        AutomatonImpl plusAutomaton = new AutomatonImpl();
        plusAutomaton.addState(0, true, false);
        plusAutomaton.addState(1, false, true);
        plusAutomaton.addTransition(0, '+', 1);
        lexer.add_automaton(TokenType.PLUS, plusAutomaton);

        // MINUS: -
        AutomatonImpl minusAutomaton = new AutomatonImpl();
        minusAutomaton.addState(0, true, false);
        minusAutomaton.addState(1, false, true);
        minusAutomaton.addTransition(0, '-', 1);
        lexer.add_automaton(TokenType.MINUS, minusAutomaton);

        // TIMES: *
        AutomatonImpl timesAutomaton = new AutomatonImpl();
        timesAutomaton.addState(0, true, false);
        timesAutomaton.addState(1, false, true);
        timesAutomaton.addTransition(0, '*', 1);
        lexer.add_automaton(TokenType.TIMES, timesAutomaton);

        // DIV: /
        AutomatonImpl divAutomaton = new AutomatonImpl();
        divAutomaton.addState(0, true, false);
        divAutomaton.addState(1, false, true);
        divAutomaton.addTransition(0, '/', 1);
        lexer.add_automaton(TokenType.DIV, divAutomaton);

        // LPAREN: (
        AutomatonImpl lparenAutomaton = new AutomatonImpl();
        lparenAutomaton.addState(0, true, false);
        lparenAutomaton.addState(1, false, true);
        lparenAutomaton.addTransition(0, '(', 1);
        lexer.add_automaton(TokenType.LPAREN, lparenAutomaton);

        // RPAREN: )
        AutomatonImpl rparenAutomaton = new AutomatonImpl();
        rparenAutomaton.addState(0, true, false);
        rparenAutomaton.addState(1, false, true);
        rparenAutomaton.addTransition(0, ')', 1);
        lexer.add_automaton(TokenType.RPAREN, rparenAutomaton);

        // WHITE_SPACE: (' '|\n|\r|\t)
        AutomatonImpl whitespaceAutomaton = new AutomatonImpl();
        whitespaceAutomaton.addState(0, true, true);
        whitespaceAutomaton.addTransition(0, ' ', 0);
        whitespaceAutomaton.addTransition(0, '\n', 0);
        whitespaceAutomaton.addTransition(0, '\r', 0);
        whitespaceAutomaton.addTransition(0, '\t', 0);
        lexer.add_automaton(TokenType.WHITE_SPACE, whitespaceAutomaton);

        this.lex = lexer;
    }

}
