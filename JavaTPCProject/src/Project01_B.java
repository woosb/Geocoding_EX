import org.json.JSONArray;
import org.json.JSONObject;

public class Project01_B {
	public static void main(String[] args) {
		//JSON-java(org.json)
		JSONArray students = new JSONArray();
		
		JSONObject student = new JSONObject();
		student.put("name", "È«±æµ¿");
		student.put("phone", "010-1111-1111");
		student.put("address", "¼­¿ï");
		System.out.println(student);

		students.put(student);
		
		student = new JSONObject();
		student.put("name", "³ª±æµ¿");
		student.put("phone", "010-2222-2222");
		student.put("address", "±¤ÁÖ");
		
		students.put(student);
		
		System.out.println(students);
		
		JSONObject object = new JSONObject();
		
		object.put("students", students);
		
		System.out.println(object.toString(2));
	}
}
