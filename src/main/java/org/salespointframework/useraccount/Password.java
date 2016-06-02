package org.salespointframework.useraccount;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.Transient;

import org.springframework.util.Assert;

/**
 * @author Hannes Weissbach
 * @author Paul Henke
 * @author Oliver Gierke
 */
@Embeddable
@EqualsAndHashCode
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE, onConstructor = @__(@Deprecated) )
public class Password {

	private @Getter(AccessLevel.PACKAGE) String password;

	/**
	 * Whether the password is encrypted.
	 */
	private @Transient @Getter boolean encrypted = true;

	/**
	 * Creates a new password. The password can be provided in encrypted form, marked by the {@code encrypted} parameter.
	 *
	 * @param password the password string, must not be {@literal null} or empty.
	 * @param encrypted whether the password is already encrypted
	 */
	private Password(String password, boolean encrypted) {

		Assert.hasText(password, "Password must not be null or empty!");

		this.password = password;
		this.encrypted = encrypted;
	}

	/**
	 * Creates a new password. The password is marked as 'not encrypted'.
	 *
	 * @param password the password string, must not be {@literal null} or empty.
	 */
	public static Password unencrypted(String password) {
		return new Password(password, false);
	}

	/**
	 * Creates a new encrypted password.
	 *
	 * @param password the password string, must not be {@literal null} or empty.
	 */
	static Password encrypted(String password) {
		return new Password(password, true);
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return encrypted ? password : "********";
	}
}
