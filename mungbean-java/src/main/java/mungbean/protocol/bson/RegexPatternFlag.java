/*
   Copyright 2009 Janne Hietamäki & 10Gen

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
package mungbean.protocol.bson;

import java.util.regex.Pattern;

public enum RegexPatternFlag {
    CANON_EQ(Pattern.CANON_EQ, 'c', "Pattern.CANON_EQ"), UNIX_LINES(Pattern.UNIX_LINES, 'd', "Pattern.UNIX_LINES"), GLOBAL(256, 'g', null), CASE_INSENSITIVE(Pattern.CASE_INSENSITIVE, 'i', null), MULTILINE(Pattern.MULTILINE, 'm', null), DOTALL(Pattern.DOTALL, 's', "Pattern.DOTALL"), LITERAL(Pattern.LITERAL, 't', "Pattern.LITERAL"), UNICODE_CASE(Pattern.UNICODE_CASE, 'u', "Pattern.UNICODE_CASE"), COMMENTS(Pattern.COMMENTS, 'x', null);

    public final int javaFlag;
    public final char flagChar;
    public final String unsupported;

    RegexPatternFlag(int javaFlag, char flagChar, String unsupported) {
        this.javaFlag = javaFlag;
        this.flagChar = flagChar;
        this.unsupported = unsupported;
    }

    public static RegexPatternFlag getByCharacter(char ch) {
        for (RegexPatternFlag flag : values()) {
            if (flag.flagChar == ch) {
                return flag;
            }
        }
        throw new IllegalArgumentException("Unable to get RegexPatternFlag for identifier " + ch);
    }

    /**
     * Converts Java regular expression flags into a string of flags for the
     * database
     * 
     * @param flags
     *            Java flags
     * @return the flags for the database
     */
    public static String patternFlags(int flags) {
        StringBuilder buf = new StringBuilder();
        for (RegexPatternFlag flag : RegexPatternFlag.values()) {
            if ((flags & flag.javaFlag) > 0) {
                buf.append(flag.flagChar);
                flags -= flag.javaFlag;
            }
        }
        if (flags > 0) {
            throw new IllegalArgumentException("some flags could not be recognized.");
        }
        return buf.toString();
    }

    /**
     * Converts a string of regular expression flags from the database in Java
     * regular expression flags.
     * 
     * @param flags
     *            flags from database
     * @return the Java flags
     */
    public static int patternFlags(String flags) {
        flags = flags.toLowerCase();
        int fint = 0;

        for (int i = 0; i < flags.length(); i++) {
            RegexPatternFlag flag = getByCharacter(flags.charAt(i));
            if (flag != null) {
                fint |= flag.javaFlag;
                if (flag.unsupported != null) {
                    throw new IllegalArgumentException("Unsupported flag: " + flag.unsupported);
                }
            } else {
                throw new IllegalArgumentException("unrecognized flag: " + flags.charAt(i));
            }
        }
        return fint;
    }
}