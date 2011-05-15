package piramide.interaction.reasoner.db.decay;

public class LogarithmicDecay implements IDecayFunction {
	
	private final int logTarget = 5 * 12; //we don't take into account trends older than 5 years
	
	@Override
	public double getDecay(int monthsPassed) {
		double logResult = Math.log10(monthsPassed)/Math.log10(logTarget);
		if (logResult < 0){
			logResult = 0;
		}		
		double decay = 1- logResult;
		
		if (decay < 0) {
			decay = 0;
		}	
		
		return decay;			
	}

}
