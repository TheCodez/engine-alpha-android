package ea.ui;

public class ButtonDefinition
{
	public String normal;
	public String gedrueckt;
	public String text;
	
	public ButtonDefinition(String normal, String gedrueckt, String text)
	{
		this.normal = normal;
		this.gedrueckt = gedrueckt;
		this.text = text;
	}
	
	public ButtonDefinition(String normal, String gedrueckt)
	{
		this(normal, gedrueckt, "");
	}
	
	public boolean istNichtNull()
	{
		return normal != "" && gedrueckt != "";
	}
}
