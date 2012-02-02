package backstage.webcrawler.newsparser.util;

public class StringUtil {

	public static final char SeparatingChar = '|';

	/**
	 * cleanup and trim the value
	 * 
	 * @param originalStr
	 */
	public static String EliminateBlankFormat(String originalStr) {
		if (originalStr == null)
			return null;
		String targetStr = originalStr.replaceAll("\\s+", " ");// 清除掉空白字符,以半角空格替换之
		targetStr = targetStr.replaceAll("　", " ");// 消除掉中文字符中的全角空格
		targetStr = targetStr.replaceAll(" ", "");// 消除掉半角空格
		targetStr = targetStr.replaceAll("\u00a0", "");// 消除掉nekohtml在网页中通过&nbsp转换的ASCII为\u00a0的字符
		targetStr = targetStr.trim();
		return targetStr;
	}

	/**
	 * use 'replace' to replace all blank char
	 * 
	 * @param originalStr
	 * @param replace
	 * @return
	 */
	public static String ReplaceBlankFormat(String originalStr, String replace) {
		if (originalStr == null)
			return null;
		String targetStr = originalStr.replaceAll("\\s+", replace);// 清除掉空白字符
		targetStr = targetStr.replaceAll("　", replace);// 消除掉中文字符中的全角空格
		targetStr = targetStr.replaceAll(" ", replace);// 消除掉中文字符中的全角空格
		targetStr = targetStr.replaceAll("\u00a0", replace);// 消除掉nekohtml在网页中通过&nbsp转换的ASCII为\u00a0的字符
		targetStr = targetStr.trim();
		return targetStr;
	}

	/**
	 * 消除文本中所有的换行符
	 * 
	 * @param sourceStr
	 * @return
	 */
	public static String RemoveAllLineBreaker(String sourceStr) {
		return sourceStr.replaceAll("\\r\\n", "");
	}

}
