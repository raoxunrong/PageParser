package backstage.webcrawler.transport.http;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.cyberneko.html.parsers.DOMParser;
import org.drools.common.NodeMemory;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import backstage.webcrawler.newsparser.util.DOMContentUtils;

class HTTPUtils {

	private static final String CHARSET_ATTR_NAME = "charset=";
	/**
	 * Extracts MIME type. Ideally the value should be extracted from HTTP
	 * header. But if it is missing an attempt can be made to determine content
	 * type based on URL and/or data.
	 * 
	 * @param contentTypeHeaderValue
	 * @param url
	 *            document URL.
	 * @param data
	 *            document content
	 * 
	 * @return MIME type for document content or null if couldn't determine the
	 *         type.
	 */
	public static String getContentType(String contentTypeHeaderValue,
			String url, byte[] data) {
		String type = null;
		if (contentTypeHeaderValue != null
				&& contentTypeHeaderValue.trim().length() > 0) {
			int i = contentTypeHeaderValue.indexOf(";");
			if (i > -1) {
				type = contentTypeHeaderValue.substring(0, i);
			} else {
				type = contentTypeHeaderValue.substring(0);
			}
		}

		if (type == null) {
			/*
			 * here url and content itself can be used to determine content
			 * type.
			 */
		}

		return type;
	}

	/**
	 * Extracts charset from HTTP header. If HTTP header is missing an attempt
	 * can be made to determine charset based on content type and data.
	 * 
	 * For example, documents with type 'text/html' can define document charset
	 * using 'meta' tag. Such documents should use characters compatible with
	 * ISO-8859-1 charset until the meta tag that defines document charset. For
	 * more details see: http://www.w3.org/TR/html4/charset.html#h-5.2.2
	 * 
	 * @param contentTypeHeaderValue
	 * @param contentType
	 *            type of data. Can be used to interpret the data.
	 * @param data
	 * @return charset or null.
	 */
	public static String getCharset(String contentTypeHeaderValue,
			String contentType, byte[] data) {
		String charset = getCharset(contentTypeHeaderValue);
		if (charset == null || charset.trim().length() == 0) {
			// add by raoxunrong, find the charset in the content
			InputStream contentBytes = new ByteArrayInputStream(data);
			InputStreamReader characterStream = null;
			try {
				characterStream = new InputStreamReader(contentBytes, "GBK");
			} catch (UnsupportedEncodingException e) {
				try {
					characterStream = new InputStreamReader(contentBytes,
							"UTF-8");
				} catch (UnsupportedEncodingException e1) {
					return null;
				}
			}
			InputSource inputSource = new InputSource();
			inputSource.setCharacterStream(characterStream);
			DOMParser parser = new DOMParser();
			try {
				parser.parse(inputSource);
			} catch (Exception e) {
				return null;
			}
			Node node = parser.getDocument();
			charset = getCharset(node);
		}

		return charset;
	}

	private static String getCharset(Node node) {
		if (node != null){
			org.w3c.dom.Document doc = DOMContentUtils.getDocumentNode(node);
	        NodeList nodeList = doc.getElementsByTagName("meta");
	        for(int i=0; i<nodeList.getLength(); i++){
	        	Node tempNode = nodeList.item(i); 
	        	NamedNodeMap attributes = tempNode.getAttributes();
	        	for(int j=0; j<attributes.getLength(); j++){
					System.out.println("Attribute " + j + " is: " + attributes.item(j));
					if(attributes.item(j).getNodeValue() != null){
						int k = (attributes.item(j).getNodeValue()).toLowerCase().indexOf(CHARSET_ATTR_NAME);
						if (k > -1) {
							return attributes.item(j).getNodeValue().substring(
									k + CHARSET_ATTR_NAME.length()).toUpperCase();
						}
					}
					
				}
	        }
		}
		
		return null;
        
	}

	private static String getCharset(String contentTypeHeaderValue) {
		String charset = null;
		String ATTR_NAME = "charset=";
		if (contentTypeHeaderValue != null) {
			int i = contentTypeHeaderValue.toLowerCase().indexOf(ATTR_NAME);
			if (i > -1) {
				charset = contentTypeHeaderValue.substring(
						i + ATTR_NAME.length()).toUpperCase();
			}
		}

		return charset;
	}

	/**
	 * Decodes content according to content encoding. This is just a place
	 * holder.
	 * 
	 * @param contentEncoding
	 *            content type.
	 * @param encodedContent
	 *            content received from the server
	 * @return decoded content.
	 */
	public static byte[] decodeContent(String contentEncoding,
			byte[] encodedContent) throws HTTPTransportException {
		byte[] decodedContent = null;
		if ("gzip".equalsIgnoreCase(contentEncoding)) {
			throw new HTTPTransportException(
					"Content-Encoding 'gzip' is not supported.");
		} else if ("deflate".equalsIgnoreCase(contentEncoding)) {
			throw new HTTPTransportException(
					"Content-Encoding 'deflate' is not supported.");
		} else if ("compress".equalsIgnoreCase(contentEncoding)) {
			throw new HTTPTransportException(
					"Content-Encoding 'compress' is not supported.");
		} else {
			decodedContent = encodedContent;
		}

		return decodedContent;
	}
}
