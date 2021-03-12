package Breccia.parser.plain;

import java.io.*;
import java.nio.file.Path;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.Files.newInputStream;


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
        final InputStream byteSource = newInputStream​( sourceFile );
        final InputStreamReader charSource = new InputStreamReader( byteSource, UTF_8 );
          // Any reasonable implementation of `InputStreamReader` is going to maintain an adequate
          // byte buffer for bulk transfer requests.  Further buffering of `byteSource` would likely
          // be counter-productive.  https://stackoverflow.com/a/27347262/2402790
        return charSource; }}



                                                        // Copyright © 2021  Michael Allan.  Licence MIT.
