package org.salespointframework.core.accountancy.payment;

import java.io.Serializable;

import org.salespointframework.util.Objects;

/**
 * A <code>PaymentMethod</code> specifies a medium by which a payment has or
 * will be made.
 * 
 * @author Hannes Weisbach
 */
public abstract class PaymentMethod implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Description of the <code>PaymentMethod</code> in human-readable form. Is
     * not null.
     */
    private String            desc;

    /**
     * Constructor which takes a String, describing the
     * <code>PaymentMethod</code> in human-readable form.
     * 
     * @param desc
     *            Description of the <code>PaymentMethod</code> in
     *            human-readable form. Must be non-null.
     */
    public PaymentMethod(String desc) {
        this.desc = Objects.requireNonNull(desc, "desc");
    }

    /**
     * The string representation of this <code>PaymentMethod</code>. It's the
     * description that was given to the constructor.
     * 
     * @return The description of this <code>PaymentMethod</code>.
     */
    @Override
    public String toString() {
        return desc;
    }
}
