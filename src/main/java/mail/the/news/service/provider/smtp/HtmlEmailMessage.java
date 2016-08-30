package mail.the.news.service.provider.smtp;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.Session;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

import mail.the.news.domain.EmailMessage;
import mail.the.news.exception.EmailServiceException;

class HtmlEmailMessage extends SmtpEmailMessage {
	
	// pattern for extracting <img> tags
    private static final Pattern IMG_PATTERN = Pattern.compile("(<[Ii][Mm][Gg]\\s*[^>]*?\\s+[Ss][Rr][Cc]\\s*=\\s*[\"'])([^\"']+?)([\"'])");

	public HtmlEmailMessage(EmailMessage emailMessage, Session session) throws EmailServiceException {
		super(new HtmlEmail(), emailMessage, session);
	}

	@Override
	protected void setBody(String body) throws EmailException {
		((HtmlEmail)this.email).setHtmlMsg(String.format("<html><body>%s</body></html>", replacePattern(body)));
		((HtmlEmail)this.email).setTextMsg( "Your email client does not support HTML messages");
	}
	
	@Override	
	protected void attach(Collection<String> filesToAttach) throws EmailException {
    	for(String file : filesToAttach)
    		((HtmlEmail)this.email).attach(createAttachment(file));
    }
	
    /**
     * Replace the regexp matching resource locations with "cid:..." references.
     *
     * @param htmlMessage the HTML message to analyze
     * @param pattern the regular expression to find resources
     * @return the HTML message containing "cid" references
     * @throws EmailException creating the email failed
     * @throws IOException resolving the resources failed
     */
    private String replacePattern(String htmlMessage) throws EmailException {
        final StringBuffer stringBuffer = new StringBuffer();

        // maps "cid" --> name
        final Map<String, String> cidCache = new HashMap<String, String>();

        // maps "name" --> dataSource
        final Map<String, File> dataSourceCache = new HashMap<String, File>();

        // replace all "img src" with a CID and embed the related image file
        final Matcher matcher = IMG_PATTERN.matcher(htmlMessage);

        // the matcher returns all instances one by one
        while (matcher.find())
        {
            // in the RegEx we have the <src> element as second "group"
        	String src = matcher.group(2);

            File img = dataSourceCache.get(src);
            // avoid loading the same data source more than once
            if (img == null)
            {
                try {
                	img = Paths.get(new URL(src).toURI()).toFile();
                	if(img.exists()) {
                		dataSourceCache.put(src, img);
                	} else {
                		throw new RuntimeException("Image file:" + src + " not found!"); // TODO
                	}    				
    			} catch (MalformedURLException | URISyntaxException e) {
    				throw new RuntimeException("Unable to parse:" + src + " to url!"); // TODO
    			}
            }

            String cid = cidCache.get(src);

            if (cid == null) {
                cid = ((HtmlEmail)email).embed(img);
                cidCache.put(src, cid);
            }

            // if we embedded something, then we need to replace the URL with
            // the CID, otherwise the Matcher takes care of adding the
            // non-replaced text afterwards, so no else is necessary here!
            matcher.appendReplacement(stringBuffer, Matcher.quoteReplacement(matcher.group(1) + "cid:" + cid + matcher.group(3)));
        }

        // append the remaining items...
        matcher.appendTail(stringBuffer);

        cidCache.clear();
        dataSourceCache.clear();

        return stringBuffer.toString();
    }

}
