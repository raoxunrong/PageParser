package backstage.shell;


import java.util.ArrayList;
import java.util.List;

import backstage.webcrawler.BasicWebCrawler;
import backstage.webcrawler.CrawlData;
import backstage.webcrawler.URLFilter;
import backstage.webcrawler.URLNormalizer;

public class FetchAndProcessCrawler {

	public static final int DEFAULT_MAX_DEPTH = 3;
	public static final int DEFAULT_MAX_DOCS = 1000;
	
	//INSTANCE VARIABLES
	// A reference to the crawled data
	CrawlData crawlData;
	
	// The location where we will store the fetched data
	String rootDir;

	// total number of iterations
    int maxDepth = DEFAULT_MAX_DEPTH;
    
    // max number of pages that will be fetched within every crawl/iteration.
    int maxDocs = DEFAULT_MAX_DOCS; 
        
    List<String> seedUrls;
    
    URLFilter urlFilter;
    
    public static void main(String[] args){
    	FetchAndProcessCrawler crawler = new FetchAndProcessCrawler("D:/pageparser/fetchedData", 1, 1);
    	//crawler.setDefaultUrls();
    	crawler.addUrl("http://news.sina.com.cn/c/2011-11-01/202423398248.shtml");
//    	crawler.addUrl("http://sports.sina.com.cn/o/2011-11-07/00245818977.shtml");
//    	crawler.addUrl("http://sports.sina.com.cn/g/2011-11-15/11445829770.shtml");
//    	crawler.addUrl("http://ent.sina.com.cn/y/2011-11-21/03573485809.shtml");
//    	crawler.addUrl("http://ent.sina.com.cn/s/h/2011-11-21/18213486746.shtml");
//    	crawler.addUrl("http://sports.sina.com.cn/l/2011-12-01/21395851855.shtml");
//    	crawler.addUrl("http://news.sina.com.cn");
    	
//    	crawler.addUrl("http://sports.sohu.com/20111107/n324735830.shtml");
//    	crawler.addUrl("http://sports.sohu.com/20111115/n325637374.shtml");
    	
//    	crawler.addUrl("http://sports.163.com/11/1114/21/7IRPAPDK00051CD5.html");
//    	crawler.addUrl("http://sports.163.com/11/1115/09/7IT183BA00051CCL.html");
    	
//    	crawler.addUrl("http://news.xinhuanet.com/politics/2011-11/16/c_111172628.htm");
    	
//    	crawler.addUrl("http://www.people.com.cn/h/2011/1117/c25408-1-3195264276.html");
    	
//    	crawler.addUrl("http://news.baidu.com/n?cmd=2&class=top&page=http%3A%2F%2Fpolitics.gmw.cn%2F2011-11%2F17%2Fcontent_2991223.htm&clk=rrel&cls=top&where=toppage");
    	
//    	crawler.addUrl("http://www.chinanews.com/ty/2011/11-16/3465419.shtml");
    	
//    	crawler.addUrl("http://baike.baidu.com/view/692.htm");
//    	crawler.addUrl("http://baike.baidu.com/view/1483859.htm");
    	
//    	crawler.addUrl("http://www.iteye.com/topic/154697"); need handle
    	
//    	crawler.addUrl("http://news.csdn.net/a/20111111/307340.html");
//    	crawler.addUrl("http://news.csdn.net/a/20111121/307731.html");
    	
//    	crawler.addUrl("http://hi.baidu.com/evilrapper/blog/item/77d9481f689c9eefe1fe0b6c.html");
    	
//    	crawler.addUrl("http://cache.baidu.com/c?m=9f65cb4a8c8507ed4fece763104c8c711923d030678197027fa3c215cc790c051c38a3e865604744ce95223a54b2121abdaf2b7732552bb191cbdc4ed7b0c32378df73637348d14342d748b8cb317f877fce4eacf259b1b5e743e4b9a2d0c82255dd24746d8081cc1b5754dd6f814764b4fadf&p=9d759a41d68113b10be29131465c&user=baidu&fm=sc&query=%CE%B1%D4%EC+User+Agent&qid=c8cd72f300e4047a&p1=2");
//    	crawler.addUrl("http://cache.baidu.com/c?m=9d78d513d9d430d84f9d97697b10c011684380127b96875568d4e21bc63307075321a3e52878564291d27d141cb20c19afe73605755066ed91df883d81ecd43569d43a67304ddc1a568458b2c84321c3&p=82769a428fdd19ee08e291284c&user=baidu&fm=sc&query=%CE%B1%D4%EC+User+Agent&qid=c8cd72f300e4047a&p1=5");
    	
//    	crawler.addUrl("http://hi.baidu.com/jabber/blog/category/Openfire");
//    	crawler.addUrl("http://hi.baidu.com/dairenjie2008/blog/item/303ab1eecd90d9c5b21cb1ec.html");
    	
//    	crawler.addUrl("http://apps.hi.baidu.com/share/detail/9221981");
//    	crawler.addUrl("http://apps.hi.baidu.com/share/detail/271631");
    	crawler.run();
    }

    public FetchAndProcessCrawler(String dir, int maxDepth, int maxDocs) {
    	
    	rootDir = dir;

    	if ( rootDir == null || rootDir.trim().length() == 0) {
    		
    		String prefix = System.getProperty("iweb2.home");
    		if (prefix == null) {
    			prefix = "..";
    		}

    		rootDir = System.getProperty("iweb2.home")+System.getProperty("file.separator")+"data";
    	}
    	
    	rootDir = rootDir+System.getProperty("file.separator")+"crawl-" + System.currentTimeMillis();
    	
    	this.maxDepth = maxDepth;
    	
    	this.maxDocs = maxDocs;
    	
    	this.seedUrls = new ArrayList<String>();
    	
    	/* default url filter configuration */
    	this.urlFilter = new URLFilter();
    	urlFilter.setAllowFileUrls(true);
    	urlFilter.setAllowHttpUrls(true);
    }
    
    
    
    public void run() {
    	        
    	crawlData = new CrawlData(rootDir);

        BasicWebCrawler webCrawler = new BasicWebCrawler(crawlData);
        webCrawler.addSeedUrls(getSeedUrls());

        webCrawler.setURLFilter(urlFilter);
        
    	long t0 = System.currentTimeMillis();

        /* run crawl */
        webCrawler.fetchAndProcess(maxDepth, maxDocs);

        System.out.println("Timer (s): [Crawler processed data] --> " + 
                (System.currentTimeMillis()-t0)*0.001);
    	
    }
    
    public List<String> getSeedUrls() { 

    	return seedUrls;
    }

    public void addUrl(String val) {
    	URLNormalizer urlNormalizer = new URLNormalizer();
    	seedUrls.add(urlNormalizer.normalizeUrl(val));
    }

    public void setAllUrls() {
    	
    	setDefaultUrls();
    	
    	// Include the spam pages ... all of them!
    	addUrl("file:///c:/iWeb2/data/ch02/spam-01.html");
    	addUrl("file:///c:/iWeb2/data/ch02/spam-biz-01.html");
    	addUrl("file:///c:/iWeb2/data/ch02/spam-biz-02.html");
    	addUrl("file:///c:/iWeb2/data/ch02/spam-biz-03.html");    	
    }
    
    public void setDefaultUrls() {
    	
    	addUrl("file:///d:/iWeb2/data/ch02/biz-01.html");
    	addUrl("file:///d:/iWeb2/data/ch02/biz-02.html");
    	addUrl("file:///d:/iWeb2/data/ch02/biz-03.html");
    	addUrl("file:///d:/iWeb2/data/ch02/biz-04.html");
    	addUrl("file:///d:/iWeb2/data/ch02/biz-05.html");
    	addUrl("file:///d:/iWeb2/data/ch02/biz-06.html");
    	addUrl("file:///d:/iWeb2/data/ch02/biz-07.html");
    	
    	addUrl("file:///d:/iWeb2/data/ch02/sport-01.html");
    	addUrl("file:///d:/iWeb2/data/ch02/sport-02.html");
    	addUrl("file:///d:/iWeb2/data/ch02/sport-03.html");

    	addUrl("file:///d:/iWeb2/data/ch02/usa-01.html");
    	addUrl("file:///d:/iWeb2/data/ch02/usa-02.html");
    	addUrl("file:///d:/iWeb2/data/ch02/usa-03.html");
    	addUrl("file:///d:/iWeb2/data/ch02/usa-04.html");

    	addUrl("file:///d:/iWeb2/data/ch02/world-01.html");
    	addUrl("file:///d:/iWeb2/data/ch02/world-02.html");
    	addUrl("file:///d:/iWeb2/data/ch02/world-03.html");
    	addUrl("file:///d:/iWeb2/data/ch02/world-04.html");
    	addUrl("file:///d:/iWeb2/data/ch02/world-05.html");
    	
    	setFilesOnlyUrlFilter();    	
	}
    
    public void setUrlFilter(URLFilter urlFilter) {
        this.urlFilter = urlFilter;
    }
    
    private void setFilesOnlyUrlFilter() {
        /* configure url filter to accept only file:// urls */
        URLFilter urlFilter = new URLFilter();
        urlFilter.setAllowFileUrls(true);
        urlFilter.setAllowHttpUrls(false);
        setUrlFilter(urlFilter);
    }
    
    public void setUrls(String val) {

        setFilesOnlyUrlFilter();
        
    	this.seedUrls.clear();
    	
    	if (val.equalsIgnoreCase("biz")) {
    		
        	addUrl("file:///c:/iWeb2/data/ch02/biz-01.html");
        	addUrl("file:///c:/iWeb2/data/ch02/biz-02.html");
        	addUrl("file:///c:/iWeb2/data/ch02/biz-03.html");
        	addUrl("file:///c:/iWeb2/data/ch02/biz-04.html");
        	addUrl("file:///c:/iWeb2/data/ch02/biz-05.html");
        	addUrl("file:///c:/iWeb2/data/ch02/biz-06.html");
        	addUrl("file:///c:/iWeb2/data/ch02/biz-07.html");
        	
    	} else if (val.equalsIgnoreCase("sport")) {
    		
        	addUrl("file:///c:/iWeb2/data/ch02/sport-01.html");
        	addUrl("file:///c:/iWeb2/data/ch02/sport-02.html");
        	addUrl("file:///c:/iWeb2/data/ch02/sport-03.html");

    	} else if (val.equalsIgnoreCase("usa")) {
    		
        	addUrl("file:///c:/iWeb2/data/ch02/usa-01.html");
        	addUrl("file:///c:/iWeb2/data/ch02/usa-02.html");
        	addUrl("file:///c:/iWeb2/data/ch02/usa-03.html");
        	addUrl("file:///c:/iWeb2/data/ch02/usa-04.html");

    	} else if (val.equalsIgnoreCase("world")) {
    		
        	addUrl("file:///c:/iWeb2/data/ch02/world-01.html");
        	addUrl("file:///c:/iWeb2/data/ch02/world-02.html");
        	addUrl("file:///c:/iWeb2/data/ch02/world-03.html");
        	addUrl("file:///c:/iWeb2/data/ch02/world-04.html");
        	addUrl("file:///c:/iWeb2/data/ch02/world-05.html");
    	} else if (val.equalsIgnoreCase("biz-docs")) {
            
            addUrl("file:///c:/iWeb2/data/ch02/biz-01.doc");
            addUrl("file:///c:/iWeb2/data/ch02/biz-02.doc");
            addUrl("file:///c:/iWeb2/data/ch02/biz-03.doc");
            addUrl("file:///c:/iWeb2/data/ch02/biz-04.doc");
            addUrl("file:///c:/iWeb2/data/ch02/biz-05.doc");
            addUrl("file:///c:/iWeb2/data/ch02/biz-06.doc");
            addUrl("file:///c:/iWeb2/data/ch02/biz-07.doc");
            
        } else if (val.equalsIgnoreCase("sport-docs")) {
            
            addUrl("file:///c:/iWeb2/data/ch02/sport-01.doc");
            addUrl("file:///c:/iWeb2/data/ch02/sport-02.doc");
            addUrl("file:///c:/iWeb2/data/ch02/sport-03.doc");

        } else if (val.equalsIgnoreCase("usa-docs")) {
            
            addUrl("file:///c:/iWeb2/data/ch02/usa-01.doc");
            addUrl("file:///c:/iWeb2/data/ch02/usa-02.doc");
            addUrl("file:///c:/iWeb2/data/ch02/usa-03.doc");
            addUrl("file:///c:/iWeb2/data/ch02/usa-04.doc");

        } else if (val.equalsIgnoreCase("world-docs")) {
            
            addUrl("file:///c:/iWeb2/data/ch02/world-01.doc");
            addUrl("file:///c:/iWeb2/data/ch02/world-02.doc");
            addUrl("file:///c:/iWeb2/data/ch02/world-03.doc");
            addUrl("file:///c:/iWeb2/data/ch02/world-04.doc");
            addUrl("file:///c:/iWeb2/data/ch02/world-05.doc");
        }    	
    	else {
    	    throw new IllegalArgumentException("Unknown value: '" + val + "'");
    	}
        	
    }
    
    public void addDocSpam() {
    	
    	addUrl("file:///c:/iWeb2/data/ch02/spam-biz-01.doc");
    	addUrl("file:///c:/iWeb2/data/ch02/spam-biz-02.doc");
    	addUrl("file:///c:/iWeb2/data/ch02/spam-biz-03.doc");    	
    }
    
	/**
	 * @return the rootDir
	 */
	public String getRootDir() {
		return rootDir;
	}

	/**
	 * @param rootDir the rootDir to set
	 */
	public void setRootDir(String rootDir) {
		this.rootDir = rootDir;
	}

	/**
	 * @return the maxNumberOfCrawls
	 */
	public int getMaxNumberOfCrawls() {
		return maxDepth;
	}

	/**
	 * @param maxNumberOfCrawls the maxNumberOfCrawls to set
	 */
	public void setMaxNumberOfCrawls(int maxNumberOfCrawls) {
		this.maxDepth = maxNumberOfCrawls;
	}

	/**
	 * @return the maxNumberOfDocsPerCrawl
	 */
	public int getMaxNumberOfDocsPerCrawl() {
		return maxDocs;
	}

	/**
	 * @param maxNumberOfDocsPerCrawl the maxNumberOfDocsPerCrawl to set
	 */
	public void setMaxNumberOfDocsPerCrawl(int maxNumberOfDocsPerCrawl) {
		this.maxDocs = maxNumberOfDocsPerCrawl;
	}

	/**
	 * @return the crawlData
	 */
	public CrawlData getCrawlData() {
		return crawlData;
	}
    
}
