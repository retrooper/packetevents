package io.github.retrooper.packetevents.packetwrappers.play.out.entitymetadata;

import com.google.common.base.Objects;

/**
 * A base wrapper class.
 *
 * @author SteelPhoenix
 */
public class AbstractWrapper {

	protected final Class<?> type;
	protected Object raw = null;

	public AbstractWrapper(Class<?> type) {
		if (type == null) {
			throw new NullPointerException("Type cannot be null");
		}

		this.type = type;
	}

	public AbstractWrapper(Class<?> type, Object raw) {
		this(type);
		setRaw(raw);
	}

	/**
	 * Get the wrapper handle.
	 *
	 * @return the handle.
	 */
	public Object getRaw() {
		if (raw == null) {
			throw new IllegalStateException("No instance set");
		}

		return raw;
	}

	/**
	 * Set the wrapper handle.
	 * Note that this method may only be called once.
	 *
	 * @param raw New handle.
	 */
	protected void setRaw(Object raw) {
		// Preconditions
		if (raw == null) {
			throw new NullPointerException("Object cannot be null");
		}
		if (!type.isAssignableFrom(raw.getClass())) {
			throw new IllegalArgumentException("Object is not of type " + type);
		}

		this.raw = raw;
	}

	/**
	 * Get the handle type.
	 *
	 * @return the handle type.
	 */
	public Class<?> getRawType() {
		return type;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(raw);
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof AbstractWrapper)) {
			return false;
		}
		return Objects.equal(raw, ((AbstractWrapper) object).raw);
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "[raw=" + raw + "]";
	}
}
