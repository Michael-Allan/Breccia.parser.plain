package Breccia.parser.plain;

import Breccia.parser.ParseError;


public class MalformedMarkup extends ParseError {


    /** @see #pointer
      * @see #getMessage()
      */
    MalformedMarkup( final ErrorPointer pointer, final String message ) {
        super( pointer.lineNumber, message + '\n' + pointer.markedLine() );
        this.pointer = pointer; }



    /** @see #pointer
      */
    static MalformedMarkup misplacedNoBreakSpace( final ErrorPointer pointer ) {
        return new MalformedMarkup( pointer, "Misplaced no-break space (Unicode 00A0)" ); }



    /** Indicant of where precisely the ill form was detected.
      */
    public final ErrorPointer pointer;



    /** @see #pointer
      */
    static MalformedMarkup spaceExpected( final ErrorPointer pointer ) {
        return new MalformedMarkup( pointer, "Plain space expected (Unicode 0020)" ); }



    /** @see #pointer
      */
    static MalformedMarkup termExpected( final ErrorPointer pointer ) {
        return new MalformedMarkup( pointer, "Term expected" ); }



    /** @see #pointer
      * @param ch The character that implies the newline that never gets completed.
      */
    static MalformedMarkup truncatedNewline( final ErrorPointer pointer, final char ch ) {
        assert ch == '\r'; // For sake of an intelligible error message; see `Language.impliesNewline`.
        return new MalformedMarkup( pointer,
          "Truncated newline: Carriage return (Unicode 000D) without line feed successor (000A)" ); }



    /** @see #pointer
      */
    static MalformedMarkup truncatedPattern( final ErrorPointer pointer ) {
        return new MalformedMarkup( pointer, "Truncated pattern" ); }



    /** @see #pointer
      */
    static MalformedMarkup unexpectedTerm( final ErrorPointer pointer ) {
        return new MalformedMarkup( pointer, "Unexpected term" ); }}



                                                        // Copyright © 2021  Michael Allan.  Licence MIT.
