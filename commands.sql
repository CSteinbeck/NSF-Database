--commands to make use of stored procedures for NSF-480 database

--view data in original non-normalized data from csv file 
SELECT * FROM Family31;

--view data in normalized table (one monkey per row)
SELECT * FROM MacacaSubject
ORDER BY SequenceID ASC;

--drop and create new MacacaSubject table with headers only
EXEC CreateMacacaSubjectTable;

--populate MacacaSubject table with data from Family31
EXEC FlattenData;


--insert new monkey (input parameters for each field)
EXEC InsertMonkey                 
                 @SubjectCode = null, 
                 @SubjectID   = 'X572', 
                 @Gender      = 'm', 
                 @BirthYear   = '1964', 
                 @DeathYear   = '2060', 
                 @MotherID    = 'R003', 
                 @Generation  = '5', 
                 @FamilyID    = '1';

--delete monkey (input parameter SubjectID)
EXEC DeleteMonkey
				@SubjectID = 'X572';

--get the data for a monkey (input parameter SubjectID)
EXEC GetMonkey
				@SubjectID = '240';

--update the death year for a monkey (input SubjectID and new DeathYear)
EXEC UpdateDeathYear
@SubjectID = 'X572',
@DeathYear = '1999'

				 

				 
