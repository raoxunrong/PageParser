package backstage.webcrawler.newsparser.util;


import java.net.MalformedURLException;
import java.net.URL;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import backstage.domain.DocStatisticsInfo;

public class DOMContentUtils {

	/**
	 * This method takes a {@link StringBuffer} and a DOM {@link Node}, and will
	 * append all the content text found beneath the DOM node to the
	 * <code>StringBuffer</code>.
	 * 
	 * <p>
	 * 
	 * If <code>abortOnNestedAnchors</code> is true, DOM traversal will be
	 * aborted and the <code>StringBuffer</code> will not contain any text
	 * encountered after a nested anchor is found.
	 * 
	 * <p>
	 * 
	 * @return true if nested anchors were found
	 */
	public static final boolean getText(StringBuffer sb, Node node,
			boolean abortOnNestedAnchors) {
		if (getTextHelper(sb, node, abortOnNestedAnchors, 0)) {
			return true;
		}
		return false;
	}

	/**
	 * This is a convinience method, equivalent to
	 * {@link #getText(StringBuffer,Node,boolean) getText(sb, node, false)}.
	 * 
	 */
	public static final void getText(StringBuffer sb, Node node) {
		getText(sb, node, false);
	}

	public static final void getUnlinkedText(StringBuffer sb, Node node) {
		getUnlinkedTextHelper(sb, node);
	}

	public static final boolean getUnlinkedTextHelper(StringBuffer sb, Node node) {
		if (NodeTypeTool.isIgnoreNode(node, "")) {
			return false;
		}
		if (NodeTypeTool.isLinkNode(node)) {
			return false;
		}
		
		if (node.getNodeType() == Node.TEXT_NODE) {
			// cleanup and trim the value
			String text = node.getNodeValue();
			text = text.replaceAll("\\s+", " ");
			text = text.trim();
			if (text.length() > 0) {
				if (sb.length() > 0)
					sb.append(' ');
				sb.append(text);
			}
		}
		boolean abort = false;
		NodeList children = node.getChildNodes();
		if (children != null) {
			int len = children.getLength();
			for (int i = 0; i < len; i++) {
				if (getUnlinkedTextHelper(sb, children.item(i))) {
					abort = true;
					break;
				}
			}
		}
		return abort;
	}

	// returns true if abortOnNestedAnchors is true and we find nested
	// anchors
	public static final boolean getTextHelper(StringBuffer sb, Node node,
			boolean abortOnNestedAnchors, int anchorDepth) {
		if (NodeTypeTool.isIgnoreNode(node, "")) {
			return false;
		}

		if (abortOnNestedAnchors
				&& ("a".equalsIgnoreCase(node.getNodeName()) || "go"
						.equalsIgnoreCase(node.getNodeName()))) {
			anchorDepth++;
			if (anchorDepth > 1)
				return true;
		}
	
		if (node.getNodeType() == Node.TEXT_NODE) {
			// cleanup and trim the value
			String text = node.getNodeValue();
			text = text.replaceAll("\\s+", " ");
			text = text.trim();
			if (text.length() > 0) {
				if (sb.length() > 0)
					sb.append(' ');
				sb.append(text);
			}
		}
		boolean abort = false;
		NodeList children = node.getChildNodes();
		if (children != null) {
			int len = children.getLength();
			for (int i = 0; i < len; i++) {
				if (getTextHelper(sb, children.item(i), abortOnNestedAnchors,
						anchorDepth)) {
					abort = true;
					break;
				}
			}
		}
		return abort;
	}

	public static final Node findParentContainer(Node childNode) {
			return findParentContainerInternal(childNode.getParentNode());
	}
	
	private static final Node findParentContainerInternal(Node childNode){
		if (childNode != null) {
			if (NodeTypeTool.isContainerNode(childNode)) {
				return childNode;
			} else {
				return findParentContainer(childNode.getParentNode());
			}
		}
		return null;
	}
	
	/** If Node contains a BASE tag then it's HREF is returned. */
	public static final URL getBase(Node node) {

		// is this node a BASE tag?
		if (node.getNodeType() == Node.ELEMENT_NODE) {

			if ("body".equalsIgnoreCase(node.getNodeName())) // stop after HEAD
				return null;

			if ("base".equalsIgnoreCase(node.getNodeName())) {
				NamedNodeMap attrs = node.getAttributes();
				for (int i = 0; i < attrs.getLength(); i++) {
					Node attr = attrs.item(i);
					if ("href".equalsIgnoreCase(attr.getNodeName())) {
						try {
							return new URL(attr.getNodeValue());
						} catch (MalformedURLException e) {
						}
					}
				}
			}
		}

		// does it contain a base tag?
		NodeList children = node.getChildNodes();
		if (children != null) {
			int len = children.getLength();
			for (int i = 0; i < len; i++) {
				URL base = getBase(children.item(i));
				if (base != null)
					return base;
			}
		}

		// no.
		return null;
	}

	public static final boolean isTitleInfo(String title, String checkedInfo) {
		if (title == null || checkedInfo == null || checkedInfo.length() < 5) {
			return false;
		}

		if (title.startsWith(checkedInfo.substring(0, (checkedInfo.length()/2) + 1))) {
			return true;
		}
		return false;
	}
	
	public static org.w3c.dom.Document getDocumentNode(Node node) {
        if( node == null ) {
            return null;
        }
        
        if( Node.DOCUMENT_NODE == node.getNodeType() ) {
            return (org.w3c.dom.Document) node;
        }
        else {
            return node.getOwnerDocument();
        }
    }
	
	public static boolean isSubjectPage(Node node, String url){
		//TODO justify the page is subject page or not
		
		DocStatisticsInfo docStatisticsInfo = getStatisticsInfo(url, node);
		
		System.out.println("Page Statistics is listed below:");
		System.out.println("url is: " + docStatisticsInfo.getUrl());
		System.out.println("text length is: " + docStatisticsInfo.getTextLength());
		System.out.println("linked text length is: " + docStatisticsInfo.getLinkedTextLength());
		System.out.println("link count is: " + docStatisticsInfo.getLinkCount());
		System.out.println("*************************************************");
		return true;
	}
	
	public static DocStatisticsInfo getStatisticsInfo(String url, Node node){
		DocStatisticsInfo doc = new DocStatisticsInfo(url);
		extractDocStatisticsInfo(node, doc);
		return doc;
	}
	
	private static void extractDocStatisticsInfo(Node node, DocStatisticsInfo docStatisticsInfo){
		if (NodeTypeTool.isIgnoreNode(node, "")) {
			return;
		}
		
		if (NodeTypeTool.isLinkNode(node)) {
			docStatisticsInfo.addLinkCount(1);
			docStatisticsInfo.addLinkedTextLength(StringUtil.ReplaceBlankFormat(getLinkedText(node), "").length());
		}
		
		if (NodeTypeTool.isTextNode(node)) {
			docStatisticsInfo.addTextLength(StringUtil.ReplaceBlankFormat(node.getNodeValue(), "").length());
		}
		
		NodeList children = node.getChildNodes();
		if (children != null) {
			int len = children.getLength();
			for (int i = 0; i < len; i++) {
				extractDocStatisticsInfo(children.item(i), docStatisticsInfo);
			}
		}
	}
	
	private static String getLinkedText(Node node) {
		if (!NodeTypeTool.isLinkNode(node)){
			return null;
		}
		
		StringBuffer strBuf = new StringBuffer();
		getText(strBuf, node);

		return strBuf.toString();
	}

	private static boolean hasOnlyWhiteSpace(Node node) {
		String val = node.getNodeValue();
		for (int i = 0; i < val.length(); i++) {
			if (!Character.isWhitespace(val.charAt(i)))
				return false;
		}
		return true;
	}
	
}
