/*
 * Copyright © 2015 Stefan Niederhauser (nidin@gmx.ch)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package guru.nidi.codeassert.pmd;

import org.hamcrest.*;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static java.lang.System.lineSeparator;

public class RegexMatcher extends TypeSafeMatcher<String> {
    private final List<Pattern> patterns;

    public RegexMatcher(List<Pattern> patterns) {
        this.patterns = patterns;
    }

    public static Matcher<String> matchesFormat(String format) {
        final List<Pattern> patterns = new ArrayList<>();
        for (final String line : format.split(lineSeparator())) {
            patterns.add(Pattern.compile(Pattern.quote(line).replace("%d", "\\E\\d+\\Q")));
        }
        return new RegexMatcher(patterns);
    }

    @Override
    protected boolean matchesSafely(String item) {
        final String[] lines = item.split(lineSeparator());
        if (patterns.size() != lines.length) {
            return false;
        }
        for (int i = 0; i < lines.length; i++) {
            if (!patterns.get(i).matcher(lines[i]).matches()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText(String.format("matches pattern%n"));
        for (final Pattern p : patterns) {
            description.appendText(pattern(p)).appendText(lineSeparator());
        }
    }

    @Override
    protected void describeMismatchSafely(String item, Description description) {
        description.appendText(String.format("was%n%s%n", item));
        final String[] lines = item.split(lineSeparator());
        int i;
        for (i = 0; i < lines.length && i < patterns.size(); i++) {
            if (!patterns.get(i).matcher(lines[i]).matches()) {
                description.appendText(String.format("At line %d expected:%n%s%nbut got%n%s",
                        i + 1, pattern(patterns.get(i)), lines[i]));
                return;
            }
        }
        if (lines.length > i) {
            description.appendText("expected no more lines, but got " + (lines.length - i) + " more.");
        } else {
            description.appendText("expected " + (patterns.size() - i) + " more lines, but got none.");
        }
    }

    private String pattern(Pattern p) {
        return p.pattern().replace("\\d+", "%d").replace("\\Q", "")
                .replaceAll("([^\\\\])\\\\E", "$1").replace("\\\\E", "\\E");
    }
}
