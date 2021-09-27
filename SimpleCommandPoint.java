package Breccia.parser.plain;


/** A command point wherein the command comprises a single term.
  *
  *     @param <C> The type of cursor.
  */
public abstract class SimpleCommandPoint<C extends BrecciaCursor> extends CommandPoint_<C> {


    /** @param command Any instance of flat markup with a tag name of ‘Command’.
      * @see FlatMarkup#make(BrecciaCursor,String) *//*
      *
      *   Taking the command pre-constructed allows passing it cleanly via the parent constructor.
      *   The alternative of a local construction passed through an initializer would be less clean
      *   and would impose a burden on parent and sister classes improper to their functions.
      */
    protected SimpleCommandPoint( final C cursor, final FlatMarkup command ) {
      super( cursor, command.text );
      this.command = command; }



////  P r i v a t e  ////////////////////////////////////////////////////////////////////////////////////



    final FlatMarkup command;



   // ━━━  P o i n t _  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    final @Override void compose() throws MalformedMarkup {
        cursor.composeDescriptor( SimpleCommandPoint.this ); }}



                                                        // Copyright © 2021  Michael Allan.  Licence MIT.
