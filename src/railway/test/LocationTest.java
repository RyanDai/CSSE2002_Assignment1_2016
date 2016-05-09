package railway.test;

import railway.*;

import org.junit.Assert;
import org.junit.Test;

/**
 * Basic tests for the {@link Location} implementation class.
 * 
 * A more extensive test suite will be performed for assessment of your code,
 * but this should get you started writing your own unit tests.
 */
public class LocationTest {

    /** Test construction of a typical location that is not at a junction */
    @Test
    public void testTypicalLocation() {
        // parameters used to construct the location under test
        int length = 9;
        JunctionBranch endPoint1 =
                new JunctionBranch(new Junction("j1"), Branch.FACING);
        JunctionBranch endPoint2 =
                new JunctionBranch(new Junction("j2"), Branch.NORMAL);

        Section section = new Section(length, endPoint1, endPoint2);
        JunctionBranch endPoint = endPoint1;
        int offset = 3;

        // the location under test
        Location location = new Location(section, endPoint, offset);

        // check the section, end-point and offset of the location
        Assert.assertEquals(section, location.getSection());
        Assert.assertEquals(endPoint, location.getEndPoint());
        Assert.assertEquals(offset, location.getOffset());

        // check whether or not this location is at a junction
        Assert.assertFalse(location.atAJunction());

        // the location is not at a junction: test that the location is on a
        // section equivalent to location.getSection()
        Section equivalentSection = new Section(length, endPoint1, endPoint2);
        Assert.assertTrue(location.onSection(equivalentSection));

        // the location is not at a junction: check that it is not on some other
        // section
        Section anotherSection =
                new Section(10, new JunctionBranch(new Junction("j1"),
                        Branch.NORMAL), new JunctionBranch(new Junction("j3"),
                        Branch.FACING));
        Assert.assertFalse(location.onSection(anotherSection));

        // check that the string representation is correct
        String actualString = location.toString();
        String expectedString = "Distance 3 from j1 along the FACING branch";
        Assert.assertEquals(expectedString, actualString);

        // check that the class invariant has been established.
        Assert.assertTrue("Invariant incorrect", location.checkInvariant());
    }

    /**
     * Check that the appropriate exception is thrown if a location is created
     * with a null end-point.
     */
    @Test(expected = NullPointerException.class)
    public void testNullEndPoint() {
        // parameters used to construct the location under test
        int length = 9;
        JunctionBranch endPoint1 =
                new JunctionBranch(new Junction("j1"), Branch.FACING);
        JunctionBranch endPoint2 =
                new JunctionBranch(new Junction("j2"), Branch.NORMAL);

        Section section = new Section(length, endPoint1, endPoint2);
        JunctionBranch endPoint = null;
        int offset = 3;

        // the location under test
        Location location = new Location(section, endPoint, offset);
    }

    /** Basic check of the equals method */
    @Test
    public void testEquals() {

        // parameters used to construct the locations under test
        JunctionBranch endPoint1 =
                new JunctionBranch(new Junction("j1"), Branch.FACING);
        JunctionBranch endPoint2 =
                new JunctionBranch(new Junction("j2"), Branch.NORMAL);
        JunctionBranch endPoint3 =
                new JunctionBranch(new Junction("j1"), Branch.REVERSE);
        JunctionBranch endPoint4 =
                new JunctionBranch(new Junction("j3"), Branch.FACING);

        Section section1 = new Section(9, endPoint1, endPoint2);
        Section section2 = new Section(12, endPoint3, endPoint4);

        // equal case: the locations are both at the same junction
        Location location1 = new Location(section1, endPoint1, 0);
        Location location2 = new Location(section2, endPoint3, 0);
        Assert.assertEquals(location1, location2);

        // equal case: the locations are not at a junction: but they have
        // equivalent end-points and non-zero offsets.
        location1 = new Location(section1, endPoint1, 3);
        location2 =
                new Location(section1, new JunctionBranch(new Junction("j1"),
                        Branch.FACING), 3);
        Assert.assertEquals(location1, location2);

        // equal case: the locations are not at a junction: they are the same
        // location described from opposite end-points of the same section.
        location1 = new Location(section1, endPoint1, 3);
        location2 = new Location(section1, endPoint2, 6);
        Assert.assertEquals(location1, location2);
        Assert.assertEquals(location1.hashCode(), location2.hashCode());

        // unequal case: basic case
        location1 = new Location(section1, endPoint1, 3);
        location2 = new Location(section1, endPoint2, 4);
        Assert.assertNotEquals(location1, location2);

    }

}
