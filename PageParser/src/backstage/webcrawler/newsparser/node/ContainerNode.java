package backstage.webcrawler.newsparser.node;

/**
 * 容器节点(如：html标签中的div，table...)
 * 
 * @author raoxunrong, xunrong.rao@roboo.com 2008-7-18
 * 
 */
public class ContainerNode extends AbstractNewsNode {

	public static final int TYPE = 1;

	/**
	 * 节点中链接文字的总个数(包括其所有的子节点)
	 */
	private int linkedTextLength = 0;

	/**
	 * 节点中非链接文字的总个数(包括其所有的子节点)
	 */
	private int valueTextLength = 0;

	public ContainerNode() {
		this.nodeType = TYPE;
	}

	public ContainerNode(String nodeName, String nodeValue,
			NewsNode parentNode) {
		super(nodeName, nodeValue, parentNode);
		this.nodeType = TYPE;
	}
	
	public int addLinkedTextLength(int additionNum){
		linkedTextLength += additionNum;
		return linkedTextLength;
	}
	
	public int addValueTextLength(int additionNum){
		valueTextLength += additionNum;
		return valueTextLength;
	}

	public int getLinkedTextLength() {
		return linkedTextLength;
	}

	public void setLinkedTextLength(int linkedTextLength) {
		this.linkedTextLength = linkedTextLength;
	}

	public int getValueTextLength() {
		return valueTextLength;
	}

	public void setValueTextLength(int valueTextLength) {
		this.valueTextLength = valueTextLength;
	}
	
	
}
