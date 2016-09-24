package org.salespointframework.order;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import javax.money.MonetaryAmount;
import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

import org.salespointframework.core.AbstractEntity;

/**
 * A chargeline represents extra expenses like shipping. This class is immutable.
 * 
 * @author Thomas Dedek
 * @author Paul Henke
 * @author Oliver Gierke
 */
@Entity
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE, onConstructor = @__(@Deprecated))
@RequiredArgsConstructor
@Getter
@ToString
public class ChargeLine extends AbstractEntity<ChargeLineIdentifier> implements Priced {

	private static final long serialVersionUID = 7589903169153242824L;

	@EmbeddedId //
	@AttributeOverride(name = "id", column = @Column(name = "CHARGELINE_ID")) //
	private ChargeLineIdentifier chargeLineIdentifier = new ChargeLineIdentifier();

	private final MonetaryAmount price;
	private final String description;

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.domain.Persistable#getId()
	 */
	public ChargeLineIdentifier getId() {
		return chargeLineIdentifier;
	}
}
