package com.josephblough.sbt.handlers;

import java.util.ArrayList;
import java.util.Stack;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.josephblough.sbt.data.Award;

public class AwardsXmlHandler extends DefaultHandler {

    private static final String RESULT_TAG = "result";
    private static final String NUMBER_FOUND_TAG = "numFound";
    private static final String ITEM_TAG = "item";
    private static final String TITLE_TAG = "title";
    private static final String LINK_TAG = "link";
    private static final String ABSTRACT_TAG = "abstract";
    private static final String AGENCY_TAG = "agency";
    private static final String PROGRAM_TAG = "program";
    private static final String PHASE_TAG = "phase";
    private static final String YEAR_TAG = "year";
    private static final String COMPANY_TAG = "company";
    private static final String RESEARCH_INSTITUTION_TAG = "ri";

    private static final int RESULT_ID = 0;
    private static final int NUMBER_FOUND_ID = 1;
    private static final int ITEM_ID = 2;
    private static final int TITLE_ID = 3;
    private static final int LINK_ID = 4;
    private static final int ABSTRACT_ID = 5;
    private static final int AGENCY_ID = 6;
    private static final int PROGRAM_ID = 7;
    private static final int PHASE_ID = 8;
    private static final int YEAR_ID = 9;
    private static final int COMPANY_ID = 10;
    private static final int RESEARCH_INSTITUTION_ID = 11;
    private static final int UNKNOWN_ID = 12;
    
    private Stack<Integer> tagStack;
    public ArrayList<Award> awards;
    private Award award;
    private StringBuffer characterBuffer;

    public void startDocument() throws SAXException {
	tagStack = new Stack<Integer>();
	awards = new ArrayList<Award>();
	characterBuffer = new StringBuffer();
    }

    public void startElement(String uri, String localName, String qName, 
	    Attributes attributes) throws SAXException {
	if (RESULT_TAG.equals(localName))
	    tagStack.push(RESULT_ID);
	else if (NUMBER_FOUND_TAG.equals(localName))
	    tagStack.push(NUMBER_FOUND_ID);
	else if (ITEM_TAG.equals(localName)) {
	    tagStack.push(ITEM_ID);
	    award = new Award();
	}
	else if (TITLE_TAG.equals(localName))
	    tagStack.push(TITLE_ID);
	else if (LINK_TAG.equals(localName))
	    tagStack.push(LINK_ID);
	else if (ABSTRACT_TAG.equals(localName))
	    tagStack.push(ABSTRACT_ID);
	else if (AGENCY_TAG.equals(localName))
	    tagStack.push(AGENCY_ID);
	else if (PROGRAM_TAG.equals(localName))
	    tagStack.push(PROGRAM_ID);
	else if (PHASE_TAG.equals(localName))
	    tagStack.push(PHASE_ID);
	else if (YEAR_TAG.equals(localName))
	    tagStack.push(YEAR_ID);
	else if (COMPANY_TAG.equals(localName))
	    tagStack.push(COMPANY_ID);
	else if (RESEARCH_INSTITUTION_TAG.equals(localName))
	    tagStack.push(RESEARCH_INSTITUTION_ID);
	else
	    tagStack.push(UNKNOWN_ID);
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
	tagStack.pop();
	
	if (ITEM_TAG.equals(localName)) {
	    awards.add(award);
	    award = null;
	}
	else if (TITLE_TAG.equals(localName))
	    award.title = characterBuffer.toString();
	else if (LINK_TAG.equals(localName))
	    award.link = characterBuffer.toString();
	else if (ABSTRACT_TAG.equals(localName))
	    award.awardAbstract = characterBuffer.toString();
	else if (AGENCY_TAG.equals(localName))
	    award.agency = characterBuffer.toString();
	else if (PROGRAM_TAG.equals(localName))
	    award.program = characterBuffer.toString();
	else if (PHASE_TAG.equals(localName))
	    award.phase = Integer.valueOf(characterBuffer.toString());
	else if (YEAR_TAG.equals(localName))
	    award.year = Integer.valueOf(characterBuffer.toString());
	else if (COMPANY_TAG.equals(localName))
	    award.company = characterBuffer.toString();
	else if (RESEARCH_INSTITUTION_TAG.equals(localName))
	    award.researchInstitution = characterBuffer.toString();
	
	// Empty out the character buffer
	characterBuffer = characterBuffer.delete(0, characterBuffer.length());
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
	switch (tagStack.peek()) {
	case TITLE_ID:
	case LINK_ID:
	case ABSTRACT_ID:
	case AGENCY_ID:
	case PROGRAM_ID:
	case PHASE_ID:
	case YEAR_ID:
	case COMPANY_ID:
	case RESEARCH_INSTITUTION_ID:
	    characterBuffer.append(xmlDecode(new String(ch, start, length)));
	    break;
	}
    }
    
    public void endDocument() throws SAXException {
    }

    private static String xmlDecode(String str) {
	return str.replaceAll("&amp;", "&").replaceAll("&apos;", "'")
	.replaceAll("&lt;", "<").replaceAll("&gt;", ">")
	.replaceAll("&quot;", "\"");
    }
}
