package assign.resources;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

public class ProjectTest extends TestCase{
	private Project project;
	private ArrayList<String> linksTest;
	
	private static int LINKS_COUNT = 2;
	private static int LINKS_ADD_COUNT = 4;
	private static String LINK_1 = "www.test.net";
	private static String LINK_2 = "www.example.com";
	private static String LINK_3 = "www.history3.com";
	private static String LINK_4 = "www.history4.com";
	
    public void setUp() throws Exception {
    	project = new Project();
        linksTest = new ArrayList<String>();
        linksTest.add(LINK_1);
        linksTest.add(LINK_2);
        project.setLinks(linksTest);
    }
	
    public void testGetLinksWithNoLinks() {
        project = new Project();
        assertNull(project.getLinks());
    }
    
    public void testGetHistory() {
    	List<String> links = project.getLinks();
    	assertNotNull(links);
    	assertEquals(LINKS_COUNT, project.getLinks().size());
    	
    	String link_1 = links.get(0);
    	assertEquals(LINK_1, link_1);
    	
    	String link_2 = links.get(1);
    	assertEquals(LINK_2, link_2);
    }
    
    public void testAddHistory() {
    	project.addLink(LINK_3);
    	project.addLink(LINK_4);
    	
    	List<String> links = project.getLinks();
    	assertEquals(LINKS_ADD_COUNT, project.getLinks().size());

    	String link_3 = links.get(2);
    	assertEquals(LINK_3, link_3);
    	
      	String link_4 = links.get(3);
    	assertEquals(LINK_4, link_4);
    }
}