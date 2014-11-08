package assign.resources;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

public class ProjectsTest extends TestCase {
	private Projects projects;
	private ArrayList<String> projectsTest;
	
	private static int PROJECTS_COUNT = 2;
	private static int PROJECTS_ADD_COUNT = 4;
	private static String PROJECT_1 = "test";
	private static String PROJECT_2 = "example";
	private static String PROJECT_3 = "history";
	private static String PROJECT_4 = "history2";
	
    public void setUp() throws Exception {
    	projects = new Projects();
        projectsTest = new ArrayList<String>();
        projectsTest.add(PROJECT_1);
        projectsTest.add(PROJECT_2);
        projects.setProjects(projectsTest);
    }
	
    public void testGetProjectsWithNoProjects() {
        projects = new Projects();
        assertNull(projects.getProjects());
    }
    
    public void testGetProjects() {
    	List<String> projectsTest = projects.getProjects();
    	assertNotNull(projects);
    	assertEquals(PROJECTS_COUNT, projects.getProjects().size());
    	
    	String project_1 = projectsTest.get(0);
    	assertEquals(PROJECT_1, project_1);
    	
    	String project_2 = projectsTest.get(1);
    	assertEquals(PROJECT_2, project_2);
    }
    
    public void testAddProjects() {
    	projects.addProject(PROJECT_3);
    	projects.addProject(PROJECT_4);
    	
    	List<String> projectList = projects.getProjects();
    	assertEquals(PROJECTS_ADD_COUNT, projects.getProjects().size());

    	String project_3 = projectList.get(2);
    	assertEquals(PROJECT_3, project_3);
    	
      	String project_4 = projectList.get(3);
    	assertEquals(PROJECT_4, project_4);
    }
}