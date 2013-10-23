package org.salespointframework.core.useraccount;

import java.util.Objects;

import javax.persistence.Embeddable;
import javax.persistence.Transient;

@Embeddable
public class Password {

    private String password;

    @Transient
    private boolean encrypted = true;

    /**
     * Empty constructor for ORM.
     * 
     */
    @Deprecated
    protected Password() {
    }


    /**
     * Creates a new password. The password is marked as 'not encrypted'.
     *
     * @param password the password string
     */
    public Password(String password) {

        this(password, false);
    }


    /**
     * Creates a new password. The password can be provided in encrypted form, marked by the <code>encrypted</code> parameter.
     *
     * @param password the password string
     * @param encrypted whether the password is already encrypted
     */
    Password(String password, boolean encrypted) {

        this.password = Objects.requireNonNull(password, "password must not be null");
        this.encrypted = Objects.requireNonNull(encrypted,"encrypted must not be null");
    }

    /**
     * Returns whether the password is encrypted.
     *
     * @return the encrypted
     */
    public boolean isEncrypted() {

        return encrypted;
    }


    @Override
    public String toString() {

        return password;
    }


    @Override
    public boolean equals(Object obj) {

        if (obj == null) {
            return false;
        }

        if (obj == this) {
            return true;
        }

        if (!obj.getClass().equals(this.getClass())) {
            return false;
        }

        Password that = (Password) obj;

        return this.password.equals(that.password) && this.encrypted == that.encrypted;
    }


    @Override
    public int hashCode() {

        int result = 17;
        result += 31 * password.hashCode();
        result += encrypted ? 0 : 1;

        return result;
    }
}
