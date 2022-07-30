package jp.co.seattle.library.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jp.co.seattle.library.dto.BookDetailsInfo;
import jp.co.seattle.library.service.BooksService;
import jp.co.seattle.library.service.ThumbnailService;

/**
 * ISBNコントローラー
 */
@Controller
public class IsbnSerchController {
	final static Logger logger = LoggerFactory.getLogger(IsbnSerchController.class);
	
	@Autowired
	private BooksService booksService;

	@Autowired
	private ThumbnailService thumbnailService;

	/**
	 * 書籍返却処理
	 * 
	 * @param locale
	 * @param bookId
	 * @param model
	 * @return
	 * @throws MalformedURLException 
	 * @throws JsonProcessingException 
	 */
	@Transactional
	@RequestMapping(value = "/isbnSerch", method = RequestMethod.POST)
	public String rentBook(Locale locale, @RequestParam("isbnSerch") String isbn, Model model) throws MalformedURLException, JsonProcessingException {
		// デバッグ用ログ
		logger.info("Welcome isbnSerchController.java! The client locale is {}.", locale);

        //URL url = new URL("https://www.googleapis.com/books/v1/volumes?q=isbn:" + isbn + "&key=AIzaSyD3OY8uG91yjAhbtStHJ6buciVFB1OHtiA");
        isbn = isbn.replace("-", "");
		
        String result = "";
        JsonNode root = null;
        try {
        	URL url = new URL("https://www.googleapis.com/books/v1/volumes?q=isbn:" + isbn);
           HttpURLConnection con = (HttpURLConnection)url.openConnection();
           con.connect(); // URL接続
           BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream())); 
           String tmp = "";

              while ((tmp = in.readLine()) != null) {
               result += tmp;
           }

           ObjectMapper mapper = new ObjectMapper();
           root = mapper.readTree(result);
           in.close();
           con.disconnect();
        }catch(Exception e) {
           e.printStackTrace();
        }

        JsonNode book = root.get("items");
        System.out.println(book);
//        System.out.println("koreha  " + book);
        System.out.println("iikanji  " + book.get(0).get("volumeInfo"));
        System.out.println("samuneoru  "  + book.get(0).get("volumeInfo").get("imageLinks"));
        
        String title =  sqlCheck("title", book);
        String beforeAuthors =  book.get(0).get("volumeInfo").get("authors").get(0).asText();
        String authors = beforeAuthors.replace("'", " ").replace(",", " ");
        String publisher =  sqlCheck("publisher", book);
        String publishedDate = sqlCheck("publishedDate", book);
        String thumbnailUrl = book.get(0).get("volumeInfo").get("imageLinks").get("thumbnail").asText();
        System.out.println(thumbnailUrl);
//        System.out.println(title);
//        System.out.println(authors);
//        System.out.println(publisher);
//        System.out.println(publishedDate);
   
        BookDetailsInfo bookInfo = new BookDetailsInfo();
        bookInfo.setTitle(title);
		bookInfo.setAuthor(authors);
		bookInfo.setPublisher(publisher);
		//bookInfo.setDescription(description);
		bookInfo.setIsbn(isbn);
		bookInfo.setPublishDate(publishedDate);
		bookInfo.setThumbnailUrl(thumbnailUrl);
        booksService.registBook(bookInfo);

		model.addAttribute("bookDetailsInfo", booksService.getBookInfo());
		
		model.addAttribute("bookStatus", "貸し出し可");

		return "details";

	}
	
	public String sqlCheck(String item, JsonNode book) {
		String result = "";
		
		try {
			result  =  book.get(0).get("volumeInfo").get(item).asText();
			result = result.replace("'", " ").replace(",", " ").replace("-", "");;
		}
		catch (NullPointerException ex){
		}
		return result;
	}
	
}
