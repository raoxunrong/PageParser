package backstage.webcrawler.newsparser.base;

import java.util.LinkedList;

import org.w3c.dom.Node;

/**
 * 
 * @author rxr created at 2011-10-26
 *
 */
public interface NewsParser {
	/**
	 * millisceconds
	 * @return
	 */
	public String getDate();
	
	public String getContent();
	
	/**
	 * 1.by url mapping. 
	 * 2.by keywords matching
	 * @return 
	 */
	public String getChannel();
	
	public String getOrigin();
	
	public String getTitle();

	public boolean extractNews( String url, LinkedList<Node> domTrees, boolean bExtractOrigin);
	
	public boolean extractNews( String url, Node domTree, boolean bExtractOrigin );
	
}
