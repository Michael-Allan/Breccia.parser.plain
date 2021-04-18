package Breccia.parser.plain;

import Breccia.parser.*;
import java.util.List;


final class AssociativeReference_ extends CommandPoint_<BrecciaCursor> implements AssociativeReference {


    AssociativeReference_( BrecciaCursor cursor ) {
        super( cursor );
        initialize();
        referentialCommand = FlatMarkup.make( cursor, "ReferentialCommand" ); }



    final Descriptor descriptor = new Descriptor();



    AssociativeReference_ endSet() {
        end = new End_();
        return this; }



    /** Late composition control flag.  Cleared on committing this associative reference through
      * its `commit` method.  Set on late composition, which is triggered either by calling
      * on the descriptor `components` or the reifying `asAssociativeReference` getter.
      *
      *     @see #commit()
      *     @see Descriptor#components()
      *     @see BrecciaCursor#asAssociativeReference()
      */
    boolean isComposed; /* Justification of late parsing and composition,
      viz. beyond what was parsed at commit time: Use cases exist which care nothing
      for associative references but their reification, and these may benefit from the time
      saved by leaving unparsed those components which contribute nothing to that reification. */



    ReferentClause referentClause;



    final ReferentClause_ referentClauseWhenPresent = new ReferentClause_();



    final FlatMarkup referentialCommand;



    ReferrerClause referrerClause;



    final ReferrerClause_ referrerClauseWhenPresent = new ReferrerClause_();



   // ━━━  A s s o c i a t i v e   R e f e r e n c e  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    public @Override ReferentClause referentClause() { return referentClause; }



    public @Override Markup referentialCommand() { return referentialCommand; }



    public @Override ReferrerClause referrerClause() { return referrerClause; }



   // ━━━  F r a c t u m _  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    protected @Override void commit() {
        super.commit();
        isComposed = false; // Pending demand.
        cursor.associativeReference( this ); }



   // ━━━  P o i n t  ━━━  C o m m a n d   P o i n t _  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    public final @Override Descriptor descriptor() { return descriptor; }



   // ▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀


    final class ContainmentClause extends Markup_ {


        ContainmentClause() { super( cursor.buffer ); }



        final CoalescentMarkupList components = new CoalescentArrayList( cursor.spooler );



       // ━━━  M a r k u p  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


        public @Override final int column() { return cursor.bufferColumn( text.start() ); }



        public @Override List<Markup> components() {
            assert components.isFlush();
            return components; }



        public @Override final int lineNumber() { return cursor.bufferLineNumber( text.start() ); }



        public @Override final String tagName() { return "ContainmentClause"; }}



   // ▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀


    final class Descriptor extends Point_<BrecciaCursor>.Descriptor {


       // ━━━  M a r k u p  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


        public final @Override List<Markup> components() throws MalformedMarkup {
            if( !isComposed ) {
                cursor.composeAssociativeReference();
                isComposed = true; }
            assert components.isFlush();
            return components; }}



   // ▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀


    final class End_ extends CommandPoint_<BrecciaCursor>.End_ implements AssociativeReference.End {


        protected @Override void commit() {
            super.commit();
            cursor.associativeReferenceEnd( this ); }}



   // ▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀


    final class InferentialReferentIndicant_ extends Markup_ implements InferentialReferentIndicant {


        InferentialReferentIndicant_() { super( cursor.buffer ); }



        final CoalescentArrayList components = new CoalescentArrayList( cursor.spooler );



        final ContainmentClause containmentClauseWhenPresent = new ContainmentClause();



        FractumIndicant fractumIndicant;



        final FractumIndicant_ fractumIndicantWhenPresent = new FractumIndicant_( cursor );



        Markup referentialForm;



        final FlatMarkup referentialFormWhenPresent = FlatMarkup.make( cursor, "ReferentialForm" );



        Markup referrerSimilarity;



        final FlatMarkup referrerSimilarityWhenPresent = FlatMarkup.make( cursor, "ReferrerSimilarity" );



       // ━━━  I n f e r e n t i a l   R e f e r e n t   I n d i c a n t  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


        public @Override FractumIndicant fractumIndicant() { return fractumIndicant; }



        public @Override Markup referentialForm() { return referentialForm; }



        public @Override Markup referrerSimilarity() { return referrerSimilarity; }



       // ━━━  M a r k u p  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


        public @Override final int column() { return cursor.bufferColumn( text.start() ); }



        public @Override List<Markup> components() {
            assert components.isFlush();
            return components; }



        public @Override final int lineNumber() { return cursor.bufferLineNumber( text.start() ); }



        public @Override final String tagName() { return "InferentialReferentIndicant"; }}



   // ▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀


    final class ReferentClause_ extends Markup_ implements ReferentClause {


        ReferentClause_() {
            super( cursor.buffer );
            componentAsFractumIndicant = List.of( fractumIndicantWhenPresent );
            componentAsInferentialReferentIndicant = List.of( inferentialReferentIndicantWhenPresent ); }



        List<Markup> components;



        final List<Markup> componentAsFractumIndicant;



        final List<Markup> componentAsInferentialReferentIndicant;



        FractumIndicant fractumIndicant;



        final FractumIndicant_ fractumIndicantWhenPresent = new FractumIndicant_( cursor );



        InferentialReferentIndicant inferentialReferentIndicant;



        final InferentialReferentIndicant_ inferentialReferentIndicantWhenPresent
          = new InferentialReferentIndicant_();



       // ━━━  R e f e r e n t   C l a u s e  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


        public @Override FractumIndicant fractumIndicant() { return fractumIndicant; }



        public @Override InferentialReferentIndicant inferentialReferentIndicant() {
            return inferentialReferentIndicant; }



       // ━━━  M a r k u p  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


        public @Override final int column() { return cursor.bufferColumn( text.start() ); }



        public @Override List<Markup> components() { return components; }



        public @Override final int lineNumber() { return cursor.bufferLineNumber( text.start() ); }



        public @Override final String tagName() { return "ReferentClause"; }}



   // ▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀


    final class ReferrerClause_ extends Markup_ implements ReferrerClause {


        ReferrerClause_() { super( cursor.buffer ); }



        final CoalescentMarkupList components = new CoalescentArrayList( cursor.spooler );



        final Pattern pattern = new Pattern( cursor );



       // ━━━  R e f e r r e r   C l a u s e  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


        public @Override Markup pattern() { return pattern; }



       // ━━━  M a r k u p  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


        public @Override final int column() {
          return bullet.column() + cursor.bufferColumnarSpan(bullet.text.start(),keyword.start()); }



        public @Override List<Markup> components() {
            assert components.isFlush();
            return components; }



        public @Override final int lineNumber() { return AssociativeReference_.this.lineNumber(); }
          // Always the referrer clause begins on the first line of the associative reference.



        public @Override final String tagName() { return "ReferrerClause"; }}}



                                                        // Copyright © 2021  Michael Allan.  Licence MIT.
