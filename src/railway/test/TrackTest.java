package railway.test;

import railway.*;
import java.util.*;
import org.junit.Assert;
import org.junit.Test;

/**
 * Basic tests for the {@link Track} implementation class.
 * 
 * A more extensive test suite will be performed for assessment of your code,
 * but this should get you started writing your own unit tests.
 */
public class TrackTest {

    /** Test the initial state of the line-up */
    @Test
    public void testInitialState() {

        // junctions that won't be on the track
        List<Junction> notJunctions = new ArrayList<>();
        notJunctions.add(new Junction("j0"));
        notJunctions.add(new Junction("j1"));

        // sections that won't be in the track
        List<Section> notSections = new ArrayList<>();
        notSections.add(new Section(9, new JunctionBranch(notJunctions.get(0),
                Branch.FACING), new JunctionBranch(notJunctions.get(1),
                Branch.FACING)));

        Track track = new Track(); // the track under test

        // check that the track does not contain a spurious section
        Assert.assertFalse(track.contains(notSections.get(0)));

        // check that the junctions are correct
        HashSet<Junction> expectedJunctions = new HashSet<>();
        Assert.assertEquals(expectedJunctions, track.getJunctions());

        // check that there is no section connected to a junction that is not
        // on the track.
        Section expectedSection = null;
        Assert.assertEquals(expectedSection,
                track.getTrackSection(notJunctions.get(0), Branch.FACING));

        // check the iterator
        Assert.assertFalse(track.iterator().hasNext());

        // check the string representation
        Assert.assertEquals("", track.toString());

        // check that the invariant has been established
        Assert.assertTrue(track.checkInvariant());
    }

    /** Test adding a null section to the track **/
    @Test(expected = NullPointerException.class)
    public void testNullAddition() {
        Track track = new Track(); // the track under test
        track.addSection(null);
    }

    /**
     * Test adding a section to a track that would result in the track becoming
     * invalid.
     **/
    @Test(expected = InvalidTrackException.class)
    public void testInvalidAddition() {
        // end-points and sections to test with

        // junctions, and sections to test with
        List<Junction> junctions = new ArrayList<>();
        junctions.add(new Junction("j0"));
        junctions.add(new Junction("j1"));
        junctions.add(new Junction("j2"));

        List<Section> sections = new ArrayList<>();
        sections.add(new Section(9, new JunctionBranch(junctions.get(0),
                Branch.FACING), new JunctionBranch(junctions.get(1),
                Branch.NORMAL)));
        sections.add(new Section(20, new JunctionBranch(junctions.get(2),
                Branch.REVERSE), new JunctionBranch(junctions.get(0),
                Branch.FACING)));

        Track track = new Track(); // the track under test
        for (Section section : sections) {
            track.addSection(section);
        }
    }

    /**
     * Test adding multiple sections to the track that do not result in the
     * track becoming invalid.
     **/
    @Test
    public void testValidAdditions() {
        // junctions that will be added to the track
        List<Junction> junctions = new ArrayList<>();
        junctions.add(new Junction("j0"));
        junctions.add(new Junction("j1"));
        junctions.add(new Junction("j2"));
        junctions.add(new Junction("j3"));
        junctions.add(new Junction("j4"));
        junctions.add(new Junction("j5"));
        junctions.add(new Junction("j6"));

        // junctions that won't be on the track
        List<Junction> notJunctions = new ArrayList<>();
        notJunctions.add(new Junction("j7"));

        // sections that will be added to the track
        List<Section> sections = new ArrayList<>();
        sections.add(new Section(9, new JunctionBranch(junctions.get(0),
                Branch.FACING), new JunctionBranch(junctions.get(1),
                Branch.FACING)));
        sections.add(new Section(20, new JunctionBranch(junctions.get(1),
                Branch.NORMAL), new JunctionBranch(junctions.get(2),
                Branch.FACING)));
        sections.add(new Section(6, new JunctionBranch(junctions.get(1),
                Branch.REVERSE), new JunctionBranch(junctions.get(3),
                Branch.NORMAL)));
        sections.add(new Section(80, new JunctionBranch(junctions.get(3),
                Branch.FACING), new JunctionBranch(junctions.get(4),
                Branch.NORMAL)));
        sections.add(new Section(10, new JunctionBranch(junctions.get(5),
                Branch.NORMAL), new JunctionBranch(junctions.get(6),
                Branch.REVERSE)));

        // sections that won't be in the track
        List<Section> notSections = new ArrayList<>();
        notSections.add(new Section(5, new JunctionBranch(junctions.get(0),
                Branch.NORMAL), new JunctionBranch(junctions.get(5),
                Branch.REVERSE)));

        Track track = new Track(); // the track under test
        for (Section section : sections) {
            track.addSection(section);
        }

        // check that the track contains all sections
        for (Section section : sections) {
            Assert.assertTrue(track.contains(section));
        }

        // check that the track does not contain a spurious section
        Assert.assertFalse(track.contains(notSections.get(0)));

        // check that the junctions are correct
        HashSet<Junction> expectedJunctions = new HashSet<>(junctions);
        Assert.assertEquals(expectedJunctions, track.getJunctions());

        // check that there is no section connected to a junction that is not
        // on the track.
        Assert.assertEquals(null,
                track.getTrackSection(notJunctions.get(0), Branch.FACING));

        // check that there is no section connected to a junction that is on the
        // track, but doesn't have a particular branch.
        Assert.assertEquals(null,
                track.getTrackSection(junctions.get(0), Branch.REVERSE));

        // check that the appropriate sections can be retrieved for all of the
        // junctions on the track
        for (Section section : sections) {
            for (JunctionBranch endPoint : section.getEndPoints()) {
                Assert.assertEquals(
                        section,
                        track.getTrackSection(endPoint.getJunction(),
                                endPoint.getBranch()));
            }
        }

        // check the iterator of the track
        Set<Section> actualSections = new HashSet<>();
        for (Section section : track) {
            Assert.assertFalse("Duplicate section detected",
                    actualSections.contains(section));
            actualSections.add(section);
        }
        Assert.assertEquals(new HashSet<Section>(sections), actualSections);

        // check that the invariant has been established
        Assert.assertTrue(track.checkInvariant());

    }
}
