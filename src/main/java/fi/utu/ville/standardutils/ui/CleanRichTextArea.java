package fi.utu.ville.standardutils.ui;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Entities.EscapeMode;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;

import com.vaadin.ui.RichTextArea;

public class CleanRichTextArea extends RichTextArea {

	private static final long serialVersionUID = -7219905133912035137L;
	private static final Whitelist whitelist;
	
	public CleanRichTextArea() {
		super();
	}
	
	public CleanRichTextArea(String caption) {
		super(caption);
	}
	
	public CleanRichTextArea(String caption, String value) {
		super(caption, value);
	}

	private String clean(String html, Whitelist whitelist) {
		
		String enterEscaped = html.replaceAll("\n", "%newline%");
		
	    Document dirty = Jsoup.parseBodyFragment(enterEscaped, "");
	    Cleaner cleaner = new Cleaner(whitelist);
	    Document clean = cleaner.clean(dirty);
	    clean.outputSettings().escapeMode(EscapeMode.xhtml);
	    clean.outputSettings().charset("UTF-8");
	    clean.outputSettings().indentAmount(0);
	    clean.outputSettings().prettyPrint(false);
	    
	    String cleaned = clean.body().html().replaceAll("\n", "").replaceAll("%newline%", "\n");
	    
		return cleaned;
	}
	
	@Override
	public String getValue() {
		String value = super.getValue();
		if (value == null) return(null);
		return clean(value, whitelist);
	}
	
	@Override
	public void setValue(String value) {
		if ((value == "") || (value == null)) {
			super.setValue(value);
			return;
		} else super.setValue(clean(value, whitelist));
	}
	
	static {
		whitelist = Whitelist.relaxed();
		whitelist.addTags("font").addAttributes("font", "size", "color", "face");
		whitelist.addTags("hr").addAttributes("hr", "align", "noshade", "size", "width");
		whitelist.addAttributes("div", "align");
		whitelist.addAttributes("span", "style"); // RichTextArea specific parameter to allow background color.
		whitelist.addEnforcedAttribute("a", "rel", "nofollow");
	}
}
