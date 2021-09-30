package Breccia.parser.plain;


final class Language {


    private Language() {}



    /** Whether character  `ch` is one that formally ends a newline.
      * Returns true if `ch` is a line feed (A).
      */
    static boolean completesNewline( final char ch ) { return ch == '\n'; }


    /** Whether code point `ch` is one that formally ends a newline.
      * Returns true if `ch` is a line feed (A).
      */
    static boolean completesNewline( final  int ch ) { return ch == '\n'; }



    /** Whether character  `ch` is proper to a newline.
      * Returns true if `ch` is a line feed (A) or carriage return (D).
      */
    static boolean impliesNewline( final char ch ) { return ch == '\n' || ch == '\r'; }


    /** Whether code point `ch` is proper to a newline.
      * Returns true if `ch` is a line feed (A) or carriage return (D).
      */
    static boolean impliesNewline( final  int ch ) { return ch == '\n' || ch == '\r'; }



    /** Whether character  `ch` is proper to a newline, yet does not formally complete it.
      * Returns true if `ch` is a carriage return (D).
      */
    static boolean impliesWithoutCompletingNewline( final char ch ) { return ch == '\r'; }


    /** Whether code point `ch` is proper to a newline, yet does not formally complete it.
      * Returns true if `ch` is a carriage return (D).
      */
    static boolean impliesWithoutCompletingNewline( final  int ch ) { return ch == '\r'; }



    /** Whether `ch` is a divider drawing character, a character in the range 2500-259F.
      */
    static boolean isDividerDrawing( final char ch ) { return '\u2500' <= ch && ch <= '\u259F'; }


    /** Whether `ch` is a divider drawing character, a character in the range 2500-259F.
      */
    static boolean isDividerDrawing( final  int ch ) { return '\u2500' <= ch && ch <= '\u259F'; }



    /** Whether character  `ch` is a whitespace character other than those allowed.  This method is
      * equivalent to `!= ' '
      *          &amp; !{@linkplain #impliesNewline(char) impliesNewline}
      *          &amp;  {@linkplain #yetIsGenerallyWhitespace(char) yetIsGenerallyWhitespace}`.
      */
    static boolean isForbiddenWhitespace( final char ch ) {
        return !isPlainWhitespace(ch) && yetIsGenerallyWhitespace(ch); }


    /** Whether code point `ch` is a whitespace character other than those allowed.  This method is
      * equivalent to `!= ' '
      *          &amp; !{@linkplain #impliesNewline(int)  impliesNewline}
      *          &amp;  {@linkplain #yetIsGenerallyWhitespace(int)  yetIsGenerallyWhitespace}`.
      */
    static boolean isForbiddenWhitespace( final  int ch ) {
        return !isPlainWhitespace(ch) && yetIsGenerallyWhitespace(ch); }



    /** Whether character  `ch` is a plain space (20) or newline constituent (A or D).
      */
    static boolean isPlainWhitespace( final char ch ) { return ch == ' ' || impliesNewline(ch); }


    /** Whether character  `ch` is a plain space (20) or newline constituent (A or D).
      */
    static boolean isPlainWhitespace( final  int ch ) { return ch == ' ' || impliesNewline(ch); }



    /** Whether character  `ch` is a plain (20) or no-break space (A0).
      */
    static boolean isSpace( final char ch ) { return ch == ' ' || ch == '\u00A0'; }


    /** Whether code point `ch` is a plain (20) or no-break space (A0).
      */
    static boolean isSpace( final  int ch ) { return ch == ' ' || ch == '\u00A0'; }



    /** Whether character `ch` is a plain space (20), no-break space (A0)
      * or newline constituent (A or D).
      */
    static boolean isWhitespace( final char ch ) { return isPlainWhitespace(ch) || ch == '\u00A0'; }


    /** Whether character `ch` is a plain space (20), no-break space (A0)
      * or newline constituent (A or D).
      */
    static boolean isWhitespace( final  int ch ) { return isPlainWhitespace(ch) || ch == '\u00A0'; }



    /** Whether code point `nonSpaceNewline` is a whitespace character other than a no-break space (A0)
      * when already it is known to be neither a plain space nor a newline constituent.
      *
      *     @param nonSpaceNewline A character known to be other than a plain space (20)
      *       or {@linkplain #impliesNewline(char) newline constituent}
      *     @throws AssertionError If assertions are enabled and `nonSpaceNewline` is a plain space
      *       or newline constituent.
      *     @see Java.Characters.isJavaOrUnicodeWhitespace(char)
      */
    static boolean yetIsGenerallyWhitespace( final char nonSpaceNewline ) {
        return yetIsGenerallyWhitespace( (int)nonSpaceNewline ); }


    /** Whether code point `nonSpaceNewline` is a whitespace character other than a no-break space (A0)
      * when already it is known to be neither a plain space nor a newline constituent.
      *
      *     @param nonSpaceNewline A code point known to be other than a plain space (20)
      *       or {@linkplain #impliesNewline(int) newline constituent}
      *     @throws AssertionError If assertions are enabled and `nonSpaceNewline` is a plain space
      *       or newline constituent.
      *     @see Java.Characters.isJavaOrUnicodeWhitespace(int)
      */
    static boolean yetIsGenerallyWhitespace( final  int nonSpaceNewline ) {
        assert !isPlainWhitespace( nonSpaceNewline );
        return Character.isWhitespace/*[TL]*/( nonSpaceNewline ) /* Which test excludes the allowed
            no-break space (A0), plus some forbidden no-break spaces.  Wherefore include the latter: */
          || nonSpaceNewline == '\u2007' || nonSpaceNewline == '\u202F'; }}



// NOTE
// ────
//   TL · Apparently implemented as a tabular lookup, at least in the JDK.  Very fast.



                                                        // Copyright © 2021  Michael Allan.  Licence MIT.
