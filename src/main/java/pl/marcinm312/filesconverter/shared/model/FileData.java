package pl.marcinm312.filesconverter.shared.model;

import java.util.Arrays;
import java.util.Objects;

public record FileData(String name, byte[] bytes) {

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		FileData fileData = (FileData) o;

		if (!Objects.equals(name, fileData.name)) return false;
		return Arrays.equals(bytes, fileData.bytes);
	}

	@Override
	public int hashCode() {
		int result = name != null ? name.hashCode() : 0;
		result = 31 * result + Arrays.hashCode(bytes);
		return result;
	}

	@Override
	public String toString() {
		return "FileData{" +
				"name='" + name + '\'' +
				'}';
	}
}
