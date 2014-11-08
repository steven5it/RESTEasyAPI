package assign.resources;

import javax.xml.bind.annotation.*;
@XmlRootElement(name="meeting")
@XmlAccessorType(XmlAccessType.FIELD)
public class Meeting {
	@XmlElement(name="name")
	private String name;
	
	@XmlElement(name="link")
	private String link;
	
	@XmlElement(name="year")
	private int year;
	
	public Meeting() {}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	
}
