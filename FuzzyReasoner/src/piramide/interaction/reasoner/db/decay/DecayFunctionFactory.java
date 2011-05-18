package piramide.interaction.reasoner.db.decay;

import java.util.Calendar;


public class DecayFunctionFactory {
	
	public static enum DecayFunctions{
		model,
		logarithmic
	}
	/*
	public IDecayFunction create(DecayFunctions function){
		switch(function){
			case model: return new ModelDecay();
			case logarithmic: return new LogarithmicDecay();
		}
		throw new IllegalArgumentException("decay function not supported: " + function);
	}
	*/
	
	public IDecayFunction create(DecayFunctions function, Calendar when){
		switch(function){
		case model: return new ModelDecay(when);
		case logarithmic: return new LogarithmicDecay(when);
	}
	throw new IllegalArgumentException("decay function not supported: " + function);
	}
	
}
