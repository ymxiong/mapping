package cc.eamon.open.mapping.exception;

import javax.lang.model.element.Element;

/**
 * Created by Eamon on 2018/10/2.
 */
public class ProcessingException extends Exception {

    private Element element;

    public ProcessingException(Element element, String msg, Object... args) {
        super(String.format(msg, args));
        this.element = element;
    }

    public Element getElement() {
        return element;
    }
}

