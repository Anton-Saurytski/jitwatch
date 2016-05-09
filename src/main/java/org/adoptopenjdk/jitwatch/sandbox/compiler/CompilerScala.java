/*
 * Copyright (c) 2013-2016 Chris Newland.
 * Licensed under https://github.com/AdoptOpenJDK/jitwatch/blob/master/LICENSE-BSD
 * Instructions: https://github.com/AdoptOpenJDK/jitwatch/wiki
 */
package org.adoptopenjdk.jitwatch.sandbox.compiler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.adoptopenjdk.jitwatch.process.AbstractProcess;
import org.adoptopenjdk.jitwatch.sandbox.ISandboxLogListener;
import org.adoptopenjdk.jitwatch.sandbox.Sandbox;

public class CompilerScala extends AbstractProcess implements ICompiler
{
	private Path compilerPath;

	private final String COMPILER_NAME = "scalac";

	public CompilerScala(String languageHomeDir) throws FileNotFoundException
	{
		super(Sandbox.PATH_STD_ERR, Sandbox.PATH_STD_OUT);

		compilerPath = Paths.get(languageHomeDir, "bin", COMPILER_NAME);

		if (!compilerPath.toFile().exists())
		{
			throw new FileNotFoundException("Could not find " + COMPILER_NAME);
		}

		compilerPath = compilerPath.normalize();
	}

	@Override
	public boolean compile(List<File> sourceFiles, List<String> classpathEntries, File outputDir, ISandboxLogListener logListener)
			throws IOException
	{
		List<String> commands = new ArrayList<>();

		commands.add(compilerPath.toString());

		String outputDirPath = outputDir.getAbsolutePath().toString();

		List<String> compileOptions = Arrays.asList(new String[] { "-g:vars", "-d", outputDirPath });

		commands.addAll(compileOptions);

		if (classpathEntries.size() > 0)
		{
			commands.add("-classpath");

			commands.add(makeClassPath(classpathEntries));
		}

		for (File sourceFile : sourceFiles)
		{
			commands.add(sourceFile.getAbsolutePath());
		}

		return runCommands(commands, logListener);
	}
}