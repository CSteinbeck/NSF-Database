package csdbvo;
import java.io.File;
import java.io.FileNotFoundException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import csdbdao.SqlServerDbAccessor;

public class SubjectEntry {
	private int SequenceId;
	private String SubjectCode;
	private String OriginalSubjectId;
	private char Gender;
	private int BirthYear, DeathYear;
	private String MotherId;
	private int Generation, FamilyId;
	
	// for comparison only
	public SubjectEntry(int seqNo, String id) {
		SequenceId = seqNo;
		OriginalSubjectId = id;
	}
	
	// for founder subject
	public SubjectEntry(String founderId, int familyId) {
		OriginalSubjectId = founderId;
		this.FamilyId = familyId;
		Gender = 'F';
	}
		
	public SubjectEntry(String lineInCsv) {
		//System.out.println(lineInCsv);
		String[] parts = lineInCsv.split(",");
		SequenceId = Integer.parseInt(parts[0]);
		SubjectCode = null; // for now
		OriginalSubjectId = parts[2];
		Gender = parts[3].charAt(0);
		BirthYear = Integer.parseInt(parts[4]);
		DeathYear = (parts[5].trim().equals("R"))?(0):
			(parts[5].length()==0)?-1:Integer.parseInt(parts[5]);
		MotherId = parts[6];
		Generation = Integer.parseInt(parts[7]);
		FamilyId = Integer.parseInt(parts[8]);
	}
	
	public SubjectEntry(String[] dbrecord) {
		//System.out.println(lineInCsv);
		SequenceId = Integer.parseInt(dbrecord[0]);
		//SubjectCode = null; // for now
		OriginalSubjectId = dbrecord[1];
		Gender = dbrecord[2].charAt(0);
		BirthYear = Integer.parseInt(dbrecord[3]);
		DeathYear = (dbrecord[4].trim().equals("R"))?(0):
			(dbrecord[4].length()==0)?-1:Integer.parseInt(dbrecord[4]);
		MotherId = dbrecord[5];
		Generation = Integer.parseInt(dbrecord[6]);
		FamilyId = Integer.parseInt(dbrecord[7]);
	}
	
	public static List<SubjectEntry> loadEntriesFromFile(String file) {
		List<SubjectEntry> subjects = new ArrayList<SubjectEntry>();
		Scanner input = null;
		try {
			input = new Scanner(new File(file));
			
			// print the column labels
			System.out.println(input.nextLine());
			
			while (input.hasNextLine()) {
				subjects.add(new SubjectEntry(input.nextLine()));
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return subjects;
	}
	
	public static List<SubjectEntry> loadEntriesFromDB(String table) {
		List<SubjectEntry> subjects = new ArrayList<SubjectEntry>();
		
		SqlServerDbAccessor sqda = new SqlServerDbAccessor();
		sqda.setDbName("CayoSantiagoRhesusDB");
		sqda.connectToDb();
		String SQL = "SELECT * FROM " + table + " WHERE FamilyID = 1 ORDER BY Generation";
		try {
			Statement stmt = sqda.getConnection().createStatement();
			ResultSet rs = stmt.executeQuery(SQL);
			String[] row = new String[8];
			while(rs.next()) {
				for (int i=1; i<=8; i++) {
					row[i-1] = ((rs.getString(i) == null) ? "" : rs.getString(i));
				}
				subjects.add(new SubjectEntry(row));
			}
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return subjects;
	}
	
	@Override
	public boolean equals(Object o) {
		boolean isTheSame = false;
		
		if (o instanceof SubjectEntry) {
			SubjectEntry se = (SubjectEntry)o;
			isTheSame = se.OriginalSubjectId.equals(OriginalSubjectId);
		}
		
		return isTheSame;
	}
	
	@Override
	public String toString() {
		String dy = "";
		if (DeathYear == 0)
			dy += "R";
		else if (DeathYear > -1)
				dy += DeathYear;
		return FamilyId + "#" + SequenceId + "-" + Gender + "(" 
				+ BirthYear + "-" + dy + ")" + "-" + OriginalSubjectId; //to replace osId (Generation+1);
		/*
		return OriginalSubjectId + "-" + Gender + "(" 
		+ BirthYear + "-" + DeathYear + ")" + MotherId + "-" 
		+ Generation + "#" + FamilyId;
		*/
	}

	public String getMotherID() {
		// TODO Auto-generated method stub
		return MotherId;
	}

	public int getGeneration() {
		// TODO Auto-generated method stub
		return this.Generation;
	}

	public String getOriginalId() {
		// TODO Auto-generated method stub
		return this.OriginalSubjectId;
	}

	public char getGender() {
		// TODO Auto-generated method stub
		return Gender;
	}
}
