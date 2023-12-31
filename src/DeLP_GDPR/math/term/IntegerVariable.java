package DeLP_GDPR.math.term;




/**
 * This class models an integer variable as a mathematical term.
 */
public class IntegerVariable extends Variable{
	
	/**
	 * Creates a new variable with the given name.
	 * @param name the name of this variable.
	 */
	public IntegerVariable(String name){
		super(name);
	}
	
	/**
	 * Creates a new variable with the given name.
	 * @param name the name of this variable.
	 * @param isPositive whether this variables should be positive.
	 */
	public IntegerVariable(String name, boolean isPositive){
		super(name,isPositive);	
	}
	
	/**
	 * Creates a new variable with the given name and bounds.
	 * @param name the name of this variable.
	 * @param lowerBound the lower bound of the variable.
	 * @param upperBound the upper bound of the variable.
	 */
	public IntegerVariable(String name, double lowerBound, double upperBound){
		super(name,lowerBound,upperBound);
	}
		
	/* (non-Javadoc)
	 * @see DeLP_GDPR.math.term.Term#isInteger()
	 */
	@Override
	public boolean isInteger(){
		return true;
	}
}
