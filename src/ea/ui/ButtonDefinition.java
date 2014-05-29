package ea.ui;

public class ButtonDefinition
{
	public String normal;
	public String gedrueckt;
	
	public ButtonDefinition(String normal, String gedrueckt)
	{
		this.normal = normal;
		this.gedrueckt = gedrueckt;
	}
	
	public boolean istNichtNull()
	{
		return normal != "" && gedrueckt != "";
	}
}
