package cn.featherfly.component.sorm;

import java.util.Locale;

import cn.featherfly.common.exception.LocalizedException;

/**
 * <p>
 * SimpleOrm异常
 * </p>
 *
 * @author zhongj
 * @since 1.0
 * @version 1.0
 */
public class SimpleORMException extends LocalizedException {

	private static final long serialVersionUID = 3200233346249297455L;

	/**
     * 
     */
    public SimpleORMException() {
        super();
    }

    /**
     * @param message
     * @param locale
     * @param ex
     */
    public SimpleORMException(String message, Locale locale, Throwable ex) {
        super(message, locale, ex);
    }

    /**
     * @param message
     * @param locale
     */
    public SimpleORMException(String message, Locale locale) {
        super(message, locale);
    }

    /**
     * @param message
     * @param argus
     * @param locale
     * @param ex
     */
    public SimpleORMException(String message, Object[] argus, Locale locale,
            Throwable ex) {
        super(message, argus, locale, ex);
    }

    /**
     * @param message
     * @param argus
     * @param locale
     */
    public SimpleORMException(String message, Object[] argus, Locale locale) {
        super(message, argus, locale);
    }

    /**
     * @param message
     * @param argus
     * @param ex
     */
    public SimpleORMException(String message, Object[] argus, Throwable ex) {
        super(message, argus, ex);
    }

    /**
     * @param message
     * @param argus
     */
    public SimpleORMException(String message, Object[] argus) {
        super(message, argus);
    }

    /**
     * @param message
     * @param ex
     */
    public SimpleORMException(String message, Throwable ex) {
        super(message, ex);
    }

    /**
     * @param message
     */
    public SimpleORMException(String message) {
        super(message);
    }

    /**
     * @param ex
     */
    public SimpleORMException(Throwable ex) {
        super(ex);
    }
}
