package piramide.interaction.reasoner.db.decay;

public interface IDecayFunction {
	
	double getDecay(int monthsPassed);
	int getActualYear();
	int getActualMonth();
	
}
