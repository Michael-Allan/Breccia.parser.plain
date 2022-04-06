package Breccia.parser.plain;

import java.io.*;
import java.nio.file.Path;


/** The present project.  Included are one or two miscellaneous resources,
  * residual odds and ends that properly fit nowhere else.
  */
public final class Project {


    private Project() {}



    /** Opens a Breccian source file for reading, returning a reader suited to the purpose.
      */
    public static Reader newSourceReader( final Path sourceFile ) throws IOException { /* Little point
           in dealing with the `IOException` at this level, because anyway the caller must deal with it
           on closing the reader, usually by appending a `catch` to a try-with-resources block. */
        return java.nio.file.Files.newBufferedReader​( sourceFile ); }}



                                                   // Copyright © 2021-2022  Michael Allan.  Licence MIT.
