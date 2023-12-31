package DeLP_GDPR.math.term;




/**
 * This class is represents a general math exception;
 *
 */
public class GeneralMathException extends Exception {

	/**
	 * For serialization.
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Creates a new general math exception.
	 */
	public GeneralMathException(){
		super();
	}
	
	/**
	 * Creates a new general math exception.
	 * @param s an error message
	 */
	public GeneralMathException(String s){
		super(s);
	}
}
