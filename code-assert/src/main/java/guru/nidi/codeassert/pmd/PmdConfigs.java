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

import guru.nidi.codeassert.config.*;

import static guru.nidi.codeassert.pmd.PmdRulesets.*;

public final class PmdConfigs {
    private PmdConfigs() {
    }

    public static CollectorTemplate<Ignore> minimalPmdIgnore() {
        return CollectorTemplate.forA(PmdViolationCollector.class)
                .because("junit", In.classes("*Test", "Test*")
                        .ignore("JUnitSpelling", "JUnitAssertionsShouldIncludeMessage", "AvoidDuplicateLiterals",
                                "SignatureDeclareThrowsException", "TooManyStaticImports"))
                .because("I don't agree", In.everywhere()
                        .ignore("MethodArgumentCouldBeFinal", "AvoidFieldNameMatchingMethodName",
                                "CommentDefaultAccessModifier", "AbstractNaming", "AvoidFieldNameMatchingTypeName",
                                "UncommentedEmptyConstructor", "UseStringBufferForStringAppends",
                                "UncommentedEmptyMethodBody", "EmptyMethodInAbstractClassShouldBeAbstract",
                                "InefficientEmptyStringCheck"))
                .because("it's equals", In.methods("equals")
                        .ignore("NPathComplexity", "ModifiedCyclomaticComplexity", "StdCyclomaticComplexity",
                                "CyclomaticComplexity", "ConfusingTernary"))
                .because("it's hashCode", In.methods("hashCode")
                        .ignore("ConfusingTernary"));
    }

    public static CollectorTemplate<Ignore> dependencyTestIgnore(Class<?> dependencyTest) {
        return CollectorTemplate.of(Ignore.class).just(In.clazz(dependencyTest)
                .ignore("AvoidDollarSigns", "VariableNamingConventions", "SuspiciousConstantFieldName"));
    }

    public static CollectorTemplate<Ignore> cpdIgnoreEqualsHashCodeToString() {
        return CollectorTemplate.forA(CpdMatchCollector.class)
                .because("equals, hashCode, toString sometimes look the same", In.everywhere()
                        .ignore("public boolean equals(Object", "public int hashCode()", "public String toString()"));
    }

    public static PmdRuleset[] defaultPmdRulesets() {
        return new PmdRuleset[]{
                basic(), braces(),
                comments().maxLines(35).maxLineLen(120).requirement(PmdRulesets.Comments.Requirement.Ignored),
                codesize().excessiveMethodLength(40).tooManyMethods(30),
                design(), empty().allowCommentedEmptyCatch(true), exceptions(), imports(), junit(),
                naming().variableLen(1, 25).methodLen(2),
                optimizations(), strings(),
                sunSecure(), typeResolution(), unnecessary(), unused()};
    }
}
