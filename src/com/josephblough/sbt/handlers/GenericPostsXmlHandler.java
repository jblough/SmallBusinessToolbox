package com.josephblough.sbt.handlers;

import java.util.ArrayList;
import java.util.Stack;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.josephblough.sbt.data.GenericPost;

public class GenericPostsXmlHandler extends DefaultHandler {

    private static final String RESULT_TAG = "result";
    private static final String NUMBER_FOUND_TAG = "numFound";
    private static final String ITEM_TAG = "item";
    private static final String TITLE_TAG = "title";
    private static final String URL_TAG = "url";
    private static final String CLOSE_DATE_TAG = "close_date";
    private static final String DAYS_TO_CLOSE_TAG = "days_to_close";
    

    private static final int RESULT_ID = 0;
    private static final int NUMBER_FOUND_ID = 1;
    private static final int ITEM_ID = 2;
    private static final int TITLE_ID = 3;
    private static final int URL_ID = 4;
    private static final int CLOSE_DATE_ID = 5;
    private static final int DAYS_TO_CLOSE_ID = 6;
    private static final int UNKNOWN_ID = 7;
    
    private Stack<Integer> tagStack;
    public ArrayList<GenericPost> posts;
    private GenericPost post;
    private StringBuffer characterBuffer;

    public void startDocument() throws SAXException {
	tagStack = new Stack<Integer>();
	posts = new ArrayList<GenericPost>();
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
	    post = new GenericPost();
	}
	else if (TITLE_TAG.equals(localName))
	    tagStack.push(TITLE_ID);
	else if (URL_TAG.equals(localName))
	    tagStack.push(URL_ID);
	else if (CLOSE_DATE_TAG.equals(localName))
	    tagStack.push(CLOSE_DATE_ID);
	else if (DAYS_TO_CLOSE_TAG.equals(localName))
	    tagStack.push(DAYS_TO_CLOSE_ID);
	else
	    tagStack.push(UNKNOWN_ID);
	
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
	tagStack.pop();
	
	if (ITEM_TAG.equals(localName)) {
	    // Require that a post has a URL at the very least (sometimes items are just "There are no result found.")
	    if (post.url != null && !"".equals(post.url)) {
		posts.add(post);
	    }
	    post = null;
	}
	else if (TITLE_TAG.equals(localName))
	    post.title = characterBuffer.toString();
	else if (URL_TAG.equals(localName))
	    post.url = characterBuffer.toString();
	else if (CLOSE_DATE_TAG.equals(localName))
	    post.closeDate = characterBuffer.toString();
	else if (DAYS_TO_CLOSE_TAG.equals(localName))
	    post.daysToClose = characterBuffer.toString();
	
	// Empty out the character buffer
	characterBuffer = characterBuffer.delete(0, characterBuffer.length());
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
	switch (tagStack.peek()) {
	case TITLE_ID:
	case URL_ID:
	case CLOSE_DATE_ID:
	case DAYS_TO_CLOSE_ID:
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
