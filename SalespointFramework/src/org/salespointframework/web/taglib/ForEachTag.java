package org.salespointframework.web.taglib;

import java.io.IOException;
import java.util.Iterator;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;

// via http://stackoverflow.com/questions/6212622/how-can-i-work-with-iterables-in-my-jsp-pages


/**
 * 
 * @author Paul Henke
 *
 */
@SuppressWarnings("serial")
public class ForEachTag extends BodyTagSupport {

    private Iterable<?> iterable = null;
    private Iterator<?> iterator = null;
    
    private String var = null;

    @Override
	public int doStartTag() throws JspException {
        if (iterator == null) {
            iterator = iterable.iterator();
        }
        if (iterator.hasNext()) {
            Object element = iterator.next();
            pageContext.setAttribute(var, element);
            return (EVAL_BODY_AGAIN);
        } else
            return (SKIP_BODY);
    }

    @Override
	public int doAfterBody() throws JspException {
        if (bodyContent != null) {
            try {
                JspWriter out = getPreviousOut();
                out.print(bodyContent.getString());
                bodyContent.clearBody();
            } catch (IOException e) {
                throw new JspException(e);
            }
        }
        if (iterator.hasNext()) {
            Object element = iterator.next();
            pageContext.setAttribute(var, element);
            return (EVAL_BODY_AGAIN);
        } else {
            return (SKIP_BODY);
        }
    }

    public void setItems(Iterable<?> iterable) {
        this.iterable = iterable;
        iterator = null;
    }

    public void setVar(String var) {
        this.var = var;
    }
}