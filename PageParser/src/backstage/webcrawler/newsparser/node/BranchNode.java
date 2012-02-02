package backstage.webcrawler.newsparser.node;

public class BranchNode extends AbstractNewsNode {

	public static final int TYPE = 4;

	public BranchNode() {
		this.nodeType = TYPE;
	}

	public BranchNode(String nodeName, String nodeValue, NewsNode parentNode) {
		super(nodeName, nodeValue, parentNode);
		this.nodeType = TYPE;
	}
}
