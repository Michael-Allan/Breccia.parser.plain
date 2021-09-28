package Breccia.parser.plain;


/** A command point wherein the command comprises a single term.
  *
  *     @param <C> The type of cursor.
  */
public abstract class SimpleCommandPoint<C extends BrecciaCursor> extends CommandPoint_<C> {


    protected SimpleCommandPoint( final C cursor ) { super( cursor, /*hasSimpleCommand*/true ); }



////  P r i v a t e  ////////////////////////////////////////////////////////////////////////////////////


   // ━━━  P o i n t _  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    final @Override void compose() throws MalformedMarkup {
        cursor.composeDescriptor( SimpleCommandPoint.this ); }}



                                                        // Copyright © 2021  Michael Allan.  Licence MIT.
