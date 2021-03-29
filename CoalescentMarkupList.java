package Breccia.parser.plain;

import Breccia.parser.Markup;
import java.util.List;


/** A list of markup components augmented to support coalescence.  It enables generic markup that would
  * otherwise be listed as separate components, yet is all of the same type and lies contiguous
  * in the cursor buffer, to be coalesced into a single component.
  *
  * <p>Warning: if an element is removed from the list after coalescence has begun and before the next
  * {@linkplain #clear() clearance call}, then the effect of further coalescence is undefined.
  * In other words, coalescence-removal-coalescence with no intermediate clearance
  * is an invalid order of operations.</p>
  */
interface CoalescentMarkupList extends List<Markup> {


    /** Appends to this list the flat markup bounded in the cursor by buffer positions `start` and `end`.
      * By default the markup is appended as a distinct component with a generic tag name of ‘Markup’.
      * If already the list ends with such a component, however, then effectively the markup is appended
      * by scheduled coalescence with that component on the next flush.
      *
      * <p>Warning: be sure to call `flush` before attempting to read the appended markup.</p>
      *
      *     @see Breccia.parser.Markup#tagName()
      *     @see #flush()
      *     @throws AssertionError If assertions are enabled and `start` is not less than `end`.
      *     @throws AssertionError If coalescence is attempted and `start` is unequal to the `end`
      *       given in the previous call to this method.  Cf. the contiguity stipulated
      *       by `Markup.{@linkplain Breccia.parser.Markup#components() components}`.
      */
    public void appendFlat( int start, int end );



    /** Completes any pending coalescence.
      */
    public void flush();



    /** Tells whether this list is ready for end use, any coalescence that had been pending
      * now being complete.
      */
    public boolean isFlush();



   // ━━━  C o l l e c t i o n  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    /** Resets all coalescent state. {@inheritDoc}
      */
    public @Override void clear(); }



                                                        // Copyright © 2021  Michael Allan.  Licence MIT.
