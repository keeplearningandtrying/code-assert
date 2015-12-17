/*
 * Copyright (C) 2015 Stefan Niederhauser (nidin@gmx.ch)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package guru.nidi.codeassert.model;

import org.junit.Test;

import java.io.IOException;
import java.util.Collection;

import static guru.nidi.codeassert.model.PackageCollector.all;
import static org.junit.Assert.assertEquals;

/**
 *
 */
public class ProjectTest {
    @Test
    public void simple() throws IOException {
        final Collection<JavaPackage> packs = new Project()
                .fromCode(Path.TEST_CLASSES, Path.CLASSES)
                .readPackages(all());
        assertEquals(35, packs.size());
    }

    @Test
    public void filter() throws IOException {
        final Collection<JavaPackage> packs = new Project()
                .fromCode(Path.TEST_CLASSES, Path.CLASSES)
                .readPackages(all().excluding("java.*", "javax.*", "org.*"));
        assertEquals(15, packs.size());
    }
}
