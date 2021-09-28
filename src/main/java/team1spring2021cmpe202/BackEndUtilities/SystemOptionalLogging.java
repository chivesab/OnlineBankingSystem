package team1spring2021cmpe202.BackEndUtilities;

public class SystemOptionalLogging {
	public static void log(String string) {
		boolean loggingEnabled = System.getenv("SysOLogEnabled").equals("true") ? true : false;
		if(loggingEnabled) {
			System.out.println(string);
		}
	}
}
