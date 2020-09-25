--commands to make use of stored procedures for NSF-480 database

--view data in original non-normalized data from csv file 
select * from Family31;

--view data in normalized table (one monkey per row)
select * from MacacaSubject
ORDER BY SequenceID asc;

--drop and create new MacacaSubject table with headers only
exec CreateMacacaSubjectTable;

--populate MacacaSubject table with data from Family31
exec FlattenData;


--insert new monkey (input parameters for each field)
exec InsertMonkey                 
                 @SubjectCode = null, 
                 @SubjectID   = 'X572', 
                 @Gender      = 'm', 
                 @BirthYear   = '1964', 
                 @DeathYear   = '2060', 
                 @MotherID    = 'R003', 
                 @Generation  = '5', 
                 @FamilyID    = '1';

--delete monkey (input parameter SubjectID)
exec DeleteMonkey
				@SubjectID = 'X572';

--get the data for a monkey (input parameter SubjectID)
exec GetMonkey
				@SubjectID = '240';

--update the death year for a monkey (input SubjectID and new DeathYear)
exec UpdateDeathYear
@SubjectID = 'X572',
@DeathYear = '1999'

				 