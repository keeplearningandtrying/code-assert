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
package guru.nidi.codeassert;

import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.BuildPluginManager;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.*;
import org.apache.maven.project.MavenProject;

import static org.twdata.maven.mojoexecutor.MojoExecutor.*;

/**
 * Run a test that can consume the Jacoco test coverage data.
 */
@Mojo(name = "assert", defaultPhase = LifecyclePhase.TEST, requiresDependencyResolution = ResolutionScope.TEST)
public class AssertMojo extends AbstractMojo {
    static final String JACOCO_VERSION = "0.7.7.201606060606";

    /**
     * The test class to be run.
     */
    @Parameter(property = "testClass", defaultValue = "CodeCoverage")
    private String testClass;

    @Parameter(defaultValue = "${project}", readonly = true)
    private MavenProject mavenProject;

    @Parameter(defaultValue = "${session}", readonly = true)
    private MavenSession mavenSession;

    @Component
    private BuildPluginManager pluginManager;

    public void execute() throws MojoExecutionException, MojoFailureException {
        report();
        runTest();
    }

    private void report() throws MojoExecutionException {
        executeMojo(
                plugin(
                        groupId("org.jacoco"),
                        artifactId("jacoco-maven-plugin"),
                        version(JACOCO_VERSION)
                ),
                goal("report"),
                configuration(),
                executionEnvironment(mavenProject, mavenSession, pluginManager)
        );
    }

    private void runTest() throws MojoExecutionException {
        executeMojo(
                plugin(
                        groupId("org.apache.maven.plugins"),
                        artifactId("maven-surefire-plugin"),
                        version("2.19.1")
                ),
                goal("test"),
                configuration(
                        element("test", testClass),
                        element("failIfNoTests", "false")
                ),
                executionEnvironment(mavenProject, mavenSession, pluginManager)
        );
    }
}
