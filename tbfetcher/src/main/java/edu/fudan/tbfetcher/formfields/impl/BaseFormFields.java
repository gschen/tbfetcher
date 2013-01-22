package edu.fudan.tbfetcher.formfields.impl;

import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;

import edu.fudan.tbfetcher.formfields.FormFields;
import edu.fudan.tbfetcher.pojo.BasePostInfo;
import edu.fudan.tbfetcher.utils.FormFieldsUtils;


/**
 * 
 * 自动构造form fields
 * @author JustinChen
 *
 */
public class BaseFormFields implements FormFields {

	private BasePostInfo basePostInfo;
	private List<NameValuePair> formFieldsNvps;
	
	
	public void setFormFieldsNvps(List<NameValuePair> formFieldsNvps) {
		this.formFieldsNvps = formFieldsNvps;
	}


	public void setBasePostInfo(BasePostInfo basePostInfo) {
		this.basePostInfo = basePostInfo;
	}


	public List<NameValuePair> getFormFields(HttpClient httpClient) {
		return FormFieldsUtils.getNameValuePairList(httpClient, basePostInfo, formFieldsNvps);
	}

}
