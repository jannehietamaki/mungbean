package com.mongodb.protocol.bson;

import java.util.regex.Pattern;

import com.mongodb.protocol.LittleEndianDataReader;
import com.mongodb.protocol.LittleEndianDataWriter;

public class BSONPattern extends BSONCoder<Pattern> {

	protected BSONPattern() {
		super(11, Pattern.class);
	}

	@Override
	protected Pattern decode(BSONCoders bson, LittleEndianDataReader reader) {
		return Pattern.compile(reader.readCString(), RegexPatternFlag.patternFlags(reader.readCString()));
	}

	@Override
	protected void encode(BSONCoders bson, Pattern pattern, LittleEndianDataWriter writer) {
		writer.writeCString(pattern.pattern());
		writer.writeCString(RegexPatternFlag.patternFlags(pattern.flags()));
	}
}
