package Breccia.parser.plain;

import static java.lang.String.format;


public class ForbiddenWhitespace extends MalformedMarkup {


    /** @see #pointer
      * @see #ch
      */
    ForbiddenWhitespace( final ErrorPointer pointer, final char ch ) {
        super( pointer, "Unicode " + format( "%04x", Integer.valueOf(ch) ));
        this.ch = ch; }



    /** The forbidden character.
      */
    public final char ch; }


                                                        // Copyright © 2021  Michael Allan.  Licence MIT.
