package railway;

import java.util.*;

/**
 * <p>
 * A mutable class representing the layout of a railway track.
 * </p>
 *
 * <p>
 * A railway track is made up of a number of sections. A junction is on the
 * track if and only if the junction is at one of the end-points of a section in
 * the track.
 * 
 * Each junction on the track has between one and three branches that connect it
 * to sections of the track, and it can have at most one branch of each type.
 * (I.e. a junction may have not have two or more branches of type
 * Branch.FACING.)
 * </p>
 *
 */
public class Track implements Iterable<Section> {

    private ArrayList<Section> trackList;
    private static final String newLine = System.getProperty("line.separator");

    /**
     * Creates a new track with no sections.
     */
    public Track() {
        ArrayList<Section> trackList = new ArrayList<Section>();
        this.trackList = trackList;
    }

    /**
     * <p>
     * Adds the given section to the track, unless the addition of the section
     * would result in the track becoming invalid.
     * </p>
     * 
     * <p>
     * If the section is null, then a NullPointerException is thrown and the
     * track is not modified. <br>
     * <br>
     * 
     * If the track already contains an equivalent section, then the track is
     * not modified by this operation.<br>
     * <br>
     * 
     * If the section is not null, and the track does not already contain an
     * equivalent section, but the addition of the section would result in one
     * of the track junctions being connected to more than one section on the
     * same branch, then an InvalidTrackException is thrown, and the track is
     * not modified.<br>
     * <br>
     * 
     * Otherwise, the section is added to the track.
     * </p>
     * 
     * @param section
     *            the section to be added to the track.
     * @throws NullPointerException
     *             if section is null
     * @throws InvalidTrackException
     *             if the track does not already contain an equivalent section,
     *             but it already contains a section that is connected to one of
     *             the same end-points as the given section. (Recall that a
     *             junction can only be connected to one section on a given
     *             branch.) Two end-points are the considered to be the same if
     *             they are equivalent according to the equals method of the
     *             JunctionBranch class.
     */
    public void addSection(Section section) throws NullPointerException,
            IllegalArgumentException {
        if(section == null){
        	throw new NullPointerException("section can not be null");
        }
        if(!(trackList.contains(section))){
        	trackList.add(section);
        }
        boolean duplicateFlag = false;
        ArrayList<JunctionBranch> testDuplicate = new ArrayList<JunctionBranch>();
        for(int i = 0; i < trackList.size(); i++){
        	for(JunctionBranch endPoint : trackList.get(i).getEndPoints()){
        		if(testDuplicate.contains(endPoint)){
        			duplicateFlag = true;
        			break;
        		} else {
        			testDuplicate.add(endPoint);
        		}
        	}
        }
        if(duplicateFlag == true){
        	trackList.remove(section);
        	throw new InvalidTrackException("duplicate endPoints");
        }
    }

    /**
     * If the track contains a section that is equivalent to this one, then it
     * is removed from the layout of the railway, otherwise this method does not
     * alter the railway layout in any way.
     * 
     * @param section
     *            the section to be removed from the track
     */
    public void removeSection(Section section) {
        if(trackList.contains(section)){
        	trackList.remove(section);
        }
    }

    /**
     * Returns true if the track contains the given section and false otherwise.
     * 
     * @param section
     *            the section whose presence in the track is to be checked
     * @return true iff the track contains a section that is equivalent to the
     *         given parameter.
     */
    public boolean contains(Section section) {
        if(trackList.contains(section)){
        	return true;
        }
        return false;
    }

    /**
     * Returns a set of all the junctions in the track that are connected to at
     * least one section of the track.
     * 
     * @return The set of junctions in the track.
     */
    public Set<Junction> getJunctions() {
        HashSet<Junction> junctions = new HashSet<Junction>();
        for(int i = 0; i < trackList.size(); i++){
        	for(JunctionBranch endPoint : trackList.get(i).getEndPoints()){
        		junctions.add(endPoint.getJunction());
        	}
        }
        return junctions;
        
    }

    /**
     * If the track contains a section that is connected to the given junction
     * on the given branch, then it returns that section, otherwise it returns
     * null.
     * 
     * @param junction
     *            the junction for which the section will be returned
     * @param branch
     *            the branch of the junction for which the section will be
     *            returned
     * @return the section of track that is connected to the junction on the
     *         given branch, if there is one, otherwise null
     */
    public Section getTrackSection(Junction junction, Branch branch) {
    	for(int i = 0; i < trackList.size(); i++){
    		for(JunctionBranch endPoint : trackList.get(i).getEndPoints()){
        		if(endPoint.getJunction().equals(junction) && endPoint.getBranch().equals(branch)){
        			return trackList.get(i);
        		}
        	}
    	}
        return null; 
    }

    /**
     * Returns an iterator over the sections in the track. (The iterator can
     * return the sections on the track in any order.)
     */
    @Override
    public Iterator<Section> iterator() {
        return trackList.iterator();
    }

    /**
     * The string representation of a track contains a line-separated
     * concatenation of the string representations of the sections that make up
     * the track. The sections can appear in any order.
     * 
     * The line separator string used to separate the sections should be
     * retrieved in a machine-independent way by calling the function
     * System.getProperty("line.separator").
     */
    @Override
    public String toString() {
    	String result = "";
    	if(trackList.size() == 0){
    		return "";
    	}
    	else if(trackList.size() == 1){
    		return trackList.get(0).toString();
    	} else{
    		result += trackList.get(0).toString();
    		for(int i = 1; i < trackList.size(); i++){
    			result += newLine;
    			result += trackList.get(i).toString();
    		}
    	}
        return result; 
    }

    /**
     * Determines whether this class is internally consistent (i.e. it satisfies
     * its class invariant).
     * 
     * This method is only intended for testing purposes.
     * 
     * @return true if this class is internally consistent, and false otherwise.
     */
    public boolean checkInvariant() {
        if(trackList == null){
        	return false;
        }
        if(trackList.contains(null)){
        	return false;
        }
        return true;
    }

}
