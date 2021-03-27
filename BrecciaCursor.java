package Breccia.parser.plain;

import Breccia.parser.*;
import java.io.IOException;
import java.io.Reader;
import java.lang.annotation.*;
import java.nio.CharBuffer;
import java.nio.file.Path;
import Java.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static Java.CharBuffers.newDelimitableCharSequence;
import static Java.CharBuffers.transferDirectly;
import static Java.CharSequences.equalInContent;
import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.SOURCE;
import static java.lang.Character.codePointAt;
import static java.lang.Character.isAlphabetic;
import static java.lang.Character.isDigit;
import static java.util.Arrays.binarySearch;
import static Breccia.parser.plain.Language.*;
import static Breccia.parser.plain.MalformedMarkup.*;
import static Breccia.parser.plain.Project.newSourceReader;


/** A reusable, pull parser of plain Breccia as reflected through a cursor.
  */
public class BrecciaCursor implements ReusableCursor {


    public BrecciaCursor() {
        final String[] commandPointKeywords = { // Those specific to Breccia, in lexicographic order.
            "private" };
        final CommandPoint_<?>[] commandPoints = { // Each at the same index as its keyword above.
            basicPrivatizer }; // ‘private’
        this.commandPointKeywords = commandPointKeywords;
        this.commandPoints = commandPoints; }



   // ━━━  C u r s o r  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    public final @Override @NarrowNot AssociativeReference asAssociativeReference() {
        return state == associativeReference? associativeReference : null; }



    public final @Override @NarrowNot AssociativeReference.End asAssociativeReferenceEnd() {
        return state == associativeReferenceEnd? associativeReferenceEnd : null; }



    public final @Override @NarrowNot BodyFractum asBodyFractum() {
        return state == bodyFractum? bodyFractum : null; }



    public final @Override @NarrowNot BodyFractum.End asBodyFractumEnd() {
        return state == bodyFractumEnd? bodyFractumEnd : null; }



    public final @Override @NarrowNot CommandPoint asCommandPoint() {
        return state == commandPoint? commandPoint : null; }



    public final @Override @NarrowNot CommandPoint.End asCommandPointEnd() {
        return state == commandPointEnd? commandPointEnd : null; }



    public final @Override @NarrowNot Division asDivision() {
        return state == division? division : null; }



    public final @Override @NarrowNot Division.End asDivisionEnd() {
        return state == divisionEnd? divisionEnd : null; }



    /** Returns the present parse state as `Empty`, or null if the markup source is not empty.
      */
    public final @Override @NarrowNot Empty asEmpty() { return state == empty? empty : null; }



    public final @Override @NarrowNot FileFractum asFileFractum() {
        if( state != fileFractum ) return null;
        if( !fileFractum.isCompositionParsed ) {
            parseFileFractum();
            assert fileFractum.isCompositionParsed; }
        return fileFractum; }



    public final @Override @NarrowNot FileFractum.End asFileFractumEnd() {
        return state == fileFractumEnd? fileFractumEnd : null; }



    public final @Override @NarrowNot Fractum asFractum() { return state == fractum? fractum : null; }



    public final @Override @NarrowNot Fractum.End asFractumEnd() {
        return state == fractumEnd? fractumEnd : null; }



    public final @Override @NarrowNot Halt asHalt() { return state == halt? halt : null; }



    public final @Override @NarrowNot PlainCommandPoint asPlainCommandPoint() {
        return state == plainCommandPoint? plainCommandPoint : null; }



    public final @Override @NarrowNot PlainCommandPoint.End asPlainCommandPointEnd() {
        return state == plainCommandPointEnd? plainCommandPointEnd : null; }



    public final @Override @NarrowNot PlainPoint asPlainPoint() {
        return state == plainPoint? plainPoint : null; }



    public final @Override @NarrowNot PlainPoint.End asPlainPointEnd() {
        return state == plainPointEnd? plainPointEnd : null; }



    public final @Override @NarrowNot Point asPoint() { return state == point? point : null; }



    public final @Override @NarrowNot Point.End asPointEnd() {
        return state == pointEnd? pointEnd : null; }



    public final @Override @NarrowNot Privatizer asPrivatizer() {
        return state == privatizer? privatizer : null; }



    public final @Override @NarrowNot Privatizer.End asPrivatizerEnd() {
        return state == privatizerEnd? privatizerEnd : null; }



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


    public final @Override void markupSource( final Reader r ) throws ParseError {
        try { _markupSource( r ); }
        catch( ParseError x ) {
            disable();
            throw x; }}



    public final @Override void perState( final Path sourceFile, final Consumer<ParseState> sink )
          throws ParseError {
        try( final Reader r = newSourceReader​( sourceFile )) {
            markupSource( r );
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
            markupSource( r );
            while( sink.test(state) && !state.isFinal() ) _next(); }
        catch( IOException x ) { throw new Unhandled( x ); }
        catch( ParseError x ) {
            disable();
            throw x; }}



////  P r i v a t e  ////////////////////////////////////////////////////////////////////////////////////


    /** @param aKeywords Additional keywords in lexicographic order
      *   to merge into {@linkplain #commandPointKeywords commandPointKeywords}.
      * @param aPoints The corresponding command points,
      *   ordered as per {@linkplain #commandPointKeywords commandPointKeywords}.
      */
    protected final void addCommandPoints( final String[] aKeywords, final CommandPoint_<?>[] aPoints ) {
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



    /** Appends to the given markup list a component of flat markup comprising the buffered text
      * bounded by the given buffer positions.
      */
    private void append( final int start, final int end, final List<Markup> components ) {
        final FlatMarkup markup = spooler.flatMarkup.unwind();
        markup.text.delimit( start, end );
        components.add( markup ); }



    /** The source buffer.  Except where an API requires otherwise (e.g. `delimitSegment`), the buffer
      * is maintained at a default position of zero, whence it may be treated whole as a `CharSequence`.
      */
    final CharBuffer buffer = CharBuffer.allocate( bufferCapacity );
 // final CharBuffer buffer = CharBuffer.allocate( bufferCapacity + 1 ) // TEST with a positive
 //   .slice( 1, bufferCapacity );                                     // `arrayOffset`. [BAO]



    /** The capacity of the read buffer in 16-bit code units.  Parsing markup with a fractal head large
      * enough to overflow the buffer will cause an `{@linkplain OverlargeHead OverlargeHead}` exception.
      */
    static final int bufferCapacity; static {
        int n = 0x1_0000; // 65536, minimizing the likelihood of having to throw `OverlargeHead`.
        // Now assume the IO system will transfer that much on each refill request by `delimitSegment`.
        // Let it do so even while the buffer holds the already-read portion of the present segment:
        n += 0x1000; // 4096, more than ample for that segment.
        bufferCapacity = n; }



    /** Returns the columnar offset at the given buffer position, resolving its line
      * within the parsed region of the present fractum.  If the position lies outside
      * of the parsed region, then the fallbacks of `resolveLine` apply.
      *
      *     @see Markup#column()
      *     @param position A buffer position within the parsed region of the present fractum.
      *     @see LineResolver#resolveLine(int)
      */
    final @Subst int bufferColumn( final int position ) {
        lineResolver.resolveLine( position );
        return bufferColumnarSpan( lineResolver.start, position ); }



    /** Returns the number of grapheme clusters between buffer positions `start` and `end`.
      *
      *     @see <a href='https://unicode.org/reports/tr29/'>
      *       Grapheme clusters in Unicode text segmentation</a>
      */
    final int bufferColumnarSpan( final int start, final int end ) {
        bufferColumnarSpanSeq.delimit( start, end );
        graphemeClusterMatcher.reset( /*input sequence*/bufferColumnarSpanSeq );
        int count = 0;
        while( graphemeClusterMatcher.find() ) ++count;
        return count; }



    private final DelimitableCharSequence bufferColumnarSpanSeq = newDelimitableCharSequence( buffer );



    /** Resolves the line number at `position`.  If `position` lies outside of the parsed region
      * of the present fractum, then the fallbacks of `resolveLine` apply.
      *
      *     @param position A buffer position within the parsed region of the present fractum.
      *     @see LineResolver#resolveLine(int)
      */
    final @Subst int bufferLineNumber( final int position ) {
        lineResolver.resolveLine( position );
        return lineResolver.number; }




    private @Subst MalformedMarkup.Pointer bufferPointer() { return bufferPointer( buffer.position() ); }



    /** Makes an error pointer to the given buffer position, resolving its line within
      * the parsed region of the present fractum.  If the position lies outside
      * of the parsed region, then the fallbacks of `resolveLine` apply.
      *
      *     @param position A buffer position within the parsed region of the present fractum.
      *     @see LineResolver#resolveLine(int)
      */
    private @Subst MalformedMarkup.Pointer bufferPointer( final int position ) {
        lineResolver.resolveLine( position );
        final int lineStart = lineResolver.start;
        final int lineLength; { // Or partial length, if the whole line has yet to enter the buffer.
            final int lineIndexNext = lineResolver.index + 1;
            if( lineIndexNext < fractumLineEnds.length ) { // Then measure the easy way:
                lineLength = fractumLineEnds.array[lineIndexNext] - lineStart; }
            else { // The line has yet to be parsed to its end.  Measure it the hard way:
                final int pN = buffer.limit();
                int p = position;
                while( p < pN && !completesNewline(buffer.get(p++)) );
                lineLength = p - lineStart; }}
        final String line = buffer.slice( lineStart, lineLength ).toString();
        final int column = bufferColumnarSpan( lineStart, position );
        return new MalformedMarkup.Pointer( lineResolver.number, line, column ); }



    private @Subst MalformedMarkup.Pointer bufferPointerBack() {
        return bufferPointer( buffer.position() - 1 ); }



    private final BulletEndSeeker bulletEndSeeker = new BulletEndSeeker();



    /** The keywords of command points in lexicographic order as defined by `CharSequence.compare`.
      * A keyword is any term that may appear first in the command.
      *
      *     @see CharSequence#compare(CharSequence,CharSequence)
      *     @see #addCommandPoints(String[],CommandPoint_[])
      */
    protected String[] commandPointKeywords;




    /** Concrete parse states for command points, each at the same index as its corresponding keyword
      * in `commandPointKeywords`.
      */
    protected CommandPoint_<?>[] commandPoints;



    private final BlockParser commentBlockParser = new BlockParser() {
        @Override int parseAfterPossibleLeadDelimiterCharacter( int c, final List<Markup> markup ) {
            throw new UnsupportedOperationException(); }};



    /** Reads through any fractal segment located at `segmentStart`, beginning at the present buffer
      * position, and sets the remainder of its determining bounds.  Ensure before calling this method
      * that the following are updated.
      *
      * <ul><li>`{@linkplain #fractumStart       fractumStart}`</li>
      *     <li>`{@linkplain #fractumIndentWidth fractumIndentWidth}`</li>
      *     <li>`{@linkplain #fractumLineCounter fractumLineCounter}`</li>
      *     <li>`{@linkplain #segmentStart       segmentStart}`</li></ul>
      *
      * <p>Also ensure that:</p>
      *
      * <ul><li>`{@linkplain #fractumLineEnds fractumLineEnds}` is empty in the case of a segment
      *         that begins a new fractum, and</li>
      *     <li>the buffer is positioned at the `{@linkplain #segmentEndIndicator segmentEndIndicator}`
      *         of the preceding segment, or at zero in case of a new markup source.</li></ul>
      *
      * <p>This method updates the following.</p>
      *
      * <ul><li>`{@linkplain #fractumLineEnds         fractumLineEnds}`</li>
      *     <li>`{@linkplain #segmentEnd              segmentEnd}`</li>
      *     <li>`{@linkplain #segmentEndIndicator     segmentEndIndicator}`</li>
      *     <li>`{@linkplain #segmentEndIndicatorChar segmentEndIndicatorChar}`</li></ul>
      *
      * <p>Always the first call to this method for a new source of markup will determine the bounds
      * of the file head.  For a headless file, the first call returns with `segmentEnd` equal
      * to `segmentStart`, so treating the non-existent head as though it were a segment of zero extent.
      * All other calls result in bounds of positive extent.</p>
      *
      * <p>This method may shift the contents of the buffer, rendering invalid all buffer offsets
      * save those recorded in the fields named above.</p>
      *
      *     @throws ForbiddenWhitespace For any forbidden whitespace detected from the initial
      *       buffer position through the newly determined `segmentEndIndicator`.
      *     @throws MalformedMarkup For any misplaced no-break space that occurs from the initial buffer
      *       position through the newly determined `segmentEndIndicator`, except on the first line of a
      *       point, where instead `{@linkplain #reifyPoint(int) reifyPoint}` polices this offence.
      *     @throws MalformedMarkup For any malformed line break that occurs from the initial
      *       buffer position through the newly determined `segmentEndIndicator`.
      */
    private void delimitSegment() throws ParseError {
        assert segmentStart != fractumStart || fractumLineEnds.isEmpty();
        final boolean isFileHead = fractumIndentWidth < 0;
        assert buffer.position() == (isFileHead? 0 : segmentEndIndicator);
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

            if( !buffer.hasRemaining() ) { // Redundant only on the first pass with a new `markupSource`.

              // Prepare buffer for refill
              // ──────────────
                if( fractumStart == 0 ) { // Then maybe the last fill was partial and capacity remains.
                    final int capacity = buffer.capacity();
                    if( buffer.limit() == capacity ) throw new OverlargeHead( fractumLineNumber() );
                    buffer.limit( capacity ); } // Ready to refill.
                else { // Shift out predecessor markup, freeing buffer space:
                    final int shift = fractumStart; // To put the (partly read) present fractum at zero.
                    buffer.position( shift ).compact(); // Shifted and limit extended, ready to refill.
                    fractumStart = 0; // Or `fractumStart -= shift`, so adjust the other variables:
                    segmentStart -= shift;
                    final int[] endsArray = fractumLineEnds.array;
                    for( int e = fractumLineEnds.length - 1; e >= 0; --e ) { endsArray[e] -= shift; }
                    lineStart -= shift; }

              // Refill buffer, or detect exhaustion of the markup source
              // ─────────────
                assert buffer.hasRemaining(); // Not yet full, that is.
                buffer.mark();
                final int count; {
                    try { count = transferDirectly( sourceReader, buffer ); }
                    catch( IOException x ) { throw new Unhandled( x ); }}
                final int p = buffer.position();
                buffer.limit( p ).reset(); // Whether to resume scanning, or regardless for consistency.
                if( count < 0 ) { // Then the markup source is exhausted.
                    if( impliesWithoutCompletingNewline( ch )) { // So ends with e.g. a carriage return.
                        throw truncatedNewline( bufferPointer(), ch ); }
                    segmentEnd = segmentEndIndicator = p;
                    segmentEndIndicatorChar = '\u0000';
                    fractumLineEnds.add( segmentEnd ); /* The end of the final line.  All lines end with
                      a newline (and so were counted already) except the final line, which never does. */
                    break; } // Segment end boundary = end of markup source.
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
                throw truncatedNewline( bufferPointerBack(), chLast ); }

          // Or forbidden whitespace
          // ───────────────────────
            if( ch != ' ' && yetIsWhitespace(ch) ) {
                  // A partial test, limited to Unicode plane zero, pending a cause to suffer
                  // the added complexity and potential speed drag of testing full code points.
                throw new ForbiddenWhitespace( bufferPointerBack(), ch ); }

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
                            segmentEndIndicator = lineStart + indentAccumulator;
                            assert segmentEndIndicator == buffer.position() - 1; // Where `ch` is.
                            segmentEndIndicatorChar = ch; // Segment end boundary = either a divider,
                            break; }                     // or a point with a non-backslashed bullet.
                        inPerfectlyIndentedBackslashes = inIndentedBackslashes = true; } /* Indicating
                          either a comment-block delimiter, or the beginning of a backslashed bullet. */
                    else if( ch == '\\' ) inIndentedBackslashes = true; } // Indicating the beginning of
                inMargin = false; }                                       // a comment-block delimiter.
            else if( inPerfectlyIndentedBackslashes ) {
                if( ch == '\\' ) continue; // To the end of the backslash sequence.
                if( ch != ' ' ) {
                    segmentEnd = lineStart;
                    segmentEndIndicator = lineStart + indentAccumulator;
                    segmentEndIndicatorChar = buffer.get( segmentEndIndicator );
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
                    throw misplacedNoBreakSpace( bufferPointerBack() ); }
                inIndentedBackslashes = false; }
            else if( ch == '\u00A0' ) { // A no-break space not `inMargin` ∴ delimiting no indent blind.
                if( inCommentBlock ) continue;
                if( !isFileHead && !isDividerDrawing(segmentEndIndicatorChar) // In a point head,
                 && fractumLineEnds.isEmpty() ) {                                // on the first line.
                    continue; } // Leaving the first line of this point to be policed by `reifyPoint`.
                throw misplacedNoBreakSpace( bufferPointerBack() ); }}}



    /** Ensures this cursor is rendered unusable for the present markup source,
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



    /** The end boundaries of the lines of the present fractum.  Each is recorded as a buffer position,
      * which is either the position of the first character of the succeeding line, or `buffer.limit`
      * in the case of the final line of the markup source.  Invariably each is preceded by a newline.
      */
    private @Subst final IntArrayExtensor fractumLineEnds = new IntArrayExtensor( new int[0x100] );
      // Each an adjustable buffer position. [ABP]



    /** The ordinal number of the first line of the present fractum.
      * Lines are numbered beginning at one.
      */
    final @Subst int fractumLineNumber() { return fractumLineCounter + 1; }



    /** The start position in the buffer of the present fractum, if any,
      * which is the position of its first character.
      */
    private @Subst int fractumStart; // [ABP]



    private final Matcher graphemeClusterMatcher = Pattern.compile( "\\X" ).matcher( "" );
      // The alternative for cluster discovery (within the JDK) is `java.txt.BreakIterator`, but
      // apparently it is outdated in this regard, wheras `java.util.regex` was updated for JDK 15.
      // https://bugs.openjdk.java.net/browse/JDK-8174266
      // https://bugs.openjdk.java.net/browse/JDK-8243579



    /** A record of the present parse state’s indent and fractal ancestry in list form.  It records
      * indent in perfect units by its adjusted size: ``fractumIndentWidth / 4 == hierarchy.size - 1`.
      * It records fractal ancestry by ancestral parse states each at an index equal to its indent in
      * perfect units, beginning with the parse state of the top-level body fractum and ending with
      * that of the present body fractum itself at index `hierarchy.size - 1`.  It records unoccupied
      * indents by padding their corresponding indeces with null parse states.  For parse states other
      * than body fracta, the hierarchy list is always empty.
      *
      * <p>Be careful with the ancestral parse states — all but the final element of the list —
      * as their content is no longer valid at the present cursor position.</p>.
      *
      *     @see #fractumIndentWidth
      */
    private final @Subst ArrayList<BodyFractum_<?>> hierarchy = new ArrayList<>();



    private final BlockParser indentBlindParser = new BlockParser() {
        @Override int parseAfterPossibleLeadDelimiterCharacter( int c, final List<Markup> markup ) {
            throw new UnsupportedOperationException(); }};



    private final LineResolver lineResolver = new LineResolver();



    private void _markupSource( final Reader r ) throws ParseError {
        sourceReader = r;
        final int count; {
            try { count = transferDirectly( sourceReader, buffer.clear() ); }
            catch( IOException x ) { throw new Unhandled( x ); }}
        if( count < 0 ) {
            buffer.limit( 0 );
 /**/       basicEmpty.commit();
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
            segmentStart = segmentEnd = segmentEndIndicator = 0;
            delimitSegment(); }
        buffer.rewind(); // As per `buffer` contract.
 /**/   readyFileFractum().commit();
        hierarchy.clear(); }



    private void _next() throws ParseError { /* Below and above, in the left margin,
          an empty comment marks each point of commitment to a new parse state. */
        assert !state.isFinal();
        if( segmentEnd == buffer.limit() ) { // Then no fracta remain.
            while( fractumIndentWidth >= 0 ) { // Unwind any past body fracta, ending each.
                fractumIndentWidth -= 4;
                final BodyFractum_<?> past = hierarchy.remove( hierarchy.size() - 1 );
                if( past != null ) {
 /**/               past.end.commit();
                    return; }}
 /**/       basicFileFractum.end.commit();
            return; }
        final int nextIndentWidth = segmentEndIndicator - segmentEnd; /* The offset from the start of
          the next fractum (`segmentEnd`) to its first non-space character (`segmentEndIndicator`). */
        assert nextIndentWidth >= 0 && nextIndentWidth % 4 == 0; // A body fractum, perfectly indented.
        if( !state.isInitial() ) { // Then unwind any past siblings from `hierarchy`, ending each.
            while( fractumIndentWidth >= nextIndentWidth ) { /* For its own purposes, this loop maintains
                  the records of `fractumIndentWidth` and `hierarchy` even through the ending states
                  of past siblings, during which they are meaningless for their intended purposes. */
                fractumIndentWidth -= 4;
                final BodyFractum_<?> pastSibling = hierarchy.remove( hierarchy.size() - 1 );
                if( pastSibling != null ) {
 /**/               pastSibling.end.commit();
                    return; }}}

        // Changing what follows?  Sync → `markupSource`.
        spooler.rewind();
        fractumStart = segmentEnd; // It starts at the end boundary of the present segment.
        fractumIndentWidth = nextIndentWidth;
        fractumLineCounter += fractumLineEnds.length; /* Its line number is the line number
          of the present fractum plus the *line count* of the present fractum. */
        fractumLineEnds.clear();
        if( isDividerDrawing( segmentEndIndicatorChar )) { /* Then next is a divider segment,
              starting a division whose head comprises all contiguous divider segments. */
            do nextSegment(); while( isDividerDrawing( segmentEndIndicatorChar )); // Scan through each.
            buffer.rewind(); // As per `buffer` contract.
 /**/       readyDivision().commit(); }
        else { // Next is a point.
            nextSegment(); // Scan through to the end boundary of its head.
            buffer.rewind(); // As per `buffer` contract.
 /**/       reifyPoint().commit(); }
        final int i = fractumIndentWidth / 4; // Indent in perfect units, that is.
        while( hierarchy.size() < i ) hierarchy.add( null ); // Padding for unoccupied ancestral indents.
        assert state == bodyFractum;
        hierarchy.add( bodyFractum ); }



    private void nextSegment() throws ParseError {
        buffer.position( segmentEndIndicator );

        // Changing what follows?  Sync → `markupSource`.
        segmentStart = segmentEnd;
        delimitSegment(); }



    /** Parses any foregap at buffer position `c`,
      * adding each of its components to the given markup list.
      *
      *     @return The end boundary of the gap, or `c` if there is none.
      */
    private int parseAnyForegap( int c, final List<Markup> markup ) {
        if( c >= segmentEnd ) return c;
        int wRun = c; /* The last potential start position of a run of plain whitespace characters,
          each either a plain space or newline constituent. */

      // Establish the loop invariant
      // ────────────────────────────
        char ch = buffer.get( c );
        if( ch == ' ' ) {
            c = throughAnyS( ++c );
            if( c >= segmentEnd ) {
                append( wRun, c, markup );
                return c; }
            ch = buffer.get( c ); }

      // Loop through the foregap form
      // ─────────────────────────────
        for( ;; ) { /* Loop invariant.  Character `ch` at position `c` lies within the fractal segment,
              and is not a plain space.  Rather it is the first character of either a newline or lead
              delimiter of a block construct in the foregap, or of a term just after the foregap. */
            assert c < segmentEnd && ch != ' ';
            if( impliesNewline( ch )) {
                ++c; // Past the newline, or at least one character of it.
                if( c >= segmentEnd ) {
                    append( wRun, c, markup );
                    break; }
                ch = buffer.get( c );
                if( ch != ' ' ) continue; // Already the invariant is re-established.
                c = throughAnyS( ++c ); } // Re-establishing the invariant, part 1.
            else { // Expect a comment block or indent blind, or a term that bounds the foregap.
                if( wRun < c ) append( wRun, c, markup ); // Plain whitespace that came before.
                final BlockParser parser;
                if( ch == '\\' ) parser = commentBlockParser;
                else if( ch == /*no-break space*/'\u00A0' ) parser = indentBlindParser;
                else break; // The foregap ends at a non-backslashed term.
                c = parser.parseAfterPossibleLeadDelimiterCharacter( ++c, markup );
                if( !parser.didParse ) break; // The foregap ends at a backslashed term.
                if( c >= segmentEnd ) break; // This block ends both the foregap and fractal segment.
                wRun = c; // Potentially the next plain whitespace run begins here.
                c = parser.postSpaceEnd; } // Re-establishing the invariant, part 1.

          // re-establish the invariant, part 2
          // ┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈
            if( c >= segmentEnd ) {
                assert wRun < c;
                append( wRun, c, markup );
                break; }
            ch = buffer.get( c ); }
        return c; }



    final void parseFileDescriptor() throws MalformedMarkup {
        final FileFractum_.FileDescriptor_ descriptor = fileFractum.descriptor;
        final List<Markup> cc = descriptor.components;
        cc.clear();
        assert buffer.position() == 0;
        assert fractumStart == 0;
        int c = 0;
        assert segmentEnd > 0;
        c = parseForegap( c, cc );
        for( ;; ) {
            if( c >= segmentEnd ) break;
            c = parseTerms( c, cc );
            if( c >= segmentEnd ) break;
            c = parsePostgap( c, cc ); }
        assert c == segmentEnd;
        descriptor.isCompositionParsed = true; }



    final void parseFileFractum() {
        final FileFractum_ f = fileFractum;
        if( fractumStart == segmentEnd ) {
            f.components = FileFractum_.componentsWhenAbsent;
            f.descriptor = null; }
        else {
            final FileFractum_.FileDescriptor_ d = f.descriptorWhenPresent;
            d.isCompositionParsed = false; // Pending demand.
            // No need to delimit `d.text`, which being identical to `f.text` is already delimited.
            f.components = f.componentsWhenPresent;
            f.descriptor = d; }
        f.isCompositionParsed = true; }



    /** Parses a foregap at buffer position `c`, adding each of its components to the given markup list.
      *
      *     @return The end boundary of the gap.
      *     @throws MalformedMarkup If no foregap occurs at `c`.
      */
    private int parseForegap( int c, final List<Markup> markup ) throws MalformedMarkup {
        if( c /*moved*/!= (c = parseAnyForegap( c, markup ))) return c;
        throw new MalformedMarkup( bufferPointer(c), "Foregap expected" ); }



    private int parsePostgap( int c, final List<Markup> markup ) {
        throw new UnsupportedOperationException(); }



    private int parseTerms( int c, final List<Markup> markup ) {
        throw new UnsupportedOperationException(); }



    /** Readies `basicDivision` to be committed as the present parse state.
      * Ensure before calling this method that all other cursor fields are initialized save `hierarchy`.
      */
    private Division_ readyDivision() {
        basicDivision.text.delimit( fractumStart, segmentEnd ); // Proper to fracta.
        basicDivision.perfectIndent.text.delimit( // Proper to body fracta.
          fractumStart, fractumStart + fractumIndentWidth );
        return basicDivision; }



    /** Readies `basicFileFractum` to be committed as the present parse state.
      * Ensure before calling this method that all other cursor fields are initialized save `hierarchy`.
      */
    private FileFractum_ readyFileFractum() {
        basicFileFractum.text.delimit( fractumStart, segmentEnd ); // Proper to fracta.
        basicFileFractum.isCompositionParsed = false; // Pending demand.
        return basicFileFractum; }



    /** Parses enough of a command point to learn its concrete type and return its parse state
      * ready to commit.  This method is a subroutine of `reifyPoint`.
      *
      *     @param bulletEnd The buffer position just after the bullet, viz. its end boundary.
      *       Already it is known (and asserted) to hold a plain space character. *//*
      *
      *     @uses #xSeq
      */
    private CommandPoint_<?> reifyCommandPoint( final int bulletEnd ) throws MalformedMarkup {
        int c = bulletEnd + 1; // Past the known space character.
        c = throughAnyS( c ); // Past any others.
        xSeq.delimit( c, c = throughTerm(c) );
        final boolean presentsPrivately;
        if( equalInContent( "privately", xSeq )) {
            presentsPrivately = true;
            c = throughS( c );
            xSeq.delimit( c, c = throughTerm(c) ); }
        else presentsPrivately = false;

      // Resolve its concrete parse state
      // ────────────────────────────────
        c = binarySearch( commandPointKeywords, xSeq, CharSequence::compare );
        final CommandPoint_<?> p = c >= 0? commandPoints[c] : basicPlainCommandPoint;

      // Therein delimit the components already parsed above
      // ──────────────────────────────
        if( presentsPrivately ) p.modifierSet.add( CommandPoint.Modifier.privately );
        else p.modifierSet.clear();
        p.keyword.delimitAs( xSeq );
        return p; }



    /** Parses enough of a point to learn its concrete type and return its parse state ready to commit.
      * Ensure before calling this method that all other cursor fields are initialized save `hierarchy`.
      *
      *     @throws MalformedMarkup For any misplaced no-break space occuring on the same line.  Note
      *       that elsewhere `{@linkplain #delimitSegment() delimitSegment}` polices this offence. *//*
      *
      *     @uses #xSeq
      */
    private Point_<?> reifyPoint() throws MalformedMarkup {
        final int bullet = fractumStart + fractumIndentWidth;
        assert buffer.position() == 0;

      // Find the end boundary of the bullet
      // ─────────────────────
        int c = bullet; // The last parsed position.
        BulletEndSeeker endSeeker = null; // Any that finds the end by a comment appender or line end.
        final int bulletEnd;
        final boolean wasLineEndFound; {
            int chLast = codePointAt( buffer, c );
              // Invariant: always `chLast` holds a non-whitespace character internal to the bullet.
              // Reading by full code point in order accurately to test for alphanumeric characters.
              // Advancing by full cluster in order to apply that test to base characters alone.
            final Matcher mCluster = graphemeClusterMatcher.reset( /*input sequence*/buffer )
              .region( c, buffer.limit() );
            for( final int cEnd = segmentEnd;; ) {
                mCluster.find(); // Succeeds, else the following throws `IllegalStateException`.
                c = mCluster.end(); // The cluster-aware equivalent of `c += charCount(chLast)`.
                if( c >= cEnd ) {
                    assert c == cEnd: "No character can straddle the boundary of a fractal segment";
                    wasLineEndFound = true; // Ends at head end.
                    break; }
                int ch = codePointAt( buffer, c );
                if( impliesNewline( ch )) {
                    wasLineEndFound = true; // Ends at line break.
                    break; }
                if( isAlphabetic(chLast) || isDigit(chLast) ) { // Then `chLast` is alphanumeric.
                    if( ch == ' ' ) {
                        final var s = bulletEndSeeker;
                        s.seekFromSpace( c, cEnd );
                        if( s.wasAppenderFound ) {
                            wasLineEndFound = false; // Ends at comment appender.
                            endSeeker = s;
                            break; }
                        if( s.wasLineEndFound ) {
                            wasLineEndFound = true; // Ends at line break or head end.
                            endSeeker = s;
                            break; }
                        c = s.cNextNonSpace;
                        chLast = codePointAt( buffer, c );
                        continue; }
                    if( ch == '\u00A0' ) throw misplacedNoBreakSpace( bufferPointer( c )); }
                else { // `chLast` is non-alphanumeric and (by contract) non-whitespace.
                    if( ch == ' ' ) {
                        wasLineEndFound = false; // Ends at space.
                        break; }
                    if( ch == '\u00A0'/*no-break space*/ ) {
                        final var s = bulletEndSeeker;
                        s.seekFromNoBreakSpace( c, cEnd );
                        if( s.wasAppenderFound ) {
                            wasLineEndFound = false; // Ends at comment appender.
                            endSeeker = s;
                            break; }
                        if( s.wasLineEndFound ) {
                            wasLineEndFound = true; // Ends at line break or head end.
                            endSeeker = s;
                            break; }
                        c = s.cNextNonSpace;
                        chLast = codePointAt( buffer, c );
                        continue; }}
                chLast = ch; }
            bulletEnd = c; }

      // Police any remainder of the bullet line for misplaced no-break spaces
      // ────────────────────
        if( !wasLineEndFound ) {
            if( endSeeker == null ) {
                assert !impliesNewline( buffer.get( c )); // Not to fall outside the line.
                ++c; } // To the next unparsed position.
            else {
                assert endSeeker.wasAppenderFound;
                c = endSeeker.cDelimiterTightEnd; }
            for(; c < segmentEnd; ++c ) {
                final char ch = buffer.get( c );
                if( impliesNewline( ch )) break;
                if( ch == '\u00A0' ) throw misplacedNoBreakSpace( bufferPointer( c )); }}

      // Resolve the concrete parse state
      // ────────────────────────────────
        xSeq.delimit( bullet, bulletEnd );
        final Point_<?> p;
        if( equalInContent( ":", xSeq )) {
            if( endSeeker != null ) { // Then the only case is that of the bullet ending (wrongly for
                assert buffer.get(bulletEnd) == '\u00A0'; // a command point) at a no-break space.
                throw spaceExpected( bufferPointer( bulletEnd )); }
            if( wasLineEndFound ) { // Then the bullet ends directly at the line end, with no
                throw termExpected( bufferPointer( c )); } // command between the two.
            assert buffer.get(bulletEnd) == ' '; // The only remaining case.
            p = reifyCommandPoint( bulletEnd ); }
        else p = basicPlainPoint;

      // Therein delimit the components proper to all types of point, already parsed above
      // ──────────────────────────────
        final var cc = p.components;
        final int ccMax = Point_.componentsMax;
        p.text.delimit(                    fractumStart,      segmentEnd ); // Proper to fracta.
        p.perfectIndent.text.delimit( /*0*/fractumStart, /*1*/bullet );    // Proper to body fracta.
        p.bullet.text.delimit(        /*1*/bullet,       /*2*/bulletEnd );
        if( bulletEnd < segmentEnd ) {
            final var d = p.descriptorWhenPresent;
            d.text.delimit(           /*2*/bulletEnd,    /*3*/segmentEnd );
            if( cc.size() < ccMax ) cc.add( d ); // Ensuring inclusion.
            assert cc.size() == ccMax;
            p.descriptor = d; }
        else { // A descriptorless point at file end.
            assert bulletEnd == segmentEnd;
            if( cc.size() == ccMax ) cc.remove( ccMax - 1 ); // Ensuring exclusion.
            else assert cc.size() == ccMax - 1;
            p.descriptor = null; }

      // Ready to commit
      // ───────────────
        return p; }



    /** The end boundary in the buffer of the present fractal segment, which is the position
      * after its final character.  This is zero in case of an empty markup source
      * or headless file fractum, the only cases of a zero length fractal segment.
      * If the value here is the buffer limit, then no segment remains in the markup source.
      */
    private @Subst int segmentEnd;



    /** The buffer position of the first non-space character of the present fractal segment’s
      * linear-order successor, or the buffer limit if there is none.
      */
    private @Subst int segmentEndIndicator;



    /** The character at `segmentEndIndicator`, or the null character (00) if there is none.
      */
    private char segmentEndIndicatorChar;



    /** The start position in the buffer of the present fractal segment, if any,
      * which is the position of its first character.
      */
    private @Subst int segmentStart; // [ABP]



    private Reader sourceReader;



    private final ResourceSpooler spooler = new ResourceSpooler( this );



    private ParseState state;



    /** Scans through any sequence at buffer position `c` of plain space characters,
      * namely ‘S’ in the language definition.
      *
      *     @return The end boundary of the sequence, or `c` if there is none.
      */
    private int throughAnyS( int c ) {
        while( c < segmentEnd  &&  buffer.get(c) == ' ' ) ++c;
        return c; }



    /** Scans through any sequence at buffer position `c` of characters that are neither plain spaces
      * nor proper to a newline.
      *
      *     @return The end boundary of the sequence, or `c` if there is none.
      */
    private int throughAnyTerm( int c ) {
        for(; c < segmentEnd; ++c ) {
            final char ch = buffer.get( c );
            if( ch == ' ' || impliesNewline(ch) ) break; }
        return c; }



    /** Scans through a sequence at buffer position `c` of plain space characters,
      * namely ‘S’ in the language definition.
      *
      *     @return The end boundary of the sequence.
      *     @throws MalformedMarkup If no such sequence occurs at `c`.
      */
    private int throughS( int c ) throws MalformedMarkup {
        if( c /*moved*/!= (c = throughAnyS( c ))) return c;
        throw spaceExpected( bufferPointer( c )); }



    /** Scans through a sequence at buffer position `c` of characters that are neither plain spaces
      * nor proper to a newline.
      *
      *     @return The end boundary of the sequence.
      *     @throws MalformedMarkup If no such sequence occurs at `c`.
      */
    private int throughTerm( int c ) throws MalformedMarkup {
        if( c /*moved*/!= (c = throughAnyTerm( c ))) return c;
        throw termExpected( bufferPointer( c )); }



    private final DelimitableCharSequence xSeq = newDelimitableCharSequence( buffer );
      // For use only where declared.



   // ┈┈┈  s t a t e   t y p i n g  ┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈


    final void associativeReference( AssociativeReference r ) { associativeReference = r; }


        private AssociativeReference associativeReference;


        private final AssociativeReference_ basicAssociativeReference // [CIC]
          = new AssociativeReference_( this ).endSet();



    final void associativeReferenceEnd( AssociativeReference.End e ) { associativeReferenceEnd = e; }


        private AssociativeReference.End associativeReferenceEnd;



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



   // ▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀


    private static abstract class BlockParser {


        boolean didParse;



        abstract int parseAfterPossibleLeadDelimiterCharacter( int c, List<Markup> markup );



        int postSpaceEnd; }



   // ▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀


    /** A device to detect a comment appender or line end where it forms the end boundary of a bullet.
      */
    private final class BulletEndSeeker {


        /** Set when `wasAppenderFound` to the tight end boundary in the buffer of its delimiter.
          * If a plain space character (20) succeeds the delimiting backslash sequence,
          * then the tight end boundary is the position subsequent to that space character,
          * otherwise the position subsequent to the backslash sequence.
          */
        int cDelimiterTightEnd;



        /** Either the buffer position of the next non-space character (neither 20 nor A0),
          * or `buffer.limit`.
          */
        int cNextNonSpace;



        /** Tells whether the slash at `c` delimits a comment appender.
          * Updates `cDelimiterTightEnd` accordingly.
          *
          *     @param cSlash Buffer position of a (known) slash character ‘\’.
          *     @param cEnd End boundary of the point head.
          */
        boolean isDelimiterSlashAt( final int cSlash, final int cEnd ) {
            for( cDelimiterTightEnd = cSlash + 1;; ) {
                if( cDelimiterTightEnd == cEnd ) {
                    return true; }
                final char ch = buffer.charAt( cDelimiterTightEnd );
                if( ch != '\\' ) {
                    if( ch == ' ' ) {
                        ++cDelimiterTightEnd; // Past the space character, as per the contract.
                        return true; }
                    return impliesNewline( ch ); }}}



        /** Detects whether `c` is followed by plain space that delimits a comment appender,
          * recording the result in one or more fields of this seeker.
          *
          *     @param c Buffer position of a (known) no-break space character.
          *     @param cEnd End boundary of the point head.
          *     @throws MalformedMarkup On detection of a misplaced no-break space.
          */
        void seekFromNoBreakSpace( int c, final int cEnd ) throws MalformedMarkup {
            assert c < cEnd;
            if( ++c == cEnd ) {
                wasAppenderFound = false;
                wasLineEndFound = true; }
            else {
                final char ch = buffer.charAt( c );
                if( ch == ' ' ) {
                    seekFromSpace( c, cEnd );
                    if( wasLineEndFound || wasAppenderFound ) return;
                    throw misplacedNoBreakSpace( bufferPointer( c - 1 )); }
                else if( impliesNewline( ch )) {
                    wasAppenderFound = false;
                    wasLineEndFound = true; }
                else {
                    if( ch == '\u00A0' ) throw misplacedNoBreakSpace( bufferPointer( c ));
                    wasAppenderFound = false;
                    wasLineEndFound = false; }}
            cNextNonSpace = c; }



        /** Detects whether the plain space beginning at `c` delimits a comment appender,
          * recording the result in one or more fields of this seeker.
          *
          *     @param c Buffer position of a (known) plain space character.
          *     @param cEnd End boundary of the point head.
          *     @throws MalformedMarkup On detection of a misplaced no-break space.
          */
        void seekFromSpace( int c, final int cEnd ) throws MalformedMarkup {
            assert c < cEnd;
            for( ;; ) {
                if( ++c == cEnd ) {
                    wasAppenderFound = false;
                    wasLineEndFound = true;
                    break; }
                final char ch = buffer.charAt( c );
                if( ch != ' ' ) {
                    if( ch == '\\' ) {
                        wasAppenderFound = isDelimiterSlashAt( c, cEnd );
                        wasLineEndFound = false; }
                    else if( impliesNewline( ch )) {
                        wasAppenderFound = false;
                        wasLineEndFound = true; }
                    else {
                        if( ch == '\u00A0' ) throw misplacedNoBreakSpace( bufferPointer( c ));
                        wasAppenderFound = false;
                        wasLineEndFound = false; }
                    break; }}
            cNextNonSpace = c; }



        /** Whether a comment appender was found.  Never true when `wasLineEndFound`.
          */
        boolean wasAppenderFound;



        /** Whether a line end was encountered.  Never true when `wasAppenderFound`.
          */
        boolean wasLineEndFound; }



   // ▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀


    private final class LineResolver {


        /** The index of the resolved line in {@linkplain #fractumLineEnds fractumLineEnds}.
          */
        int index;



        /** The resolved line number.  Lines are numbered beginning at one.
          */
        int number;



        /** Resolves the line at `position`, recording the result in the fields of this resolver.
          * If `position` lies before `fractumStart`, then instead this method uses `fractumStart`;
          * if `position` lies after the region already parsed by `delimitSegment`,
          * then instead it uses the last parsed position.
          *
          *     @param position A buffer position within the parsed region of the present fractum.
          */
        @Subst void resolveLine( final int position ) {
            final int[] endsArray = fractumLineEnds.array;
            int e = 0, n = fractumLineNumber(), s = fractumStart;
            for( int end, eN = fractumLineEnds.length;    // For each line,
              e < eN && (end = endsArray[e]) < position; // if it ends before the position,
              ++e, ++n, s = end );                      // then advance to the next.
            index = e;
            number = n;
            start = s; } // The end boundary of its predecessor, if any, else `fractumStart`.



        /** The start position of the resolved line in the buffer.
          */
        int start; }



   // ▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀


    /** A warning that the target member is meaningful (fulfils its API description) only for substansive
      * parse states, those which implement `Markup` and therefore model text of non-zero length.
      * These are the parse states of {@linkplain Typestamp Typestamp} category (a).
      *
      */ @Documented @Retention(SOURCE) @Target({ FIELD, METHOD })
    private static @interface Subst {}}



// NOTES
// ─────
//   ABP  Adjustable buffer position.  This note serves as a reminder to adjust the value of the variable
//        in `delimitSegment` after each call to `buffer.compact`.
//
//   BAO  Backing-array offset.  This is non-zero in case of an array-backed buffer formed as a slice
//        of another buffer, but other cases may exist.  https://stackoverflow.com/a/24601336/2402790
//
//   CIC  Cached instance of concrete parse state.  Each instance is held in a constant field named
//        e.g. `basicFoo`, basic meaning unextended.  It could instead be held in `foo`, except then
//        it might be overwritten with an instance of a `Foo` subclass defined by a parser extension,
//        leaving the basic instance unavailable for future reuse.



                                                   // Copyright © 2020-2021  Michael Allan.  Licence MIT.
