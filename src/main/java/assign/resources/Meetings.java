package assign.resources;

import java.util.ArrayList;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="meetings")
@XmlAccessorType(XmlAccessType.FIELD)
public class Meetings {
	@XmlElement(name="meeting")
	private ArrayList<Meeting> meetingsList;
	
	public Meetings() {}
	public ArrayList<Meeting> getMeetingsList() {
		return meetingsList;
	}
	public void setMeetingsList(ArrayList<Meeting> meetingsList) {
		this.meetingsList = meetingsList;
	}

}
