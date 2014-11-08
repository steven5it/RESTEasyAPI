package assign.services;


public class EavesdropLink 
{
	private String link;
	
	public EavesdropLink ()
	{
		link = null;
	}
	
	public EavesdropLink (String type, String project, String year)
	{
		createLink(type, project, year);
	}

	protected void createLink(String type, String project, String year) {
		link = "http://eavesdrop.openstack.org/" + type + project + year;
	}
	
	public String getLink()
	{
		return link;
	}
	
	public String toString()
	{
		return "Link is: " + link;
	}
}
