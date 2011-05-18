package piramide.interaction.reasoner.db.decay;

import java.util.Calendar;

import piramide.interaction.reasoner.db.CalendarFactory;

public class LogarithmicDecay implements IDecayFunction {
	
	private final Calendar cal = CalendarFactory.now();  
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

	@Override
	public int getMaxMonth(){
		return Integer.MAX_VALUE;
	}

	@Override
	public int getActualYear(){
		return this.cal.get(Calendar.YEAR) - 1;
	}
	
	@Override
	public int getActualMonth(){
		return this.cal.get(Calendar.MONTH);
	}
}
