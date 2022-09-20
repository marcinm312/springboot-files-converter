package pl.marcinm312.filesconverter.shared.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@AllArgsConstructor
@EqualsAndHashCode
@Getter
public final class FileToZip {

	private final String name;
	private final byte[] bytes;
}
