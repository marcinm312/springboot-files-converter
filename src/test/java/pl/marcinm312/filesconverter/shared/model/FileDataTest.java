package pl.marcinm312.filesconverter.shared.model;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

class FileDataTest {

	@Test
	void equalsHashCode_differentCases() {
		EqualsVerifier.forClass(FileData.class).verify();
	}
}