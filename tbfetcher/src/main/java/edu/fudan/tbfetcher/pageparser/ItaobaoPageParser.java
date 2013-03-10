package edu.fudan.tbfetcher.pageparser;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import edu.fudan.tbfetcher.pojo.ItemComment;
import edu.fudan.tbfetcher.service.TaobaoAddressService;

/**
 * 买家信息页面解析
 * 
 * @author JustinChen
 * 
 */
public class ItaobaoPageParser extends BasePageParser {

	private static final Logger log = Logger.getLogger(ItaobaoPageParser.class);

	private ItemComment buyerInfo;

	public void setBuyerInfo(ItemComment buyerInfo) {
		this.buyerInfo = buyerInfo;
	}

	// 执行解析前确保pageUrl 不为空
	@Override
	public void parsePage() {
		/** 首先判断返回的是否是登录页面 */
		Element loginEl = doc.getElementById("J_LoginBox");
		if (null != loginEl) {
			return;
		}
		/** buyer id */
		Elements idEls = doc.select("ul.menu-bd li a");
		for (Element el : idEls) {
			String href = el.attr("href");
			if (href.contains("user_id")) {
				int begin = href.lastIndexOf("=");
				String buyId = href.substring(begin + 1, href.length());
				buyerInfo.setBuyId(buyId);
				log.info("buyer id: " + buyId);
				break;
			}
		}

		/** buyer name */
		Elements nameEls = doc.select("h2.name div.nick");
		if (nameEls.size() > 0) {
			String name = nameEls.get(0).ownText();
			buyerInfo.setBuyerName(name);
			log.info("buyer name: " + name);
		}

		/** buyer credit score */
		Elements creditEls = doc.select("img.rank");
		if (creditEls.size() > 0) {
			String numStr = creditEls.get(0).attr("title");
			int end = numStr.indexOf("个");
			if(end > 0){
				numStr = numStr.substring(0, end);
				int creditScore = Integer.valueOf(numStr);
				buyerInfo.setCreditScore(creditScore);
				log.info("buyer credit score is: " + creditScore);
			}
		}

		/** buyer description: 小有所成 */
		Elements descEls = doc.select("a.J_Honor");
		if (descEls.size() > 0) {
			StringBuffer sb = new StringBuffer();
			for (Element alink : descEls) {
				String title = alink.attr("title");
				sb.append(title + ",");
			}
			sb.deleteCharAt(sb.length() - 1);
			buyerInfo.setBuyerDesc(sb.toString());
			log.info("buyer desc: " + sb.toString());
		}

		/** buyer gender */
		Elements metaEls = doc.select("div.meta");
		if (metaEls.size() > 0) {
			Element metaEl = metaEls.get(0);
			String gender = "";
			if (metaEl.select("span.male").size() > 0) {
				gender = "男";
			}
			if (metaEl.select("span.female").size() > 0) {
				gender = "女";
			}
			buyerInfo.setGender(gender);
			log.info("buyer gender: " + gender);
		}

		/** buyer address */
		Elements addressEls = doc.select("span.J_district");
		if (addressEls.size() > 0) {
			Element addEl = addressEls.get(0);
			String addressCode = addEl.ownText();

			String[] address = TaobaoAddressService.getInstance().getAddress(addressCode);
			
			if(null != address){
				buyerInfo.setBuyerAddress(address[0]);
				buyerInfo.setAddrProvince(address[1]);
				buyerInfo.setAddrCity(address[2]);
			}
			
			log.info("buyer address: " + address);
		}
	}
}
