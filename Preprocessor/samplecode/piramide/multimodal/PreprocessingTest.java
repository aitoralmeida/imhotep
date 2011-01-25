import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Display;

public static class PreprocessingTest {
	
	//#if ${piramide.devices.height} < 300
	public static void CreateGUI(){
		Form form = new Form("Preprocessor Sample");
		exitCommand = new Command("Exit", Command.EXIT, 1);

		form.append("height is NOT greater than 300!");

		form.setCommandListener(this);
		form.addCommand(exitCommand);
		Display.getDisplay(this).setCurrent(form);
	}
	//#endif
	
	
	//#if ${piramide.devices.height} > 300
	public static void CreateGUI(){
		Form form = new Form("Preprocessor Sample");
		exitCommand = new Command("Exit", Command.EXIT, 1);

		form.append("height is greater than 300!");

		form.setCommandListener(this);
		form.addCommand(exitCommand);
		Display.getDisplay(this).setCurrent(form);
	}
	//#endif
	
	
}
