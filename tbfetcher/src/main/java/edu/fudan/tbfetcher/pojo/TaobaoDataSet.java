package edu.fudan.tbfetcher.pojo;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import edu.fudan.tbfetcher.pojo.TopRankItemInfo;

public class TaobaoDataSet {
	private static final Logger log = Logger.getLogger(TaobaoDataSet.class);
	public static List<TopRankItemInfo> topTenItemInfos =new ArrayList<TopRankItemInfo>();
	public static List<SellerInSearchResult> sellerInSearchResults = new ArrayList<SellerInSearchResult>();
	public static List<ItemInfo> itemInfos = new ArrayList<ItemInfo>();

	public static void printList() {
		for(int i = 0; i < topTenItemInfos.size(); ++i){
			log.info("Top ten item info is: "+topTenItemInfos.get(i).getCategoryName());
			log.info("Top ten item info is: "+topTenItemInfos.get(i).getItemName());
			log.info("Top ten item info is: "+topTenItemInfos.get(i).getTopRank());
		}
		log.info("The size of the seller in search result is: "+sellerInSearchResults.size());
	}
}
