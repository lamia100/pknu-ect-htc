package data;

public class Book {
	private String title;
	private String author;
	private String press;
	
	public Book(String title, String author, String press) {
		this.title = title;
		this.author = author;
		this.press = press;
	}
		
	public String getTitle() {
		return title;
	}

	public String getAuthor() {
		return author;
	}

	public String getPress() {
		return press;
	}

	public String toString() {
		return "제목 : " + title + "\n저자 : " + author + "\n출판사 : " + press;
	}
}