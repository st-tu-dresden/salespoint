package test.rounding;

import static org.junit.Assert.*;

import java.math.BigDecimal;

import org.junit.Test;
import org.salespointframework.core.quantity.rounding.*;

public class TestRounding {
	BigDecimal d;
	
	@Test
	public void RoundUpNeg() {
		d = BigDecimal.valueOf(-4.45);
		assertEquals(BigDecimal.valueOf(-4.5), new RoundUpStrategy(1).round(d));
	}
	
	@Test
	public void RoundUpPos() {
		d = BigDecimal.valueOf(4.45);
		assertEquals(BigDecimal.valueOf(4.5), new RoundUpStrategy(1).round(d));
	}
	
	@Test
	public void RoundDownNeg() {
		d = BigDecimal.valueOf(-4.45);
		assertEquals(BigDecimal.valueOf(-4.4), new RoundDownStrategy(1).round(d));
	}
	
	@Test
	public void RoundDownPos() {
		d = BigDecimal.valueOf(4.45);
		assertEquals(BigDecimal.valueOf(4.4), new RoundDownStrategy(1).round(d));
	}
	
	@Test
	public void RoundPos5() {
		d = BigDecimal.valueOf(4.45);
		assertEquals(BigDecimal.valueOf(4.5), new RoundStrategy(1, 5).round(d));
	}
	
	@Test
	public void RoundPos6() {
		d = BigDecimal.valueOf(4.45);
		assertEquals(BigDecimal.valueOf(4.4), new RoundStrategy(1, 6).round(d));
	}
	
	@Test
	public void RoundNeg5() {
		d = BigDecimal.valueOf(-4.45);
		assertEquals(BigDecimal.valueOf(-4.5), new RoundStrategy(1, 5).round(d));
	}
	
	@Test
	public void RoundNeg6() {
		d = BigDecimal.valueOf(-4.45);
		assertEquals(BigDecimal.valueOf(-4.4), new RoundStrategy(1, 6).round(d));
	}
	
	@Test
	public void RoundCeilPos() {
		d = BigDecimal.valueOf(4.45);
		assertEquals(BigDecimal.valueOf(4.5), new RoundCeilStrategy(1).round(d));
	}
	
	@Test
	public void RoundCeilNeg() {
		d = BigDecimal.valueOf(-4.45);
		assertEquals(BigDecimal.valueOf(-4.4), new RoundCeilStrategy(1).round(d));
	}
	
	@Test
	public void RoundFloorPos() {
		d = BigDecimal.valueOf(4.45);
		assertEquals(BigDecimal.valueOf(4.4), new RoundFloorStrategy(1).round(d));
	}
	
	@Test
	public void RoundFloorNeg() {
		d = BigDecimal.valueOf(-4.45);
		assertEquals(BigDecimal.valueOf(-4.5), new RoundFloorStrategy(1).round(d));
	}
	
	@Test
	public void RoundUpStepPos() {
		d = BigDecimal.valueOf(76);
		assertTrue(BigDecimal.valueOf(100).compareTo(new RoundUpStepStrategy(BigDecimal.valueOf(50)).round(d)) == 0);
	}
	
	@Test
	public void RoundUpStepNeg() {
		d = BigDecimal.valueOf(-1.223);
		assertTrue(BigDecimal.valueOf(-1.225).compareTo(new RoundUpStepStrategy(BigDecimal.valueOf(0.005)).round(d)) == 0);
	}

	@Test
	public void RoundDownStepPos() {
		d = BigDecimal.valueOf(76);
		assertTrue(BigDecimal.valueOf(50).compareTo(new RoundDownStepStrategy(BigDecimal.valueOf(50)).round(d)) == 0);
	}
	
	@Test
	public void RoundDownStepNeg() {
		d = BigDecimal.valueOf(-1.223);
		assertTrue(BigDecimal.valueOf(-1.22).compareTo(new RoundDownStepStrategy(BigDecimal.valueOf(0.005)).round(d)) == 0);
	}

}
