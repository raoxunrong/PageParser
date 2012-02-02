package backstage.webcrawler.newsparser.parser.common;

import backstage.webcrawler.model.FetchedDocument;
import backstage.webcrawler.model.ProcessedDocument;

/**
 * Interface for parsing document that was retrieved/fetched during
 * collection phase.  
 */
public interface DocumentParser {
    public ProcessedDocument parse(FetchedDocument doc) 
        throws DocumentParserException;
}
