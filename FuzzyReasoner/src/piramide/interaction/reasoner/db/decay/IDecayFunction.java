package piramide.interaction.reasoner.db.decay;

public interface IDecayFunction {
	
	double getDecay(int monthsPassed);
	int getMaxMonth();
	int getActualYear();
	int getActualMonth();
	
}
