package Breccia.parser.plain;

import Breccia.parser.Markup;
import java.util.List;


/** Anonymous composite markup; markup that has parsed components but no name
  * in the Breccia language definition.
  *
  *     @param <L> The type of component list.
  *     @see NamelessFlatMarkup *//*
  *
  * No `DeepMarkup` is defined for named composites.  Rather than pass type parameters and constructor
  * parameters up through the class hierarchy, each composite declares the `components` method and field
  * at whatever depth in the hierarchy a suitable type for it becomes known.
  */
public abstract class NamelessDeepMarkup<L extends List<Markup>> extends Markup_ {


    protected NamelessDeepMarkup( Fractum_ f, L components ) {
        super( f );
        this.components = components; }



   // ━━━  M a r k u p  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    public final @Override List<Markup> components() { return components; }



    /** Returns ‘Markup’.
      */
    public final @Override String tagName() { return "Markup"; } /* `Markup_` could not declare this,
      e.g. as a default implementation, without inadvertently overriding the default implementations
      of the `Breccia.parser` interfaces. */



////  P r i v a t e  ////////////////////////////////////////////////////////////////////////////////////


    final L components; }


                                                        // Copyright © 2021  Michael Allan.  Licence MIT.
