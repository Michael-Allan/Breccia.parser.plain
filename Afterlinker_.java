package Breccia.parser.plain;

import Breccia.parser.*;
import java.util.List;


final class Afterlinker_ extends CommandPoint_<BrecciaCursor> implements Afterlinker {


    Afterlinker_( BrecciaCursor cursor ) { super( cursor ); }



    Afterlinker_ endSet() {
        end = new End_();
        return this; }



    ObjectClause objectClause;



    final ObjectClause_ objectClauseWhenPresent = new ObjectClause_();



    final FlatGranum referentialCommand = FlatGranum.make( cursor, "ReferentialCommand" );



    SubjectClause subjectClause;



    final SubjectClause_ subjectClauseWhenPresent = new SubjectClause_();



   // ━━━  A s s o c i a t i v e   R e f e r e n c e  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    public @Override ObjectClause objectClause() { return objectClause; }



    public @Override Granum referentialCommand() { return referentialCommand; }



    public @Override SubjectClause subjectClause() { return subjectClause; }



   // ━━━  F r a c t u m _  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    protected @Override void commit() {
        super.commit();
        cursor.afterlinker( this ); }



   // ━━━  P o i n t _  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    final @Override void compose() throws MalformedText { cursor.composeAfterlinker(); }



   // ▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀


    final class End_ extends CommandPoint_<BrecciaCursor>.End_ implements Afterlinker.End {


        protected @Override void commit() {
            super.commit();
            cursor.afterlinkerEnd( this ); }}



   // ▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀


    final class FractalContextLocant_ extends Granum_ implements FractalContextLocant {


        FractalContextLocant_() { super( Afterlinker_.this.cursor ); }



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


    final class ObjectClause_ extends Granum_ implements ObjectClause {


        ObjectClause_() {
            super( Afterlinker_.this.cursor );
            componentsAsFractalContextLocant = List.of( fractalContextLocantWhenPresent );
            componentsAsFractumLocant = List.of( fractumLocantWhenPresent ); }



        List<Granum> components;



        final List<Granum> componentsAsFractalContextLocant;



        final List<Granum> componentsAsFractumLocant;



        FractalContextLocant fractalContextLocant;



        final FractalContextLocant_ fractalContextLocantWhenPresent = new FractalContextLocant_();



        FractumLocant fractumLocant;



        final FractumLocant_ fractumLocantWhenPresent = new FractumLocant_( cursor );



       // ━━━  G r a n u m  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


        public @Override @AdjunctSlow int column() { return cursor.bufferColumn( text.start() ); }



        public @Override List<Granum> components() { return components; }



        public @Override @AdjunctSlow int lineNumber() {
            return cursor.bufferLineNumber( text.start() ); }



       // ━━━  R e f e r e n t   C l a u s e  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


        public @Override FractumLocant fractumLocant() { return fractumLocant; }



        public @Override FractalContextLocant fractalContextLocant() { return fractalContextLocant; }}



   // ▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀


    final class SubjectClause_ extends Granum_ implements SubjectClause {


        SubjectClause_() { super( Afterlinker_.this.cursor ); }



        final CoalescentGranalList components = new GranalArrayList( cursor.spooler );



        final PatternMatcher_ patternMatcher = new PatternMatcher_( cursor );



       // ━━━  G r a n u m  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


        public @Override @AdjunctSlow int column() { return command.column(); }
          // Always the subject clause leads the command.



        public @Override List<Granum> components() {
            assert components.isFlush();
            return components; }



        public @Override @AdjunctSlow int lineNumber() { return command.lineNumber(); }
          // Always the subject clause leads the command.



       // ━━━  R e f e r r e r   C l a u s e  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


        public @Override PatternMatcher patternMatcher() { return patternMatcher; }}}



                                                   // Copyright © 2021-2024  Michael Allan.  Licence MIT.
