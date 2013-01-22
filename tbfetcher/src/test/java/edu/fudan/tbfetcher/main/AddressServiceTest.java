package edu.fudan.tbfetcher.main;

import edu.fudan.tbfetcher.service.AddressService;
import edu.fudan.tbfetcher.service.TaobaoAddressService;

public class AddressServiceTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		AddressService service = TaobaoAddressService.getInstance();
		System.out.println(service.getAddress("140200"));
	}

}
