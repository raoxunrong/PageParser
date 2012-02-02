package backstage.shell;

import backstage.ranking.PageRankMatrixBuilder;
import backstage.ranking.PageRankMatrixH;
import backstage.ranking.Rank;
import backstage.webcrawler.CrawlData;

public class PageRank extends Rank {
    
	PageRankMatrixBuilder pageRankBuilder;
	
	public PageRank(CrawlData crawlData) {
        pageRankBuilder = new PageRankMatrixBuilder(crawlData);
        pageRankBuilder.run();     
	}
	
	@Override
	public PageRankMatrixH getH() {
		return pageRankBuilder.getH();
	}
	
}
