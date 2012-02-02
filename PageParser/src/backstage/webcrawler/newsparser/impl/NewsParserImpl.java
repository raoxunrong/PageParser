package backstage.webcrawler.newsparser.impl;

import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import backstage.webcrawler.newsparser.News;
import backstage.webcrawler.newsparser.base.NewsParser;
import backstage.webcrawler.newsparser.conf.NewsParserConfig;
import backstage.webcrawler.newsparser.util.DOMContentUtils;
import backstage.webcrawler.newsparser.util.NodeTypeTool;
import backstage.webcrawler.newsparser.util.StringUtil;


/**
 * 
 * @author rxr created at 2011-10-26
 * 
 */
public class NewsParserImpl implements NewsParser {

	private News news;

	private StringBuffer titleBuf = new StringBuffer();
	private StringBuffer publishedTime = new StringBuffer();
	private StringBuffer originSource = new StringBuffer();
	private StringBuffer mainContent = new StringBuffer();

	@Override
	public boolean extractNews(String url, LinkedList<Node> domTrees,
			boolean extractOrigin) {

		return false;
	}

	@Override
	public boolean extractNews(String url, Node domTree, boolean extractOrigin) {
		rootNode = domTree;
		if (getTitle(rootNode)) {
			if (!findMainContainerNode(rootNode)) {
				mainContainerNode = rootNode;
			}
		}

		System.out.println("Title is " + titleBuf.toString());

		getPublishedTime(mainContainerNode);
		System.out.println("PublishedTime is " + publishedTime.toString());

		if (extractOrigin) {
			getOriginSource(mainContainerNode);
			System.out.println("OriginSource is " + originSource.toString());
		}

		getMainContent(mainContainerNode);
		while (mainContent.length() < NewsParserConfig.get()
				.getContentMinLength()) {
			System.out
					.println("Temp Main content is " + mainContent.toString());
			mainContent.setLength(0);
			mainContainerNode = DOMContentUtils
					.findParentContainer(mainContainerNode);
			getMainContent(mainContainerNode);
		}
		System.out.println("Main content is " + mainContent.toString());
		return false;
	}

	@Override
	public String getChannel() {
		if (news != null)
			return news.getChannel();
		return null;
	}

	@Override
	public String getContent() {
		if (news != null)
			return news.getContent();
		return null;
	}

	@Override
	public String getDate() {
		if (news != null)
			return news.getDate();
		return null;
	}

	@Override
	public String getOrigin() {
		if (news != null)
			return news.getOrigin();
		return null;
	}

	@Override
	public String getTitle() {
		if (news != null)
			return news.getTitle();
		return null;
	}

	public News getNews() {
		return news;
	}

	private boolean extractContent() {
		return false;
	}

	private boolean extractTitle(Node node) {
		return false;
	}

	private boolean extractDate() {
		return false;
	}

	private boolean extractOrigin() {
		return false;
	}

	/****************************************************************************/

	private Node rootNode;
	private Node titleNode;
	private Node publishedTimeNode;
	private Node originSourceNode;
	private Node mainContainerNode;

	private static Pattern dateRegPattern;
	private static Pattern originRegPattern;

	static {
		dateRegPattern = Pattern.compile(NewsParserConfig.get().getDateRegex());
		originRegPattern = Pattern.compile(NewsParserConfig.get()
				.getOriginRegex());
	}

	/**
	 * This method takes a {@link StringBuffer} and a DOM {@link Node}, and will
	 * append the content text found beneath the first <code>title</code> node
	 * to the <code>StringBuffer</code>.
	 * 
	 * @return true if a title node was found, false otherwise
	 */
	private final boolean getTitle(Node node) {
		if (node == null) {
			return false;
		}

		org.w3c.dom.Document doc = DOMContentUtils.getDocumentNode(node);
		NodeList nodeList = doc.getElementsByTagName("title");
		Node matchedNode = nodeList.item(0);
		if (matchedNode != null) {
			String title = matchedNode.getTextContent();
			if (title != null) {
				titleBuf.append(title);
				titleNode = matchedNode;
				return true;
			}
		}
		return false;
	}

	private final boolean getPublishedTime(Node node) {
		if (publishedTimeNode != null)
			return true;

		if (node == null) {
			return false;
		}

		boolean abort = false;
		if (NodeTypeTool.isIgnoreNode(node, ""))
			return false;

		if (NodeTypeTool.isLinkNode(node)) {
			return false;
		}

		if (node.getNodeType() == Node.TEXT_NODE) {
			String text = StringUtil.EliminateBlankFormat(node.getNodeValue());

			Matcher matcher = dateRegPattern.matcher(text);
			if (matcher.find()) {
				publishedTime.append(node.getNodeValue());
				publishedTimeNode = node;
				abort = true;
			}
		} else {
			NodeList children = node.getChildNodes();
			if (children != null) {
				int len = children.getLength();
				for (int i = 0; i < len; i++) {
					if (getPublishedTime(children.item(i))) {
						abort = true;
						break;
					}
				}
			}
		}
		return abort;
	}

	private final boolean getOriginSource(Node node) {
		if (node == null) {
			return false;
		}

		boolean abort = false;
		if (NodeTypeTool.isIgnoreNode(node, ""))
			return false;

		if (node.getNodeType() == Node.TEXT_NODE) {
			String text = StringUtil.EliminateBlankFormat(node.getNodeValue());

			Matcher matcher = originRegPattern.matcher(text);
			if (matcher.find()) {
				originSource.append(text);
				originSourceNode = node;
				abort = true;
			}
		} else {
			NodeList children = node.getChildNodes();
			if (children != null) {
				int len = children.getLength();
				for (int i = 0; i < len; i++) {
					if (getOriginSource(children.item(i))) {
						abort = true;
						break;
					}
				}
			}
		}
		return abort;
	}

	private final boolean getMainContent(Node node) {
		String tempText = null;
		while (!isContentEnd(node)) {
			if (NodeTypeTool.isBranchNode(node) && mainContent.length() > 0)
				mainContent.append("\n");
			if (NodeTypeTool.isIgnoreNode(node, null)) {
				return false;
			}

			if (NodeTypeTool.isLinkNode(node)) {
				// TODO handler
			}

			if (node.getNodeType() == Node.TEXT_NODE) {
				// cleanup and trim the value
				tempText = StringUtil.EliminateBlankFormat(node.getNodeValue());

				mainContent.append(' ').append(tempText);

			}

			boolean abort = false;
			NodeList children = node.getChildNodes();
			if (children != null) {
				int len = children.getLength();
				for (int i = 0; i < len; i++) {
					if (getMainContent(children.item(i))) {
						abort = true;
						break;
					}
				}
			}
			return abort;
		}
		return true;

	}

	private final boolean findMainContainerNode(Node rootNode) {
		if (titleNode == null) {
			return false;
		}

		if (rootNode == null) {
			return false;
		}

		boolean abort = false;
		if (NodeTypeTool.isIgnoreNode(rootNode, ""))
			return false;

		if (rootNode == titleNode) {
			return false;
		}

		if (rootNode.getNodeType() == Node.TEXT_NODE) {
			String text = StringUtil.EliminateBlankFormat(rootNode
					.getNodeValue());

			if (DOMContentUtils.isTitleInfo(titleBuf.toString().trim(), text)) {
				titleBuf.setLength(0);
				titleBuf.append(text);
				mainContainerNode = DOMContentUtils
						.findParentContainer(rootNode);
				abort = true;
			} else {
				Matcher matcher = dateRegPattern.matcher(text);
				if (matcher.find()) {
					publishedTime.append(rootNode.getNodeValue());
					publishedTimeNode = rootNode;
					mainContainerNode = DOMContentUtils
							.findParentContainer(rootNode);
					abort = true;
				}
			}
		} else {
			NodeList children = rootNode.getChildNodes();
			if (children != null) {
				int len = children.getLength();
				for (int i = 0; i < len; i++) {
					if (findMainContainerNode(children.item(i))) {
						abort = true;
						break;
					}
				}
			}
		}
		return abort;
	}

	private final boolean isContentEnd(Node node) {
		if (node.getNodeType() == Node.TEXT_NODE) {
			String text = StringUtil.EliminateBlankFormat(node.getNodeValue());
			if (NewsParserConfig.get().getFilterStrList().contains(text)) {
				return true;
			}
		}
		return false;
	}
}
