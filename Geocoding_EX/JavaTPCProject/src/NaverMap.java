import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

import javax.swing.ImageIcon;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import kr.inflearn.AddressVO;

public class NaverMap implements ActionListener{
	
	Project01_F naverMap;
	public NaverMap(Project01_F naverMap) {
		this.naverMap = naverMap;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		String client_id = "anrunf4tgp";
		String client_secret = "3zX2Qvvkk83G69ApNxLSsTqvMR27KOEXDUxBiQyp";
		AddressVO vo = null;
		try {
			String address = naverMap.address.getText();
			String addr = URLEncoder.encode(address, "UTF-8");//입력 공백도 문자처리해줘야 한다.
			String apiURL = "https://naveropenapi.apigw.ntruss.com/map-geocode/v2/geocode?query=" + addr;
			URL url = new URL(apiURL);
			HttpURLConnection con = (HttpURLConnection)url.openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("X-NCP-APIGW-API-KEY-ID", client_id);
			con.setRequestProperty("X-NCP-APIGW-API-KEY", client_secret);
			int responseCode = con.getResponseCode();
			BufferedReader br;
			
			if(responseCode == 200) {
				br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
			}else {
				br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
			}
			String inputLine;
			StringBuffer response = new StringBuffer();
			while((inputLine = br.readLine()) != null) {
				response.append(inputLine);
			}
			br.close();
			JSONTokener tokener = new JSONTokener(response.toString());
			JSONObject object = new JSONObject(tokener);
			System.out.println(object);
			
			JSONArray arr = object.getJSONArray("addresses");
			for(int i =0; i<arr.length(); i++) {
				JSONObject temp = (JSONObject) arr.get(i);
				//AddressVO 객체에 주소정보를 저장
				vo = new AddressVO();
				vo.setRoadAddress((String)temp.get("roadAddress"));
				vo.setJibunAddress((String)temp.getString("jibunAddress"));
				vo.setX((String)temp.get("x"));
				vo.setY((String)temp.get("y"));
				System.out.println(vo);
			}
			map_service(vo);
		}catch(Exception err) {
			err.printStackTrace();
		}
	}
	
	public void map_service(AddressVO vo) {
		String URL_STATICMAP = 	"https://naveropenapi.apigw.ntruss.com/map-static/v2/raster?";
		try {
			
			String pos = URLEncoder.encode(vo.getX() + " " + vo.getY(), "UTF-8");
			String url = URL_STATICMAP;
			url += "center=" + vo.getX() + "," + vo.getY();
			url += "&level=16&w=700&h=500";
			url += "&markers=type:t|size:mid|pos:" + pos + "|label:" + URLEncoder.encode(vo.getRoadAddress(), "UTF-8");
			URL u = new URL(url);
			HttpURLConnection con = (HttpURLConnection)u.openConnection();
			con.setRequestMethod("GET");
			con.addRequestProperty("X-NCP-APIGW-API-KEY-ID", "anrunf4tgp");
			con.addRequestProperty("X-NCP-APIGW-API-KEY", "3zX2Qvvkk83G69ApNxLSsTqvMR27KOEXDUxBiQyp");
			int responseCode = con.getResponseCode();
			BufferedReader br;
			
			if(responseCode == 200) {//정상호출
				InputStream is = con.getInputStream();
				int read = 0;
				byte[] bytes = new byte[1024];
				String tempname = Long.valueOf(new Date().getTime()).toString();
				File f = new File(tempname + ".jpg");
				f.createNewFile();
				
				OutputStream outputStream = new FileOutputStream(f);
				while((read = is.read(bytes)) != -1) {
					outputStream.write(bytes, 0, read);
				}
				is.close();
				ImageIcon img = new ImageIcon(f.getName());
				naverMap.imageLabel.setIcon(img);
				naverMap.resAddress.setText(vo.getRoadAddress());
				naverMap.jibunAddress.setText(vo.getJibunAddress());
				naverMap.resX.setText("위도"+vo.getX());
				naverMap.resY.setText("경도"+vo.getY());
				outputStream.close();
			}else {
				System.out.println(responseCode);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
