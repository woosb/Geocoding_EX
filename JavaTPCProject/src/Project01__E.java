import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

public class Project01__E {
	//지도 이미지 생성 메서드
	public static void map_service(String point_x, String point_y, String address) {
		String URL_STATICMAP = 	"https://naveropenapi.apigw.ntruss.com/map-static/v2/raster?";
		try {
			String pos = URLEncoder.encode(point_x + " " + point_y, "UTF-8");
			String url = URL_STATICMAP;
			url += "center=" + point_x + "," + point_y;
			url += "&level=16&w=700&h=500";
			url += "&markers=type:t|size:mid|pos:" + pos + "|label:" + URLEncoder.encode(address, "UTF-8");
			URL u = new URL(url);
			HttpURLConnection con = (HttpURLConnection)u.openConnection();
			con.setRequestMethod("GET");
			con.addRequestProperty("X-NCP-APIGW-API-KEY-ID", "anrunf4tgp");
			con.addRequestProperty("X-NCP-APIGW-API-KEY", "3zX2Qvvkk83G69ApNxLSsTqvMR27KOEXDUxBiQyp");
			int responseCode = con.getResponseCode();
			BufferedReader br;
			
			if(responseCode == 200) { //정상호출
				InputStream is = con.getInputStream();
				int read = 0;
				byte[] bytes = new byte[1024];
				//랜덤으로 이름 생성
				String tempname = Long.valueOf(new Date().getTime()).toString();
				File f = new File(tempname + ".jpg");
				OutputStream outputStream = new FileOutputStream(f);
				
				while((read = is.read(bytes)) != -1) {
					outputStream.write(bytes,0,read);
				}
				is.close();
			}else {//에러 발생
				br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
				String inputLine;
				StringBuffer response = new StringBuffer();
				while((inputLine = br.readLine()) != null) {
					response.append(inputLine);
				}
				br.close();
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args) {
		
		String apiURL = "https://naveropenapi.apigw.ntruss.com/map-geocode/v2/geocode?query=";
		String client_id = "anrunf4tgp";
		String client_secret ="3zX2Qvvkk83G69ApNxLSsTqvMR27KOEXDUxBiQyp";
		String mapURL = "https://naveropenapi.apigw.ntruss.com/map-static/v2/raster";
		//BufferdReader : 문자스트림 , System.in: 바이트 스트림, InputStreamReader :바이트스트림을 문자스트림으로  
		BufferedReader io = new BufferedReader(new InputStreamReader(System.in));
		
		try {
			System.out.println("주소를 입력 하세요: ");
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
				br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8")); //입력장치와 도킹
			}else {
				br = new BufferedReader(new InputStreamReader(con.getErrorStream(), "UTF-8"));
			}
			
			String line;
			StringBuffer response = new StringBuffer(); //JSON
			
			String x = "";
			String y = "";
			String z = "";
			
			while((line = br.readLine()) != null) { //버퍼로 부터 응용프로그램으로 입력값을 받아오기
				response.append(line);
			}
			
			br.close();
			
			JSONTokener tokener = new JSONTokener(response.toString());
			JSONObject object = new JSONObject(tokener);
			System.out.println(object.toString());
			
			
			JSONArray arr = object.getJSONArray("addresses");
			
				JSONObject temp = (JSONObject) arr.get(0);
				System.out.println("address : " + temp.get("roadAddress"));
				System.out.println("지번 : " + temp.get("jibunAddress"));
				System.out.println("경도 : " + temp.get("x"));
				System.out.println("위도 : " + temp.get("y"));
				x = (String) temp.get("x");
				y = (String)temp.get("y");
				z = (String)temp.get("roadAddress");
			
			
			map_service(x, y, z);
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	

}
