package DeLP_GDPR.delp.parser;

import org.tweetyproject.commons.Formula;
import org.tweetyproject.commons.Parser;
import org.tweetyproject.commons.ParserException;

import DeLP_GDPR.delp.syntax.DefeasibleLogicProgram;
import DeLP_GDPR.delp.syntax.DefeasibleRule;
import DeLP_GDPR.delp.syntax.DelpFact;
import DeLP_GDPR.delp.syntax.StrictRule;
import DeLP_GDPR.logics.commons.syntax.Constant;
import DeLP_GDPR.logics.commons.syntax.Predicate;
import DeLP_GDPR.logics.commons.syntax.Variable;
import DeLP_GDPR.logics.commons.syntax.interfaces.Term;
import DeLP_GDPR.logics.fol.syntax.FolAtom;
import DeLP_GDPR.logics.fol.syntax.FolFormula;
import DeLP_GDPR.logics.fol.syntax.FolSignature;
import DeLP_GDPR.logics.fol.syntax.Negation;

import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
  * This class implements a parser for defeasible logic programs. The BNF for defeasible
  * logic program files is given by (start symbol is THEORY)
  * <br>
  * <pre>
  * THEORY  		::== (EXPRESSION)+
  * EXPRESSION 		::== FACT | STRICTRULE | DEFEASIBLERULE
  * FACT 			::== LITERAL + "."
  * STRICTRULE 		::== LITERAL + "&lt;-" + RULEBODY + "."
  * DEFEASIBLERULE 	::== LITERAL + "-&lt;" + RULEBODY + "."
  * RULEBODY 		::== LITERAL | LITERAL + "," + RULEBODY
  * LITERAL 		::== "~" + ATOM | ATOM
  * ATOM 			::== PREDICATE | PREDICATE + "(" + TERMLIST + ")"
  * TERMLIST 		::== TERM | TERM + "," + TERMLIST
  * TERM 			::== VARIABLE | CONSTANT | QUOTED_STRING
  *
  * PREDICATE is a sequence of symbols from {a,...,z,A,...,Z,0,...,9,_,-} with a letter at the beginning.
  * VARIABLE is a sequence of symbols from {a,...,z,A,...,Z,0,...,9,_,-} with an uppercase letter at the beginning.
  * CONSTANT is  a sequence of symbols from {a,...,z,A,...,Z,0,...,9,_,-} with an lowercase letter at the beginning.
  * QUOTED_STRING is all characters between double quotes.
  * </pre>
  */
@SuppressWarnings("all")
public class DelpParser extends Parser<DefeasibleLogicProgram,Formula> implements DelpParserConstants {

        private FolSignature signature = new FolSignature();

        public DelpParser() { this(new StringReader("")); }

        public DefeasibleLogicProgram parseBeliefBase(Reader reader) throws ParserException{
                try {
                        ReInit(reader);
                        return this.Theory(this.signature);
                } catch(ParseException e) {
                        throw new ParserException(e);
                }
        }

    /*
     * A formula here is a Literal, that is an Atom or a negated Atom.
     * The class DelpQuery encapsulates the following.
     * The Atom is either a DeLP predicate (a predicate with arity > 0) or a
     * DeLP constant or variable (a predicate with arity == 0).
     * In the case of a real predicate, test all arguments whether they are
     * DeLP variables (= begin with upper case letter) or DeLP constants.
     *
     * All predicates and constants need to be present in the current signature to parse.
     *
     * @param reader the reader to parse from
     * @return a Formula, which is always a DelpQuery in this implementation,
     *         that has been successfully parsed with the current signature in mind
     * @throws ParserException if the reader cannot be successfully parsed into a formula
     */
        public Formula parseFormula(Reader reader) throws ParserException{
                try {
                        ReInit(reader);
                        FolFormula fol = this.Formula(this.signature);
                        // check that formula contains only known constants and predicates:
                        FolAtom atom =  (FolAtom) fol.getAtoms().iterator().next();
            Predicate p = atom.getPredicate();
            if (signature.getPredicate(p.getName()) == null)
                throw new ParseException("Formula contains unknown predicate '" + p + "'");
            if (signature.getPredicate(p.getName()).getArity() != p.getArity())
                throw new ParseException("Formula contains predicate '" + p + "' with non-matching arity");
            for (Term t : atom.getArguments()) {
                if (t instanceof Constant &&
                    signature.getConstant(((Constant) t).get()) == null)
                        throw new ParseException("Formula constains unknown constant '" + t + "'");
            }
            return fol;
                } catch(ParseException e) {
                        throw new ParserException(e);
                }
        }

        public FolSignature getSignature(){
                return this.signature;
        }

    private Constant createConstant(String image, DefeasibleLogicProgram delp, FolSignature signature) throws ParseException {
                // treat constants also as predicates with arity = 0 to be consistent with
                // parsing queries as formulae:

        // only add constant/predicate to signature if parsing DeLP!
        Constant constant = new Constant(image);
        if(delp != null && !signature.containsConstant(image))
            signature.add(constant);
        Predicate predicate = new Predicate(image);
        if(delp != null && !signature.containsPredicate(image))
            signature.add(predicate);
        if(signature.containsPredicate(image) && signature.getPredicate(image).getArity() != 0)
            throw new ParseException("Wrong arity of predicate as constant '" + image + "'");
        return constant;
    }

  final public DefeasibleLogicProgram Theory(FolSignature signature) throws ParseException {
        DefeasibleLogicProgram delp = new DefeasibleLogicProgram();
    label_1:
    while (true) {
      Expression(delp,signature);
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case NAME:
      case 12:
        ;
        break;
      default:
        jj_la1[0] = jj_gen;
        break label_1;
      }
    }
    jj_consume_token(0);
                {if (true) return delp;}
    throw new Error("Missing return statement in function");
  }

  final public void Expression(DefeasibleLogicProgram delp,FolSignature signature) throws ParseException {
        FolFormula lit;
        Set<FolFormula> body = new HashSet<FolFormula>();
        FolFormula b;
    lit = Literal(delp,signature);
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case 8:
      jj_consume_token(8);
                 delp.add(new DelpFact(lit));
      break;
    case 9:
      jj_consume_token(9);
      b = Literal(delp,signature);
                                                    body.add(b);
      label_2:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case 10:
          ;
          break;
        default:
          jj_la1[1] = jj_gen;
          break label_2;
        }
        jj_consume_token(10);
        b = Literal(delp,signature);
                                               body.add(b);
      }
      jj_consume_token(8);
                    delp.add(new StrictRule(lit,body));
      break;
    case 11:
      jj_consume_token(11);
      b = Literal(delp,signature);
                                                    body.add(b);
      label_3:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case 10:
          ;
          break;
        default:
          jj_la1[2] = jj_gen;
          break label_3;
        }
        jj_consume_token(10);
        b = Literal(delp,signature);
                                                       body.add(b);
      }
      jj_consume_token(8);
                            delp.add(new DefeasibleRule(lit,body));
      break;
    default:
      jj_la1[3] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
  }

  final public FolFormula Formula(FolSignature signature) throws ParseException {
        FolFormula lit;
    lit = Literal(null,signature);
    jj_consume_token(0);
                                           {if (true) return lit;}
    throw new Error("Missing return statement in function");
  }

  final public FolFormula Literal(DefeasibleLogicProgram delp,FolSignature signature) throws ParseException {
        FolAtom atom;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case NAME:
      atom = Atom(delp,signature);
                                   {if (true) return atom;}
      break;
    case 12:
      jj_consume_token(12);
      atom = Atom(delp,signature);
                                       {if (true) return new Negation(atom);}
      break;
    default:
      jj_la1[4] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
    throw new Error("Missing return statement in function");
  }

  final public FolAtom Atom(DefeasibleLogicProgram delp,FolSignature signature) throws ParseException {
        Token p;
        List<Term<?>> terms = new ArrayList<Term<?>>();
        Term<?> t;
    p = jj_consume_token(NAME);
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case 13:
      jj_consume_token(13);
      t = Term(delp,signature);
                                            terms.add(t);
      label_4:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case 10:
          ;
          break;
        default:
          jj_la1[5] = jj_gen;
          break label_4;
        }
        jj_consume_token(10);
        t = Term(delp,signature);
                                              terms.add(t);
      }
      jj_consume_token(14);
      break;
    default:
      jj_la1[6] = jj_gen;
      ;
    }
        // only add predicate to signature if parsing DeLP!
        Predicate predicate = new Predicate(p.image,terms.size());
        if(delp != null && !signature.containsPredicate(p.image))
            signature.add(predicate);
        if(signature.containsPredicate(p.image) && signature.getPredicate(p.image).getArity() != terms.size())
                {if (true) throw new ParseException("Wrong arity of predicate '" + p.image + "'");}
        {if (true) return new FolAtom(predicate,terms);}
    throw new Error("Missing return statement in function");
  }

  final public Term Term(DefeasibleLogicProgram delp,FolSignature signature) throws ParseException {
        Token t;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case NAME:
      t = jj_consume_token(NAME);
                if(Character.isUpperCase(t.image.charAt(0)))
                        {if (true) return new Variable(t.image);}
                {if (true) return createConstant(t.image, delp, signature);}
      break;
    case QUOTED:
      t = jj_consume_token(QUOTED);
        // get rid of quotes:
        String text = t.image.substring(1, t.image.length() - 1);
        {if (true) return createConstant(text, delp, signature);}
      break;
    default:
      jj_la1[7] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
    throw new Error("Missing return statement in function");
  }

  /* Generated Token Manager. */
  public DelpParserTokenManager token_source;
  SimpleCharStream jj_input_stream;
  /* Current token. */
  public Token token;
  /* Next token. */
  public Token jj_nt;
  private int jj_ntk;
  private int jj_gen;
  final private int[] jj_la1 = new int[8];
  static private int[] jj_la1_0;
  static {
      jj_la1_init_0();
   }
   private static void jj_la1_init_0() {
      jj_la1_0 = new int[] {0x1040,0x400,0x400,0xb00,0x1040,0x400,0x2000,0xc0,};
   }

  /* Constructor with InputStream. */
  public DelpParser(java.io.InputStream stream) {
     this(stream, null);
  }
  /* Constructor with InputStream and supplied encoding */
  public DelpParser(java.io.InputStream stream, String encoding) {
    try { jj_input_stream = new SimpleCharStream(stream, encoding, 1, 1); } catch(java.io.UnsupportedEncodingException e) { throw new RuntimeException(e); }
    token_source = new DelpParserTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 8; i++) jj_la1[i] = -1;
  }

  /* Reinitialise. */
  public void ReInit(java.io.InputStream stream) {
     ReInit(stream, null);
  }
  /* Reinitialise. */
  public void ReInit(java.io.InputStream stream, String encoding) {
    try { jj_input_stream.ReInit(stream, encoding, 1, 1); } catch(java.io.UnsupportedEncodingException e) { throw new RuntimeException(e); }
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 8; i++) jj_la1[i] = -1;
  }

  /* Constructor. */
  public DelpParser(java.io.Reader stream) {
    jj_input_stream = new SimpleCharStream(stream, 1, 1);
    token_source = new DelpParserTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 8; i++) jj_la1[i] = -1;
  }

  /* Reinitialise. */
  public void ReInit(java.io.Reader stream) {
    jj_input_stream.ReInit(stream, 1, 1);
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 8; i++) jj_la1[i] = -1;
  }

  /* Constructor with generated Token Manager. */
  public DelpParser(DelpParserTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 8; i++) jj_la1[i] = -1;
  }

  /* Reinitialise. */
  public void ReInit(DelpParserTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 8; i++) jj_la1[i] = -1;
  }

  private Token jj_consume_token(int kind) throws ParseException {
    Token oldToken;
    if ((oldToken = token).next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    if (token.kind == kind) {
      jj_gen++;
      return token;
    }
    token = oldToken;
    jj_kind = kind;
    throw generateParseException();
  }


/* Get the next Token. */
  final public Token getNextToken() {
    if (token.next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    jj_gen++;
    return token;
  }

/* Get the specific Token. */
  final public Token getToken(int index) {
    Token t = token;
    for (int i = 0; i < index; i++) {
      if (t.next != null) t = t.next;
      else t = t.next = token_source.getNextToken();
    }
    return t;
  }

  private int jj_ntk() {
    if ((jj_nt=token.next) == null)
      return (jj_ntk = (token.next=token_source.getNextToken()).kind);
    else
      return (jj_ntk = jj_nt.kind);
  }

  private java.util.List<int[]> jj_expentries = new java.util.ArrayList<int[]>();
  private int[] jj_expentry;
  private int jj_kind = -1;

  /* Generate ParseException. */
  public ParseException generateParseException() {
    jj_expentries.clear();
    boolean[] la1tokens = new boolean[15];
    if (jj_kind >= 0) {
      la1tokens[jj_kind] = true;
      jj_kind = -1;
    }
    for (int i = 0; i < 8; i++) {
      if (jj_la1[i] == jj_gen) {
        for (int j = 0; j < 32; j++) {
          if ((jj_la1_0[i] & (1<<j)) != 0) {
            la1tokens[j] = true;
          }
        }
      }
    }
    for (int i = 0; i < 15; i++) {
      if (la1tokens[i]) {
        jj_expentry = new int[1];
        jj_expentry[0] = i;
        jj_expentries.add(jj_expentry);
      }
    }
    int[][] exptokseq = new int[jj_expentries.size()][];
    for (int i = 0; i < jj_expentries.size(); i++) {
      exptokseq[i] = jj_expentries.get(i);
    }
    return new ParseException(token, exptokseq, tokenImage);
  }

  /* Enable tracing. */
  final public void enable_tracing() {
  }

  /* Disable tracing. */
  final public void disable_tracing() {
  }

}
