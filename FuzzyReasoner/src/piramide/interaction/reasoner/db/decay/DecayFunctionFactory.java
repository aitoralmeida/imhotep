package piramide.interaction.reasoner.db.decay;


public class DecayFunctionFactory {
	
	public static enum DecayFunctions{
		model,
		logarithmic
	}
	
	public IDecayFunction create(DecayFunctions function){
		switch(function){
			case model: return new ModelDecay();
			case logarithmic: return new LogarithmicDecay();
		}
		throw new IllegalArgumentException("decay function not supported: " + function);
	}

}
