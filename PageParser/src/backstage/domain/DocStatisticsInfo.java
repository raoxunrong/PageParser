package backstage.domain;

public class DocStatisticsInfo {

	private String url;

	private int textLength;
	private int linkedTextLength;
	private int linkCount;
	private int maxTextBlockLength;
	
	public DocStatisticsInfo(String url) {
		super();
		this.url = url;
	}
	
	public void addTextLength(int length){
		textLength += length;
	}
	
	public void addLinkedTextLength(int length){
		linkedTextLength += length;
	}
	
	public void addLinkCount(int length){
		linkCount += length;
	}
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getTextLength() {
		return textLength;
	}

	public void setTextLength(int textCount) {
		this.textLength = textCount;
	}

	public int getLinkedTextLength() {
		return linkedTextLength;
	}

	public void setLinkedTextLength(int linkedTextLength) {
		this.linkedTextLength = linkedTextLength;
	}

	public int getLinkCount() {
		return linkCount;
	}

	public void setLinkCount(int linkCount) {
		this.linkCount = linkCount;
	}

	public int getMaxTextBlockLength() {
		return maxTextBlockLength;
	}

	public void setMaxTextBlockLength(int maxTextBlockLength) {
		this.maxTextBlockLength = maxTextBlockLength;
	}

}
