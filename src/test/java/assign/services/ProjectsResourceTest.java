package assign.services;

import java.util.ArrayList;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

import org.junit.Before;
import org.junit.Test;

import assign.resources.Meeting;
import assign.resources.Project;
import assign.resources.ProjectDetails;
import assign.resources.Projects;
import junit.framework.TestCase;
import static org.mockito.Mockito.*;

public class ProjectsResourceTest extends TestCase {
	DBService dbService;

	@Before
	public void setUp() {
		dbService = new DBService();
	}

	@Test
	public void testListProjects() {
	}

	public void testListMeetingsLinks() {

	}

	public void testListIrclogsLinks() {

	}

	public void testCreateMarshaler() {

	}

	public void testIrclogsProjects() {

	}

	public void testPopulateLinks() {

	}

	public void testPopulateProjects() {

	}

	@Test
	public void testCreateProjectDetails() {
		Client client = ClientBuilder.newClient();
		try {
			System.out.println("*** Delete project ***");
			Response response = client.target("http://localhost:8080/myeavesdrop/projects/newProject")
					.request().delete();
			
			System.out.println("*** Create a new Project ***");

			String xml = "<project>" + "<name>newProject</name>"
					+ "<description>This is a new project</description>"
					+ "</project>";

			response = client
					.target("http://localhost:8080/myeavesdrop/projects")
					.request().post(Entity.xml(xml));

			assertEquals(201, response.getStatus());

			if (response.getStatus() != 201)
				throw new RuntimeException("Failed to create project");
			String location = response.getLocation().toString();
			System.out.println("Location: " + location);
			response.close();
			
			System.out.println("*** GET Created Project **");
			String project = client.target(location).request()
					.get(String.class);
			System.out.println(project);

			String updateProjectDetails = "<project>"
					+ "<description>This is an updated project</description>"
					+ "</project>";
			response = client.target(location).request()
					.put(Entity.xml(updateProjectDetails));
			if (response.getStatus() != 204)
				throw new RuntimeException("Failed to update");
			response.close();
			System.out.println("**** After Update ***");
			project = client.target(location).request().get(String.class);
			System.out.println(project);
			
			
			System.out.println("*** Create a new Meeting for project ***");

			String addMeetings = "<meeting>" + "<name>newMeeting</name>"
					+ "<link>http://www.newLink.com</link>" + "<year>1900</year>"
					+ "</meeting>";

			response = client
					.target("http://localhost:8080/myeavesdrop/projects/newProject/meetings")
					.request().post(Entity.xml(addMeetings));

			assertEquals(201, response.getStatus());

			if (response.getStatus() != 201)
				throw new RuntimeException("Failed to create project");
			location = response.getLocation().toString();
			System.out.println("Location: " + location);
			response.close();
			
			System.out.println("*** GET Created Project with Meeting **");
			String projectMeeting = client.target(location).request()
					.get(String.class);
			System.out.println(projectMeeting);
			 
		} finally {
			client.close();
		}
	}

	@Test
	public void testAddProjectDetails() {
		String name = "testAddName";
		String description = "testAddDescription";
		dbService.addProjectDetails(name, description);

		ProjectDetails projectDetails = dbService
				.getProjectDetails("testAddName");
		assertNotNull(projectDetails);
	}
	
	@Test
	public void testDeleteProjectDetailsByName() {
		   String name = "testDeleteName";
		   String description = "testDeleteDescription";
		   dbService.addProjectDetails(name, description);
		   
		   ProjectDetails projectDetails = dbService.getProjectDetails(name);
		   assertNotNull(projectDetails);
		   
		   dbService.deleteProjectDetailsByName(name);
		   
		   projectDetails = dbService.getProjectDetails(name);
		   assertNull(projectDetails);
	}
	
	@Test
	public void testUpdateProjectDescriptionByName() {
		dbService.deleteProjectDetailsByName("testUpdateName");
		
		String name = "testUpdateName";
		String description = "testUpdateDescriptionBefore";
		dbService.addProjectDetails(name, description);
		
		ProjectDetails projectDetails = dbService.getProjectDetails(name);
		assertNotNull(projectDetails);
		assertEquals(description, projectDetails.getDescription());
		
		String newDescription = "testUpdateDescriptionAfter";
		dbService.updateProjectDescriptionByName(name, newDescription);
		
		projectDetails = dbService.getProjectDetails(name);
		assertNotNull(projectDetails);
		assertEquals(newDescription, projectDetails.getDescription());
	}
	
	@Test
	public void testAddMeetingForProject() {
		dbService.deleteProjectDetailsByName("testMeetingAddProjectName");
		
		String projectName = "testMeetingAddProjectName";
		String description = "testMeetingAddDescription";
		dbService.addProjectDetails(projectName, description);
		ProjectDetails projectDetails = dbService.getProjectDetails(projectName);
		assertNotNull(projectDetails);
		
		String name = "testAddMeetingName";
		String link = "http://AddtestMeetingLink";
		int year = 2000;
		dbService.addMeetingForProject(projectName, name, link, year);
		
		ArrayList<Meeting> meetings = dbService.getMeetingsForProject(projectName);
		assertNotNull(meetings);
		assertEquals(meetings.get(0).getName(), name);
	}

}
