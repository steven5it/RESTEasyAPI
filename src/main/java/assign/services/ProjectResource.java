package assign.services;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.URI;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import assign.resources.Meeting;
import assign.resources.Meetings;
import assign.resources.Project;
import assign.resources.ProjectDetails;
import assign.resources.Projects;

@Path("/myeavesdrop")
public class ProjectResource {
	protected static Log logger = LogFactory.getLog(ProjectResource.class
			.getName());

	private Projects projects = new Projects();
	private Project project = new Project();
	private EavesdropLink eavesdropLink = new EavesdropLink();
	private DBService dbs = new DBService();

	public void setProjects(Projects projects) {
		this.projects = projects;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public void setEavesdropLink(EavesdropLink link) {
		this.eavesdropLink = link;
	}

	@GET
	@Path("/projects")
	@Produces("application/xml")
	public StreamingOutput listProjects() throws JAXBException {
		ArrayList<String> projectsList = new ArrayList<String>();
		projects.setProjects(projectsList);

		eavesdropLink.createLink("meetings/", "", "");
		populateProjects(eavesdropLink);
		eavesdropLink.createLink("irclogs/", "", "");
		populateProjects(eavesdropLink);

		JAXBContext context = JAXBContext.newInstance(Projects.class);
		Marshaller m = createMarshaller(context);

		return new StreamingOutput() {
			public void write(OutputStream outputStream) {
				try {
					m.marshal(projects, outputStream);
				} catch (JAXBException e) {
					e.printStackTrace();
				}
			}
		};
	}

	@GET
	@Path("/projects/{project}/meetings/{year : \\d+}")
	@Produces("application/xml")
	public StreamingOutput listMeetingsLinks(
			@PathParam("project") String projectName,
			@PathParam("year") int year) throws JAXBException {

		eavesdropLink.createLink("meetings/", projectName + "/",
				String.valueOf(year));
		logger.info("link is: " + eavesdropLink);
		project.setName(projectName);

		populateLinks(eavesdropLink);

		JAXBContext context = JAXBContext.newInstance(Project.class);
		Marshaller m = createMarshaller(context);

		return new StreamingOutput() {
			public void write(OutputStream outputStream) {
				try {
					m.marshal(project, outputStream);
				} catch (JAXBException e) {
					e.printStackTrace();
				}
			}
		};
	}

	@GET
	@Path("/projects/{project}/irclogs")
	@Produces("application/xml")
	public StreamingOutput listIrclogsLinks(
			@PathParam("project") String projectName) throws JAXBException {

		String irclogsProject = irclogsProject(projectName);
		eavesdropLink.createLink("irclogs/", irclogsProject, "");
		logger.info("link is: " + eavesdropLink);
		project.setName(projectName);

		populateLinks(eavesdropLink);

		JAXBContext context = JAXBContext.newInstance(Project.class);
		Marshaller m = createMarshaller(context);

		return new StreamingOutput() {
			public void write(OutputStream outputStream) {
				try {
					m.marshal(project, outputStream);
				} catch (JAXBException e) {
					e.printStackTrace();
				}
			}
		};
	}

	@POST
	@Path("/projects")
	@Consumes("application/xml")
	public Response createProject(InputStream is) {
		logger.info("creating a new project");
		ProjectDetails projectDetails = readProjectDetails(is);
		dbs.addProjectDetails(projectDetails.getName(), projectDetails.getDescription());
		return Response.created(URI.create("/myeavesdrop/projects/" + projectDetails.getName())).build();
	}

	@PUT
	@Path("/projects/{project}")
	@Consumes("application/xml")
	public void updateProject(@PathParam("project") String projectName, InputStream is) {
		logger.info("updating project: " + projectName);
		ProjectDetails update = readProjectDetails(is);
		update.setName(projectName);
		ProjectDetails current = dbs.getProjectDetails(update.getName());
		if (current == null)
			throw new WebApplicationException(Response.Status.NOT_FOUND);

		dbs.updateProjectDescriptionByName(update.getName(), update.getDescription());
	}

	@POST
	@Path("/projects/{project}/meetings")
	@Consumes("application/xml")
	public Response createMeeting(@PathParam("project") String projectName, InputStream is) {
		logger.info("creating project: " + projectName);
		Meeting meeting = readMeeting(is);
		dbs.addMeetingForProject(projectName, meeting.getName(), meeting.getLink(), meeting.getYear());
		return Response.created(URI.create("/myeavesdrop/projects/" + projectName)).build();
	}
	
	@GET
	@Path("/projects/{project}")
	@Produces("application/xml")
	public StreamingOutput listProjectDetails(
			@PathParam("project") String projectName) throws JAXBException, SQLException {

		logger.info("Retrieving project details");
		ProjectDetails projectDetails = dbs.getProjectDetails(projectName);
		if (projectDetails == null) {
			logger.warn("Attempt to GET project that does not exist");
			return null;
		}
		logger.info("ProjectDetails name is: " + projectDetails.getName()
				+ ", description is: " + projectDetails.getDescription());
		ArrayList<Meeting> meetingsList = dbs.getMeetingsForProject(projectName);
		Meetings meetings = new Meetings();
		meetings.setMeetingsList(meetingsList);
		if (meetingsList != null) {
			projectDetails.setMeetings(meetings);
		}
		
		
		JAXBContext context = JAXBContext.newInstance(ProjectDetails.class);
		Marshaller m = createMarshaller(context);

		return new StreamingOutput() {
			public void write(OutputStream outputStream) {
				try {
					m.marshal(projectDetails, outputStream);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
	}

	@DELETE
	@Path("/projects/{project}")
	public void deleteProject(@PathParam("project") String projectName) {
		logger.info("deleting project: " + projectName);
		dbs.deleteProjectDetailsByName(projectName);
	}

	protected Marshaller createMarshaller(JAXBContext context)
			throws JAXBException {
		Marshaller marshaller = context.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
		return marshaller;
	}

	protected String irclogsProject(String project) {
		return "%23" + project + "/";
	}

	private void populateLinks(EavesdropLink eavesdropLink) {
		ArrayList<String> linksList = new ArrayList<String>();
		try {
			org.jsoup.nodes.Document doc = Jsoup.connect(
					eavesdropLink.getLink()).get();
			Elements links = doc.select("a[href]");
			int i = 0;
			for (org.jsoup.nodes.Element link : links) {
				if (i++ < 5)
					continue;
				linksList.add(link.attr("abs:href"));
				logger.info("Page link is: " + link.attr("abs:href"));
			}
			project.setLinks(linksList);
		} catch (IOException e) {
			logger.error("IO Exception error connection Jsoup to document");
			e.printStackTrace();
		}
	}

	private void populateProjects(EavesdropLink eavesdropLink) {

		try {
			org.jsoup.nodes.Document doc = Jsoup.connect(
					eavesdropLink.getLink()).get();
			Elements projectNames = doc.select("a[href]");
			int i = 0;
			for (org.jsoup.nodes.Element project : projectNames) {
				if (i++ < 5)
					continue;
				int length = project.text().length();
				String projectString = project.text().substring(0, length - 1);
				projects.addProject(projectString);
				logger.info("Project is: " + projectString);
			}
		} catch (IOException e) {
			logger.error("IO Exception error connection Jsoup to document");
			e.printStackTrace();
		}
	}

	public ProjectDetails readProjectDetails(InputStream is) {
		try {
			DocumentBuilder builder = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder();
			Document doc = builder.parse(is);
			Element root = doc.getDocumentElement();
			ProjectDetails projectDetails = new ProjectDetails();
			NodeList nodes = root.getChildNodes();
			for (int i = 0; i < nodes.getLength(); i++) {
				Element element = (Element) nodes.item(i);
				if (element.getTagName().equals("name")) {
					projectDetails.setName(element.getTextContent());
				} else if (element.getTagName().equals("description")) {
					projectDetails.setDescription(element.getTextContent());
				}
			}
			return projectDetails;
		} catch (Exception e) {
			throw new WebApplicationException(e, Response.Status.BAD_REQUEST);
		}
	}
	
	public Meeting readMeeting(InputStream is) {
		try {
			DocumentBuilder builder = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder();
			Document doc = builder.parse(is);
			Element root = doc.getDocumentElement();
			Meeting meeting = new Meeting();
			NodeList nodes = root.getChildNodes();
			for (int i = 0; i < nodes.getLength(); i++) {
				Element element = (Element) nodes.item(i);
				if (element.getTagName().equals("name")) {
					meeting.setName(element.getTextContent());
				} else if (element.getTagName().equals("link")) {
					meeting.setLink(element.getTextContent());
				} else if (element.getTagName().equals("year")) {
					int year = Integer.parseInt(element.getTextContent());
					meeting.setYear(year);
				}
			}
			return meeting;
		} catch (Exception e) {
			throw new WebApplicationException(e, Response.Status.BAD_REQUEST);
		}
	}

}
