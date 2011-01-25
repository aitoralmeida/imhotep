/*
 * Copyright (C) 2010 PIRAmIDE-SP3 authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * This software consists of contributions made by many individuals, 
 * listed below:
 *
 * Author: Aitor Almeida <aitor.almeida@deusto.es>
 *         Pablo Ordu�a <pablo.orduna@deusto.es>
 *
 */

package piramide.multimodal.preprocessor;

import java.util.List;
import java.util.Vector;

import piramide.multimodal.preprocessor.exceptions.SyntaxErrorException;
import piramide.multimodal.preprocessor.parser.Branch;
import piramide.multimodal.preprocessor.parser.ConditionNode;
import piramide.multimodal.preprocessor.parser.ElseBranch;
import piramide.multimodal.preprocessor.parser.ErrorNode;
import piramide.multimodal.preprocessor.parser.HiddenNode;
import piramide.multimodal.preprocessor.parser.IfBranch;
import piramide.multimodal.preprocessor.parser.Node;
import piramide.multimodal.preprocessor.parser.TextNode;

public class Lexer {
	
	private Directives directives;

	private static class LexerResult{
		private final int lastLine;
		private final Node [] nodes;
		
		public LexerResult(int lastLine, Node [] nodes){
			this.lastLine = lastLine;
			this.nodes = nodes;
		}

		public int getLastLine() {
			return this.lastLine;
		}
		
		public Node [] getNodes() {
			return this.nodes;
		}
	}
	
	public Lexer (String directivestart)
	{
		this.directives = new Directives(directivestart);
	}
	
	public Node [] lex(String filename, String text) throws SyntaxErrorException {
		final String [] lines = this.realSplit(text, "\n");
		
		final LexerResult result = this.lex(filename, lines, 0);
		if(result.lastLine != lines.length)
			throw new SyntaxErrorException("An 'if' clause was expected and end of file found", lines.length - 1);
		return result.getNodes();
	}
	
	/**
	 * "AA\n\n".split("\n") returns { "AA" }, discarding the other two "". This implementation doesn't.
	 */
	private String [] realSplit(String s, String expr){
		List<String> found = new Vector<String>();
		
		String remaining = s;
		int pos;
		do{
			pos = remaining.indexOf(expr);
			if(pos >= 0){
				String current = remaining.substring(0, pos);
				found.add(current);
				remaining = remaining.substring(pos + 1);
			}else{
				found.add(remaining);
			}
		}while(pos >= 0);
		
		return found.toArray(new String[]{});
	}
	
	private LexerResult lex(String filename, String [] lines, int startLine) throws SyntaxErrorException {
		final List<Node> finalNodes = new Vector<Node>();
		
		String currentText = "";
		
		for(int currentLineNumber = startLine; currentLineNumber < lines.length; ++currentLineNumber){
			final String currentLine = lines[currentLineNumber];
			
			if(isDirectiveLine(currentLine)){
				final int pos = currentLine.indexOf(this.directives.DIRECTIVE_START);
				final String rest = currentLine.substring(0, pos);
				
				currentText += rest;
				if(currentText.length() > 0){
					finalNodes.add(new TextNode(currentText));
					currentText = "";
				}
				
				if(currentLine.indexOf(this.directives.HIDDEN) == pos){
					
					finalNodes.add(new HiddenNode(currentLineNumber, currentLine.substring(pos + this.directives.HIDDEN.length())));
					
				}else if(currentLine.indexOf(this.directives.USER_DEFINED_ERROR) == pos){
					
					finalNodes.add(new ErrorNode(currentLineNumber, currentLine.substring(pos + this.directives.USER_DEFINED_ERROR.length())));
					
				}else if(currentLine.indexOf(this.directives.IF) == pos){
					
					final List<Branch> branches = new Vector<Branch>();
					
					currentLineNumber = parseConditionNode(filename, lines, currentLineNumber, branches);
					
					final ConditionNode currentNode = new ConditionNode(currentLineNumber, branches); 
					finalNodes.add(currentNode);
					
				}else if(currentLine.indexOf(this.directives.ELIF) == pos){
					return new LexerResult(currentLineNumber, finalNodes.toArray(new Node[]{}));
				}else if(currentLine.indexOf(this.directives.ELSE) == pos){
					return new LexerResult(currentLineNumber, finalNodes.toArray(new Node[]{}));
				}else if(currentLine.indexOf(this.directives.ENDIF) == pos){
					return new LexerResult(currentLineNumber, finalNodes.toArray(new Node[]{}));
				}else{
					throw new SyntaxErrorException("Unrecognized token after " + this.directives.DIRECTIVE_START + ": " + currentLine + " in line: " + currentLineNumber + " in file: " + filename, currentLineNumber);
				}
			}else
				currentText += currentLine + "\n";
		}
		
		if(currentText.length() > 0){
			currentText = currentText.substring(0, currentText.length() - 1);
			finalNodes.add(new TextNode(currentText));
		}
		
		final Node [] returnNodes = finalNodes.toArray(new Node[]{});
		return new LexerResult(lines.length, returnNodes);
	}

	private boolean isDirectiveLine(final String line) {
		return line.indexOf(this.directives.DIRECTIVE_START) >= 0;
	}

	private int parseConditionNode(String filename, String[] lines, int initialLineNumber, List<Branch> branches) throws SyntaxErrorException {
		int currentLineNumber = initialLineNumber;
		
		do{
			if(currentLineNumber >= lines.length)
				throw new SyntaxErrorException("End of file found before #if directive finished in line: " + (lines.length - 1), lines.length - 1);
			final String newLine  = lines[currentLineNumber];
			final int startingPos = newLine.indexOf(this.directives.DIRECTIVE_START);
			
			if(isEndif(newLine, startingPos))
				break;
			
			final LexerResult result = this.lex(filename, lines, currentLineNumber + 1); // recursively until next elif / else
			
			if(isElse(newLine, startingPos))
				branches.add(new ElseBranch(result.getNodes()));
			else{
				
				final int currentPos = calculateCurrentPos_if_elif(newLine, startingPos);
				final String condition = newLine.substring(currentPos);
				
				branches.add(new IfBranch(result.getLastLine(), condition, result.getNodes()));
			}

			currentLineNumber = result.getLastLine(); // Continue from that point
		}while(true);
		
		return currentLineNumber;
	}

	private boolean isElse(String line, int startingPos) {
		return line.indexOf(this.directives.ELSE) == startingPos;
	}

	private boolean isEndif(String line, int startingPos) {
		return line.indexOf(this.directives.ENDIF) == startingPos;
	}

	private int calculateCurrentPos_if_elif(String line, int pos) {
		int currentPos = line.indexOf(this.directives.IF);
		if(currentPos != pos)
			currentPos = line.indexOf(this.directives.ELIF);
		else
			return currentPos + this.directives.IF.length();
		if(currentPos != pos)
			throw new IllegalStateException("Invalid directive found: " + line);
		else
			return currentPos + this.directives.ELIF.length();
	}
	
}
