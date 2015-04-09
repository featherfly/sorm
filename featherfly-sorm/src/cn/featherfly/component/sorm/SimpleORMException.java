package cn.featherfly.component.sorm;


/**
 * <p>
 * SimpleOrm异常
 * </p>
 *
 * @author 钟冀
 * @since 1.0
 * @version 1.0
 */
public class SimpleORMException extends RuntimeException {

	private static final long serialVersionUID = 3200233346249297455L;

	/**
	 * Constructs a new runtime exception with <code>null</code> as its detail
	 * message. The cause is not initialized, and may subsequently be
	 * initialized by a call to {@link #initCause}.
	 */
	public SimpleORMException() {
		super();
	}

	/**
	 * @param message
	 *            the detail message. The detail message is saved for later
	 *            retrieval by the {@link #getMessage()} method.
	 */
	public SimpleORMException(String message) {
		super(message);
	}

	/**
	 *
	 * @param message
	 *            the detail message (which is saved for later retrieval by the
	 *            {@link #getMessage()} method).
	 * @param cause
	 *            the cause (which is saved for later retrieval by the
	 *            {@link #getCause()} method). (A <tt>null</tt> value is
	 *            permitted, and indicates that the cause is nonexistent or
	 *            unknown.)
	 */
	public SimpleORMException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 *
	 * @param e
	 *            the cause (which is saved for later retrieval by the
	 *            {@link #getCause()} method). (A <tt>null</tt> value is
	 *            permitted, and indicates that the cause is nonexistent or
	 *            unknown.)
	 */
	public SimpleORMException(Throwable e) {
		super(e);
	}
}
