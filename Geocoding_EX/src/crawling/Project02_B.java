package crawling;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import kr.inflearn.DownloadBroker;

public class Project02_B {
	public static void main(String[] args) {
		String url = "https://sum.su.or.kr:8888/bible/today/Ajax/Bible/BodyMatter?qt_ty=QT1";
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		try {
			Document doc = Jsoup.connect(url).post();

			System.out.println("[입력 -> 년(yyyy)-월(mm)-일(dd)] : ");
			String bible = br.readLine();
			url = url + "&Base_de=" + bible + "&bibleType=1";

			System.out.println("========================================");
			
			Element bible_text = doc.select(".bible_text").first();
			Element bibleinfo_box = doc.select(".bibleinfo_box").first();
			Elements liList = doc.select(".body_list > li");

			System.out.println(bible_text.text());
			System.out.println(bibleinfo_box.text());
			for(Element li : liList) {
				System.out.print(li.select(".num").first().text() + " : ");
				System.out.println(li.select(".info").first().text());
			}
			
			
			//download resource(mp3, image)	
			Element tag = doc.select("source").first();
			String dPath = tag.attr("src").trim();
			String fileName = dPath.substring(dPath.lastIndexOf("/") + 1);
			
			/*
			Elements imgTag = doc.select(".book_list > li");
			for(Element img : imgTag) {
				String iPath = img.select(".book_thum > .img > img").attr("src").trim();
 				String imgName = iPath.substring(iPath.lastIndexOf("/")+1);
			}
			*/
			Runnable r = new DownloadBroker(dPath, fileName);
			Thread dLoad = new Thread(r);
			dLoad.start();
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
