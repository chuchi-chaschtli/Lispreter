# Lispreter
Open source Lisp to Java Interpreter

## Usage
Makefile/Runfile (or Mavenization) coming soon.

For now, clone the repository and compile it.

The following command line arguments may be specified:
- `-d` : debug error output to `stdout`.
- `-in <file>` : specify an input file to interpret. Otherwise, defaults to `stdin`.
- `-out <file>` : specify an output file to interpret. Otherwise, defaults to `stdout`.
- `-s <string>` : specifies the following string as a separator for each evaluation.  Defaults to `\n`.

## Design
The main design of this interpreter is separated into two components: the Lexer and
the Parser. The lexer determines the sentence structure, while the parser analyzes the
grammatical structure.

#### Lexer
The lexer performs lexical analysis. Given an input stream of characters, the Lexer's
goal is to separate the stream into tokens. These tokens are passed to the Parser.

#### Parser
The parser takes the token stream and converts it into a Syntax Tree. This syntax tree
represents the program output of the original CL file.

A syntax tree consists of multiple nodes. Each Node is either an Atom, or an S-Expression.
An S-Expression is either an Atom or a pair of S-Expressions. This allows for a recursive,
nested tree hierarchy, which is how data is represented in Lisp.

#### Environment
The environment manages lookup and extension for function definitions. A function may
either be a primitive operation (aka, essentially hard-coded) or a user-defined function.
A user-defined function can be anonymous. Since all functions are stored by the Environment,
lookup becomes trivial. An Environment may be updated given an old environment state, or
a mapping of alias -> node values.

## Contributing

This project is open-source, and falls under the [GNU GPLv3 License](https://github.com/AoHRuthless/Lispreter/blob/master/LICENSE).
As such, feel free to contribute.

#### Style Guidelines
- Wrap lines at 80 characters.
- The indentation hierarchy is 4 spaces.
- Open brackets on same line, use brackets even for one-line statements.

#### To-Do
- Bug Fixes. See the [Issue Tracker](https://github.com/AoHRuthless/Lispreter/issues).
- Support for constants and structs.
- Recursive Tail Optimization. Java 8 fails to support any TCO whatsoever, unlike CL, which tremendously slows the interpreter operations.