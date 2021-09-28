package Breccia.parser.plain;

import Breccia.parser.*;
import java.util.List;


final class AssociativeReference_ extends CommandPoint_<BrecciaCursor> implements AssociativeReference {


    AssociativeReference_( BrecciaCursor cursor ) {
        super( cursor );
        referentialCommand = FlatMarkup.make( cursor, "ReferentialCommand" ); }



    AssociativeReference_ endSet() {
        end = new End_();
        return this; }



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
        cursor.associativeReference( this ); }



   // ━━━  P o i n t _  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    final @Override void compose() throws MalformedMarkup { cursor.composeAssociativeReference(); }



   // ▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀


    final class ContainmentClause extends Markup_ {


        ContainmentClause() { super( cursor.buffer ); }



        final CoalescentMarkupList components = new CoalescentArrayList( cursor.spooler );



       // ━━━  M a r k u p  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


        public final @Override int column() { return cursor.bufferColumn( text.start() ); }



        public @Override List<Markup> components() {
            assert components.isFlush();
            return components; }



        public final @Override int lineNumber() { return cursor.bufferLineNumber( text.start() ); }



        public final @Override String tagName() { return "ContainmentClause"; }}



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


        public final @Override int column() { return cursor.bufferColumn( text.start() ); }



        public @Override List<Markup> components() {
            assert components.isFlush();
            return components; }



        public final @Override int lineNumber() { return cursor.bufferLineNumber( text.start() ); }}



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


        public final @Override int column() { return cursor.bufferColumn( text.start() ); }



        public @Override List<Markup> components() { return components; }



        public final @Override int lineNumber() { return cursor.bufferLineNumber( text.start() ); }}



   // ▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀


    final class ReferrerClause_ extends Markup_ implements ReferrerClause {


        ReferrerClause_() { super( cursor.buffer ); }



        final CoalescentMarkupList components = new CoalescentArrayList( cursor.spooler );



        final Pattern pattern = new Pattern( cursor );



       // ━━━  R e f e r r e r   C l a u s e  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


        public @Override Markup pattern() { return pattern; }



       // ━━━  M a r k u p  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


        public final @Override int column() {
          return bullet.column() + cursor.bufferColumnarSpan(bullet.text.start(),keyword.start()); }



        public @Override List<Markup> components() {
            assert components.isFlush();
            return components; }



        public @Override int lineNumber() { return AssociativeReference_.this.lineNumber(); }}}
          // Always the referrer clause begins on the first line of the associative reference.



                                                        // Copyright © 2021  Michael Allan.  Licence MIT.
