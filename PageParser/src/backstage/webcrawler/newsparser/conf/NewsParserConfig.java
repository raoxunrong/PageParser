package backstage.webcrawler.newsparser.conf;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class NewsParserConfig {

	private static Document doc;

	private static final String defaultConfigPath = "./conf/newsParser-config.xml";

	private static NewsParserConfig instance = new NewsParserConfig();

	private static List<String> ignoreTagList = null;

	private static List<String> containerTagList = null;

	private static List<IgnoreTag> specificIgnoreTagList = null;

	private static List<String> linkTagList = null;

	private static List<String> channelTagList = null;

	private static List<String> titleTagList = null;

	private static List<String> branchTagList = null;

	private static List<String> decoratedTagList = null;

	private static List<String> originRegexList = null;

	private static String originRegex = null;

	private static List<String> dateRegexList = null;

	private static List<String> dateParserList = null;

	private static String dateRegex = null;

	private static List<String> filterStrList = null;

	private static double contentThreshold = 0.0d;

	private static int contentMinLength = 0;
	
	private static int minContentBetweenLink = 0;

	public static NewsParserConfig get() {
		return instance;
	}

	public static void load() {
		get();
	}

	public NewsParserConfig() {
		try {
			InputSource inputSource = new InputSource(defaultConfigPath);
			inputSource.setEncoding("UTF-8");
			doc = DocumentBuilderFactory.newInstance().newDocumentBuilder()
					.parse(inputSource);
			System.out.println("doc type is " + doc.getDoctype());
//			Element rootElement = doc.getDocumentElement();
//			printTagName(rootElement, "");
		} catch (Exception e) {
			doc = null;
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	// test, should be removed
//	private void printTagName(Element rootElement, String parentTagName) {
//		System.out.println(parentTagName);
//		System.out.println(rootElement.getTagName() + ": "
//				+ rootElement.getNodeValue());
//		NodeList children = rootElement.getChildNodes();
//		if (children.getLength() > 0) {
//			for (int i = 0; i < children.getLength(); i++) {
//				if (children.item(i).getNodeType() == Node.ELEMENT_NODE)
//					printTagName((Element) children.item(i), parentTagName
//							+ "-" + rootElement.getTagName());
//			}
//		}
//	}

	public List<String> getIgnoreTagList() {
		if (ignoreTagList == null) {
			ignoreTagList = new LinkedList<String>();
			NodeList nl = doc.getElementsByTagName("ingoreTag");
			if (nl.getLength() == 0) {
				System.out.println("ignoreTagList's length is "
						+ nl.getLength());
				return null;
			}
			Node root = nl.item(0);
			Node child = root.getFirstChild();
			String ignoreTag = "";
			do {
				if (child.getNodeType() == Node.TEXT_NODE) {
					child = child.getNextSibling();
					continue;
				}
				if (child.getFirstChild() != null)
					ignoreTag = child.getFirstChild().getNodeValue();

				ignoreTagList.add(ignoreTag);
				child = child.getNextSibling();
			} while (child != null);
		}
		return ignoreTagList;
	}

	public List<String> getContainerTagList() {
		if (containerTagList == null) {
			containerTagList = new LinkedList<String>();
			NodeList nl = doc.getElementsByTagName("containerTag");
			Node root = nl.item(0);
			if (root == null) {
				return containerTagList;
			}
			Node child = root.getFirstChild();
			String containerTag = "";
			do {
				if (child.getNodeType() == Node.TEXT_NODE) {
					child = child.getNextSibling();
					continue;
				}
				if (child.getFirstChild() != null)
					containerTag = child.getFirstChild().getNodeValue();

				containerTagList.add(containerTag);
				child = child.getNextSibling();
			} while (child != null);
		}
		return containerTagList;
	}

	public List<String> getDecoratedTagList() {
		if (decoratedTagList == null) {
			decoratedTagList = new LinkedList<String>();
			NodeList nl = doc.getElementsByTagName("decoratedTag");
			Node root = nl.item(0);
			Node child = root.getFirstChild();
			String decoratedTag = "";
			do {
				if (child.getNodeType() == Node.TEXT_NODE) {
					child = child.getNextSibling();
					continue;
				}
				if (child.getFirstChild() != null)
					decoratedTag = child.getFirstChild().getNodeValue();

				decoratedTagList.add(decoratedTag);
				child = child.getNextSibling();
			} while (child != null);
		}
		return decoratedTagList;
	}

	public List<String> getLinkTagList() {
		if (linkTagList == null) {
			linkTagList = new LinkedList<String>();
			NodeList nl = doc.getElementsByTagName("linkTag");
			Node root = nl.item(0);
			if (root == null) {
				return linkTagList;
			}
			Node child = root.getFirstChild();
			String linkTag = "";
			do {
				if (child.getNodeType() == Node.TEXT_NODE) {
					child = child.getNextSibling();
					continue;
				}
				if (child.getFirstChild() != null)
					linkTag = child.getFirstChild().getNodeValue();

				linkTagList.add(linkTag);
				child = child.getNextSibling();
			} while (child != null);
		}
		return linkTagList;
	}

	public List<String> getFilterStrList() {
		if (filterStrList == null) {
			filterStrList = new LinkedList<String>();
			NodeList nl = doc.getElementsByTagName("filterStr");
			Node root = nl.item(0);
			Node child = root.getFirstChild();
			String filterStr = "";
			do {
				if (child.getNodeType() == Node.TEXT_NODE) {
					child = child.getNextSibling();
					continue;
				}
				if (child.getFirstChild() != null)
					filterStr = child.getFirstChild().getNodeValue();

				filterStrList.add(filterStr);
				child = child.getNextSibling();
			} while (child != null);
		}
		return filterStrList;
	}

	public List<String> getChannelTagList() {
		if (channelTagList == null) {
			channelTagList = new LinkedList<String>();
			NodeList nl = doc.getElementsByTagName("channelTag");
			Node root = nl.item(0);
			if (root == null) {
				return channelTagList;
			}
			Node child = root.getFirstChild();
			String channelTag = "";
			do {
				if (child.getNodeType() == Node.TEXT_NODE) {
					child = child.getNextSibling();
					continue;
				}
				if (child.getFirstChild() != null)
					channelTag = child.getFirstChild().getNodeValue();

				channelTagList.add(channelTag);
				child = child.getNextSibling();
			} while (child != null);
		}
		return channelTagList;
	}

	public List<String> getTitleTagList() {
		if (titleTagList == null) {
			titleTagList = new LinkedList<String>();
			NodeList nl = doc.getElementsByTagName("titleTag");
			Node root = nl.item(0);
			if (root == null) {
				return titleTagList;
			}
			Node child = root.getFirstChild();
			String titleTag = "";
			do {
				if (child.getNodeType() == Node.TEXT_NODE) {
					child = child.getNextSibling();
					continue;
				}
				if (child.getFirstChild() != null)
					titleTag = child.getFirstChild().getNodeValue();

				titleTagList.add(titleTag);
				child = child.getNextSibling();
			} while (child != null);
		}
		return titleTagList;
	}

	public List<String> getBranchTagList() {
		if (branchTagList == null) {
			branchTagList = new LinkedList<String>();
			NodeList nl = doc.getElementsByTagName("branchTag");
			Node root = nl.item(0);
			if (root == null) {
				return branchTagList;
			}
			Node child = root.getFirstChild();
			String branchTag = "";
			do {
				if (child.getNodeType() == Node.TEXT_NODE) {
					child = child.getNextSibling();
					continue;
				}
				if (child.getFirstChild() != null)
					branchTag = child.getFirstChild().getNodeValue();

				branchTagList.add(branchTag);
				child = child.getNextSibling();
			} while (child != null);
		}
		return branchTagList;
	}

	public List<String> getOriginRegexList() {
		if (originRegexList == null) {
			originRegexList = new LinkedList<String>();
			NodeList nl = doc.getElementsByTagName("origin-regex");
			Node root = nl.item(0);
			if (root == null) {
				return originRegexList;
			}
			Node child = root.getFirstChild();
			String originRegex = "";
			do {
				if (child.getNodeType() == Node.TEXT_NODE) {
					child = child.getNextSibling();
					continue;
				}
				if (child.getFirstChild() != null)
					originRegex = child.getFirstChild().getNodeValue();

				originRegexList.add(originRegex);
				child = child.getNextSibling();
			} while (child != null);
		}
		return originRegexList;
	}

	public String getOriginRegex() {
		if (originRegex == null) {
			List<String> originRegexList = getOriginRegexList();
			StringBuffer strBuffer = new StringBuffer();
			for (String regex : originRegexList) {
				if (strBuffer.length() > 0)
					strBuffer.append('|');
				strBuffer.append(regex);
			}
			originRegex = strBuffer.toString();
		}
		return originRegex;
	}

	public List<String> getDateRegexList() {
		if (dateRegexList == null) {
			dateRegexList = new LinkedList<String>();
			NodeList nl = doc.getElementsByTagName("date-regex");
			Node root = nl.item(0);
			if (root == null) {
				return dateRegexList;
			}
			Node child = root.getFirstChild();
			String dateRegex = "";
			do {
				if (child.getNodeType() == Node.TEXT_NODE) {
					child = child.getNextSibling();
					continue;
				}
				if (child.getFirstChild() != null)
					dateRegex = child.getFirstChild().getNodeValue();

				dateRegexList.add(dateRegex);
				child = child.getNextSibling();
			} while (child != null);
			System.out.println("Date-regex length is " + dateRegexList.size());
		}
		return dateRegexList;
	}

	public String getDateRegex() {
		if (dateRegex == null) {
			List<String> dateRegexList = getDateRegexList();
			StringBuffer strBuffer = new StringBuffer();
			for (String regex : dateRegexList) {
				if (strBuffer.length() > 0)
					strBuffer.append('|');
				strBuffer.append(regex);
			}
			dateRegex = strBuffer.toString();
		}
		return dateRegex;
	}

	public double getContentThreshold() {
		if (contentThreshold <= 0) {
			NodeList nl = doc.getElementsByTagName("contentThreshold");
			Node child = nl.item(0).getFirstChild();
			if (child != null)
				contentThreshold = Double.parseDouble(child.getNodeValue());
		}
		return contentThreshold;
	}

	public int getContentMinLength() {
		if (contentMinLength <= 0) {
			NodeList nl = doc.getElementsByTagName("contentMinLength");
			Node child = nl.item(0).getFirstChild();
			if (child != null)
				contentMinLength = Integer.parseInt(child.getNodeValue());
		}
		return contentMinLength;
	}
	
	public int getMinContentBetweenLink() {
		if (minContentBetweenLink <= 0) {
			NodeList nl = doc.getElementsByTagName("minContentBetweenLink");
			Node child = nl.item(0).getFirstChild();
			if (child != null)
				minContentBetweenLink = Integer.parseInt(child.getNodeValue());
		}
		return minContentBetweenLink;
	}

	public List<IgnoreTag> getSpecificIgnoreTag(String siteName) {
		if (specificIgnoreTagList == null) {
			Node siteNode = getSiteNode(siteName);
			if (siteNode == null)
				return null;
			NodeList children = siteNode.getChildNodes();
			List<Node> rootList = new ArrayList<Node>();
			specificIgnoreTagList = new ArrayList<IgnoreTag>();
			if (children != null)
				for (int i = 0; i < children.getLength(); i++) {
					Node node = children.item(i);
					if (node.getNodeName().equalsIgnoreCase("ignoreTag")) {
						rootList.add(node);
					}
				}
			for (int i = 0; i < rootList.size(); i++) {
				NodeList temp = rootList.get(i).getChildNodes();
				IgnoreTag ignoreTag = new IgnoreTag();
				for (int j = 0; j < temp.getLength(); j++) {
					if (temp.item(j).getNodeName().equalsIgnoreCase("tag")) {
						ignoreTag.setTag(temp.item(j).getFirstChild()
								.getNodeValue());
					} else if (temp.item(j).getNodeName().equalsIgnoreCase(
							"attribute")) {
						ignoreTag.setAttribute(temp.item(j).getFirstChild()
								.getNodeValue());
					} else if (temp.item(j).getNodeName().equalsIgnoreCase(
							"value")) {
						ignoreTag.setValue(temp.item(j).getFirstChild()
								.getNodeValue());
					}
				}
				specificIgnoreTagList.add(ignoreTag);
			}

		}
		return specificIgnoreTagList;
	}

	private Node getSiteNode(String siteName) {
		NodeList nl = doc.getElementsByTagName("site");
		if (nl != null && nl.getLength() > 0) {
			for (int i = 0; i < nl.getLength(); i++) {
				if (nl.item(i).getAttributes().getNamedItem("name")
						.getNodeValue().equalsIgnoreCase(siteName)) {
					return nl.item(i);
				}
			}
		}
		return null;
	}

	public static void main(String[] args) {
		NewsParserConfig config = NewsParserConfig.get();
		System.out.println(config.getIgnoreTagList());
		System.out.println(config.getContainerTagList());
		System.out.println(config.getLinkTagList());
		System.out.println(config.getChannelTagList());
		System.out.println(config.getTitleTagList());
		System.out.println(config.getBranchTagList());
		System.out.println(config.getDateRegex());
		System.out.println(config.getOriginRegex());
		System.out.println(config.getContentThreshold());
		System.out.println(config.getFilterStrList());
		System.out.println(config.getDecoratedTagList());
		System.out.println(config.getSpecificIgnoreTag(""));
	}

	public class IgnoreTag {
		String tag = null;

		String attribute = null;

		String value = null;

		public IgnoreTag() {
			super();
		}

		public IgnoreTag(String tag, String attribute, String value) {
			super();
			this.tag = tag;
			this.attribute = attribute;
			this.value = value;
		}

		public String getAttribute() {
			return attribute;
		}

		public void setAttribute(String attribute) {
			this.attribute = attribute;
		}

		public String getTag() {
			return tag;
		}

		public void setTag(String tag) {
			this.tag = tag;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}

		@Override
		public String toString() {

			StringBuffer info = new StringBuffer();
			info.append(tag).append("\n").append(attribute).append("\n")
					.append(value);
			return info.toString();
		}

	}
}
