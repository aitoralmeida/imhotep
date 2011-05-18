import java.io.File;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;

import piramide.interaction.reasoner.FuzzyInferredResult;
import piramide.interaction.reasoner.FuzzyReasonerWizardFacade;
import piramide.interaction.reasoner.Geolocation;
import piramide.interaction.reasoner.RegionDistributionInfo;
import piramide.interaction.reasoner.creator.WarningStore;
import piramide.interaction.reasoner.db.decay.DecayFunctionFactory.DecayFunctions;


public class GeneratingGraphics {
	
	private static final String SMALL = "small";
	private static final String NORMAL = "normal";
	private static final String BIG = "big";
	
	public static void main(String [] args) throws Exception {
		
		final FuzzyReasonerWizardFacade facade = new FuzzyReasonerWizardFacade();
		final RegionDistributionInfo [] regions = new RegionDistributionInfo[]{new RegionDistributionInfo(SMALL, 1 / 2.0), new RegionDistributionInfo(NORMAL, 1 / 2.0), new RegionDistributionInfo(BIG, 1 /  2.0)};
		final WarningStore warningStore = new WarningStore();
		final String deviceName = "nokia 6630";
		final Geolocation geo = Geolocation.ALL;
		final Map<String, RegionDistributionInfo[]> inputVariables = new HashMap<String, RegionDistributionInfo[]>();
		inputVariables.put("reso_size", regions);
		final Map<String, RegionDistributionInfo[]> outputVariables = new HashMap<String, RegionDistributionInfo[]>();
		outputVariables.put("bar", regions);
		
		final Map<Month, MonthInformation> information = new HashMap<Month, MonthInformation>();
		
		boolean finished = false;
		
		for(int year = 2004; year < 2012 && !finished; ++year){
			for(int month = 0; month < 12 && !finished; ++month){
				final Calendar when = Calendar.getInstance();
				when.set(Calendar.YEAR, year);
				when.set(Calendar.MONTH, month);
				
				if(when.after(Calendar.getInstance())){
					finished = true;
					break;
				}
		
				final FuzzyInferredResult result;
				try{
					result = facade.getInferredValues(deviceName, warningStore, new HashMap<String, Object>(), inputVariables, geo, DecayFunctions.model, when, outputVariables, "");
				}catch(IllegalArgumentException e){
					System.err.println(month);
					System.err.println(year);
					e.printStackTrace();
					continue;
				}
				System.out.println("got the results");
				final Map<String, LinkedHashMap<String, Double>> values = result.getValues();
				final LinkedHashMap<String, Double> defuzzified = values.get("reso_size");
				final MonthInformation monthInformation = new MonthInformation(defuzzified.get(SMALL), defuzzified.get(NORMAL), defuzzified.get(BIG));
				
				Month omonth = new Month(year, month);
				System.out.println("when: " + omonth);
				System.out.println("month information: " + monthInformation);
				
				information.put(omonth, monthInformation);
			}
		}
		
		ObjectMapper mapper = new ObjectMapper();
		mapper.writeValue(new File("dump.json"), information);
		System.out.println("Finished");
	}
}
