package pl.marcinm312.filesconverter.shared.model;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

class ZipFileTest {

	@Test
	void equalsHashCode_differentCases() {
		EqualsVerifier.forClass(ZipFile.class).verify();
	}
}