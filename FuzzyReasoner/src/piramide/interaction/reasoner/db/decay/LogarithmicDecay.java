package piramide.interaction.reasoner.db.decay;

import java.util.Calendar;

import piramide.interaction.reasoner.db.CalendarFactory;

public class LogarithmicDecay implements IDecayFunction {
	
	private final Calendar when;  
	private final int logTarget = 5 * 12; //we don't take into account trends older than 5 years
	
	public LogarithmicDecay(Calendar when){
		this.when = when;
	}
	
	public LogarithmicDecay(){
		this(CalendarFactory.now());
	}
	
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
	public int getActualYear(){
		return this.when.get(Calendar.YEAR) - 1;
	}
	
	@Override
	public int getActualMonth(){
		return this.when.get(Calendar.MONTH);
	}
}
