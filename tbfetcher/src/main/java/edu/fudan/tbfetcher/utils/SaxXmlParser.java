package edu.fudan.tbfetcher.utils;

import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import edu.fudan.tbfetcher.pojo.CategoryInfo;

public class SaxXmlParser {

	public void execute() {
		try {

			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();

			DefaultHandler handler = new DefaultHandler() {

				boolean bfname = false;
				boolean blname = false;

				public void startElement(String uri, String localName,
						String qName, Attributes attributes)
						throws SAXException {

					System.out.println("Start Element :" + qName);

					if (qName.equalsIgnoreCase("name")) {
						bfname = true;
					}

					if (qName.equalsIgnoreCase("url")) {
						blname = true;
					}

				}

				public void endElement(String uri, String localName,
						String qName) throws SAXException {

					System.out.println("End Element :" + qName);

				}

				public void characters(char ch[], int start, int length)
						throws SAXException {

					if (bfname) {
						System.out.println("First Name : "
								+ new String(ch, start, length));
						bfname = false;
					}

					if (blname) {
						System.out.println("Last Name : "
								+ new String(ch, start, length));
						blname = false;
					}


				}

			};

			saxParser.parse("settings.xml", handler);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	 public static void main(String argv[]) {
		 SaxXmlParser xml = new SaxXmlParser();
		 xml.execute();
	 }
}
