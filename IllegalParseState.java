package Breccia.parser.plain;

import Java.CharacterPointer;


public class IllegalParseState extends IllegalStateException {


    /** @see #pointer
      * @see #getMessage()
      */
    IllegalParseState( final CharacterPointer pointer, final String message ) {
        super( message + '\n' + pointer.markedLine() );
        this.pointer = pointer; }



    /** Indicant of where precisely the illegal parse state occured.
      */
    public final CharacterPointer pointer; }



                                                   // Copyright © 2021-2022  Michael Allan.  Licence MIT.
