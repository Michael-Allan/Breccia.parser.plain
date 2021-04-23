package Breccia.parser.plain;


public class IllegalParseState extends IllegalStateException {


    /** @see #pointer
      * @see #getMessage()
      */
    IllegalParseState( final ErrorPointer pointer, final String message ) {
        super( message + '\n' + pointer.markedLine() );
        lineNumber = pointer.lineNumber;
        this.pointer = pointer; }



    /** Ordinal number of the line in which the illegal parse state occured.
      * Lines are numbered beginning at one.
      */
    public final int lineNumber;



    /** Indicant of where precisely the illegal parse state occured.
      */
    public final ErrorPointer pointer; }



                                                        // Copyright © 2021  Michael Allan.  Licence MIT.
