package Breccia.parser.plain;

import Breccia.parser.AssociativeReference;
import Breccia.parser.Markup;
import java.util.List;


final class AssociativeReference_ extends CommandPoint_<BrecciaCursor> implements AssociativeReference {


    AssociativeReference_( BrecciaCursor cursor ) {
        super( cursor );
        initialize(); }



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



    public ReferentClause referentClause() { throw new UnsupportedOperationException(); }



    public Markup referentialCommand() { throw new UnsupportedOperationException(); }



    public ReferrerClause referrerClause() { throw new UnsupportedOperationException(); }



   // ━━━  F r a c t u m _  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    protected @Override void commit() {
        super.commit();
        isComposed = false; // Pending demand.
        cursor.associativeReference( this ); }



   // ━━━  P o i n t  ━━━  C o m m a n d   P o i n t _  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    public final @Override Descriptor descriptor() { return descriptor; }



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
            cursor.associativeReferenceEnd( this ); }}}



                                                        // Copyright © 2021  Michael Allan.  Licence MIT.
