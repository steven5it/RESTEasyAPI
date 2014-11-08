package assign.resources;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="project")
@XmlAccessorType(XmlAccessType.FIELD)
public class ProjectDetails {

	@XmlElement(name="name")
	private String name;
	
	@XmlElement(name = "description")
	private String description;
	
	@XmlElement(name="meetings")
	private Meetings meetings;
//	private ArrayList<Meeting> meetings;
	
	
	public ProjectDetails() {}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public Meetings getMeetings() {
		return meetings;
	}
	public void setMeetings(Meetings meetings) {
		this.meetings = meetings;
	}
}
