package backstage.webcrawler.newsparser.node;

public class TextNode extends AbstractNewsNode {

	public static final int TYPE = 3;
	
	public TextNode(){
		this.nodeType = TYPE;
	}
	
	public TextNode(String nodeName, String nodeValue, NewsNode parentNode){
		super(nodeName, nodeValue, parentNode);
		this.nodeType = TYPE;
	}

	public void setCenter(boolean center){
		this.center = center;
	}
}
