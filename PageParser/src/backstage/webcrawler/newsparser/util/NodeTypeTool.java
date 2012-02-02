package backstage.webcrawler.newsparser.util;


import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import backstage.webcrawler.newsparser.conf.NewsParserConfig;
import backstage.webcrawler.newsparser.conf.NewsParserConfig.IgnoreTag;
import backstage.webcrawler.newsparser.node.BranchNode;
import backstage.webcrawler.newsparser.node.ContainerNode;
import backstage.webcrawler.newsparser.node.LinkedNode;
import backstage.webcrawler.newsparser.node.NewsNode;

/**
 * 节点类型及属性相关操作工具类
 * 
 * @author raoxunrong, xunrong.rao@roboo.com 2008-7-23
 * 
 */
public class NodeTypeTool {

	private static NewsParserConfig config;

	static {
		config = NewsParserConfig.get();
	}

	/**
	 * 判断节点是否是该被过滤
	 * 
	 * @param node
	 * @return
	 */
	public static boolean isIgnoreNode(Node node, String siteName) {
		if (null == node || null == node.getNodeName()) {
			return true;
		}
		if (config.getIgnoreTagList()
				.contains(node.getNodeName().toLowerCase())
				|| node.getNodeType() == Node.COMMENT_NODE) {
			return true;
		}

		if (siteName != null && !siteName.equals("")) {
			List<IgnoreTag> ignoreTagList = config
					.getSpecificIgnoreTag(siteName);
			if (ignoreTagList != null && ignoreTagList.size() > 0)
				for (IgnoreTag ignoreTag : ignoreTagList) {
					if (ignoreTag.getTag() != null) {
						if (ignoreTag.getTag().equalsIgnoreCase(
								node.getNodeName())) {
							if (ignoreTag.getAttribute() != null) {
								Node temp = node.getAttributes().getNamedItem(
										ignoreTag.getAttribute());
								if (temp != null
										&& temp.getNodeValue()
												.equalsIgnoreCase(
														ignoreTag.getValue())) {
									return true;
								}
							} else
								return true;
						} else
							continue;
					}
				}
		}
		return false;
	}

	/**
	 * 判断节点是否是分段节点
	 * 
	 * @param node
	 * @return
	 */
	public static boolean isBranchNode(Node node) {
		if (null == node || null == node.getNodeName()) {
			return false;
		}
		if (config.getBranchTagList()
				.contains(node.getNodeName().toLowerCase())) {
			return true;
		}
		return false;
	}

	/**
	 * 判断一个节点是否是 新闻来源 信息节点
	 * 
	 * @param node
	 * @return
	 */
	public static boolean isOriginNode(Node node) {
		String text = "";
		if (isLinkNode(node)) {
			// 节点为链接节点
			text = StringUtil.EliminateBlankFormat(node.getFirstChild()
					.getNodeValue());
		} else if (node.getNodeType() == Node.TEXT_NODE) {
			// 节点为常规字符节点
			text = StringUtil.EliminateBlankFormat((node.getNodeValue()));
		} else
			return false;
		if (text == null)
			return false;
		if (text.length() < 2 || text.length() > 40) {
			return false;
		}
		String originRegex = config.getOriginRegex();
		Pattern pattern = Pattern.compile(originRegex);
		Matcher matcher = pattern.matcher(text);
		if (matcher.find()) {
			return true;
		}
		return false;
	}

	/**
	 * 判断节点是否是容器节点
	 * 
	 * @param node
	 * @return
	 */
	public static boolean isContainerNode(Node node) {
		if (config.getContainerTagList().contains(
				node.getNodeName().toLowerCase())) {
			return true;
		}
		return false;
	}

	/**
	 * 判断节点是否是链接节点
	 * 
	 * @param node
	 * @return
	 */
	public static boolean isLinkNode(Node node) {
		if(node != null && config.getLinkTagList().contains(node.getNodeName().toLowerCase()))
			return true;
		return false;
	}

	/**
	 * 判断一个节点是否是文字节点（且满足内容不为空）
	 * 
	 * @param node
	 * @return
	 */
	public static boolean isTextNode(Node node) {
		if (node.getNodeType() == Node.TEXT_NODE && node.getNodeValue() != null)
			return true;
		return false;
	}

	/**
	 * 判断一个节点是否是属于居中
	 * 
	 * @param node
	 * @return
	 */
	public static boolean isCenter(Node node) {
		if (isTextNode(node)) {
			// 属于文本节点
			return isCenter(node.getParentNode());
		}
		NamedNodeMap title_attrs = node.getAttributes();
		if (title_attrs != null)
			for (int i = 0; i < title_attrs.getLength(); i++) {
				Node title_attr = title_attrs.item(i);
				if ("align".equalsIgnoreCase(title_attr.getNodeName())) {
					if ("center".equalsIgnoreCase(title_attr.getNodeValue())
							|| "middle".equalsIgnoreCase(title_attr
									.getNodeValue()))
						return true;
					else
						return false;
				}
			}
		if (node.getNodeName().equalsIgnoreCase("center")) {
			return true;
		}
		if (!isContainerNode(node)) {
			return node.getParentNode() != null ? isCenter(node.getParentNode())
					: false;
		} else
			return false;
	}

	/**
	 * 判断一个链接节点是否是单独存在的（当存在一个链接自成一行或其他的情况，则这个链接和正文基本无语义联系，可能是相关链接等等） 只对链接型节点有效
	 * 
	 * @param node
	 * @return
	 */
	public static boolean isAlone(Node node) {
		if (isLinkNode(node)) {
			do {
				if (node.getNextSibling() != null
						&& node.getNextSibling().getNodeName()
								.equalsIgnoreCase("br"))
					// 当出现换行符的时候可以肯定这个节点一定是单独存在的
					return true;
				node = node.getParentNode();
				if (node == null)
					return true;
				if (config.getBranchTagList().contains(
						node.getNodeName().toLowerCase())) {
					NodeList children = node.getChildNodes();
					for (int i = 0; i < children.getLength(); i++) {
						Node child = children.item(i);
						if (child.getNodeType() == Node.TEXT_NODE) {
							// 如果分枝节点含有信息节点(节点的字符长度大于5)，则此链接节点不为单独存在
							if (StringUtil.EliminateBlankFormat(
									child.getNodeValue()).length() > 5)
								return false;
						} else if (!config.getLinkTagList().contains(
								child.getNodeName().toLowerCase())
								&& !config.getBranchTagList().contains(
										child.getNodeName().toLowerCase())
								&& !config.getDecoratedTagList().contains(
										child.getNodeName().toLowerCase())) {
							// 如果分枝节点的子节点含有除了链接节点,分枝节点,修饰节点和非信息节点以外的节点，
							// 则此链接节点不为单独存在
							return false;
						}
					}
					return true;
				}
			} while (!isContainerNode(node));
		}
		return false;
	}

	/**
	 * 节点是否是修饰节点(如：font，color等)
	 * 
	 * @param node
	 * @return
	 */
	public static boolean isDecoratedNode(Node node) {
		if (config.getDecoratedTagList().contains(
				node.getNodeName().toLowerCase())) {
			return true;
		}
		return false;
	}

	/**
	 * 用以判断一个节点是否是标题节点
	 * 
	 * @param node
	 * @return
	 */
	public static boolean isTitle(Node node) {
		if (node.getNodeType() == Node.TEXT_NODE) {
			return isTitle(node.getParentNode());
		} else if (config.getTitleTagList().contains(
				node.getNodeName().toLowerCase())) {
			return true;
		}
		return false;
	}

	/**
	 * 用以判断是否是频道链接中间的连接字符（如：国际>军事 中的字符'>'）
	 * 
	 * @param text
	 * @return
	 */
	public static boolean isMarkChannelTag(String text) {
		if (config.getChannelTagList().contains(text)) {
			return true;
		}
		return false;
	}

	/**
	 * 返回解析日期的正则表达式
	 * 
	 * @return
	 */
	public static String getDateRegex() {
		return config.getDateRegex();
	}

	/**
	 * 返回判断内容节点的阈值参数
	 * 
	 * @return
	 */
	public static double getContentThreshold() {
		return config.getContentThreshold();
	}

	/**
	 * 找出一个容器节点最大的子容器节点（判断节点大小的根据为容器节点所含的文本字符数目）
	 * 
	 * @param node
	 * @return
	 */
	public static ContainerNode findMaxSubNode(ContainerNode node) {
		List<NewsNode> childrenNode = node.getChildrenNodes();
		List<ContainerNode> candidateNode = new LinkedList<ContainerNode>();
		if (childrenNode != null) {
			for (NewsNode child : childrenNode) {
				if (child.getNodeType() == ContainerNode.TYPE) {
					candidateNode.add((ContainerNode) child);
				}
			}
			Collections.sort(candidateNode, new Comparator<ContainerNode>() {
				public int compare(ContainerNode o1, ContainerNode o2) {
					if (o1.getValueTextLength() > o2.getValueTextLength())
						return -1;
					if (o1.getValueTextLength() < o2.getValueTextLength())
						return 1;
					return 0;
				}
			});
			// if (candidateNode.size() > 0) {
			// for (int i = 0; i < candidateNode.size(); i++) {
			// if ((((double) candidateNode.get(i).getValueTextLength()) /
			// ((double) candidateNode
			// .get(i).getLinkedTextLength() + (double) candidateNode
			// .get(i).getValueTextLength())) > 0.65)
			// return candidateNode.get(i);
			// }
			// }
			if (candidateNode.size() > 0)
				return candidateNode.get(0);
		}
		return null;
	}

	/**
	 * 打印newsNode节点信息
	 * 
	 * @param node
	 * @param prefix
	 */
	public static void print(NewsNode node, String prefix) {

		if (null == node)
			return;

		if (node.getNodeType() == ContainerNode.TYPE) {
			System.out.println(prefix + "<" + node.getNodeName() + "  "
					+ ((ContainerNode) node).getValueTextLength() + " "
					+ ((ContainerNode) node).getLinkedTextLength() + ">");

			if (node.hasChildrenNode()) {
				List<NewsNode> nodeList = node.getChildrenNodes();
				for (int i = 0; i < nodeList.size(); i++) {
					print(nodeList.get(i), prefix + "\t");
				}
			}

			System.out.println(prefix + "</" + node.getNodeName() + ">");
		} else if (node.getNodeType() == BranchNode.TYPE) {
			System.out.println(prefix + "<" + node.getNodeName() + "/>");
		} else {
			String aloneInfo = "";
			if (node.getNodeType() == LinkedNode.TYPE) {
				if (((LinkedNode) node).isAlone())
					aloneInfo = "alone";
				else
					aloneInfo = "notAlone";
			}
			System.out.print(prefix + "<" + node.getNodeName() + "  "
					+ (node.isCenter() ? "center " : "notCenter ") + aloneInfo
					+ " >");
			System.out.print(node.getNodeValue());
			System.out.print("</" + node.getNodeName() + ">");
			System.out.println();
		}
	}
}
