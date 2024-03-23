package Breccia.parser.plain;

import Breccia.parser.*;
import Java.*;
import java.io.IOException;
import java.io.Reader;
import java.lang.annotation.*;
import java.nio.CharBuffer;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;

import static Breccia.parser.plain.Language.*;
import static Breccia.parser.plain.MalformedText.*;
import static Breccia.parser.plain.Project.newSourceReader;
import static Java.CharBuffers.newDelimitableCharSequence;
import static Java.CharBuffers.transferDirectly;
import static Java.CharSequences.equalInContent;
import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.SOURCE;
import static java.lang.Character.codePointAt;
import static java.lang.Character.isAlphabetic;
import static java.lang.Character.isDigit;
import static Java.StringBuilding.clear;
import static Java.Unicode.graphemeClusterPattern;
import static java.util.Arrays.binarySearch;
import static java.util.Arrays.sort;


/** A reusable, pull parser of plain Breccia as reflected through a cursor.
  */
public class BrecciaCursor implements ReusableCursor {


    public BrecciaCursor() {

      // ══════════════════════════
      // Late field initializations — each would fail if written in line with the field declarator
      // ══════════════════════════

      // `spooler` dependant
      // ┈┈┈┈┈┈┈┈┈
        _appendageParserC = new AppendageParserC();

      // `basicAfterlinker` (etc.) dependants
      // ┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈
        final String[] commandPointKeywords = { // Those known to Breccia, in lexicographic order.
            "N.B.",
            "NB",
            "ad",
            "cf.",
            "contra",
            "e.g.",
            "i.e.",
            "join",
            "note",
            "on",
            "pace",
            "private",
            "q.v.",
            "re",
            "sc.",
            "see",
            "viz." };
        final CommandPoint_<?>[] commandPoints = { // Each at the same index as its keyword above.
            basicAfterlinker,   // ‘N.B.’
            basicAfterlinker,   // ‘NB’
            basicNoteCarrier,   // ‘ad’
            basicAfterlinker,   // ‘cf.’
            basicAfterlinker,   // ‘contra’
            basicAfterlinker,   // ‘e.g.’
            basicAfterlinker,   // ‘i.e.’
            basicAfterlinker,   // ‘join’
            basicNoteCarrier,   // ‘note’
            basicNoteCarrier,   // ‘on’
            basicAfterlinker,   // ‘pace’
            basicPrivatizer,    // ‘private’
            basicAfterlinker,   // ‘q.v.’
            basicAfterlinker,   // ‘re’
            basicAfterlinker,   // ‘sc.’
            basicAfterlinker,   // ‘see’
            basicAfterlinker }; // ‘viz.’
        this.commandPointKeywords = commandPointKeywords;
        this.commandPoints = commandPoints; }



   // ━━━  C u r s o r  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    public final @Override @NarrowNot Afterlinker asAfterlinker() throws MalformedText {
        if( state != afterlinker ) return null;
        afterlinker.ensureComposition();
        return afterlinker; }



    public final @Override @NarrowNot Afterlinker.End asAfterlinkerEnd() {
        return state == afterlinkerEnd ? afterlinkerEnd : null; }



    public final @Override @NarrowNot AlarmPoint asAlarmPoint() {
        return state == alarmPoint ? alarmPoint : null; }



    public final @Override @NarrowNot AlarmPoint.End asAlarmPointEnd() {
        return state == alarmPointEnd ? alarmPointEnd : null; }



    public final @Override @NarrowNot AsidePoint asAsidePoint() {
        return state == asidePoint ? asidePoint : null; }



    public final @Override @NarrowNot AsidePoint.End asAsidePointEnd() {
        return state == asidePointEnd ? asidePointEnd : null; }



    public final @Override @NarrowNot BodyFractum asBodyFractum() {
        return state == bodyFractum ? bodyFractum : null; }



    public final @Override @NarrowNot BodyFractum.End asBodyFractumEnd() {
        return state == bodyFractumEnd ? bodyFractumEnd : null; }



    public final @Override @NarrowNot CommandPoint asCommandPoint() {
        return state == commandPoint ? commandPoint : null; }



    public final @Override @NarrowNot CommandPoint.End asCommandPointEnd() {
        return state == commandPointEnd ? commandPointEnd : null; }



    public final @Override @NarrowNot Division asDivision() {
        return state == division ? division : null; }



    public final @Override @NarrowNot Division.End asDivisionEnd() {
        return state == divisionEnd ? divisionEnd : null; }



    public final @Override @NarrowNot Empty asEmpty() { return state == empty ? empty : null; }



    public final @Override @NarrowNot FileFractum asFileFractum() {
        return state == fileFractum ? fileFractum : null; }



    public final @Override @NarrowNot FileFractum.End asFileFractumEnd() {
        return state == fileFractumEnd ? fileFractumEnd : null; }



    public final @Override @NarrowNot Fractum asFractum() { return state == fractum ? fractum : null; }



    public final @Override @NarrowNot Fractum.End asFractumEnd() {
        return state == fractumEnd ? fractumEnd : null; }



    public final @Override @NarrowNot Halt asHalt() { return state == halt ? halt : null; }



    public final @Override @NarrowNot NoteCarrier asNoteCarrier() {
        return state == noteCarrier ? noteCarrier : null; }



    public final @Override @NarrowNot NoteCarrier.End asNoteCarrierEnd() {
        return state == noteCarrierEnd ? noteCarrierEnd : null; }



    public final @Override @NarrowNot PlainCommandPoint asPlainCommandPoint() {
        return state == plainCommandPoint ? plainCommandPoint : null; }



    public final @Override @NarrowNot PlainCommandPoint.End asPlainCommandPointEnd() {
        return state == plainCommandPointEnd ? plainCommandPointEnd : null; }



    public final @Override @NarrowNot PlainPoint asPlainPoint() {
        return state == plainPoint ? plainPoint : null; }



    public final @Override @NarrowNot PlainPoint.End asPlainPointEnd() {
        return state == plainPointEnd ? plainPointEnd : null; }



    public final @Override @NarrowNot Point asPoint() { return state == point ? point : null; }



    public final @Override @NarrowNot Point.End asPointEnd() {
        return state == pointEnd ? pointEnd : null; }



    public final @Override @NarrowNot Privatizer asPrivatizer() {
        return state == privatizer ? privatizer : null; }



    public final @Override @NarrowNot Privatizer.End asPrivatizerEnd() {
        return state == privatizerEnd ? privatizerEnd : null; }



    public final @Override @NarrowNot TaskPoint asTaskPoint() {
        return state == taskPoint ? taskPoint : null; }



    public final @Override @NarrowNot TaskPoint.End asTaskPointEnd() {
        return state == taskPointEnd ? taskPointEnd : null; }



    public final @Override boolean isPrivatized( final int[] xuncFractalDescent ) {
        if( !state.isFinal() || state == halt ) throw new IllegalStateException();
        final int pEnd = xuncPrivatized.length;
        if( pEnd == 0 ) return false; // No fractum is privatized, so nothing at all is privatized.
        if( xuncPrivatized.array[0] == -1 ) return true; // The file fractum is, so everything is.
        int pStart = 0;
        for( final int xD: xuncFractalDescent ) {
            for( int p = pStart; p < pEnd; ++p ) {
                final int xP = xuncPrivatized.array[p];
                if( xP == xD) return true; /* A fractum in the granum’s line of descent is privatized
                  (either an ancestor or the granum itself) whereby the granum too is privatized. */
                if( xP > xD ) { // Then the remaining `xP` will also be greater, none matching.
                    pStart = p; /* Start here for the next `xD` because none of the preceding `xP`
                      will be able to match it, because it will be larger than the present `xD`. */
                    break; }}}
        return false; } /* No fractum in the granum’s line of descent is privatized (neither an ancestor
          nor the granum itself) so the granum is not privatized. */



    public final @Override @NarrowNot ParseState next() throws ParseError {
        if( state.isFinal() ) throw new NoSuchElementException();
        try { _next(); }
        catch( ParseError x ) {
            disable();
            throw x; }
        assert !(state instanceof Empty || state instanceof Halt);
        return state; }



    public final @Override void perState( final Consumer<ParseState> sink ) throws ParseError {
        try {
            for( ;; ) {
                sink.accept( state );
                if( state.isFinal() ) break;
                _next(); }}
        catch( ParseError x ) {
            disable();
            throw x; }}



    public final @Override void perStateConditionally( final Predicate<ParseState> sink )
          throws ParseError {
        try {
            while( sink.test(state) && !state.isFinal() ) _next(); }
        catch( ParseError x ) {
            disable();
            throw x; }}



    public final @Override @NarrowNot ParseState state() { return state; }



   // ━━━  R e u s a b l e   C u r s o r  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    public final @Override void perState( final Path sourceFile, final Consumer<ParseState> sink )
          throws ParseError {
        try( final Reader r = newSourceReader​( sourceFile )) {
            source( r );
            for( ;; ) {
                sink.accept( state );
                if( state.isFinal() ) break;
                _next(); }}
        catch( IOException x ) { throw new Unhandled( x ); }
        catch( ParseError x ) {
            disable();
            throw x; }}



    public final @Override void perStateConditionally( final Path sourceFile,
          final Predicate<ParseState> sink ) throws ParseError {
        try( final Reader r = newSourceReader​( sourceFile )) {
            source( r );
            while( sink.test(state) && !state.isFinal() ) _next(); }
        catch( IOException x ) { throw new Unhandled( x ); }
        catch( ParseError x ) {
            disable();
            throw x; }}



    public final @Override void source( final Reader r ) throws ParseError {
        try { _source( r ); }
        catch( ParseError x ) {
            disable();
            throw x; }}



////  P r i v a t e  ////////////////////////////////////////////////////////////////////////////////////


    /** @param aKeywords Additional keywords in lexicographic order
      *   to merge into {@linkplain #commandPointKeywords commandPointKeywords}.
      * @param aPoints The corresponding command points,
      *   each at the same index as its keyword in `aKeywords`.
      */
    protected final void addCommandPointKeywords( final String[] aKeywords,
          final CommandPoint_<?>[] aPoints ) {
        final String[] bKeywords = commandPointKeywords;
        final CommandPoint_<?>[] bPoints = commandPoints;
        final int aN = aKeywords.length;
        final int bN = bKeywords.length;

      // Merge sort  (a, b) → m
      // ──────────
        final int mN = bN + aN;
        final String[] mKeywords = new String[ mN ];
        final String[] rKeywords; // Remainder, one of `a` or `bKeywords`.
        final CommandPoint_<?>[] mPoints = new CommandPoint_<?>[ mN ];
        final CommandPoint_<?>[] rPoints; // One of `a` or `bPoints`.
        int m = 0;
        int r;// Index to remainder, either of `a` or `b`.
        String rKeyword;
        for( int a = 0, b = 0;; ++m ) {
            final String aKeyword = aKeywords[a];
            final String bKeyword = bKeywords[b];
            final int signum = CharSequence.compare( aKeyword, bKeyword );
            if( signum < 0 ) {
                mKeywords[m] = aKeyword;
                mPoints[m] = aPoints[a];
                if( ++a == aN ) {
                    r = b;
                    rKeyword = bKeyword;
                    rKeywords = bKeywords;
                    rPoints = bPoints;
                    break; }}
            else if( signum > 0 ) {
                mKeywords[m] = bKeyword;
                mPoints[m] = bPoints[b];
                if( ++b == bN ) {
                    r = a;
                    rKeyword = aKeyword;
                    rKeywords = aKeywords;
                    rPoints = aPoints;
                    break; }}
            else throw new IllegalStateException(); } // Already the keyword was there.
        for( ++m;; rKeyword = rKeywords[++r] ) {
            mKeywords[m] = rKeyword;
            mPoints[m] = rPoints[r];
            if( ++m == mN ) break; }
        commandPointKeywords = mKeywords;
        commandPoints = mPoints; }



    private final AppendageParser appendageParser = new AppendageParser();



    private final AppendageParserC _appendageParserC;



    /** Resets and returns `_appendageParserC`.
      */
    private AppendageParserC appendageParserCReset() {
        _appendageParserC.reset();
        return _appendageParserC; }



    /** Parses any comment appender the delimiter of which (a backslash sequence)
      * would begin at buffer position `b`, adding it to the given granum list.
      * Already the text before `b` is known to be well formed for the purpose.
      *
      *     @return The end boundary of the comment appender, or `b` if none was found.
      */
    private int appendAnyCommentAppender( int b, final List<Granum> grana ) {
        if( b < segmentEnd  &&  buffer.get(b) == '\\'  &&
              commentaryHoldDetector.slashStartsDelimiter(b) ) {
            final CommentAppender_ appender = spooler.commentAppender.unwind();
            appender.text.delimit( b, b = compose( appender ));
            grana.add( appender ); }
        return b; }



    /** Parses any sequence of divider drawing characters at buffer position `b`,
      * adding it to the given granum list.
      *
      *     @return The end boundary of the sequence, or `b` if none was found.
      */
    private int appendAnyDrawing( final int b, final CoalescentGranalList grana ) {
        final int c = throughAnyDrawing( b );
        if( c /*moved*/!= b ) grana.appendFlat( b, c );
        return c; }



    /** Parses any foregap at buffer position `b`, adding each of its components
      * to the given granum list.  Already `b` is known to bound either the end
      * of the fractal segment (edge case) or the start of a line within it.
      *
      *     @return The end boundary of the foregap, or `b` if none was found.
      */
    private int appendAnyForegap( int b, final CoalescentGranalList grana ) {
        if( b >= segmentEnd ) return b; /* As required, either `b` bounds the segment end (left),
          or a line start (right). */ assert b == segmentStart || completesNewline(buffer.get(b-1));
        int bLine = b; // The last position at which a line starts.
        int bFlat = b; /* The last potential start position of flat text,
          each character being either a plain space or newline constituent. */

      // Establish the loop invariant
      // ────────────────────────────
        char ch = buffer.get( b );
        if( ch == ' ' ) {
            b = throughAnyS( ++b );
            if( b >= segmentEnd ) {
                grana.appendFlat( bFlat, b );
                return b; }
            ch = buffer.get( b ); }

      // Loop through the foregap form
      // ─────────────────────────────
        boolean endsAtTerm = false; // Set true if a bounding term is discovered.
        for( ;; ) { /* Loop invariant: character `ch` at `b` lies within the fractal segment and is not
              a plain space.  Rather it is a newline constituent or the first character either of the
              lead delimiter of a block construct in the foregap, or of a term just after the foregap. */
            assert b < segmentEnd && ch != ' ';
            if( completesNewline( ch )) {
                ++b; // Past the newline.
                if( b >= segmentEnd ) {
                    grana.appendFlat( bFlat, b );
                    break; }
                bLine = b;
                ch = buffer.get( b );
                if( ch != ' ' ) continue; // Already the invariant is re-established.
                b = throughAnyS( ++b ); } // Re-establishing the invariant, part 1.
            else if( impliesWithoutCompletingNewline( ch )) {
                ch = buffer.get( ++b ); // Toward its completion.
                assert impliesNewline( ch ); // Already `delimitSegment` has tested for this.
                continue; } // Already the invariant is re-established.
            else { // Expect either a block (comment block or indent blind) or a bounding term.
                if( bFlat < bLine ) {
                    grana.appendFlat( bFlat, bLine ); // Flat text that came before the line.
                    bFlat = bLine; }
                final BlockParser parser;
                if( ch == '\\' ) parser = commentBlockParser;
                else if( ch == /*no-break space*/'\u00A0' ) parser = indentBlindParser;
                else {
                    endsAtTerm = true; // Namely a non-backslashed term.
                    break; }
                if( b /*unmoved*/== (b = parser.appendIfDelimiter( b, bLine, grana ))) {
                    endsAtTerm = true; // Namely a backslashed term.
                    break; }
                if( b >= segmentEnd ) break; // This block ends both the foregap and fractal segment.
                bLine = b; // This block has ended with a line break.
                bFlat = b; // Potentially the next run of flat text begins here.
                b = parser.postSpaceEnd; } // Re-establishing the invariant, part 1.

          // re-establish the invariant, part 2
          // ┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈
            if( b >= segmentEnd ) {
                assert bFlat < b;
                grana.appendFlat( bFlat, b );
                break; }
            ch = buffer.get( b ); }
        if( endsAtTerm && bFlat < b ) grana.appendFlat( bFlat, b ); // Flat text that came before.
        return b; }



    /** Parses any newline at buffer position `b`, adding it to the given granum list.
      *
      *     @return The end boundary of the newline, or `b` if none was found.
      */
    private int appendAnyNewline( final int b, final CoalescentGranalList grana ) {
        final int c = throughAnyNewline( b );
        if( b != c ) grana.appendFlat( b, c );
        return c; }



    /** Parses any sequence of newlines at buffer position `b`, adding it to the given granum list.
      *
      *     @return The end boundary of the sequence, or `b` if none was found.
      */
    private int appendAnyNewlines( final int b, final CoalescentGranalList grana ) {
        final int c = throughAnyNewlines( b );
        if( b != c ) grana.appendFlat( b, c );
        return c; }



    /** Parses any postgap at buffer position `b`,
      * adding each of its components to the given granum list.
      *
      *     @return The end boundary of the postgap, or `b` if none was found.
      */
    private int appendAnyP( int b, final CoalescentGranalList grana ) {
        if( b /*moved*/!= (b = appendAnyS( b, grana ))) {
            if( b /*moved*/!= (b = appendAnyCommentAppender( b, grana ))) {
                return appendAnyForegap( b, grana ); }}
        if( b /*moved*/!= (b = appendAnyNewline( b, grana ))) b = appendAnyForegap( b, grana );
        return b; }



    /** Parses at buffer position `b` any regular-expression pattern matcher, adding it to the given
      * granum and pattern-matcher lists.  Alone any delimiter ‘`’ at `b` will commit this method
      * to parsing a matcher in full, failing which it will throw a malformed-text exception.
      *
      *     @return The end boundary of the pattern matcher, or `b` if none was found.
      */
    private int appendAnyPatternMatcher( int b, final List<Granum> grana,
          final List<PatternMatcher_> matchers ) throws MalformedText {
        if( b < segmentEnd && buffer.get(b) == '`' ) {
            final PatternMatcher_ matcher = spooler.patternMatcher.unwind();
            b = appendPatternMatcherAt( b, grana, matcher );
            matchers.add( matcher ); }
        return b; }



    /** Parses any sequence at buffer position `b` of plain space characters,
      * namely ‘S’ in the language definition, adding it to the given granum list.
      *
      *     @return The end boundary of the sequence, or `b` if none was found.
      */
    private int appendAnyS( final int b, final CoalescentGranalList grana ) {
        final int c = throughAnyS( b );
        if( b != c ) grana.appendFlat( b, c );
        return c; }



    /** Parses any term at buffer position `b`, adding it to the given granum list.
      *
      *     @return The end boundary of the term, or `b` if none was found.
      */
    private int appendAnyTerm( final int b, final CoalescentGranalList grana ) {
        final int c = termParser.throughAny( b );
        if( c /*moved*/!= b ) grana.appendFlat( b, c );
        return c; }



    /** Parses a postgap at buffer position `b`, adding each of its components to the given granum list.
      *
      *     @return The end boundary of the postgap.
      *     @throws MalformedText If no postgap occurs at `b`.
      */
    private int appendP( int b, final CoalescentGranalList grana ) throws MalformedText {
        if( b /*moved*/!= (b = appendAnyP( b, grana ))) return b;
        throw new MalformedText( characterPointer(b), "Postgap expected" ); }



    /** Parses at buffer position `b` a regular-expression pattern matcher,
      * adding it to the given granum list.
      *
      *     @param matcher The matcher instance to use for the purpose.
      *     @return The end boundary of the pattern matcher.
      *     @throws MalformedText If no pattern matcher occurs at `b`.
      */
    private int appendPatternMatcher( int b, final List<Granum> grana, final PatternMatcher_ matcher )
          throws MalformedText {
        if( b < segmentEnd && buffer.get(b) == '`' ) return appendPatternMatcherAt( b, grana, matcher );
        throw new MalformedText( characterPointer(b), "Pattern delimiter expected" ); }



    /** Parses at buffer position `b` a regular-expression pattern matcher,
      * adding it to the given granum list.  Already `b` is known to hold the lead delimiter ‘`’.
      *
      *     @param matcher The matcher instance to use for the purpose.
      *     @return The end boundary of the pattern matcher.
      *     @throws MalformedText If no pattern matcher occurs at `b`.
      */
    private int appendPatternMatcherAt( int b, final List<Granum> grana, final PatternMatcher_ matcher )
          throws MalformedText {
        final int bMatcher = b;

      // Left pattern delimiter
      // ──────────────────────
        assert b < segmentEnd && buffer.get(b) == '`';
        matcher.patternDelimiterLeft.text.delimit( b, ++b );

      // Pattern
      // ───────
        pattern: {
            final Pattern pattern = matcher.pattern;
            final CoalescentGranalList cc = pattern.components;
            cc.clear();
            final int bPattern = b;
            boolean inEscape = false; // Whether the last character was a backslash ‘\’.
            while( b < segmentEnd ) {
                final char ch = buffer.get( b );
                if( impliesNewline( ch )) break;

              // Backslashed sequences
              // ─────────────────────
                if( inEscape ) {
                    if( ch == 'b' || ch == 'd' || ch == 'R' ) {
                        final FlatGranum special = spooler.backslashedSpecial.unwind();
                        special.text.delimit( b - 1, ++b ); // Including the prior backslash.
                        cc.add( special ); }
                    else if( ch == 'N' ) {
                        final int bStart = b - 1; // The prior backslash.
                        b = throughBackslashedSpecialNQualifier( ++b );
                        final FlatGranum special = spooler.backslashedSpecial.unwind();
                        special.text.delimit( bStart, b );
                        cc.add( special ); }
                    else if( '0' <= ch && ch <= '9' || isAlphabetic( ch )) {
                        final int a = b - 1;
                        final StringBuilder s = clear( stringBuilder );
                        s.append( "Unsupported backslash sequence: `" ); // Viz. reserved for future use.
                        s.append( buffer, a, b + 1 );
                        s.append( '`' );
                        throw new MalformedText( characterPointer(a), s.toString() ); }
                    else {
                        final FlatGranum literalizer = spooler.literalizer.unwind();
                        literalizer.text.delimit( b - 1, b );
                        cc.add( literalizer );
                        cc.appendFlat( b, ++b ); }
                    inEscape = false;
                    continue; }
                if( ch == '\\' ) {
                    inEscape = true;
                    ++b;
                    continue; }

              // Metacharacters, anchored prefixes and variable interpolators
              // ──────────────
                if( ch == '^' ) {
                    final int bStart = b++;
                    if( b < segmentEnd && completesAnchoredPrefix( buffer.get( b ))) {
                        final FlatGranum prefix = spooler.anchoredPrefix.unwind();
                        prefix.text.delimit( bStart, ++b );
                        cc.add( prefix ); }
                    else {
                        final FlatGranum metacharacter = spooler.metacharacter.unwind();
                        metacharacter.text.delimit( bStart, b );
                        cc.add( metacharacter ); }
                    continue; }
                if( ch == '$' ) {
                    final int bStart = b++;
                    if( b /*moved*/!= (b = throughAnyBracketedVariableName( b ))) {
                        final FlatGranum variable = spooler.variable.unwind();
                        variable.text.delimit( bStart, b );
                        cc.add( variable ); }
                    else {
                        final FlatGranum metacharacter = spooler.metacharacter.unwind();
                        metacharacter.text.delimit( bStart, b );
                        cc.add( metacharacter ); }
                    continue; }
                if( ch == '.' || ch == '|' || ch == '*' || ch == '+' || ch == '?' ) {
                    final FlatGranum metacharacter = spooler.metacharacter.unwind();
                    metacharacter.text.delimit( b, ++b );
                    cc.add( metacharacter );
                    continue; }
                if( ch == '[' || ch == ']' || ch == '{' || ch == '}' ) {
                    throw new MalformedText( characterPointer(b),
                      "Illegal use of a reserved symbol, must be literalized with `\\`" ); }

              // Terminus
              // ────────
                if( ch == '`' ) {
                    if( b == bPattern ) throw new MalformedText( characterPointer(b), "Empty pattern" );
                    pattern.text.delimit( bPattern, b );
                    cc.flush();
                    break pattern; }

              // Group delimiters
              // ────────────────
                if( ch == '(' ) {
                    final int bStart = b++;
                    if( b < segmentEnd && buffer.get(b) == '?' ) {
                        final int c = b + 1;
                        if( c < segmentEnd && buffer.get(c) == ':' ) b = c + 1; }
                    final FlatGranum delimiter = spooler.groupDelimiter.unwind();
                    delimiter.text.delimit( bStart, b );
                    cc.add( delimiter );
                    continue; }
                if( ch == ')' ) {
                    final FlatGranum delimiter = spooler.groupDelimiter.unwind();
                    delimiter.text.delimit( b, ++b );
                    cc.add( delimiter );
                    continue; }

              // Literal characters
              // ──────────────────
                cc.appendFlat( b, ++b ); }
            throw truncatedPattern( characterPointer( b )); }

      // Right pattern delimiter
      // ───────────────────────
        assert buffer.get(b) == '`'; // As per § Terminus, above.
        matcher.patternDelimiterRight.text.delimit( b, ++b );
        final DelimitableGranumList cc = matcher.components;
        assert cc.sizeLimit() == 4; // Accordingly, numeric literals are used below.

      // Match modifiers
      // ───────────────
        if( atMatchModifier( b )) {
            final int a = b;
            do ++b; while( atMatchModifier( b )); // Cf. the various `throughAny` methods.
            final FlatGranum mm = matcher.matchModifiersWhenPresent;
            mm.text.delimit( a, b );
            matcher.matchModifiers = mm;
            cc.end( 4 ); } // Extended to include the modifier series.
        else {
            matcher.matchModifiers = null;
            cc.end( 3 ); } // Retracted to exclude the modifier series.

        matcher.text.delimit( bMatcher, b );
        grana.add( matcher );
        return b; }



    /** Parses a sequence at buffer position `b` of plain space characters,
      * namely ‘S’ in the language definition, adding it to the given granum list.
      *
      *     @return The end boundary of the sequence.
      *     @throws MalformedText If no such sequence occurs at `b`.
      */
    private int appendS( int b, final CoalescentGranalList grana ) throws MalformedText {
        grana.appendFlat( b, b = throughS(b) );
        return b; }



    /** Parses a term at buffer position `b`, adding it to the given granum list.
      *
      *     @return The end boundary of the term.
      *     @throws MalformedText If no term occurs at `b`.
      */
    private int appendTerm( int b, final CoalescentGranalList grana ) throws MalformedText {
        grana.appendFlat( b, b = termParser.through(b) );
        return b; }



    /** @param b A buffer position.
      * @throws MalformedText If neither a matcher modifier nor term boundary occurs at `b`.
      */
    private boolean atMatchModifier( final int b ) throws MalformedText {
        if( b < segmentEnd ) {
            final char ch = buffer.get( b );
            if( termParser.isProper( ch )) {
                if( matchModifiers.indexOf(ch) >= 0 ) return true;
                throw new MalformedText( characterPointer(b), "Unexpected character" ); }}
        return false; }



    /** The source buffer.  Except where an API requires otherwise (e.g. `delimitSegment`), the buffer
      * is maintained at a default position of zero, whence it may be treated whole as a `CharSequence`.
      */
    final CharBuffer buffer = CharBuffer.allocate( bufferCapacity );
 // final CharBuffer buffer = CharBuffer.allocate( bufferCapacity + 1 ) // TEST with a positive
 //   .slice( 1, bufferCapacity );                                     // `arrayOffset`. [BAO]



    /** The capacity of the read buffer in UTF-16 code units.  Parsing text with a fractal head large
      * enough to overflow the buffer will cause an `{@linkplain OverlargeHead OverlargeHead}` exception.
      */
    private static final int bufferCapacity;



    private final Matcher bufferClusterMatcher = graphemeClusterPattern.matcher( buffer );



    /** Returns the columnar offset at the given buffer position, resolving its line
      * within the parsed region of the present fractal head.  If the position lies outside
      * of the parsed region, then the fallbacks of `locateLine` apply.
      *
      *     @see Granum#column()
      *     @param position A buffer position within the parsed region of the present fractal head.
      *     @see LineLocator#locateLine(int)
      */
    final @Subst int bufferColumn( final int position ) {
        lineLocator.locateLine( position );
        return bufferClusterCount( lineLocator.start(), position ); }



    /** Returns the number of grapheme clusters between buffer positions `start` and `end`.
      * Omits any partial cluster at the end of the span.
      *
      *     @see <a href='https://unicode.org/reports/tr29/#Grapheme_Cluster_Boundaries'>
      *       Grapheme cluster boundaries in Unicode text segmentation</a>
      */
    final int bufferClusterCount( final int start, final int end ) {
      return bufferGCC.clusterCount( start, end ); }



    private final GraphemeClusterCounter bufferGCC =  new GraphemeClusterCounter(
      buffer, bufferClusterMatcher );



    /** The threshold in UTF-16 code units of free space required to initiate refill of the buffer
      * without zero-shifting the already-read part of the present segment.  The main purpose is to avoid
      * pointless shifting before the final, empty transfer that signals exhaustion of the text source.
      */
    private static final int bufferHeadRoom; static {
        bufferHeadRoom = 0x2000; // 8192, sufficient for the purpose.
        int n = 0x1_0000; // 65536, minimizing the likelihood of having to throw `OverlargeHead`.
        // Now assume the IO system will transfer that much on each refill request by `delimitSegment`.
        // Let it do so even while the buffer holds the already-read portion of the present segment:
        n += bufferHeadRoom / 2; // 4096, more than ample for that segment.
        bufferCapacity = n; }



    /** Resolves the line number at `position`.  If `position` lies outside of the parsed region
      * of the present fractal head, then the fallbacks of `locateLine` apply.
      *
      *     @param position A buffer position within the parsed region of the present fractal head.
      *     @see LineLocator#locateLine(int)
      */
    final @Subst int bufferLineNumber( final int position ) {
        lineLocator.locateLine( position );
        return lineLocator.number(); }



    private final BulletEndSeeker bulletEndSeeker = new BulletEndSeeker();



    private @Subst CharacterPointer characterPointer() { return characterPointer( buffer.position() ); }



    /** Makes a character pointer to the given buffer position, locating its line within
      * the parsed region of the present fractal head.  If the position lies outside
      * of the parsed region, then the fallbacks of `locateLine` apply.
      *
      *     @param position A buffer position within the parsed region of the present fractal head.
      *     @see LineLocator#locateLine(int) *//*
      *     @paramImplied #stringBuilder2
      */
    @Subst CharacterPointer characterPointer( final int position ) {

      // Locate the line
      // ───────────────
        lineLocator.locateLine( position );
        final int lineStart = lineLocator.start();

      // Resolve its content
      // ───────────────────
        final int lineEnd; { // Or less, if the whole line has yet to enter the buffer.
            final int lineIndex = lineLocator.index();
            if( lineIndex < fractumLineEnds.length ) { // Then delimit the line the easy way:
                lineEnd = fractumLineEnds.array[lineIndex]; }
            else { // The line has yet to be parsed to its end.  Delimit it the hard way:
                final int pN = buffer.limit();
                int p = position;
                while( p < pN && !completesNewline(buffer.get(p++)) );
                lineEnd = p; }}
        final String line = clear(stringBuilder2).append( buffer, lineStart, lineEnd ).toString();

      // Form the pointer
      // ────────────────
        final int column = bufferClusterCount( lineStart, position );
        return new CharacterPointer( line, column, lineLocator.number() ); }



    private @Subst CharacterPointer characterPointerBack() {
        return characterPointer( buffer.position() - 1 ); }



    /** The keywords of command points in lexicographic order as defined by `CharSequence.compare`.
      * A keyword is any term that may appear first in the command.
      *
      *     @see CharSequence#compare(CharSequence,CharSequence)
      *     @see #addCommandPointKeywords(String[],CommandPoint_[])
      */
    protected String[] commandPointKeywords;



    /** Concrete parse states for command points, each at the same index as its corresponding keyword
      * in `commandPointKeywords`.
      */
    protected CommandPoint_<?>[] commandPoints;



    private final CommentaryHoldDetector commentaryHoldDetector = new CommentaryHoldDetector();



    private final BlockParser commentBlockParser = new CommentBlockParser();



    /** Whether character  `ch` is one that formally ends an achored prefix `^*`, `^+` or `^^`.
      * Returns true if `ch` is ‘*’, ‘+’ or ‘^’.
      */
    private static boolean completesAnchoredPrefix( final char ch ) {
        return ch == '*' || ch == '+' || ch == '^'; }



    /** @return The end boundary of the holder, viz. past any terminal newline. *//*
      * @paramImplied #commentaryHoldDetector What detected the hold in the text.
      */
    private int compose( final CommentaryHolder_ holder ) {
        final CommentaryHoldDetector detector = commentaryHoldDetector;
        final DelimitableGranumList cc = holder.components;
        assert cc.sizeLimit() == 5; // Accordingly, numeric literals are used below.

      // `c1_delimiter`
      // ──────────────
        final int delimiterEnd = detector.delimit( holder ); // Its `c1_delimiter.text`, that is.

      // `c2_white`, if any
      // ──────────
        final int whiteEnd = detector.whiteEnd;
        if( whiteEnd == delimiterEnd ) {
            cc.end( 2 ); // The holder ends without `c2_white`.
            holder.c3_commentary = null; // Wherefore it also ends without `c3_commentary`.
            return delimiterEnd; }
        holder.c2_white.text.delimit( delimiterEnd, whiteEnd );

      // `c3_commentary` and `c4_white`, if the former or both
      // ──────────────────────────────
        if( !detector.hasDetectedCommentary ) {
            cc.end( 3 ); // The holder ends without `c3_commentary`.
            holder.c3_commentary = null;
            return whiteEnd; }
        int b = whiteEnd; /* The scan begins at the end boundary of `c2_white`,
          which is the start of the leading term of `c3_commentary`. */
        assert !isPlainWhitespace( buffer.get( b ));
        ++b; // Past the first character of the leading term.
        final int commentaryEnd;
        cc: for( ;; ) {
            term: for(;; ++b ) { // [TEB]
                if( b >= segmentEnd ) {
                    commentaryEnd = b;
                    cc.end( 4 ); // The holder ends without `c4_white`.
                    break cc; }
                final char ch = buffer.get( b );
                if( ch == ' ' || impliesWithoutCompletingNewline(ch) ) break term;
                if( completesNewline( ch )) {
                    cc.end( 5 ); // The holder ends with a newline.
                    holder.c4_white.text.delimit( commentaryEnd = b, ++/*past the newline*/b );
                    break cc; }}
            final int bWhite = b++; // Where the latest plain whitespace starts.
            white: for(;; ++b ) {
                if( b >= segmentEnd ) {
                    cc.end( 5 ); // The holder ends with a plain space.
                    holder.c4_white.text.delimit( commentaryEnd = bWhite, segmentEnd );
                    break cc; }
                final char ch = buffer.get( b );
                if( completesNewline( ch )) {
                    cc.end( 5 ); // The holder ends with a newline.
                    holder.c4_white.text.delimit( commentaryEnd = bWhite, ++/*past the newline*/b );
                    break cc; }
                if( !( ch == ' ' || impliesWithoutCompletingNewline(ch) )) break white; }}
        holder.c3_commentaryWhenPresent.text.delimit( whiteEnd, commentaryEnd );
        holder.c3_commentary = holder.c3_commentaryWhenPresent;
        return b; }



    /** @see Afterlinker_#compose()
      */
    final void composeAfterlinker() throws MalformedText {
        final Afterlinker_ linker = afterlinker;
        assert !linker.isComposed;
        final DelimitableCharSequence keyword = linker.keyword;
        final CoalescentGranalList dcc = linker.descriptor.components;
        dcc.clear();
        int b;
        final int bCommand = keyword.start();
        dcc.appendFlat( linker.bullet.text.end(), b = bCommand );

      // Command
      // ───────
        dcc.add( linker.command ); /* Added early (before parsing it) because the parse might overshoot
          the command into a subsequent postgap, prematurely adding it to `dcc`. */
        final CoalescentGranalList cc = linker.command.components;
        cc.clear();
        final int bReferentialCommand;
        final DelimitableCharSequence referentialCommandKeyword;
        if( equalInContent( "re", keyword )) {

          // Subject clause, from keyword
          // ───────────────
            final var cS = linker.subjectClauseWhenPresent;
            final CoalescentGranalList cScc = cS.components;
            cScc.clear();
            cScc.appendFlat( b, b = keyword.end() );
            b = appendP( b, cScc );
            b = appendPatternMatcher( b, cScc, cS.patternMatcher );
            cS.text.delimit( bCommand, b );
            cScc.flush();
            cc.add( linker.subjectClause = cS );
            b = appendP( b, cc );

          // Referential command, from scratch
          // ───────────────────
            bReferentialCommand = b;
            b = termParser.through( b );
            xSeq.delimit( bReferentialCommand, b );
            referentialCommandKeyword = xSeq; }
        else { // A subject clause is absent.
            linker.subjectClause = null;

          // Referential command, from keyword
          // ───────────────────
            bReferentialCommand = keyword.start();
            b = keyword.end();
            referentialCommandKeyword = keyword; }
        if( equalInContent( "see", referentialCommandKeyword )) {
            final int c;
            int d = b;
            if( d /*moved*/!= (d = c = throughAnyS( d ))
             && d /*moved*/!= (d = termParser.throughAny( d ))) {
                xSeq.delimit( c, d );
                if( equalInContent("also",xSeq) || equalInContent("e.g.",xSeq) ) b = d; }}
        else if( equalInContent( "cf.", referentialCommandKeyword )) {
            final int c;
            int d = b;
            if( d /*moved*/!= (d = c = throughAnyS( d ))
             && d /*moved*/!= (d = termParser.throughAny( d ))) {
                xSeq.delimit( c, d );
                if( equalInContent( "e.g.", xSeq )) b = d; }}
        else trap: {
            if( !equalInContent( "re", referentialCommandKeyword )) {
                final int k = binarySearch( commandPointKeywords, referentialCommandKeyword,
                  CharSequence::compare );
                if( k >= 0 && commandPoints[k] == basicAfterlinker ) break trap; }
            throw new MalformedText( characterPointer(bReferentialCommand),
              "Unrecognized referential command" ); }
        linker.referentialCommand.text.delimit( bReferentialCommand, b );
        cc.add( linker.referentialCommand );

        linker.objectClause = null; // Till proven otherwise.
        boolean toParseAppendage = false; /* Whether a postgap following the command has been added
          to `dcc`, but no parse of a subsequent appendage clause has yet been attempted. */
        final AppendageParserC pAC = appendageParserCReset();
        b = pAC.appendP_AnyClause( b, /*outer*/dcc, /*inner*/cc, linker );
        cO: if( !pAC.wasAppended  &&  b < segmentEnd ) {

          // Object clause
          // ───────────────
            final var cO = linker.objectClauseWhenPresent;
            final var cOLParser = objectClauseLocantParser;
            final int bStart = b;
            b = cOLParser.appendAny( b, cO.fractalContextLocantWhenPresent );
            if( b /*moved*/!= bStart ) {   // Then a fractal context locant is present
                cO.fractumLocant = null; // instead of a fractum locant.
                cO.fractalContextLocant = cO.fractalContextLocantWhenPresent;
                cO.components = cO.componentsAsFractalContextLocant; }
            else { // A fractum locant is present instead of a fractal context locant.
                cO.fractalContextLocant = null;
                b = cOLParser.append( b, cO.fractumLocantWhenPresent,
                  /*failureMessage*/null/*none ∵ the foregoing guarantees at least a term*/ );
                cO.fractumLocant = cO.fractumLocantWhenPresent;
                cO.components = cO.componentsAsFractumLocant; }
            cO.text.delimit( bStart, cOLParser.bEnd );
            cc.add( linker.objectClause = cO );
            if( cOLParser.wasAnyPostgapParsed ) {
                final GranalArrayList cOIcc = cOLParser.components;
                final int cTermEnd = cOLParser.cTermEnd;
                final int cN = cOIcc.size();
                if( cTermEnd < cN ) { /* Then components of a final postgap were inadvertently
                      appended to `cOIcc`.  Move them to `dcc`, where they belong:  [AMP] */
                    int c = cTermEnd;
                    do dcc.add( cOIcc.get( c++ )); while( c < cN );
                    cOIcc.removeRange( cTermEnd, cN ); /* With this, `cOIcc` would be broken
                      by any further coalescence.  But none will occur ∵ it is now complete. */
                    toParseAppendage = true; }}
            else if( b /*moved*/!= (b = appendAnyP( b, dcc ))) toParseAppendage = true; }
        linker.command.text.delimit( bCommand, b );
        cc.flush();
        if( toParseAppendage ) b = pAC.appendAny( b, dcc, linker );
        if( b < segmentEnd ) throw unexpectedTerm( characterPointer( b ));
        assert b == segmentEnd: parseEndsWithSegment(b);
        dcc.flush(); }



    /** @see NonCommandPoint#compose()
      */
    final void composeDescriptor( final NonCommandPoint p ) throws MalformedText {
        assert !p.isComposed;
        final CoalescentGranalList cc = p.descriptor.components;
        cc.clear();
        int b = p.bullet.text.end();
        assert segmentEnd > b;
        cc: {
            if( buffer.get(b) == '\u00A0' ) { // A single no-break space may bound the bullet.
                cc.appendFlat( b, ++b );
                if( b >= segmentEnd ) break cc; } // It may also comprise the whole descriptor.
            if( b /*unmoved*/== (b = appendAnyP( b, cc ))) {
                throw new IllegalParseState( characterPointer(b), "Postgap expected" ); }
                // Because no alternative is possible if `reifyPoint` has done its job.
            while( b /*moved*/!= (b = appendAnyTerm( b, cc ))
                && b /*moved*/!= (b = appendAnyP( b, cc ))); }
        assert b == segmentEnd: parseEndsWithSegment(b);
        cc.flush(); }



    /** @see PlainCommandPoint#compose()
      */
    final void composeDescriptor( final PlainCommandPoint_ p ) throws MalformedText {
        // This method parses as such neither the command nor any appendage clause,
        // as they would be difficult to parse without knowing the command form.
        assert !p.isComposed;
        final CoalescentGranalList cc = p.descriptor.components;
        cc.clear();
        int b;
        cc.appendFlat( p.bullet.text.end(), b = p.keyword.end() );
        while( b /*moved*/!= (b = appendAnyP( b, cc ))
            && b /*moved*/!= (b = appendAnyTerm( b, cc )));
        assert b == segmentEnd: parseEndsWithSegment(b);
        cc.flush();
        assert p.appendageClause == null; } // Never parsed, as explained above.



    /** @see SimpleCommandPoint#compose()
      */
    final void composeDescriptor( final SimpleCommandPoint<?> p ) throws MalformedText {
        assert !p.isComposed;
        final CoalescentGranalList cc = p.descriptor.components;
        cc.clear();
        final DelimitableCharSequence keyword = p.keyword;
        cc.appendFlat( p.bullet.text.end(), keyword.start() );

      // Command
      // ───────
        assert p.command.components.isEmpty(); // The command is simple, its text being identical to
        assert p.command.text == keyword;     // the keyword already delimited by `reifyCommandPoint`.
        cc.add( p.command );
        int b = appendAnyP( keyword.end(), cc );

      // Appendage clause
      // ────────────────
        b = appendageParser.appendAny( b, cc, p );
        assert b == segmentEnd: parseEndsWithSegment(b);
        cc.flush(); }



    /** @see Divider_#areSegmentsComposed
      */
    final void composeDividerSegments() {
        final Division_ div = basicDivision;
        assert !div.areSegmentsComposed;
        final Division_.DividerSegmentList segments = div.components;
        final int sN = segments.size();
        int s = 0;
        final int segmentEndWas = segmentEnd; // End of present fractal segment.
        try { // With a shifting value of `segmentEnd`, for sake of calls herein to parsing methods.
            do {
                final DividerSegment_ seg = segments.get( s );
                int b = seg.text.start();
                seg.perfectIndent.text.delimit( b, b += seg.indentWidth );
                final CoalescentGranalList cc = seg.components;
                cc.clear();

              // Perfect indent
              // ──────────────
                cc.add( seg.perfectIndent );
                segmentEnd = seg.text.end(); // Setting temporarily to the end of this divider `seg`.

              // Divider drawing sequence (initial)
              // ────────────────────────
                cc.appendFlat( b, b = throughAnyDrawing( ++b/*after the initial drawing character*/ ));
                boolean lastWasDrawing = true;
                for( ;; ) {
                    for( ;; ) {
                        if( b /*unmoved*/== (b = appendAnyP( b, cc ))) break;
                        lastWasDrawing = false;
                        if( b /*unmoved*/== (b = appendAnyDrawing( b, cc ))) break;
                        lastWasDrawing = true; }
                    if( b >= segmentEnd ) break;

                  // Division label
                  // ──────────────
                    final int bLabel = b;
                    if( lastWasDrawing ) {
                         b = labelTermParser.throughAnyContiguous( b ); }
                    else b = labelTermParser.throughAny( b );
                    int bLabelEnd = b;
                    if( bLabelEnd == bLabel ) { // This should have been impossible, given the foregoing.
                        throw new IllegalParseState( characterPointer(b), "Division label expected" ); }
                    boolean spacedAppenderFollows = false; /* Whether the label is directly followed
                      by space, which in turn is followed by comment appender. */
                    for( ;; ) {
                        final int bSpace = b;
                        if( b /*unmoved*/== (b = throughAnyS( b ))) break;
                        if( b >= segmentEnd ) break;
                        if( buffer.get(b) == '\\' && commentaryHoldDetector.slashStartsDelimiter(b) ) {
                            b = bSpace;
                            spacedAppenderFollows = true;
                            break; }
                        if( b /*unmoved*/== (b = labelTermParser.throughAny( b ))) break;
                        bLabelEnd = b; }
                    final FlatGranum label = spooler.divisionLabel.unwind();
                    label.text.delimit( bLabel, bLabelEnd );
                    cc.add( label );
                    if( spacedAppenderFollows ) continue; /* Rather than trouble to append it (with any
                      ensuing foregap) and then recover the loop invariant, simply let it be redetected
                      and appended by `appendAnyP` atop the loop. */
                    if( b > bLabelEnd ) cc.appendFlat( bLabelEnd, b ); // Trailing plain whitespace.
                    if( b >= segmentEnd ) break;

                  // Divider drawing sequence, if any (continued atop the loop)
                  // ────────────────────────
                    if( b /*moved*/!= (b = appendAnyDrawing( b, cc ))) lastWasDrawing = true; }
                assert b == segmentEnd: parseEndsWithSegment(b);
                cc.flush(); } while( ++s < sN );
            assert segmentEnd == segmentEndWas; } // The segments end with the present fractal segment.
        finally { segmentEnd = segmentEndWas; }} // Restore the original value regardless.



    /** @see FileFractum_#isComposed
      */
    final void composeFileFractum() {
        final FileFractum_ f = fileFractum;
        assert !f.isComposed;
        final CoalescentGranalList cc = f.componentsWhenPresent;
        cc.clear();
        int b = 0;
        assert b == fractumStart && segmentEnd > b;
        if( b /*unmoved*/== (b = appendAnyForegap( b, cc ))) {
            throw new IllegalParseState( characterPointer(b), "Foregap expected" ); }
            // Because no alternative is possible if `delimitSegment` has done its job.
        while( b /*moved*/!= (b = appendAnyTerm( b, cc ))
            && b /*moved*/!= (b = appendAnyP( b, cc )));
        assert b == segmentEnd: parseEndsWithSegment(b);
        cc.flush();
        f.components = cc; }



    /** @see NoteCarrier_#compose()
      */
    final void composeNoteCarrier() throws MalformedText {
        final NoteCarrier_ nC = noteCarrier;
        assert !nC.isComposed;
        final DelimitableCharSequence keyword = nC.keyword;
        final CoalescentGranalList dcc = nC.descriptor.components;
        dcc.clear();
        int b;
        final int bCommand = keyword.start();
        dcc.appendFlat( nC.bullet.text.end(), b = bCommand );

      // Command
      // ───────
        dcc.add( nC.command ); /* Added early (before parsing it) because the parse that follows
          will overshoot the command into any subsequent postgap, prematurely adding it to `dcc`. */
        final CoalescentGranalList cc = nC.command.components;
        cc.clear();
        int bCommandEnd;
        if( equalInContent( "note", keyword )) { // Then no pertainment clause is present, only a label.

          // Label
          // ─────
            nC.patternMatcher = null;
            nC.labelWhenPresent.text.delimit( b, b = keyword.end() );
            cc.add( nC.labelWhenPresent );
            bCommandEnd = b;
            if( b /*moved*/!= (b = appendAnyP( b, dcc ))) {
                b = appendageParser.appendAny( b, dcc, nC ); }}
        else { // A pertainment clause is present and possibly a label, too.

          // Pertainment clause, comprising a preposition and a pattern matcher
          // ──────────────────
            nC.prepositionWhenPresent.text.delimit( b, b = keyword.end() );
            cc.add( nC.prepositionWhenPresent );
            b = appendP( b, cc );
            b = appendPatternMatcher( b, cc, nC.patternMatcher = nC.patternMatcherWhenPresent );
            bCommandEnd = b; // Pending proof to the contrary.
            final AppendageParserC pAC = appendageParserCReset();
            if( b /*moved*/!= (b = pAC.appendAnyP_AnyClause( b, /*outer*/dcc, /*inner*/cc, nC ))
              &&  !pAC.wasAppended  &&  b < segmentEnd ) {

              // Label, too
              // ─────
                final DelimitableCharSequence text = nC.labelWhenPresent.text;
                text.delimit( b, b = termParser.through(b) );
                if( !equalInContent( "note", text )) {
                    throw new MalformedText( characterPointer( text.start() ),
                      "Unrecognized label in note carrier, expecting ‘note’" ); }
                cc.add( nC.labelWhenPresent );
                bCommandEnd = b;
                if( b /*moved*/!= (b = appendAnyP( b, dcc ))) b = pAC.appendAny( b, dcc, nC ); }}
        nC.command.text.delimit( bCommand, bCommandEnd );
        cc.flush();
        if( b < segmentEnd ) throw unexpectedTerm( characterPointer( b ));
        assert b == segmentEnd: parseEndsWithSegment(b);
        dcc.flush(); }



    /** Reads through any fractal segment located at `segmentStart`, beginning at the present buffer
      * position, and sets the remainder of its determining bounds.  Ensure before calling this method
      * that the following are updated.<ul>
      *
      *     <li>`{@linkplain #fractumStart       fractumStart}`</li>
      *     <li>`{@linkplain #fractumIndentWidth fractumIndentWidth}`</li>
      *     <li>`{@linkplain #fractumLineCounter fractumLineCounter}`</li>
      *     <li>`{@linkplain #segmentStart       segmentStart}`</li></ul>
      *
      * <p>Also ensure that:</p><ul>
      *
      *     <li>`{@linkplain #fractumLineEnds fractumLineEnds}` is empty in the case of a segment
      *         that begins a new fractum, and</li>
      *     <li>the buffer is positioned at the `{@linkplain #segmentEndIndicant segmentEndIndicant}`
      *         of the preceding segment, or at zero in case of a new text source.</li></ul>
      *
      * <p>This method updates the following.</p><ul>
      *
      *     <li>`{@linkplain #fractumLineEnds        fractumLineEnds}`</li>
      *     <li>`{@linkplain #segmentEnd             segmentEnd}`</li>
      *     <li>`{@linkplain #segmentEndIndicant     segmentEndIndicant}`</li>
      *     <li>`{@linkplain #segmentEndIndicantChar segmentEndIndicantChar}`</li></ul>
      *
      * <p>And, in case of a buffer shift:</p><ul>
      *
      *     <li>`{@linkplain #xunc} xunc`</li></ul>
      *
      * <p>Moreover if the shift happens while delimiting a divider segment, then for each segment `s`
      *  of `{@linkplain #basicDivision basicDivision}.components`, this method updates:</p><ul>
      *
      *     <li>`{@linkplain DividerSegment s}.text.start`</li>
      *     <li>`{@linkplain DividerSegment s}.text.end`</li></ul>
      *
      * <p>Always the first call to this method for a new source of text will determine the bounds
      * of the file head.  For a headless file, the first call returns with `segmentEnd` equal
      * to `segmentStart`, so treating the non-existent head as though it were a segment of zero extent.
      * All other calls result in bounds of positive extent.</p>
      *
      * <p>This method may shift the contents of the buffer, rendering invalid all buffer offsets
      * save those recorded in the fields named above.</p>
      *
      *     @throws ForbiddenWhitespace For any forbidden whitespace detected from the initial
      *       buffer position through the newly determined `segmentEndIndicant`.
      *     @throws MalformedText For any misplaced no-break space that occurs from the initial buffer
      *       position through the newly determined `segmentEndIndicant`, except on the first line of
      *       a point, where instead `{@linkplain #reifyPoint(int) reifyPoint}` polices this offence.
      *     @throws MalformedText For any malformed line break that occurs from the initial
      *       buffer position through the newly determined `segmentEndIndicant`.
      */
    private void delimitSegment() throws ParseError {
        assert segmentStart != fractumStart || fractumLineEnds.isEmpty();
        final boolean isFileHead = fractumIndentWidth < 0;
        assert buffer.position() == (isFileHead ? 0 : segmentEndIndicant);
        int lineStart = segmentStart; // [ABP]
        assert lineStart == 0 || completesNewline(buffer.get(lineStart-1)); /* Either the preceding text
          is unreachable (does not exist, or lies outside the buffer) or it comprises a newline. */
        boolean inMargin = isFileHead; /* True while blindly scanning a left margin, where the next
          `get` might yield either an indent space or the indented, initial character of the line. */
        int indentAccumulator = 0; // What reveals the end boundary of the segment.
        boolean inPerfectlyIndentedBackslashes = false;
        boolean inIndentedBackslashes = false;
        boolean inCommentBlock = false; // Scanning where `gets` may yield block commentary.
        for( char ch = '\u0000', chLast = '\u0000';; chLast = ch ) { // Scan character by character:

          // ════════════════════════
          // Keep the buffer supplied
          // ════════════════════════

            if( !buffer.hasRemaining() ) { // Redundant only on the first pass with a new `sourceReader`.

              // Prepare buffer for refill
              // ──────────────
                final int capacity = buffer.capacity();
                if( fractumStart == 0 ) { // Then maybe the last fill was partial and capacity remains.
                    if( buffer.limit() == capacity ) throw new OverlargeHead( fractumLineNumber() );
                    buffer.limit( capacity ); } // Ready to refill.
                else if( capacity - buffer.limit() >= bufferHeadRoom ) {
                    buffer.limit( capacity ); } // Ready to refill.
                else { // Shift out predecessor text, freeing buffer space:
                    final int shift = fractumStart; // To put the (partly read) present fractum at zero.
                    buffer.position( shift ).compact(); // Shifted and limit extended, ready to refill.
                    xunc += shift;
                    fractumStart = 0; // Or `fractumStart -= shift`, so adjust the other variables:
                    segmentStart -= shift;
                    if( isDividerDrawing( segmentEndIndicantChar )) { // Then in a division.
                        final var segments = basicDivision.components;
                        for( int s = segments.size() - 1; s >= 0; --s ) {
                            final var sText = segments.get(s).text;
                            sText.project( sText.start() - shift, sText.length() ); }}
                    final int[] endsArray = fractumLineEnds.array;
                    for( int e = fractumLineEnds.length - 1; e >= 0; --e ) { endsArray[e] -= shift; }
                    lineStart -= shift; }

              // Refill buffer, or detect exhaustion of the text source
              // ─────────────
                assert buffer.hasRemaining(); // Not yet full, that is.
                buffer.mark();
                final int count; {
                    try { count = transferDirectly( sourceReader, buffer ); }
                    catch( IOException x ) { throw new Unhandled( x ); }}
                final int p = buffer.position();
                buffer.limit( p ).reset(); // Whether to resume scanning, or regardless for consistency.
                if( count < 0 ) { // Then the text source is exhausted.
                    if( impliesWithoutCompletingNewline( ch )) { // So ends with e.g. a carriage return.
                        throw truncatedNewline( characterPointer(), ch ); }
                    segmentEnd = segmentEndIndicant = p;
                    segmentEndIndicantChar = '\u0000';
                    fractumLineEnds.add( segmentEnd ); /* The end of the final line.  All lines end with
                      a newline (and so were counted already) except the final line, which never does. */
                    break; } // Segment end boundary = end of text source.
                if( count == 0 ) throw new IllegalStateException(); }
                  // Undefined in the `Reader` API, given the buffer `hasRemaining` space.


          // ═════════════════════════════
          // Scan forward by one character, seeking the end boundary of the segment
          // ═════════════════════════════

            ch = buffer.get();

          // Detect any line break
          // ─────────────────────
            if( completesNewline( ch )) { // Then record the fact:
                lineStart = buffer.position();
                fractumLineEnds.add( lineStart );
                inMargin = true;
                indentAccumulator = 0; // Thus far.
                inPerfectlyIndentedBackslashes = inIndentedBackslashes = false;
                inCommentBlock = false;
                continue; }
            if( impliesWithoutCompletingNewline( ch )) continue; // To its completion.
            if( impliesWithoutCompletingNewline( chLast )) { // Then its completion has failed.
                throw truncatedNewline( characterPointerBack(), chLast ); }

          // Or forbidden whitespace
          // ───────────────────────
            if( ch != ' ' && yetIsGenerallyWhitespace(ch) ) {
                  // A partial test, limited to Unicode plane zero, pending a cause to suffer
                  // the added complexity and potential speed drag of testing full code points.
                throw new ForbiddenWhitespace( characterPointerBack(), ch ); }

          // Or the end boundary
          // ───────────────────
            if( inMargin ) { // Then detect any perfect indent that marks the end boundary:
                if( ch == ' ' ) {
                    ++indentAccumulator;
                    continue; } // To any indented, initial character of the line.
                if( ch != /*no-break space*/'\u00A0' ) { // Viz. not an indent blind.
                    if( indentAccumulator % 4 == /*perfect*/0 ) {
                        if( ch != '\\' ) {
                            segmentEnd = lineStart;
                            segmentEndIndicant = lineStart + indentAccumulator;
                            assert segmentEndIndicant == buffer.position() - 1; // Where `ch` is.
                            segmentEndIndicantChar = ch; // Segment end boundary = either a divider,
                            break; }                    // or a point with a non-backslashed bullet.
                        inPerfectlyIndentedBackslashes = inIndentedBackslashes = true; } /* Indicating
                          either a comment-block delimiter, or the beginning of a backslashed bullet. */
                    else if( ch == '\\' ) inIndentedBackslashes = true; } // Indicating the beginning of
                inMargin = false; }                                       // a comment-block delimiter.
            else if( inPerfectlyIndentedBackslashes ) {
                if( ch == '\\' ) continue; // To the end of the backslash sequence.
                if( ch != ' ' ) {
                    segmentEnd = lineStart;
                    segmentEndIndicant = lineStart + indentAccumulator;
                    segmentEndIndicantChar = buffer.get( segmentEndIndicant );
                    break; } // Segment end boundary = point with a backslashed bullet.
                inPerfectlyIndentedBackslashes = inIndentedBackslashes = false;
                inCommentBlock = true; }

          // Or a misplaced no-break space
          // ─────────────────────────────
            else if( inIndentedBackslashes ) {
                if( ch == '\\' ) continue; // To the end of the backslash sequence.
                if( ch == ' ' ) inCommentBlock = true;
                else if( ch == '\u00A0' ) { // A no-break space.
                    assert isFileHead || !fractumLineEnds.isEmpty(); /* The sequence of backslashes
                      lies either in the file head or a line after the first line of the segment.
                      Nowhere else could imperfectly indented backslashes occur.  So either way, this
                      no-break space lies outside of the first line of a point where `reifyPoint`
                      does the policing.  No need ∴ to guard against trespassing on its jurisdiction. */
                    throw misplacedNoBreakSpace( characterPointerBack() ); }
                inIndentedBackslashes = false; }
            else if( ch == '\u00A0' ) { // A no-break space not `inMargin` ∴ delimiting no indent blind.
                if( inCommentBlock ) continue;
                if( !isFileHead && !isDividerDrawing(segmentEndIndicantChar) // In a point head,
                 && fractumLineEnds.isEmpty() ) {                           // on the first line.
                    continue; } // Leaving the first line of this point to be policed by `reifyPoint`.
                throw misplacedNoBreakSpace( characterPointerBack() ); }}}



    /** Ensures this cursor is rendered unusable for the present text source,
      * e.g. owing to an irrecoverable parse error.
      */
    private void disable() {
        if( state != null && state.isFinal() ) return; // Already this cursor is effectively unusable.
        basicHalt.commit(); }



    /** The offset from the start of the present fractum to its first non-space character.  This is -4 in
      * the case of the file fractum, a multiple of four (including zero) in the case of body fracta.
      */
    @Subst int fractumIndentWidth;



    /** The number of lines before the present fractum.
      */
    private @Subst int fractumLineCounter;



    /** The end boundaries of the lines of the present fractal head, each a buffer position.
      *
      *     @see Java.TextLineLocator#endsRegional
      */
    @Subst final IntArrayExtensor fractumLineEnds = new IntArrayExtensor( new int[0x100] ); // = 256
      // Each an adjustable buffer position. [ABP]



    /** The ordinal number of the first line of the present fractum.
      * Lines are numbered beginning at one.
      */
    final @Subst int fractumLineNumber() { return fractumLineCounter + 1; }



    /** The start position in the buffer of the present fractum, if any,
      * which is the position of its first character.
      */
    private @Subst int fractumStart; // [ABP]



    /** A record in list form of the present fractum’s indent and line of descent.
      * The indent (as measured in spatial tetrads) it records by way of list size:
      * `hierarchy.size - 1 == fractumIndentWidth / 4`.  Descent it records by list entries
      * each at an index equal to the descendant’s indent in spatial tetrads beginning with
      * the top descendant (least descended) and ending with the present fractum (most)
      * at index `hierarchy.size - 1`.  Unoccupied indents it records as null entries.
      *
      *     @see #fractumIndentWidth
      */
    final @Subst ArrayList<Hierarch> hierarchy = new ArrayList<>();



    private final BlockParser indentBlindParser = new IndentBlindParser();



    private final LabelTermParser labelTermParser = new LabelTermParser();



    private final LineLocator lineLocator = new LineLocator();



    /** A list of the recognized modifiers for regular-expression pattern matching.  Parser extensions
      * may modify this list at any time prior to parsing.
      */
    protected String matchModifiers = "isp";



    private void _next() throws ParseError { /* Below and above, in the left margin,
          an empty comment marks each point of commitment to a new parse state. */
        assert !state.isFinal();
        if( segmentEnd == buffer.limit() ) { // Then no fracta remain.
            while( fractumIndentWidth >= 0 ) { // Unwind and recursively end any final body fractum.
                fractumIndentWidth -= 4;
                final Hierarch past = hierarchy.remove( hierarchy.size() - 1 );
                if( past != null ) {
 /**/               past.pendingEnd.commit();
                    sourceSpooler.hierarch.rewind( past );
                    return; }}
 /**/       basicFileFractum.end.commit();
            rest();
            return; }
        final int nextIndentWidth = segmentEndIndicant - segmentEnd; /* The offset from the start of
          the next fractum (`segmentEnd`) to its first non-space character (`segmentEndIndicant`). */
        assert nextIndentWidth >= 0 && nextIndentWidth % 4 == 0; /* The start of a body fractum —
          more specifically of a point head or divider segment — indicated by a perfect indent. */
        if( !state.isInitial() ) { // Then unwind and recursively end any previous sibling.
            while( fractumIndentWidth >= nextIndentWidth ) { /* For its own purposes, this loop maintains
                  the records of `fractumIndentWidth` and `hierarchy` even through the ending states of
                  a previous sibling, during which they are meaningless for their intended purposes. */
                fractumIndentWidth -= 4;
                final Hierarch past = hierarchy.remove( hierarchy.size() - 1 );
                if( past != null ) {
 /**/               past.pendingEnd.commit();
                    sourceSpooler.hierarch.rewind( past );
                    return; }}}

        // Changing what follows?  Sync → `_source`.
        spooler.rewind();
        fractumStart = segmentEnd; // It starts at the end boundary of the present segment.
        fractumIndentWidth = nextIndentWidth;
        fractumLineCounter += fractumLineEnds.length; /* Its line number is the line number
          of the present fractum plus the *line count* of the present fractal head. */
        fractumLineEnds.clear();
        if( isDividerDrawing( segmentEndIndicantChar )) { /* Then next is a divider segment,
              starting a division whose head comprises all contiguous divider segments. */
            int segmentIndentWidth = nextIndentWidth;
            basicDivision.components.clear();
            for( ;; ) { // Scan through and ready each divider segment.
                nextSegment();
                buffer.rewind(); // Concordant with `buffer` contract.
                readyDividerSegment( segmentIndentWidth );
                if( !isDividerDrawing( segmentEndIndicantChar )) break;
                segmentIndentWidth = segmentEndIndicant - segmentEnd; /* Same as
                  the calculation of `nextIndentWidth`, described further above. */
                assert segmentIndentWidth >= 0 && segmentIndentWidth % 4 == 0; } // Perfectly indented.
            readyDivision();
 /**/       basicDivision.commit(); }
        else { // Next is a point.
            nextSegment(); // Scan through to the end boundary of its head.
            buffer.rewind(); // Concordant with `buffer` contract.
 /**/       reifyPoint().commit(); }
        final int i = fractumIndentWidth / 4; // Indent in spatial tetrads, that is.
        while( hierarchy.size() < i ) hierarchy.add( null ); // Padding for unoccupied ancestral indents.
        hierarchy.add( sourceSpooler.hierarch.unwind().set() ); }



    private void nextSegment() throws ParseError {
        buffer.position( segmentEndIndicant );

        // Changing what follows?  Sync → `_source`.
        segmentStart = segmentEnd;
        delimitSegment(); }



    private String parseEndsWithSegment( final int b ) {
        return "Parse ends with the segment\n" + characterPointer(b).markedLine(); }



    /** Readies and adds a divider segment to `basicDivision`.  Ensure before calling this method
      * that all cursor fields other than `hierarchy` and `state` are initialized.
      *
      *     @see Division_.DividerSegment_#indentWidth
      */
    private void readyDividerSegment( final int indentWidth ) {
        final DividerSegment_ segment = basicDivision.components.add();
        segment.text.delimit( segmentStart, segmentEnd ); /* Position nothing else in advance of a
          potential `delimitSegment` which, in the event of a buffer shift, will *shift* nothing else. */
        segment.indentWidth = indentWidth; } /* Meantime record this for use in delimiting
          the perfect indent later, when its final position is known. */



    /** Readies `basicDivision` to be committed as the present parse state.
      * Ensure before calling this method that all other cursor fields are initialized save `hierarchy`.
      */
    private void readyDivision() {
        basicDivision.text.delimit( fractumStart, segmentEnd ); } // Proper to fracta.



    /** Readies `basicFileFractum` to be committed as the present parse state.
      * Ensure before calling this method that all other cursor fields are initialized save `hierarchy`.
      */
    private void readyFileFractum() {
        assert fractumLineNumber() == 1; // Concordant with `FileFractum.lineNumber`.
        final FileFractum_ f = basicFileFractum;
        assert fractumStart == 0;
        f.text.delimit( 0, segmentEnd ); } // Proper to fracta.



    private final ObjectClauseLocantParser objectClauseLocantParser
      = new ObjectClauseLocantParser();



    /** Parses enough of a command point to learn its concrete type and return its parse state
      * ready to commit.  This method is a subroutine of `reifyPoint`.
      *
      *     @param bullet The buffer position of the bullet.
      *     @param bulletEnd The buffer position just after the bullet, viz. its end boundary.
      *       Already it is known (and asserted) to hold a plain space character.
      */
    private CommandPoint_<?> reifyCommandPoint( final int bullet, final int bulletEnd )
          throws MalformedText {
        int b = bulletEnd + 1; // Past the known space character.
        b = throughAnyS( b ); // Past any others.
        xSeq.delimit( b, b = termParser.through(b) );
        final DelimitableCharSequence privately;
        final DelimitableCharSequence keyword;
        if( equalInContent( "privately", xSeq )) {
            privately = xSeq;
            b = throughS( b );
            ySeq.delimit( b, b = termParser.through(b) );
            keyword = ySeq; }
        else {
            privately = null;
            keyword = xSeq; }

      // Resolve the concrete parse state
      // ────────────────────────────────
        b = binarySearch( commandPointKeywords, keyword, CharSequence::compare );
        final CommandPoint_<?> p = b >= 0 ? commandPoints[b] : basicPlainCommandPoint;

      // Delimit therein the components already parsed, for they are proper to all types of command point
      // ──────────────────────────────
        p.text.delimit(                    fractumStart,      segmentEnd ); // Proper to fracta.
        p.perfectIndent.text.delimit( /*0*/fractumStart, /*1*/bullet );    // Proper to body fracta.
        p.bullet       .text.delimit( /*1*/bullet,       /*2*/bulletEnd );
        p.descriptor() .text.delimit( /*2*/bulletEnd,    /*3*/segmentEnd );
        if( privately != null ) {
            p.modifiers.add( "privately" );
            xuncPrivatized.add( p.xunc() ); }
        else p.modifiers.clear();
        p.keyword.delimitAs( keyword );

      // Ready to commit
      // ───────────────
        return p; }



    /** Parses enough of a point other than a command point to learn its concrete type and return
      * its parse state ready to commit.  This method is a subroutine of `reifyPoint`.
      *
      *     @param bullet The buffer position of the bullet.
      *     @param bulletEnd The buffer position just after the bullet, viz. its end boundary.
      *     @param mightBeSpecial Whether the point might be special: an alarm, aside or task point.
      */
    private NonCommandPoint reifyNonCommandPoint( final int bullet, final int bulletEnd,
          final boolean mightBeSpecial ) {

      // Resolve the concrete parse state
      // ────────────────────────────────
        final NonCommandPoint p;
        if( mightBeSpecial ) {
            final char chLast = buffer.get( bulletEnd - 1 );
            if( chLast == '+' ) p = basicTaskPoint;
            else if( bulletEnd - bullet/*length*/ == 1 ) {
                if( chLast == '/' ) p = basicAsidePoint;
                else p = basicPlainPoint; }
            else if( chLast == '!' && buffer.get(bulletEnd-2) == '!' ) p = basicAlarmPoint;
            else p = basicPlainPoint; }
        else p = basicPlainPoint;

      // Therein delimit the components proper to all types of non-command point, and already parsed
      // ──────────────────────────────
        final DelimitableGranumList cc = p.components;
        assert cc.sizeLimit() == 3; // Accordingly, numeric literals are used below.
        p              .text.delimit(      fractumStart,      segmentEnd ); // Proper to fracta.
        p.perfectIndent.text.delimit( /*0*/fractumStart, /*1*/bullet );    // Proper to body fracta.
        p.bullet       .text.delimit( /*1*/bullet,       /*2*/bulletEnd );
        if( bulletEnd < segmentEnd ) {
            final var d = p.descriptorWhenPresent;
            d          .text.delimit( /*2*/bulletEnd,    /*3*/segmentEnd );
            cc.end( 3 ); // Extended to include the descriptor.
            p.descriptor = d; }
        else { // A descriptorless point at file end.
            assert bulletEnd == segmentEnd;
            cc.end( 2 ); // Retracted to exclude the descriptor.
            p.descriptor = null; }

      // Ready to commit
      // ───────────────
        return p; }



    /** Parses enough of a point to learn its concrete type and return its parse state ready to commit.
      * Ensure before calling this method that all other cursor fields are initialized save `hierarchy`.
      *
      *     @throws MalformedText For any misplaced no-break space occuring on the same line.  Note
      *       that elsewhere `{@linkplain #delimitSegment() delimitSegment}` polices this offence.
      */
    private Point_<?> reifyPoint() throws MalformedText {
        final int bullet = fractumStart + fractumIndentWidth;

      // Find the end boundary of the bullet
      // ─────────────────────
        int b = bullet; // The last parsed position.
        BulletEndSeeker endSeeker = null; // Any that finds the end by a comment appender or line end.
        final int bulletEnd;
        final boolean wasLineEndFound; {
            int chLast = codePointAt( buffer, b );
              // Invariant: always `chLast` holds a non-whitespace character internal to the bullet.
              // Reading by full code point in order accurately to test for alphanumeric characters.
              // Advancing by full cluster in order to apply that test to base characters alone.
            final Matcher mCluster = bufferClusterMatcher.region( b, buffer.limit() );
            for( ;; ) {
                mCluster.find(); // Succeeds, else the following throws `IllegalStateException`.
                b = mCluster.end(); // The cluster-aware equivalent of `b += charCount(chLast)`.
                if( b >= segmentEnd ) {
                    assert b == segmentEnd: "No character straddles the boundary of a fractal segment";
                    wasLineEndFound = true; // Ends at head end.
                    break; }
                int ch = codePointAt( buffer, b );
                if( impliesNewline( ch )) {
                    wasLineEndFound = true; // Ends at line break.
                    break; }
                if( isAlphabetic(chLast) || isDigit(chLast) ) { // Then `chLast` is alphanumeric.
                    if( ch == ' ' ) {
                        final var s = bulletEndSeeker;
                        s.seekFromSpace( b );
                        if( s.wasAppenderFound ) {
                            wasLineEndFound = false; // Ends at comment appender.
                            endSeeker = s;
                            break; }
                        if( s.wasLineEndFound ) {
                            wasLineEndFound = true; // Ends at line break or head end.
                            endSeeker = s;
                            break; }
                        b = s.bNextNonSpace;
                        chLast = codePointAt( buffer, b );
                        continue; }
                    if( ch == '\u00A0' ) throw misplacedNoBreakSpace( characterPointer( b )); }
                else { // `chLast` is non-alphanumeric and (by contract) non-whitespace.
                    if( ch == ' ' ) {
                        wasLineEndFound = false; // Ends at space.
                        break; }
                    if( ch == '\u00A0'/*no-break space*/ ) {
                        final var s = bulletEndSeeker;
                        s.seekFromNoBreakSpace( b );
                        if( s.wasAppenderFound ) {
                            wasLineEndFound = false; // Ends at comment appender.
                            endSeeker = s;
                            break; }
                        if( s.wasLineEndFound ) {
                            wasLineEndFound = true; // Ends at line break or head end.
                            endSeeker = s;
                            break; }
                        b = s.bNextNonSpace;
                        chLast = codePointAt( buffer, b );
                        continue; }}
                chLast = ch; }
            bulletEnd = b; }

      // Police any remainder of the bullet line for misplaced no-break spaces
      // ────────────────────
        if( !wasLineEndFound ) {
            if( endSeeker == null ) {
                assert !impliesNewline( buffer.get( b )); // Not to fall outside the line.
                ++b; } // To the next unparsed position.
            else {
                assert endSeeker.wasAppenderFound;
                b = endSeeker.bDelimiterFullEnd; }
            for(; b < segmentEnd; ++b ) {
                final char ch = buffer.get( b );
                if( impliesNewline( ch )) break;
                if( ch == '\u00A0' ) throw misplacedNoBreakSpace( characterPointer( b )); }}

      // Resolve the concrete parse state
      // ────────────────────────────────
        final boolean mightBeSpecial = endSeeker == null; /* Whether the point might be special (alarm,
          aside, command or task point).  Its bullet would have to end with a non-alphanumeric character
          (‘!’, ‘/’, ‘:’ or ‘+’) and not be directly followed by a no-break space.  With the code above,
          this combination occurs only with `endSeeker == null`, which test suffices to guard against
          the presence of a no-break space after a bullet of any form.  So assert: */
        assert !(mightBeSpecial && bulletEnd < segmentEnd && buffer.get(bulletEnd) == '\u00A0');
        final Point_<?> p;
        if( mightBeSpecial  &&  bulletEnd - bullet/*length*/ == 1  &&  buffer.get(bullet) == ':' ) {
            if( wasLineEndFound ) throw termExpected( characterPointer( b ));
              // The bullet ends directly at the line end, with no intervening command.
            assert buffer.get(bulletEnd) == ' '; // The only remaining case.
            p = reifyCommandPoint( bullet, bulletEnd ); }
        else p = reifyNonCommandPoint( bullet, bulletEnd, mightBeSpecial );

      // Ready to commit
      // ───────────────
        return p; }



    /** The recognized qualifiers of file locants.  Parser extensions may modify this list
      * at any time prior to parsing.
      */
    protected final ArrayList<String> fileLocantQualifiers =     // A list as opposed to a set
      new ArrayList<>( fileLocantQualifiers_initialCapacity ); { // for sake of fast iteration.
        fileLocantQualifiers.add( "non-fractal" ); }



    final static int fileLocantQualifiers_initialCapacity = 4;



    private final void rest() { sort( xuncPrivatized.array, 0, xuncPrivatized.length ); } // As per API.



    /** The end boundary in the buffer of the present fractal segment, which is the position
      * after its final character.  This is zero in case of an empty text source
      * or headless file fractum, the only cases of a zero length fractal segment.
      * If the value here is the buffer limit, then no segment remains in the text source.
      */
    @Subst int segmentEnd;



    /** The buffer position of the first non-space character of the present fractal segment’s
      * linear-order successor, or the buffer limit failing a successor.
      */
    private @Subst int segmentEndIndicant;



    /** The character at `segmentEndIndicant`, or the null character (00) if there is none.
      */
    private char segmentEndIndicantChar;



    /** The start position in the buffer of the present fractal segment, if any,
      * which is the position of its first character.
      */
    private @Subst int segmentStart; // [ABP]



    private void _source( final Reader r ) throws ParseError {
        sourceReader = r;
        sourceSpooler.rewind();
        xuncPrivatized.clear();
        xunc = 0;
        final int count; {
            try { count = transferDirectly( sourceReader, buffer.clear() ); }
            catch( IOException x ) { throw new Unhandled( x ); }}
        if( count < 0 ) {
            buffer.limit( 0 );
 /**/       basicEmpty.commit();
            rest();
            return; }
        if( count == 0 ) throw new IllegalStateException(); // Forbidden by `Reader` for array reads.
        buffer.flip();

        // Changing what follows?  Sync → `_next`.
        spooler.rewind();
        fractumStart = 0;
        fractumIndentWidth = -4;
        fractumLineCounter = 0;
        fractumLineEnds.clear(); {
            // Changing this part of it?  Sync → `nextSegment`.
            segmentStart = segmentEnd = segmentEndIndicant = 0;
            delimitSegment(); }
        buffer.rewind(); // Concordant with `buffer` contract.
        readyFileFractum();
 /**/   basicFileFractum.commit();
        hierarchy.clear(); }



    private Reader sourceReader;



    private final SourceSpooler sourceSpooler = new SourceSpooler( this );



    final FractalSpooler spooler = new FractalSpooler( this );



    private ParseState state;



    private final StringBuilder stringBuilder = new StringBuilder( /*initial capacity*/bufferHeadRoom );



    private final StringBuilder stringBuilder2 = new StringBuilder( /*initial capacity*/bufferHeadRoom );



    private final TermParser termParser = new TermParser();



    /** Scans through any bracketed NAME of a ‘${NAME}’ variable interpolator at buffer position `b`,
      * beginning with the ‘{’ delimitier.  Already the character before `b` is known to be `$`.
      *
      *     @return The end boundary of the variable interpolator, or `b` if none was found.
      */
    private int throughAnyBracketedVariableName( final int b ) {
        v: if( b < segmentEnd && buffer.get(b) == '{' ) {
            int v = b + 1;
            for( final int nameStart = v;; ++v ) {
                if( v >= segmentEnd ) break v;
                final char ch = buffer.get( v );
                if( ch == '\\'  || ch == '`' || impliesNewline(ch) ) break v;
                if( ch == '}' ) {
                    if( v > nameStart ) return v + 1;
                    break v; }}}
        return b; }



    /** Scans through any sequence of divider drawing characters at buffer position `b`.
      *
      *     @return The end boundary of the sequence, or `b` if none was found.
      */
    private int throughAnyDrawing( int b ) {
        while( b < segmentEnd && isDividerDrawing(buffer.get(b)) ) ++b;
        return b; }



    /** Scans through any newline at buffer position `b`.
      *
      *     @return The end boundary of the newline, or `b` if none was found.
      */
    private int throughAnyNewline( int b ) {
        for(; b < segmentEnd; ++b ) {
            final char ch = buffer.get( b );
            if( completesNewline( ch )) {
                ++b; // Past the newline.
                break; }
            if( !impliesWithoutCompletingNewline( ch )) break; }
        return b; }



    /** Scans through any sequence of newlines at buffer position `b`.
      *
      *     @return The end boundary of the sequence, or `b` if none was found.
      */
    private int throughAnyNewlines( int b ) {
        while( b < segmentEnd && impliesNewline(buffer.get(b)) ) ++b; /* This implies a sequence of
          well-formed newlines only because already `delimitSegment` has tested for malformed ones. */
        return b; }



    /** Scans through any sequence at buffer position `b` of plain space characters,
      * namely ‘S’ in the language definition.
      *
      *     @return The end boundary of the sequence, or `b` if none was found.
      */
    private int throughAnyS( int b ) {
        while( b < segmentEnd && buffer.get(b) == ' ' ) ++b;
        return b; }



    /** Scans through the bracketed qualifier of a ‘\N{⋯}’ sequence at buffer position `b`,
      * beginning with the ‘{’ delimitier.
      *
      *     @see <a href='https://perldoc.perl.org/perlrebackslash#Named-or-numbered-characters-and-character-sequences'>
      *       Named or numbered characters</a>
      *     @return The end boundary of the qualifier, after the terminal ‘}’.
      *     @throws MalformedText If no such qualifier occurs at `b`.
      */
    private int throughBackslashedSpecialNQualifier( int b ) throws MalformedText {
        if( b < segmentEnd && buffer.get(b) == '{' ) ++b;
        else throw new MalformedText( characterPointer(b), "Curly bracket ‘{’ expected" );
        final int bContent = b; // Subsequent to the opening ‘{’ delimiter.
        boolean inNumeric = false; // Whether the content begins ‘U+’, denoting a numbered qualifier.
        for( char ch = '\u0000', chLast = '\u0000';  b < segmentEnd;  chLast = ch, ++b ) {
            ch = buffer.get( b );
            if( impliesNewline( ch )) break;
            if( ch == '}' ) {
                if( inNumeric ) {
                    if( b - bContent < 3 ) {
                        throw new MalformedText(
                          characterPointer(b), "Hexadecimal digit expected" ); }}
                else if( b == bContent ) {
                    throw new MalformedText( characterPointer(b), "Empty qualifier" ); }
                return ++b; }
            if( ch == '+'  &&  chLast == 'U'  &&  b - bContent == 1 ) inNumeric = true;
            else if( inNumeric ) {
                if( !( '0' <= ch && ch <= '9' || 'A' <= ch && ch <= 'F' || 'a' <= ch && ch <= 'f' )) {
                    throw new MalformedText( characterPointer(b), "Hexadecimal digit expected" ); }}
            else if( !( 'A' <= ch && ch <= 'Z'
              || b > bContent && ( '0' <= ch && ch <= '9' || ch == ' ' || ch == '-' ))) {
                // See Names § 4.8, `https://www.unicode.org/versions/Unicode13.0.0/ch04.pdf`
                throw new MalformedText( characterPointer(b),
                  "Character not allowed here, Unicode " + (int)ch ); }}
        throw truncatedPattern( characterPointer( b )); }



    /** Scans through a sequence at buffer position `b` of plain space characters,
      * namely ‘S’ in the language definition.
      *
      *     @return The end boundary of the sequence.
      *     @throws MalformedText If no such sequence occurs at `b`.
      */
    private int throughS( int b ) throws MalformedText {
        if( b /*moved*/!= (b = throughAnyS( b ))) return b;
        throw spaceExpected( characterPointer( b )); }



    private final DelimitableCharSequence xSeq = newDelimitableCharSequence( buffer );
      // A shared, reusable instance.



    /** The offset of the read buffer from the start of the text source, in UTF-16 code units.
      * This tells how far the buffer has been shifted by `delimitSegment`.
      *
      *     @see Granum#xunc()
      *     @see #buffer
      */
    int xunc;



    /** Extensible array of xunc offsets of directly privatized fracta.  That of any file fractum
      * is entered as -1.  The array may contain duplicate entries.  On any final parse state
      * other than `Halt`, the array is sorted in ascending order.
      */
    final IntArrayExtensor xuncPrivatized = new IntArrayExtensor( new int[0x400] ); // = 1024



    private final DelimitableCharSequence ySeq = newDelimitableCharSequence( buffer );
      // Shared reusable instance



   // ┈┈┈  s t a t e   t y p i n g  ┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈


    final void afterlinker( Afterlinker_ r ) { afterlinker = r; }


        private Afterlinker_ afterlinker;


        private final Afterlinker_ basicAfterlinker = new Afterlinker_( this ).endSet(); // [CIC]



    final void afterlinkerEnd( Afterlinker.End e ) { afterlinkerEnd = e; }


        private Afterlinker.End afterlinkerEnd;



    final void alarmPoint( AlarmPoint p ) { alarmPoint = p; }


        private AlarmPoint alarmPoint;


        private final AlarmPoint_ basicAlarmPoint = new AlarmPoint_( this ).endSet(); // [CIC]



    final void alarmPointEnd( AlarmPoint.End e ) { alarmPointEnd = e; }


        private AlarmPoint.End alarmPointEnd;



    final void asidePoint( AsidePoint p ) { asidePoint = p; }


        private AsidePoint asidePoint;


        private final AsidePoint_ basicAsidePoint = new AsidePoint_( this ).endSet(); // [CIC]



    final void asidePointEnd( AsidePoint.End e ) { asidePointEnd = e; }


        private AsidePoint.End asidePointEnd;



    final void bodyFractum( BodyFractum_<?> f ) { bodyFractum = f; }


        private BodyFractum_<?> bodyFractum;



    final void bodyFractumEnd( BodyFractum.End e ) { bodyFractumEnd = e; }


        private BodyFractum.End bodyFractumEnd;



    final void commandPoint( CommandPoint_<?> p ) { commandPoint = p; }


        private CommandPoint_<?> commandPoint;



    final void commandPointEnd( CommandPoint.End e ) { commandPointEnd = e; }


        private CommandPoint.End commandPointEnd;



    final void division( Division_ d ) { division = d; }


        private Division_ division;


        private final Division_ basicDivision = new Division_( this ).endSet(); // [CIC]



    final void divisionEnd( Division.End e ) { divisionEnd = e; }


        private Division.End divisionEnd;



    final void empty( Empty e ) { state = empty = e; }


        private Empty empty;


        private final Empty_ basicEmpty = new Empty_( this ); // [CIC]



    final void fileFractum( FileFractum_ f ) { fileFractum = f; }


        private FileFractum_ fileFractum;


        private final FileFractum_ basicFileFractum = new FileFractum_( this ).endSet(); // [CIC]



    final void fileFractumEnd( FileFractum.End e ) { fileFractumEnd = e; }


        private FileFractum.End fileFractumEnd;



    final void fractum( Fractum_<?> f ) { state = fractum = f; }


        private Fractum_<?> fractum;



    final void fractumEnd( Fractum.End e ) { state = fractumEnd = e; }


        private Fractum.End fractumEnd;



    final void halt( Halt e ) { state = halt = e; }


        private Halt halt;


        private final Halt_ basicHalt = new Halt_( this ); // [CIC]



    final void noteCarrier( NoteCarrier_ r ) { noteCarrier = r; }


        private NoteCarrier_ noteCarrier;


        private final NoteCarrier_ basicNoteCarrier = new NoteCarrier_( this ).endSet(); // [CIC]



    final void noteCarrierEnd( NoteCarrier.End e ) { noteCarrierEnd = e; }


        private NoteCarrier.End noteCarrierEnd;



    final void plainCommandPoint( PlainCommandPoint p ) { plainCommandPoint = p; }


        private PlainCommandPoint plainCommandPoint;


        private final PlainCommandPoint_ basicPlainCommandPoint // [CIC]
          = new PlainCommandPoint_( this ).endSet();



    final void plainCommandPointEnd( PlainCommandPoint.End e ) { plainCommandPointEnd = e; }


        private PlainCommandPoint.End plainCommandPointEnd;



    final void plainPoint( PlainPoint p ) { plainPoint = p; }


        private PlainPoint plainPoint;


        private final PlainPoint_ basicPlainPoint = new PlainPoint_( this ).endSet(); // [CIC]



    final void plainPointEnd( PlainPoint.End e ) { plainPointEnd = e; }


        private PlainPoint.End plainPointEnd;



    final void point( Point_<?> p ) { point = p; }


        private Point_<?> point;



    final void pointEnd( Point.End e ) { pointEnd = e; }


        private Point.End pointEnd;



    final void privatizer( Privatizer p ) { privatizer = p; }


        private Privatizer privatizer;


        private final Privatizer_ basicPrivatizer = new Privatizer_( this ).endSet(); // [CIC]



    final void privatizerEnd( Privatizer.End e ) { privatizerEnd = e; }


        private Privatizer.End privatizerEnd;



    final void taskPoint( TaskPoint p ) { taskPoint = p; }


        private TaskPoint taskPoint;


        private final TaskPoint_ basicTaskPoint = new TaskPoint_( this ).endSet(); // [CIC]



    final void taskPointEnd( TaskPoint.End e ) { taskPointEnd = e; }


        private TaskPoint.End taskPointEnd;



   // ▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀


    /** A parser of appendage clauses in command points.
      */
    private class AppendageParser {


        /** @return The end boundary of the appendage clause.
          */
        protected int append( int b, final List<Granum> grana, final CommandPoint_<?> p )
              throws MalformedText {
            final CommandPoint_<?>.AppendageClause_ cA = p.appendageClauseWhenPresent;
            final int a = b;
            cA.delimiter.text.delimit( a, ++b );
            final CoalescentGranalList cc = cA.appendage.components;
            cc.clear();
            b = appendP( b, cc );
            b = appendTerm( b, cc );
            while( b /*moved*/!= (b = appendAnyP( b, cc ))
                && b /*moved*/!= (b = appendAnyTerm( b, cc )));
            cA.appendage.text.delimit( a + 1, b );
            cc.flush();
            cA.text.delimit( a, b );
            grana.add( p.appendageClause = cA );
            return b; }



        /** Parses any appendage clause the delimiter of which (‘:’) would begin at buffer position `b`,
          * adding it to the given granum list and assigning it to `p.appendageClause`.
          * Already the text before `b` is known to be well formed for the purpose.
          *
          * <p>If no appendage clause is found, this method assigns null to `p.appendageClause`.</p>
          *
          *     @return The end boundary of the appendage clause, or `b` if none was found.
          */
        int appendAny( final int b, final List<Granum> grana, final CommandPoint_<?> p )
              throws MalformedText {
            if( isDelimiterAt( b )) return append( b, grana, p );
            p.appendageClause = null;
            return b; }



        protected final boolean isDelimiterAt( final int b ) {
            final int c = b + 1;
            return c < segmentEnd  &&  buffer.get(b) == ':'  &&  isPlainWhitespace(buffer.get(c)); }}



   // ▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀


    private final class AppendageParserC extends AppendageParser {


        protected @Override int append( int b, final List<Granum> grana, final CommandPoint_<?> p )
              throws MalformedText {
            assert !wasAppended;
            b = super.append( b, grana, p );
            wasAppended = true;
            return b; }



        /** {@inheritDoc} <p>This method simply returns `b` if already an appendage clause
          *   `{@linkplain #wasAppended wasAppended}`.</p>
          */
        @Override int appendAny( final int b, final List<Granum> grana, final CommandPoint_<?> p )
              throws MalformedText {
            if( wasAppended ) return b;
            return super.appendAny( b, grana, p ); }



        /** Parses any postgap at buffer position `b`, with or without a succeeding appendage clause,
          * appending the result to one of two given granum lists.  Four normal cases are possible:<ol>
          *
          *     <li>Neither postgap nor appendage clause is present, in which case this method
          *         simply returns `b`.</li>
          *     <li>A non-terminal postgap alone is present, in which case this method equates to
          *         `appendAnyP(b, innerGrana)`.</li>
          *     <li>A terminal postgap alone is present, in which case this method equates to
          *         `appendAnyP(b, outerGrana)`.</li>
          *     <li>Both postgap and appendage clause are present, in which case this method equates to
          *         appendP(b, outerGrana); appendAny(b, outerGrana, p)`.</li></ol>
          *
          *     @param outerGrana The component list of the command-point descriptor.
          *     @param innerGrana The component list of the command in the command-point descriptor.
          *     @return The end boundary of the appended text, or `b` if none was appended.
          *     @throws IllegalStateException If already an appendage clause
          *       `{@linkplain #wasAppended wasAppended}`.
          */
        int appendAnyP_AnyClause( int b, final CoalescentGranalList outerGrana,
              final CoalescentGranalList innerGrana, final CommandPoint_<?> p ) throws MalformedText {
            if( wasAppended ) throw new IllegalStateException( "Appendage clause already appended" );
            cachedGrana.clear();
            if( b /*moved*/!= (b = appendAnyP( b, cachedGrana ))) {
                cachedGrana.flush();
                if( b < segmentEnd ) {
                    if( isDelimiterAt( b )) { // Both a postgap and an appendage clause are present.
                        outerGrana.addAll( cachedGrana );
                        b = append( b, outerGrana, p ); }
                    else innerGrana.addAll( cachedGrana ); } // A non-terminal postgap alone is present.
                else outerGrana.addAll( cachedGrana ); } // A terminal postgap alone is present.
            return b; }



        /** Parses a postgap at buffer position `b`, with or without a succeeding appendage clause,
          * appending the result to one of two given granum lists.  Three normal cases are possible:<ol>
          *
          *     <li>A non-terminal postgap alone is present, in which case this method equates to
          *         `appendAnyP(b, innerGrana)`.</li>
          *     <li>A terminal postgap alone is present, in which case this method equates to
          *         `appendAnyP(b, outerGrana)`.</li>
          *     <li>Both postgap and appendage clause are present, in which case this method equates to
          *         appendP(b, outerGrana); appendAny(b, outerGrana, p)`.</li></ol>
          *
          *     @param outerGrana The component list of the command-point descriptor.
          *     @param innerGrana The component list of the command in the command-point descriptor.
          *     @return The end boundary of the appended text.
          *     @throws IllegalStateException If already an appendage clause
          *       `{@linkplain #wasAppended wasAppended}`.
          *     @throws MalformedText If no postgap occurs at `b`.
          */
        int appendP_AnyClause( final int b, final CoalescentGranalList outerGrana,
              final CoalescentGranalList innerGrana, final CommandPoint_<?> p ) throws MalformedText {
            final int c = appendAnyP_AnyClause( b, outerGrana, innerGrana, p );
            if( c /*unmoved*/== b ) throw new MalformedText( characterPointer(b), "Postgap expected" );
            return c; }



        private final CoalescentGranalList cachedGrana = new GranalArrayList( spooler );



        /** Clears the state variables of this parser to their default values.
          */
        void reset() { wasAppended = false; }



        /** Whether an appendage clause has been appended since the last reset of this parser.
          */
        boolean wasAppended; }



   // ▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀


    private static abstract class BlockParser {


        /** Parses any block, the lead delimiter of which would begin with the known character
          * of buffer position `b`, adding it to the given granum list.  Already the text
          * through `b` is known to be well formed for the purpose.
          *
          *     @param bLine Buffer position of the start of the line wherein `b` lies.
          *     @return The end boundary of the block, or `b` if none was found.
          */
        abstract int appendIfDelimiter( int b, int bLine, List<Granum> grana );



        /** Set if the parse succeeds to the space-extended end boundary of the parsed block.
          * If a sequence of plain space characters (20) succeeds the block, then this records
          * the end boundary of that sequence; otherwise it records the end boundary of the block.
          */
        int postSpaceEnd; }



   // ▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀


    /** A device to detect a comment appender or line end where it forms the end boundary of a bullet.
      */
    private final class BulletEndSeeker {


        /** Set when `wasAppenderFound` to the full end boundary in the buffer of its delimiter.
          * If a plain space character (20) succeeds the delimiting backslash sequence,
          * then the full end boundary is the position subsequent to that space character,
          * otherwise the position subsequent to the backslash sequence.
          */
        int bDelimiterFullEnd;



        /** Either the buffer position of the next non-space character (neither 20 nor A0),
          * or `buffer.limit`.
          */
        int bNextNonSpace;



        /** Detects whether the known no-break space at `b` is followed by a plain space
          * and backslash sequence that delimits a comment appender, recording the result
          * in one or more fields of this seeker.
          *
          *     @param b Buffer position of a (known) no-break space character.
          *     @throws MalformedText On detection of a misplaced no-break space.
          */
        void seekFromNoBreakSpace( int b ) throws MalformedText {
            assert b < segmentEnd;
            if( ++b == segmentEnd ) {
                wasAppenderFound = false;
                wasLineEndFound = true; }
            else {
                final char ch = buffer.charAt( b );
                if( ch == ' ' ) {
                    seekFromSpace( b );
                    if( wasLineEndFound || wasAppenderFound ) return;
                    throw misplacedNoBreakSpace( characterPointer( b - 1 )); }
                if( impliesNewline( ch )) {
                    wasAppenderFound = false;
                    wasLineEndFound = true; }
                else {
                    if( ch == '\u00A0' ) throw misplacedNoBreakSpace( characterPointer( b ));
                    wasAppenderFound = false;
                    wasLineEndFound = false; }}
            bNextNonSpace = b; }



        /** Detects whether the known, plain space beginning at `b` is followed by a backslash sequence
          * that delimits a comment appender, recording the result in one or more fields of this seeker.
          *
          *     @param b Buffer position of a (known) plain space character.
          *     @throws MalformedText On detection of a misplaced no-break space.
          */
        void seekFromSpace( int b ) throws MalformedText {
            assert b < segmentEnd;
            for( ;; ) {
                if( ++b == segmentEnd ) {
                    wasAppenderFound = false;
                    wasLineEndFound = true;
                    break; }
                final char ch = buffer.charAt( b );
                if( ch != ' ' ) {
                    if( ch == '\\' ) {
                        wasAppenderFound = slashStartsDelimiter( b );
                        wasLineEndFound = false; }
                    else if( impliesNewline( ch )) {
                        wasAppenderFound = false;
                        wasLineEndFound = true; }
                    else {
                        if( ch == '\u00A0' ) throw misplacedNoBreakSpace( characterPointer( b ));
                        wasAppenderFound = false;
                        wasLineEndFound = false; }
                    break; }}
            bNextNonSpace = b; }



        /** Tells whether the known backslash at `b` starts the delimiter of a comment appender,
          * and updates `bDelimiterFullEnd` accordingly.  Already the text through `b`
          * is known to be well formed for the purpose.
          *
          *     @param b Buffer position of a (known) backslash character ‘\’.
          */
        private boolean slashStartsDelimiter( int b ) { // Cf. the namesake of `CommentaryHoldDetector`.
            while( ++b < segmentEnd ) {
                final char ch = buffer.charAt( b );
                if( ch != '\\' ) {
                    if( ch == ' ' ) ++b; // Past the space character, concordant with the contract.
                    else if( !impliesNewline( ch )) return false;
                    break; }}
            bDelimiterFullEnd = b;
            return true; }



        /** Whether a comment appender was found.  Never true when `wasLineEndFound`.
          */
        boolean wasAppenderFound;



        /** Whether a line end was encountered.  Never true when `wasAppenderFound`.
          */
        boolean wasLineEndFound; }




   // ▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀


    private final class CommentaryHoldDetector {


        /** Delimits the text of `holder.c1_delimiter` and returns `delimiterEnd`.
          */
        int delimit( final CommentaryHolder_ holder ) {
            holder.c1_delimiter.text.delimit( delimiterStart, delimiterEnd );
            return delimiterEnd; }



        /** Set when `slashStartsDelimiter` to the end boundary in the buffer
          * of the backslash sequence that forms the delimiter.
          */
        private int delimiterEnd;



        int delimiterLength() { return delimiterEnd - delimiterStart; }



        private int delimiterStart;



        /** Set when `slashStartsDelimiter` to tell whether the appender holds commentary.
          */
        boolean hasDetectedCommentary;



        /** Tells whether the known backslash at `b` starts the delimiter of a comment appender,
          * and updates the fields of this detector accordingly.  Already the text through `b`
          * is known to be well formed for the purpose.
          *
          *     @param b Buffer position of a (known) backslash character ‘\’.
          */
        boolean slashStartsDelimiter( int b ) { // Cf. the namesake of `BulletEndSeeker`.
            final int bOriginal = b;
            for( ;; ) {
                if( ++b >= segmentEnd ) {
                    hasDetectedCommentary = false;
                    delimiterEnd = b;
                    break; }
                char ch = buffer.charAt( b );
                if( ch != '\\' ) {
                    final int a = b;
                    if( ch == ' ' ) {
                        b = throughAnyS( ++b );
                        if( b /*moved*/!= (b = throughAnyNewline( b ))) hasDetectedCommentary = false;
                        else hasDetectedCommentary = b < segmentEnd; }
                    else if(( b = throughAnyNewline( b )) /*unmoved*/== a ) return false;
                    else hasDetectedCommentary = false;
                    delimiterEnd = a;
                    break; }}
            delimiterStart = bOriginal;
            whiteEnd = b;
            return true; }



        /** Set when `slashStartsDelimiter` to the end boundary in the buffer of colinear plain whitespace
          * subsequent to `delimiterEnd`, or to `delimiterEnd` if no such whitespace occurs.
          * Here ‘colinear’ implies at most a single terminal newline.
          */
        int whiteEnd; }



   // ▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀


    private final class CommentBlockParser extends BlockParser {


        /** {@inheritDoc} <p>Here ‘lead delimiter’ means a backslash sequence
          * in the first line of the comment block.</p>
          */
        @Override int appendIfDelimiter( int b, int bLine, final List<Granum> parentGrana ) {
            final CommentaryHoldDetector detector = commentaryHoldDetector;
            if( detector.slashStartsDelimiter( b )) {
                // Changing what follows?  Sync → namesake method of `IndentBlindParser`.
                final int bBlock = bLine;
                final CommentBlock_ block = spooler.commentBlock.unwind();
                final var blockGrana = block.components;
                blockGrana.clear();
                for( ;; ) {
                    final var line = spooler.commentBlockLine.unwind();
                    final DelimitableGranumList lineGrana = line.components;

                  // `c0_white`
                  // ──────────
                    if( bLine < b/*delimiter*/ ) {
                        lineGrana.start( 0 ); // The line starts with `c0_white`.
                        line.c0_white.text.delimit( bLine, b ); }
                    else lineGrana.start( 1 ); // It starts with `c1_delimiter`.

                  // `c1_delimiter` through `c4_white`
                  // ─────────────────────────────────
                    line.text.delimit( bLine, b = compose( line ));
                    if( detector.hasDetectedCommentary ) {
                        line.c3_commentaryTagName(
                          detector.delimiterLength() == 1 ? "Commentary" : "Label" ); }
                    blockGrana.add( line );

                  // Toward the next line, if any
                  // ────────────────────
                    final int d = throughAnyS( b ); // To the delimiter of any succeeding block line.
                    if( d < segmentEnd && buffer.get(d) == '\\' && detector.slashStartsDelimiter(d) ) {
                        bLine = b;
                        b = d; }
                    else { // The block has its end boundary at `b`.
                        postSpaceEnd = d;
                        break; }}
                block.text.delimit( bBlock, b );
                parentGrana.add( block ); }
            return b; }}



   // ▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀


    /** An entry in the hierarchy list representing a single body fractum.
      *
      *     @see #hierarchy
      */
    final class Hierarch {


        /** The state to commit when this hierarch is unwound from the hierarchy,
          * so ending the body fractum that it represents.
          */
        private BodyFractum_<?>.End_ pendingEnd;



        /** Sets the fields of this hierarch from the present body fractum and returns the hierarch.
          * This is a convenience method.
          *
          *     @throws IllegalStateException If `state` is not `bodyFractum`.
          */
        private Hierarch set() {
            if( state != bodyFractum ) throw new IllegalStateException();
            pendingEnd = (BodyFractum_<?>.End_)bodyFractum.end;
            xunc = bodyFractum.xunc();
            return this; }



        /** The xunc offset of the body fractum.
          *
          *     @see Granum#xunc()
          */
        int xunc() { return xunc; }



        private int xunc; }



   // ▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀


    private final class IndentBlindParser extends BlockParser {


        /** {@inheritDoc} <p>Here ‘lead delimiter’ means a no-break space in the first line
          * of the indent blind.  Since this much is already given to be well formed,
          * and nothing more is wanted, always a parse will occur.</p>
          *
          *     @return The end boundary of the block subsequent to `b`.
          */
        @Override int appendIfDelimiter( int b, int bLine, final List<Granum> parentGrana ) {
            // Changing what follows?  Sync → namesake method of `CommentBlockParser`.
            final int bBlind = bLine;
            final IndentBlind_ blind = spooler.indentBlind.unwind();
            final var blindGrana = blind.components;
            blindGrana.clear();
            for( ;; ) {
                final var line = spooler.indentBlindLine.unwind();
                final List<Granum> lineGrana = line.components;
                lineGrana.clear();
                Granum_ m;

              // 0. Indent, if any
              // ─────────
                if( bLine < b/*delimiter*/ ) {
                    m = line.indentWhenPresent;
                    m.text.delimit( bLine, b );
                    lineGrana.add( m ); }

              // 1. Delimiter
              // ────────────
                m = line.delimiter;
                m.text.delimit( b, ++b );
                lineGrana.add( m ); // Now what follows, if anything?

              // 2. Substance, if any
              // ────────────
                final int bSubstance = b;
                boolean endsWithAppender = false;
                for( char ch, chLast = /*delimiter*/'\u00A0'; b < segmentEnd; ++b, chLast = ch ) {
                    ch = buffer.get( b );
                    if( completesNewline( ch )) {
                        ++b; // Past the newline.
                        break; }
                    if( ch == '\\' && chLast == ' ' && commentaryHoldDetector.slashStartsDelimiter(b) ) {
                        endsWithAppender = true;
                        break; }}
                if( bSubstance < b ) {
                    m = line.substance = line.substanceWhenPresent;
                    m.text.delimit( bSubstance, b );
                    lineGrana.add( m );

                  // 3. Comment appender, if any
                  // ───────────────────
                    if( endsWithAppender ) {
                        final CommentAppender_ appender = spooler.commentAppender.unwind();
                        appender.text.delimit( b, b = compose( appender ));
                        lineGrana.add( appender ); }}
                else {
                    line.substance = null;
                    assert !endsWithAppender; } // Impossible without substance at least of plain space.

              // This line as a whole
              // ─────────
                line.text.delimit( bLine, b );
                blindGrana.add( line );

              // Toward the next line, if any
              // ────────────────────
                final int d = throughAnyS( b ); // To the delimiter of any subsequent line of the blind.
                if( d < segmentEnd && buffer.get(d) == /*no-break space*/'\u00A0' ) {
                    bLine = b;
                    b = d; }
                else { // The blind has its end boundary at `b`.
                    postSpaceEnd = d;
                    break; }}
            blind.text.delimit( bBlind, b );
            parentGrana.add( blind );
            return b; }}



   // ▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀


    /** A parser of the terms in division labels.
      */
    private final class LabelTermParser extends TermParser {


        /** Scans through any term at buffer position `b`, given that `b` is known to succeed directly
          * a divider drawing character.  If this is *not* known, then ensure the contrary is known
          * and use instead `{@linkplain #throughAny(int) throughAny}`.
          *
          *     @return The end boundary of the term, or `b` if none was found.
          */
        int throughAnyContiguous( int b ) {
            while( b < segmentEnd && isProper(buffer.get(b)) ) ++b; /* Whether the term comprises a
              sequence of backslashes is immaterial, ∵ here it would not form a comment delimiter. */
            return b; }



       // ━━━  T e r m   P a r s e r  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


        protected @Override boolean isProper( final char ch ) {
            return !isWhitespace(ch) && !isDividerDrawing(ch); }



        protected @Override int resultOnBackslashes( final int bOriginal, final int bEnd,
              final char chEnd ) {
            return isDividerDrawing(chEnd) ? bEnd : bOriginal; }



        protected @Override MalformedText termExpected( final int b ) {
            return new MalformedText( characterPointer(b), "Division-label term expected" ); }}



   // ▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀


    private final class LineLocator extends TextLineLocator {


        LineLocator() { super( fractumLineEnds ); }



        /** Locates the line of the text source in which the given buffer position falls.
          *
          *     @param position A buffer position.  Normally it lies in a region of the present fractal
          *       head already parsed by `delimitSegment`.  If rather it lies before `fractumStart`,
          *       then instead this method uses `fractumStart`; or if it lies after the parsed region,
          *       then instead this method uses the last parsed position.
          */
        @Subst void locateLine( int position ) {
          super.locateLine( position, fractumStart, fractumLineNumber() ); }}



   // ▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀


    /** A parser of the locants that populate object clauses, namely fractum locants
      * and fractal context locants.
      */
    private final class ObjectClauseLocantParser {


        /** Parses a fractum locant at buffer position `b`, adding its components to `loF.components`
          * and updating the fields of this parser.
          *
          *     @param failureMessage The failure message to use in the event no fractum locant occurs,
          *       or null if one must occur, in which case an illegal-state exception is thrown instead.
          *     @return The end boundary of the last thing that was parsed (fractum locant
          *       or subsequent postgap).
          *     @throws MalformedText If no fractum locant occurs at `b`.
          */
        int append( int b, final FractumLocant_ loF, final String failureMessage )
              throws MalformedText {
            final int bOriginal = b;
            final GranalArrayList cc = loF.components;
            cc.clear();
            composition: {

              // Pattern-matcher series
              // ──────────────────────
                final List<PatternMatcher_> matchers = loF.patternMatchersWhenPresent;
                matchers.clear();
                while( b /*moved*/!= (b = appendAnyPatternMatcher( b, cc, matchers ))) {
                    cTermEnd = cc.size();
                    b = appendAnyP( bEnd = b, cc );
                    if( b /*unmoved*/== bEnd || b >= segmentEnd || buffer.get(b) != '@' ) {
                        loF.fileLocant = null;          // No file locant is present,
                        loF.patternMatchers = matchers; // only a pattern-matcher series.
                        break composition; }
                    final FlatGranum opC = spooler.contextOperator.unwind();
                    opC.text.delimit( b, ++b );
                    cc.add( opC );          // The context operator ‘@’,
                    b = appendP( b, cc ); } // and its trailing postgap.
                final int nPM = matchers.size();
                loF.patternMatchers = nPM == 0 ? null : matchers;

              // File locant
              // ───────────
                final var loFile = loF.fileLocantWhenPresent;
                if( b /*unmoved*/== (b = appendAny( b, loFile ))) {
                    if( nPM > 0 ) { // Then a context operator and postgap were just parsed.
                        throw new MalformedText( characterPointer(b), "File locant expected" ); }
                    // No fractum locant is present, at all.
                    if( failureMessage == null ) throw new IllegalStateException();
                      // Concordant with contract.
                    throw new MalformedText( characterPointer(b), failureMessage ); }
                cc.add( loF.fileLocant = loFile );

              // Finalization where `loF` ends with a file locant
              // ────────────
                wasAnyPostgapParsed = false;
                loF.text.delimit( bOriginal, bEnd = b );
                cc.flush();
                return b; }

          // Finalization where `loF` comprises a pattern series
          // ────────────
            wasAnyPostgapParsed = true;
            components = cc;
            loF.text.delimit( bOriginal, bEnd ); // `bEnd` not `b`, which bounds instead any postgap.
            cc.flush();
            return b; }



        /** Parses any fractal context locant at buffer position `b`, adding its components
          * to `loFC.components` and updating the fields of this parser.
          *
          *     @return The end boundary of the last thing that was parsed (fractal context locant
          *       or subsequent postgap), or `b` if no fractal context locant is present.
          */
        int appendAny( int b, final Afterlinker_. FractalContextLocant_ loFC ) throws MalformedText {
            if( b < segmentEnd && buffer.get(b) == '@' ) {
                final int bOriginal = b;
                final GranalArrayList cc = loFC.components;
                cc.clear();
                final FlatGranum opC = loFC.contextOperator;
                opC.text.delimit( b, ++b );
                cc.add( opC );              // The context operator ‘@’,
                b = appendP( b, cc ); // and its trailing postgap.
                final var loF = loFC.fractumLocant;
                b = append( b, loF, "Fractum locant expected" ); // Which sets the parser fields.
                cc.add( loF );

              // Finalization
              // ────────────
                wasAnyPostgapParsed = false;
                loFC.text.delimit( bOriginal, b );
                cc.flush(); }
            return b; }



        /** Parses any file locant at buffer position `a`,
          * adding its components to `loFile.components`.
          *
          *     @return The end boundary of the file locant, or `a` if none was found.
          */
        private int appendAny( final int a, final FileLocant_ loFile ) throws MalformedText {
            int d = termParser.throughAny( a ); // End bound of term.
            if( d /*moved*/!= a ) {
                int b = a; // Start bound of term.
                final CoalescentGranalList cc = loFile.components;
                cc.clear();
                loFile.qualifiers.clear();
                qualifiers: for( String qualifier;; ) {
                    xSeq.delimit( b, d );
                    for( int q = fileLocantQualifiers.size();; ) {
                        --q;
                        qualifier = fileLocantQualifiers.get( q );
                        if( equalInContent( xSeq, qualifier )) break;
                        if( q == 0 ) break qualifiers; }
                    loFile.qualifiers.add( qualifier );
                    cc.appendFlat( b, d );
                    b = appendP( d, cc );
                    d = termParser.through( b ); }
                loFile.reference.text.delimit( b, d );
                cc.add( loFile.reference );
                loFile.text.delimit( a, d );
                cc.flush(); }
            return d; }



        /** Set on each successful parse to the end boundary in the buffer of the locant.
          */
        int bEnd;



        /** When `wasAnyPostgapParsed`, this component list contains the final term of the locant.
          *
          *     @see #cTermEnd
          */
        GranalArrayList components;



        /** Set when `wasAnyPostgapParsed` to the end boundary in `components` of the last term
          * that was added (correctly), which is also the start boundary of any subsequent postgap
          * whose components were appended (inadvertently and incorrectly).  The caller must remove
          * any such components to the component list of the point descriptor, where they belong.
          */
        int cTermEnd; // [AMP]



        /** When true, the caller must transfer from `components` any subsequent to `cTermEnd`, as there
          * described.  When false, the caller must parse any postgap subsequent to the object clause.
          *
          *     @see #cTermEnd
          */
        boolean wasAnyPostgapParsed; } // [AMP]



   // ▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀


    /** A warning that the target member is meaningful (fulfils its API description) only for substansive
      * parse states, those which implement `Granum` and therefore model text of non-zero length.
      * These are the parse states of {@linkplain Typestamp Typestamp} category (a).
      */
    private static @Documented @Retention(SOURCE) @Target({ FIELD, METHOD }) @interface Subst {}



   // ▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀


    /** A parser of terms.  A term (as the language defines it) is a sequence of non-whitespace
      * characters that does not comprise a sequence of backslashes ‘\’.
      */
    private class TermParser {


        /** Whether `ch` is proper to a term.
          */
        protected boolean isProper( final char ch ) { return !isWhitespace( ch ); } // [TEB]



        /** @param bEnd The end boundary of the backslash sequence comprising the would-be term.
          * @param chEnd The character at `bEnd`, or the null character (00) if there is none.
          */
        protected int resultOnBackslashes( final int bOriginal, int bEnd, char chEnd ) {
            return bOriginal; }



        /** @see MalformedText#pointer
          */
        protected MalformedText termExpected( int b ) {
            return MalformedText.termExpected( characterPointer( b )); }



        /** Scans through a term at buffer position `b`.
          *
          *     @return The end boundary of the term.
          *     @throws MalformedText If no term occurs at `b`.
          */
        final int through( int b ) throws MalformedText {
            if( b /*moved*/!= (b = throughAny( b ))) return b;
            throw termExpected( b ); }



        /** Scans through any term at buffer position `b`.
          *
          *     @return The end boundary of the term, or `b` if none was found.
          */
        final int throughAny( int b ) {
            final int bOriginal;
            final char chFirst; {
                if( b >= segmentEnd ) return b;
                chFirst = buffer.get( b );
                if( !isProper( chFirst )) return b;
                bOriginal = b++; }
            if( chFirst == '\\' ) { // Then scan what remains by the exhaustive method. (edge case)
                boolean comprisesBackslashes = true; // Thus far.
                char ch = '\u0000';
                for(; b < segmentEnd; ++b ) {
                    ch = buffer.get( b );
                    if( !isProper( ch )) break;
                    if( ch != '\\' ) comprisesBackslashes = false; }
                if( comprisesBackslashes ) return resultOnBackslashes( bOriginal, b, ch ); }
            else while( b < segmentEnd && isProper(buffer.get(b)) ) ++b; // The easy way. (typical case)
            return b; }}}



// NOTES
// ─────
//   ABP  Adjustable buffer position.  This note serves as a reminder to adjust the value of the variable
//        in `delimitSegment` after each call to `buffer.compact`.
//
//   AMP  Avoiding misplaced postgaps.  The code here entails clean-up of any misplaced postgap,
//        repositioning it after the fact.  It would be better, however, to avoid misplacement
//        in the first place.  See e.g. the solution of `AppendageParserC`.
//
//   BAO  Backing-array offset.  This is non-zero in case of an array-backed buffer formed as a slice
//        of another buffer, but other cases may exist.  https://stackoverflow.com/a/24601336/2402790
//
//   CIC  Cached instance of concrete parse state.  Each instance is held in a constant field named
//        e.g. `basicFoo`, basic meaning unextended.  It could instead be held in `foo`, except then
//        it might be overwritten with an instance of a `Foo` subclass defined by a parser extension,
//        leaving the basic instance unavailable for future reuse.
//
//   TEB  Term-end bounding.  Marking an instance of code that tests for the end boundary of a term.



                                                   // Copyright © 2020-2024  Michael Allan.  Licence MIT.
