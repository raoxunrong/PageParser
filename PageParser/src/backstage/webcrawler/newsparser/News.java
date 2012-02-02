package backstage.webcrawler.newsparser;

import java.text.SimpleDateFormat;

/**
 * 
 * @author rxr created at 2011-10-26
 *
 */
public class News {
	
	private long id;
	private String url;
	private String content;
	private String title;
	private String date;
	private String channel;
	private String md5;
	private String site;
	private String origin;
	private String reserved;
	
	private short fileNo;
	private int    offset;
	
	
	public static final String DIR_NAME = "news";
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	
	
	
	public News(){
		setReserved("all");		
	}
	
//	public News(String url, String content, String title, long  date, String channel, String site ){
//		setId(System.currentTimeMillis());
//		setUrl(url);
//		setContent(content);
//		setTitle(title);
//		setDate(date);
//		setChannel(channel);
//		setSite(site);
//		this.md5 = MD5Hash.digest( this.content ).toString();
//		setReserved("all");
//		setOrigin( site );
//	}
	
	public News(String url, String content, String title, long  date, String channel, String site, String origin ){
//		setId(System.currentTimeMillis());
		setUrl(url);
		setContent(content);
		setTitle(title);
		setDate(date);
		setChannel(channel);
		setSite(site);
		setReserved("all");
		if( origin== null ){
			setOrigin( site );	
		}else{
			setOrigin( origin );
		}
	}
	
	public long getId(){
		return id;
	}
	
	public void setId(long id){
		this.id = id;
	}
	
	public String getUrl(){
		return url;
	}
	
	public void setUrl(String url){
		this.url = url;
	}
	
	public String getContent(){
		return content;
	}
	
	public void setContent(String content ){
		this.content = content;
	}

	public String getTitle(){
		return title;
	}
	
	public void setTitle( String title ){
		this.title = title;
	}
	
	public String getDate(){
		return date;
	}
	
	public void setDate(String date){
//		String n = ParseNewsImpl.convertToDate(date);
//		if( n!=null ){
//			setDate(n);
//		}else{
//			this.date = date;	
//		}
		this.date = date;
	}
	
	public String getChannel(){
		return this.channel;
	}
	
	public void setChannel(String channel){
		this.channel = channel;
	}
	
	public void setMd5(String md5){
		this.md5 = md5;
	}
	
	public String getMd5(){
		return this.md5;
	}

	
	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}

	public void setReserved(String reserved ){
		this.reserved = reserved;
	}
	
	public String getReserved(){
		return this.reserved;
	}
	
	public void setDate( long date ){
		this.date = sdf.format(date);
	}
	
	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}
	

	public short getFileNo() {
		return fileNo;
	}

	public void setFileNo(short fileNo) {
		this.fileNo = fileNo;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}
	
}
