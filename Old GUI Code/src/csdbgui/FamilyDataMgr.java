package csdbgui;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import csdbvo.SubjectEntry;

public class FamilyDataMgr {
	private String file;
	
	private List<SubjectEntry> allSubjects = new ArrayList<SubjectEntry>();
	private Map<Integer, List<SubjectEntry>> subsByGen = 
			new TreeMap<Integer, List<SubjectEntry>>();
	private Map<SubjectEntry, List<SubjectEntry>> momKids =
			new HashMap<SubjectEntry, List<SubjectEntry>>();
	private SubjectEntry founder;
	
	public FamilyDataMgr(String file) {
		this.file = file;
		//this.allSubjects = SubjectEntry.loadEntriesFromFile(file);
		
		this.allSubjects = SubjectEntry.loadEntriesFromDB(file);
		
		// add founder to the map
		momKids.put(founder, new ArrayList<SubjectEntry>());
		Map<String, SubjectEntry> potentialMothersCurGen = 
				new HashMap<String, SubjectEntry>();
		int curGen = 0;
		subsByGen.put(curGen, new ArrayList<SubjectEntry>());
		for (SubjectEntry se : allSubjects) {
			if (se.getGeneration() == curGen)
				;//subsByGen.get(curGen).add(se);
			else {
				curGen++;
				subsByGen.put(curGen, new ArrayList<SubjectEntry>());				
			}
			subsByGen.get(curGen).add(se);
		}
	}

		/*
		SubjectEntry curMom = founder;
		
		for (SubjectEntry se : allSubjects) {
			if (se.getGeneration() > curGen) {
				curGen++;
				potentialMothersCurGen = new HashMap<String, SubjectEntry>();
			}
			
			if (se.getGender() == 'F')
				potentialMothersCurGen.put(se.getOriginalId(), se);
			
			if (se.getMotherID().equals(curMom.getOriginalId()))
				momKids.get(curMom).add(se);
			else {
				curMom = potentialMothersCurGen.get(se.getMotherID());
				momKids.put(curMom).add(se);
			}
		}
		*/

	public static void main(String[] args) {
		FamilyDataMgr mgr = new FamilyDataMgr("CSRhesusSubject");
		//mgr.Setfounder(new SubjectEntry("22", 1));

		//System.out.println(mgr.momKids);
		for (Integer i : mgr.subsByGen.keySet())
			System.out.println(mgr.subsByGen.get(i));
	}

	private void Setfounder(SubjectEntry subjectEntry) {
		founder = subjectEntry;
	}

	public Map<Integer, List<SubjectEntry>> getSubjectsByGen() {
		// TODO Auto-generated method stub
		return subsByGen;
	}

}
