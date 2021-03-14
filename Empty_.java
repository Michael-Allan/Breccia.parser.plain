package Breccia.parser.plain;

import Breccia.parser.Empty;


public class Empty_ implements Empty {


    protected Empty_( BrecciaCursor cursor ) { this.cursor = cursor; }



    public void commit() { cursor.empty( this ); }



////  P r i v a t e  ////////////////////////////////////////////////////////////////////////////////////


    protected final BrecciaCursor cursor; }



                                                        // Copyright © 2021  Michael Allan.  Licence MIT.
