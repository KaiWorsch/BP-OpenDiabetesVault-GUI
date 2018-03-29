/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opendiabetesvaultgui.markdownparser;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import static java.util.stream.Collectors.joining;
import java.util.stream.Stream;
import org.eclipse.mylyn.wikitext.core.parser.MarkupParser;
import org.eclipse.mylyn.wikitext.markdown.core.MarkdownLanguage;

/**
 * help class to handle md to html parsing.
 * @author Daniel Schäfer, Martin Steil, Julian Schwind, Kai Worsch
 */
public class Parser {

    /**
     * takes an md file given as a filepath, returns the given file as html code
     * as a String.
     *
     * @param filePath path of the file to parse
     * @return html code as a String
     * @throws FileNotFoundException Exception if the file cannot be found
     */
    public static String mdParse(final String filePath)
            throws FileNotFoundException {
        String fileContent = readFile(filePath);
        fileContent = parseString(fileContent);
        return fileContent;
    }

    /**
     * takes md code as a String and returns it as html code.
     *
     * @param markupContent the md code
     * @return the html code as a String
     */
    private static String parseString(final String markupContent) {
        MarkupParser markupParser = new MarkupParser();
        markupParser.setMarkupLanguage(new MarkdownLanguage());
        String htmlContent = markupParser.parseToHtml(markupContent);
        return htmlContent;
    }

    /**
     * takes a filepath, reads the given file and parses it into a String with
     * whitespaces.
     *
     * stackoverflow.com/questions/5868369/how-to-read-a-large-text-file-line-by-line-using-java
     * www.leveluplunch.com/java/examples/convert-java-util-stream-to-string/
     *
     * @param filePath path of the file to read
     * @return the filecontent as a String
     * @throws FileNotFoundException Exception if the file cannot be found
     */
    private static String readFile(final String filePath)
            throws FileNotFoundException {
        try (Stream<String> stream = Files.lines(Paths.get(filePath))) {
            String returnValue = stream.
                    collect(joining(System.lineSeparator()));
            return returnValue;
        } catch (IOException ex) {
            Logger.getLogger(Parser.class.getName()).
                    log(Level.SEVERE, null, ex);
        }
        return "Error reading file";
    }

}
