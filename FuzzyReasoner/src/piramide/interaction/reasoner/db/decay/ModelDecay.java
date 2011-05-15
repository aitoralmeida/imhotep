package piramide.interaction.reasoner.db.decay;

public class ModelDecay implements IDecayFunction {

	@Override
	public double getDecay(int monthsPassed) {
		double decay;
		if (monthsPassed <= 15){  //1st year
			decay = 1.0;
		} else if (monthsPassed > 15 & monthsPassed <= 24){ // 2nd sear
			decay = 0.9;
		} else if (monthsPassed > 24 & monthsPassed <= 36){ //3rd year
			decay = 0.4;
		} else if (monthsPassed > 36 & monthsPassed <= 60){ //4rd and 5th year
			decay = 0.1;
		}else{ //the rest
			decay = 0.05;
		}
		return decay;
	}

}
