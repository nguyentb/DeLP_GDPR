package DeLP_GDPR.logics.pl.syntax;




import java.util.Collection;

import org.tweetyproject.commons.Formula;
import org.tweetyproject.commons.Signature;
import org.tweetyproject.commons.SingleSetSignature;

/**
 * This class captures the signature of a specific propositional language.
 */
public class PlSignature extends SingleSetSignature<Proposition> {

	/**
	 * Creates a new (empty) propositional signature.
	 */
	public PlSignature() {
		super();
	}

	/**
	 * Creates a new propositional signature with the given set of propositions.
	 * 
	 * @param propositions a set of propositions.
	 */
	public PlSignature(Collection<? extends Proposition> propositions) {
		this();
		formulas.addAll(propositions);
	}

	/**
	 * Creates a new propositional signature with the given number of propositions
	 * (named "A0"..."Anumvars".
	 * 
	 * @param numvars the number of variables.
	 */
	public PlSignature(int numvars) {
		this();
		for (int i = 0; i < numvars; i++)
			formulas.add(new Proposition("A" + i));
	}
	
	/**
	 * Creates a new propositional signature that is a copy of the given signature.
	 * @param sig a signature
	 */
	public PlSignature(PlSignature sig) {
		this(); 
		this.add(sig);
	}

	/**
	 * Returns the set of atoms appearing in the given collection of formulas.
	 * 
	 * @param formulas a set of propositional formulas
	 * @return the signature of the formulas.
	 */
	public static PlSignature getSignature(Collection<? extends PlFormula> formulas) {
		PlSignature signature = new PlSignature();
		for (PlFormula f : formulas)
			signature.addAll(f.getAtoms());
		return signature;
	}

	@Override
	public void add(Object obj) {
		if (obj instanceof Proposition)
			formulas.add((Proposition) obj);
		else if (obj instanceof PlFormula) 
			formulas.addAll(((PlFormula) obj).getAtoms());
		else if (obj instanceof PlSignature)
			this.addSignature((Signature) obj);
		else if (obj instanceof Formula)
			this.addSignature(((Formula) obj).getSignature());
		else
			throw new IllegalArgumentException("Unknown type " + obj.getClass());
	}

	@Override
	public PlSignature clone() {
		return new PlSignature(this.formulas);
	}

}
