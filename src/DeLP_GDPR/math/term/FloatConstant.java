package DeLP_GDPR.math.term;



/**
 * This class encapsulates a float as a term.
 */
public class FloatConstant extends Constant {
	
	/**
	 * the actual float.
	 */	
	private double f;
	
	/**
	 * Creates a new Float.
	 * @param f a float.
	 */
	public FloatConstant(float f){
		this.f = f;
	}
	
	/**
	 * Creates a new Float.
	 * @param f a double.
	 */
	public FloatConstant(double f){
		this.f = (float)f;
	}
	
	/**
	 * Get the value of this float.
	 * @return the value of this float.
	 */
	public double getValue(){
		return this.f;
	}
	
	/* (non-Javadoc)
	 * @see DeLP_GDPR.math.term.Term#isInteger()
	 */
	@Override
	public boolean isInteger(){
		return false;
	}
	
	/* (non-Javadoc)
	 * @see DeLP_GDPR.math.term.Term#toString()
	 */
	@Override
	public String toString(){
		return String.valueOf(this.f);
	}
}
