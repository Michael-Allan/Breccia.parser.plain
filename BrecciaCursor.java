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
            "cf.",
            "e.g.",
            "i.e.",
            "join",
            "N.B.",
            "NB",
            "private",
            "re",
            "see",
            "viz." };
        final CommandPoint_<?>[] commandPoints = { // Each at the same index as its keyword above.
            basicAssociativeReference,   // ‘cf.’
            basicAssociativeReference,   // ‘e.g.’
            basicAssociativeReference,   // ‘i.e.’
            basicAssociativeReference,   // ‘join’
            basicAssociativeReference,   // ‘N.B.’
            basicAssociativeReference,   // ‘NB’
            basicPrivatizer,             // ‘private’
            basicAssociativeReference,   // ‘re’
            basicAssociativeReference,   // ‘see’
            basicAssociativeReference }; // ‘viz.’
        this.commandPointKeywords = commandPointKeywords;
        this.commandPoints = commandPoints; }



   // ━━━  C u r s o r  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    public final @Override @NarrowNot AlarmPoint asAlarmPoint() {
        return state == alarmPoint? alarmPoint : null; }



    public final @Override @NarrowNot AlarmPoint.End asAlarmPointEnd() {
        return state == alarmPointEnd? alarmPointEnd : null; }



    public final @Override @NarrowNot AsidePoint asAsidePoint() {
        return state == asidePoint? asidePoint : null; }



    public final @Override @NarrowNot AsidePoint.End asAsidePointEnd() {
        return state == asidePointEnd? asidePointEnd : null; }



    public final @Override @NarrowNot AssociativeReference asAssociativeReference()
          throws MalformedMarkup {
        if( state != associativeReference ) return null;
        if( !associativeReference.isComposed ) {
            composeAssociativeReference();
            associativeReference.isComposed = true; }
        return associativeReference; }



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
        if( !fileFractum.isComposed ) {
            composeFileFractum();
            fileFractum.isComposed = true; }
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



    public final @Override @NarrowNot TaskPoint asTaskPoint() {
        return state == taskPoint? taskPoint : null; }



    public final @Override @NarrowNot TaskPoint.End asTaskPointEnd() {
        return state == taskPointEnd? taskPointEnd : null; }



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
      *   each at the same index as its keyword in `aKeywords`.
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
            final int lineIndex = lineResolver.index;
            if( lineIndex < fractumLineEnds.length ) { // Then measure the easy way:
                lineLength = fractumLineEnds.array[lineIndex] - lineStart; }
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



    private final CommentaryHoldDetector commentaryHoldDetector = new CommentaryHoldDetector();



    private final BlockParser commentBlockParser = new CommentBlockParser();



    /** @return The end boundary of the holder, viz. past any terminal newline. *//*
      *
      * @paramImplied #commentaryHoldDetector What detected the hold in the markup.
      */
    private int compose( final CommentaryHolder_ holder ) {
        final CommentaryHoldDetector detector = commentaryHoldDetector;
        final DelimitableMarkupList cc = holder.components;
        assert cc.sizeLimit() == 5; // Literals 2 through 5 are written herein.

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
        assert !( buffer.get(b) == ' ' || impliesNewline(buffer.get(b)) ); // Not in plain whitespace.
        ++b; // Past the first character of the leading term.
        final int commentaryEnd;
        cc: for( ;; ) {
            term: for(;; ++b ) {
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



    /** @see AssociativeReference_#isComposed
      */
    final void composeAssociativeReference() throws MalformedMarkup {
        final AssociativeReference_ rA = associativeReference;
        assert !rA.isComposed;
        final DelimitableCharSequence keyword = rA.keyword;
        final CoalescentMarkupList cc = rA.descriptor.components;
        cc.clear();
        int b;
        cc.appendFlat( rA.bullet.text.end(), b = keyword.start() );
        final int bReferentialCommand;
        final DelimitableCharSequence referentialCommandKeyword;
        if( equalInContent( "re", keyword )) {

          // Referrer clause, from keyword
          // ───────────────
            final var cR = rA.referrerClauseWhenPresent;
            final CoalescentMarkupList cRcc = cR.components;
            cRcc.clear();
            final int a = b;
            cRcc.appendFlat( b, b = keyword.end() );
            b = parsePostgap( b, cRcc );
            b = parseDelimited( b, cRcc, cR.pattern );
            cR.text.delimit( a, b );
            cRcc.flush();
            cc.add( rA.referrerClause = cR );
            b = parsePostgap( b, cc );

          // Referential command, from scratch
          // ───────────────────
            bReferentialCommand = b;
            b = throughTerm( b );
            xSeq.delimit( bReferentialCommand, b );
            referentialCommandKeyword = xSeq; }
        else { // A referrer clause is absent.
            rA.referrerClause = null;

          // Referential command, from keyword
          // ───────────────────
            bReferentialCommand = keyword.start();
            b = keyword.end();
            referentialCommandKeyword = keyword; }
        if( equalInContent( "see", referentialCommandKeyword )) {
            final int c;
            int d = b;
            if( d /*moved*/!= (d = c = throughAnyS( d ))
             && d /*moved*/!= (d = throughAnyTerm( d ))) {
                xSeq.delimit( c, d );
                if( equalInContent( "also", xSeq )) b = d; }}
        rA.referentialCommand.text.delimit( bReferentialCommand, b );
        cc.add( rA.referentialCommand );
        b = parseAnyPostgap( b, cc );
        if( b < segmentEnd ) {

          // Referent clause
          // ───────────────
            final var cR = rA.referentClauseWhenPresent;
            xSeq.delimit( b, throughTerm(b) ); // As parameter to `parseAnyInferentialReferentIndicant`.
            if( b /*moved*/!= (b = parseAnyInferentialReferentIndicant( b, cR, cc ))) {
                cR.fractumIndicant = null; // Instead an inferential referent indicant is present:
                final var iIR = cR.inferentialReferentIndicant
                  = cR.inferentialReferentIndicantWhenPresent;
                cR.components = cR.componentAsInferentialReferentIndicant;
             /* cR.text.delimit( ...
                cc.add( cR );
                b = parseAnyPostgap( b, cc ); */ } /* No need, as `parseAnyInferentialReferentIndicant`,
                which was called above, has already done all this. */
            else {
                cR.inferentialReferentIndicant = null; // Instead a fractum indicant is present:
                final var iF = cR.fractumIndicant = cR.fractumIndicantWhenPresent;
                cR.text.delimit( b, b = parse( b, iF ));
                cR.components = cR.componentAsFractumIndicant;
                cc.add( cR );
                b = parseAnyPostgap( b, cc ); }
            rA.referentClause = cR; }
        else rA.referentClause = null;
        if( b < segmentEnd ) throw unexpectedTerm( bufferPointer( b ));
        else assert b == segmentEnd;
        cc.flush(); }



    /** @see NonCommandPoint.Descriptor#isComposed
      */
    final void composeDescriptor( final NonCommandPoint p ) throws MalformedMarkup {
        assert !p.descriptor.isComposed;
        final CoalescentMarkupList cc = p.descriptor.components;
        cc.clear();
        int b = p.bullet.text.end();
        assert segmentEnd > b;
        if( b /*unmoved*/== (b = parseAnyPostgap( b, cc ))) {
            throw new IllegalStateException( "Postgap expected\n" + bufferPointer(b).markedLine() ); }
            // Because no alternative is possible if `reifyPoint` has done its job.
        while( b /*moved*/!= (b = parseAnyTerm( b, cc ))
            && b /*moved*/!= (b = parseAnyPostgap( b, cc )));
        assert b == segmentEnd: parseEndsWithSegment(b);
        cc.flush(); }



    /** @see PlainCommandPoint.Descriptor#isComposed
      */
    final void composeDescriptor( final PlainCommandPoint_ p ) throws MalformedMarkup {
        // This method omits to encapsulate the command as such, pending a use case that would justify
        // the effort, the main part of which would lie in discovering the end boundary of the command
        // *exclusive of any trailing postgap*.
        assert !p.descriptor.isComposed;
        final CoalescentMarkupList cc = p.descriptor.components;
        cc.clear();
        int b;
        cc.appendFlat( p.bullet.text.end(), b = p.keyword.end() );
        while( b /*moved*/!= (b = parseAnyPostgap( b, cc ))
            && b /*moved*/!= (b = parseAnyTerm( b, cc )));
        assert b == segmentEnd: parseEndsWithSegment(b);
        cc.flush(); }



    /** @see SimpleCommandPoint.Descriptor#isComposed
      */
    final void composeDescriptor( final SimpleCommandPoint<?> p ) throws MalformedMarkup {
        assert !p.descriptor.isComposed;
        final CoalescentMarkupList cc = p.descriptor.components;
        cc.clear();
        final FlatMarkup command = p.descriptor.command;
        cc.appendFlat( p.bullet.text.end(), command.text.start() );
        cc.add( command );
        final int b = parseAnyPostgap( command.text.end(), cc );
        if( b < segmentEnd ) throw unexpectedTerm( bufferPointer( b ));
        else assert b == segmentEnd;
        cc.flush(); }



    /** @see FileFractum_.FileDescriptor#isComposed
      */
    final void composeFileDescriptor() {
        final FileFractum_.FileDescriptor descriptor = fileFractum.descriptor;
        assert !descriptor.isComposed;
        final CoalescentMarkupList cc = descriptor.components;
        cc.clear();
        assert buffer.position() == 0;
        assert fractumStart == 0;
        int b = 0;
        assert segmentEnd > 0;
        if( b /*unmoved*/== (b = parseAnyForegap( b, cc ))) {
            throw new IllegalStateException( "Foregap expected\n" + bufferPointer(b).markedLine() ); }
            // Because no alternative is possible if `delimitSegment` has done its job.
        while( b /*moved*/!= (b = parseAnyTerm( b, cc ))
            && b /*moved*/!= (b = parseAnyPostgap( b, cc )));
        assert b == segmentEnd: parseEndsWithSegment(b);
        cc.flush(); }



    /** @see FileFractum_#isComposed
      */
    final void composeFileFractum() {
        final FileFractum_ f = fileFractum;
        assert !f.isComposed;
        if( fractumStart == segmentEnd ) {
            f.components = FileFractum_.componentsWhenAbsent;
            f.descriptor = null; }
        else {
            assert fractumLineNumber() == 1; // Concordant with `FileFractum.lineNumber`.
            final FileFractum_.FileDescriptor d = f.descriptorWhenPresent;
            d.isComposed = false; // Pending demand.
            // No need to delimit `d.text`, which being identical to `f.text` is already delimited.
            f.components = f.componentsWhenPresent;
            f.descriptor = d; }}



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
      *     <li>the buffer is positioned at the `{@linkplain #segmentEndIndicant segmentEndIndicant}`
      *         of the preceding segment, or at zero in case of a new markup source.</li></ul>
      *
      * <p>This method updates the following.</p>
      *
      * <ul><li>`{@linkplain #fractumLineEnds        fractumLineEnds}`</li>
      *     <li>`{@linkplain #segmentEnd             segmentEnd}`</li>
      *     <li>`{@linkplain #segmentEndIndicant     segmentEndIndicant}`</li>
      *     <li>`{@linkplain #segmentEndIndicantChar segmentEndIndicantChar}`</li></ul>
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
      *       buffer position through the newly determined `segmentEndIndicant`.
      *     @throws MalformedMarkup For any misplaced no-break space that occurs from the initial buffer
      *       position through the newly determined `segmentEndIndicant`, except on the first line of
      *       a point, where instead `{@linkplain #reifyPoint(int) reifyPoint}` polices this offence.
      *     @throws MalformedMarkup For any malformed line break that occurs from the initial
      *       buffer position through the newly determined `segmentEndIndicant`.
      */
    private void delimitSegment() throws ParseError {
        assert segmentStart != fractumStart || fractumLineEnds.isEmpty();
        final boolean isFileHead = fractumIndentWidth < 0;
        assert buffer.position() == (isFileHead? 0 : segmentEndIndicant);
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
                    segmentEnd = segmentEndIndicant = p;
                    segmentEndIndicantChar = '\u0000';
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
            if( ch != ' ' && yetIsGenerallyWhitespace(ch) ) {
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
                    throw misplacedNoBreakSpace( bufferPointerBack() ); }
                inIndentedBackslashes = false; }
            else if( ch == '\u00A0' ) { // A no-break space not `inMargin` ∴ delimiting no indent blind.
                if( inCommentBlock ) continue;
                if( !isFileHead && !isDividerDrawing(segmentEndIndicantChar) // In a point head,
                 && fractumLineEnds.isEmpty() ) {                           // on the first line.
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



    private final Matcher graphemeClusterMatcher
      = java.util.regex.Pattern.compile( "\\X" ).matcher( "" );
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



    private final BlockParser indentBlindParser = new IndentBlindParser();



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
            segmentStart = segmentEnd = segmentEndIndicant = 0;
            delimitSegment(); }
        buffer.rewind(); // Concordant with `buffer` contract.
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
        final int nextIndentWidth = segmentEndIndicant - segmentEnd; /* The offset from the start of
          the next fractum (`segmentEnd`) to its first non-space character (`segmentEndIndicant`). */
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
        if( isDividerDrawing( segmentEndIndicantChar )) { /* Then next is a divider segment,
              starting a division whose head comprises all contiguous divider segments. */
            do nextSegment(); while( isDividerDrawing( segmentEndIndicantChar )); // Scan through each.
            buffer.rewind(); // Concordant with `buffer` contract.
 /**/       readyDivision().commit(); }
        else { // Next is a point.
            nextSegment(); // Scan through to the end boundary of its head.
            buffer.rewind(); // Concordant with `buffer` contract.
 /**/       reifyPoint().commit(); }
        final int i = fractumIndentWidth / 4; // Indent in perfect units, that is.
        while( hierarchy.size() < i ) hierarchy.add( null ); // Padding for unoccupied ancestral indents.
        assert state == bodyFractum;
        hierarchy.add( bodyFractum ); }



    private void nextSegment() throws ParseError {
        buffer.position( segmentEndIndicant );

        // Changing what follows?  Sync → `markupSource`.
        segmentStart = segmentEnd;
        delimitSegment(); }



    /** Parses a fractum indicant at buffer position `b`.
      *
      *     @return The end boundary of the fractum indicant.
      *     @throws MalformedMarkup If no fractum indicant occurs at `b`.
      */
    private int parse( int b, final FractumIndicant_ iF ) throws MalformedMarkup {
     // iF.text.delimit( ...
        throw new UnsupportedOperationException(); }



    /** Parses any comment appender, the delimiter of which (a backslash sequence)
      * would begin at buffer position `b`, adding it to the given markup list.
      * Already the markup before `b` is known to be well formed for the purpose.
      *
      *     @return The end boundary of the comment appender, or `b` if there is none.
      */
    private int parseAnyCommentAppender( int b, final List<Markup> markup ) {
        if( b < segmentEnd && buffer.get(b) == '\\'
              && commentaryHoldDetector.slashStartsDelimiter(b) ) {
            final CommentAppender_ appender = spooler.commentAppender.unwind();
            appender.text.delimit( b, b = compose( appender ));
            markup.add( appender ); }
        return b; }



    /** Parses any foregap at buffer position `b`, adding each of its components
      * to the given markup list.  Already `b` is known to bound either the end
      * of the fractal segment (edge case) or the start of a line within it.
      *
      *     @return The end boundary of the foregap, or `b` if there is none.
      */
    private int parseAnyForegap( int b, final CoalescentMarkupList markup ) {
        if( b >= segmentEnd ) return b; /* As required, either `b` bounds the segment end (left),
          or a line start (right). */ assert b == segmentStart || completesNewline(buffer.get(b-1));
        int bLine = b; // The last position at which a line starts.
        int bFlat = b; /* The last potential start position of flat markup,
          each character being either a plain space or newline constituent. */

      // Establish the loop invariant
      // ────────────────────────────
        char ch = buffer.get( b );
        if( ch == ' ' ) {
            b = throughAnyS( ++b );
            if( b >= segmentEnd ) {
                markup.appendFlat( bFlat, b );
                return b; }
            ch = buffer.get( b ); }

      // Loop through the foregap form
      // ─────────────────────────────
        for( ;; ) { /* Loop invariant.  Character `ch` at `b` lies within the fractal segment and is not
              a plain space.  Rather it is a newline constituent or the first character either of the
              lead delimiter of a block construct in the foregap, or of a term just after the foregap. */
            assert b < segmentEnd && ch != ' ';
            if( completesNewline( ch )) {
                ++b; // Past the newline.
                if( b >= segmentEnd ) {
                    markup.appendFlat( bFlat, b );
                    break; }
                bLine = b;
                ch = buffer.get( b );
                if( ch != ' ' ) continue; // Already the invariant is re-established.
                b = throughAnyS( ++b ); } // Re-establishing the invariant, part 1.
            else if( impliesWithoutCompletingNewline( ch )) {
                ch = buffer.get( ++b ); // Toward its completion.
                assert impliesNewline( ch ); // Already `delimitSegment` has tested for this.
                continue; } // Already the invariant is re-established.
            else { // Expect a comment block or indent blind, or a term that bounds the foregap.
                if( bFlat < b ) markup.appendFlat( bFlat, b ); // Flat markup that came before.
                final BlockParser parser;
                if( ch == '\\' ) parser = commentBlockParser;
                else if( ch == /*no-break space*/'\u00A0' ) parser = indentBlindParser;
                else break; // The foregap ends at a non-backslashed term.
                if( b /*unmoved*/== (b = parser.parseIfDelimiter( b, bLine, markup ))) {
                    break; } // The foregap ends at a backslashed term.
                if( b >= segmentEnd ) break; // This block ends both the foregap and fractal segment.
                bLine = b; // This block has ended with a line break.
                bFlat = b; // Potentially the next run of flat markup begins here.
                b = parser.postSpaceEnd; } // Re-establishing the invariant, part 1.

          // re-establish the invariant, part 2
          // ┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈
            if( b >= segmentEnd ) {
                assert bFlat < b;
                markup.appendFlat( bFlat, b );
                break; }
            ch = buffer.get( b ); }
        return b; }



    /** Parses any inferential referent indicant at buffer position `b`, adding its components to
      * `cR.inferentialReferentIndicantWhenPresent.components`, delimiting `cR.text`,
      * and adding `cR` with any subsequent postgap to `rADcc`.
      *
      *     @param rADcc The descriptor component list of the associative reference containing `cR`.
      *     @return The end boundary of the last thing parsed (inferential referent indicant or postgap)
      *       or `b` if no inferential referent indicant is present, in which case this method leaves
      *       `cR.inferentialReferentIndicantWhenPresent.components` empty, but otherwise leaves
      *       `cR` and `rADcc` untouched. *//*
      *
      *     @paramImplied xSeq The character sequence in the buffer of the first term of `cR`.
      */
    private int parseAnyInferentialReferentIndicant( int b,  final AssociativeReference_.
          ReferentClause_ cR, final CoalescentMarkupList rADcc ) throws MalformedMarkup {
        final int bOriginal = b;
        final var iIR = cR.inferentialReferentIndicantWhenPresent;
        final CoalescentArrayList cc = iIR.components;
        cc.clear();
        final var seqTerm = xSeq; // The character sequence of the last discovered term of `cR`.
        int cTermEnd = 0; // The end boundary in `cc` of the last term added.
        composition: {

          // i. Referrer similarity
          // ──────────────────────
            if( equalInContent( "same", seqTerm ) || equalInContent( "similar", seqTerm )) {
                b = seqTerm.end();
                final var sim = iIR.referrerSimilarityWhenPresent;
                iIR.referrerSimilarity = sim;
                sim.text.delimit( seqTerm.start(), b );
                cc.add( sim );
                cTermEnd = cc.size();
                if( b /*unmoved*/== (b = parseAnyPostgap( b, cc ))) break composition;
                final int d = throughAnyTerm( b );
                if( d /*unmoved*/== b ) break composition;
                seqTerm.delimit( b, d ); }

          // ii. Referential form
          // ────────────────────
            if( equalInContent( "head", seqTerm ) || equalInContent( "term", seqTerm )) {
                b = seqTerm.end();
                final var form = iIR.referentialFormWhenPresent;
                iIR.referentialForm = form;
                form.text.delimit( seqTerm.start(), b );
                cc.add( form );
                cTermEnd = cc.size();
                if( b /*unmoved*/== (b = parseAnyPostgap( b, cc ))) break composition;
                final int d = throughAnyTerm( b );
                if( d /*unmoved*/== b ) break composition;
                seqTerm.delimit( b, d ); }

          // iii. Containment
          // ────────────────
            if( equalInContent( "@", seqTerm )) {
                final var cC = iIR.containmentClauseWhenPresent;
                final CoalescentMarkupList cCcc = cC.components;
                cCcc.clear();
                cCcc.appendFlat( b, ++b ); // The ‘@’.
                b = parsePostgap( b, cCcc );
                final var iF = iIR.fractumIndicant = iIR.fractumIndicantWhenPresent;
                b = parse( b, iF );
                cCcc.add( iF );
                cC.text.delimit( seqTerm.start(), b );
                cCcc.flush();
                cc.add( cC );

              // Finalization of `cR` and `iIR` wherein the latter ends with a containment clause (iii)
              // ────────────
                iIR.text.delimit( bOriginal, b );
                cc.flush();
                cR.text.delimit( bOriginal, b );
                rADcc.add( cR );

              // Postgap subsequent to `cR`
              // ───────
                b = parseAnyPostgap( b, rADcc );
                return b; }
            if( b == bOriginal ) return b; } // No inferential referent indicant is present.

      // Finalization of `cR` and `iIR` wherein the latter comprises one or both
      // ────────────        of referrer similarity (i) and referential form (ii)
        final int d = seqTerm.end();
        iIR.text.delimit( bOriginal, d ); // Using `d`, not `b` which bounds instead any postgap.
        cc.flush();
        cR.text.delimit( bOriginal, d );
        rADcc.add( cR );

      // Postgap subsequent to `cR`
      // ───────
        final int cN = cc.size();
        assert cTermEnd != 0; // It has been set.
        if( cTermEnd < cN ) { /* Then a final postgap is present, which inadvertently
              was appended to `cc`.  Move it from `cc` to `rADcc`, where it belongs.
              Having to do so is why this method itself must (above) add `cR` to `rADcc`. */
            for( int c = cTermEnd; c < cN; ++c ) rADcc.add( cc.get( c ));
            cc.removeRange( cTermEnd, cN ); } /* With this removal, `cc` would be broken by any
              further coalescence.  There will be none, however, as its content is now complete. */
        return b; }



    /** Parses any sequence of newlines at buffer position `b`, adding it to the given markup list.
      *
      *     @return The end boundary of the sequence, or `b` if there is none.
      */
    private int parseAnyNewlines( final int b, final CoalescentMarkupList markup ) {
        final int c = throughAnyNewlines( b );
        if( b != c ) markup.appendFlat( b, c );
        return c; }



    /** Parses any postgap at buffer position `b`,
      * adding each of its components to the given markup list.
      *
      *     @return The end boundary of the postgap, or `b` if there is none.
      */
    private int parseAnyPostgap( int b, final CoalescentMarkupList markup ) {
        if( b /*moved*/!= (b = parseAnyS( b, markup ))) {
            if( b /*moved*/!= (b = parseAnyCommentAppender( b, markup ))) {
                return parseAnyForegap( b, markup ); }}
        if( b /*moved*/!= (b = parseAnyNewlines( b, markup ))) b = parseAnyForegap( b, markup );
        return b; }



    /** Parses any sequence at buffer position `b` of plain space characters,
      * namely ‘S’ in the language definition, adding it to the given markup list.
      *
      *     @return The end boundary of the sequence, or `b` if there is none.
      */
    private int parseAnyS( final int b, final CoalescentMarkupList markup ) {
        final int c = throughAnyS( b );
        if( b != c ) markup.appendFlat( b, c );
        return c; }



    /** Parses any term at buffer position `b`, adding it to the given markup list.
      *
      *     @return The end boundary of the term, or `b` if there is none.
      */
    private int parseAnyTerm( final int b, final CoalescentMarkupList markup ) {
        final int c = throughAnyTerm( b );
        if( b != c ) markup.appendFlat( b, c );
        return c; }



    private String parseEndsWithSegment( final int b ) {
        return "Parse ends with the segment\n" + bufferPointer(b).markedLine(); }



    /** Parses at buffer position `b` a regular-expression pattern complete with its delimiters,
      * adding each to the given markup list.
      *
      *     @param pattern The pattern instance to use for the purpose.
      *     @return The end boundary of the delimiter.
      *     @throws MalformedMarkup If no such delimiter occurs at `b`.
      */
    private int parseDelimited( int b, final List<Markup> markup, final Pattern pattern )
          throws MalformedMarkup {
        if( b < segmentEnd  &&  buffer.get(b) == '`' ) {
            final FlatMarkup delimiter = spooler.patternDelimiter.unwind();
            delimiter.text.delimit( b, ++b );
            markup.add( delimiter ); }
        else throw new MalformedMarkup( bufferPointer(b), "Pattern delimiter expected" );
        final CoalescentMarkupList cc = pattern.components;
        cc.clear();
        boolean inEscape = false; // Whether the last character was a backslash ‘\’.
        for( final int bPattern = b; b < segmentEnd; ) {
            final char ch = buffer.get( b );
            if( impliesNewline( ch )) break;

          // Backslashed sequences
          // ─────────────────────
            if( inEscape ) {
                if( ch == 'b' || ch == 'd' || ch == 'R' || ch == 't' ) {
                    final FlatMarkup special = spooler.backslashedSpecial.unwind();
                    special.text.delimit( b - 1, ++b ); // Including the prior backslash.
                    cc.add( special ); }
                else if( ch == 'N' ) {
                    final int bStart = b - 1; // The prior backslash.
                    b = throughBackslashedSpecialNQualifier( ++b );
                    final FlatMarkup special = spooler.backslashedSpecial.unwind();
                    special.text.delimit( bStart, b );
                    cc.add( special ); }
                else {
                    final FlatMarkup literalizer = spooler.literalizer.unwind();
                    literalizer.text.delimit( b - 1, b );
                    cc.add( literalizer );
                    cc.appendFlat( b, ++b ); }
                inEscape = false;
                continue; }
            if( ch == '\\' ) {
                inEscape = true;
                ++b;
                continue; }

          // Metacharacters
          // ──────────────
            if( ch == '^' ) {
                final int bStart = b++;
                if( b < segmentEnd  &&  buffer.get(b) == '^' ) { // A second ‘^’, so making ‘^^’.
                    final FlatMarkup indent = spooler.perfectIndent.unwind();
                    indent.text.delimit( bStart, ++b );
                    cc.add( indent ); }
                else {
                    final FlatMarkup metacharacter = spooler.metacharacter.unwind();
                    metacharacter.text.delimit( bStart, b );
                    cc.add( metacharacter ); }
                continue; }
            if( ch == '.' || ch == '$' || ch == '|' || ch == '*' || ch == '+' || ch == '?' ) {
                final FlatMarkup metacharacter = spooler.metacharacter.unwind();
                metacharacter.text.delimit( b, ++b );
                cc.add( metacharacter );
                continue; }

          // Group delimiters
          // ────────────────
            if( ch == '(' ) {
                final int bStart = b++;
                if( b < segmentEnd  &&  buffer.get(b) == '?' ) {
                    final int c = b + 1;
                    if( c < segmentEnd  &&  buffer.get(c) == ':' ) b = c + 1;}
                final FlatMarkup delimiter = spooler.groupDelimiter.unwind();
                delimiter.text.delimit( bStart, b );
                cc.add( delimiter );
                continue; }
            if( ch == ')' ) {
                final FlatMarkup delimiter = spooler.groupDelimiter.unwind();
                delimiter.text.delimit( b, ++b );
                cc.add( delimiter );
                continue; }

          // Closing pattern delimiter
          // ─────────────────────────
            if( ch == '`' ) {
                if( b == bPattern ) throw new MalformedMarkup( bufferPointer(b), "Empty pattern" );
                pattern.text.delimit( bPattern, b );
                cc.flush();
                markup.add( pattern );
                final FlatMarkup delimiter = spooler.patternDelimiter.unwind();
                delimiter.text.delimit( b, ++b );
                markup.add( delimiter );
                return b; }

          // Literal characters
          // ──────────────────
            cc.appendFlat( b, ++b ); }
        throw truncatedPattern( bufferPointer( b )); }



    /** Parses a postgap at buffer position `b`, adding each of its components to the given markup list.
      *
      *     @return The end boundary of the postgap.
      *     @throws MalformedMarkup If no postgap occurs at `b`.
      */
    private int parsePostgap( int b, final CoalescentMarkupList markup ) throws MalformedMarkup {
        if( b /*moved*/!= (b = parseAnyPostgap( b, markup ))) return b;
        throw new MalformedMarkup( bufferPointer(b), "Postgap expected" ); }



    /** Parses a sequence at buffer position `b` of plain space characters,
      * namely ‘S’ in the language definition, adding it to the given markup list.
      *
      *     @return The end boundary of the sequence.
      *     @throws MalformedMarkup If no such sequence occurs at `b`.
      */
    private int parseS( int b, final CoalescentMarkupList markup ) throws MalformedMarkup {
        markup.appendFlat( b, b = throughS(b) );
        return b; }



    /** Parses a term at buffer position `b`, adding it to the given markup list.
      *
      *     @return The end boundary of the term.
      *     @throws MalformedMarkup If no term occurs at `b`.
      */
    private int parseTerm( int b, final CoalescentMarkupList markup ) throws MalformedMarkup {
        markup.appendFlat( b, b = throughTerm(b) );
        return b; }



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
        basicFileFractum.isComposed = false; // Pending demand.
        return basicFileFractum; }



    /** Parses enough of a command point to learn its concrete type and return its parse state
      * ready to commit.  This method is a subroutine of `reifyPoint`.
      *
      *     @param bullet The buffer position of the bullet.
      *     @param bulletEnd The buffer position just after the bullet, viz. its end boundary.
      *       Already it is known (and asserted) to hold a plain space character. *//*
      */
    private CommandPoint_<?> reifyCommandPoint( final int bullet, final int bulletEnd )
          throws MalformedMarkup {
        int b = bulletEnd + 1; // Past the known space character.
        b = throughAnyS( b ); // Past any others.
        xSeq.delimit( b, b = throughTerm(b) );
        final DelimitableCharSequence privately;
        final DelimitableCharSequence keyword;
        if( equalInContent( "privately", xSeq )) {
            privately = xSeq;
            b = throughS( b );
            ySeq.delimit( b, b = throughTerm(b) );
            keyword = ySeq; }
        else {
            privately = null;
            keyword = xSeq; }

      // Resolve the concrete parse state
      // ────────────────────────────────
        b = binarySearch( commandPointKeywords, keyword, CharSequence::compare );
        final CommandPoint_<?> p = b >= 0? commandPoints[b] : basicPlainCommandPoint;

      // Therein delimit the components proper to all types of command point, and already parsed
      // ──────────────────────────────
        p.text.delimit(                    fractumStart,      segmentEnd ); // Proper to fracta.
        p.perfectIndent.text.delimit( /*0*/fractumStart, /*1*/bullet );    // Proper to body fracta.
        p.bullet       .text.delimit( /*1*/bullet,       /*2*/bulletEnd );
        p.descriptor() .text.delimit( /*2*/bulletEnd,    /*3*/segmentEnd );
        if( privately != null ) {
            p.privatelyWhenPresent.delimitAs( privately );
            p.modifierSet.add( CommandPoint.Modifier.privately ); }
        else p.modifierSet.clear();
        p.keyword.delimitAs( keyword );

      // Ready to commit
      // ───────────────
        return p; }



    /** Parses enough of a point other than a command point to learn its concrete type and return
      * its parse state ready to commit.  This method is a subroutine of `reifyPoint`.
      *
      *     @param bullet The buffer position of the bullet.
      *     @param bulletEnd The buffer position just after the bullet, viz. its end boundary.
      */
    private NonCommandPoint reifyNonCommandPoint( final int bullet, final int bulletEnd ) {

      // Resolve the concrete parse state
      // ────────────────────────────────
        final char chLast = buffer.get( bulletEnd - 1 );
        final NonCommandPoint p;
        if( chLast == '+' ) p = basicTaskPoint;
        else if( bulletEnd - bullet/*length*/ == 1 ) {
            if( chLast == '/' ) p = basicAsidePoint;
            else p = basicPlainPoint; }
        else if( chLast == '!'  &&  buffer.get(bulletEnd-2) == '!' ) p = basicAlarmPoint;
        else p = basicPlainPoint;

      // Therein delimit the components proper to all types of non-command point, and already parsed
      // ──────────────────────────────
        final var cc = p.components;
        p              .text.delimit(      fractumStart,      segmentEnd ); // Proper to fracta.
        p.perfectIndent.text.delimit( /*0*/fractumStart, /*1*/bullet );    // Proper to body fracta.
        p.bullet       .text.delimit( /*1*/bullet,       /*2*/bulletEnd );
        if( bulletEnd < segmentEnd ) {
            final var d = p.descriptorWhenPresent;
            d          .text.delimit( /*2*/bulletEnd,    /*3*/segmentEnd );
            d.isComposed = false; // Pending demand.
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
      *     @throws MalformedMarkup For any misplaced no-break space occuring on the same line.  Note
      *       that elsewhere `{@linkplain #delimitSegment() delimitSegment}` polices this offence. *//*
      */
    private Point_<?> reifyPoint() throws MalformedMarkup {
        final int bullet = fractumStart + fractumIndentWidth;
        assert buffer.position() == 0;

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
            final Matcher mCluster = graphemeClusterMatcher.reset( /*input sequence*/buffer )
              .region( b, buffer.limit() );
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
                    if( ch == '\u00A0' ) throw misplacedNoBreakSpace( bufferPointer( b )); }
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
                if( ch == '\u00A0' ) throw misplacedNoBreakSpace( bufferPointer( b )); }}

      // Resolve the concrete parse state
      // ────────────────────────────────
        final Point_<?> p;
        if( bulletEnd - bullet/*length*/ == 1  &&  buffer.get(bullet) == ':' ) {
            if( endSeeker != null ) { // Then the only case is that of the bullet ending (wrongly for
                assert buffer.get(bulletEnd) == '\u00A0'; // a command point) at a no-break space.
                throw spaceExpected( bufferPointer( bulletEnd )); }
            if( wasLineEndFound ) { // Then the bullet ends directly at the line end, with no
                throw termExpected( bufferPointer( b )); } // command between the two.
            assert buffer.get(bulletEnd) == ' '; // The only remaining case.
            p = reifyCommandPoint( bullet, bulletEnd ); }
        else p = reifyNonCommandPoint( bullet, bulletEnd );

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
    private @Subst int segmentEndIndicant;



    /** The character at `segmentEndIndicant`, or the null character (00) if there is none.
      */
    private char segmentEndIndicantChar;



    /** The start position in the buffer of the present fractal segment, if any,
      * which is the position of its first character.
      */
    private @Subst int segmentStart; // [ABP]



    private Reader sourceReader;



    final ResourceSpooler spooler = new ResourceSpooler( this );



    private ParseState state;



    /** Scans through any newline at buffer position `b`.
      *
      *     @return The end boundary of the newline, or `b` if there is none.
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
      *     @return The end boundary of the sequence, or `b` if there is none.
      */
    private int throughAnyNewlines( int b ) {
        while( b < segmentEnd  &&  impliesNewline(buffer.get(b)) ) ++b; /* This implies a sequence of
          well-formed newlines only because already `delimitSegment` has tested for malformed ones. */
        return b; }



    /** Scans through any sequence at buffer position `b` of plain space characters,
      * namely ‘S’ in the language definition.
      *
      *     @return The end boundary of the sequence, or `b` if there is none.
      */
    private int throughAnyS( int b ) {
        while( b < segmentEnd  &&  buffer.get(b) == ' ' ) ++b;
        return b; }



    /** Scans through any term at buffer position `b`.  A term is (as the language defines it) a sequence
      * of non-whitespace characters that does not comprise a sequence of backslashes ‘\’.
      *
      *     @return The end boundary of the term, or `b` if there is none.
      */
    private int throughAnyTerm( int b ) {
        final int bOriginal;
        final char chFirst; {
            if( b >= segmentEnd ) return b;
            chFirst = buffer.get( b );
            if( isWhitespace( chFirst )) return b;
            bOriginal = b++; }
        if( chFirst == '\\' ) { // Then scan the remainder by the slow, exhaustive method. (edge case)
            boolean comprisesBackslashes = true; // Thus far.
            for(; b < segmentEnd; ++b ) {
                final char ch = buffer.get( b );
                if( isWhitespace( ch )) break;
                if( ch != '\\' ) comprisesBackslashes = false; }
            if( comprisesBackslashes ) return bOriginal; }
        else while( b < segmentEnd && !isWhitespace(buffer.get(b)) ) ++b; // The fast way. (typical case)
        return b; }



    /** Scans through the bracketed qualifier of a ‘\N{⋯}’ sequence at buffer position `b`,
      * beginning with the ‘{’ delimitier.
      *
      *     @see <a href='https://perldoc.perl.org/perlrebackslash#Named-or-numbered-characters-and-character-sequences'>
      *       Named or numbered characters</a>
      *     @return The end boundary of the qualifier, after the terminal ‘}’.
      *     @throws MalformedMarkup If no such qualifier occurs at `b`.
      */
    private int throughBackslashedSpecialNQualifier( int b ) throws MalformedMarkup {
        if( b < segmentEnd  &&  buffer.get(b) == '{' ) ++b;
        else throw new MalformedMarkup( bufferPointer(b), "Curly bracket ‘{’ expected" );
        final int bContent = b; // Subsequent to the opening ‘{’ delimiter.
        boolean inNumeric = false; // Whether the content begins ‘U+’, denoting a numbered qualifier.
        for( char ch = '\u0000', chLast = '\u0000';  b < segmentEnd;  chLast = ch, ++b ) {
            ch = buffer.get( b );
            if( impliesNewline( ch )) break;
            if( ch == '}' ) {
                if( inNumeric ) {
                    if( b - bContent < 3 ) {
                        throw new MalformedMarkup( bufferPointer(b), "Hexadecimal digit expected" ); }}
                else if( b == bContent ) {
                    throw new MalformedMarkup( bufferPointer(b), "Empty qualifier" ); }
                return ++b; }
            if( ch == '+'  &&  chLast == 'U'  &&  b - bContent == 1 ) inNumeric = true;
            else if( inNumeric ) {
                if( !( ch >= '0' && ch <= '9' || ch >= 'A' && ch <= 'F' || ch >= 'a' && ch <= 'f' )) {
                    throw new MalformedMarkup( bufferPointer(b), "Hexadecimal digit expected" ); }}
            else if( !( ch >= 'A' && ch <= 'Z'
              || b > bContent && ( ch >= '0' && ch <= '9' || ch == ' ' || ch == '-' ))) {
                // See Names § 4.8, `https://www.unicode.org/versions/Unicode13.0.0/ch04.pdf`
                throw new MalformedMarkup( bufferPointer(b),
                  "Character not allowed here, Unicode " + (int)ch ); }}
        throw truncatedPattern( bufferPointer( b )); }



    /** Scans through a sequence at buffer position `b` of plain space characters,
      * namely ‘S’ in the language definition.
      *
      *     @return The end boundary of the sequence.
      *     @throws MalformedMarkup If no such sequence occurs at `b`.
      */
    private int throughS( int b ) throws MalformedMarkup {
        if( b /*moved*/!= (b = throughAnyS( b ))) return b;
        throw spaceExpected( bufferPointer( b )); }



    /** Scans through a term at buffer position `b`.  A term is (as the language defines it) a sequence
      * of non-whitespace characters that does not comprise a sequence of backslashes ‘\’.
      *
      *     @return The end boundary of the term.
      *     @throws MalformedMarkup If no term occurs at `b`.
      */
    private int throughTerm( int b ) throws MalformedMarkup {
        if( b /*moved*/!= (b = throughAnyTerm( b ))) return b;
        throw termExpected( bufferPointer( b )); }



    private final DelimitableCharSequence xSeq = newDelimitableCharSequence( buffer );
      // Shared reusable instance



    private final DelimitableCharSequence ySeq = newDelimitableCharSequence( buffer );
      // Shared reusable instance



   // ┈┈┈  s t a t e   t y p i n g  ┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈┈


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



    final void associativeReference( AssociativeReference_ r ) { associativeReference = r; }


        private AssociativeReference_ associativeReference;


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



    final void taskPoint( TaskPoint p ) { taskPoint = p; }


        private TaskPoint taskPoint;


        private final TaskPoint_ basicTaskPoint = new TaskPoint_( this ).endSet(); // [CIC]



    final void taskPointEnd( TaskPoint.End e ) { taskPointEnd = e; }


        private TaskPoint.End taskPointEnd;



   // ▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀


    private static abstract class BlockParser {


        /** Parses any block, the lead delimiter of which would begin with the known character
          * of buffer position `b`, adding it to the given markup list.  Already the markup
          * through `b` is known to be well formed for the purpose.
          *
          *     @param bLine Buffer position of the start of the line wherein `b` lies.
          *     @return The end boundary of the block, or `b` if there is none.
          */
        abstract int parseIfDelimiter( int b, int bLine, List<Markup> markup );



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
          *     @throws MalformedMarkup On detection of a misplaced no-break space.
          */
        void seekFromNoBreakSpace( int b ) throws MalformedMarkup {
            assert b < segmentEnd;
            if( ++b == segmentEnd ) {
                wasAppenderFound = false;
                wasLineEndFound = true; }
            else {
                final char ch = buffer.charAt( b );
                if( ch == ' ' ) {
                    seekFromSpace( b );
                    if( wasLineEndFound || wasAppenderFound ) return;
                    throw misplacedNoBreakSpace( bufferPointer( b - 1 )); }
                if( impliesNewline( ch )) {
                    wasAppenderFound = false;
                    wasLineEndFound = true; }
                else {
                    if( ch == '\u00A0' ) throw misplacedNoBreakSpace( bufferPointer( b ));
                    wasAppenderFound = false;
                    wasLineEndFound = false; }}
            bNextNonSpace = b; }



        /** Detects whether the known, plain space beginning at `b` is followed by a backslash sequence
          * that delimits a comment appender, recording the result in one or more fields of this seeker.
          *
          *     @param b Buffer position of a (known) plain space character.
          *     @throws MalformedMarkup On detection of a misplaced no-break space.
          */
        void seekFromSpace( int b ) throws MalformedMarkup {
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
                        if( ch == '\u00A0' ) throw misplacedNoBreakSpace( bufferPointer( b ));
                        wasAppenderFound = false;
                        wasLineEndFound = false; }
                    break; }}
            bNextNonSpace = b; }



        /** Tells whether the known backslash at `b` starts the delimiter of a comment appender,
          * and updates `bDelimiterFullEnd` accordingly.  Already the markup through `b`
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
          * and updates the fields of this detector accordingly.  Already the markup through `b`
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
        @Override int parseIfDelimiter( int b, int bLine, final List<Markup> parentMarkup ) {
            final CommentaryHoldDetector detector = commentaryHoldDetector;
            if( detector.slashStartsDelimiter( b )) {
                // Changing what follows?  Sync → namesake method of `IndentBlindParser`.
                final int bBlock = bLine;
                final CommentBlock_ block = spooler.commentBlock.unwind();
                final var blockMarkup = block.components;
                blockMarkup.clear();
                for( ;; ) {
                    final var line = spooler.commentBlockLine.unwind();
                    final DelimitableMarkupList lineMarkup = line.components;

                  // `c0_white`
                  // ──────────
                    if( bLine < b/*delimiter*/ ) {
                        lineMarkup.start( 0 ); // The line starts with `c0_white`.
                        line.c0_white.text.delimit( bLine, b ); }
                    else lineMarkup.start( 1 ); // It starts with `c1_delimiter`.

                  // `c1_delimiter` through `c4_white`
                  // ─────────────────────────────────
                    line.text.delimit( bLine, b = compose( line ));
                    if( detector.hasDetectedCommentary ) {
                        line.c3_commentaryTagName(
                          detector.delimiterLength() == 1? "Commentary" : "Label" ); }
                    blockMarkup.add( line );

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
                parentMarkup.add( block ); }
            return b; }}



   // ▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀


    private final class IndentBlindParser extends BlockParser {


        /** {@inheritDoc} <p>Here ‘lead delimiter’ means a no-break space in the first line
          * of the indent blind.  Since this much is already given to be well formed,
          * and nothing more is wanted, always a parse will occur.</p>
          *
          *     @return The end boundary of the block subsequent to `b`.
          */
        @Override int parseIfDelimiter( int b, int bLine, final List<Markup> parentMarkup ) {
            // Changing what follows?  Sync → namesake method of `CommentBlockParser`.
            final int bBlind = bLine;
            final IndentBlind_ blind = spooler.indentBlind.unwind();
            final var blindMarkup = blind.components;
            blindMarkup.clear();
            for( ;; ) {
                final var line = spooler.indentBlindLine.unwind();
                final List<Markup> lineMarkup = line.components;
                lineMarkup.clear();
                Markup_ m;

              // 0. Indent, if any
              // ─────────
                if( bLine < b/*delimiter*/ ) {
                    m = line.indentWhenPresent;
                    m.text.delimit( bLine, b );
                    lineMarkup.add( m ); }

              // 1. Delimiter
              // ────────────
                m = line.delimiter;
                m.text.delimit( b, ++b );
                lineMarkup.add( m ); // Now what follows, if anything?

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
                    lineMarkup.add( m );

                  // 3. Comment appender, if any
                  // ───────────────────
                    if( endsWithAppender ) {
                        final CommentAppender_ appender = spooler.commentAppender.unwind();
                        appender.text.delimit( b, b = compose( appender ));
                        lineMarkup.add( appender ); }}
                else {
                    line.substance = null;
                    assert !endsWithAppender; } // Impossible without substance at least of plain space.

              // This line as a whole
              // ─────────
                line.text.delimit( bLine, b );
                blindMarkup.add( line );

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
            parentMarkup.add( blind );
            return b; }}



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
            for( int end, eN = fractumLineEnds.length;     // For each line, if its end boundary
              e < eN && (end = endsArray[e]) <= position; // sits at or before the position,
              ++e, ++n, s = end );                       // then advance to the next line.
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
