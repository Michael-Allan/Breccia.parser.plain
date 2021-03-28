package Breccia.parser.plain;

import Breccia.parser.Markup;
import java.util.List;


/** A list of markup components augmented to support coalescence of flat markup at its foot.
  *
  * <p>Warning: if an element is removed from the list after coalescence has begun and before the next
  * {@linkplain #clear() clearance call}, then the effect of further coalescence is undefined.
  * Coalescence-removal-coalescence with no intermediate clearance is an invalid order of operations.</p>
  */
interface CoalescentMarkupList extends List<Markup> {


    /** If the list size is unchanged since flat, coalescent markup
      * was {@linkplain #flatFoot(FlatMarkup) last appended}, then this method returns
      * the appended instance, ready for coalescing.  Otherwise it returns null.
      */
    public FlatMarkup flatFoot();



    /** Appends an instance of flat, coalescent markup to this list.
      */
    public void flatFoot( FlatMarkup f );



   // ━━━  C o l l e c t i o n  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    /** Resets all coalescent state. {@inheritDoc}
      */
    public @Override void clear(); }



                                                        // Copyright © 2021  Michael Allan.  Licence MIT.
