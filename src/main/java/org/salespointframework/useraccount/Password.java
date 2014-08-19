package org.salespointframework.useraccount;

import javax.persistence.Embeddable;
import javax.persistence.Transient;

import org.springframework.util.Assert;

/**
 * @author Hannes Weissbach
 * @author Paul Henke
 * @author Oliver Gierke
 */
@Embeddable
public class Password {

	private String password;
	private @Transient boolean encrypted = true;

	/**
	 * Empty constructor for ORM.
	 */
	@Deprecated
	protected Password() {}

	/**
	 * Creates a new password. The password is marked as 'not encrypted'.
	 *
	 * @param password the password string, must not be {@literal null} or empty.
	 */
	public Password(String password) {
		this(password, false);
	}

	/**
	 * Creates a new password. The password can be provided in encrypted form, marked by the {@code encrypted} parameter.
	 *
	 * @param password the password string, must not be {@literal null} or empty.
	 * @param encrypted whether the password is already encrypted
	 */
	Password(String password, boolean encrypted) {

		Assert.hasText(password, "Password must not be null or empty!");

		this.password = password;
		this.encrypted = encrypted;
	}

	/**
	 * Returns whether the password is encrypted.
	 *
	 * @return the encrypted
	 */
	public boolean isEncrypted() {
		return encrypted;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return password;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {

		if (obj == this) {
			return true;
		}

		if (!(obj instanceof Password)) {
			return false;
		}

		Password that = (Password) obj;

		return this.password.equals(that.password) && this.encrypted == that.encrypted;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {

		int result = 17;
		result += 31 * password.hashCode();
		result += encrypted ? 0 : 1;

		return result;
	}
}
