package assign.resources;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="project")
@XmlAccessorType(XmlAccessType.FIELD)
public class Project {

	@XmlAttribute
	private String name;
	
	@XmlElement(name = "link")
	private ArrayList<String> links;
	
	public Project() {}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public ArrayList<String> getLinks() {
		return links;
	}
	public void setLinks(ArrayList<String> links) {
		this.links = links;
	}
	
	public void addLink(String link) {
		if (!links.contains(link)) {
			links.add(link);
		}
	}
}
