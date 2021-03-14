package Breccia.parser.plain;

import Breccia.parser.Halt;


public class Halt_ implements Halt {


    protected Halt_( BrecciaCursor cursor ) { this.cursor = cursor; }



    public void commit() { cursor.halt( this ); }



////  P r i v a t e  ////////////////////////////////////////////////////////////////////////////////////


    protected final BrecciaCursor cursor; }



                                                        // Copyright © 2021  Michael Allan.  Licence MIT.
