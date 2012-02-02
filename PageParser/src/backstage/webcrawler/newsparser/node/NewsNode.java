package backstage.webcrawler.newsparser.node;

import java.util.List;

/**
 * 新闻节点
 * 
 * @author raoxunrong, xunrong.rao@roboo.com 2008-7-18
 * 
 */
public interface NewsNode {

	/**
	 * 获取父节点
	 * 
	 * @return
	 */
	public NewsNode getParentNode();

	/**
	 * 获取孩子节点
	 * 
	 * @return
	 */
	public List<NewsNode> getChildrenNodes();

	/**
	 * 判断是否含有孩子节点
	 * 
	 * @return
	 */
	public boolean hasChildrenNode();

	/**
	 * 判断是否含有父节点
	 * 
	 * @return
	 */
	public boolean hasParentNode();

	/**
	 * 添加一个孩子节点
	 * 
	 * @param node
	 */
	public void addChildNode(NewsNode node);

	/**
	 * 删除一个孩子节点
	 * 
	 * @param node
	 */
	public void removeChildNode(NewsNode node);

	/**
	 * 获取节点类型
	 * 
	 * @return
	 */
	public int getNodeType();

	/**
	 * 获取节点名称
	 * 
	 * @return
	 */
	public String getNodeName();

	/**
	 * 获取节点值
	 * 
	 * @return
	 */
	public String getNodeValue();

	/**
	 * 设置节点值
	 * 
	 * @return
	 */
	public String setNodeValue(String value);

	/**
	 * 节点是否居中
	 * 
	 * @return
	 */
	public boolean isCenter();
}
