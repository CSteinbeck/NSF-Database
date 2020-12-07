package csdbdao;

public interface JTableModelAdaptor {
	String[] getColNamesForTable(String tableName);
	
	Object[][] getData(String tableName);
}
