import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

public class Project01_D {

	class Typetester {
	    void printType(byte x) {
	        System.out.println(x + " is an byte");
	    }
	    void printType(int x) {
	        System.out.println(x + " is an int");
	    }
	    void printType(float x) {
	        System.out.println(x + " is an float");
	    }
	    void printType(double x) {
	        System.out.println(x + " is an double");
	    }
	    void printType(char x) {
	        System.out.println(x + " is an char");
	    }
	}
	public static void main(String[] args) {
		
		String apiURL = "https://naveropenapi.apigw.ntruss.com/map-geocode/v2/geocode?query=";
		String client_id = "anrunf4tgp";
		String client_secret ="3zX2Qvvkk83G69ApNxLSsTqvMR27KOEXDUxBiQyp";
		String mapURL = "https://naveropenapi.apigw.ntruss.com/map-static/v2/raster";
		//BufferdReader : ���ڽ�Ʈ�� , System.in: ����Ʈ ��Ʈ��, InputStreamReader :����Ʈ��Ʈ���� ���ڽ�Ʈ������  
		BufferedReader io = new BufferedReader(new InputStreamReader(System.in));
		
		try {
			System.out.println("�ּҸ� �Է� �ϼ���: ");
			String address = io.readLine();
			String addr = URLEncoder.encode(address, "UTF-8");
			String reqUrl = apiURL + addr;
			
			URL url = new URL(reqUrl);
			HttpURLConnection con = (HttpURLConnection)url.openConnection();
			con.setRequestMethod("GET");
			con.addRequestProperty("X-NCP-APIGW-API-KEY-ID", client_id);
			con.addRequestProperty("X-NCP-APIGW-API-KEY", client_secret);
			int responseCode = con.getResponseCode(); //200
			
			BufferedReader br;
			if(responseCode == 200) {
				br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8")); //�Է���ġ�� ��ŷ
			}else {
				br = new BufferedReader(new InputStreamReader(con.getErrorStream(), "UTF-8"));
			}
			
			String line;
			StringBuffer response = new StringBuffer(); //JSON
			while((line = br.readLine()) != null) { //���۷� ���� �������α׷����� �Է°��� �޾ƿ���
				response.append(line);
			}
			
			br.close();
			
			JSONTokener tokener = new JSONTokener(response.toString());
			JSONObject object = new JSONObject(tokener);
			System.out.println(object.toString());
			
			
			JSONArray arr = object.getJSONArray("addresses");
			for(int i = 0; i < arr.length(); i++) {
				JSONObject temp = (JSONObject) arr.get(i);
				System.out.println("address : " + temp.get("roadAddress"));
				System.out.println("���� : " + temp.get("jibunAddress"));
				System.out.println("�浵 : " + temp.get("x"));
				System.out.println("���� : " + temp.get("y"));
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		
		
	}
}
