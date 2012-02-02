package backstage.shell;


import java.io.File;

import backstage.lucene.LuceneIndexBuilder;
import backstage.webcrawler.CrawlData;
import backstage.webcrawler.CrawlDataProcessor;
import backstage.webcrawler.utils.FileUtils;

public class LuceneIndexer {
	
	private String baseDir;
	
	private String luceneIndexDir;
	
	public LuceneIndexer(String dir) {
		
		baseDir = dir;
		luceneIndexDir = baseDir+System.getProperty("file.separator")+"lucene-index";
	}
	
    public void run() {
    
        // load existing data
        CrawlData crawlData = new CrawlData(baseDir);
        crawlData.init(); 
        
        File luceneIndexRootDir = new File(getLuceneDir());
        
        // Delete the index directory, if it exists
        FileUtils.deleteDir(luceneIndexRootDir);
        luceneIndexRootDir.mkdirs();
        
        CrawlDataProcessor luceneIndexBuilder = new LuceneIndexBuilder(luceneIndexRootDir, crawlData);
        
        System.out.print("Starting the indexing ... ");
        
        luceneIndexBuilder.run();

        System.out.println("Indexing completed! \n");        
    }
    
    public String getLuceneDir() {
    	
    	return luceneIndexDir;
    }
    
}
