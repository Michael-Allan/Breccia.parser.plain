package Breccia.parser.plain;

import Breccia.parser.FileFractum;
import Breccia.parser.Markup;
import java.util.ArrayList;
import java.util.List;


final class FileFractum_ extends Fractum_<BrecciaCursor> implements FileFractum {


    FileFractum_( BrecciaCursor cursor ) {
        super( cursor );
        componentsWhenPresent = List.of( descriptorWhenPresent ); }



    List<Markup> components;



    static final List<Markup> componentsWhenAbsent = List.of();



    final List<Markup> componentsWhenPresent;



    FileDescriptor_ descriptor;



    final FileDescriptor_ descriptorWhenPresent = new FileDescriptor_();



    FileFractum_ endSet() {
        end = new End_();
        return this; }



    /** Cleared before committing by `readyFileFractum`,
      * set on user demand of `asFileFractum` or `components`.
      *
      *     @see BrecciaCursor#readyFileFractum()
      *     @see BrecciaCursor#asFileFractum()
      *     @see #components()
      */
    boolean isCompositionParsed;



   // ━━━  F i l e   F r a c t u m  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    public final @Override FileDescriptor descriptor() { return descriptor; }



   // ━━━  F r a c t u m _  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    @Override void commit() {
        super.commit();
        cursor.fileFractum( this ); }



   // ━━━  M a r k u p  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


    public final @Override List<Markup> components() {
        if( !isCompositionParsed ) {
            cursor.parseFileFractum();
            assert isCompositionParsed; }
        return components; }



   // ▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀


    final class End_ extends Fractum_<BrecciaCursor>.End_ implements FileFractum.End {


        @Override void commit() {
            super.commit();
            cursor.fileFractumEnd( this ); }}



   // ▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀


    final class FileDescriptor_ extends Markup_ implements FileDescriptor {


        FileDescriptor_() { super( FileFractum_.this.text ); }



        final List<Markup> components = new ArrayList<>();



        /** Cleared by `parseFileFractum`, set on user demand of `components`.
          *
          *     @see BrecciaCursor#parseFileFractum()
          *     @see #components()
          */
        boolean isCompositionParsed;



       // ━━━  M a r k u p  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


        public @Override int column() { return FileFractum_.this.column(); }



        public final @Override List<Markup> components() throws MalformedMarkup {
            if( !isCompositionParsed ) {
                cursor.parseFileDescriptor();
                assert isCompositionParsed; }
            return components; }



        public @Override int lineNumber() { return FileFractum_.this.lineNumber(); }}}



                                                        // Copyright © 2021  Michael Allan.  Licence MIT.
