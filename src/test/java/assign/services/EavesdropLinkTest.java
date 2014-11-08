package assign.services;

import junit.framework.TestCase;

import org.junit.Test;

public class EavesdropLinkTest extends TestCase {
	private EavesdropLink link;
	private static String PATH_1 = "1/";
	private static String PATH_2 = "A/";
	private static String PATH_3 = "Z/";
	private static String TEST_LINK = "http://eavesdrop.openstack.org/1/A/Z/";
	private static String TEST_LINK_TO_STRING = "Link is: http://eavesdrop.openstack.org/1/A/Z/";
	
    public void setUp() throws Exception {
        link = new EavesdropLink();
    }
	
    public void testEavesdropLink() {
		link = new EavesdropLink(PATH_1, PATH_2, PATH_3);
        assertEquals(link.getLink(), TEST_LINK);
    }
	
	public void testCreateLink() {
		link.createLink("1/", "A/", "Z/");
		assertEquals(link.getLink(), TEST_LINK);
		
		String linkToString = link.toString();
		assertEquals(linkToString, TEST_LINK_TO_STRING);
	}

}
