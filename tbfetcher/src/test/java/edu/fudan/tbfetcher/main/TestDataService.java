package edu.fudan.tbfetcher.main;

import org.junit.Test;

import edu.fudan.tbfetcher.service.TaobaoDsDataService;

public class TestDataService {

	@Test
	public void test() {
		// fail("Not yet implemented");
		TaobaoDsDataService taobaoDsDataService = new TaobaoDsDataService();
		String address = taobaoDsDataService.getAddress("130127");
		System.out.print(address);
	}

}
