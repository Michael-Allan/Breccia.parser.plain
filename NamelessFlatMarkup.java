package Breccia.parser.plain;


/** Anonymous non-composite markup; markup that has no parsed components and goes unnamed
  * in the Breccia language definition.
  *
  *     @see NamelessDeepMarkup
  */
abstract class NamelessFlatMarkup extends FlatMarkup {


    NamelessFlatMarkup( Fractum_ f ) { super( f ); }



   // ━━━  M a r k u p  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    /** Returns ‘Markup’.
      */
    public final @Override String tagName() { return "Markup"; }} /* `Markup_` could not declare this,
      e.g. as a default implementation, without inadvertently overriding the default implementations
      of the `Breccia.parser` interfaces. */



                                                        // Copyright © 2021  Michael Allan.  Licence MIT.
