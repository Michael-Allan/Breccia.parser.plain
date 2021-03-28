package Breccia.parser.plain;

import Breccia.parser.Markup;
import java.util.ArrayList;


final class CoalescentArrayList extends ArrayList<Markup> implements CoalescentMarkupList {


   // ━━━  C o a l e s c e n t   M a r k u p   L i s t  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    public @Override FlatMarkup flatFoot() { return size() == flatFootSize? flatFoot: null; }



    public @Override void flatFoot( final FlatMarkup f ) {
        add( f );
        flatFoot = f;
        flatFootSize = size(); }



   // ━━━  C o l l e c t i o n  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    public @Override void clear() {
        super.clear();
        flatFoot = null; }



////  P r i v a t e  ////////////////////////////////////////////////////////////////////////////////////


    private FlatMarkup flatFoot;



    private int flatFootSize; }


                                                        // Copyright © 2021  Michael Allan.  Licence MIT.
