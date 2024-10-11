package com.agilepeak.blog;

import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.junit.jupiter.InjectSoftAssertions;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.regex.Pattern;

@ExtendWith(SoftAssertionsExtension.class)
public class RegexTest {

    @InjectSoftAssertions
    private SoftAssertions softly;

    @Test
    void regexWithDollarEnd_fourDigitsWithTrailingNewLine() {
        String fourDigitsWithNewLine = "1234\n";
        String fourDigitsRegexWithStartAndEnd = "^\\d{4}$";

        softly.assertThat(fourDigitsWithNewLine.matches(fourDigitsRegexWithStartAndEnd))
                .as("String.matches(regex) needs the entire string to match, so the trailing \n prevents it.")
                .isFalse();

        softly.assertThat(Pattern.matches(fourDigitsRegexWithStartAndEnd, fourDigitsWithNewLine))
                .as("Pattern.matches(regex, input) needs the entire string to match, so the trailing \n prevents it.")
                .isFalse();

        // Many would expect the trailing \n to prevent a match because it should not be allowed by a regex that specifies exactly 4 digits and nothing else between start and end.
        // BUT...
        // From https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/regex/Pattern.html#lt
        // If MULTILINE mode is not activated, the regular expression ^ ignores line terminators and only matches at the beginning of the entire input sequence.
        // The regular expression $ matches at the end of the entire input sequence,
        // *** BUT ALSO MATCHES JUST BEFORE THE LAST LINE TERMINATOR IF THIS IS NOT FOLLOWED BY ANY OTHER INPUT CHARACTER. ***
        // Other line terminators are ignored, including the last one if it is followed by other input characters.
        //
        // Interestingly the corresponding Javadoc for 8, 11 and 17 does NOT describe this behaviour but the class DOES behave the same in all versions.
        // https://github.com/openjdk/jdk/commit/35acb891660fd5e0fee48b56acb16a6a193417ed and https://bugs.openjdk.org/browse/JDK-8296292
        // detail the issue being reported and the commit that changed the Javadoc
        softly.assertThat(Pattern.compile(fourDigitsRegexWithStartAndEnd).matcher(fourDigitsWithNewLine).find())
                .as("Matcher.find() without MULTILINE matches due to unusual behaviour of $ as described in the Javadoc")
                .isTrue();

        // If MULTILINE mode is activated then ^ matches at the beginning of input and after any line terminator except at the end of input.
        // When in MULTILINE mode $ matches just before a line terminator or the end of the input sequence.
        softly.assertThat(Pattern.compile(fourDigitsRegexWithStartAndEnd, Pattern.MULTILINE).matcher(fourDigitsWithNewLine).find())
                .as("Matcher.find() with MULTILINE")
                .isTrue();
    }

    @Test
    void regexWithLowercaseZedEnd_fourDigitsWithTrailingNewLine() {
        String fourDigitsWithNewLine = "1234\n";
        // Regex uses \z (lowercase) rather than $ to match the end of the input
        // \z matches "The end of the input"
        String fourDigitsRegexWithStartAndEnd = "^\\d{4}\\z";

        softly.assertThat(fourDigitsWithNewLine.matches(fourDigitsRegexWithStartAndEnd))
                .as("String.matches(regex) needs the entire string to match, so the trailing \n prevents it.")
                .isFalse();

        softly.assertThat(Pattern.matches(fourDigitsRegexWithStartAndEnd, fourDigitsWithNewLine))
                .as("Pattern.matches(regex, input) needs the entire string to match, so the trailing \n prevents it.")
                .isFalse();

        softly.assertThat(Pattern.compile(fourDigitsRegexWithStartAndEnd).matcher(fourDigitsWithNewLine).find())
                .as("Matcher.find() without MULTILINE does NOT match due to \\z behaving differently to $ when the string ends with a line terminator")
                .isFalse();

        softly.assertThat(Pattern.compile(fourDigitsRegexWithStartAndEnd, Pattern.MULTILINE).matcher(fourDigitsWithNewLine).find())
                .as("Matcher.find() with MULTILINE does NOT match due to \\z behaving the same regardless of MULTILINE")
                .isFalse();
    }

    @Test
    void regexWithUppercaseZedEnd_fourDigitsWithTrailingNewLine() {
        String fourDigitsWithNewLine = "1234\n";
        // Regex uses \Z (uppercase) rather than $ to match the end of the input
        // \Z (uppercase) matches "The end of the input but for the final terminator, if any"
        String fourDigitsRegexWithStartAndEnd = "^\\d{4}\\Z";

        softly.assertThat(fourDigitsWithNewLine.matches(fourDigitsRegexWithStartAndEnd))
                .as("String.matches(regex) needs the entire string to match, so the trailing \n prevents it.")
                .isFalse();

        softly.assertThat(Pattern.matches(fourDigitsRegexWithStartAndEnd, fourDigitsWithNewLine))
                .as("Pattern.matches(regex, input) needs the entire string to match, so the trailing \n prevents it.")
                .isFalse();

        softly.assertThat(Pattern.compile(fourDigitsRegexWithStartAndEnd).matcher(fourDigitsWithNewLine).find())
                .as("Matcher.find() without MULTILINE does match due to \\Z matching the end of the input but for the final terminator, if any")
                .isTrue();

        softly.assertThat(Pattern.compile(fourDigitsRegexWithStartAndEnd, Pattern.MULTILINE).matcher(fourDigitsWithNewLine).find())
                .as("Matcher.find() with MULTILINE does match due to \\Z behaving the same regardless of MULTILINE")
                .isTrue();
    }

    @Test
    void regexWithDollarEnd_fourDigits() {
        String fourDigits = "1234";
        String fourDigitsRegexWithStartAndEnd = "^\\d{4}$";

        softly.assertThat(fourDigits.matches(fourDigitsRegexWithStartAndEnd))
                .as("String.matches(regex)")
                .isTrue();

        softly.assertThat(Pattern.matches(fourDigitsRegexWithStartAndEnd, fourDigits))
                .as("Pattern.matches(regex, input)")
                .isTrue();

        // ^ and $ only match the beginning and end of the entire input when not using MULTILINE (the default)
        softly.assertThat(Pattern.compile(fourDigitsRegexWithStartAndEnd).matcher(fourDigits).find())
                .as("Matcher.find() without MULTILINE")
                .isTrue();

        // If MULTILINE mode is activated then ^ matches at the beginning of input and after any line terminator except at the end of input.
        // When in MULTILINE mode $ matches just before a line terminator or the end of the input sequence.
        softly.assertThat(Pattern.compile(fourDigitsRegexWithStartAndEnd, Pattern.MULTILINE).matcher(fourDigits).find())
                .as("Matcher.find() with MULTILINE")
                .isTrue();
    }

    @Test
    void regexWithoutEnd_fourDigitsBetweenLetters() {
        String fourDigitsBetweenLetters = "a1234b";
        String fourDigitsRegexWithoutStartAndEnd = "\\d{4}";

        softly.assertThat(fourDigitsBetweenLetters.matches(fourDigitsRegexWithoutStartAndEnd))
                .as("String.matches(regex) requires the entire string to match even when the regex does not specify ^$")
                .isFalse();

        softly.assertThat(Pattern.matches(fourDigitsRegexWithoutStartAndEnd, fourDigitsBetweenLetters))
                .as("Pattern.matches(regex, input) requires the entire string to match even when the regex does not specify ^$")
                .isFalse();

        // ^ and $ only match the beginning and end of the entire input when not using MULTILINE (the default)
        softly.assertThat(Pattern.compile(fourDigitsRegexWithoutStartAndEnd).matcher(fourDigitsBetweenLetters).find())
                .as("Matcher.find() without MULTILINE will match part of the string when the regex does not specify ^$")
                .isTrue();

        // If MULTILINE mode is activated then ^ matches at the beginning of input and after any line terminator except at the end of input.
        // When in MULTILINE mode $ matches just before a line terminator or the end of the input sequence.
        softly.assertThat(Pattern.compile(fourDigitsRegexWithoutStartAndEnd, Pattern.MULTILINE).matcher(fourDigitsBetweenLetters).find())
                .as("Matcher.find() with MULTILINE will match part of the string when the regex does not specify ^$")
                .isTrue();
    }
}
