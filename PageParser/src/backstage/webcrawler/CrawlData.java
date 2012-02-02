package backstage.webcrawler;


import java.io.File;

import backstage.webcrawler.db.FetchedDocsDB;
import backstage.webcrawler.db.KnownUrlDB;
import backstage.webcrawler.db.PageLinkDB;
import backstage.webcrawler.db.ProcessedDocsDB;

public class CrawlData {
    
    private File crawlRootDir;
    
    private FetchedDocsDB fetchedDocsDB;
    private ProcessedDocsDB processedDocsDB;
    private KnownUrlDB knownUrlsDB;
    private PageLinkDB pageLinkDB;
    
    public File getCrawlRootDir() {
        return crawlRootDir;
    }

    public KnownUrlDB getKnownUrlsDB() {
        return knownUrlsDB;
    }

    public PageLinkDB getPageLinkDB() {
        return pageLinkDB;
    }

    public FetchedDocsDB getFetchedDocsDB() {
        return fetchedDocsDB;
    }

    public ProcessedDocsDB getProcessedDocsDB() {
        return processedDocsDB;
    }

    public CrawlData(String rootDir) {
        this.crawlRootDir = new File(rootDir);
        crawlRootDir.mkdirs();
        
        File fetchedDocsDBRoot = new File(crawlRootDir, "fetched");
        this.fetchedDocsDB = new FetchedDocsDB(fetchedDocsDBRoot);
        
        File processedDocsDBRoot = new File(crawlRootDir, "processed");
        this.processedDocsDB = new ProcessedDocsDB(processedDocsDBRoot);
        
        File knownUrlsDBRoot = new File(crawlRootDir, "knownurls");
        this.knownUrlsDB = new KnownUrlDB(knownUrlsDBRoot);

        File pageLinkDBRoot = new File(crawlRootDir, "pagelinks");
        this.pageLinkDB = new PageLinkDB(pageLinkDBRoot);
    }
    
    public void init() {
        this.fetchedDocsDB.init();
        this.processedDocsDB.init();
        this.knownUrlsDB.init();
        this.pageLinkDB.init();
    }
    
    public void delete() {
        this.fetchedDocsDB.delete();
        this.processedDocsDB.delete();
        this.knownUrlsDB.delete();
        this.pageLinkDB.delete();
    }
}
