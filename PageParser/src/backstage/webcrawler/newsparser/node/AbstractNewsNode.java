package backstage.webcrawler.newsparser.node;

import java.util.LinkedList;
import java.util.List;

/**
 * 抽象新闻节点
 * 
 * @author raoxunrong, xunrong.rao@roboo.com 2008-7-18
 * 
 */
public abstract class AbstractNewsNode implements NewsNode {

	/**
	 * 节点名称
	 */
	protected String nodeName = null;

	/**
	 * 节点值
	 */
	protected String nodeValue = null;

	/**
	 * 父节点
	 */
	protected NewsNode parentNode = null;

	/**
	 * 孩子节点
	 */
	protected LinkedList<NewsNode> childrenList = new LinkedList<NewsNode>();

	/**
	 * 节点类型
	 */
	protected int nodeType;

	protected boolean center = false;// 节点是否居中,默认为否

	public AbstractNewsNode() {
	}

	public AbstractNewsNode(String nodeName, String nodeValue,
			NewsNode parentNode) {
		this.nodeName = nodeName;
		this.nodeValue = nodeValue;
		this.parentNode = parentNode;
	}

	public List<NewsNode> getChildrenNodes() {
		return childrenList;
	}

	public NewsNode getParentNode() {
		return parentNode;
	}

	public boolean hasChildrenNode() {
		return (childrenList == null || childrenList.size() == 0) ? false
				: true;
	}

	public boolean hasParentNode() {
		return parentNode == null ? false : true;
	}

	public int getNodeType() {
		return nodeType;
	}

	public void addChildNode(NewsNode node) {
		childrenList.add(node);
	}

	public void removeChildNode(NewsNode node) {
		childrenList.remove(node);
	}

	public String getNodeName() {
		return nodeName;
	}

	public String getNodeValue() {
		return nodeValue;
	}

	public String setNodeValue(String value) {
		return this.nodeValue = value;
	}

	public boolean isCenter() {
		return center;
	}

}
