package assign.resources;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="projects")
@XmlAccessorType(XmlAccessType.FIELD)
public class Projects {
	
	@XmlElement(name = "project")
	private ArrayList<String> projects;
	
	public ArrayList<String> getProjects() {
		return projects;
	}
	
	public void setProjects(ArrayList<String> projects) {
		this.projects = projects;
	}
	
	public void addProject(String project) {
		if (!projects.contains(project)) {
			projects.add(project);
		}
	}
}
