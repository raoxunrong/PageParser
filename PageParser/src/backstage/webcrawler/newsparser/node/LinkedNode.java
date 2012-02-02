package backstage.webcrawler.newsparser.node;

/**
 * 链接型节点
 * 
 * @author raoxunrong, xunrong.rao@roboo.com 2008-7-23
 * 
 */
public class LinkedNode extends AbstractNewsNode {

	public static final int TYPE = 2;

	private boolean isAlone = false;// 标注一个链接节点是否是单独存在的，即和内容无关

	public LinkedNode() {
		this.nodeType = TYPE;
	}

	public LinkedNode(String nodeName, String nodeValue, NewsNode parentNode) {
		super(nodeName, nodeValue, parentNode);
		this.nodeType = TYPE;
	}

	public void setCenter(boolean center) {
		this.center = center;
	}

	public boolean isAlone() {
		return isAlone;
	}

	public void setAlone(boolean isAlone) {
		this.isAlone = isAlone;
	}
}
