package Breccia.parser.plain;

import Breccia.parser.AssociativeReference;
import Breccia.parser.Markup;
import java.util.List;


final class AssociativeReference_ extends CommandPoint_<BrecciaCursor> implements AssociativeReference {


    AssociativeReference_( BrecciaCursor cursor ) {
        super( cursor );
        initialize(); }



    final Descriptor descriptor = new Descriptor() {

        public @Override List<Markup> components() { return components; }};



    AssociativeReference_ endSet() {
        end = new End_();
        return this; }



    public ImperativeClause imperativeClause() { throw new UnsupportedOperationException(); }



   // ━━━  F r a c t u m _  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    protected @Override void commit() {
        super.commit();
        cursor.associativeReference( this ); }



   // ━━━  P o i n t  ━━━  C o m m a n d   P o i n t _  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    public final @Override Descriptor descriptor() { return descriptor; }



   // ▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀


    final class End_ extends CommandPoint_<BrecciaCursor>.End_ implements AssociativeReference.End {


        protected @Override void commit() {
            super.commit();
            cursor.associativeReferenceEnd( this ); }}}



                                                        // Copyright © 2021  Michael Allan.  Licence MIT.
