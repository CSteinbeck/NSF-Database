package csdbtools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import csdbdao.SqlServerDbAccessor;

public class CsDbTool {
	private SqlServerDbAccessor sqda;
	
	private String skip = "69"; //?
	
	public CsDbTool() {
		this.sqda = new SqlServerDbAccessor();
		sqda.setDbName("CayoSantiagoRhesusDB");
		sqda.connectToDb();
	}
	
	// loading images into the ImageSeries table and the Scan table
	public void loadImagesFromDirectory(String dir) {
		String intoScan = "INSERT INTO Scan (ScanID, MethodID, SubjectID, Site) " +
		          "VALUES (?,  'CT', ?, ?)";
		String intoImageSeries = "INSERT INTO ImageSeries (ScanID, ImageNote, ImageFile, Notes) " +
		          "VALUES (?, ?, ?, ?)";
		String updateBlob = "UPDATE ImageSeries SET ImageBlob = ? WHERE SeriesID = ?";
		PreparedStatement intoScanStmt, intoImageSeriesStmt, updateBlobStmt;
		try {
			intoScanStmt = sqda.getConnection().prepareStatement(intoScan);
			intoImageSeriesStmt = sqda.getConnection().prepareStatement(intoImageSeries);
			updateBlobStmt = sqda.getConnection().prepareStatement(updateBlob);
			
			int l = new File(dir + "/Data").listFiles().length;
			File[][] images = new File[3][l];
			String[] dirName = {"Front", "Rear", "Side"};
			for (int i=0; i<dirName.length; i++) {
				images[i] = new File(dir + "/" + dirName[i]).listFiles();
			}
			// insert into Scan
			int iScan = 2;
			int jImage = 5;
			int dummySubjectID = 22;
			String fileName;
			String site;
			
			FileInputStream imageInputStream;
			String imgDir = "Images/CT Scans/";
			String imageFile;
			
			// loop through the scans
			for (int i=0; i<l; i++) {
				fileName = images[0][i].getName();
				if (fileName.substring(0, 2).equals(skip))
					continue;
				
				site = "Skull";
				if (!Character.isDigit(fileName.charAt(0)))
					site = "Hand";
				intoScanStmt.setInt(1, iScan);
				intoScanStmt.setInt(2, dummySubjectID);
				intoScanStmt.setString(3, site);
				System.out.println(iScan + "' " + dummySubjectID + "' " +
						site);
				if (iScan > 2)
					intoScanStmt.executeUpdate();
				
				// loop through the images
				for (int j=0; j<3; j++, jImage++) {
					fileName = images[j][i].getName();
					imageFile = imgDir + dirName[j] + "/" + fileName;

					//intoImageSeriesStmt.setInt(1, jImage);
					intoImageSeriesStmt.setInt(1, iScan);
					intoImageSeriesStmt.setString(2, dirName[j]);
					intoImageSeriesStmt.setString(3, fileName);
					intoImageSeriesStmt.setString(4, imageFile);
					System.out.println(jImage + "' " + iScan + "' " +  dirName[j] + "' " +
							fileName + "' " + imageFile);
					if (jImage > 5)
						intoImageSeriesStmt.executeUpdate();
					
					imageInputStream = new FileInputStream(new File(imageFile));
					updateBlobStmt.setBinaryStream(1, imageInputStream);
					updateBlobStmt.setInt(2, jImage);
					System.out.println(jImage + "' " + imageInputStream);
					updateBlobStmt.executeUpdate();
				}
				iScan++;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String dir = "Images/CT Scans";
		CsDbTool tool = new CsDbTool();
		tool.loadImagesFromDirectory(dir);
		/*
		System.out.println(dir + "/Rear");
		File[] files = new File(dir + "/Rear").listFiles();
		System.out.println(files.length);
		
		for (File f : files) {
			System.out.println(f.getName());
		}
		*/
	}

}
