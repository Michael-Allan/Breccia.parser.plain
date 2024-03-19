package Breccia.parser.plain;

import Breccia.parser.*;
import java.util.List;


final class AssociativeReference_ extends CommandPoint_<BrecciaCursor> implements AssociativeReference {


    AssociativeReference_( BrecciaCursor cursor ) { super( cursor ); }



    AssociativeReference_ endSet() {
        end = new End_();
        return this; }



    ReferentClause referentClause;



    final ReferentClause_ referentClauseWhenPresent = new ReferentClause_();



    final FlatGranum referentialCommand = FlatGranum.make( cursor, "ReferentialCommand" );



    ReferrerClause referrerClause;



    final ReferrerClause_ referrerClauseWhenPresent = new ReferrerClause_();



   // ━━━  A s s o c i a t i v e   R e f e r e n c e  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    public @Override ReferentClause referentClause() { return referentClause; }



    public @Override Granum referentialCommand() { return referentialCommand; }



    public @Override ReferrerClause referrerClause() { return referrerClause; }



   // ━━━  F r a c t u m _  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    protected @Override void commit() {
        super.commit();
        cursor.associativeReference( this ); }



   // ━━━  P o i n t _  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    final @Override void compose() throws MalformedText { cursor.composeAssociativeReference(); }



   // ▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀


    final class End_ extends CommandPoint_<BrecciaCursor>.End_ implements AssociativeReference.End {


        protected @Override void commit() {
            super.commit();
            cursor.associativeReferenceEnd( this ); }}



   // ▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀


    final class InferentialFractumLocant_ extends Granum_ implements InferentialFractumLocant {


        InferentialFractumLocant_() { super( AssociativeReference_.this.cursor ); }



        final GranalArrayList components = new GranalArrayList( cursor.spooler );



        final FlatGranum contextOperator = FlatGranum.make( cursor, "ContextOperator" );



        final FractumLocant_ fractumLocant = new FractumLocant_( cursor );



       // ━━━  G r a n u m  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


        public @Override @AdjunctSlow int column() { return cursor.bufferColumn( text.start() ); }



        public @Override List<Granum> components() {
            assert components.isFlush();
            return components; }



        public @Override @AdjunctSlow int lineNumber() {
            return cursor.bufferLineNumber( text.start() ); }



       // ━━━  I n f e r e n t i a l   R e f e r e n t   I n d i c a n t  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


        public @Override FractumLocant fractumLocant() { return fractumLocant; }}



   // ▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀


    final class ReferentClause_ extends Granum_ implements ReferentClause {


        ReferentClause_() {
            super( AssociativeReference_.this.cursor );
            componentsAsFractumLocant = List.of( fractumLocantWhenPresent );
            componentsAsInferentialFractumLocant = List.of( inferentialFractumLocantWhenPresent ); }



        List<Granum> components;



        final List<Granum> componentsAsFractumLocant;



        final List<Granum> componentsAsInferentialFractumLocant;



        FractumLocant fractumLocant;



        final FractumLocant_ fractumLocantWhenPresent = new FractumLocant_( cursor );



        InferentialFractumLocant inferentialFractumLocant;



        final InferentialFractumLocant_ inferentialFractumLocantWhenPresent
          = new InferentialFractumLocant_();



       // ━━━  G r a n u m  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


        public @Override @AdjunctSlow int column() { return cursor.bufferColumn( text.start() ); }



        public @Override List<Granum> components() { return components; }



        public @Override @AdjunctSlow int lineNumber() {
            return cursor.bufferLineNumber( text.start() ); }



       // ━━━  R e f e r e n t   C l a u s e  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


        public @Override FractumLocant fractumLocant() { return fractumLocant; }



        public @Override InferentialFractumLocant inferentialFractumLocant() {
            return inferentialFractumLocant; }}



   // ▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀


    final class ReferrerClause_ extends Granum_ implements ReferrerClause {


        ReferrerClause_() { super( AssociativeReference_.this.cursor ); }



        final CoalescentGranalList components = new GranalArrayList( cursor.spooler );



        final PatternMatcher_ patternMatcher = new PatternMatcher_( cursor );



       // ━━━  G r a n u m  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


        public @Override @AdjunctSlow int column() { return command.column(); }
          // Always the referrer clause leads the command.



        public @Override List<Granum> components() {
            assert components.isFlush();
            return components; }



        public @Override @AdjunctSlow int lineNumber() { return command.lineNumber(); }
          // Always the referrer clause leads the command.



       // ━━━  R e f e r r e r   C l a u s e  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


        public @Override PatternMatcher patternMatcher() { return patternMatcher; }}}



                                                   // Copyright © 2021-2024  Michael Allan.  Licence MIT.
